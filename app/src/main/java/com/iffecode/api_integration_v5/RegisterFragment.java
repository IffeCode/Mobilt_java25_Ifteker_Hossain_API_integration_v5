package com.iffecode.api_integration_v5;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference db;


    private EditText usernameInput;
    private EditText passwordInput;
    private EditText fullnameInput;
    private EditText emailInput;
    private Button birthBtn;
    private Spinner genderSpinner;
    private Button registerBtn;

    private Button exitBtn;




    public RegisterFragment() {
        // Required empty public constructor
    }



    @NonNull //Får inte vara null och måste fyllas i med ett värde!
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,
                container, false);

        usernameInput = view.findViewById(R.id.registerUsernameInput);
        passwordInput = view.findViewById(R.id.registerPasswordTextPassword);
        fullnameInput = view.findViewById(R.id.registerFullnameInput);
        emailInput = view.findViewById(R.id.registerEmailInput);

        birthBtn = view.findViewById(R.id.birthBtn);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        registerBtn = view.findViewById(R.id.registerCompleteBtn);

        exitBtn = view.findViewById(R.id.exitRegisteBtn);


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users");



        birthBtn.setOnClickListener(v -> {
            Calendar calender = Calendar.getInstance();

            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" + String.format("%02d", selectedMonth
                                + 1) + "-" + String.format("%02d", selectedDay);

                        birthBtn.setText(date);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();

        });

        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item, genders
        );

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderAdapter);

        registerBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String fullname = fullnameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            String gender = genderSpinner.getSelectedItem().toString();
            String dateOfBirth = birthBtn.getText().toString();



            if (username.isEmpty() || password.isEmpty() ||
            fullname.isEmpty() || email.isEmpty()){
                Toast.makeText(requireContext(), "Please fill in all fields!",
                        Toast.LENGTH_SHORT).show();;

                        return;
            }

            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

            if (!password.matches(passwordRegex)) {
                Toast.makeText(
                        requireContext(),
                        "Password must contain at least 8 characters, one uppercase letter, one lowercase letter and one number!",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (dateOfBirth.equals("Select Date")){
                Toast.makeText(requireContext(), "Please select date of birth!",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                            String userId = auth.getCurrentUser().getUid();

                            db.child(userId).child("username")
                                            .setValue(username);
                            db.child(userId).child("fullname")
                                            .setValue(fullname);
                            db.child(userId).child("email")
                                            .setValue(email);
                            db.child(userId).child("gender")
                                            .setValue(gender);
                            db.child(userId).child("dateOfBirth")
                                            .setValue(dateOfBirth);

                            Toast.makeText(requireContext(), "Registration was a success!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(),"Registration failed, try again!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
        });

        exitBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}