package com.example.nextlevel.nextlevelgamingesports

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat

class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnTogglePassword = findViewById<ImageView>(R.id.btnTogglePassword)

        var isPasswordVisible = false
        btnTogglePassword?.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nl_green))
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nl_lavender))
            }
            etPassword.setSelection(etPassword.text.length)
        }

        findViewById<View>(R.id.btnSignIn).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            CartManager.customerEmail = email
            toast("Welcome back!")
            goTo<MainActivity>()
            finish()
        }
    }
}
