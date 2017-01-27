package com.sharmastech.skillhouettes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.adapters.SkillsAdapter;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.tasks.HTTPPostTask;
import com.sharmastech.skillhouettes.tasks.HTTPTask;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by w7u on 1/5/2017.
 */

public class Skill_houttes_home extends AppCompatActivity implements IItemHandler, View.OnClickListener {

    private RecyclerView mRecyclerView;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private SkillsAdapter adapter;

    public Dialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_skills);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CustomDialog();
//            }
//        });
        String username = getFromStore("username");
        if (username.length()!= 0) {

            findViewById(R.id.fab).setOnClickListener(onClick);
        }else {
            findViewById(R.id.fab).setVisibility(View.GONE);
        }
        mSwipeRefreshLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new SkillsAdapter(this);
        adapter.setOnClickListener(this);

        mRecyclerView.setAdapter(adapter);

        getData();
    }

    public void CustomDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        dialog.setCanceledOnTouchOutside(true);

        dialog.findViewById(R.id.add_student).setOnClickListener(onClick);
        dialog.findViewById(R.id.add_student_skill).setOnClickListener(onClick);
        dialog.findViewById(R.id.add_student_contact).setOnClickListener(onClick);

        dialog.show();

    }

    private void getData() {
        try {
            String url = AppSettings.getInstance(this).getPropertyValue("skill_detail");
            HTTPTask get = new HTTPTask(this, this);
            get.userRequest(getString(R.string.pwait), 1, url);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFinish(Object results, int requestId) {

        Utils.dismissProgress();
        try {
            switch (requestId) {
                case 1:
                    if (results != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                        JSONObject jsonobj = new JSONObject(results.toString());
                        if (jsonobj.optString("status").equalsIgnoreCase("0")) {
                            JSONArray jsonarray2 = jsonobj.getJSONArray("skills_detail");

                            if (jsonarray2.length() > 0) {

                                adapter.setItems(jsonarray2);
                                adapter.notifyDataSetChanged();

                                return;
                            }

                            return;
                        }
                        if (jsonobj.optString("status").equalsIgnoreCase("1")) {
                            showToast("No Data Found");
                            findViewById(R.id.tv_content_txt).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.tv_content_txt)).setText("No Data Found");
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
    public void onError(String errorCode, int requestType) {
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        showToast(errorCode);

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {


        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        RelauchActivity(Video_Play_Skill.class, adapter.getItems(), itemPosition);


    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.add_student:
                    dialog.dismiss();
                    launchActivity(StudentRegister.class, 1000);
                    break;

                case R.id.add_student_skill:
                    dialog.dismiss();
                    launchActivity(Skill_Student.class, 1000);
                    break;
                case R.id.add_student_contact:
                    dialog.dismiss();
                    launchActivity(FeedBack.class, 1001);
                    break;

                case R.id.fab:
                    CustomDialog();
                    break;
                default:
                    break;
            }

        }
    };

    public String getFromStore(String key) {
        return getSharedPreferences("Skills", 0).getString(key, "");
    }

    private void launchActivity(Class<?> cls, int requestCode) {
        Intent mainIntent = new Intent(Skill_houttes_home.this, cls);
        startActivityForResult(mainIntent, requestCode);
    }

    private void RelauchActivity(Class<?> cls, JSONArray videoData, int position) {
        Intent studentdata = new Intent(this, cls);
        studentdata.putExtra("position", position);
        studentdata.putExtra("videoData", videoData.toString());
        startActivity(studentdata);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchActivity(LandingPage.class, 1001);
    }
}
