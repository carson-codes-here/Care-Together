package com.example.mtd319_eca_carsontham;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button b, s, resetBtn;
    DataService ds;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sm = new SessionManager(getApplicationContext());
        ds = new DataService(getApplicationContext());
        if (sm.getLogin()) {
            Intent goToHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToHomeIntent);
            finish();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        b = findViewById(R.id.button);
        s = findViewById(R.id.signUpBtn);

        // this code is to reset the entire database and re-populating the items
        resetBtn = findViewById(R.id.resetApp);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ds.populateDB(getApplicationContext());
                Toast.makeText(LoginActivity.this, "DB successfully resetted", Toast.LENGTH_SHORT).show();
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ds = new DataService(getApplicationContext());
                Intent goToSignUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(goToSignUpIntent);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = username.getText().toString();
                String loginPw = password.getText().toString();

                if (loginName.equals("Admin") && loginPw.equals("admin123")) {
                    Intent goToAdminPageIntent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(goToAdminPageIntent);
                    finish();
                }

                if (loginName.isEmpty()){
                    username.setError("Invalid credentials");
                } else if (loginPw.isEmpty()) {
                    password.setError("Invalid credentials");
                } else {
                    if (ds.checkValidUser(loginName) && ds.checkValidPw(loginName, loginPw)) {
                        sm.setLogin(true);
                        sm.setUser(loginName, ds.getUserId(loginName));
                        Intent goToHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goToHomeIntent);
                        finish();
                    } else{
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}