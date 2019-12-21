package com.belaku.a4p1w;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";
    private String mVerificationId;

    EditText editTextPhno;
    Button BtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        findViewByIds();






    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }



    private void findViewByIds() {
        editTextPhno = findViewById(R.id.edtx_phno);
        BtnLogin = findViewById(R.id.btn_login);
        BtnLogin.setOnClickListener(AuthActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                break;
        }
    }
}
