package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

class StationAvailabilityActivity : BaseActivity() {

    private lateinit var pills: Map<Int, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_availability)

        pills = mapOf(
            R.id.pillAll to findViewById<TextView>(R.id.pillAll),
            R.id.pillPc to findViewById<TextView>(R.id.pillPc),
            R.id.pillConsole to findViewById<TextView>(R.id.pillConsole)
        )
        pills.forEach { (id, pill) -> pill.setOnClickListener { select(id) } }
        select(R.id.pillAll)

        // TODO: replace the hardcoded counts in the layout with live data from your backend.
        findViewById<View>(R.id.btnReserveNext).setOnClickListener {
            CartManager.add(CartItem("RTX 4080 PC Station (1hr)", 120))
            toast("Station reserved")
            goTo<BookingCartActivity>()
        }
    }

    private fun select(selectedId: Int) {
        pills.forEach { (id, pill) ->
            val on = id == selectedId
            pill.setBackgroundResource(if (on) R.drawable.bg_pill_selected else R.drawable.bg_pill_outline)
            pill.setTextColor(
                ContextCompat.getColor(this, if (on) R.color.nl_black else R.color.nl_white)
            )
        }
        show(R.id.cardPcs, selectedId == R.id.pillAll || selectedId == R.id.pillPc)
        show(R.id.cardConsoles, selectedId == R.id.pillAll || selectedId == R.id.pillConsole)
        show(R.id.cardVr, selectedId == R.id.pillAll)
    }

    private fun show(id: Int, visible: Boolean) {
        findViewById<View>(id).visibility = if (visible) View.VISIBLE else View.GONE
    }
}
