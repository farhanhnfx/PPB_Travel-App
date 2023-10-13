package com.example.travelapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.travelapp.databinding.ActivityRegisterBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityRegisterBinding
    private var canRegister = false
    companion object {
        const val EXTRA_EMAIL = "extra_email";
        const val EXTRA_USERNAME = "extra_username";
        const val EXTRA_PASSWORD = "extra_password";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            btnShowTglLahir.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "tgl_lahir")
            }
            btnRegister.setOnClickListener {
                if (!canRegister) {
                    Toast.makeText(this@RegisterActivity, "Umur belum mencukupi!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (editEmail.text.isEmpty() || editUsername.text.isEmpty() || editPassword.text.isEmpty()) {
                        Toast.makeText(this@RegisterActivity, "Isi semua kolom pendaftaran!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val email = editEmail.text.toString()
                        val username = editUsername.text.toString()
                        val password = editPassword.text.toString()
                        val intentToLoginActivity =
                            Intent(this@RegisterActivity, LoginActivity::class.java)
                                .apply {
                                    putExtra(EXTRA_EMAIL, email)
                                    putExtra(EXTRA_USERNAME, username)
                                    putExtra(EXTRA_PASSWORD, password)
                                }
                        startActivity(intentToLoginActivity)
                    }
                }
            }
        }
    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        with (binding) {
            btnShowTglLahir.text = "$dayOfMonth/${month+1}/$year"
            canRegister = checkUserAges(dayOfMonth, month, year)
        }
    }

    private fun checkUserAges(dd:Int, mm:Int, yyyy:Int): Boolean {
        val calendar = Calendar.getInstance()
        val yearNow = calendar.get(Calendar.YEAR)
        val monthNow = calendar.get(Calendar.MONTH)
        val dayNow = calendar.get(Calendar.DAY_OF_MONTH)

        if (yearNow - yyyy > 15) {
            return true
        }
        else if (yearNow - yyyy == 15) {
            if (monthNow - mm > 1) {
                return true
            }
            else if (monthNow - mm == 0) {
                if (dayNow - dd >= 0) {
                    return true
                }
            }
        }
        return false
    }

//    private fun getUserAges(tgl_lahir:String): Int {
//        val tmp = SimpleDateFormat("dd/MM/yyyy").parse(tgl_lahir)
//        val now = Date()
//        val diff = now.time - tmp.time
//
//    }
}