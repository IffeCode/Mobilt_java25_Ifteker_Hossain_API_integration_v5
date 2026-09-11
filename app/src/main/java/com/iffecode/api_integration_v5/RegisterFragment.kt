package com.iffecode.api_integration_v5

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class RegisterFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var db: DatabaseReference? = null


    private var usernameInput: EditText? = null
    private var passwordInput: EditText? = null
    private var fullnameInput: EditText? = null
    private var emailInput: EditText? = null
    private var birthBtn: Button? = null
    private var genderSpinner: Spinner? = null
    private var registerBtn: Button? = null

    private var exitBtn: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_register,
            container,
            false
        )

        usernameInput = view.findViewById<EditText>(R.id.registerUsernameInput)
        passwordInput = view.findViewById<EditText>(R.id.registerPasswordTextPassword)
        fullnameInput = view.findViewById<EditText>(R.id.registerFullnameInput)
        emailInput = view.findViewById<EditText>(R.id.registerEmailInput)

        birthBtn = view.findViewById<Button>(R.id.birthBtn)
        genderSpinner = view.findViewById<Spinner>(R.id.genderEditSpinner)
        registerBtn = view.findViewById<Button>(R.id.registerCompleteBtn)

        exitBtn = view.findViewById<Button>(R.id.exitRegisteBtn)


        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("users")



        birthBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                OnDateSetListener { view1: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val date = selectedYear.toString() + "-" + String.format(
                        "%02d", selectedMonth
                                + 1
                    ) + "-" + String.format("%02d", selectedDay)
                    birthBtn!!.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        })

        val genders = arrayOf<String?>("Male", "Female", "Other")
        val genderAdapter = ArrayAdapter<String?>(
            requireContext(),
            android.R.layout.simple_spinner_item, genders
        )

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        genderSpinner!!.setAdapter(genderAdapter)


        registerBtn!!.setOnClickListener(View.OnClickListener { v: View? ->

            val username = usernameInput!!.getText().toString().trim()
            val password = passwordInput!!.getText().toString().trim()
            val fullname = fullnameInput!!.getText().toString().trim()
            val email = emailInput!!.getText().toString().trim()

            val gender = genderSpinner!!.getSelectedItem().toString()
            val dateOfBirth = birthBtn!!.getText().toString()

            if (
                username.isEmpty() ||
                password.isEmpty() ||
                fullname.isEmpty() ||
                email.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields!",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (
                password.length < 8 ||
                !password.any { it.isUpperCase() } ||
                !password.any { it.isLowerCase() } ||
                !password.any { it.isDigit() }
            ) {

                Toast.makeText(
                    requireContext(),
                    "Password must contain at least 8 characters, one uppercase letter, one lowercase letter and one number!",
                    Toast.LENGTH_LONG
                ).show()

            } else if (dateOfBirth == "Select Date") {

                Toast.makeText(
                    requireContext(),
                    "Please select date of birth!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            val userId = auth!!.currentUser!!.uid

                            db!!.child(userId).child("username")
                                .setValue(username)

                            db!!.child(userId).child("fullname")
                                .setValue(fullname)

                            db!!.child(userId).child("email")
                                .setValue(email)

                            db!!.child(userId).child("gender")
                                .setValue(gender)

                            db!!.child(userId).child("dateOfBirth")
                                .setValue(dateOfBirth)

                            Toast.makeText(
                                requireContext(),
                                "Registration was a success!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Registration failed, try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })


        exitBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            requireActivity().getSupportFragmentManager().popBackStack()
        })

        return view
    }
}