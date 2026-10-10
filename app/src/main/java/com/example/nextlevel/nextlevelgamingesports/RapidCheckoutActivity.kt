package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class RapidCheckoutActivity : BaseActivity() {

    private companion object {
        const val HOURS = 4
        const val RATE_PER_RIG_HOUR = 25     // placeholder price - change to your real rate
        const val VIP_LOUNGE = 500
        const val SERVER_HOST = 300
        const val CATERING = 400
        const val GROUP_DISCOUNT_PCT = 10
        val RIG_OPTIONS = intArrayOf(5, 10, 15, 20)
    }

    private var rigs = 10
    private var total = 0

    private lateinit var cbVip: CheckBox
    private lateinit var cbServer: CheckBox
    private lateinit var cbCatering: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapid_checkout)

        cbVip = findViewById(R.id.cbVipLounge)
        cbServer = findViewById(R.id.cbServerHost)
        cbCatering = findViewById(R.id.cbCatering)

        findViewById<View>(R.id.tvRigSelection).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Number of rigs")
                .setItems(RIG_OPTIONS.map { "$it Rigs" }.toTypedArray()) { _, i ->
                    rigs = RIG_OPTIONS[i]
                    recalc()
                }
                .show()
        }
        listOf(cbVip, cbServer, cbCatering).forEach { it.setOnCheckedChangeListener { _, _ -> recalc() } }

        findViewById<View>(R.id.btnExpressCheckout).setOnClickListener {
            // The quote already includes its own group discount, so it goes in as a single line.
            CartManager.clear()
            CartManager.add(CartItem("Custom Rig & Party ($rigs rigs, $HOURS hrs)", total))
            goTo<CheckoutActivity>()
        }

        recalc()
    }

    private fun recalc() {
        val rigsCost = rigs * HOURS * RATE_PER_RIG_HOUR
        var addOns = 0
        if (cbVip.isChecked) addOns += VIP_LOUNGE
        if (cbServer.isChecked) addOns += SERVER_HOST
        if (cbCatering.isChecked) addOns += CATERING

        val discount = (rigsCost + addOns) * GROUP_DISCOUNT_PCT / 100
        total = rigsCost + addOns - discount

        findViewById<TextView>(R.id.tvRigSelection).text = "$rigs Rigs Selected & $HOURS Hours"
        findViewById<TextView>(R.id.tvRigsLabel).text = "${rigs}x RTX 4080 Rigs"
        findViewById<TextView>(R.id.tvRigsCost).text = formatRand(rigsCost)
        findViewById<TextView>(R.id.tvAddOnsCost).text = formatRand(addOns)
        findViewById<TextView>(R.id.tvDiscountAmt).text = "-${formatRand(discount)}"
        findViewById<TextView>(R.id.tvQuoteTotal).text = formatRand(total)
    }
}
