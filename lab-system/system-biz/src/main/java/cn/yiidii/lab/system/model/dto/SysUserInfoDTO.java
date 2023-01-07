package cn.yiidii.lab.system.model.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.yiidii.auth.support.LoginUser;
import cn.yiidii.base.annotation.Sensitive;
import cn.yiidii.base.domain.enums.SensitiveStrategy;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.base.util.ServletUtil;
import cn.yiidii.lab.system.model.enums.UserSource;
import cn.yiidii.web.constant.CommonConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class SysUserInfoDTO {
    public Long id;
    private String uuid;
    private UserSource source;
    private String username;
    private String nickname;
    private String avatar;
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;
    private Status status;
    private LocalDateTime createTime;
    private List<SysRoleInfoDTO> roles;
    private List<SysPermissionInfoDTO> permissions;

    public LoginUser toLoginUser() {
        UserAgent userAgent = ServletUtil.getUserAgent();
        String platformName = userAgent.getPlatform().getName();
        String osName = userAgent.getOs().getName();
        String os = StrUtil.equalsAnyIgnoreCase(CommonConstant.UNKNOWN_EN, platformName, osName) ?
                CommonConstant.UNKNOWN_EN : StrUtil.format("{} {}", platformName, osName);
        return LoginUser.builder()
                .userId(id)
                .username(username)
                .nickname(nickname)
                .token(null)
                .loginTime(LocalDateTime.now())
                .ipaddr(ServletUtil.getClientIP())
                .loginLocation(ServletUtil.getLocation())
                .browser(userAgent.getBrowser().getName())
                .os(os)
                .menus(new ArrayList<>())
                .roles(roles.stream().filter(Objects::nonNull).map(SysRoleInfoDTO::getCode).distinct().collect(Collectors.toList()))
                .permission(permissions.stream().filter(Objects::nonNull).map(SysPermissionInfoDTO::getCode).distinct().collect(Collectors.toList()))
                .build();
    }
}
