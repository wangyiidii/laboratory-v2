package cn.yiidii.lab.system.model.constant;

/**
 * 系统常量
 *
 * @author ed w
 * @since 1.0
 */
public interface SysConst {

    String REGX_MAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    String REGX_CODE = "^[0-9a-zA-Z_]{4,}$";

    String ROLE_ADMIN_CODE = "ADMIN";
}
