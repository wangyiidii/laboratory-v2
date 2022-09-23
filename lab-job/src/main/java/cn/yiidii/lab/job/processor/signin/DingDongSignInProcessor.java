package cn.yiidii.lab.job.processor.signin;

import cn.yiidii.base.util.ContextUtil;
import cn.yiidii.lab.job.model.enums.DailySignInJobEnum;
import cn.yiidii.lab.job.service.DailySignInJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * DingDongSignInSignProcessor
 *
 * @author ed w
 * @date 2022/9/21 15:46
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DingDongSignInProcessor implements BasicProcessor {

    private final DailySignInJobService dailySignInJobService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        ContextUtil.set("omsLogger", context.getOmsLogger());

        String ret = dailySignInJobService.checkIn(DailySignInJobEnum.DINGDONG);

        return new ProcessResult(true, ret);
    }

}