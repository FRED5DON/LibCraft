package ts.app.sagosoft.com.libcraft;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ts.app.sagosoft.com.libcraft.model.AppInfo;

/**
 * Created by FRED_angejia on 2016/1/13.
 */
public class AppWisperService extends IntentService {

    private Set<AppInfo> mAppList;
    /**
     * 最后运行的App
     */
    private AppInfo mLastApp;
    private boolean isRun = false;
    private final static boolean DEBUG_TEST=true;

    /**
     * 每20秒发送数据
     */
    private static final long SEND_TIME_SPAN = 20000;

    public AppWisperService(String name) {
        super(name);
    }

    public AppWisperService() {
        super("angejia_wisper");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isRun=true;
        onWatchStart();
//        startForeground(0,new Notification());
    }

    /**
     * 监听APP核心代码
     */
    private void onWatchStart() {
        long time = 0;
        String currentAppPackageName = null;
        AppWisTool.init(this);
        while (isRun) {
            EventHelper.getHelper().post(isRun);
            long mTime = System.currentTimeMillis();
            if (mAppList == null || mAppList.size() == 0) {
                mAppList = AppWisTool.getAppList(AppWisperService.this, 1);
            } else {
                Set<AppInfo> desApplst = AppWisTool.getAppList(AppWisperService.this, 1);
                //粗略判断如果应用数发生变化【应用的安装、卸载】则通知mAppList进行改变【由于是日志仅作更新，保留原信息】
                if (desApplst.size() != mAppList.size()) {
                    onNotifyAppListSetChanged(desApplst);
                }
            }
            //1 检查当前运行的程序
            currentAppPackageName = AppWisTool.getTopRuningTaskPackageName(AppWisperService.this);
            //2 在list中查找是否存在
            AppInfo target = AppWisTool.getTarget(mAppList, currentAppPackageName);
            //存在则计时、计次、计入span(最后启动的App开始时间做结算)
            if (target != null) {
                if (target.getCount() == 0) {
                    target.setCount(1);
                    target.setStartTime(mTime - 1000);
                    mLastApp = target;
                }
                target.setSpan(target.getSpan() + mTime - target.getStartTime());
                target.setStartTime(mTime);
                if (mLastApp != null && target.getAppPackage().equals(mLastApp.getAppPackage())) {
                } else {
                    if (mLastApp == null) {
                    } else {
                        mLastApp.setSpan(mLastApp.getSpan() + mTime - mLastApp.getStartTime());
                        target.setCount(target.getCount() + 1);
                    }
                    //更新最后使用的app
                    mLastApp = target;
                }

            } else {
                if(mLastApp!=null){
                    mLastApp.setStartTime(mTime);
                }
                //如果是测试阶段 打开暴力测试【没经过时间间隔都会传[无论处于launcher还是系统APP界面]】
                if(!DEBUG_TEST){
                    //停止计时 立即进行下次检测
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    //continue的情况产生于用户不停留在任何第三方应用中或者当前没有运行的程序
                    continue;
                }
            }
            //程序每隔一段时间执行一次
            try {
                final int span = 500;
                Thread.sleep(span);
                time += span;
                if (time <0) {//溢出判断
                    time = 0;
                }
            } catch (InterruptedException e) {
            }
            //TODO 每 SEND_TIME_SPAN 统计一次报告
            if (time % SEND_TIME_SPAN == 0) {
                com.alibaba.fastjson.JSONObject result = AppWisTool.mkResult(mAppList);
                if (result != null) {
                    EventHelper.getHelper().post(result);
                    Log.i("AppWisperService", "==========" + Thread.currentThread().getName() + "==========");
                    SpringMusicClient.getSpringMusicClient().createNewAlbum(result, new Callback<String>(){

                        @Override
                        public void success(String s, Response response) {
                            Log.i("AppWisperService", String.valueOf(s));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("AppWisperService", "error: " + error.getMessage());
                        }
                    });
                }
            }
        }
        EventHelper.getHelper().post(isRun);
    }


    /**
     * @param mAppList
     */
    public void onNotifyAppListSetChanged(Set<AppInfo> mAppList) {
        if (mAppList == null) {
            return;
        }
        //mAppList中添加不存在的app
        for (AppInfo a : mAppList) {
            if (!this.mAppList.contains(a)) {
                this.mAppList.add(a);
            }
        }
        //设置标记
        for (AppInfo b : this.mAppList) {
            if (!mAppList.contains(b)) {
                b.setIsInstalled(false);
            }else{
                b.setIsInstalled(true);
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
        EventHelper.getHelper().post(isRun);
        super.onDestroy();
    }
}
