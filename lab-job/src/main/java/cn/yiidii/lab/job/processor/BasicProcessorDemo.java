package cn.yiidii.lab.job.processor;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.apiplatform.model.dto.TencentVideoSignInResponseDTO;
import cn.yiidii.apiplatform.service.SignInService;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.constant.SysUseConfigEnum;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.entity.SysUserConfig;
import cn.yiidii.lab.system.service.ISysUserConfigService;
import cn.yiidii.lab.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BasicProcessorDemo
 *
 * @author ed w
 * @date 2022/9/21 15:46
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicProcessorDemo implements BasicProcessor {

    private final ISysUserService userService;
    private final ISysUserConfigService userConfigService;
    private final SignInService signInService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        // 在线日志功能，可以直接在控制台查看任务日志，非常便捷
        OmsLogger omsLogger = context.getOmsLogger();

        // 所有用户
        List<SysUser> allUser = userService.lambdaQuery()
                .select(SysUser::getId, SysUser::getUsername)
                .eq(SysUser::getStatus, Status.ENABLED)
                .list();

        AtomicLong success = new AtomicLong();
        AtomicLong fail = new AtomicLong();
        allUser.forEach(u -> {
            SysUserConfig config = userConfigService.getConfigByUserId(u.getId(), SysUseConfigEnum.CK_TENCENT_VIDEO.getKey());
            if (Objects.isNull(config)) {
                omsLogger.debug("[腾讯视频] {}未配置腾讯视频Cookie", u.getUsername());
                return;
            }
            try {
                TencentVideoSignInResponseDTO ret = signInService.tencentVideo(config.getValue());
                omsLogger.info("[腾讯视频] {}签到成功，获取{}V力值", u.getUsername(), ret.getCheckinScore());
                success.getAndIncrement();
            } catch (Exception e) {
                omsLogger.warn("[腾讯视频] {}签到异常: {}", u.getUsername(), e.getMessage());
                fail.getAndIncrement();
            }
        });

        return new ProcessResult(true, StrUtil.format("腾讯签到，成功: {}，失败：{}", success.get(), fail.get()));
    }

}