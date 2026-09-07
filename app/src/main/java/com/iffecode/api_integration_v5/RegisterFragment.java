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

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText fullnameInput;
    private EditText emailInput;
    private Button birthBtn;
    private Spinner genderSpinner;
    private Button registerBtn;




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
                android.R.layout.simple_list_item_1, genders
        );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}