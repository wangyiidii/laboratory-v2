package cn.yiidii.apiplatform.support;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * ApplicationInit
 *
 * @author ed w
 * @date 2022/9/21 9:23
 */
@Slf4j
@Component
public class ApplicationInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // this.checkPython();
    }

    private void checkPython() {
        try {
            String version = RuntimeUtil.execForStr("python --version").replace("Python", "").trim();
            log.info("检测到已安装Python, 版本: {}", version);
        } catch (IORuntimeException e) {
            log.warn("检测到未安装Python");
            System.exit(-1);
            throw new RuntimeException("未安装python");
        }
    }
}
