package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View

class RigsStationsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rigs_stations)

        bind(R.id.cardVr, R.id.btnAddVr, CartItem("VR Gaming Setup (1hr)", 100, "Rig", "", R.drawable.virtual_reality_experience))
        bind(R.id.cardSimRacing, R.id.btnAddSimRacing, CartItem("Sim Racing Rig (1hr)", 150, "Rig", "", R.drawable.racing_simulator))
        bind(R.id.cardConsole, R.id.btnAddConsole, CartItem("PS5 & Xbox Booth (1hr)", 80, "Rig", "", R.drawable.ps5_xbox))
        bind(R.id.cardPc, R.id.btnAddPc, CartItem("RTX 4080 PC Station (1hr)", 120, "Rig", "", R.drawable.rtx_4080))
    }

    private fun bind(cardId: Int, addId: Int, item: CartItem) {
        val add = View.OnClickListener {
            CartManager.add(item)
            toast("${item.name} added")
            goTo<BookingCartActivity>()
        }
        findViewById<View>(cardId)?.setOnClickListener(add)
        findViewById<View>(addId)?.setOnClickListener(add)
    }
}
