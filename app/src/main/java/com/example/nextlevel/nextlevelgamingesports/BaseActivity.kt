package com.example.nextlevel.nextlevelgamingesports

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * All screens extend this. It draws edge-to-edge with light status/nav bar icons
 * (our screens are dark) and pushes the layout's root view away from the system bars.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        applySystemBarInsets()
    }

    private fun applySystemBarInsets() {
        val root = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) ?: return
        val l = root.paddingLeft
        val t = root.paddingTop
        val r = root.paddingRight
        val b = root.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            v.setPadding(l + bars.left, t + bars.top, r + bars.right, b + bars.bottom)
            insets
        }
    }
}
