package com.callblocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SecretCodeReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SECRET_CODE") return
        val on = BlockState.toggle(ctx)
        val msg = if (on) "\u26D4 Blocking: ON" else "\u2705 Blocking: OFF"
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }
}
