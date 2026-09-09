package com.iffecode.api_integration_v5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;


    EditText email;
    EditText password;
    Button loginbtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginbtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (emailText.isEmpty() || passwordText.isEmpty()){

                    Toast.makeText(MainActivity.this,
                            "Please fill in email and password",
                            Toast.LENGTH_SHORT)
                            .show();

                    return;

                }

                auth.signInWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT)
                                        .show();

                                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                                startActivity(i);
                                //finish(); //Användaren kan inte trycka bakåt på android knappen och komma tillbaka till login skämen

                            } else {
                                Toast.makeText(MainActivity.this, "Login failed!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });





            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FrameLayout container = findViewById(R.id.registerFragmentContainer);
                container.setVisibility(View.VISIBLE);

                RegisterFragment fragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registerFragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}