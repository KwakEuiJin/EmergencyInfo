package com.kej.emergencyinfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.kej.emergencyinfo.Content.BIRTH_DATE
import com.kej.emergencyinfo.Content.BLOODY_TYPE
import com.kej.emergencyinfo.Content.CAUTIONS
import com.kej.emergencyinfo.Content.NAME
import com.kej.emergencyinfo.Content.PHONE_NUMBER
import com.kej.emergencyinfo.Content.USER_INFORMATION
import com.kej.emergencyinfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        binding.deleteButton.setOnClickListener {
            deleteData()
        }

        binding.phoneNumberValueTextView.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)){
                val phoneNumber = binding.phoneNumberValueTextView.text.toString().replace("-","")
                data = Uri.parse("tel:$phoneNumber")
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).apply {
            with(binding) {
                nameValueTextView.text = getString(NAME, "미정")
                birthDateValueTextView.text = getString(BIRTH_DATE, "")
                bloodyTypeValueTextView.text = getString(BLOODY_TYPE, "")
                phoneNumberValueTextView.text = getString(PHONE_NUMBER, "")
                val cautions = getString(CAUTIONS, "")
                cautionsTextView.isGone = cautions.isNullOrEmpty()
                cautionsValueTextView.isGone = cautions.isNullOrEmpty()
                cautionsValueTextView.text = cautions
            }

        }
    }

    private fun deleteData() {
        getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit().apply {
            clear()
            apply()
        }
        getData()
        Toast.makeText(this, "데이터를 삭제하였습니다.", Toast.LENGTH_SHORT).show()
    }

}