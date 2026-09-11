package com.iffecode.api_integration_v5;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ProfileEditFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference db;

    private EditText fullnameEditText;
    private EditText usernameEditText;
    private Spinner genderEditSpinner;
    private Button dateOfBirthEditButton;
    private Button updateButton;
    private Button toProfileBtn;

    public ProfileEditFragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(
                R.layout.fragment_profile_edit,
                container,
                false
        );


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users");


        fullnameEditText = view.findViewById(R.id.fullnameEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);

        genderEditSpinner = view.findViewById(R.id.genderEditSpinner);
        dateOfBirthEditButton =
                view.findViewById(R.id.dateOfBirthEditButton);

        updateButton = view.findViewById(R.id.updateButton);
        toProfileBtn = view.findViewById(R.id.editToProfileBtn);


        String[] genders = {"Male", "Female", "Other"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genders
        );

        genderAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        genderEditSpinner.setAdapter(genderAdapter);


        loadUserData();


        dateOfBirthEditButton.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            requireContext(),
                            (view1, selectedYear, selectedMonth, selectedDay) -> {

                                String date =
                                        selectedYear + "-" +
                                                String.format("%02d",
                                                        selectedMonth + 1) + "-" +
                                                String.format("%02d",
                                                        selectedDay);

                                dateOfBirthEditButton.setText(date);
                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });


        updateButton.setOnClickListener(v -> {

            String fullname =
                    fullnameEditText.getText().toString().trim();

            String username =
                    usernameEditText.getText().toString().trim();

            String gender =
                    genderEditSpinner.getSelectedItem().toString();

            String dateOfBirth =
                    dateOfBirthEditButton.getText().toString();

            String userId =
                    auth.getCurrentUser().getUid();


            if (!fullname.isEmpty()) {
                db.child(userId).child("fullname").setValue(fullname);
            }


            if (!username.isEmpty()) {
                db.child(userId).child("username").setValue(username);
            }


            if (!gender.isEmpty()) {
                db.child(userId).child("gender").setValue(gender);
            }


            if (!dateOfBirth.equals("Select Date")) {
                db.child(userId).child("dateOfBirth").setValue(dateOfBirth);
            }

            Toast.makeText(
                    requireContext(),
                    "Profile updated!",
                    Toast.LENGTH_SHORT
            ).show();


            NavController navController =
                    Navigation.findNavController(v);

            navController.popBackStack();
        });


        toProfileBtn.setOnClickListener(v -> {

            NavController navController =
                    Navigation.findNavController(v);

            navController.popBackStack();
        });

        return view;
    }

    private void loadUserData() {

        String userId =
                auth.getCurrentUser().getUid();

        db.child(userId).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                DataSnapshot snapshot = task.getResult();

                String fullname =
                        snapshot.child("fullname").getValue(String.class);

                String username =
                        snapshot.child("username").getValue(String.class);

                String gender =
                        snapshot.child("gender").getValue(String.class);

                String dateOfBirth =
                        snapshot.child("dateOfBirth").getValue(String.class);

                if (fullname != null) {
                    fullnameEditText.setText(fullname);
                }

                if (username != null) {
                    usernameEditText.setText(username);
                }

                if (gender != null) {

                    ArrayAdapter<String> adapter =
                            (ArrayAdapter<String>) genderEditSpinner.getAdapter();

                    int position =
                            adapter.getPosition(gender);

                    if (position >= 0) {
                        genderEditSpinner.setSelection(position);
                    }
                }

                if (dateOfBirth != null) {
                    dateOfBirthEditButton.setText(dateOfBirth);
                }
            }
        });

    }
}