package ts.app.sagosoft.com.libcraft.model;

import java.util.List;
import java.util.Map;

public class HttpProperty {
    private Map<String, List<String>> header;
    private int responseCode;
    private String responseBody;


    private String responseCookie;

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    private String responseMsg;

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getResponseCookie() {
        return responseCookie;
    }

    public void setResponseCookie(String responseCookie) {
        this.responseCookie = responseCookie;
    }
}