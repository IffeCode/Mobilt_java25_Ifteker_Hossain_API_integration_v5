package com.iffecode.api_integration_v5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    private var textView6: TextView? = null
    private var textView7: TextView? = null

    private var profileBtn: Button? = null
    private var weatherBtn: Button? = null

    private var navHostFragment: View? = null

    private var logoutBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main2)


        textView6 = findViewById<TextView>(R.id.textView6)
        textView7 = findViewById<TextView>(R.id.textView7)

        profileBtn = findViewById<Button>(R.id.profileBtn)
        weatherBtn = findViewById<Button>(R.id.weatherBtn)

        logoutBtn = findViewById<Button>(R.id.logoutBtn)

        val navHost =
            getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        navHostFragment = findViewById<View>(R.id.nav_host_fragment)


        val navController =
            navHost!!.navController

        navHostFragment!!.setVisibility(View.GONE)


        profileBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            textView6!!.setVisibility(View.GONE)
            textView7!!.setVisibility(View.GONE)
            profileBtn!!.setVisibility(View.GONE)
            weatherBtn!!.setVisibility(View.GONE)

            navHostFragment!!.setVisibility(View.VISIBLE)
            navController.navigate(R.id.profileFragment)
        })


        weatherBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            textView6!!.setVisibility(View.GONE)
            textView7!!.setVisibility(View.GONE)
            profileBtn!!.setVisibility(View.GONE)
            weatherBtn!!.setVisibility(View.GONE)

            navHostFragment!!.setVisibility(View.VISIBLE)
            navController.navigate(R.id.weatherFragment)
        })


        logoutBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity2, MainActivity::class.java)

            startActivity(intent)
            finish()
        })




        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    fun showHome() {
        navHostFragment!!.setVisibility(View.GONE)

        textView6!!.setVisibility(View.VISIBLE)
        textView7!!.setVisibility(View.VISIBLE)
        profileBtn!!.setVisibility(View.VISIBLE)
        weatherBtn!!.setVisibility(View.VISIBLE)
    }
}