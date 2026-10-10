package com.example.nextlevel.nextlevelgamingesports

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat

class BookingCartActivity : BaseActivity() {

    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_cart)

        container = findViewById(R.id.cartContainer)

        findViewById<View>(R.id.btnProceedDetails).setOnClickListener {
            if (CartManager.items.isEmpty()) {
                toast("Please select at least one package or experience first")
            } else {
                goTo<CheckoutActivity>()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun render() {
        // Rows
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)
        CartManager.groupedItems.forEach { group ->
            val row = inflater.inflate(R.layout.item_cart_row, container, false)
            row.findViewById<TextView>(R.id.tvItemName).text = group.item.name
            row.findViewById<TextView>(R.id.tvItemSubtext).text = group.item.category
            row.findViewById<TextView>(R.id.tvQuantity).text = "${group.quantity}"
            row.findViewById<TextView>(R.id.tvItemPrice).text = formatRand(group.totalPrice)

            val img = row.findViewById<ImageView>(R.id.imgCartItem)
            if (group.item.imageRes != 0) {
                img.setImageResource(group.item.imageRes)
                img.imageTintList = null
            } else {
                img.setImageResource(R.drawable.ic_gamepad)
                img.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nl_green))
            }

            row.findViewById<View>(R.id.btnPlus)?.setOnClickListener {
                CartManager.add(group.item)
                render()
            }
            row.findViewById<View>(R.id.btnMinus)?.setOnClickListener {
                CartManager.removeOne(group.item)
                render()
            }
            row.findViewById<View>(R.id.btnRemove)?.setOnClickListener {
                while (CartManager.items.any { it.name == group.item.name }) {
                    CartManager.removeOne(group.item)
                }
                render()
            }
            container.addView(row)
        }

        val layoutEmpty = findViewById<View>(R.id.layoutEmpty)
        layoutEmpty?.visibility = if (CartManager.items.isEmpty()) View.VISIBLE else View.GONE
        findViewById<View>(R.id.btnBrowsePackages)?.setOnClickListener {
            goTo<PackagesActivity>()
        }

        // Discount tier banner
        val count = CartManager.items.size
        val discountPct = CartManager.discountPercent

        findViewById<TextView>(R.id.tvDiscountTier).text = when {
            count > 3 -> "15% Maximum Discount Tier Unlocked!"
            count == 3 -> "10% Discount Tier Unlocked!"
            count == 2 -> "5% Discount Tier Unlocked!"
            else -> "0% Discount (Select 2+ items for discount)"
        }

        findViewById<TextView>(R.id.tvTierBadge)?.text = "TIER " + when {
            count > 3 -> "3 (15%)"
            count == 3 -> "2 (10%)"
            count == 2 -> "1 (5%)"
            else -> "0 (0%)"
        }

        findViewById<TextView>(R.id.tvNextTier).text = when {
            count > 3 -> "Max 15% discount applied"
            count == 3 -> "Next: Add 1 more item for 15% off"
            count == 2 -> "Next: Add 1 more item for 10% off"
            count == 1 -> "Next: Add 1 more item for 5% off"
            else -> "Next: Add 2 items for 5% off"
        }

        val progressPct = when {
            count > 3 -> 100
            count == 3 -> 75
            count == 2 -> 50
            count == 1 -> 25
            else -> 0
        }
        findViewById<ProgressBar>(R.id.progressDiscount).progress = progressPct

        // Breakdown Totals
        findViewById<TextView>(R.id.tvSubtotal).text = formatRand(CartManager.subtotal)
        findViewById<TextView>(R.id.tvDiscount).text =
            "-$discountPct% (-${formatRand(CartManager.discountAmount)})"
        findViewById<TextView>(R.id.tvVat)?.text = formatRand(CartManager.vatAmount)
        findViewById<TextView>(R.id.tvEstimatedTotal).text = formatRand(CartManager.total)
    }
}
