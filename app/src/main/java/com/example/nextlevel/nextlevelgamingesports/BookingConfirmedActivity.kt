package com.example.nextlevel.nextlevelgamingesports

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback

class BookingConfirmedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmed)

        findViewById<View>(R.id.tvViewPass).setOnClickListener {
            // TODO: show a QR code for the booking
            toast("QR pass coming soon")
        }

        findViewById<View>(R.id.btnLogOut).setOnClickListener {
            goTo<SignInActivity>(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        // Back goes Home instead of returning to the (already paid) checkout.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goTo<HomeActivity>(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        })
    }
}
