package cn.yiidii.lab.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.yiidii.base.util.TreeUtil;
import cn.yiidii.lab.system.model.body.SysMenuSaveBody;
import cn.yiidii.lab.system.model.entity.SysMenu;
import cn.yiidii.lab.system.service.ISysMenuService;
import cn.yiidii.base.R;
import cn.yiidii.web.annotation.Add;
import cn.yiidii.web.annotation.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SysMenuController
 *
 * @author ed w
 * @since 1.0
 */
@SaCheckLogin
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @GetMapping("/list")
    public R<?> listMenu() {
        List<SysMenu> menuList = sysMenuService.list();
        return R.ok(menuList);
    }

    @GetMapping("/tree")
    public R<?> tree() {
        List<SysMenu> menuList = sysMenuService.list();
        return R.ok(TreeUtil.buildTree(menuList));
    }

    @GetMapping("/{id}")
    public R<?> getById(@PathVariable Long id) {
        return R.ok(sysMenuService.getById(id));
    }

    @PostMapping
    public R<?> add(@RequestBody @Validated(Add.class) SysMenuSaveBody sysMenuSaveBody) {
        SysMenu sysMenu = BeanUtil.toBean(sysMenuSaveBody, SysMenu.class);
        return R.ok(sysMenuService.save(sysMenu));
    }

    @PutMapping
    public R<?> update(@RequestBody @Validated(Update.class) SysMenuSaveBody sysMenuSaveBody) {
        SysMenu sysMenu = BeanUtil.toBean(sysMenuSaveBody, SysMenu.class);
        return R.ok(sysMenuService.updateById(sysMenu));
    }

}
