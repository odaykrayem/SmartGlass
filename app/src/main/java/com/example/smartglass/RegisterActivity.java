package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText mFnameEt, mLnameEt, mGlassId, mPasswordET;
    Button mRegisterBtn;
    TextView mSignUpLoginBtn;
    TextView mAboutUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();

        mSignUpLoginBtn.setOnClickListener(v -> {
            finish();
        });

        mRegisterBtn.setOnClickListener(v -> {
            goToMainActivity();
//            mRegisterBtn.setEnabled(false);
//            if(validateUserData()){
//                registerUser();
//            }
        });

        mAboutUsBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUsActivity.class)));

    }

    private void bindViews() {
        mFnameEt = findViewById(R.id.f_name);
        mLnameEt = findViewById(R.id.l_name);
        mGlassId = findViewById(R.id.glass_id);
        mPasswordET = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.register_btn);
        mSignUpLoginBtn = findViewById(R.id.signup_login_btn);
        mAboutUsBtn = findViewById(R.id.about_btn);
    }

    private boolean validateUserData() {

        //first getting the values
        final String pass = mPasswordET.getText().toString();
        final String fname = mFnameEt.getText().toString();
        final String lname = mLnameEt.getText().toString();
        final String glassId = mGlassId.getText().toString();

        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        if (TextUtils.isEmpty(lname)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }


        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }


        if (TextUtils.isEmpty(glassId)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }



        return true;
    }

    private void registerUser() {

//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Processing Please wait...");
//        pDialog.show();
//
//        //first getting the values
//        final String pass = mPasswordET.getText().toString();
//        final String name = mNameET.getText().toString();
//        final String phone = mPhoneET.getText().toString().trim();
//
//        AndroidNetworking.post("http://nawar.scit.co/oup/bansharna/api/auth/signup.php")
//                .addBodyParameter("name", name)
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
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("data");
//                                User user;
//                                SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.USER);
//                                user = new User(
//                                        Integer.parseInt(userJson.getString("id")),
//                                        userJson.getString("name"),
//                                        "+966 "+userJson.getString("phone")
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                                goToUserMainActivity();
//                                finish();
//
//                                mRegisterBtn.setEnabled(true);
//                            } else if(obj.getInt("status") == -1){
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                mRegisterBtn.setEnabled(true);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        pDialog.dismiss();
//                        mRegisterBtn.setEnabled(true);
//                        Toast.makeText(RegisterActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}