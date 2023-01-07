package cn.yiidii.lab.apiplatform.controller;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.lab.apiplatform.model.body.LtDiffMonitBody;
import cn.yiidii.lab.apiplatform.model.dto.LtUsageDiffDTO;
import cn.yiidii.lab.apiplatform.service.LtService;
import cn.yiidii.base.annotation.ApiPostNotify;
import cn.yiidii.base.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 联通
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/lt")
@RequiredArgsConstructor
public class LtController {

    private final LtService ltService;

    @ApiPostNotify
    @ApiOperation("联通跳点监控")
    @PostMapping("/diff")
    public R<LtUsageDiffDTO> getDiff(@RequestBody @Validated LtDiffMonitBody body) {
        LtUsageDiffDTO diffDTO = ltService.getUsageInfoDiff(body.getCookie(), body.isReset());
        BigDecimal diff = diffDTO.getDiff();
        String msg;
        if (diff.compareTo(BigDecimal.ZERO) <= 0) {
            msg = "当前无跳点";
        } else {
            msg = StrUtil.format("发生跳点: {}M", diff.setScale(2));
        }
        return R.ok(diffDTO, msg);
    }

}
