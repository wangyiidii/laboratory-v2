package cn.yiidii.lab.system.model.dto;

import cn.yiidii.base.domain.entity.TreeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 路由配置信息
 *
 * @author Lion Li
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo extends TreeEntity<RouterVo, Long> {

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVo meta;


}
