package com.iffecode.api_integration_v5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference db;

    private TextView fullnameText;
    private TextView usernameText;
    private TextView emailText;
    private TextView genderText;
    private TextView dateOfBirthText;

    private Button profileToHomeBtn;
    private Button profileToEditBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,
                container,
                false);

        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance()
                .getReference("users");

        fullnameText = view.findViewById(R.id.fullnameText);
        usernameText = view.findViewById(R.id.usernameText);
        emailText = view.findViewById(R.id.emailText);
        genderText = view.findViewById(R.id.genderText);
        dateOfBirthText = view.findViewById(R.id.dateOfBirthText);

        profileToHomeBtn =
                view.findViewById(R.id.ProfileToHomeBtn);

        profileToEditBtn =
                view.findViewById(R.id.ProfileToEditBtn);

        loadUserData();

        profileToHomeBtn.setOnClickListener(v -> {

            MainActivity2 activity =
                    (MainActivity2) requireActivity();

            activity.showHome();
        });

        profileToEditBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.profileEditFragment);
        });



       return view;
    }

    private void loadUserData() {

        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId =
                auth.getCurrentUser().getUid();

        db.child(userId).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                DataSnapshot snapshot = task.getResult();

                String fullname =
                        snapshot.child("fullname")
                                .getValue(String.class);

                String username =
                        snapshot.child("username")
                                .getValue(String.class);

                String email =
                        snapshot.child("email")
                                .getValue(String.class);

                String gender =
                        snapshot.child("gender")
                                .getValue(String.class);

                String dateOfBirth =
                        snapshot.child("dateOfBirth")
                                .getValue(String.class);


                if (fullname != null) {
                    fullnameText.setText(
                            "Full name: " + fullname
                    );
                }

                if (username != null) {
                    usernameText.setText(
                            "Username: " + username
                    );
                }

                if (email != null) {
                    emailText.setText(
                            "Email: " + email
                    );
                }

                if (gender != null) {
                    genderText.setText(
                            "Gender: " + gender
                    );
                }

                if (dateOfBirth != null) {
                    dateOfBirthText.setText(
                            "Date of birth: " + dateOfBirth
                    );
                }

            } else {

                Toast.makeText(
                        requireContext(),
                        "Could not load profile",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}