package ts.app.sagosoft.com.libcraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.activities.BaseActivity;
import ts.app.sagosoft.com.libcraft.activities.DialogsActivity;
import ts.app.sagosoft.com.libcraft.activities.PostManActivity;
import ts.app.sagosoft.com.libcraft.activities.RxDemoActivity;
import ts.app.sagosoft.com.libcraft.db.AppInfoHelper;
import ts.app.sagosoft.com.libcraft.model.AppInfo;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.button_toast)
    Button buttonToast;
    @InjectView(R.id.button_showapps)
    Button buttonShowapps;
    @InjectView(R.id.button_starttask)
    Button buttonstart;
    private List<String> result;
    private boolean isRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        String s = "{\"type\":\"has_new_msg\",\"data\":{\"user_msg_flag\":1}}";
        try {
            byte[] b = s.getBytes("utf-8");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                sb.append(",");
                sb.append(b[i]);
            }
            Log.i("MainActivity", sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void startTask() {
        Intent service = new Intent();
        service.setClass(this, AppsService.class);
        startService(service);
    }

    private void stopTask() {
        Intent service = new Intent();
        service.setClass(this, AppsService.class);
        stopService(service);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventHelper.getHelper().register(this);
    }

    @Subscribe
    public void onReceive(ArrayList<String> content) {
        if (content == null) {
            return;
        }
        this.result = content;
    }

    @Subscribe
    public void onServiceRun(Boolean isRun) {
        if (this.isRun == isRun) {
            return;
        }
        this.isRun = isRun;
        if (isRun) {
            buttonstart.setText("停止App检测服务");
        } else {
            buttonstart.setText("启动App检测服务");
        }
    }

    @OnClick(R.id.button_dialog)
    public void onClickDialogEvent(View view) {
        startActivity(DialogsActivity.mkIntent(this));
    }

    @OnClick(R.id.button_toast)
    public void onClickToastEvent(View view) {
        uITopToast.build(MainActivity.this, null, "http://7teb43.com2.z0.glb.qiniucdn.com/FlqdfHWM8F9pyBtMf_gzMh4kFgdt", "title", "content")
                .showWithAnimation().hideWithMills(0);

    }

    @OnClick(R.id.button_showapps)
    public void onClickAppsEvent(View view) {
        boolean isContainSystem = false;
        String content = AppWisTool.getAppList(this, 1).toString();
        uITopToast.build(MainActivity.this, null, "http://7teb43.com2.z0.glb.qiniucdn.com/FlqdfHWM8F9pyBtMf_gzMh4kFgdt",
                String.format("手机安装的应用%s", isContainSystem ? "[包含系统应用]" : "[不包含系统应用]"), content)
                .showWithAnimation().hideWithMills(0);
    }

    @OnClick(R.id.button_starttask)
    public void onClickStartTaskEvent(View view) {
        if (!isRun) {
            startTask();
            buttonstart.setText("停止App检测服务");
        } else {
            stopTask();
            buttonstart.setText("启动App检测服务");
        }
        isRun = !isRun;

    }

    @OnClick(R.id.button_taskreport)
    public void onClickTaskReportEvent(View view) {
        if (!isRun) {
            showCustomToast("提示", "对不起，检测服务没打开");
            return;
        }

        String mresult;
        if (result == null) {
            mresult = "日志正在生成，请稍后获取";
        } else {
            mresult = String.valueOf(result);
        }
        uITopToast.build(MainActivity.this, null, "http://7teb43.com2.z0.glb.qiniucdn.com/FlqdfHWM8F9pyBtMf_gzMh4kFgdt",
                "来自Service", mresult)
                .showWithAnimation().hideWithMills(0);
    }


    @OnClick(R.id.button_writeSqlite)
    public void onClickWriteDb(View view) {
        AppInfo item = new AppInfo();
        item.setAppPackage(this.getPackageName());
        item.setAppName(getResources().getString(R.string.app_name));
        item.setStartTime(System.currentTimeMillis());
        item.setCount(item.getCount() + 1);
        try {
            Dao.CreateOrUpdateStatus status = AppInfoHelper.getHelper(this).getAppInfoDao().createOrUpdate(item);//传入id则更新
            if (status.isCreated() || status.isUpdated()) {
                showCustomToast("提示", String.format("数据 => %s 【%s】成功", String.valueOf(item), status.isCreated() ? "插入" : "更新"));
            } else {
                showCustomToast("提示", "数据操作失败");
            }
        } catch (SQLException e) {
            showCustomToast("提示", "数据查询失败 " + e.getMessage());
        }
    }


    @OnClick(R.id.button_readSqlite)
    public void onClickReadDb(View view) {
        try {
            List<AppInfo> list = AppInfoHelper.getHelper(this).getAppInfoDao().queryForAll();
            showCustomToast("提示", String.format("数据查询结果 => %s ", String.valueOf(list)));
        } catch (SQLException e) {
            showCustomToast("提示", "数据查询失败 " + e.getMessage());
        }
    }

    @OnClick(R.id.button_clearTableSqlite)
    public void onClickClearDb(View view) {
        try {
            int status = AppInfoHelper.getHelper(this).getAppInfoDao().delete(AppInfoHelper.getHelper(this).getAppInfoDao().queryForAll());
            showCustomToast("提示", String.format(" %d rows are affected ", status));
        } catch (SQLException e) {
            showCustomToast("提示", "数据表清空失败 " + e.getMessage());
        }
    }

    @OnClick(R.id.button_postman)
    public void onClickPostMan(View view) {
        startActivity(PostManActivity.mkIntent(this));
    }

    @OnClick(R.id.button_rxdemo)
    public void onClickRxDemo(View view) {
        startActivity(RxDemoActivity.mkIntent(this));
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventHelper.getHelper().unregister(this);
    }
}
