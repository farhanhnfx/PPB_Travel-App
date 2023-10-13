package com.example.travelapp

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.travelapp.AddTravelPlanActivity.Companion.EXTRA_ASAL
import com.example.travelapp.AddTravelPlanActivity.Companion.EXTRA_CLASS
import com.example.travelapp.AddTravelPlanActivity.Companion.EXTRA_PAKET
import com.example.travelapp.AddTravelPlanActivity.Companion.EXTRA_TGL_PERJALANAN
import com.example.travelapp.AddTravelPlanActivity.Companion.EXTRA_TUJUAN
import com.example.travelapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            binding.cardLastTrip.visibility = View.VISIBLE
            binding.txtCardTgl.text = data?.getStringExtra(EXTRA_TGL_PERJALANAN).toString()
            binding.txtCardAsal.text = data?.getStringExtra(EXTRA_ASAL).toString()
            binding.txtCardTujuan.text = data?.getStringExtra(EXTRA_TUJUAN).toString()
            binding.txtCardClass.text = data?.getStringExtra(EXTRA_CLASS).toString()

            val selectedPaket = data?.getStringArrayExtra(EXTRA_PAKET)
            var formatted = ""
            if (selectedPaket!!.isNotEmpty()) {
                for (i in 0 until selectedPaket!!.size - 1) {
                    formatted += " - " + selectedPaket[i] + "\n"
                }
                formatted += " - " + selectedPaket[selectedPaket.size - 1]
            }
            else {
                formatted = "Tidak ada paket yang kamu pilih"
            }
            binding.txtCardPaket.text = formatted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            datePickerCheck.init(datePickerCheck.year, datePickerCheck.month, datePickerCheck.dayOfMonth) {
                    _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month+1}/$year"
                if (selectedDate == txtCardTgl.text.toString()) {
                    Toast.makeText(this@DashboardActivity, "Sudah ada rencana perjalanan!", Toast.LENGTH_SHORT).show()
                }
            }
            btnAddPlan.setOnClickListener {
                val intent = Intent(this@DashboardActivity, AddTravelPlanActivity::class.java)
                launcher.launch(intent)
            }
        }
    }
}