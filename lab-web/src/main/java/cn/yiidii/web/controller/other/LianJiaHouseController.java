package cn.yiidii.web.controller.other;

import cn.yiidii.lab.other.model.vo.LianJiaHouseQueryParam;
import cn.yiidii.lab.other.service.ILianjiaHouseService;
import cn.yiidii.web.PageQuery;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * LianJiaHouseController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/lianjia")
@RequiredArgsConstructor
public class LianJiaHouseController {

    private final ILianjiaHouseService lianjiaHouseService;

    @GetMapping("/list")
    public R<?> listHouse(LianJiaHouseQueryParam queryParam, PageQuery pageQuery) {
        return R.ok(lianjiaHouseService.listHousePage(queryParam, pageQuery));
    }

    @GetMapping("/{houseCode}/list")
    public R<?> getListByHouseCode(@PathVariable String houseCode) {
        return R.ok(lianjiaHouseService.getListByHouseCode(houseCode));
    }

}
