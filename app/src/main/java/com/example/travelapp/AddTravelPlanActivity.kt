package com.example.travelapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.travelapp.databinding.ActivityAddTravelPlanBinding
import com.google.android.material.button.MaterialButton
import kotlin.math.abs

class AddTravelPlanActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityAddTravelPlanBinding
    private val listClass = mapOf<String, Int>(
//        "Pilih Kelas" to 0,
        "Ekonomi" to 30000,
        "Bisnis" to 40000,
        "Eksekutif" to 50000,
    )
    private val listPaket = mapOf<String, Int>(
        "Makan siang" to 15000,
        "Duduk pinggir jendela" to 10000,
        "Duduk bagian depan" to 5000,
        "Wi-Fi" to 3000,
        "Asuransi" to 30000,
        "Bantal & Selimut" to 15000,
        "TV & Film" to 20000
    )

    private var hargaPrimer =0
    private var hargaClass = 0
    private var hargaPaket = 0
    private var stringTglPerjalanan = ""
    private var selectedPaket = mutableListOf<String>()

    companion object {
        const val EXTRA_TGL_PERJALANAN = "extra_tgl_perjalanan"
        const val EXTRA_ASAL = "extra_asal"
        const val EXTRA_TUJUAN = "extra_tujuan"
        const val EXTRA_CLASS = "extra_class"
        const val EXTRA_PAKET = "extra_paket"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTravelPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            btnShowCalendar.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "tgl_perjalanan")
            }

            spinnerAsal.adapter = ArrayAdapter<String>(this@AddTravelPlanActivity, android.R.layout.simple_spinner_item,
                                                        resources.getStringArray(R.array.stasiun_name))
            spinnerTujuan.adapter = ArrayAdapter<String>(this@AddTravelPlanActivity, android.R.layout.simple_spinner_item,
                                                        resources.getStringArray(R.array.stasiun_name))
            spinnerAsal.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        hargaPrimer = calculateHargaPrimer()
                        updateHargaView()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            spinnerTujuan.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        hargaPrimer = calculateHargaPrimer()
                        updateHargaView()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

            spinnerClass.adapter = ArrayAdapter<String>(this@AddTravelPlanActivity, android.R.layout.simple_spinner_item,
                                                        listClass.keys.toList())
            spinnerClass.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        hargaClass = listClass[spinnerClass.selectedItem.toString()]!!
                        updateHargaView()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

            toggleGroupPaket.addOnButtonCheckedListener { group, checkedId, isChecked ->
                val selected = findViewById<MaterialButton>(checkedId)
                val nama = selected.text.toString()
                val harga = listPaket[selected.text.toString()]
                if (isChecked) {
                    if (harga != null) {
                        hargaPaket += harga
                        selectedPaket.add(nama)
                    }
                    selected.setBackgroundColor(getColor(R.color.myrtie_green))
                    selected.setTextColor(getColor(R.color.white))
                }
                else {
                    if (harga != null) {
                        hargaPaket -= harga
                        selectedPaket.remove(nama)
                    }
                    selected.setBackgroundColor(getColor(R.color.white))
                    selected.setTextColor(getColor(R.color.dark))
                }
                updateHargaView()
            }

            btnPesan.setOnClickListener {
                if (stringTglPerjalanan == "") {
                    Toast.makeText(this@AddTravelPlanActivity, "Mohon pilih tanggal perjalanan!", Toast.LENGTH_SHORT).show()
                }
                else if (spinnerAsal.selectedItemPosition == spinnerTujuan.selectedItemPosition) {
                    Toast.makeText(this@AddTravelPlanActivity, "Mohon pilih tujuan perjalanan!", Toast.LENGTH_SHORT).show()
                }
                else {
                    val intent = Intent().apply {
                        putExtra(EXTRA_TGL_PERJALANAN, stringTglPerjalanan)
                        putExtra(EXTRA_ASAL, spinnerAsal.selectedItem.toString())
                        putExtra(EXTRA_TUJUAN, spinnerTujuan.selectedItem.toString())
                        putExtra(EXTRA_CLASS, spinnerClass.selectedItem.toString())
                        putExtra(EXTRA_PAKET, selectedPaket.toTypedArray())
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun updateHargaView() {
        binding.txtHarga.text = "Rp${hargaPrimer + hargaClass + hargaPaket}"
    }
    private fun calculateHargaPrimer(): Int {
        val diff = abs(binding.spinnerAsal.selectedItemPosition - binding.spinnerTujuan.selectedItemPosition)
        return diff * 10000
    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val calendar = Calendar.getInstance()
        val yearNow = calendar.get(Calendar.YEAR)
        val monthNow = calendar.get(Calendar.MONTH)
        val dayNow = calendar.get(Calendar.DAY_OF_MONTH)
        var valid = false
//
//        if (year == yearNow) {
//            if (month == monthNow) {
//                if (dayOfMonth >= dayNow) {
//                    valid = true
//                }
//            }
//        }
        if (dayOfMonth >= dayNow) {
            if (month == monthNow && year == yearNow) {
                valid = true
            }
        }
        else {
            if (month > monthNow && year >= yearNow) {
                valid = true
            }
        }
        if (valid) {
            binding.btnShowCalendar.text = "$dayOfMonth ${monthToString(month)} $year"
            stringTglPerjalanan = "$dayOfMonth/${month + 1}/$year"
        }
    }
    private fun monthToString(month: Int): String {
        return when(month) {
            0 -> "Januari"
            1 -> "Februari"
            2 -> "Maret"
            3 -> "April"
            4 -> "Mei"
            5 -> "Juni"
            6 -> "Juli"
            7 -> "Agustus"
            8 -> "September"
            9 -> "Oktober"
            10 -> "November"
            11 -> "Desember"
            else -> "Tidak valid"
        }
    }
}