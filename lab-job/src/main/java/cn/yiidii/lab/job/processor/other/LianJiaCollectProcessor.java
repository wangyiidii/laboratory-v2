package cn.yiidii.lab.job.processor.other;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.util.ContextUtil;
import cn.yiidii.lab.job.model.enums.DailySignInJobEnum;
import cn.yiidii.lab.job.service.DailySignInJobService;
import cn.yiidii.lab.other.service.ILianjiaHouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * LianJiaCollectProcessor
 *
 * @author ed w
 * @date 2022/9/21 15:46
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LianJiaCollectProcessor implements BasicProcessor {

    private final ILianjiaHouseService lianjiaHouseService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger omsLogger = context.getOmsLogger();
        try {
            omsLogger.error("LianJia采集开始");
            lianjiaHouseService.collect();
            omsLogger.error("LianJia采集完成");
            return new ProcessResult(true);
        } catch (Exception e) {
            omsLogger.error(StrUtil.format("LianJia采集发生异常: {}", e.getMessage()));
            return new ProcessResult(false);
        }
    }

}