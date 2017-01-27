package com.sharmastech.skillhouettes;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sharmastech.skillhouettes.Animations.FullscreenVideoLayout;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user on 1/26/2017.
 */

public class CompareVideo extends AppCompatActivity implements IItemHandler {
    private FullscreenVideoLayout stu_videoLayout,teacher_videoLayout;
    private String skillid, student_id, teachervideo, studentvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_video);

        try {

            skillid = getIntent().getStringExtra("skillid");
            student_id = getIntent().getStringExtra("student_id");
            getCompareVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stu_videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview_student);
        stu_videoLayout.setActivity(this);
        stu_videoLayout.setShouldAutoplay(true);

        teacher_videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview_teacher);
        teacher_videoLayout.setActivity(this);
        teacher_videoLayout.setShouldAutoplay(true);
    }

    private void getCompareVideo() {
        try {
            String url = AppSettings.getInstance(this).getPropertyValue("st_compare_video");
            JSONObject object = new JSONObject();
            object.put("skillid", skillid.trim());
            object.put("studentid", student_id.trim());
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
           // Utils.showProgress(getString(R.string.pwait), this);
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
                        JSONObject object = new JSONObject(results.toString());
                        if (object.optString("status").equalsIgnoreCase("0")) {
                            teachervideo = object.optString("teachervideo");
                            studentvideo = object.optString("studentvideo");
                            loadVideo();
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
        showToast(errorCode);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        teacher_videoLayout.resize();
        stu_videoLayout.resize();
    }

    public void loadVideo() {
        try {
            if (teachervideo != null) {
                String vpath = AppSettings.getInstance(this).getPropertyValue("i_path");
                String tepath = vpath + teachervideo;

                try {

                    Uri tevideoUri = Uri.parse(tepath);
                    teacher_videoLayout.setVideoURI(tevideoUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Video Url not found...");
                return;
            }
            if (studentvideo != null) {
                String vpath = AppSettings.getInstance(this).getPropertyValue("i_path");
                String stpath = vpath + studentvideo;
                try {

                    Uri stvideoUri = Uri.parse(stpath);
                    stu_videoLayout.setVideoURI(stvideoUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Video Url not found...");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
