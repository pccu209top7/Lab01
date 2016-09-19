package org.ghost.study.android;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by StanleyCheng on 2016/9/13.
 */
public class MyMapDBHelper extends SQLiteOpenHelper {

    private Context context;

    private static MyMapDBHelper myMapDBHelper;

    private MyMapDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
    }

    public static MyMapDBHelper getInstance(Context context) {
        if (myMapDBHelper == null) {
            myMapDBHelper = new MyMapDBHelper(context, "myMap.db", null, 1);
        }
        return myMapDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(context.getString(R.string.createTableGoal));

        /*
        測試資料
         */
        Resources resources = context.getResources();
        String[] goals = resources.getStringArray(R.array.goals);

        for (String goal : goals) {

            ContentValues goalStrToDB = new ContentValues();
            goalStrToDB.put("goal", goal);

            long rowId = db.insert(context.getString(R.string.tableNameGoal), null, goalStrToDB);

            //test-
            Log.d("init table rowId = ", String.valueOf(rowId));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
