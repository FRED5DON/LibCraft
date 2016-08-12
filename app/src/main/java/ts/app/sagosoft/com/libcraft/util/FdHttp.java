package ts.app.sagosoft.com.libcraft.util;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import ts.app.sagosoft.com.libcraft.model.HttpProperty;

/**
 * Created by FRED_angejia on 2016/1/21.
 */
public class FdHttp {

    /**
     *
     * @param method
     * @param urlString
     * @param data
     * @param headers
     * @param response
     */
    public static HttpProperty httpRequest(String method,String urlString, String data,JSONObject headers, HttpResponse response) {
        try {
            // 请求数据
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if(method==null||method.length()==0){
                method="GET";
            }
            conn.setRequestMethod(method);
            if(headers!=null){
                for (String key:headers.keySet() ) {
                    conn.setRequestProperty(key,headers.getString(key));
                }
            }
//            conn.setRequestProperty("Content-Type", "application/json"); // 设置请求头
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if("POST".equals(method) && data!=null && data.trim().length()>0){
                conn.setRequestProperty("Content-Length", data.getBytes().length + "");
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes()); // 写出消息体
                out.flush();
                out.close();
            }else{

            }
            HttpProperty httpProperty = new HttpProperty();
            Map<String, List<String>> map = conn.getHeaderFields();
           try{
               InputStream in = conn.getInputStream();
               byte[] buff = new byte[8192];
               int size = 0;
               StringBuffer sb = new StringBuffer();
               while ((size = in.read(buff)) != -1) {
                   String s = new String(buff, 0, size, "UTF-8");
                   sb.append(s);
               }
               httpProperty.setResponseBody(sb.toString());
           }catch(FileNotFoundException e){
               httpProperty.setResponseBody(e.getMessage());
           }
            httpProperty.setHeader(map);
            httpProperty.setResponseCode(conn.getResponseCode());
            httpProperty.setResponseMsg(conn.getResponseMessage());
            if(map.containsKey("Set-Cookie")){
                httpProperty.setResponseCookie(String.valueOf(map.get("Set-Cookie")));
            }
//              (conn.getResponseCode() == 200);
            if(response!=null){
                response.onResponse(httpProperty);
            }
            return httpProperty;
        } catch (Exception e) {
            e.printStackTrace();
            if(response!=null){
                response.onResponseError(e.getMessage());
            }
            return null;
        }

    }



    public interface HttpResponse {

        void onResponse(HttpProperty property);
        void onResponseError(String errMsg);


    }
}
