package com.example.otpactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.otpactivity.databinding.ActivityCDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class c_dashboard : AppCompatActivity() {
    private lateinit var binding : ActivityCDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_c_dashboard)

        binding.signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent_ = Intent(this, MainActivity::class.java)
            intent_.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent_.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent_)
            finish()
        }

    }
}