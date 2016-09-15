package org.ghost;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

/**
 * 待練習議題
 * <p>
 * ArrayAdapter 複寫getView() 使用自己的layout
 * ArrayAdapter 使用ArrayList
 * ArrayAdapter 使用靜態Array資源
 * <p>
 * SimpleCursorAdapter
 * SimpleCursorAdapter 使用CurosrLoader
 * <p>
 * SimpleAdapter
 * <p>
 * Fregment FregmentActivity
 * <p>
 * Service
 * IntentService
 * <p>
 * Camera Camera2
 * Google map
 * QR Code
 * <p>
 * GridView
 * ScrollView
 * HorizontalScrollView
 * SearchView
 * RecycleView ViewHolder
 * <p>
 * FireBase Cloud Messaging
 */
public class AdapterTestActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    private ListView listView;
    private Button createData;
    private SQLiteDatabase db;
    //    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_test);

//        Calendar now = Calendar.getInstance();
//        Log.d("DATE", now.getTime().toString());
//        now.add(Calendar.DAY_OF_YEAR, 3);
//        Log.d("DATE", now.getTime().toString());

        findViews();

        File file0 = Environment.getDataDirectory();
        File file1 = Environment.getDownloadCacheDirectory();
        File file2 = Environment.getExternalStorageDirectory();
        File file3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File file4 = Environment.getRootDirectory();
        File file6 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
/*
        09-03 16:34:21.241 17275-17275/com.finance.ghost D/file0: /data
        09-03 16:34:21.241 17275-17275/com.finance.ghost D/file1: /cache
        09-03 16:34:21.241 17275-17275/com.finance.ghost D/file2: /storage/emulated/0
        09-03 16:34:21.241 17275-17275/com.finance.ghost D/file3: /storage/emulated/0/Music
        09-03 16:34:21.241 17275-17275/com.finance.ghost D/file4: /system
        09-03 16:41:44.081 20982-20982/com.finance.ghost D/file6: /storage/emulated/0/Pictures
        09-03 16:36:49.141 19741-19741/com.finance.ghost D/externalStorageState: mounted
*/
        String externalStorageState = Environment.getExternalStorageState();
        boolean state = externalStorageState.equals(Environment.MEDIA_MOUNTED);

        Log.d("file0", file0.getAbsolutePath());
        Log.d("file1", file1.getAbsolutePath());
        Log.d("file2", file2.getAbsolutePath());
        Log.d("file3", file3.getAbsolutePath());
        Log.d("file4", file4.getAbsolutePath());
        Log.d("file6", file6.getAbsolutePath());

        Log.d("externalStorageState", externalStorageState);


        Calendar cal = Calendar.getInstance();
        // 設定於 3 分鐘後執行
        cal.add(Calendar.MINUTE, 3);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "play_hskay");

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);



        /*
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2) {

            @Override
            public int getCount() {
                return getResources().getStringArray(R.array.fruitNames).length;
            }

            @Override
            public Object getItem(int position) {
                return getResources().getStringArray(R.array.fruitNamesDesc)[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View resultView = null;

                if (convertView == null) {
                    Resources resources = getResources();
                    String[] fruitNames = resources.getStringArray(R.array.fruitNames);
                    String[] fruitNamesDesc = resources.getStringArray(R.array.fruitNamesDesc);

                    resultView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);

                    TextView text1 = (TextView) resultView.findViewById(android.R.id.text1);
                    text1.setText(fruitNames[position]);
                    TextView text2 = (TextView) resultView.findViewById(android.R.id.text2);
                    text2.setText(fruitNamesDesc[position]);
                } else {
                    resultView = convertView;
                }
                return resultView;
            }
        };
        */

//        db = openOrCreateDatabase(getString(R.string.dbName), Context.MODE_PRIVATE, null, null);
//        db.execSQL(getString(R.string.createTableSql));

        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 5);


//        setContentView(R.layout.my);
//        WebView mWebView = null;
//        mWebView = (WebView) findViewById(R.id.webview);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl("file:///android_asset/new.html");


    }

    private void findViews() {
//        createData = (Button) findViewById(R.id.createData);
//        createData.setOnClickListener(this::createData);
//
//        listView = (ListView) findViewById(R.id.listView);
//        listView.setOnItemClickListener(this::listOnItemClick);

//        gridView = (GridView) findViewById(R.id.gridView);
    }

    private void createData(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "阿鬼");
        contentValues.put("age", 35);
        contentValues.put("score", 98.5);

        long rowId = db.insert(getString(R.string.tableName), null, contentValues);
        Log.d("rowId", String.valueOf(rowId));

        Cursor cursor = db.query(getString(R.string.tableName), null, null, null, null, null, null);

        //test
