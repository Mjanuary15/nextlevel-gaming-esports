package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View

class LiveStreamActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_stream)

        findViewById<View>(R.id.btnRegisterNext).setOnClickListener {
            // TODO: send the registration to your backend
            toast("You're registered for the next tournament")
        }
    }
}
