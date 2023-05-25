package com.example.agrolibre.view.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agrolibre.R
import com.example.agrolibre.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    companion object{
        private  const val AUTH_REQUEST_CODE = 1234

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        googleLogin()
        phoneLogin()
        emailLogin()
    }
    fun googleLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
            /*AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),*/
        )
        binding.btnLoginGoogle.setOnClickListener{
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),AUTH_REQUEST_CODE)
        }
    }
    fun phoneLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build()
            /*AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),*/
        )

    }
    fun emailLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
            /*AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),*/
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        try {
            if( requestCode == AUTH_REQUEST_CODE){
                val response:IdpResponse? = IdpResponse.fromResultIntent(data)
                if(resultCode == Activity.RESULT_OK){
                    val user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    Toast.makeText(this, "Bienvenid@ ${user!!.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Toast.makeText(this, "Ocurrío un error ${response!!.error!!.errorCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, "Ocurrío un error", Toast.LENGTH_SHORT).show()
        }

    }
}