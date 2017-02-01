package com.sharmastech.skillhouettes;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.Animations.CircleTransform;
import com.sharmastech.skillhouettes.adapters.FeaturesAdapter;
import com.sharmastech.skillhouettes.callbacks.IDownloadCallback;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.common.Item;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;
import com.sharmastech.skillhouettes.controls.ProgressControl;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.tasks.FileUploader;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * Created by user on 1/26/2017.
 */

public class SingleStudentSkill extends AppCompatActivity implements View.OnClickListener, IItemHandler, IDownloadCallback {

    private ImageLoader imageLoader;
    private ImageView st_image, st_footer;

    private RecyclerView mRecyclerView, horizontal_recycler_view;

    private CustomSwipeToRefresh mSwipeRefreshLayout;

    private FeaturesAdapter adapter;

    private String student_id, featureid = null, status = null;

    private Dialog rDialog, upDialog;

    private RadioGroup radioGroup = null;

    private String statuscont = null, skillid = null, video = null, displayname = null;

    private LinearLayout attachmentLayout = null;

    private Hashtable<Integer, FileUploader> requestItems = new Hashtable<Integer, FileUploader>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_student_skill_details);


        findViewById(R.id.view_video).setOnClickListener(onClick);
        findViewById(R.id.add_video).setOnClickListener(onClick);
        findViewById(R.id.compare_video).setOnClickListener(onClick);

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
                skillid = jsonObject.optString("skillid");
                video = jsonObject.optString("video");

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

