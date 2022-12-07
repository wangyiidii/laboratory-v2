package cn.yiidii.lab.other.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.lab.other.mapper.LianJiaHouseMapper;
import cn.yiidii.lab.other.model.dto.LianJiaCollectConfigDTO;
import cn.yiidii.lab.other.model.entity.LianJiaHouse;
import cn.yiidii.lab.other.model.vo.LianJiaHouseQueryParam;
import cn.yiidii.lab.other.service.ILianjiaHouseService;
import cn.yiidii.lab.system.model.entity.DictDistrict;
import cn.yiidii.lab.system.service.IDictDistrictService;
import cn.yiidii.web.PageQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LianJiaServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LianJiaHouseHouseServiceImpl extends ServiceImpl<LianJiaHouseMapper, LianJiaHouse> implements ILianjiaHouseService {

    private final ConfigService configService;
    private final IDictDistrictService dictDistrictService;

    @Override
    public void collect() {
        List<LianJiaCollectConfigDTO> lianJiaCollectConfigDTOS = JsonUtils.parseArray(configService.get("lianjia_collect", "[]"), LianJiaCollectConfigDTO.class);
        if (CollUtil.isEmpty(lianJiaCollectConfigDTOS)) {
            return;
        }

        // 遍历采集每个配置
        lianJiaCollectConfigDTOS.forEach(this::doCollect);
    }

    @Override
    public Page<LianJiaHouse> listHousePage(LianJiaHouseQueryParam queryParam, PageQuery pageQuery) {
        return this.getBaseMapper().selectListPage(pageQuery.build(), new QueryWrapper<>());
    }

    @Override
    public List<LianJiaHouse> getListByHouseCode(String houseCode) {
        LocalDate threeMonthAgo = DateUtil.offsetMonth(new Date(), -90).toLocalDateTime().toLocalDate();
        return lambdaQuery().eq(LianJiaHouse::getHouseCode, houseCode)
                .gt(LianJiaHouse::getDate, threeMonthAgo)
                .orderByAsc(LianJiaHouse::getDate)
                .list();
    }

    public void doCollect(LianJiaCollectConfigDTO config) {
        if (CollUtil.isEmpty(config.getKeywords())) {
            return;
        }

        DictDistrict city = dictDistrictService.getById(config.getCityId());

        List<LianJiaHouse> houses = config.getKeywords()
                .stream().map(keyword -> {
                    String res = HttpUtil.get(StrUtil.format("https://{}/zufang/rs{}", config.getDomain(), keyword));
                    Document document = Jsoup.parse(res);
                    int total = Integer.parseInt(document.getElementsByClass("content__title--hl").get(0).text());
                    return document.getElementsByClass("content__list--item").stream().map(ele -> {
                        String houseCode = ele.attr("data-house_code");
                        String title = ele.getElementsByClass("content__list--item--title").get(0)
                                .getElementsByTag("a").get(0).text();
                        String[] descSplit = ele.getElementsByClass("content__list--item--des").get(0).text().split("/");
                        String[] districtInfo = descSplit[0].split("-");
                        String area = descSplit[1].replace("㎡", "").trim();
                        String orientation = descSplit[2].trim();
                        String type = descSplit[3].trim();
                        String districtName = districtInfo[0].trim();
                        String address = Arrays.stream(ArrayUtil.sub(districtInfo, 1, descSplit.length + 1)).collect(Collectors.joining("-")).trim();
                        String priceStr = ele.getElementsByClass("content__list--item-price").get(0).getElementsByTag("em")
                                .text().trim();
                        String tags = ele.getElementsByClass("content__list--item--bottom").get(0).text().trim();
                        String lastInfo = ele.getElementsByClass("content__list--item--time").get(0).text().trim();

                        // 区 可能没有，需要处理 TODO
                        DictDistrict district = dictDistrictService.get(config.getCityId(), districtName);

                        // detail
                        Document detailDocument = Jsoup.parse(HttpUtil.get(StrUtil.format("https://{}/zufang/{}.html", config.getDomain(), houseCode)));
                        List<Element> picListEles = detailDocument.getElementsByClass("content__article__info3").get(0).getElementsByClass("piclist");
                        List<String> pictures = new LinkedList<>();
                        if (CollUtil.isNotEmpty(picListEles)) {
                            picListEles.get(0).getElementsByTag("img").stream().forEach(img -> pictures.add(img.attr("src")));
                        }
                        String subtitleText = detailDocument.getElementsByClass("content__subtitle").text();
                        String lastMaintainTimeStr = ReUtil.getGroup0("(\\d{4})-(\\d{1,2})-(\\d{1,2})", subtitleText);

                        return new LianJiaHouse()
                                .setHouseCode(houseCode)
                                .setPicture(JsonUtils.toJsonString(pictures))
                                .setTitle(title)
                                .setPrice(new BigDecimal(priceStr))
                                .setProvinceId(city.getPid())
                                .setCityId(city.getId())
                                .setDistrictId(district.getId())
                                .setAddress(address)
                                .setArea(new BigDecimal(area))
                                .setOrientation(orientation)
                                .setType(type)
                                .setTags(tags)
                                .setLastMaintainTime(DateUtil.parseDate(lastMaintainTimeStr).toLocalDateTime().toLocalDate())
                                .setDate(LocalDate.now());
                    }).collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.getBaseMapper().batchSaveOrUpdateOnDuplicateUpdate(houses);
    }


}
