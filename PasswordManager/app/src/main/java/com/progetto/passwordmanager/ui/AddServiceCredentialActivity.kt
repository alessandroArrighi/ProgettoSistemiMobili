package com.progetto.passwordmanager.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.progetto.passwordmanager.R

class AddServiceCredentialActivity() : AppCompatActivity() {

    private lateinit var toastName: String

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_credential)

        val update = intent.getStringExtra("oldService")
        toastName = resources.getString(R.string.addServiceToast)

        if(update != null) {
            findViewById<EditText>(R.id.txtService).setText(
                intent.getStringExtra("oldService")
            )

            findViewById<EditText>(R.id.txtUser).setText(
                intent.getStringExtra("oldUser")
            )

            findViewById<EditText>(R.id.txtPassword).setText(
                intent.getStringExtra("oldPassword")
            )

            findViewById<TextView>(R.id.title).text = resources.getString(R.string.modServiceTitile)

            toastName = resources.getString(R.string.modServiceToast)
        }

        findViewById<Button>(R.id.btnAddService)
            .setOnClickListener {
                val txtService = findViewById<TextInputEditText>(R.id.txtService).text.toString()
                val txtUser = findViewById<TextInputEditText>(R.id.txtUser).text.toString()
                val txtPassword = findViewById<TextInputEditText>(R.id.txtPassword).text.toString()
                sendData(txtService, txtUser, txtPassword)
            }
    }

    private fun sendData(txtService: String, txtUser: String, txtPassword: String) {
        val returnIntent = intent

        returnIntent.putExtra("resultService", txtService)
        returnIntent.putExtra("resultUser", txtUser)
        returnIntent.putExtra("resultPassword", txtPassword)
        setResult(RESULT_OK, returnIntent)
        Toast.makeText(this, toastName, Toast.LENGTH_SHORT).show()
        finish();
    }
}