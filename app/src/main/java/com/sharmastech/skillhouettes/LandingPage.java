package com.sharmastech.skillhouettes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LandingPage extends FragmentActivity {

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landingpage);

        findViewById(R.id.tv_login).setOnClickListener(onClick);
        findViewById(R.id.tv_cafu).setOnClickListener(onClick);
        findViewById(R.id.tv_subscribenow).setOnClickListener(onClick);

        findViewById(R.id.tv_clickhere).setOnClickListener(onClick);
        findViewById(R.id.tv_login_student).setOnClickListener(onClick);
        findViewById(R.id.tv_logout_tchr).setOnClickListener(onClick);
        findViewById(R.id.tv_logout_stu).setOnClickListener(onClick);

        String username = getFromStore("username");
        if (username.length()!=0){
            findViewById(R.id.tv_logout_tchr).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.tv_login)).setText(Html.fromHtml("<u>Teacher Home</u>"));
        }else {
            ((TextView)findViewById(R.id.tv_login)).setText(Html.fromHtml("<u>Teacher Login</u>"));
        }
        String id = getFromStore("studentid");
        if (id.length()!=0){
            findViewById(R.id.tv_logout_stu).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.tv_login_student)).setText(Html.fromHtml("<u>Student Home</u>"));
        }else {
            ((TextView)findViewById(R.id.tv_login_student)).setText(Html.fromHtml("<u>Student Login</u>"));
        }

    }

    OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.tv_login:
                    String username = getFromStore("username");
                    if (username.length()!= 0) {
                        launchActivity(Skill_houttes_home.class, 1004);
                    } else {
                        launchActivity(Login.class, 1000);
                    }
                    break;
                case R.id.tv_login_student:
                    String id = getFromStore("studentid");
                    if (id.length()!= 0) {
                        //launchActivity(St_details.class, 1003);
                        Intent st_detail=new Intent(getApplicationContext(),St_details.class);
                        st_detail.putExtra("emailid",getFromStore("emailid"));
                        st_detail.putExtra("password",getFromStore("password"));
                        st_detail.putExtra("lname",getFromStore("lname"));
                        st_detail.putExtra("fname",getFromStore("fname"));
                        st_detail.putExtra("mobile",getFromStore("mobile"));
                        st_detail.putExtra("simage",getFromStore("simage"));
                        startActivity(st_detail);
                        finish();
                    } else {
                        launchActivity(Student_Login.class, 1005);
                    }
                    break;
                case R.id.tv_clickhere:
                    launchActivity(Skill_houttes_home.class, 1002);
                    break;

                case R.id.tv_subscribenow:
                    launchActivity(SignUp.class, 1001);
                    break;

                case R.id.tv_logout_tchr:
                    addToStore("username","");
                    finish();
                    break;
                case R.id.tv_logout_stu:
                    addToStore("studentid","");
                    addToStore("emailid","");
                    addToStore("password","");
                    finish();
                    break;

                default:
                    break;
            }

        }
    };
    public String getFromStore(String key) {
        return getSharedPreferences("Skills", 0).getString(key, "");
    }
    public void addToStore(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("Skills", 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void launchActivity(Class<?> cls, int requestCode) {
        Intent mainIntent = new Intent(LandingPage.this, cls);
        startActivityForResult(mainIntent, requestCode);
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showEditTextError(EditText edittext, String error) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
            edittext.setError(error);
        else
            edittext.setError(Html.fromHtml("<font color='black'>" + error + "!</font>"));
    }


}
