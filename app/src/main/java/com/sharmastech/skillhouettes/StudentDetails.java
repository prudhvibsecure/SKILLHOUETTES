package com.sharmastech.skillhouettes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharmastech.skillhouettes.Animations.CircleTransform;
import com.sharmastech.skillhouettes.adapters.FooterAdapter;
import com.sharmastech.skillhouettes.adapters.SkillsAdapter;
import com.sharmastech.skillhouettes.adapters.StudentsAdapter;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.common.Item;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by w7u on 1/6/2017.
 */

public class StudentDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageLoader imageLoader;
    private ImageView st_image;

    private RecyclerView mRecyclerView;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private SkillsAdapter adapter;

    private String student_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentsskills);

        findViewById(R.id.fab).setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        mRecyclerView.setNestedScrollingEnabled(true);


        mSwipeRefreshLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new SkillsAdapter(this);
        adapter.setOnClickListener(this);


        mRecyclerView.setAdapter(adapter);


        st_image = (ImageView) findViewById(R.id.iv_studentimage);


        imageLoader = new ImageLoader(this, true);
        try {
            findViewById(R.id.pb_content_bar).setVisibility(View.GONE);
            String data = getIntent().getStringExtra("studentData");
            int position = getIntent().getIntExtra("position", 0);

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray skillsobject = jsonObject.getJSONArray("skills");
                for (int j = 0; j < skillsobject.length(); j++) {
                    adapter.setItems(skillsobject);
                    adapter.notifyDataSetChanged();

                }

                if (i == position) {
                    ((TextView) findViewById(R.id.tv_studenttitle)).setText(jsonObject.optString("firstname"));
                    ((TextView) findViewById(R.id.tv_favouriteband)).setText(Html.fromHtml("Email Address :" + " " + jsonObject.optString("email")));
                    ((TextView) findViewById(R.id.tv_studenthobies)).setText(Html.fromHtml("Mobile Number :" + " " + jsonObject.optString("phoneno")));
                    ((TextView) findViewById(R.id.tv_studentname)).setText(Html.fromHtml("Name :" + " " + jsonObject.optString("firstname") + " " + jsonObject.optString("lastname")));
                    String image_url = AppSettings.getInstance(this).getPropertyValue("i_path");
                    String st_ad_img = image_url + jsonObject.optString("simage");
                    student_id = jsonObject.optString("studentid");
                    Picasso.with(this).load(st_ad_img).transform(new CircleTransform()).error(R.drawable.background).into(st_image);
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        lauchActivity(SkillsDetails.class, adapter.getItems(), itemPosition);
    }

    private void lauchActivity(Class<?> cls, JSONArray studentData, int position) {
        Intent studentdata = new Intent(this, cls);
        studentdata.putExtra("student_id", student_id);
        studentdata.putExtra("position", position);
        studentdata.putExtra("studentData", studentData.toString());
        studentdata.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(studentdata);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent lanch = new Intent(this, Skill_Student.class);
        startActivity(lanch);
    }
}
