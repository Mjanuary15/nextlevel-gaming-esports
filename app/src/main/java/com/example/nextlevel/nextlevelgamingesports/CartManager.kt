package com.example.nextlevel.nextlevelgamingesports

import androidx.annotation.DrawableRes
import java.text.NumberFormat
import java.util.Locale

data class CartItem(
    val name: String,
    val price: Int,
    val category: String = "Package",
    val description: String = "",
    @DrawableRes val imageRes: Int = 0
)

data class CartGroup(
    val item: CartItem,
    val quantity: Int
) {
    val totalPrice: Int get() = item.price * quantity
}

/**
 * Catalog of all offerings described in Next Level Gaming & Esports Arena Scenario
 */
object Catalog {
    val gamingPackages = listOf(
        CartItem(
            name = "Ultimate Gamer Pass",
            price = 1500,
            category = "Gaming Package",
            description = "Unlimited gaming access for a full day. Includes PC & Console gaming, high-speed internet, snack voucher & tournament entry.",
            imageRes = R.drawable.ultimate_gamer
        ),
        CartItem(
            name = "VIP Gaming Experience",
            price = 1500,
            category = "Gaming Package",
            description = "Premium gaming experience with exclusive facilities. Includes private booth, premium equipment, food & drinks, priority booking & personal assistant.",
            imageRes = R.drawable.vip_gaming_experience
        ),
        CartItem(
            name = "Esports Training Package",
            price = 1500,
            category = "Gaming Package",
            description = "Improve competitive gaming skills with professional coaching. Includes strategy coaching, team communication, match analysis & feedback.",
            imageRes = R.drawable.esports_training_package
        ),
        CartItem(
            name = "Birthday Party Package",
            price = 1500,
            category = "Gaming Package",
            description = "Host a gaming-themed birthday celebration. Includes reserved gaming area, multiplayer competitions, decorations, catering & prizes.",
            imageRes = R.drawable.birthday_package
        )
    )

    val individualExperiences = listOf(
        CartItem(
            name = "Virtual Reality Experience",
            price = 750,
            category = "Individual Experience",
            description = "Explore immersive virtual reality games. Includes VR headset, choice of games & staff assistance.",
            imageRes = R.drawable.virtual_reality_experience
        ),
        CartItem(
            name = "Racing Simulator Challenge",
            price = 750,
            category = "Individual Experience",
            description = "Race on professional driving simulators. Includes racing simulator, choice of tracks & leaderboard competition.",
            imageRes = R.drawable.racing_simulator
        ),
        CartItem(
            name = "Escape Room Challenge",
            price = 750,
            category = "Individual Experience",
            description = "Solve puzzles and escape before time runs out. Includes team challenge, themed escape room & digital scorecard.",
            imageRes = R.drawable.escape_room
        )
    )

    val allItems: List<CartItem> get() = gamingPackages + individualExperiences
}

/**
 * In-memory cart & quotation model shared across the application.
 */
object CartManager {

    private val _items = mutableListOf<CartItem>()
    val items: List<CartItem> get() = _items

    val groupedItems: List<CartGroup>
        get() = _items.groupBy { it.name }
            .map { (_, list) -> CartGroup(list.first(), list.size) }

    var customerName: String = ""
    var customerPhone: String = ""
    var customerEmail: String = ""

    fun add(item: CartItem) { _items.add(item) }
    fun removeAt(index: Int) { if (index in _items.indices) _items.removeAt(index) }
    fun removeOne(item: CartItem) {
        val idx = _items.indexOfFirst { it.name == item.name }
        if (idx != -1) _items.removeAt(idx)
    }
    fun clear() { _items.clear() }

    fun setItems(newList: List<CartItem>) {
        _items.clear()
        _items.addAll(newList)
    }

    val itemCount: Int get() = _items.size

    val subtotal: Int get() = _items.sumOf { it.price }

    /**
     * Discount Rules from Scenario 3:
     * - 1 booking = 0% discount
     * - 2 bookings = 5% discount
     * - 3 bookings = 10% discount
     * - More than 3 bookings (> 3) = 15% discount
     */
    val discountPercent: Int
        get() = when {
            _items.size > 3 -> 15
            _items.size == 3 -> 10
            _items.size == 2 -> 5
            else -> 0
        }

    val discountAmount: Double get() = subtotal * (discountPercent / 100.0)

    val subtotalAfterDiscount: Double get() = subtotal - discountAmount

    /** VAT is 15% on the total amount */
    val vatAmount: Double get() = subtotalAfterDiscount * 0.15

    val total: Double get() = subtotalAfterDiscount + vatAmount
}

/** Formats integer rand amounts e.g., 1500 -> "R1,500" */
fun formatRand(amount: Int): String =
    "R" + NumberFormat.getIntegerInstance(Locale.US).format(amount)

/** Formats double rand amounts e.g., 1500.50 -> "R1,500.50" */
fun formatRand(amount: Double): String =
    "R" + String.format(Locale.US, "%,.2f", amount)