//        rDialog = new Dialog(this);
//        rDialog.setContentView(R.layout.notification);
//
//
//        TextView title = (TextView) rDialog.findViewById(R.id.txt_notification);
//        title.setText("Update Future status");
//        Button submit = (Button) rDialog.findViewById(R.id.notify_sub);
//        if ("Yes".equalsIgnoreCase(status)) {
//            ((RadioButton) rDialog.findViewById(R.id.notify_yes)).setChecked(true);
//        } else {
//            ((RadioButton) rDialog.findViewById(R.id.notify_no)).setChecked(true);
//        }
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upDateStatus(itemPosition, student_id, featureid, status);
//            }
//
//        });
//
//        rDialog.show();
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
                        this.finish();
                    }
                    break;
                case 2:
                    JSONObject jsonob = new JSONObject(results.toString());
                    if (jsonob.optString("status").equalsIgnoreCase("0")) {
                        showToast(jsonob.optString("statusdescription"));
                        upDialog.cancel();
                    } else if (jsonob.optString("status").equalsIgnoreCase("1")) {
                        showToast(jsonob.optString("statusdescription"));
                        upDialog.cancel();
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

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.view_video:
                    Intent video_intent = new Intent(getApplicationContext(), Video_Play_Skill.class);
                    video_intent.putExtra("video", video);
                    startActivity(video_intent);
                    break;

                case R.id.add_video:
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, 2798);
                    break;
                case R.id.compare_video:
                    Intent compare = new Intent(getApplicationContext(), CompareVideo.class);
                    compare.putExtra("skillid", skillid);
                    compare.putExtra("student_id", student_id);
                    startActivity(compare);
                    break;
                default:
                    break;
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {

            case 2798:
                if (resultCode == RESULT_OK) {

                    if (data == null) {
                        showToast("File not Found");
                        return;
                    }
                    validateSelectedFile(processFileMetadata(data));
                }
                break;

        }
    }

    private void validateSelectedFile(Item item) {
        try {
            if (item == null) {
                showToast("This file not supported");
                return;
            }

            long fileSize = 0;
            fileSize = Long.parseLong(item.getAttribute("size"));
            if (fileSize > 3000000) {
                showToast("upload file must be 3MB below");
                return;
            }
            addUploadVideo(item, fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addUploadVideo(final Item fullItem, long fileSize) {
        upDialog = new Dialog(this);
        upDialog.setContentView(R.layout.upload_videofile);

        int viewId = Integer.parseInt(getCustomSystemTime());

        View view = View.inflate(this, R.layout.template_attachments_layout, null);
        view.setId(viewId);

//        view.findViewById(R.id.tv_attachmentAction).setOnClickListener(this);
//        view.findViewById(R.id.tv_attachmentAction).setTag(viewId);

        ((ProgressControl) view.findViewById(R.id.download_progress)).setText(fullItem.getAttribute("displayname"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 10);

        String attachTime = getDeviceDateTime("yyyyMMdd_hhmmss");

        displayname = fullItem.getAttribute("displayname");
        if (displayname.contains(".")) {

            String[] temp = displayname.split("\\.");
            displayname = temp[0] + "_" + attachTime + "." + temp[1];

        } else {

            displayname = displayname + "_" + attachTime;

        }

        fullItem.setAttribute("attachname", displayname);
        view.setTag(fullItem);
        attachmentLayout = (LinearLayout) upDialog.findViewById(R.id.ll_attachments_layout);
        attachmentLayout.addView(view, params);

        startUploadingFile(fullItem, viewId);
        final String videoname = fullItem.getAttribute("attachname");
        TextView submit = (TextView) upDialog.findViewById(R.id.upload_file);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitVideoFile(skillid, student_id, videoname, displayname);
            }

        });

        upDialog.show();

    }

    private void submitVideoFile(String skillid, String student_id, String time_attach, String videoname) {
        try {

            if (requestItems != null && requestItems.size() > 0) {
                showToast("Your Video Uploading please wait...");
                return;
            }
            String url = AppSettings.getInstance(this).getPropertyValue("st_add_video");
            JSONObject object = new JSONObject();
            object.put("studentid", student_id);
            object.put("skillid", skillid);
            object.put("videoname", time_attach);
            object.put("video", videoname);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCustomSystemTime() {
        DateFormat dateFormat = new SimpleDateFormat("hhmmssSSS");
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

    private void startUploadingFile(Item item, int requestId) {

        String url = AppSettings.getInstance(this).getPropertyValue("upload_file");

        FileUploader uploader = new FileUploader(this, this);
        uploader.setFileName(item.getAttribute("displayname"), item.getAttribute("attachname"));
        uploader.userRequest("", requestId, url, item.getAttribute("data"));

        requestItems.put(requestId, uploader);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Item processFileMetadata(Intent intent) {

        String url = intent.getDataString();

        url = URLDecoder.decode(url);

        String id = "0";

        url = url.replace("content://", "");

        if (url.contains(":"))
            id = url.substring(url.lastIndexOf(":") + 1, url.length());
        else
            id = url.substring(url.lastIndexOf("/") + 1, url.length());


        String sel = MediaStore.Video.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), null, sel,
                new String[]{id}, null);


        Item item = null;

        if (cursor != null && cursor.moveToFirst()) {
            item = new Item("");

            String[] resultsColumns = cursor.getColumnNames();
            do {

                for (int i = 0; i < resultsColumns.length; i++) {
                    String key = resultsColumns[i];
                    String value = cursor.getString(cursor.getColumnIndexOrThrow(resultsColumns[i]));

                    if (value != null) {
                        if (key.contains("_"))
                            key = key.replace("_", "");
                        item.setAttribute(key, value);

                    }
                }

            } while (cursor.moveToNext());

            cursor.close();
            cursor = null;

        }

        if (item == null) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            Uri uri = intent.getData();

            if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {

                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        // return Environment.getExternalStorageDirectory() +
                        // "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String documentId = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(documentId));

                    try {
                        cursor = getContentResolver().query(contentUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            item = new Item("");
                            String[] resultsColumns = cursor.getColumnNames();

                            for (int i = 0; i < resultsColumns.length; i++) {
                                String key = resultsColumns[i];
                                String value = cursor.getString(cursor.getColumnIndexOrThrow(resultsColumns[i]));

                                // Log.e("" + key, value + "");

                                if (value != null) {
                                    if (key.contains("_"))
                                        key = key.replace("_", "");
                                    item.setAttribute(key, value);

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }

                }
            }


        }

        return item;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getDeviceDateTime(String dtFormat) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis());

        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat(dtFormat);

        return format.format(date);

    }


    @Override
    public void onStateChange(int what, int arg1, int arg2, Object obj, int requestId) {
        try {

            switch (what) {

                case -1: // failed

                    View fview = attachmentLayout.findViewById(requestId);
                    Item fitem = (Item) fview.getTag();

                    //removeAttachment(requestId);

                    showToast("Attachment " + fitem.getAttribute("displayname") + " failed. Please try again. ! "
                            + obj.toString());

                    fitem = null;
                    fview = null;

                    // uploadingIds.remove(requestId);

                    break;

                case 1: // progressBar

                    View pview = attachmentLayout.findViewById(requestId);

                    ((ProgressControl) pview.findViewById(R.id.download_progress)).updateProgressState((Long[]) obj);// setText(fullItem.getAttribute("displayname"));

                    break;

                case 0: // success
                    if (obj != null) {
                        JSONObject jsonObject = new JSONObject(obj.toString());

                        if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                            View sview = attachmentLayout.findViewById(requestId);
                            Item sitem = (Item) sview.getTag();

                            ((ProgressControl) sview.findViewById(R.id.download_progress)).hideProgressBar();

                            ((ProgressControl) sview.findViewById(R.id.download_progress))
                                    .setText(sitem.getAttribute("displayname"));
                            FileUploader uploader = requestItems.remove(requestId);
                            // uploader.cancelrequest();
                            uploader = null;
                            return;
                        } else if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                            showToast(obj + "");
                        }
                    }


                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }

    }
}
