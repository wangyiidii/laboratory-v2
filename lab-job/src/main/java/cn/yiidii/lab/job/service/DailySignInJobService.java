package cn.yiidii.lab.job.service;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.apiplatform.model.dto.DingDongSignInResponseDTO;
import cn.yiidii.apiplatform.model.dto.EverPhotoSignInResponseDTO;
import cn.yiidii.apiplatform.model.dto.TencentVideoSignInResponseDTO;
import cn.yiidii.apiplatform.service.SignInService;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.base.util.ContextUtil;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.lab.job.model.enums.DailySignInJobEnum;
import cn.yiidii.lab.job.model.exception.UserConfigNotFountException;
import cn.yiidii.lab.system.model.constant.SysUseConfigEnum;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.entity.SysUserConfig;
import cn.yiidii.lab.system.service.ISysUserConfigService;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.log.OmsLogger;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DailySignInService
 *
 * @author ed w
 * @date 2022/9/23 10:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DailySignInJobService {

    private final ISysUserService userService;
    private final ISysUserConfigService userConfigService;
    private final SignInService signInService;

    public String checkIn(DailySignInJobEnum jobEnum) {
        // 所有用户
        List<SysUser> allUser = userService.lambdaQuery()
                .select(SysUser::getId, SysUser::getUsername)
                .eq(SysUser::getStatus, Status.ENABLED)
                .list();

        // 汇总数据
        int success = 0, notConfigured = 0, fail = 0;
        // 执行签到
        for (SysUser u : allUser) {
            try {
                this.singleSignIn(u, jobEnum);
                success++;
            } catch (UserConfigNotFountException e) {
                notConfigured++;
            } catch (Exception e) {
                log.warn("[{}] e: {}", jobEnum.getDesc(), e.getMessage());
                fail++;
            }
        }

        return StrUtil.format("总: {}, 成功: {}, 失败: {}, 未配置: {}", allUser.size(), success, fail, notConfigured);
    }

    private void singleSignIn(SysUser u, DailySignInJobEnum jobEnum) {
        OmsLogger omsLogger = ContextUtil.get("omsLogger", OmsLogger.class);

        switch (jobEnum) {
            // 腾讯视频签到
            case TENCENT_VIDEO: {
                SysUserConfig config = userConfigService.getConfigByUserId(u.getId(), SysUseConfigEnum.SIGNIN_CK_TENCENT_VIDEO.getKey());
                if (Objects.isNull(config)) {
                    omsLogger.debug("[{}] {}未配置腾讯视频Cookie", jobEnum.getDesc(), u.getUsername());
                    throw new UserConfigNotFountException();
                }
                try {
                    TencentVideoSignInResponseDTO ret = signInService.tencentVideo(config.getValue());
                    omsLogger.info("[{}] {}签到成功，获取{}V力值", jobEnum.getDesc(), u.getUsername(), ret.getCheckinScore());
                } catch (Exception e) {
                    omsLogger.warn("[{}] {}签到异常: {}", jobEnum.getDesc(), u.getUsername(), e.getMessage());
                    throw e;
                }
                break;
            }

            // 叮咚买菜
            case DINGDONG: {
                SysUserConfig config = userConfigService.getConfigByUserId(u.getId(), SysUseConfigEnum.SIGNIN_CK_DINGDONG.getKey());
                if (Objects.isNull(config)) {
                    omsLogger.debug("[{}] {}未配置叮咚买菜Cookie", jobEnum.getDesc(), u.getUsername());
                    throw new UserConfigNotFountException();
                }
                try {
                    DingDongSignInResponseDTO ret = signInService.dingDong(config.getValue());
                    omsLogger.info("[{}] {}签到成功, 获取{}积分", jobEnum.getDesc(), u.getUsername(), ret.getData().getPointNum());
                } catch (Exception e) {
                    omsLogger.warn("[{}] {}签到异常: {}", jobEnum.getDesc(), u.getUsername(), e.getMessage());
                    throw e;
                }
                break;
            }

            // 时光相册
            case EVER_PHOTO: {
                SysUserConfig config = userConfigService.getConfigByUserId(u.getId(), SysUseConfigEnum.SIGNIN_CK_EVER_PHOTO.getKey());
                if (Objects.isNull(config)) {
                    omsLogger.debug("[{}] {}未配置时光相册Cookie", jobEnum.getDesc(), u.getUsername());
                    throw new UserConfigNotFountException();
                }
                try {
                    EverPhotoSignInResponseDTO ret = signInService.everPhoto(config.getValue());
                    omsLogger.info("[{}] {}签到成功，{}当前连续签到{}天，共获取{}MB空间", jobEnum.getDesc(), u.getUsername(),
                            ret.getData().getContinuity(), ret.getData().getTotalReward() / 1024 / 1024);
                } catch (Exception e) {
                    omsLogger.warn("[{}] {}签到异常: {}", jobEnum.getDesc(), u.getUsername(), e.getMessage());
                    throw e;
                }
                break;
            }

            // IIOS
            case IIOS: {
                SysUserConfig config = userConfigService.getConfigByUserId(u.getId(), SysUseConfigEnum.SIGNIN_CFG_IIOS.getKey());
                if (Objects.isNull(config)) {
                    omsLogger.debug("[{}] {}未配置iios", jobEnum.getDesc(), u.getUsername());
                    throw new UserConfigNotFountException();
                }
                try {
                    Map<String, String> iiosCfg = JsonUtils.parseObject(config.getValue(), Map.class);
                    R<String> iiosRet = signInService.iios(iiosCfg.get("email"), iiosCfg.get("password"));
                    omsLogger.info("[{}] {}签到成功，结果: {}", jobEnum.getDesc(), u.getUsername(), iiosRet.getMsg());
                } catch (Exception e) {
                    omsLogger.warn("[{}] {}签到异常: {}", jobEnum.getDesc(), u.getUsername(), e.getMessage());
                    throw e;
                }
                break;
            }

            default: {
                return;
            }
        }
    }

}
