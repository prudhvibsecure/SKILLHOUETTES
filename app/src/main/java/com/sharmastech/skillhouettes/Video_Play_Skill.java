package com.sharmastech.skillhouettes;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.Animations.FullscreenVideoLayout;
import com.sharmastech.skillhouettes.common.AppSettings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by w7u on 1/9/2017.
 */

public class Video_Play_Skill extends Activity {

    private FullscreenVideoLayout videoLayout;

    private String video_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);

        videoLayout.setActivity(this);
        videoLayout.setShouldAutoplay(true);

        try {
            String videodata = getIntent().getStringExtra("videoData");
            int position = getIntent().getIntExtra("position", 0);
            video_url = getIntent().getStringExtra("video");

            JSONArray jsonArray = new JSONArray(videodata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == position) {
                    String slid = jsonObject.optString("skillid");
                    JSONArray skillsobject = jsonObject.getJSONArray("skillvideo_detail");
                    for (int j = 0; j < skillsobject.length(); j++) {
                        JSONObject mjson = skillsobject.getJSONObject(j);
                        String sid = mjson.optString("skillid");
                        video_url = mjson.optString("video");
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadVideo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        videoLayout.resize();
    }

    public void loadVideo() {

        if (video_url != null) {
            String vpath = AppSettings.getInstance(this).getPropertyValue("i_path");
            String tpath = vpath + video_url;
            try {

                Uri videoUri = Uri.parse(tpath);
                videoLayout.setVideoURI(videoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            showToast("Video Url not found...");
            return;
        }
    }

    public void reset(View v) {
        if (this.videoLayout != null) {
            this.videoLayout.reset();
            loadVideo();
        }
    }
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
