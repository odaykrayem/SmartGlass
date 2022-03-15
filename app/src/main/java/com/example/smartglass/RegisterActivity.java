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

public class RegisterActivity extends AppCompatActivity {

    EditText mNameET, mEmailET, mPasswordET;
    Button mRegisterBtn;
    TextView mSignUpLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPasswordET = findViewById(R.id.password);
        mNameET = findViewById(R.id.name);
        mEmailET = findViewById(R.id.email);
        mRegisterBtn = findViewById(R.id.register_btn);
        mSignUpLoginBtn = findViewById(R.id.signup_login_btn);
        mSignUpLoginBtn.setOnClickListener(v -> {
            finish();
        });

        mRegisterBtn.setOnClickListener(v -> {
            mRegisterBtn.setEnabled(false);
            if(validateUserData()){
                registerUser();
            }
        });


    }


    private boolean validateUserData() {

        //first getting the values
        final String pass = mPasswordET.getText().toString();
        final String name = mNameET.getText().toString();
        final String email = mEmailET.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }


        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        return true;
    }

    private void registerUser() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values
        final String email = mEmailET.getText().toString();
        final String pass = mPasswordET.getText().toString();
        final String name = mNameET.getText().toString();

        AndroidNetworking.post(Urls.REGISTER_URL)
                .addBodyParameter("email", email)
                .addBodyParameter("name", name)
                .addBodyParameter("password", pass)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
                        try {
                            Log.e("register", response.toString());

                            //converting response to json object
                            if(response.getInt("status") == 1){
                                int  id = Integer.parseInt(response.getString("id"));
                                String  email = response.getString("email");
                                String  name = response.getString("name");
                                User user = new User(id, name , email);

                                String message = response.getString("message");
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(RegisterActivity.this).userLogin(user);
                                pDialog.dismiss();
                                goToMainActivity();
                                finish();
                                mRegisterBtn.setEnabled(true);

                            } else if(response.getInt("status") == 0){
                                Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                mRegisterBtn.setEnabled(true);
                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            mRegisterBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("login catch", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("login", anError.getMessage());
                    }
                });

    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}