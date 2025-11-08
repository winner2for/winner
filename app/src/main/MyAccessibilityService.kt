package com.example.autocontrol

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter

class MyAccessibilityService : AccessibilityService() {
    private val TAG = "MyAccessibilityService"
    private val AGENT_URL = "http://127.0.0.1:3000/api/command"
    private val AGENT_SECRET = "change_this_secret"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        try {
            if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                val text = if (event.text != null && event.text.size > 0) event.text[0].toString() else null
                Log.d(TAG, "Notification: $text")
                Toast.makeText(this, "Notification reçue", Toast.LENGTH_SHORT).show()
                // Example: send a safe mapped command (change mapping as needed)
                val safeCmd = "input keyevent 26" // exemple : allumer écran
                sendCommandToAgent(safeCmd)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Accessibility error", e)
        }
    }

    override fun onInterrupt() {}

    private fun sendCommandToAgent(cmd: String): Boolean {
        try {
            val url = URL(AGENT_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            conn.setRequestProperty("X-Agent-Secret", AGENT_SECRET)
            val payload = "{"cmd":"${cmd}","reason":"from-accessibility"}"
            val wr = OutputStreamWriter(conn.outputStream)
            wr.write(payload)
            wr.flush()
            val code = conn.responseCode
            Log.d(TAG, "Agent response code: $code")
            return code >= 200 && code < 300
        } catch (e: Exception) {
            Log.e(TAG, "Send error", e)
            return false
        }
    }
}
