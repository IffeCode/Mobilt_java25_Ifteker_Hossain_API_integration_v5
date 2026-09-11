package com.iffecode.api_integration_v5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView6;
    private TextView textView7;

    private Button profileBtn;
    private Button weatherBtn;

    private View navHostFragment;

    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);


        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);

        profileBtn = findViewById(R.id.profileBtn);
        weatherBtn = findViewById(R.id.weatherBtn);

        logoutBtn = findViewById(R.id.logoutBtn);

        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        navHostFragment = findViewById(R.id.nav_host_fragment);


        NavController navController =
                navHost.getNavController();

        navHostFragment.setVisibility(View.GONE);


        profileBtn.setOnClickListener(v -> {

            textView6.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            profileBtn.setVisibility(View.GONE);
            weatherBtn.setVisibility(View.GONE);

            navHostFragment.setVisibility(View.VISIBLE);

            navController.navigate(R.id.profileFragment);
        });


        weatherBtn.setOnClickListener(v -> {

            textView6.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            profileBtn.setVisibility(View.GONE);
            weatherBtn.setVisibility(View.GONE);

            navHostFragment.setVisibility(View.VISIBLE);

            navController.navigate(R.id.weatherFragment);
        });


        logoutBtn.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainActivity2.this, MainActivity.class);

            startActivity(intent);

            finish();
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void showHome() {

        navHostFragment.setVisibility(View.GONE);

        textView6.setVisibility(View.VISIBLE);
        textView7.setVisibility(View.VISIBLE);
        profileBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.VISIBLE);
    }

}