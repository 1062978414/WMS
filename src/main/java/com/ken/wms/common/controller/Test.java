package com.ken.wms.common.controller;
import com.github.pagehelper.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class Test {
    public static void main(String[] args) throws IOException {
        String url = "C:/Users/DL/Desktop/111113013.jpg";
        String str = Test.ImageToBase64ByLocal(url);
        System.out.println(str);
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(400);
        manager.setDefaultMaxPerRoute(30);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager)
                .build();
        HttpPost httpPost = new HttpPost("http://deepi.sogou.com:80/api/sogouService");
        String pid = "1bb78468bdff687c0335cae8e0b138ca";
        String service = "basicOpenOcr";
        String salt = "15084041116012";
        // base64 string file picture,too long in the case we will omit string
        String image = str;
        String imageShort = image.substring(0,1024);
        String sign = DigestUtils.md5Hex(pid + service + salt + imageShort + "4e6023b306c05f9b9b8e8a3645725243");

        List nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("pid", pid));
        nvps.add(new BasicNameValuePair("lang", "zh-CHS"));
        nvps.add(new BasicNameValuePair("image", image));
        nvps.add(new BasicNameValuePair("service", service));
        nvps.add(new BasicNameValuePair("salt", salt));
        nvps.add(new BasicNameValuePair("sign", sign));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpRequestBase requestBase = httpPost;
        requestBase.addHeader("content-type", "application/x-www-form-urlencoded");
        requestBase.addHeader("accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(requestBase);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        System.out.println(result);
    }

    /**
     * 本地图片转换成base64字符串
     * @param imgFile	图片本地路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:40:46
     */
    public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }



    /**
     * 在线图片转换成base64字符串
     *
     * @param imgURL	图片线上路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:43:18
     */
    public static String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }


    /**
     * base64字符串转换成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:42:17
     */
    public static boolean Base64ToImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

        if (StringUtil.isEmpty(imgStr)) // 图像数据为空
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
