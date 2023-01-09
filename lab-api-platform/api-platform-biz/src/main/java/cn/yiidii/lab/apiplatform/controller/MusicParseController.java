package cn.yiidii.lab.apiplatform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.base.R;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.base.exception.BizException;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MusicParseController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/music-parse")
@RequiredArgsConstructor
public class MusicParseController {

    private final ConfigService configService;

    @GetMapping
    public R<?> parse(@RequestParam String songMid) {
        String url = qqMusicParse(songMid);
        return R.ok(Map.of("url", url));
    }

    private String qqMusicParse(String songMid) {
        String cookie = configService.get("qqMusic_cookie", "");

        String data = "{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":123456,\"g_tk_new_20200303\":1225752628,\"g_tk\":1225752628},\"req_1\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"5308123525\",\"songmid\":[\"#songmid#\"],\"songtype\":[0,0,0],\"uin\":\"123456\",\"loginflag\":1,\"platform\":\"20\"}},\"req_2\":{\"module\":\"music.musicasset.SongFavRead\",\"method\":\"IsSongFanByMid\",\"param\":{\"v_songMid\":[\"#songmid#\"]}},\"req_3\":{\"method\":\"GetCommentCount\",\"module\":\"music.globalComment.GlobalCommentRead\",\"param\":{\"request_list\":[{\"biz_type\":1,\"biz_id\":\"332151800\",\"biz_sub_type\":0}]}},\"req_4\":{\"module\":\"music.musichallAlbum.AlbumInfoServer\",\"method\":\"GetAlbumDetail\",\"param\":{\"albumMid\":\"002orv4k2x9oJt\"}},\"req_5\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"7891902884\",\"songmid\":[\"#songmid#\"],\"songtype\":[0],\"uin\":\"123456\",\"loginflag\":1,\"platform\":\"20\"}},\"req_6\":{\"module\":\"music.trackInfo.UniformRuleCtrl\",\"method\":\"CgiGetTrackInfo\",\"param\":{\"ids\":[],\"types\":[0,0,0]}}}";
        data = data.replaceAll("#songmid#", songMid);

        String sign = qqMusicSign(data);
        HttpResponse res = HttpRequest.post("https://u.y.qq.com/cgi-bin/musics.fcg?sign=" + sign)
                .body(data)
                .cookie(cookie)
                .execute();
        JSONObject resJo = JSONObject.parseObject(res.body());
        String purl = resJo.getJSONObject("req_1").getJSONObject("data").getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
        if (StrUtil.isBlank(purl)) {
            throw new BizException("解析异常");
        }
        String url = "https://dl.stream.qqmusic.qq.com/" + purl;
        return url;
    }

    /**
     * 获取QQ音乐sign
     *
     * @param data data
     * @return sign
     */
    private String qqMusicSign(String data) {
        String s = "{\"0\":0,\"1\":1,\"2\":2,\"3\":3,\"4\":4,\"5\":5,\"6\":6,\"7\":7,\"8\":8,\"9\":9,\"A\":10,\"B\":11,\"C\":12,\"D\":13,\"E\":14,\"F\":15}";
        Map<String, String> k1 = JSONObject.parseObject(s, Map.class);
        int[] l1 = new int[]{212, 45, 80, 68, 195, 163, 163, 203, 157, 220, 254, 91, 204, 79, 104, 6};
        String[] t = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/", "="};

        String[] md5 = DigestUtil.md5Hex(data).toUpperCase().split("");

        String t1 = md5[21] + md5[4] + md5[9] + md5[26] + md5[16] + md5[20] + md5[27] + md5[30];
        String t3 = md5[18] + md5[11] + md5[3] + md5[2] + md5[1] + md5[7] + md5[6] + md5[25];

        List<Integer> ls2 = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            Integer x1 = Convert.toInt(k1.get(md5[i * 2]));
            Integer x2 = Convert.toInt(k1.get(md5[i * 2 + 1]));
            Integer x3 = ((x1 * 16) ^ x2) ^ l1[i];
            ls2.add(x3);
        }

        List<String> ls3 = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                ls3.add(t[ls2.get(ls2.size() - 1) >> 2]);
                ls3.add(t[(ls2.get(ls2.size() - 1) & 3) << 4]);
            } else {
                Integer x4 = ls2.get(i * 3) >> 2;
                Integer x5 = (ls2.get(i * 3 + 1) >> 4) ^ ((ls2.get(i * 3) & 3) << 4);
                Integer x6 = (ls2.get(i * 3 + 2) >> 6) ^ ((ls2.get(i * 3 + 1) & 15) << 2);
                Integer x7 = 63 & ls2.get(i * 3 + 2);
                ls3.add(t[x4]);
                ls3.add(t[x5]);
                ls3.add(t[x6]);
                ls3.add(t[x7]);
            }
        }

        String t2 = CollUtil.join(ls3, "").replaceAll("[\\/+]", "");
        String sign = "zzb" + t1 + t2 + t3;
        return sign.toLowerCase();
    }
}
