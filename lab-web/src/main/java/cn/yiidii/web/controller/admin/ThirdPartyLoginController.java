package cn.yiidii.web.controller.admin;

import cn.yiidii.lab.system.model.body.ThirdPartyLoginBody;
import cn.yiidii.lab.system.service.IThirdPartyLoginService;
import cn.yiidii.web.R;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第三方登录
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/third-party")
@RequiredArgsConstructor
public class ThirdPartyLoginController {
    private final AuthRequestFactory factory;
    private final IThirdPartyLoginService thirdPartyLoginService;

    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @PostMapping(value = "/{type}/callback")
    public R<?> login(@PathVariable String type, @RequestBody ThirdPartyLoginBody loginBody) {
        return R.ok(thirdPartyLoginService.login(type, loginBody));
    }
}
