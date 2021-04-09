package com.xiaozhuo.ctbsb.common.utils;

import java.net.URLEncoder;
import java.time.Instant;
import java.util.Date;

import com.xiaozhuo.ctbsb.common.exception.Asserts;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class OcrUtil {

    private final static String ak = "Rdza2mVpNGgWRjwTmrwxSEYY";
    private final static String sk = "83vfVG9ejdEwOypRMk1TyvSqKkHewncV";
    private final static long EXPIRATION_TIME = 432000000;
    private static String access_token;
    private static Date expireDate;

    public static String getToken(String ak, String sk){
        Date curDate = new Date();
        if(expireDate == null || curDate.after(expireDate)){
            // 获取token地址
            String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
            String getAccessTokenUrl = authHost
                    // 1. grant_type为固定参数
                    + "grant_type=client_credentials"
                    // 2. 官网获取的 API Key
                    + "&client_id=" + ak
                    // 3. 官网获取的 Secret Key
                    + "&client_secret=" + sk;
            try {
                URL realUrl = new URL(getAccessTokenUrl);
                // 打开和URL之间的连接
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonObject = new JSONObject(result);
                access_token = jsonObject.getString("access_token");

                expireDate = new Date( Instant.now().toEpochMilli() + EXPIRATION_TIME);
                return access_token;
            } catch (Exception e) {
                Asserts.fail("获取token失败！");
            }
        }
        return access_token;
    }

    public static String BaiduOcr(String questionImagePath){
        String accessToken = getToken(ak,sk);
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {
            // 本地文件路径
            byte[] imgData = FileUtil.readFileByBytes(questionImagePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;
            return HttpUtil.post(url, accessToken, param);
        } catch (Exception e) {
            Asserts.fail("Api调用失败");
        }
        return null;
    }
}
