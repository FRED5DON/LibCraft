package ts.app.sagosoft.com.libcraft.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ts.app.sagosoft.com.libcraft.model.AppInfo;

/**
 * Created by FRED_angejia on 2016/1/20.
 */
public class AppInfoHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "app3rd.db";
    private static final int DATABASE_VERSION = 2;
    private static AppInfoHelper helper;
    private Dao<AppInfo, Long> logDao;

    public AppInfoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized AppInfoHelper getHelper(Context context) {
        if (helper == null) {
            synchronized (AppInfoHelper.class) {
                helper = new AppInfoHelper(context);
            }
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        //初始化建表
        try {
            TableUtils.createTableIfNotExists(connectionSource, AppInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            if (oldVersion == 1) {
                TableUtils.dropTable(connectionSource, AppInfo.class, true);
                TableUtils.createTableIfNotExists(connectionSource, AppInfo.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<AppInfo, Long> getAppInfoDao() throws SQLException {
        if (logDao == null) {
            logDao = getDao(AppInfo.class);
        }
        return logDao;
    }

}
