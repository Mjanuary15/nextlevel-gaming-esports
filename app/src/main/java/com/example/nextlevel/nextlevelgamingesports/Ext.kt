package com.example.nextlevel.nextlevelgamingesports

import android.app.Activity
import android.content.Intent
import android.widget.Toast

/** Start another screen:  goTo<HomeActivity>()  */
inline fun <reified T : Activity> Activity.goTo(flags: Int = 0) {
    startActivity(Intent(this, T::class.java).addFlags(flags))
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
