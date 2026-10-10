package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView

class CheckoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val total = formatRand(CartManager.total)
        findViewById<TextView>(R.id.tvTotalPayable).text = total
        findViewById<Button>(R.id.btnPay).text = "Pay $total"
        findViewById<TextView>(R.id.tvSavings).text =
            if (CartManager.discountPercent > 0)
                "Saved ${CartManager.discountPercent}% (${formatRand(CartManager.discountAmount)}) with Multi-Booking Discount"
            else
                "No discount applied"

        val group = findViewById<RadioGroup>(R.id.rgPayment)

        findViewById<View>(R.id.btnPay).setOnClickListener {
            if (CartManager.items.isEmpty()) {
                toast("Your cart is empty")
                return@setOnClickListener
            }
            val method = when (group.checkedRadioButtonId) {
                R.id.rbCard -> "card"
                R.id.rbZapper -> "SnapScan / Zapper"
                else -> "instant EFT"
            }
            // TODO: hand off to your payment provider here (PayFast, Ozow, Yoco, Peach, ...)
            toast("Paying by $method")
            CartManager.clear()
            goTo<BookingConfirmedActivity>()
        }
    }
}
