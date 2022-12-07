package cn.yiidii.lab.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 省市区
 *
 * @author ed w
 * @since 1.0
 */
@TableName("dict_district")
@Data
public class DictDistrict {
    private Integer id;
    private Integer pid;
    private Integer deep;
    private String name;
    private String pinyinPrefix;
    private String pinyin;
    private Long extId;
    private String extName;
}
