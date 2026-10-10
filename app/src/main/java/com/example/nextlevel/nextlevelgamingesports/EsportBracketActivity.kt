package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View

class EsportBracketActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esport_bracket)

        // Tapping the LIVE badge opens the full tournament / leaderboard screen.
        findViewById<View>(R.id.badgeLive).setOnClickListener { goTo<LiveStreamActivity>() }
    }
}
