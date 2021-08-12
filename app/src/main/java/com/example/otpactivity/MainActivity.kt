package com.example.otpactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.otpactivity.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var mobileNumber: String = ""
    var verificationID: String = ""
    var token_: String = ""
    private lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

            auth = FirebaseAuth.getInstance()

            binding.getOtp.setOnClickListener {
                mobileNumber = "+91"+PhoneNumber.text.toString().trim()

                if (!mobileNumber.isEmpty()){

                    binding.progressBar.visibility = View.VISIBLE

                    logintask()

                }else{
                    binding.PhoneNumber.error = "Enter Your Phone Number"
                }

            }

        }

    private fun logintask() {

        val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding.apply { 
                    progressBar.visibility = View.GONE
                }
                Toast.makeText(this@MainActivity, "Invalid PhoneNumber/Verification failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                    binding.progressBar.visibility = View.GONE
                    verificationID = p0.toString()
                    token_ = p1.toString()


                        binding.PhoneNumber.setText("")

                binding.PhoneNumber.hint = "Enter OTP"
                binding.getOtp.text = "Verify OTP"
                        
                        binding.getOtp.setOnClickListener {
                            binding.progressBar.visibility = View.VISIBLE
                            VerifyAuthentication(verificationID,binding.PhoneNumber.text.toString())
                        }


                Log.e("Login : verificationId ", verificationID)
                Log.e("Login : token ", token_)
            }

            override fun onCodeAutoRetrievalTimeOut(verificationID: String) {
                super.onCodeAutoRetrievalTimeOut(verificationID)
                Toast.makeText(this@MainActivity, "Verification Timeout", Toast.LENGTH_SHORT).show()
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(mobileNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.getOtp.setOnClickListener {
        auth.signInWithCredential(credential)

            .addOnCompleteListener(this
            ) { p0 ->
                if (p0.isSuccessful) {
                    val user = p0.result?.user
                    val intent = Intent(this@MainActivity, c_dashboard::class.java).apply {
                        putExtra("message", "Taking you in")
                    }
                    startActivity(intent)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Invalid Otp", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
    private fun VerifyAuthentication(verificationID: String, otpText: String) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID,otpText)
        signInWithPhoneAuthCredential(phoneAuthCredential)

    }

}


