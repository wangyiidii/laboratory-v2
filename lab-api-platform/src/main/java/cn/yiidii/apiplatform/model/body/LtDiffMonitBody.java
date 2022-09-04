package cn.yiidii.apiplatform.model.body;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * LtDIffMonitBody
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class LtDiffMonitBody {

    @NotNull(message = "cookie不能为空")
    private String cookie;
    private boolean reset = false;
}
