package com.zhanghai.dbflowapp.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhanghai on 2018/07/07.
 */

public class FileDatabaseContext extends ContextWrapper {


    public FileDatabaseContext(Context context) {
        super(context);
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }


    @Override
    public File getDatabasePath(String name) {

        Log.i("zhh_file", "name---" + name);

        //判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {//如果不存在,
            Log.e("zhh_sd：", "SD卡不存在，请加载SD卡");
            return null;
        } else {//如果存在
            ////获取sd卡路径
            String dbDir = Environment.getExternalStorageDirectory().getPath() + "/" + "bigsea";
            String dbPath = dbDir + "/" + name;//数据库路径
            //判断目录是否存在，不存在则创建该目录
            File dirFile = new File(dbDir);
            if (!dirFile.exists())
                dirFile.mkdirs();
            //数据库文件是否创建成功
            boolean isFileCreateSuccess = false;
            //判断文件是否存在，不存在则创建该文件
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try {
                    isFileCreateSuccess = dbFile.createNewFile();//创建文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                isFileCreateSuccess = true;
            //返回数据库文件对象
            if (isFileCreateSuccess)
                return dbFile;
            else
                return getApplicationContext().getDatabasePath(name);
        }


    }


    //重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    //Android 4.0会调用此方法获取数据库。
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}
