package cn.yiidii.apiplatform.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.constant.ApiCacheKeyConst;
import cn.yiidii.apiplatform.model.dto.LtInfoResponseDTO;
import cn.yiidii.apiplatform.model.dto.LtUsageDiffDTO;
import cn.yiidii.apiplatform.model.dto.LtUsageInfoResponseDTO;
import cn.yiidii.apiplatform.model.enums.ApiExceptionCode;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.redis.RedisUtils;
import cn.yiidii.web.support.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 联通
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
public class LtService {

    /**
     * 获取联通账户基本信息
     *
     * @param cookie cookie
     * @return {@link LtInfoResponseDTO}
     */
    public LtInfoResponseDTO getInfo(String cookie) {
        HttpResponse response = HttpRequest.get("https://m.client.10010.com/servicequerybusiness/query/myInformation")
                .cookie(cookie)
                .execute();
        LtInfoResponseDTO infoDTO;
        try {
            infoDTO = JsonUtils.parseObject(response.body(), LtInfoResponseDTO.class);
        } catch (Exception e) {
            log.debug(StrUtil.format("获取联通账户基本信息异常, e: {}", e.getMessage()));
            throw new BizException(ApiExceptionCode.COOKIE_EXPIRED);
        }
        return infoDTO;
    }

    /**
     * 获取联通资源用量信息 [服务-查询-余量查询]
     *
     * @param cookie cookie
     * @return {@link LtUsageInfoResponseDTO}
     */
    public LtUsageInfoResponseDTO getUsageInfo(String cookie) {
        HttpResponse response = HttpRequest.post("https://m.client.10010.com/servicequerybusiness/operationservice/queryOcsPackageFlowLeftContentRevisedInJune")
                .cookie(cookie)
                .body("externalSources=&contactCode=&serviceType=&saleChannel=&channelCode=&duanlianjieabc=&ticket=&ticketPhone=&ticketChannel=&userNumber=&language=")
                .execute();
        String body = response.body();
        if (StrUtil.equals(body, "999998")) {
            throw new BizException(ApiExceptionCode.COOKIE_EXPIRED_LT);
        }

        return JsonUtils.parseObject(response.body(), LtUsageInfoResponseDTO.class);
    }

    /**
     * 跳点
     *
     * @param cookie cookie
     * @param reset  是否重置
     */
    public LtUsageDiffDTO getUsageInfoDiff(String cookie, boolean reset) {
        String phoneNumber = this.getPhoneNumberFormCookie(cookie);

        // 获取联通账户信息
        LtInfoResponseDTO.Data ltInfo;
        if (StrUtil.isBlank(phoneNumber)) {
            ltInfo = this.getInfo(cookie).getData();
            phoneNumber = ltInfo.getCurrentLoginNumber();
        }

        String ltInfoCacheKey = StrUtil.format(ApiCacheKeyConst.LT_INFO, phoneNumber);
        if (RedisUtils.hasKey(ltInfoCacheKey)) {
            ltInfo = RedisUtils.getCacheObject(ltInfoCacheKey);
        } else {
            ltInfo = this.getInfo(cookie).getData();
            RedisUtils.setCacheObject(ltInfoCacheKey, ltInfo);
        }


        // 用量信息
        LtUsageInfoResponseDTO lastUsageInfo;
        String usageInfoCacheKey = StrUtil.format(ApiCacheKeyConst.LT_USAGE_INFO, phoneNumber);
        if (!RedisUtils.hasKey(usageInfoCacheKey)) {
            RedisUtils.setCacheObject(usageInfoCacheKey, this.getUsageInfo(cookie));
        }

        lastUsageInfo = RedisUtils.getCacheObject(usageInfoCacheKey);
        LtUsageInfoResponseDTO currentUsageInfo = this.getUsageInfo(cookie);

        // 计算差值
        LtUsageDiffDTO diff = calcDiff(currentUsageInfo, lastUsageInfo);
        diff.setPkgName(ltInfo.getMyPackage().getProductName())
                .setPhoneNumber(phoneNumber)
                .setLastTime(DateUtil.parseLocalDateTime(lastUsageInfo.getTime()));

        if (reset) {
            RedisUtils.setCacheObject(usageInfoCacheKey, currentUsageInfo);
        }

        return diff;

    }

    /**
     * 从联通cookie里面手机号码
     *
     * @param cookie 联通cookie
     * @return 手机号码
     */
    private String getPhoneNumberFormCookie(String cookie) {
        Map<String, String> cookieMap = ServletUtil.getCookieMap(cookie);
        String cMobile = cookieMap.getOrDefault("c_mobile", "");
        String uAccount = cookieMap.getOrDefault("u_account", "");
        String custId = cookieMap.getOrDefault("custId", "");
        String unicomMallUid = cookieMap.getOrDefault("unicomMallUid", "");
        return StrUtil.firstNonBlank(cMobile, uAccount, custId, unicomMallUid);
    }

    /**
     * 计算跳点
     *
     * @param curr 当前
     * @param last 上次
     * @return {@link LtUsageDiffDTO}
     */
    private LtUsageDiffDTO calcDiff(LtUsageInfoResponseDTO curr, LtUsageInfoResponseDTO last) {
        List<String> directionalPackageResourceType = RedisUtils.getCacheList(ApiCacheKeyConst.LT_DIRECTIONAL_PACKAGE_RESOURCE_TYPE);

        // flow

        // curr
        BigDecimal currSum = curr.getSummary().getSum();
        BigDecimal currGeneric = BigDecimal.ZERO;
        BigDecimal currDirection = BigDecimal.ZERO;
        BigDecimal currFree = curr.getSummary().getFreeFlow();
        LtUsageInfoResponseDTO.Resource currFlow = curr.getResources().stream().
                filter(r -> r.getType().equals("flow"))
                .findFirst()
                .get();
        for (LtUsageInfoResponseDTO.Detail detail : currFlow.getDetails()) {
            String resourceType = detail.getResourceType();
            BigDecimal use = detail.getUse();
            if (directionalPackageResourceType.contains(resourceType)) {
                currDirection = currDirection.add(use);
            } else {
                currGeneric = currGeneric.add(use);
            }
        }

        // last
        BigDecimal lastGeneric = BigDecimal.ZERO;
        LtUsageInfoResponseDTO.Resource lastFlow = last.getResources().stream().
                filter(r -> r.getType().equals("flow"))
                .findFirst()
                .get();
        for (LtUsageInfoResponseDTO.Detail detail : lastFlow.getDetails()) {
            String resourceType = detail.getResourceType();
            if (!directionalPackageResourceType.contains(resourceType)) {
                lastGeneric = lastGeneric.add(detail.getUse());
            }
        }

        LtUsageDiffDTO diff = new LtUsageDiffDTO()
                .setSum(currSum)
                .setPkg(
                        new LtUsageDiffDTO.Pkg()
                                .setDirection(currDirection)
                                .setGeneric(currGeneric)
                )
                .setFree(currFree)
                .setDiff(currGeneric.subtract(lastGeneric));
        return diff;
    }

}
