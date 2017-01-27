package com.sharmastech.skillhouettes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import android.app.DatePickerDialog.OnDateSetListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.tasks.HTTPostJson;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONObject;

/**
 * Created by w7u on 1/5/2017.
 */

public class StudentRegister extends FragmentActivity implements IItemHandler {

    private EditText et_dob = null;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        et_dob = (EditText) findViewById(R.id.et_dob);
        findViewById(R.id.tv_signup).setOnClickListener(onClick);
        findViewById(R.id.et_dob).setOnClickListener(onClick);

    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.tv_signup:
                    signUpDefault();
                    break;
                case R.id.et_dob:
                    showDate();
                    break;

                default:
                    break;
            }

        }
    };

    private void signUpDefault() {

        try {

//            String teacherid = ((EditText) findViewById(R.id.et_teacherid)).getText().toString().trim();
//
//            if (teacherid.length() == 0) {
//                showEditTextError((EditText) findViewById(R.id.et_teacherid), getString(R.string.peti));
//                return;
//            }

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

            String et_number = ((EditText) findViewById(R.id.et_number)).getText().toString().trim();
            if (et_number.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_number), getString(R.string.pepn));
                return;
            }

            String et_dob = ((EditText) findViewById(R.id.et_dob)).getText().toString().trim();
            if (et_dob.length() == 0) {
                showEditTextError((EditText) findViewById(R.id.et_dob), getString(R.string.pedb));
                return;
            }

            String url = AppSettings.getInstance(this).getPropertyValue("student_register");
            JSONObject object = new JSONObject();

            object.put("username", et_username);
            object.put("password", et_password);
            object.put("firstname", et_firstname);
            object.put("lastname", et_lastname);
            object.put("email", et_emailid);
            object.put("phoneno", et_number);
            object.put("dob", et_dob);
//            object.put("streetno", et_password);
//            object.put("city", et_password);
//            object.put("state", et_password);
//            object.put("country", et_password);
//            object.put("schoolname", et_password);
//            object.put("latitude", et_password);
//            object.put("longitude", et_password);
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDate() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_dob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void launchActivity(Class<?> cls, int requestCode) {
        Intent mainIntent = new Intent(StudentRegister.this, cls);
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
                            StudentRegister.this.finish();
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