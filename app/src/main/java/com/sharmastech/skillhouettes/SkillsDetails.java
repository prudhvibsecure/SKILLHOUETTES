package com.sharmastech.skillhouettes;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.Animations.CircleTransform;
import com.sharmastech.skillhouettes.adapters.FeaturesAdapter;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by w7u on 1/11/2017.
 */

public class SkillsDetails extends AppCompatActivity implements View.OnClickListener, IItemHandler {

    private ImageLoader imageLoader;
    private ImageView st_image, st_footer;

    private RecyclerView mRecyclerView, horizontal_recycler_view;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private FeaturesAdapter adapter;

    private String student_id, featureid = null, status = null;

    private Dialog rDialog;

    private RadioGroup radioGroup = null;

    private String statuscont=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_skillsdetails);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        mRecyclerView.setNestedScrollingEnabled(true);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new FeaturesAdapter(this);
        adapter.setOnClickListener(this);

        mRecyclerView.setAdapter(adapter);

        st_image = (ImageView) findViewById(R.id.iv_studentimage);
        st_footer = (ImageView) findViewById(R.id.footer);

        imageLoader = new ImageLoader(this, true);
        try {
            //findViewById(R.id.pb_content_bar).setVisibility(View.GONE);
            String data = getIntent().getStringExtra("studentData");
            int position = getIntent().getIntExtra("position", 0);
            student_id = getIntent().getStringExtra("student_id");

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                JSONArray skillsobject = jsonObject.getJSONArray("features");
                for (int j = 0; j < skillsobject.length(); j++) {

                    adapter.setItems(skillsobject);
                    adapter.notifyDataSetChanged();

                }
                if (i == position) {

                    ((TextView) findViewById(R.id.tv_studenttitle)).setText(jsonObject.optString("skillname"));
                    ((TextView) findViewById(R.id.tv_favouriteband)).setText(Html.fromHtml("Favorite Band :" + " " + jsonObject.optString("band")));
                    ((TextView) findViewById(R.id.tv_studenthobies)).setText(Html.fromHtml("Hobbies :" + " " + jsonObject.optString("hobbies")));
                    ((TextView) findViewById(R.id.tv_studentdesc)).setText(Html.fromHtml("Description :" + " " + jsonObject.optString("description")));
                    ((TextView) findViewById(R.id.tv_studentname)).setText(Html.fromHtml("Name :" + jsonObject.optString("skillname")));
                    String image_url = AppSettings.getInstance(this).getPropertyValue("i_path");
                    String st_ad_img = image_url + jsonObject.optString("skillpicture");

                    String st_ad_footer = image_url + jsonObject.optString("expectedskillimage");
                    imageLoader.DisplayImage(Utils.urlEncode(st_ad_footer), st_footer, 2);

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
        final int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        String data = getIntent().getStringExtra("studentData");

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray skillsobject = jsonObject.getJSONArray("features");
                for (int j = 0; j < skillsobject.length(); j++) {
                    JSONObject jsonObject2 = skillsobject.getJSONObject(j);
                    if (j == itemPosition) {
                        featureid = jsonObject2.optString("featureid");
                        status = jsonObject2.optString("status");

                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rDialog = new Dialog(this);
        rDialog.setContentView(R.layout.notification);


        TextView title = (TextView) rDialog.findViewById(R.id.txt_notification);
        title.setText("Update Future status");
        Button submit = (Button) rDialog.findViewById(R.id.notify_sub);
        if ("Yes".equalsIgnoreCase(status)) {
            ((RadioButton) rDialog.findViewById(R.id.notify_yes)).setChecked(true);
        } else {
            ((RadioButton) rDialog.findViewById(R.id.notify_no)).setChecked(true);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateStatus(itemPosition, student_id, featureid, status);
            }

        });

        rDialog.show();
    }

    private void upDateStatus(int itemPosition, String student_id, String featureid, String status) {
        try {

            RadioGroup rg = (RadioGroup) rDialog.findViewById(R.id.notify_grp);
            String content = ((RadioButton) rDialog.findViewById(rg.getCheckedRadioButtonId())).getText()
                    .toString();
            String url = AppSettings.getInstance(this).getPropertyValue("update_status");
            JSONObject object = new JSONObject();
            object.put("studentid", student_id);
            object.put("featureid", featureid);
            object.put("status", content);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);
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
                    JSONObject jsonobj = new JSONObject(results.toString());
                    if (jsonobj.optString("status").equalsIgnoreCase("0")) {
                        showToast(jsonobj.optString("statusdescription"));
                        rDialog.cancel();
                        lauchActivity(Skill_Student.class,1000);
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
        showToast(errorCode);
    }

    private void lauchActivity(Class<?> cls,  int requestCode) {
        Intent mainIntent = new Intent(this, cls);
        startActivityForResult(mainIntent, requestCode);
        this.finish();
    }
}