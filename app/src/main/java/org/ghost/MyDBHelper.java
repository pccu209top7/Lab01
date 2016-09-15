package org.ghost;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by StanleyCheng on 2016/9/6.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private Context context;

    public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(context.getString(R.string.createTableTesterSql));

        for(int i=0; i<20; i++){

            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 9, i, 12, 55, 40);


            ContentValues contentValues = new ContentValues();
            contentValues.put("name", context.getString(R.string.ghost)+i);
            contentValues.put("age", i);
            contentValues.put("tm", calendar.getTimeInMillis());


            long rowId = db.insert("tester", null, contentValues);
            Log.v("rowId ====", String.valueOf(rowId));
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
