package cn.yiidii.apiplatform.support.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * BasicProcessorDemo
 *
 * @author ed w
 * @date 2022/9/21 15:46
 */
@Slf4j
@Component
public class BasicProcessorDemo implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        // 在线日志功能，可以直接在控制台查看任务日志，非常便捷
        OmsLogger omsLogger = context.getOmsLogger();
        omsLogger.info("BasicProcessorDemo start to process, current JobParams is {}.", context.getJobParams());

        log.info("BasicProcessorDemo process...");

        // 返回结果，该结果会被持久化到数据库，在前端页面直接查看，极为方便
        return new ProcessResult(true, "process success");
    }
}