package cn.yiidii.web.controller.admin;

import cn.yiidii.lab.system.model.entity.DictDistrict;
import cn.yiidii.lab.system.service.IDictDistrictService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DictDistrictController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/dict/district")
@RequiredArgsConstructor
public class DictDistrictController {

    private final IDictDistrictService dictDistrictService;

    @GetMapping("/{id}")
    public R<?> getById(@NotNull(message = "缺少必要参数parent") @PathVariable Integer id) {
        return R.ok(dictDistrictService.getById(id));
    }


    @GetMapping("/p/{pid}")
    public R<?> getByPid(@NotNull(message = "缺少必要参数parent") @PathVariable Integer pid) {
        List<DictDistrict> districts = dictDistrictService.getByPid(pid);
        return R.ok(districts);
    }

    @GetMapping("/p/{pid}/{name}")
    public R<?> get(@NotNull(message = "缺少必要参数parent") @PathVariable Integer pid, @PathVariable String name) {
        return R.ok(dictDistrictService.get(pid, name));
    }


}
