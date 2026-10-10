package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import com.nextlevel.esports.ui.home.HomeScreen

class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val composeView = ComposeView(this).apply {
            setContent {
                HomeScreen(
                    onRegister = { this@HomeActivity.goTo<EsportBracketActivity>() },
                    onManageSession = { this@HomeActivity.goTo<StationAvailabilityActivity>() },
                    onCategoryClick = { cat ->
                        when (cat) {
                            "solo" -> this@HomeActivity.goTo<RigsStationsActivity>()
                            "groups" -> this@HomeActivity.goTo<PackagesActivity>()
                            "corporate" -> this@HomeActivity.goTo<CustomQuoteActivity>()
                        }
                    }
                )
            }
        }
        setContentView(composeView)
    }
}
