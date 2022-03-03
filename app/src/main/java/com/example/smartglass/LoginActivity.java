package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartglass.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    EditText mGlassIDEt, mPasswordET;
    Button mLoginBtn;
    TextView mLoginSignUpBtn;
    TextView mAboutUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            goToMainActivity();
            finish();
        }

        bindViews();

        mLoginSignUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        mLoginBtn.setOnClickListener(v -> {
//            mLoginBtn.setEnabled(false);
//            if(validateUserData()){
//                userLogin();
//            }
            goToMainActivity();
        });

        mAboutUsBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUsActivity.class)));

    }

    private void bindViews() {
        mGlassIDEt = findViewById(R.id.glass_id);
        mPasswordET = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.login_btn);
        mLoginSignUpBtn = findViewById(R.id.login_signup_btn);
        mAboutUsBtn = findViewById(R.id.about_btn);
    }

    private boolean validateUserData() {

        //first getting the values
        final String glassId = mGlassIDEt.getText().toString();
        final String pass = mPasswordET.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(glassId)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mLoginBtn.setEnabled(true);
            return false;
        }

        //checking if password is empty
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mLoginBtn.setEnabled(true);
            return false;
        }

        return true;

    }


    private void userLogin() {
//
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Processing Please wait...");
//        pDialog.show();
//
//        //first getting the values
//        final String phone = mPhoneET.getText().toString();
//        final String pass = mPasswordET.getText().toString();
//
//        AndroidNetworking.post("http://nawar.scit.co/oup/bansharna/api/auth/login.php")
//                .addBodyParameter("phone", phone)
//                .addBodyParameter("password", pass)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        pDialog.dismiss();
//
//                        try {
//                            //converting response to json object
//                            JSONObject obj = response;
//
//                            //if no error in response
//                            if (obj.getInt("status") == 1) {
//
//                                JSONObject userJson = obj.getJSONObject("data");
//
//                                int userType = userJson.getInt("type");
//                                //getting the user from the response
//                                User user;
//                                switch (userType){
//                                    case Constants.ADMIN:
//                                        //getting the user from the response
//                                        SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.ADMIN);
//                                        user = new User(
//                                                Integer.parseInt(userJson.getString("id")),
//                                                userJson.getString("name"),
//                                                "+966 "+userJson.getString("phone")
//                                        );
//                                        //storing the user in shared preferences
//                                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(user);
//
//                                        gotoAdminMainActivity();
//                                        finish();
//                                        break;
//                                    case Constants.USER:
//                                        SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.USER);
//                                        user = new User(
//                                                Integer.parseInt(userJson.getString("id")),
//                                                userJson.getString("name"),
//                                                "+966 "+userJson.getString("phone")
//                                        );
//                                        //storing the user in shared preferences
//                                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(user);
//
//                                        goToMainActivity();
//                                        finish();
//                                        break;
//                                }
//
//                                mLoginBtn.setEnabled(true);
//
//                            } else if(obj.getInt("status") == -1){
//                                Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                mLoginBtn.setEnabled(true);
//                            }
//                        } catch (JSONException e) {
//                            mLoginBtn.setEnabled(true);
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        pDialog.dismiss();
//                        mLoginBtn.setEnabled(true);
//                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}