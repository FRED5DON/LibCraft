package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.model.HttpProperty;
import ts.app.sagosoft.com.libcraft.util.FdHttp;

/**
 * Created by FRED on 2016/1/21.
 */
public class PostManActivity extends BaseActivity implements FdHttp.HttpResponse {


    @InjectView(R.id.sp_postman_method)
    Spinner spPostmanMethod;
    @InjectView(R.id.sp_postman_contentType)
    Spinner spPostmanContentType;
    @InjectView(R.id.et_postman_url)
    EditText etPostmanUrl;
    @InjectView(R.id.bt_postman_request)
    Button btPostmanRequest;
    @InjectView(R.id.et_postman_header)
    EditText etPostmanHeader;

    @InjectView(R.id.et_postman_body)
    EditText etPostmanBody;
    @InjectView(R.id.layout_postman_body)
    LinearLayout layoutPostmanBody;
    @InjectView(R.id.tv_postman_resp_body)
    TextView tvPostmanRespBody;
    @InjectView(R.id.tv_postman_resp_cookies)
    TextView tvPostmanRespCookies;
    @InjectView(R.id.tv_postman_resp_headers)
    TextView tvPostmanRespHeaders;

    @InjectView(R.id.tv_postman_resp_status)
    TextView tvPostmanRespStatus;
    @InjectView(R.id.et_postman_resp_body)
    EditText etPostmanRespBody;

    @InjectView(R.id.tv_postman_url)
    TextView tvPostmanUrl;
    @InjectView(R.id.layout_postman_mask)
    LinearLayout layoutPostmanMask;

    private static final String[] method = {"GET", "POST", "PUT", "HEAD", "DELETE", "OPTIONS", "TRACE"};
    private static final String[] contentTypes = {ContentType.JSON.name() + "(" + ContentType.JSON.getValue() + ")",
            ContentType.TEXT.name() + "(" + ContentType.TEXT.getValue() + ")", ContentType.XML.name() + "(" + ContentType.XML.getValue() + ")", ContentType.BINARY.name()};
    ContentType contentType = ContentType.JSON;

    public enum ContentType {
        JSON("application/json"),
        TEXT("text/plain"),
        XML("application/xml"),
        BINARY("stream");

        public String getValue() {
            return value;
        }

        String value;

        ContentType(String s) {
            this.value = s;
        }
    }

    private int responseTitleIndex;
    private int spPostmanMethodIndex;
    private int spPostmanContentTypeIndex;
    private TextView[] lists;
    private HttpProperty property;

    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, PostManActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postman);
        ButterKnife.inject(this);
        setTitle("POSTMAN");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, method);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostmanMethod.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contentTypes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostmanContentType.setAdapter(adapter2);
        TextView[] list = {tvPostmanRespBody, tvPostmanRespCookies, tvPostmanRespHeaders};
        this.lists = list;
    }


    @OnClick(R.id.bt_postman_request)
    public void request() {
        etPostmanRespBody.setText("");
        tvPostmanRespStatus.setText("---");
        if (tvPostmanUrl.getText().toString() == null || tvPostmanUrl.getText().toString().isEmpty()) {
            showCustomToast("注意", "请填写URL");
            return;
        }
        try {
            JSONObject headers = JSON.parseObject(etPostmanHeader.getText().toString());
            if (headers == null) {
                headers = new JSONObject();
            }
            headers.put("Content-Type", contentType.getValue());
            String data=null;
            if(layoutPostmanBody.getVisibility()!=View.GONE){
                etPostmanBody.getText().toString();
            }
            final String postData=data;
            final JSONObject header = headers;
            new Thread() {
                @Override
                public void run() {
                    FdHttp.httpRequest(method[spPostmanMethodIndex], tvPostmanUrl.getText().toString(),
                            postData,
                            header, PostManActivity.this);
                }
            }.start();
        } catch (Exception e) {
            showCustomToast("注意", "Headers 不是一个json");
        }

    }

    @Override
    public void onResponse(HttpProperty property) {
        this.property = property;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

    }

    @Override
    public void onResponseError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCustomToast("出错了", msg);
            }
        });
    }


    private void updateUI() {
        if (property == null) {
            return;
        }
        tvPostmanRespStatus.setText(String.valueOf(property.getResponseCode() + " " + property.getResponseMsg()));
        if (responseTitleIndex == 0) {
            etPostmanRespBody.setText(property.getResponseBody());
        } else if (responseTitleIndex == 1) {
            etPostmanRespBody.setText(property.getResponseCookie());
        } else if (responseTitleIndex == 2) {
            StringBuffer sb = new StringBuffer();
            Map<String, List<String>> headers = property.getHeader();
            etPostmanRespBody.setText(JSON.toJSONString(headers));
        }
    }

    @OnClick(R.id.bt_postman_url_confirm)
    public void writeUrlToTvUrl() {
        layoutPostmanMask.setVisibility(View.GONE);
        String url = etPostmanUrl.getText().toString();
        tvPostmanUrl.setText(url);
    }

    @OnClick(R.id.tv_postman_url)
    public void writeUrl() {
        layoutPostmanMask.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (layoutPostmanMask.getVisibility() == View.VISIBLE) {
            layoutPostmanMask.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    @OnItemSelected(R.id.sp_postman_method)
    public void OnItemClickMethod(AdapterView<?> parent, View view,
                                  int pos, long id) {
        spPostmanMethodIndex = pos;
        if ("GET".equals(method[pos]) || "HEAD".equals(method[pos])) {
            layoutPostmanBody.setVisibility(View.GONE);
        } else {
            layoutPostmanBody.setVisibility(View.VISIBLE);
        }
    }

    @OnItemSelected(R.id.sp_postman_contentType)
    public void OnItemClickContentType(AdapterView<?> parent, View view,
                                       int pos, long id) {
        spPostmanContentTypeIndex = pos;
        if (pos == 0) {
            contentType = ContentType.JSON;
        } else if (pos == 1) {
            contentType = ContentType.TEXT;
        } else if (pos == 2) {
            contentType = ContentType.XML;
        } else if (pos == 3) {
            contentType = ContentType.BINARY;
        }
    }


    @OnClick(R.id.tv_postman_resp_body)
    public void responseBody() {
        changeRespTab(R.id.tv_postman_resp_body);
    }

    @OnClick(R.id.tv_postman_resp_cookies)
    public void responseCookies() {
        changeRespTab(R.id.tv_postman_resp_cookies);
    }

    @OnClick(R.id.tv_postman_resp_headers)
    public void responseHeaders() {
        changeRespTab(R.id.tv_postman_resp_headers);
    }


    public void changeRespTab(int resId) {
        for (int i = 0; i < lists.length; i++) {
            if (lists[i].getId() == resId) {
                responseTitleIndex = i;
                lists[i].setEnabled(false);
                lists[i].setTextColor(0xFF000000);
            } else {
                lists[i].setEnabled(true);
                lists[i].setTextColor(0xFF999999);
            }
        }
        renderResponseBody();
    }

    private void renderResponseBody() {
        updateUI();
    }


    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }


}
