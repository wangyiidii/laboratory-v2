package cn.yiidii.lab.apiplatform.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LtAccountInfo
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class LtInfoResponseDTO {

    private String code;
    private String desc;
    private Data data;

    @lombok.Data
    public static class Data {
        private NumberInfo numberInfo;
        private MyPackage myPackage;
        private String currentLoginNumber;
    }

    @lombok.Data
    public static class NumberInfo {
        @JsonAlias("opendate")
        private String openDate;
    }

    @lombok.Data
    @NoArgsConstructor
    public static class MyPackage {
        @JsonAlias("productname")
        private String productName;

        @JsonAlias("flowGn")
        private String flowGn;
    }
}