package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.smartglass.model.User;
import com.example.smartglass.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText mEmailET, mPasswordET;
    Button mLoginBtn;
    TextView mLoginSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            goToMainActivity();
            finish();
        }

        mEmailET = findViewById(R.id.email);
        mPasswordET = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.login_btn);
        mLoginSignUpBtn = findViewById(R.id.login_signup_btn);
        mLoginSignUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        mLoginBtn.setOnClickListener(v -> {
            mLoginBtn.setEnabled(false);
            if(validateUserData()){
                userLogin();
            }
        });


    }


    private boolean validateUserData() {
        //first getting the values
        final String email = mEmailET.getText().toString();
        final String pass = mPasswordET.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(email)) {
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

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values
        final String email = mEmailET.getText().toString();
        final String pass = mPasswordET.getText().toString();

        AndroidNetworking.post(Urls.LOGIN_URL)
                .addBodyParameter("email", email)
                .addBodyParameter("password", pass)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
                        try {
                            //converting response to json object
                            if(response.getInt("status") == 1){
                                int  id = Integer.parseInt(response.getString("id"));
                                String  email = response.getString("email");
                                String  name = response.getString("name");
                                User user = new User(id, name , email);

                            SharedPrefManager.getInstance(LoginActivity.this).userLogin(user);
                            goToMainActivity();
                            finish();
                            mLoginBtn.setEnabled(true);

                            } else if(response.getInt("status") == 0){
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                mLoginBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            mLoginBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("login catch", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("login", anError.getMessage());
                    }
                });
//
    }
    private void editStatus() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String sentenceId = "";

        AndroidNetworking.post(Urls.EDIT_STATUS_URL)
                .addBodyParameter("sentence_id", sentenceId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
                        try {
                            //converting response to json object
                            if(response.getInt("status") == 1){
                                String message = response.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            } else if(response.getInt("status") == 0){
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("edit status ", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("edit status", anError.getMessage());
                    }
                });
//
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}