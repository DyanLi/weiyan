package com.dyan.weiyan.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2017/7/10.
 */
public class WeiyanUtil {
    public static int OK_CODE = 0;
    public static int FAIL_CODE = 1;

    public static int TICKET_NORMAL = 0;
    public static int TICKET_EXPIRED = 1;

    public static String[] IMAGE_ALLOWED_SUFFIX = new String[]{"png", "bmp", "jpg", "jpeg"};

    public static String IMAGE_DIR = "/Users/Dyan/Pictures/upload";
    public static String DOMAIN_DIR = "http://127.0.0.1:8080/";

    public static int MESSAGE_UNREAD = 0;
    public static int MESSAGE_READ = 1;

    public static void UpdateCookieTicket(HttpServletResponse response, String ticket, int maxAge) {
        Cookie cookie = new Cookie("ticket", ticket);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    public static JSONObject GetJSON(boolean ret, Map<String, String> res) {
        JSONObject json = GetJSON(ret);
        json.putAll(res);
        return json;
    }

    public static JSONObject GetJSON(boolean ret, String msg) {
        JSONObject json = GetJSON(ret);
        json.put("msg", msg);
        return json;
    }

    public static JSONObject GetJSON(boolean ret) {
        JSONObject json = new JSONObject();
        json.put("code", ret ? 0 : 1);
        return json;
    }

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    public static boolean isFileAllowed(String filename){
        for (String ele : IMAGE_ALLOWED_SUFFIX) {
            if (ele.equals(filename)){
                return true;
            }
        }
        return false;
    }
}
