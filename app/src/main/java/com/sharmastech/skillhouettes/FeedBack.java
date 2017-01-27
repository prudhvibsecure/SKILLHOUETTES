package com.sharmastech.skillhouettes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.adapters.StudentsAdapter;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by w7u on 1/10/2017.
 */

public class FeedBack extends AppCompatActivity implements IItemHandler, View.OnClickListener {

    private RecyclerView mRecyclerView;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private StudentsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_skills);

        findViewById(R.id.fab).setVisibility(View.GONE);
        mSwipeRefreshLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new StudentsAdapter(this);
        adapter.setOnClickListener(this);

        mRecyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        try{

            String url= AppSettings.getInstance(this).getPropertyValue("student_skill");
            JSONObject object=new JSONObject();
            object.put("teacherid",getFromStore("teacherid"));
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {

        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        lauchActivity(ZoomImageStudent.class, adapter.getItems(), itemPosition);

    }
    private void lauchActivity(Class<?> cls, JSONArray studentData, int position) {
        Intent studentdata = new Intent(this, cls);
        studentdata.putExtra("position", position);
        studentdata.putExtra("studentData", studentData.toString());
        startActivity(studentdata);
    }
    @Override
    public void onFinish(Object results, int requestId) {


        try {
            switch (requestId) {
                case 1:
                    findViewById(R.id.pb_content_bar).setVisibility(View.GONE);
                    if (results != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                        JSONObject jsonobj = new JSONObject(results.toString());
                        if (jsonobj.optString("status").equalsIgnoreCase("0")) {
                            JSONArray jsonarray2 = jsonobj.getJSONArray("student_details");

                            if (jsonarray2.length() > 0) {

                                adapter.setItems(jsonarray2);
                                adapter.notifyDataSetChanged();

                                return;
                            }

                            return;
                        }
                        if (jsonobj.optString("status").equalsIgnoreCase("1")){
                            showToast("No Data Found");
                            findViewById(R.id.tv_content_txt).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.tv_content_txt)).setText("No Data Found");
                            return;
                        }

                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String errorCode, int requestId) {
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        showToast(errorCode);
    }
    public String getFromStore(String key) {
        return getSharedPreferences("Skills", 0).getString(key, "");
    }
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}