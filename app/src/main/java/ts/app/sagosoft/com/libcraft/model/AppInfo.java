package ts.app.sagosoft.com.libcraft.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by FRED_angejia on 2016/1/13.
 */
public class AppInfo {

    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField
    private String appName;

    @DatabaseField
    private String appPackage;
    /**
     * 应用再次激活时间(+span 就是最后活跃时间)
     */
    @DatabaseField
    private long startTime;

    @DatabaseField
    private long span;

    @DatabaseField
    private boolean isInstalled = true;

//    private Drawable appIcon;
//    private Intent appIntent;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setIsInstalled(boolean isInstalled) {
        this.isInstalled = isInstalled;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int count;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSpan() {
        return span;
    }

    public void setSpan(long span) {
        this.span = span;
    }


    @Override
    public String toString() {
        return "AppInfo{" +
                "_id=" + _id +
                ", appName='" + appName + '\'' +
                ", appPackage='" + appPackage + '\'' +
                ", startTime=" + startTime +
                ", span=" + span +
                ", isInstalled=" + isInstalled +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfo)) return false;
        AppInfo appInfo = (AppInfo) o;
        return getAppPackage().equals(appInfo.getAppPackage());
    }

    /**
     * 只要包名相同则认为是同一款app
     * @return
     */
    @Override
    public int hashCode() {
        return getAppPackage().hashCode();
    }


    /*@Override
    public String toString() {
        return "应用名称：" + appName +
                ", 包名：'" + appPackage + "\r\n";
    }*/
}
