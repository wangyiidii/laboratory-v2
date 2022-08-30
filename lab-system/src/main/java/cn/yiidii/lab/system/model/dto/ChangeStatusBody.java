package cn.yiidii.lab.system.model.dto;

import cn.yiidii.base.domain.enums.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 变更用户状态表单
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class ChangeStatusBody {

    @NotNull(message = "变更状态不能为空")
    private Status status;
}
