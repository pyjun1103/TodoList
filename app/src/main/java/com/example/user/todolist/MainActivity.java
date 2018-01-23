package com.example.user.todolist;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.todolist.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText txt_memo;
    List<MemoVO> memos; //ArrayList<MemoVO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_memo = findViewById(R.id.txt_memo);
        RecyclerView memo_list = findViewById(R.id.memo_list);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        memos = dbHelper.getAllList();

        final RecyclerView.Adapter memoAdapter = new MemoAdapter(memos);
        RecyclerView.LayoutManager memoLayoutManager = new LinearLayoutManager(getApplicationContext());

        memo_list.setAdapter(memoAdapter);
        memo_list.setLayoutManager(memoLayoutManager);

        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(getApplicationContext(),
                    new LinearLayoutManager(this).getOrientation());

        memo_list.addItemDecoration(dividerItemDecoration);
        memo_list.addItemDecoration(new VerticalSpace(40));

        ItemTouchHelper.SimpleCallback simpleCallback
                 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                long id = memos.get(position).getId();
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.delete(id);
                memos.remove(position);
                memoAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(memo_list);
        ImageButton btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMemo = txt_memo.getText().toString();
                if(strMemo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "메모 입력", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm : ss");

                    String getDate = simpleDateFormat.format(date);
                    String getTime = simpleTimeFormat.format(date);

                DBHelper dbHelper = new DBHelper(getApplicationContext());
                MemoVO vo = new MemoVO(getDate,getTime,strMemo);
                dbHelper.saveMemo(vo);
                memos = dbHelper.getAllList();
//                    memos.add(new MemoVO(getDate,getTime,strMemo));
                    memoAdapter.notifyDataSetChanged();
                    txt_memo.setText("");
                }
            });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public List<MemoVO> getMemos() {
        List<MemoVO> memos = new ArrayList<MemoVO>();
        memos.add(new MemoVO("2018-01-01", "10:11:12", "새해 첫날"));
        memos.add(new MemoVO("2018-01-01", "10:11:13", "새해 첫날"));
        memos.add(new MemoVO("2018-01-01", "10:11:14", "새해 첫날"));
        memos.add(new MemoVO("2018-01-01", "10:11:15", "새해 첫날"));
        memos.add(new MemoVO("2018-01-01", "10:11:16", "새해 첫날"));

        return memos;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
