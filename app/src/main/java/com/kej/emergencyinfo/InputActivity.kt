package com.kej.emergencyinfo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kej.emergencyinfo.Content.BIRTH_DATE
import com.kej.emergencyinfo.Content.BLOODY_TYPE
import com.kej.emergencyinfo.Content.CAUTIONS
import com.kej.emergencyinfo.Content.NAME
import com.kej.emergencyinfo.Content.PHONE_NUMBER
import com.kej.emergencyinfo.Content.USER_INFORMATION
import com.kej.emergencyinfo.databinding.ActivityInputBinding
import java.time.LocalDateTime

class InputActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityInputBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchData()

        val currentTime = LocalDateTime.now()

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            binding.birthDateValueTextView.text = "$year-${month.inc()}-$dayOfMonth"
        }

        binding.bloodyTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this, R.array.bloody_types, android.R.layout.simple_list_item_1
        )

        binding.birthDataLayer.setOnClickListener {
            DatePickerDialog(
                this,
                listener,
                currentTime.year,
                currentTime.monthValue - 1,
                currentTime.dayOfMonth
            ).show()
            Log.d("캘린더", "${currentTime.year}  ${currentTime.monthValue}   ${currentTime.dayOfMonth}")
        }

        binding.cautionsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.cautionsEditText.isVisible = isChecked
        }

        binding.saveButton.setOnClickListener {
            saveData()
            finish()
        }

    }

    private fun fetchData() = with(binding) {
        cautionsEditText.isVisible = cautionsCheckBox.isChecked

    }

    private fun saveData() {
        getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit().apply {
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOODY_TYPE, getBloodyType())
            putString(PHONE_NUMBER, binding.phoneNumberEditText.text.toString())
            putString(BIRTH_DATE, binding.birthDateValueTextView.text.toString())
            putString(CAUTIONS, getCautions())
            apply()
        }
        Toast.makeText(this, "저장을 완료하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodyType(): String {
        val bloodyAlphabet = binding.bloodyTypeSpinner.selectedItem.toString()
        val bloodySign = if (binding.bloodyTypePlus.isChecked) "+" else "-"
        return "$bloodySign$bloodyAlphabet"
    }

    private fun getCautions(): String {
        return if (binding.cautionsCheckBox.isChecked) {
            binding.cautionsEditText.text.toString()
        } else {
            ""
        }
    }
}
