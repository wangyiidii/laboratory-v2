package cn.yiidii.web.controller;

import cn.yiidii.web.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * index
 *
 * @author ed w
 * @since 1.0
 */
@RestController
public class IndexController {

    @GetMapping
    public R<?> index() {
        return R.ok(null, "请通过接口访问！");
    }
}
