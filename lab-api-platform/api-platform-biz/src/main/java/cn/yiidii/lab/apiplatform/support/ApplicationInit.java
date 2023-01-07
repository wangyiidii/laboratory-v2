package cn.yiidii.lab.apiplatform.support;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.yiidii.base.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

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
        this.clearVar();
//        this.checkPython();

    }

    private void clearVar() {
        String var = System.getProperty("user.dir").concat(File.separator).concat("var");
        boolean del = FileUtil.del(var);
        if (del) {
            log.info("删除var文件");
        }
    }

    private void checkPython() {
        try {
            String version = ProcessUtil.execForStr("python --version")
                    .getResult().replace("Python", "").trim();
            log.info("检测到已安装Python, 版本: {}", version);
        } catch (IORuntimeException e) {
            log.warn("检测到未安装Python");
            System.exit(1);
            throw new RuntimeException("未安装python");
        }
    }
}
