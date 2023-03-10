package com.example.mtd319_eca_carsontham;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    EditText usernameText, passwordText;
    DataService ds;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ds = new DataService(this);

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String pw = passwordText.getText().toString();

                if (!ds.checkValidUser(username)) {
                    ds.registerNewUser(getApplicationContext(), username, pw);
                    Intent goToLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goToLoginIntent);
                    finish();
                } else {
                    usernameText.setError("Username not available. Please choose another name");
                }
            }
        });

    }
}