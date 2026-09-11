package com.iffecode.api_integration_v5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var db: DatabaseReference? = null

    private var fullnameText: TextView? = null
    private var usernameText: TextView? = null
    private var emailText: TextView? = null
    private var genderText: TextView? = null
    private var dateOfBirthText: TextView? = null

    private var profileToHomeBtn: Button? = null
    private var profileToEditBtn: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance()
            .getReference("users")

        fullnameText = view.findViewById<TextView>(R.id.fullnameText)
        usernameText = view.findViewById<TextView>(R.id.usernameText)
        emailText = view.findViewById<TextView>(R.id.emailText)
        genderText = view.findViewById<TextView>(R.id.genderText)
        dateOfBirthText = view.findViewById<TextView>(R.id.dateOfBirthText)

        profileToHomeBtn =
            view.findViewById<Button>(R.id.ProfileToHomeBtn)

        profileToEditBtn =
            view.findViewById<Button>(R.id.ProfileToEditBtn)

        loadUserData()

        profileToHomeBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val activity =
                requireActivity() as MainActivity2
            activity.showHome()
        })

        profileToEditBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val navController = findNavController(v!!)
            navController.navigate(R.id.profileEditFragment)
        })



        return view
    }

    private fun loadUserData() {
        if (auth!!.getCurrentUser() == null) {
            return
        }

        val userId =
            auth!!.getCurrentUser()!!.getUid()

        db!!.child(userId).get()
            .addOnCompleteListener(OnCompleteListener { task: Task<DataSnapshot>? ->
                if (task!!.isSuccessful()) {
                    val snapshot = task.getResult()

                    val fullname =
                        snapshot.child("fullname")
                            .getValue<String?>(String::class.java)

                    val username =
                        snapshot.child("username")
                            .getValue<String?>(String::class.java)

                    val email =
                        snapshot.child("email")
                            .getValue<String?>(String::class.java)

                    val gender =
                        snapshot.child("gender")
                            .getValue<String?>(String::class.java)

                    val dateOfBirth =
                        snapshot.child("dateOfBirth")
                            .getValue<String?>(String::class.java)


                    if (fullname != null) {
                        fullnameText!!.setText(
                            "Full name: " + fullname
                        )
                    }

                    if (username != null) {
                        usernameText!!.setText(
                            "Username: " + username
                        )
                    }

                    if (email != null) {
                        emailText!!.setText(
                            "Email: " + email
                        )
                    }

                    if (gender != null) {
                        genderText!!.setText(
                            "Gender: " + gender
                        )
                    }

                    if (dateOfBirth != null) {
                        dateOfBirthText!!.setText(
                            "Date of birth: " + dateOfBirth
                        )
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not load profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}