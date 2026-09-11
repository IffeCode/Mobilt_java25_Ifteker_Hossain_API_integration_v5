package com.iffecode.api_integration_v5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null


    var email: EditText? = null
    var password: EditText? = null
    var loginbtn: Button? = null
    var registerBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        email = findViewById<EditText>(R.id.loginEmail)
        password = findViewById<EditText>(R.id.loginPassword)
        loginbtn = findViewById<Button>(R.id.loginBtn)
        registerBtn = findViewById<Button>(R.id.registerBtn)


        loginbtn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val emailText = email!!.getText().toString().trim { it <= ' ' }
                val passwordText = password!!.getText().toString().trim { it <= ' ' }

                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please fill in email and password",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    return
                }

                auth!!.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(OnCompleteListener { task: Task<AuthResult?>? ->
                        if (task!!.isSuccessful()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            val i = Intent(this@MainActivity, MainActivity2::class.java)
                            startActivity(i)

                            //finish(); //Användaren kan inte trycka bakåt på android knappen och komma tillbaka till login skämen
                        } else {
                            Toast.makeText(
                                this@MainActivity, "Login failed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        })

        registerBtn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val container = findViewById<FrameLayout>(R.id.registerFragmentContainer)
                container.setVisibility(View.VISIBLE)

                val fragment = RegisterFragment()
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registerFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })




        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }
}