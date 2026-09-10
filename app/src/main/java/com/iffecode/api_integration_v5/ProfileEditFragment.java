package com.iffecode.api_integration_v5;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileEditFragment extends Fragment {


    public ProfileEditFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_edit,
                container,
                false);
        
        Button toProfileBtn = view.findViewById(R.id.editToProfileBtn);
        
        toProfileBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });
        // Inflate the layout for this fragment
        return view;
    }
}