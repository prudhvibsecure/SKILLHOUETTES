package com.sharmastech.skillhouettes;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by w7u on 1/11/2017.
 */

public class ZoomImageStudent extends AppCompatActivity implements IItemHandler, OnTouchListener, OnDragListener {
    private static final String LOGCAT = null;
    private ImageLoader imageLoader;
    private ImageView st_image;
    private TextView effort, attention, respect, spirit, effort1, attention1, respect1, spirit1, effort2, attention2, respect2, spirit2;
    private EditText comments_name = null;

    private String tag = " ", student_id;

    private LinearLayout container;
    private View view;
    Dialog folderDialog;

    private String effrot_st = null, attention_st = null, respect_st = null, spirit_st = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_layout_feed);
        imageLoader = new ImageLoader(this, true);
        st_image = (ImageView) findViewById(R.id.stu_image);

//        findViewById(R.id.tv_er).setOnLongClickListener(this);
//        findViewById(R.id.tv_as).setOnLongClickListener(this);
//        findViewById(R.id.tv_rs).setOnLongClickListener(this);
//        findViewById(R.id.tv_ss).setOnLongClickListener(this);

        effort = (TextView) findViewById(R.id.tv_er);
        attention = (TextView) findViewById(R.id.tv_as);
        respect = (TextView) findViewById(R.id.tv_rs);
        spirit = (TextView) findViewById(R.id.tv_ss);

        effort1 = (TextView) findViewById(R.id.tv_er_1);
        attention1 = (TextView) findViewById(R.id.tv_as_1);
        respect1 = (TextView) findViewById(R.id.tv_rs_1);
        spirit1 = (TextView) findViewById(R.id.tv_ss_1);

        effort2 = (TextView) findViewById(R.id.tv_er_2);
        attention2 = (TextView) findViewById(R.id.tv_as_2);
        respect2 = (TextView) findViewById(R.id.tv_rs_2);
        spirit2 = (TextView) findViewById(R.id.tv_ss_2);

        effort.setOnTouchListener(this);
        attention.setOnTouchListener(this);
        respect.setOnTouchListener(this);
        spirit.setOnTouchListener(this);

        effort1.setOnDragListener(this);
        attention1.setOnDragListener(this);
        respect1.setOnDragListener(this);
        spirit1.setOnDragListener(this);

        effort2.setOnDragListener(this);
        attention2.setOnDragListener(this);
        respect2.setOnDragListener(this);
        spirit2.setOnDragListener(this);


