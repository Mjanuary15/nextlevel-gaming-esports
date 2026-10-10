package com.example.nextlevel.nextlevelgamingesports

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val GLOW_BLUR_RADIUS = 18f

class SplashActivity : AppCompatActivity() {

    private var pulse: ObjectAnimator? = null
    private var glowPulse: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logoShapes = findViewById<ImageView>(R.id.logoShapes)
        val logoGlow = findViewById<ImageView>(R.id.logoGlow)

        // 1. Shapes (ring, bar, X, triangle) – animated vector from XML
        (logoShapes.drawable as? Animatable)?.start()
        (logoGlow.drawable as? Animatable)?.start()

        if (Build.VERSION.SDK_INT >= 31) {
            logoGlow.setRenderEffect(
                RenderEffect.createBlurEffect(GLOW_BLUR_RADIUS, GLOW_BLUR_RADIUS, Shader.TileMode.CLAMP)
            )

            glowPulse = ObjectAnimator.ofFloat(logoGlow, View.ALPHA, 0f, 0.9f).apply {
                startDelay = 1500L
                duration = 800L
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        glowPulse = ObjectAnimator.ofFloat(logoGlow, View.ALPHA, 0.55f, 0.9f).apply {
                            duration = 1200L
                            repeatMode = ObjectAnimator.REVERSE
                            repeatCount = ObjectAnimator.INFINITE
                            start()
                        }
                    }
                })
                start()
            }
        }

        // 2. Text words rise + fade in, staggered (same timings as the HTML preview)
        val words = listOf(
            R.id.tvNext to 2000L,
            R.id.tvLevel to 2100L,
            R.id.tvGaming to 2200L,
            R.id.tvAmp to 2300L,
            R.id.tvEsports to 2400L,
            R.id.tvArena to 2500L
        )
        words.forEach { (id, delay) -> rise(findViewById(id), delay, 500L, 6f) }

        // 3. EST 2023
        rise(findViewById(R.id.tvEst), 2900L, 600L, 0f)

        // 4. Loading bar: track grows from centre, green fill grows from the left
        val track = findViewById<View>(R.id.ruleTrack).apply { scaleX = 0f }
        track.animate().scaleX(1f).setStartDelay(2600).setDuration(500).start()

        val fill = findViewById<View>(R.id.ruleFill).apply {
            post { pivotX = 0f }          // grow from the left edge
            scaleX = 0f
        }
        fill.animate().scaleX(1f).setStartDelay(3100).setDuration(1100)
            .setInterpolator(AccelerateDecelerateInterpolator()).start()

        // 5. Get Started button slides up, then gently pulses
        val btn = findViewById<Button>(R.id.btnStart)
        btn.translationY = dp(30f)
        btn.animate().alpha(1f).translationY(0f)
            .setStartDelay(4200).setDuration(600)
            .setInterpolator(OvershootInterpolator(1.4f))
            .withEndAction {
                pulse = ObjectAnimator.ofPropertyValuesHolder(
                    btn,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.04f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.04f)
                ).apply {
                    duration = 1000
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = ObjectAnimator.INFINITE
                    start()
                }
            }.start()

        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun rise(v: View, delay: Long, duration: Long, offsetDp: Float) {
        v.translationY = dp(offsetDp)
        v.animate().alpha(1f).translationY(0f)
            .setStartDelay(delay).setDuration(duration).start()
    }

    private fun dp(value: Float) = value * resources.displayMetrics.density

    override fun onDestroy() {
        pulse?.cancel()
        glowPulse?.cancel()
        super.onDestroy()
    }
}
