package com.example.nextlevel.nextlevelgamingesports

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class ArenaManagerActivity : BaseActivity() {

    private val podIds = listOf(R.id.pod01, R.id.pod05, R.id.pod06)
    private val selected = podIds.toMutableSet()   // all pods selected by default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arena_manager)

        // Tap a pod to include / exclude it from "Reboot All Selected Pods".
        podIds.forEach { id ->
            findViewById<View>(id).setOnClickListener { v ->
                if (!selected.remove(id)) selected.add(id)
                v.alpha = if (id in selected) 1f else 0.4f
            }
        }

        findViewById<View>(R.id.btnRebootPods).setOnClickListener {
            if (selected.isEmpty()) toast("Select at least one pod")
            else toast("Rebooting ${selected.size} pod(s)...")   // TODO: call your fleet API
        }

        findViewById<View>(R.id.btnEmergencyShutdown).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Emergency shutdown")
                .setMessage("This will shut down every station in the arena. Continue?")
                .setPositiveButton("Shut down") { _, _ -> toast("Shutdown command sent") }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val btnAssign = findViewById<TextView>(R.id.btnAssignPod)
        btnAssign.setOnClickListener {
            val pod = findViewById<TextView>(R.id.pod06)
            pod.text = "POD 06\nIN USE"
            pod.setBackgroundResource(R.drawable.bg_pod_in_use)
            pod.setTextColor(ContextCompat.getColor(this, R.color.nl_green))
            btnAssign.text = "Assigned"
            btnAssign.setTextColor(ContextCompat.getColor(this, R.color.nl_lavender))
            btnAssign.isEnabled = false
        }
    }
}
