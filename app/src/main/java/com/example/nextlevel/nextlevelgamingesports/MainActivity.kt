package com.example.nextlevel.nextlevelgamingesports

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nextlevel.esports.ui.home.HomeScreen

class MainActivity : AppCompatActivity() {

    private lateinit var contentContainer: ViewGroup
    private lateinit var navHome: View
    private lateinit var navPackages: View
    private lateinit var navBookings: View
    private lateinit var navQuote: View

    private lateinit var iconNavHome: ImageView
    private lateinit var textNavHome: TextView
    private lateinit var iconNavPackages: ImageView
    private lateinit var textNavPackages: TextView
    private lateinit var iconNavBookings: ImageView
    private lateinit var textNavBookings: TextView
    private lateinit var iconNavQuote: ImageView
    private lateinit var textNavQuote: TextView

    private var currentTab = 0
    private var activeSubScreenLayoutRes: Int? = null
    private val tabViewCache = mutableMapOf<Int, View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        contentContainer = findViewById(R.id.contentContainer)
        val bottomNav = findViewById<View>(R.id.bottomNavInclude)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            contentContainer.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            bottomNav?.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        navHome = bottomNav.findViewById(R.id.navHome)
        navPackages = bottomNav.findViewById(R.id.navPackages)
        navBookings = bottomNav.findViewById(R.id.navBookings)
        navQuote = bottomNav.findViewById(R.id.navQuote)

        iconNavHome = bottomNav.findViewById(R.id.iconNavHome)
        textNavHome = bottomNav.findViewById(R.id.textNavHome)
        iconNavPackages = bottomNav.findViewById(R.id.iconNavPackages)
        textNavPackages = bottomNav.findViewById(R.id.textNavPackages)
        iconNavBookings = bottomNav.findViewById(R.id.iconNavBookings)
        textNavBookings = bottomNav.findViewById(R.id.textNavBookings)
        iconNavQuote = bottomNav.findViewById(R.id.iconNavQuote)
        textNavQuote = bottomNav.findViewById(R.id.textNavQuote)

        navHome.setOnClickListener { switchTab(0) }
        navPackages.setOnClickListener { switchTab(1) }
        navBookings.setOnClickListener { switchTab(2) }
        navQuote.setOnClickListener { switchTab(3) }

