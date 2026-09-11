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
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class ProfileEditFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var db: DatabaseReference? = null

    private var fullnameEditText: EditText? = null
    private var usernameEditText: EditText? = null
    private var genderEditSpinner: Spinner? = null
    private var dateOfBirthEditButton: Button? = null
    private var updateButton: Button? = null
    private var toProfileBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_profile_edit,
            container,
            false
        )


        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("users")


        fullnameEditText = view.findViewById<EditText>(R.id.fullnameEditText)
        usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)

        genderEditSpinner = view.findViewById<Spinner>(R.id.genderEditSpinner)
        dateOfBirthEditButton =
            view.findViewById<Button>(R.id.dateOfBirthEditButton)

        updateButton = view.findViewById<Button>(R.id.updateButton)
        toProfileBtn = view.findViewById<Button>(R.id.editToProfileBtn)


        val genders = arrayOf<String?>("Male", "Female", "Other")

        val genderAdapter = ArrayAdapter<String?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            genders
        )

        genderAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        genderEditSpinner!!.setAdapter(genderAdapter)


        loadUserData()


        dateOfBirthEditButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(
                    requireContext(),
                    OnDateSetListener { view1: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                        val date =
                            selectedYear.toString() + "-" + String.format(
                                "%02d",
                                selectedMonth + 1
                            ) + "-" + String.format(
                                "%02d",
                                selectedDay
                            )
                        dateOfBirthEditButton!!.setText(date)
                    },
                    year,
                    month,
                    day
                )
            datePickerDialog.show()
        })


        updateButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val fullname =
                fullnameEditText!!.getText().toString().trim { it <= ' ' }
            val username =
                usernameEditText!!.getText().toString().trim { it <= ' ' }

            val gender =
                genderEditSpinner!!.getSelectedItem().toString()

            val dateOfBirth =
                dateOfBirthEditButton!!.getText().toString()

            val userId =
                auth!!.getCurrentUser()!!.getUid()


            if (!fullname.isEmpty()) {
                db!!.child(userId).child("fullname").setValue(fullname)
            }


            if (!username.isEmpty()) {
                db!!.child(userId).child("username").setValue(username)
            }


            if (!gender.isEmpty()) {
                db!!.child(userId).child("gender").setValue(gender)
            }


            if (dateOfBirth != "Select Date") {
                db!!.child(userId).child("dateOfBirth").setValue(dateOfBirth)
            }

            Toast.makeText(
                requireContext(),
                "Profile updated!",
                Toast.LENGTH_SHORT
            ).show()


            val navController =
                findNavController(v!!)
            navController.popBackStack()
        })


        toProfileBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val navController =
                findNavController(v!!)
            navController.popBackStack()
        })

        return view
    }

    private fun loadUserData() {
        val userId =
            auth!!.getCurrentUser()!!.getUid()

        db!!.child(userId).get()
            .addOnCompleteListener(OnCompleteListener { task: Task<DataSnapshot>? ->
                if (task!!.isSuccessful()) {
                    val snapshot = task.getResult()

                    val fullname =
                        snapshot.child("fullname").getValue<String?>(String::class.java)

                    val username =
                        snapshot.child("username").getValue<String?>(String::class.java)

                    val gender =
                        snapshot.child("gender").getValue<String?>(String::class.java)

                    val dateOfBirth =
                        snapshot.child("dateOfBirth").getValue<String?>(String::class.java)

                    if (fullname != null) {
                        fullnameEditText!!.setText(fullname)
                    }

                    if (username != null) {
                        usernameEditText!!.setText(username)
                    }

                    if (gender != null) {
                        val adapter =
                            genderEditSpinner!!.getAdapter() as ArrayAdapter<String?>

                        val position =
                            adapter.getPosition(gender)

                        if (position >= 0) {
                            genderEditSpinner!!.setSelection(position)
                        }
                    }

                    if (dateOfBirth != null) {
                        dateOfBirthEditButton!!.setText(dateOfBirth)
                    }
                }
            })
    }
}