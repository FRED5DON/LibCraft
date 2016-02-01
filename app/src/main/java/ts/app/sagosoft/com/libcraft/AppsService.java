package ts.app.sagosoft.com.libcraft;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ts.app.sagosoft.com.libcraft.model.AppInfo;

/**
 * Created by FRED_angejia on 2016/1/13.
 */
public class AppsService extends IntentService {

    private boolean isRun = false;

    /**
     * 时间间隔
     */
    private static final long SEND_TIME_SPAN = 800;

    private Set<AppInfo> systemApps;  //系统应用
    private Set<AppInfo> tPApps;
    private List<String> logs = new ArrayList<>();
    private AppInfo lastAppInfo;

    public AppsService(String name) {
        super(name);
    }

    public AppsService() {
        super("angejia_AppsService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tPApps = AppWisTool.getAppList(AppsService.this, 1);
        if (isRun) {
            return;//如果启动过了 什么也不干
        }
        isRun = true;
        onWatchStart();
//        startForeground(0,new Notification());
    }

    /**
     * 最后运行的App[包含系统App、Launcher等]
     */
    public String lastAppPackageName;

    /**
     * 监听APP核心代码
     */
    private void onWatchStart() {
        long time = 0;
        String currentAppPackageName = null;
        if (systemApps == null) {
            systemApps = AppWisTool.getAppList(AppsService.this, 2);
        }
        while (isRun) {
            //1 检查当前运行的程序
            //2 保证此应用不是系统应用先
            currentAppPackageName = AppWisTool.getTopRuningTaskPackageName(AppsService.this);
            AppInfo nowApp = AppWisTool.getTarget(tPApps, currentAppPackageName);
            if (AppWisTool.getTarget(systemApps, currentAppPackageName) == null) {
                //在当前活动应用包名可以获得的情况下才做记录：
                //   如果当前应用与上次不同时（包括切换到桌面、不统计系统应用时切换到系统应用）
                if (!TextUtils.isEmpty(currentAppPackageName)) {
                    if (!currentAppPackageName.equals(lastAppPackageName)) {
                        //同一款应用不需要发送Log
                        if (!TextUtils.isEmpty(lastAppPackageName) && AppWisTool.getTarget(systemApps, lastAppPackageName) == null) {
                            //FIXME 发送上个应用的关闭Log
                            if (lastAppInfo != null) {
                                Log.i("AppsService", "上个应用" + lastAppInfo.getAppName() + "关闭");
                            }
                        }
                        if (nowApp == null) {
                            Log.i("AppsService", "新应用" + currentAppPackageName + "");
                            tPApps = AppWisTool.getAppList(AppsService.this, 1);
                            AppInfo newApp = AppWisTool.getTarget(tPApps, currentAppPackageName);
                            if (newApp != null) {
                                nowApp = newApp;
                            }
                        }
                        if (nowApp != null) {
                            //FIXME 发送currentAppPackageName 打开Log
                            Log.i("AppsService", "当前应用" + nowApp.getAppName() + "打开");
                        }
                    }
                }
            } else {
                //如果当前应用是系统应用 新增的
                //   上个应用不是系统应用
                if (!TextUtils.isEmpty(lastAppPackageName) && AppWisTool.getTarget(systemApps, lastAppPackageName) == null) {
                    //FIXME 发送上个应用的关闭Log
                    if (lastAppInfo != null) {
                        Log.i("AppsService", "上个应用" + lastAppInfo.getAppName() + "关闭");
                        Log.i("AppsService", "当前应用[包名]" + currentAppPackageName + "是系统应用");
                    }
                }
            }
            AppInfo lastApp = AppWisTool.getTarget(tPApps, lastAppPackageName);
            if (AppWisTool.getTarget(systemApps, lastAppPackageName) == null && lastAppInfo != null && lastApp == null) {
                //上个应用被卸载了
                if (tPApps != null) {
                    Log.i("AppsService", "上个应用" + lastAppInfo.getAppName() + "被卸载");
                    tPApps.remove(lastAppInfo);

                }
            }
            lastAppPackageName = currentAppPackageName;
            lastAppInfo = nowApp;
            //程序每隔一段时间执行一次
            try {
                Thread.sleep(SEND_TIME_SPAN);
            } catch (InterruptedException e) {
            }
        }

    }


    @Override
    public void onLowMemory() {
        isRun = false;
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}
