package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CustomQuoteActivity : BaseActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText

    private lateinit var cbUltimateGamer: CheckBox
    private lateinit var cbVipExperience: CheckBox
    private lateinit var cbEsportsTraining: CheckBox
    private lateinit var cbBirthdayParty: CheckBox

    private lateinit var cbVrExperience: CheckBox
    private lateinit var cbRacingSim: CheckBox
    private lateinit var cbEscapeRoom: CheckBox

    private lateinit var panelQuoteBreakdown: View
    private lateinit var tvQuotedCustomerInfo: TextView
    private lateinit var tvSelectedItemsSummary: TextView
    private lateinit var tvQuoteSubtotal: TextView
    private lateinit var tvQuoteDiscount: TextView
    private lateinit var tvQuoteVat: TextView
    private lateinit var tvQuoteFinalTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_quote)

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)

        cbUltimateGamer = findViewById(R.id.cbUltimateGamer)
        cbVipExperience = findViewById(R.id.cbVipExperience)
        cbEsportsTraining = findViewById(R.id.cbEsportsTraining)
        cbBirthdayParty = findViewById(R.id.cbBirthdayParty)

        cbVrExperience = findViewById(R.id.cbVrExperience)
        cbRacingSim = findViewById(R.id.cbRacingSim)
        cbEscapeRoom = findViewById(R.id.cbEscapeRoom)

        panelQuoteBreakdown = findViewById(R.id.panelQuoteBreakdown)
        tvQuotedCustomerInfo = findViewById(R.id.tvQuotedCustomerInfo)
        tvSelectedItemsSummary = findViewById(R.id.tvSelectedItemsSummary)
        tvQuoteSubtotal = findViewById(R.id.tvQuoteSubtotal)
        tvQuoteDiscount = findViewById(R.id.tvQuoteDiscount)
        tvQuoteVat = findViewById(R.id.tvQuoteVat)
        tvQuoteFinalTotal = findViewById(R.id.tvQuoteFinalTotal)

        // Pre-fill existing cart selections if any
        preselectCartItems()

        findViewById<View>(R.id.btnCalculateTotal).setOnClickListener {
            calculateAndDisplayQuote()
        }

        findViewById<View>(R.id.btnSubmitBookingRequest).setOnClickListener {
            submitBookingRequest()
        }
    }

    private fun getSelectedOfferings(): List<CartItem> {
        val selected = mutableListOf<CartItem>()
        if (cbUltimateGamer.isChecked) selected.add(Catalog.gamingPackages[0])
        if (cbVipExperience.isChecked) selected.add(Catalog.gamingPackages[1])
        if (cbEsportsTraining.isChecked) selected.add(Catalog.gamingPackages[2])
        if (cbBirthdayParty.isChecked) selected.add(Catalog.gamingPackages[3])

        if (cbVrExperience.isChecked) selected.add(Catalog.individualExperiences[0])
        if (cbRacingSim.isChecked) selected.add(Catalog.individualExperiences[1])
        if (cbEscapeRoom.isChecked) selected.add(Catalog.individualExperiences[2])
        return selected
    }

    private fun preselectCartItems() {
        val currentCartNames = CartManager.items.map { it.name }.toSet()
        if (currentCartNames.contains("Ultimate Gamer Pass")) cbUltimateGamer.isChecked = true
        if (currentCartNames.contains("VIP Gaming Experience")) cbVipExperience.isChecked = true
        if (currentCartNames.contains("Esports Training Package")) cbEsportsTraining.isChecked = true
        if (currentCartNames.contains("Birthday Party Package")) cbBirthdayParty.isChecked = true

        if (currentCartNames.contains("Virtual Reality Experience")) cbVrExperience.isChecked = true
        if (currentCartNames.contains("Racing Simulator Challenge")) cbRacingSim.isChecked = true
        if (currentCartNames.contains("Escape Room Challenge")) cbEscapeRoom.isChecked = true

        if (CartManager.customerName.isNotEmpty()) etName.setText(CartManager.customerName)
        if (CartManager.customerPhone.isNotEmpty()) etPhone.setText(CartManager.customerPhone)
        if (CartManager.customerEmail.isNotEmpty()) etEmail.setText(CartManager.customerEmail)
    }

    private fun calculateAndDisplayQuote(): Boolean {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty()) {
            etName.error = "Please enter your full name"
            etName.requestFocus()
            return false
        }
        if (phone.isEmpty()) {
            etPhone.error = "Please enter your phone number"
            etPhone.requestFocus()
            return false
        }
        if (email.isEmpty()) {
            etEmail.error = "Please enter your email address"
            etEmail.requestFocus()
            return false
        }

        val selectedItems = getSelectedOfferings()
        if (selectedItems.isEmpty()) {
            toast("Please select at least one gaming package or experience")
            return false
        }

        // Store selection in shared CartManager array
        CartManager.customerName = name
        CartManager.customerPhone = phone
        CartManager.customerEmail = email
        CartManager.setItems(selectedItems)

        // Calculate
        val subtotal = CartManager.subtotal
        val discountPct = CartManager.discountPercent
        val discountAmt = CartManager.discountAmount
        val netSubtotal = CartManager.subtotalAfterDiscount
        val vat = CartManager.vatAmount
        val finalTotal = CartManager.total

        // Render breakdown
        tvQuotedCustomerInfo.text = "Quotation for: $name ($phone | $email)"

        val summaryText = buildString {
            append("Selected Offerings (${selectedItems.size}):\n")
            selectedItems.forEachIndexed { i, item ->
                append("${i + 1}. ${item.name} (${item.category}) — ${formatRand(item.price)}\n")
            }
        }
        tvSelectedItemsSummary.text = summaryText.trim()

        tvQuoteSubtotal.text = "Subtotal: ${formatRand(subtotal)}"
        tvQuoteDiscount.text = "Discount ($discountPct%): -${formatRand(discountAmt)}"
        tvQuoteVat.text = "VAT (15%): ${formatRand(vat)}"
        tvQuoteFinalTotal.text = "Quoted Total Fee: ${formatRand(finalTotal)}"

        panelQuoteBreakdown.visibility = View.VISIBLE
        toast("Quotation calculated successfully!")
        return true
    }

    private fun submitBookingRequest() {
        if (calculateAndDisplayQuote()) {
            val name = CartManager.customerName
            val count = CartManager.itemCount
            val totalStr = formatRand(CartManager.total)

            AlertDialog.Builder(this)
                .setTitle("Booking Request Submitted")
                .setMessage(
                    "Thank you $name!\n\nYour request for $count booking(s) totaling $totalStr has been recorded.\n\nA Next Level Gaming consultant will contact you at ${CartManager.customerPhone} / ${CartManager.customerEmail} to confirm your reservation."
                )
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
