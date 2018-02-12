package com.adnonstop.frame.net;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adnonstop.frame.util.HttpUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 单纯Okhttp封装的Helper
 */
@Keep
public class HttpHelper {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    private static OkHttpClient client = new OkHttpClient();

    private HttpHelper() {
        throw new AssertionError("No instances");
    }

    /**
     * 同步get
     *
     * @param url url地址
     * @return 返回结果：字符串表示
     * @throws IOException 异常
     */
    public static String get(String url) throws IOException {
        try {
            if (!HttpUtil.isUrlValid(url)) {
                return "";
            }
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                ResponseBody body = response.body();
                return body == null ? "" : body.string();
            }
        } catch (Exception e) {
            Log.e("HttpHelper", "getSync: e = " + e);
        }
        return "";
    }

    /**
     * post同步请求
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     */
    private String requestPost(String url, HashMap<String, String> paramsMap) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = new Request.Builder().url(url).post(body).build();
            //创建一个Call
            final Call call = client.newCall(request);
            //执行请求
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            //请求执行成功
            if (response.isSuccessful() && responseBody != null) {
                return responseBody.toString();
            }
        } catch (Exception e) {
            Log.e("HttpHelper", "requestPostBySyn: e = " + e);
        }
        return null;
    }

    /**
     * 异步get
     *
     * @param map      带有参数的Map
     * @param url      url地址
     * @param callback 返回结果监听
     */
    public static void getSync(Map<String, Object> map, @NonNull String url, @NonNull final OkHttpCallBack callback) {
        try {
            if (!HttpUtil.isUrlValid(url)) {
                return;
            }
            final Request request;
            String urlStr = "?";
            if (map != null) {
                for (Map.Entry entry : map.entrySet()) {
                    urlStr += entry.getKey().toString() + "=" + entry.getValue().toString() + "&";
                }
            }
            urlStr.substring(0, urlStr.length() - 1);
            request = new Request.Builder().url(url + urlStr).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure(request, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onResponse(response);
                }
            });
        } catch (Exception e) {
            Log.e("HttpHelper", "getSync: e = " + e);
        }
    }

    /**
     * 异步post
     *
     * @param map      带有参数的Map
     * @param url      url地址
     * @param callback 返回结果监听
     */
    public static void postfrom(Map<String, Object> map, @NonNull String url, @NonNull final OkHttpCallBack callback) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if (map != null) {
                for (Map.Entry entry : map.entrySet()) {
                    builder.add(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            RequestBody formBody = builder.build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure(request, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onResponse(response);
                }
            });
        } catch (Exception e) {
            Log.e("HttpHelper", "postfrom: e = " + e);
        }
    }

    // ======== 监听 ========

    public interface OkHttpCallBack {
        /**
         * 失败
         *
         * @param request HTTP request
         * @param e       异常
         */
        void onFailure(Request request, Exception e);

        /**
         * 成功返回
         *
         * @param response HTTP response
         */
        void onResponse(Response response);
    }
}
