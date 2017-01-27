package com.sharmastech.skillhouettes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppPreferences;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.TraceUtils;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 1/25/2017.
 */

public class Student_Login extends FragmentActivity implements IItemHandler {

    private int counter = 0;
    private String fname,lname,email,username,mobile,simage;
    private String password,emailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        findViewById(R.id.forgotPswd).setOnClickListener(onClick);
        findViewById(R.id.login_lgn).setOnClickListener(onClick);
        ((TextView)findViewById(R.id.register_lgn)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.title_head)).setText("Student Login");

    }


    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.login_lgn:
                    loginDefault();
                    break;

                case R.id.forgotPswd:
                    //launchActivity(ForgetPassword.class, 1001);
                    break;

                default:
                    break;
            }

        }
    };

    private void loginDefault() {

        try {

            emailid = ((EditText) findViewById(R.id.account_txt)).getText().toString().trim();

            if (emailid.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.account_txt), getString(R.string.peei));
                return;
            }

            password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
            if (password.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.password), getString(R.string.pep));
                return;
            }

            if (password.length() < 8) {
                showEditTextError((EditText) findViewById(R.id.password), getString(R.string.psmbc));
                return;
            }

            String url = AppSettings.getInstance(this).getPropertyValue("student_login");
            JSONObject object = new JSONObject();
            object.put("username", emailid);
            object.put("password", password);
            object.put("regid", AppPreferences.getInstance(this).getFromStore("regID"));
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);

        } catch (Exception e) {
            TraceUtils.logException(e);
        }

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void launchActivity(Class<?> cls, int requestCode) {
        Intent mainIntent = new Intent(this, cls);
        mainIntent.putExtra("emailid",emailid);
        mainIntent.putExtra("fname",fname);
        mainIntent.putExtra("lname",lname);
        mainIntent.putExtra("mobile",mobile);
        mainIntent.putExtra("username",username);
        mainIntent.putExtra("password",password);
        mainIntent.putExtra("simage",simage);
        startActivityForResult(mainIntent, requestCode);
    }

    @Override
    public void onFinish(Object results, int requestType) {

        Utils.dismissProgress();
        try {

            switch (requestType) {
                case 1:

                    if (results!=null){
                        JSONObject jsonObject = new JSONObject(results.toString());

                        if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                            addToStore("emailid", ((EditText) findViewById(R.id.account_txt)).getText().toString().trim());
                            addToStore("password", ((EditText) findViewById(R.id.password)).getText().toString().trim());
                            JSONArray jsonarray = jsonObject.getJSONArray("student_detail");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                showToast(jsonObject.optString("statusdescription"));
                                JSONObject jObject = jsonarray.getJSONObject(i);
                                addToStore("studentid", jObject.optString("studentid"));
                                addToStore("email", jObject.optString("email"));
                                addToStore("mobile", jObject.optString("phoneno"));
                                addToStore("fname", jObject.optString("firstname"));
                                addToStore("lname", jObject.optString("lastname"));
                                addToStore("simage", jObject.optString("simage"));
                                email=jObject.optString("email");
                                mobile=jObject.optString("phoneno");
                                fname=jObject.optString("firstname");
                                lname=jObject.optString("lastname");
                                username=jObject.optString("username");
                                simage=jObject.optString("simage");
                                launchActivity(St_details.class, 1003);
                                Student_Login.this.finish();
                                return;
                            }
                        }
                            showToast(jsonObject.optString("statusdescription"));
                    }
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            TraceUtils.logException(e);
        }

    }

    @Override
    public void onError(String errorData, int requestType) {
        showToast(errorData);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean emailValidation(String email) {

        if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
            return false;
        }
        int emailLenght = email.length();
        int atPosition = email.indexOf("@");

        String beforeAt = email.substring(0, atPosition);
        String afterAt = email.substring(atPosition + 1, emailLenght);

        if (beforeAt.length() == 0 || afterAt.length() == 0) {
            return false;
        }
        if (email.charAt(atPosition - 1) == '.') {
            return false;
        }
        if (email.charAt(atPosition + 1) == '.') {
            return false;
        }
        if (afterAt.indexOf(".") == -1) {
            return false;
        }
        char dotCh = 0;
        for (int i = 0; i < afterAt.length(); i++) {
            char ch = afterAt.charAt(i);
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        if (afterAt.indexOf("@") != -1) {
            return false;
        }
        int ind = 0;
        do {
            int newInd = afterAt.indexOf(".", ind + 1);

            if (newInd == ind || newInd == -1) {
                String prefix = afterAt.substring(ind + 1);
                if (prefix.length() > 1 && prefix.length() < 6) {
                    break;
                } else {
                    return false;
                }
            } else {
                ind = newInd;
            }
        } while (true);
        dotCh = 0;
        for (int i = 0; i < beforeAt.length(); i++) {
            char ch = beforeAt.charAt(i);
            if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a) || (ch == 0x2e)
                    || (ch == 0x2d) || (ch == 0x5f))) {
                return false;
            }
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1002) {
            switch (resultCode) {

                case RESULT_OK:
                    break;

                case RESULT_CANCELED:
                    break;

                default:
                    break;
            }
        }
    }

    private void showEditTextError(EditText edittext, String error) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
            edittext.setError(error);
        else
            edittext.setError(Html.fromHtml("<font color='black'>" + error + "!</font>"));
    }
    public String getFromStore(String key) {
        return getSharedPreferences("Skills", 0).getString(key, "");
    }

    public void addToStore(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("Skills", 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchActivity(LandingPage.class,1002);
    }

}
