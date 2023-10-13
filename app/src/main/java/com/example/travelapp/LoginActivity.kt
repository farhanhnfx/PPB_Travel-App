package com.example.travelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.example.travelapp.RegisterActivity.Companion.EXTRA_EMAIL
import com.example.travelapp.RegisterActivity.Companion.EXTRA_PASSWORD
import com.example.travelapp.RegisterActivity.Companion.EXTRA_USERNAME
import com.example.travelapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra(EXTRA_EMAIL)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val password = intent.getStringExtra(EXTRA_PASSWORD)

        with (binding) {
            btnLogin.setOnClickListener {
                if ((editEmail.text.toString() == email || editEmail.text.toString() == username)
                    && editPassword.text.toString() == password) {
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                }
            }
            btnShowPassword.setOnClickListener {
                if (editPassword.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                }
                else if (editPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    editPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
    }
}