package com.sharmastech.skillhouettes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.Animations.CircleTransform;
import com.sharmastech.skillhouettes.adapters.SkillsAdapter;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppPreferences;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.tasks.HTTPPostTask;
import com.sharmastech.skillhouettes.tasks.HTTPTask;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 1/25/2017.
 */

public class St_details extends AppCompatActivity implements View.OnClickListener, IItemHandler {

    private ImageLoader imageLoader;
    private ImageView st_image;

    private RecyclerView mRecyclerView;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private SkillsAdapter adapter;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentsskills);

        findViewById(R.id.fab).setVisibility(View.GONE);

        try {
            findViewById(R.id.pb_content_bar).setVisibility(View.GONE);

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
            mRecyclerView.setNestedScrollingEnabled(true);

            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());


            adapter = new SkillsAdapter(this);
            adapter.setOnClickListener(this);
            mRecyclerView.setAdapter(adapter);


            st_image = (ImageView) findViewById(R.id.iv_studentimage);
            imageLoader = new ImageLoader(this, true);
            email = getIntent().getStringExtra("emailid");
            String lname = getIntent().getStringExtra("lname");
            String fname = getIntent().getStringExtra("fname");
            String username = getIntent().getStringExtra("username");
            String mobile = getIntent().getStringExtra("mobile");
            password = getIntent().getStringExtra("password");
            String simage = getIntent().getStringExtra("simage");


            ((TextView) findViewById(R.id.tv_studenttitle)).setText(fname);
            ((TextView) findViewById(R.id.tv_favouriteband)).setText(Html.fromHtml("Email Address :" + " " + email));
            ((TextView) findViewById(R.id.tv_studenthobies)).setText(Html.fromHtml("Mobile Number :" + " " + mobile));
            ((TextView) findViewById(R.id.tv_studentname)).setText(Html.fromHtml("Name :" + " " + fname + " " + lname));
            String image_url = AppSettings.getInstance(this).getPropertyValue("i_path");
            String st_ad_img = image_url + simage;
            getData();
            Picasso.with(this).load(st_ad_img).transform(new CircleTransform()).error(R.drawable.background).into(st_image);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        try {
            String url = AppSettings.getInstance(this).getPropertyValue("student_login");
            JSONObject object = new JSONObject();
            object.put("username", email);
            object.put("password", password);
            object.put("regid", AppPreferences.getInstance(this).getFromStore("regID"));
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        lauchActivity(SingleStudentSkill.class, adapter.getItems(), itemPosition);
    }

    private void lauchActivity(Class<?> cls, JSONArray studentData, int position) {

        String student_id=getFromStore("studentid");
        Intent studentdata = new Intent(this, cls);
        studentdata.putExtra("position", position);
        studentdata.putExtra("studentData", studentData.toString());
        studentdata.putExtra("student_id", student_id);
        startActivity(studentdata);
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
                            JSONArray jsonarray2 = jsonobj.getJSONArray("student_detail");
                            for (int i = 0; i < jsonarray2.length(); i++) {
                                JSONObject jsonObject = jsonarray2.getJSONObject(i);
                                JSONArray skillsobject = jsonObject.getJSONArray("skills");
                                for (int j = 0; j < skillsobject.length(); j++) {
                                    adapter.setItems(skillsobject);
                                    adapter.notifyDataSetChanged();
                                    return;
                                }

                            }
                            return;
                        }
                        if (jsonobj.optString("status").equalsIgnoreCase("1")) {
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

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(this,LandingPage.class);
        startActivity(back);
    }
}