//        String[] columnNames = cursor.getColumnNames();
//        for(String name:columnNames){
//            Log.d("columnNames == == == > ", name);
//        }

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.adapter_embedding
                , cursor, new String[]{"name", "age", "score", "id"}, new int[]{R.id.name, R.id.age, R.id.score, R.id.idValue}, 0) {

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                TextView textViewName = (TextView) view.findViewById(R.id.name);
                TextView textViewAge = (TextView) view.findViewById(R.id.age);
                TextView textViewScore = (TextView) view.findViewById(R.id.score);
                TextView textViewIdValue = (TextView) view.findViewById(R.id.idValue);

                TextView textViewFieldName = (TextView) view.findViewById(R.id.fieldName);
                textViewFieldName.setText("識別碼 : ");

                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                float score = cursor.getFloat(cursor.getColumnIndexOrThrow("score"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                textViewName.setText(name);
                textViewAge.setText(String.valueOf(age));
                textViewScore.setText(String.valueOf(score));
                textViewIdValue.setText(String.valueOf(id));
            }
        };
        listView.setAdapter(simpleCursorAdapter);
    }

    private void listOnItemClick(AdapterView<?> parent, View view, int postion, long rowId) {

        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        String name = text1.getText().toString();

        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        String desc = text2.getText().toString();

        new AlertDialog.Builder(this)
                .setTitle(name)
                .setMessage(desc)
                .setPositiveButton(android.R.string.ok, this)
                .show();

//        Snackbar snackbar = Snackbar.make(view, "你選的是" + text1, Snackbar.LENGTH_SHORT);
//        snackbar.setAction("看進一步描述", );
    }

    private void onPositiveButtonClick(DialogInterface dialog, int whichButtonClicked) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        Log.d("Dialog which --- ", String.valueOf(which));

        Toast.makeText(this, "你按了確定", Toast.LENGTH_SHORT).show();
    }

//    class MyBaseAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return null;
//        }
//    }
}

/*
class PerfilInfoAdapter extends BaseAdapter {

    public static final int VIEW_TYPE_TITULO = 0;
    public static final int VIEW_TYPE_DESCRICAO = 1;
    public static final int VIEW_TYPE_KEY_VALUE = 2;

    private JSONArray list;
    private Activity activity;
    private ViewHolder viewHolder;

    public PerfilInfoAdapter(Activity activity, JSONArray list) {
        this.activity = activity;
        this.list = list;
    }

    protected class ViewHolder {
        TextView textViewTitulo;
        TextView textViewDescricao;
        TextView textViewKey;
        TextView textViewValue;
    }

    @Override
    public int getCount() {
        Log.d("PerfilInfoAdapter", "Number of items in array: " + Integer.toString(this.list.length()));
        return this.list.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject json = null;

        try {
            json = this.list.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        int retorno = -1;

        JSONObject json = null;

        try {
            json = this.list.getJSONObject(position);

            if (json.getString("key").equals("Titulo")) {
                retorno = VIEW_TYPE_TITULO;
            } else if (json.getString("key").equals("Descrição")
                    || json.getString("key").equals("Sou")) {
                retorno = VIEW_TYPE_DESCRICAO;
            } else {
                retorno = VIEW_TYPE_KEY_VALUE;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View container, ViewGroup viewGroup) {
        System.out.println("getView " + position + " " + container);

        this.viewHolder = null;

        int type = this.getItemViewType(position);

        if (container == null) {
            this.viewHolder = new ViewHolder();

            switch (type) {
                case VIEW_TYPE_TITULO:
                    container = this.activity.getLayoutInflater().inflate(
                            R.layout.perfil_info_full_titulo, viewGroup, false);

                    this.viewHolder.textViewTitulo = (TextView) container
                            .findViewById(R.id.perfil_info_full_textViewTitulo);
                    break;
                case VIEW_TYPE_DESCRICAO:
                    container = this.activity.getLayoutInflater().inflate(
                            R.layout.perfil_info_full_descricao, viewGroup, false);

                    this.viewHolder.textViewDescricao = (TextView) container
                            .findViewById(R.id.perfil_info_full_textVewDescricao);
                    break;
                case VIEW_TYPE_KEY_VALUE:
                    container = this.activity.getLayoutInflater().inflate(
                            R.layout.perfil_info_list, viewGroup, false);

                    this.viewHolder.textViewKey = (TextView) container
                            .findViewById(R.id.perfil_info_full_chave_valor_textFieldChave);
                    this.viewHolder.textViewValue = (TextView) container
                            .findViewById(R.id.perfil_info_full_chave_valor_textFieldValor);
                    break;
            }

            container.setTag(this.viewHolder);

        } else {
            this.viewHolder = (ViewHolder)container.getTag();
        }

        try {
            JSONObject json = this.list.getJSONObject(position);

            switch (type) {
                case VIEW_TYPE_TITULO:
                    this.viewHolder.textViewTitulo.setText(json.getString("value"));
                    break;
                case VIEW_TYPE_DESCRICAO:
                    this.viewHolder.textViewDescricao.setText(json
                            .getString("value"));
                    break;
                case VIEW_TYPE_KEY_VALUE:
                    this.viewHolder.textViewKey.setText(json.getString("key"));
                    this.viewHolder.textViewValue.setText(json.getString("value"));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return container;
    }

}
*/
