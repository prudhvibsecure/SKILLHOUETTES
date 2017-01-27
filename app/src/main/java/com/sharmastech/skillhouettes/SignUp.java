package com.sharmastech.skillhouettes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppPreferences;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.tasks.HTTPPostTask;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONObject;

public class SignUp extends AppCompatActivity implements IItemHandler {

    private Double latitude, longitude;

    private EditText user_name, password, Firstname, LastName, Email, phone, address,street,schoolname;

    String address_txt,user_name_txt, password_txt, Firstname_txt, LastName_txt, Email_txt, phone_txt,school_txt,street_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        findViewById(R.id.tv_signup).setOnClickListener(onClick);
        findViewById(R.id.et_address).setOnClickListener(onClick);

        user_name = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        Firstname = (EditText) findViewById(R.id.et_firstname);
        LastName = (EditText) findViewById(R.id.et_lastname);
        Email = (EditText) findViewById(R.id.et_emailid);
        phone = (EditText) findViewById(R.id.et_number);
        address = (EditText) findViewById(R.id.et_address);
        schoolname = (EditText) findViewById(R.id.et_school);
        street = (EditText) findViewById(R.id.et_street);

        Intent intent=getIntent();
        if (intent!=null){

            address_txt=intent.getStringExtra("address");
            user_name_txt=intent.getStringExtra("username");
            password_txt=intent.getStringExtra("password");
            Firstname_txt=intent.getStringExtra("firstname");
            LastName_txt=intent.getStringExtra("lastname");
            Email_txt=intent.getStringExtra("email");
            phone_txt=intent.getStringExtra("contact");
            school_txt=intent.getStringExtra("school");
            street_txt=intent.getStringExtra("street");
            latitude=intent.getDoubleExtra("latitude",0.00);
            longitude=intent.getDoubleExtra("longitude",0.00);

            address.setText(address_txt);
            user_name.setText(user_name_txt);
            password.setText(password_txt);
            Firstname.setText(Firstname_txt);
            LastName.setText(LastName_txt);
            Email.setText(Email_txt);
            phone.setText(phone_txt);
            schoolname.setText(school_txt);
            street.setText(street_txt);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchActivity(LandingPage.class,1002);
    }
    OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.tv_signup:
                    signUpDefault();
                    break;
                case R.id.et_address:

                    Intent maps=new Intent(SignUp.this,MyMaps.class);

                    maps.putExtra("uname",((EditText) findViewById(R.id.et_username)).getText().toString().trim());
                    maps.putExtra("pwd",((EditText) findViewById(R.id.et_password)).getText().toString().trim());
                    maps.putExtra("fname",((EditText) findViewById(R.id.et_firstname)).getText().toString().trim());
                    maps.putExtra("lname",((EditText) findViewById(R.id.et_lastname)).getText().toString().trim());
                    maps.putExtra("email",((EditText) findViewById(R.id.et_emailid)).getText().toString().trim());
                    maps.putExtra("phone",((EditText) findViewById(R.id.et_number)).getText().toString().trim());
                    maps.putExtra("school",((EditText) findViewById(R.id.et_school)).getText().toString().trim());
                    maps.putExtra("street",((EditText) findViewById(R.id.et_street)).getText().toString().trim());
                    startActivity(maps);
                    finish();
                    break;

                default:
                    break;
            }

        }
    };

    private void signUpDefault() {

        try {


            String et_username = ((EditText) findViewById(R.id.et_username)).getText().toString().trim();

            if (et_username.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_username), getString(R.string.peu));
                return;
            }

            String et_password = ((EditText) findViewById(R.id.et_password)).getText().toString().trim();
            if (et_password.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_password), getString(R.string.pep));
                return;
            }

            if (et_password.length() < 8) {
                showEditTextError((EditText) findViewById(R.id.et_password), getString(R.string.psmbc));
                return;
            }

            String et_firstname = ((EditText) findViewById(R.id.et_firstname)).getText().toString().trim();
            if (et_firstname.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_firstname), getString(R.string.pefn));
                return;
            }

            String et_lastname = ((EditText) findViewById(R.id.et_lastname)).getText().toString().trim();
            if (et_lastname.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_lastname), getString(R.string.peln));
                return;
            }

            String et_emailid = ((EditText) findViewById(R.id.et_emailid)).getText().toString().trim();
            if (et_emailid.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_emailid), getString(R.string.peei));
                return;
            }

            if (!emailValidation(et_emailid)) {
                showEditTextError((EditText) findViewById(R.id.et_emailid), getString(R.string.peavei));
                return;
            }

            String street = ((EditText) findViewById(R.id.et_street)).getText().toString().trim();

            if (street.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_street), getString(R.string.peti));
                return;
            }
            String school = ((EditText) findViewById(R.id.et_school)).getText().toString().trim();

            if (school.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_school), getString(R.string.pschool));
                return;
            }
            String et_number = ((EditText) findViewById(R.id.et_number)).getText().toString().trim();
            if (et_number.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_number), getString(R.string.pepn));
                return;
            }

            String address = ((EditText) findViewById(R.id.et_address)).getText().toString().trim();
            if (address.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_address), getString(R.string.paddr));
                return;
            }

            String mylat = String.valueOf(latitude);
            String mylang = String.valueOf(longitude);

            String url = AppSettings.getInstance(this).getPropertyValue("tchr_register");
            JSONObject object = new JSONObject();


            object.put("username", et_username);
            object.put("password", et_password);
            object.put("firstname", et_firstname);
            object.put("lastname", et_lastname);
            object.put("email", et_emailid);
            object.put("phoneno", et_number);
            object.put("streetno,", street);
            object.put("schoolname", school);
            object.put("latitude", mylat);
            object.put("longitude", mylang);
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
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

    private void launchActivity(Class<?> cls, int requestCode) {
        Intent mainIntent = new Intent(SignUp.this, cls);
        startActivityForResult(mainIntent, requestCode);
    }

    @Override
    public void onFinish(Object results, int requestType) {

        Utils.dismissProgress();
        try {

            switch (requestType) {
                case 1:
                    //parseSignUpResponse((String) results);
                    if (results != null) {
                        JSONObject jsonObject = new JSONObject(results.toString());

                        if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                            showToast(jsonObject.optString("statusdescription"));
                            SignUp.this.finish();
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
    public void onError(String errorData, int requestType) {
        showToast(errorData);
    }

//    private void parseSignUpResponse(String response) throws Exception {
//
//        if (response != null && response.length() > 0) {
//
//
//        }
//    }

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

}
