package com.adnonstop.frame.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON解析帮助类
 *
 * @author ikould on 2017/12/27.
 */

public class JSONUtil {

    public static JSONArray getJSONArray(JSONObject jsonObject, String key) throws JSONException {
        return getJSONArray(jsonObject, key, null);
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String key) throws JSONException {
        return getJSONObject(jsonObject, key, null);
    }

    public static String getString(JSONObject jsonObject, String key) throws JSONException {
        return getString(jsonObject, key, null);
    }

    public static int getInt(JSONObject jsonObject, String key) throws JSONException {
        return getInt(jsonObject, key, 0);
    }

    public static long getLong(JSONObject jsonObject, String key) throws JSONException {
        return getLong(jsonObject, key, 0L);
    }

    public static double getDouble(JSONObject jsonObject, String key) throws JSONException {
        return getDouble(jsonObject, key, 0D);
    }

    public static boolean getBoolean(JSONObject jsonObject, String key) throws JSONException {
        return getBoolean(jsonObject, key, false);
    }


    // ========== 以下带默认参数 ===========

    public static JSONArray getJSONArray(JSONObject jsonObject, String key, JSONArray defaultArray) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultArray;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultArray;
        if (!(o instanceof JSONArray))
            throw new JSONException("字段异常，非JSONArray类型!");
        return (JSONArray) o;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject defaultJson) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultJson;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultJson;
        if (!(o instanceof JSONObject))
            throw new JSONException("字段异常，非JSONObject类型!");
        return (JSONObject) o;
    }

    public static String getString(JSONObject jsonObject, String key, String defaultStr) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultStr;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultStr;
        if (!(o instanceof String))
            throw new JSONException("字段异常，非String类型!");
        return (String) o;
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultInt) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultInt;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultInt;
        if (!(o instanceof Integer))
            throw new JSONException("字段异常，非Integer类型!");
        return (int) o;
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultLong) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultLong;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultLong;
        if (!(o instanceof Long))
            throw new JSONException("字段异常，非Long类型!");
        return (long) o;
    }

    public static double getDouble(JSONObject jsonObject, String key, double defaultDouble) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultDouble;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultDouble;
        if (!(o instanceof Double))
            throw new JSONException("字段异常，非Double类型!");
        return (double) o;
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultTf) throws JSONException {
        if (jsonObject.isNull(key)) {
            return defaultTf;
        }
        Object o = jsonObject.get(key);
        if (o == null)
            return defaultTf;
        if (!(o instanceof Boolean))
            throw new JSONException("字段异常，非Boolean类型!");
        return (boolean) o;
    }
}