        // System Back Button Callback handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activeSubScreenLayoutRes != null) {
                    activeSubScreenLayoutRes = null
                    switchTab(currentTab)
                } else if (currentTab != 0) {
                    switchTab(0)
                } else {
                    finish()
                }
            }
        })

        switchTab(0)
    }

    fun switchTab(tabIndex: Int) {
        currentTab = tabIndex
        activeSubScreenLayoutRes = null
        updateNavUi(tabIndex)

        contentContainer.removeAllViews()
        findViewById<View>(R.id.bottomNavInclude)?.visibility = View.VISIBLE

        val view = tabViewCache.getOrPut(tabIndex) {
            if (tabIndex == 0) {
                ComposeView(this).apply {
                    setViewCompositionStrategy(androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        HomeScreen(
                            onRegister = {
                                showToast("Opening FC 27 Esports Bracket...")
                                loadSubScreen(R.layout.activity_esport_bracket)
                            },
                            onManageSession = {
                                loadSubScreen(R.layout.activity_station_availability)
                            },
                            onCategoryClick = { category ->
                                when (category) {
                                    "solo" -> {
                                        showToast("Opening Casual & Solo Rigs...")
                                        loadSubScreen(R.layout.activity_rigs_stations)
                                    }
                                    "groups" -> switchTab(1)
                                    "corporate" -> switchTab(3)
                                }
                            }
                        )
                    }
                }
            } else {
                val layoutRes = when (tabIndex) {
                    1 -> R.layout.activity_packages
                    2 -> R.layout.activity_booking_cart
                    3 -> R.layout.activity_custom_quote
                    else -> R.layout.activity_packages
                }
                LayoutInflater.from(this).inflate(layoutRes, contentContainer, false).also { inflatedView ->
                    setupScreenInteractions(inflatedView, tabIndex)
                }
            }
        }

        if (tabIndex == 2) {
            renderCartTab(view)
        } else if (tabIndex == 3) {
            setupQuoteTab(view)
        }

        // Safely detach view from any prior parent before adding to contentContainer
        (view.parent as? ViewGroup)?.removeView(view)
        contentContainer.addView(view)
    }

    private fun updateNavUi(activeTab: Int) {
        val activeColor = ContextCompat.getColor(this, R.color.nl_green)
        val inactiveColor = ContextCompat.getColor(this, R.color.nl_lavender)

        fun applyTabStyle(icon: ImageView, text: TextView, isActive: Boolean) {
            val color = if (isActive) activeColor else inactiveColor
            icon.imageTintList = ColorStateList.valueOf(color)
            text.setTextColor(color)
        }

        applyTabStyle(iconNavHome, textNavHome, activeTab == 0)
        applyTabStyle(iconNavPackages, textNavPackages, activeTab == 1)
        applyTabStyle(iconNavBookings, textNavBookings, activeTab == 2)
        applyTabStyle(iconNavQuote, textNavQuote, activeTab == 3)
    }

    private fun setupScreenInteractions(view: View, tabIndex: Int) {
        when (tabIndex) {
            1 -> {
                fun bindAdd(btnId: Int, cardId: Int, item: CartItem) {
                    val addAction = View.OnClickListener {
                        CartManager.add(item)
                        showToast("${item.name} added to booking!")
                        switchTab(2)
                    }
                    view.findViewById<View>(btnId)?.setOnClickListener(addAction)
                    view.findViewById<View>(cardId)?.setOnClickListener(addAction)
                }

                bindAdd(R.id.btnAddUltimateGamer, R.id.cardUltimateGamer, Catalog.gamingPackages[0])
                bindAdd(R.id.btnAddVipExperience, R.id.cardVipExperience, Catalog.gamingPackages[1])
                bindAdd(R.id.btnAddEsportsTraining, R.id.cardEsportsTraining, Catalog.gamingPackages[2])
                bindAdd(R.id.btnAddBirthdayParty, R.id.cardBirthdayParty, Catalog.gamingPackages[3])

                bindAdd(R.id.btnAddVrExperience, R.id.cardVrExperience, Catalog.individualExperiences[0])
                bindAdd(R.id.btnAddRacingSim, R.id.cardRacingSim, Catalog.individualExperiences[1])
                bindAdd(R.id.btnAddEscapeRoom, R.id.cardEscapeRoom, Catalog.individualExperiences[2])
            }
            2 -> {
                renderCartTab(view)
                view.findViewById<View>(R.id.btnProceedDetails)?.setOnClickListener {
                    if (CartManager.items.isEmpty()) {
                        showToast("Please select at least one package or experience first")
                    } else {
                        switchTab(3)
                    }
                }
            }
            3 -> {
                setupQuoteTab(view)
            }
        }
    }

    private fun renderCartTab(view: View) {
        val container = view.findViewById<LinearLayout>(R.id.cartContainer) ?: return
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
                renderCartTab(view)
            }
            row.findViewById<View>(R.id.btnMinus)?.setOnClickListener {
                CartManager.removeOne(group.item)
                renderCartTab(view)
            }
            row.findViewById<View>(R.id.btnRemove)?.setOnClickListener {
                while (CartManager.items.any { it.name == group.item.name }) {
                    CartManager.removeOne(group.item)
                }
                renderCartTab(view)
            }
            container.addView(row)
        }

        val layoutEmpty = view.findViewById<View>(R.id.layoutEmpty)
        layoutEmpty?.visibility = if (CartManager.items.isEmpty()) View.VISIBLE else View.GONE
        view.findViewById<View>(R.id.btnBrowsePackages)?.setOnClickListener {
            switchTab(1)
        }

        val count = CartManager.items.size
        val discountPct = CartManager.discountPercent

        view.findViewById<TextView>(R.id.tvDiscountTier)?.text = when {
            count > 3 -> "15% Maximum Discount Tier Unlocked!"
            count == 3 -> "10% Discount Tier Unlocked!"
            count == 2 -> "5% Discount Tier Unlocked!"
            else -> "0% Discount (Select 2+ items for discount)"
        }

        view.findViewById<TextView>(R.id.tvTierBadge)?.text = "TIER " + when {
            count > 3 -> "3 (15%)"
            count == 3 -> "2 (10%)"
            count == 2 -> "1 (5%)"
            else -> "0 (0%)"
        }

        view.findViewById<TextView>(R.id.tvNextTier)?.text = when {
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
        view.findViewById<ProgressBar>(R.id.progressDiscount)?.progress = progressPct

        view.findViewById<TextView>(R.id.tvSubtotal)?.text = formatRand(CartManager.subtotal)
        view.findViewById<TextView>(R.id.tvDiscount)?.text =
            "-$discountPct% (-${formatRand(CartManager.discountAmount)})"
        view.findViewById<TextView>(R.id.tvVat)?.text = formatRand(CartManager.vatAmount)
        view.findViewById<TextView>(R.id.tvEstimatedTotal)?.text = formatRand(CartManager.total)
    }

    private fun setupQuoteTab(view: View) {
        val etName = view.findViewById<EditText>(R.id.etName) ?: return
        val etPhone = view.findViewById<EditText>(R.id.etPhone) ?: return
        val etEmail = view.findViewById<EditText>(R.id.etEmail) ?: return

        val cbUltimateGamer = view.findViewById<CheckBox>(R.id.cbUltimateGamer)
        val cbVipExperience = view.findViewById<CheckBox>(R.id.cbVipExperience)
        val cbEsportsTraining = view.findViewById<CheckBox>(R.id.cbEsportsTraining)
        val cbBirthdayParty = view.findViewById<CheckBox>(R.id.cbBirthdayParty)

        val cbVrExperience = view.findViewById<CheckBox>(R.id.cbVrExperience)
        val cbRacingSim = view.findViewById<CheckBox>(R.id.cbRacingSim)
        val cbEscapeRoom = view.findViewById<CheckBox>(R.id.cbEscapeRoom)

        val panelQuoteBreakdown = view.findViewById<View>(R.id.panelQuoteBreakdown)
        val tvQuotedCustomerInfo = view.findViewById<TextView>(R.id.tvQuotedCustomerInfo)
        val tvSelectedItemsSummary = view.findViewById<TextView>(R.id.tvSelectedItemsSummary)
        val tvQuoteSubtotal = view.findViewById<TextView>(R.id.tvQuoteSubtotal)
        val tvQuoteDiscount = view.findViewById<TextView>(R.id.tvQuoteDiscount)
        val tvQuoteVat = view.findViewById<TextView>(R.id.tvQuoteVat)
        val tvQuoteFinalTotal = view.findViewById<TextView>(R.id.tvQuoteFinalTotal)

        // Pre-select existing cart items if any
        val cartNames = CartManager.items.map { it.name }.toSet()
        if (cartNames.contains("Ultimate Gamer Pass")) cbUltimateGamer?.isChecked = true
        if (cartNames.contains("VIP Gaming Experience")) cbVipExperience?.isChecked = true
        if (cartNames.contains("Esports Training Package")) cbEsportsTraining?.isChecked = true
        if (cartNames.contains("Birthday Party Package")) cbBirthdayParty?.isChecked = true

        if (cartNames.contains("Virtual Reality Experience")) cbVrExperience?.isChecked = true
        if (cartNames.contains("Racing Simulator Challenge")) cbRacingSim?.isChecked = true
        if (cartNames.contains("Escape Room Challenge")) cbEscapeRoom?.isChecked = true

        if (CartManager.customerName.isNotEmpty()) etName.setText(CartManager.customerName)
        if (CartManager.customerPhone.isNotEmpty()) etPhone.setText(CartManager.customerPhone)
        if (CartManager.customerEmail.isNotEmpty()) etEmail.setText(CartManager.customerEmail)

        fun calculateQuote(silent: Boolean = false): Boolean {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty()) {
                if (!silent) {
                    etName.error = "Please enter your full name"
                    etName.requestFocus()
                }
                return false
            }
            if (phone.isEmpty()) {
                if (!silent) {
                    etPhone.error = "Please enter your phone number"
                    etPhone.requestFocus()
                }
                return false
            }
            if (email.isEmpty()) {
                if (!silent) {
                    etEmail.error = "Please enter your email address"
                    etEmail.requestFocus()
                }
                return false
            }

            val selected = mutableListOf<CartItem>()
            if (cbUltimateGamer?.isChecked == true) selected.add(Catalog.gamingPackages[0])
            if (cbVipExperience?.isChecked == true) selected.add(Catalog.gamingPackages[1])
            if (cbEsportsTraining?.isChecked == true) selected.add(Catalog.gamingPackages[2])
            if (cbBirthdayParty?.isChecked == true) selected.add(Catalog.gamingPackages[3])

            if (cbVrExperience?.isChecked == true) selected.add(Catalog.individualExperiences[0])
            if (cbRacingSim?.isChecked == true) selected.add(Catalog.individualExperiences[1])
            if (cbEscapeRoom?.isChecked == true) selected.add(Catalog.individualExperiences[2])

            if (selected.isEmpty()) {
                if (!silent) showToast("Please select at least one gaming package or experience")
                return false
            }

            CartManager.customerName = name
            CartManager.customerPhone = phone
            CartManager.customerEmail = email
            CartManager.setItems(selected)

            val subtotal = CartManager.subtotal
            val discountPct = CartManager.discountPercent
            val discountAmt = CartManager.discountAmount
            val vat = CartManager.vatAmount
            val finalTotal = CartManager.total

            tvQuotedCustomerInfo?.text = "Quotation for: $name ($phone | $email)"

            val summaryText = buildString {
                append("Selected Offerings (${selected.size}):\n")
                selected.forEachIndexed { i, item ->
                    append("${i + 1}. ${item.name} (${item.category}) — ${formatRand(item.price)}\n")
                }
            }
            tvSelectedItemsSummary?.text = summaryText.trim()

            tvQuoteSubtotal?.text = "Subtotal: ${formatRand(subtotal)}"
            tvQuoteDiscount?.text = "Discount ($discountPct%): -${formatRand(discountAmt)}"
            tvQuoteVat?.text = "VAT (15%): ${formatRand(vat)}"
            tvQuoteFinalTotal?.text = "Quoted Total Fee: ${formatRand(finalTotal)}"

            panelQuoteBreakdown?.visibility = View.VISIBLE
            if (!silent) showToast("Quotation calculated successfully!")
            return true
        }

        val checkboxes = listOfNotNull(
            cbUltimateGamer, cbVipExperience, cbEsportsTraining, cbBirthdayParty,
            cbVrExperience, cbRacingSim, cbEscapeRoom
        )
        checkboxes.forEach { cb ->
            cb.setOnCheckedChangeListener { _, _ ->
                if (etName.text.isNotEmpty() && etPhone.text.isNotEmpty() && etEmail.text.isNotEmpty()) {
                    calculateQuote(silent = true)
                }
            }
        }

        view.findViewById<View>(R.id.btnCalculateTotal)?.setOnClickListener {
            calculateQuote()
        }

        view.findViewById<View>(R.id.btnSubmitBookingRequest)?.setOnClickListener {
            if (calculateQuote()) {
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

    private fun loadSubScreen(layoutRes: Int) {
        activeSubScreenLayoutRes = layoutRes
        contentContainer.removeAllViews()
        findViewById<View>(R.id.bottomNavInclude)?.visibility = View.GONE
        val view = LayoutInflater.from(this).inflate(layoutRes, contentContainer, false)
        contentContainer.addView(view)

        // Bind inner sub-screen back buttons and actions
        view.findViewById<View>(R.id.btnBack)?.setOnClickListener {
            activeSubScreenLayoutRes = null
            switchTab(currentTab)
        }
        view.findViewById<View>(R.id.badgeLive)?.setOnClickListener {
            loadSubScreen(R.layout.activity_live_stream)
        }
        view.findViewById<View>(R.id.btnPay)?.setOnClickListener {
            loadSubScreen(R.layout.activity_booking_confirmed)
        }
        view.findViewById<View>(R.id.btnLogOut)?.setOnClickListener {
            loadSubScreen(R.layout.activity_sign_in)
        }
        view.findViewById<View>(R.id.btnSignIn)?.setOnClickListener {
            switchTab(0)
        }
        view.findViewById<View>(R.id.btnExpressCheckout)?.setOnClickListener {
            loadSubScreen(R.layout.activity_checkout)
        }
        view.findViewById<View>(R.id.btnReserveNext)?.setOnClickListener {
            CartManager.add(CartItem("RTX 4080 PC Station (1hr)", 120, "Station"))
            showToast("Station Reserved! Added to bookings.")
            switchTab(2)
        }
        view.findViewById<View>(R.id.btnRegisterNext)?.setOnClickListener {
            showToast("Registered for Next Tournament!")
            switchTab(0)
        }

        fun bindRigAdd(cardId: Int, addId: Int, item: CartItem) {
            val addAction = View.OnClickListener {
                CartManager.add(item)
                showToast("${item.name} added to booking!")
                activeSubScreenLayoutRes = null
                switchTab(2)
            }
            view.findViewById<View>(cardId)?.setOnClickListener(addAction)
            view.findViewById<View>(addId)?.setOnClickListener(addAction)
        }
        bindRigAdd(R.id.cardVr, R.id.btnAddVr, CartItem("VR Gaming Setup (1hr)", 100, "Rig", "", R.drawable.virtual_reality_experience))
        bindRigAdd(R.id.cardSimRacing, R.id.btnAddSimRacing, CartItem("Sim Racing Rig (1hr)", 150, "Rig", "", R.drawable.racing_simulator))
        bindRigAdd(R.id.cardConsole, R.id.btnAddConsole, CartItem("PS5 & Xbox Booth (1hr)", 80, "Rig", "", R.drawable.ps5_xbox))
        bindRigAdd(R.id.cardPc, R.id.btnAddPc, CartItem("RTX 4080 PC Station (1hr)", 120, "Rig", "", R.drawable.rtx_4080))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
