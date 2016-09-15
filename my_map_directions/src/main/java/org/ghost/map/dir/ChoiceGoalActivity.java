package org.ghost.map.dir;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChoiceGoalActivity extends AppCompatActivity implements OnItemClickListener {

    private ListView goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_goal);

        findViews();

        SQLiteDatabase db = MyMapDBHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery(getString(R.string.findAllGoals), null);

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"goal"},
                new int[]{android.R.id.text1},
                0);

        goalList.setAdapter(simpleCursorAdapter);
    }

    private void findViews() {
        goalList = (ListView) findViewById(R.id.goalList);
        goalList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView item = (TextView) view;
        String btnTxt = item.getText().toString();

        Intent intent = getIntent();
        intent.putExtra(MapsActivity.RESULT_KEY_CHOICE_GOAL, btnTxt);
        setResult(MapsActivity.REQUEST_CODE_CHOICE_GOAL, intent);

        finish();
    }
}
