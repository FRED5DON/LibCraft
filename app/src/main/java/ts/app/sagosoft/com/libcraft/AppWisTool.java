package ts.app.sagosoft.com.libcraft;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.telephony.TelephonyManager;


import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ts.app.sagosoft.com.libcraft.model.AppInfo;

/**
 * Created by FRED_angejia on 2016/1/12.
 */
public class AppWisTool {


    private static String Model;
    private static String OSVer;
    private static String DeviceID;

    public static void init(Context context) {
        //  设备机型：如，iphone4，i9000 (Model)
        Model = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        //  OS版本：如，iOS5.0，android2.3.7 (OSVer)
        OSVer = android.os.Build.VERSION.RELEASE;
        //  设备的DeviceID，平板可能会没有，若有，则作为唯一标示设备ID——UniqueDeviceID
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr != null) {
            DeviceID = mTelephonyMgr.getDeviceId();
        }
    }

    /**
     * 获取应用信息列表
     *
     * @param context
     * @param type    是否统计系统应用[0 均包括 1 只包含第三方应用，2 只包含系统应用]
     * @return
     */
    public static Set<AppInfo> getAppList(Context context, int type) {
        Set<AppInfo> list = new HashSet<>();
        if (android.os.Build.VERSION.SDK_INT > 19) {
            PackageManager pm = context.getPackageManager();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> mResolveInfo = pm.queryIntentActivities(mainIntent, 0);
            for (ResolveInfo rinfo : mResolveInfo) {
                if (type == 2) {
                    if ((rinfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        AppInfo info = new AppInfo();
                        info.setAppName(rinfo.activityInfo.applicationInfo.loadLabel(pm)
                                .toString());
                        info.setAppPackage(rinfo.activityInfo.packageName);
                        list.add(info);
                    }
                } else {
                    if (type == 0 || (rinfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
                    {
                        AppInfo info = new AppInfo();
                        info.setAppName(rinfo.activityInfo.applicationInfo.loadLabel(pm)
                                .toString());
                        info.setAppPackage(rinfo.activityInfo.packageName);
                        list.add(info);
                    }
                }
            }
        } else {
            PackageManager pm = context.getPackageManager();
            // Return a List of all packages that are installed on the device.
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                // 判断系统/非系统应用
                if (type == 2) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        AppInfo info = new AppInfo();
                        info.setAppName(packageInfo.applicationInfo.loadLabel(pm)
                                .toString());
                        info.setAppPackage(packageInfo.packageName);
                        list.add(info);
                    }
                } else {
                    if (type == 0 || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
                    {
                        AppInfo info = new AppInfo();
                        info.setAppName(packageInfo.applicationInfo.loadLabel(pm)
                                .toString());
                        info.setAppPackage(packageInfo.packageName);
//                info.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
//                // 获取该应用安装包的Intent，用于启动该应用
//                info.setAppIntent(pm.getLaunchIntentForPackage(packageInfo.packageName));
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 获取运行时最顶层包名
     * android sdk 23+只能获取自身和launcher的包名
     * @param context
     * @return
     */
    public static String getTopRuningTaskPackageName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = "";
        if (android.os.Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP) {
            packageName =getTopRuningTaskPackageNameNew(context);
            /*//5.0的某些版本可以用此方法
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if(manager.getRunningAppProcesses()!=null && manager.getRunningAppProcesses().size()>0){
                ActivityManager.RunningAppProcessInfo rn = manager.getRunningAppProcesses().get(0);
                packageName = rn.processName;
            }*/
        } else {
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            if (list.size() > 0) {
                ComponentName cn = list.get(0).topActivity;
                packageName = cn.getPackageName();
            }
        }
        return packageName;
    }

    /**
     * 获取运行时最顶层包名
     * <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
            tools:ignore="ProtectedPermissions" />
     * api >= 21
     * @param context
     * @return
     * @throws Exception
     */
    @TargetApi(21)
    private static String getTopRuningTaskPackageNameNew(Context context){
        String topPackageName = null;
        try{
            final int PROCESS_STATE_TOP = 2;
            ActivityManager.RunningAppProcessInfo currentInfo = null;
            Field field = null;
            try {
                field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            } catch (Exception ignored) {
            }
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                    Integer state = null;
                    try {
                        state = field.getInt(app);
                    } catch (Exception e) {
                    }
                    if (state != null && state == PROCESS_STATE_TOP) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            return currentInfo.processName;
        }catch (Exception e){
        }
        return topPackageName;
    }



    /**
     * 获取包名在列表中的位置【实体信息】
     *
     * @param mAppList
     * @param currentAppPackageName 包名
     * @return
     */
    public static AppInfo getTarget(Set<AppInfo> mAppList, String currentAppPackageName) {
        if (currentAppPackageName == null || currentAppPackageName.length() == 0) {
            return null;
        }
        if (mAppList != null && mAppList.size() > 0) {
            for (AppInfo target : mAppList) {
                if (target.getAppPackage().equals(currentAppPackageName)) {
                    return target;
                }
            }
        }
        return null;
    }


    /**
     * 生成日志
     *
     * @param mAppList
     * @return
     */
    public static com.alibaba.fastjson.JSONObject mkResult(Set<AppInfo> mAppList) {
        if (mAppList != null) {
            long time = System.currentTimeMillis();
            com.alibaba.fastjson.JSONObject json = JSON.parseObject("{}");
            json.put("deviceid", DeviceID);
            json.put("model", Model);
            json.put("osver", OSVer);
            json.put("ctime", time);
            json.put("log", mAppList);
            return json;
        }
        return null;
    }
}