//        findViewById(R.id.ll_fram1).setOnDragListener(this);
//        findViewById(R.id.ll_fram2).setOnDragListener(this);


        try {
            String data = getIntent().getStringExtra("studentData");
            int position = getIntent().getIntExtra("position", 0);

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == position) {
                    student_id = jsonObject.optString("studentid");
                    String image_url = AppSettings.getInstance(this).getPropertyValue("i_path");
                    String st_ad_img = image_url + jsonObject.optString("simage");
                    imageLoader.DisplayImage(st_ad_img, st_image, 2);
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//            view.startDrag(null, shadowBuilder, view, 0);
//            view.setVisibility(View.VISIBLE);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean onDrag(View layoutview, DragEvent dragevent) {
//        int action = dragevent.getAction();
//        switch (action) {
//            case DragEvent.ACTION_DRAG_STARTED:
//                Log.d(LOGCAT, "Drag event started");
//                break;
//            case DragEvent.ACTION_DRAG_ENTERED:
//                Log.d(LOGCAT, "Drag event entered into" + layoutview.toString());
//                break;
//            case DragEvent.ACTION_DRAG_EXITED:
//                Log.d(LOGCAT, "Drag event exited from" + layoutview.toString());
//                break;
//            case DragEvent.ACTION_DROP:
//                Log.d(LOGCAT, "Dropped");
//                view = (View) dragevent.getLocalState();
//                ViewGroup owner = (ViewGroup) view.getParent();
//                owner.removeView(view);
//                container = (LinearLayout) layoutview;
//                container.addView(view);
//                view.setVisibility(View.VISIBLE);
//                if (container.getId() == R.id.ll_fram2) {
//                    view.setBackground(getResources().getDrawable(R.drawable.red_color));
//                    postYourPop();
//                }
//                if (container.getId() == R.id.ll_fram1) {
//                    view.setBackground(getResources().getDrawable(R.drawable.green_color));
//                    postYourPop();
//                }
//                break;
//            case DragEvent.ACTION_DRAG_ENDED:
//                Log.d(LOGCAT, "Drag ended");
//                if (container.getId() == R.id.ll_fram2) {
//                    tag = "Negative";
//                    switch (container.getId()){
//                        case R.id.tv_er:
//                            effrot_st = tag;
//                            break;
//                        case R.id.tv_as:
//                            attention_st = tag;
//                            break;
//                        case R.id.tv_ss:
//                            spirit_st = tag;
//                            break;
//                        case R.id.tv_rs:
//                            respect_st = tag;
//                            break;
//                        default:
//                            break;
//                    }
////                    if (effort.getText().equals("E")) {
////                        effrot_st = tag;
////                        break;
////                    }
////                    if (attention.getText().equals("A")) {
////                        attention_st = tag;
////                        break;
////                    }
////                    if (spirit.getText().equals("S")) {
////                        spirit_st = tag;
////                        break;
////                    }
////                    if (respect.getText().equals("R")) {
////                        respect_st = tag;
////                        break;
////                    }
//                    return true;
//                }
//                if (container.getId() == R.id.ll_fram1) {
//                    tag = "Postitive";
//                    switch (container.getId()){
//                        case R.id.tv_er:
//                            effrot_st = tag;
//                            break;
//                        case R.id.tv_as:
//                            attention_st = tag;
//                            break;
//                        case R.id.tv_ss:
//                            spirit_st = tag;
//                            break;
//                        case R.id.tv_rs:
//                            respect_st = tag;
//                            break;
//                        default:
//                            break;
//                    }
////                    if (effort.getText().equals("E")) {
////                        effrot_st = tag;
////                        break;
////                    }
////                    if (attention.getText().equals("A")) {
////                        attention_st = tag;
////                        break;
////                    }
////                    if (spirit.getText().equals("S")) {
////                        spirit_st = tag;
////                        break;
////                    }
////                    if (respect.getText().equals("R")) {
////                        respect_st = tag;
////                        break;
////                    }
//                    return true;
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            //handle the dragged view being dropped over a target view
            TextView dropped = (TextView) event.getLocalState();
            TextView dropTarget = (TextView) v;


            //stop displaying the view where it was before it was dragged
            dropped.setVisibility(View.INVISIBLE);

            //if an item has already been dropped here, there will be different string
            String drag_txt = dropTarget.getText().toString();
            //if there is already an item here, set it back visible in its original place

            if (v.getId() == R.id.tv_er_1) {
                if (drag_txt.equals(effort1.getText().toString())) {

                    effort1.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.green_color));
                    postYourPop();
                }
            }
            if (v.getId() == R.id.tv_as_1) {
                if (drag_txt.equals(attention1.getText().toString())) {
                    attention1.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.green_color));
                    postYourPop();
                }
            }
            if (v.getId() == R.id.tv_ss_1) {
                if (drag_txt.equals(spirit1.getText().toString())) {
                    spirit1.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.green_color));
                }
            }
            if (v.getId() == R.id.tv_rs_1) {
                if (drag_txt.equals(respect1.getText().toString())) {
                    respect1.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.green_color));
                    postYourPop();
                }

            }
            if (v.getId() == R.id.tv_er_2) {
                if (drag_txt.equals(effort2.getText().toString())) {
                    effort2.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.red_color));
                    postYourPop();
                }
            }
            if (v.getId() == R.id.tv_as_2) {
                if (drag_txt.equals(attention2.getText().toString())) {

                    attention2.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.red_color));
                    postYourPop();
                }
            }
            if (v.getId() == R.id.tv_ss_2) {
                if (drag_txt.equals(spirit1.getText().toString())) {
                    spirit2.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.red_color));
                    postYourPop();
                }
            }
            if (v.getId() == R.id.tv_rs_2) {
                if (drag_txt.equals(respect2.getText().toString())) {
                    respect2.setVisibility(View.VISIBLE);
                    dropTarget.setText(dropped.getText());
                    dropTarget.setBackground(getResources().getDrawable(R.drawable.red_color));
                    postYourPop();

                }

            }

        }
        return true;
    }

    //When text1 or text2 or text3 gets clicked or touched then this method will be called
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        } else return false;
    }

    private void postYourPop() {

        folderDialog = new Dialog(this);
        folderDialog.setContentView(R.layout.edit_st_comment);
        TextView title = (TextView) folderDialog.findViewById(R.id.txt_comment);
        title.setText("Feedback");
        TextView submit = (TextView) folderDialog.findViewById(R.id.bn_bu_submit);
        comments_name = (EditText) folderDialog.findViewById(R.id.cmttxt_comm);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCooment();
            }

        });

        folderDialog.show();
    }

    private void postCooment() {
        try {

            String ucomments = comments_name.getText().toString().trim();
            if (ucomments.length() == 0) {
                showToast("Give Your Comments");
                return;
            }

//positive comments
            String poseffort1 = effort1.getText().toString().trim();
            if (poseffort1.equals("E")) {
                poseffort1 = poseffort1.replace("E", "" + "Positive");
            }
            if (poseffort1.equals("S")) {
                poseffort1 = poseffort1.replace("S", "" + "Positive");
            }
            if (poseffort1.equals("A")) {
                poseffort1 = poseffort1.replace("A", "" + "Positive");
            }
            if (poseffort1.equals("R")) {
                poseffort1 = poseffort1.replace("R", "" + "Positive");
            }
            String posattention1 = attention1.getText().toString().trim();
            if (posattention1.equals("A")) {
                posattention1 = posattention1.replace("A", "" + "Positive");
            }
            if (posattention1.equals("S")) {
                posattention1 = posattention1.replace("S", "" + "Positive");
            }
            if (posattention1.equals("E")) {
                posattention1 = posattention1.replace("E", "" + "Positive");
            }
            if (posattention1.equals("R")) {
                posattention1 = posattention1.replace("R", "" + "Positive");
            }

            String posspirit1 = spirit1.getText().toString().trim();
            if (posspirit1.equals("S")) {
                posspirit1 = posspirit1.replace("S", "" + "Positive");
            }
            if (posspirit1.equals("A")) {
                posspirit1 = posspirit1.replace("A", "" + "Positive");
            }
            if (posspirit1.equals("E")) {
                posspirit1 = posspirit1.replace("E", "" + "Positive");
            }
            if (posspirit1.equals("R")) {
                posspirit1 = posspirit1.replace("R", "" + "Positive");
            }
            String posrespect1 = respect1.getText().toString().trim();
            if (posrespect1.equals("R")) {
                posrespect1 = posrespect1.replace("R", "" + "Positive");
            }
            if (posrespect1.equals("S")) {
                posrespect1 = posrespect1.replace("S", "" + "Positive");
            }
            if (posrespect1.equals("E")) {
                posrespect1 = posrespect1.replace("E", "" + "Positive");
            }
            if (posrespect1.equals("A")) {
                posrespect1 = posrespect1.replace("A", "" + "Positive");
            }
//negative comments

            String poseffort2 = effort2.getText().toString().trim();
            if (poseffort2.equals("E")) {
                poseffort2 = poseffort1.replace("E", "" + "Negative");
            }
            if (poseffort2.equals("S")) {
                poseffort2 = poseffort2.replace("S", "" + "Negative");
            }
            if (poseffort2.equals("A")) {
                poseffort2 = poseffort2.replace("A", "" + "Negative");
            }
            if (poseffort2.equals("R")) {
                poseffort2 = poseffort2.replace("R", "" + "Negative");
            }
            String posattention2 = attention2.getText().toString().trim();
            if (posattention2.equals("A")) {
                posattention2 = posattention2.replace("A", "" + "Negative");
            }
            if (posattention2.equals("S")) {
                posattention2 = posattention2.replace("S", "" + "Negative");
            }
            if (posattention2.equals("E")) {
                posattention2 = posattention2.replace("E", "" + "Negative");
            }
            if (posattention2.equals("R")) {
                posattention2 = posattention2.replace("R", "" + "Negative");
            }

            String posspirit2 = spirit2.getText().toString().trim();
            if (posspirit2.equals("S")) {
                posspirit2 = posspirit2.replace("S", "" + "Negative");
            }
            if (posspirit2.equals("A")) {
                posspirit2 = posspirit2.replace("A", "" + "Negative");
            }
            if (posspirit2.equals("E")) {
                posspirit2 = posspirit2.replace("E", "" + "Negative");
            }
            if (posspirit2.equals("R")) {
                posspirit2 = posspirit2.replace("R", "" + "Negative");
            }
            String posrespect2 = respect2.getText().toString().trim();
            if (posrespect2.equals("R")) {
                posrespect2 = posrespect2.replace("R", "" + "Negative");
            }
            if (posrespect2.equals("S")) {
                posrespect2 = posrespect2.replace("S", "" + "Negative");
            }
            if (posrespect2.equals("E")) {
                posrespect2 = posrespect2.replace("E", "" + "Negative");
            }
            if (posrespect2.equals("A")) {
                posrespect2 = posrespect2.replace("A", "" + "Negative");
            }

            String url = AppSettings.getInstance(this).getPropertyValue("feed_back");
            JSONObject object = new JSONObject();

            object.put("teacherid", getFromStore("teacherid"));
            object.put("studentid", student_id);
            object.put("effort", poseffort1 + poseffort2);
            object.put("attention", posattention1 + posattention2);
            object.put("respect", posrespect1 + posrespect2);
            object.put("spirit", posspirit1 + posspirit2);
            object.put("comments", ucomments.toString());

            HTTPostJson post = new HTTPostJson(ZoomImageStudent.this, ZoomImageStudent.this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public String getFromStore(String key) {
        return getSharedPreferences("Skills", 0).getString(key, "");
    }

    @Override
    public void onFinish(Object results, int requestId) {

        Utils.dismissProgress();
        try {
            switch (requestId) {
                case 1:
                    if (results != null) {
                        JSONObject jsonObject = new JSONObject(results.toString());

                        if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                            showToast(jsonObject.optString("statusdescription"));
                            folderDialog.dismiss();
                            ZoomImageStudent.this.finish();
                        }
                        showToast(jsonObject.optString("statusdescription"));
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
        showToast(errorCode);
    }
}

