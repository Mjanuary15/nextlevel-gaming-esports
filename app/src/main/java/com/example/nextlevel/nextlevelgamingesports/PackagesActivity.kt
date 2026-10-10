package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View

class PackagesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)

        // Gaming Packages (R1,500 each)
        bindItem(R.id.btnAddUltimateGamer, R.id.cardUltimateGamer, Catalog.gamingPackages[0])
        bindItem(R.id.btnAddVipExperience, R.id.cardVipExperience, Catalog.gamingPackages[1])
        bindItem(R.id.btnAddEsportsTraining, R.id.cardEsportsTraining, Catalog.gamingPackages[2])
        bindItem(R.id.btnAddBirthdayParty, R.id.cardBirthdayParty, Catalog.gamingPackages[3])

        // Individual Experiences (R750 each)
        bindItem(R.id.btnAddVrExperience, R.id.cardVrExperience, Catalog.individualExperiences[0])
        bindItem(R.id.btnAddRacingSim, R.id.cardRacingSim, Catalog.individualExperiences[1])
        bindItem(R.id.btnAddEscapeRoom, R.id.cardEscapeRoom, Catalog.individualExperiences[2])
    }

    private fun bindItem(btnId: Int, cardId: Int, item: CartItem) {
        findViewById<View>(btnId)?.setOnClickListener { addAndGo(item) }
        findViewById<View>(cardId)?.setOnClickListener { addAndGo(item) }
    }

    private fun addAndGo(item: CartItem) {
        CartManager.add(item)
        toast("${item.name} added to booking")
        goTo<BookingCartActivity>()
    }
}
