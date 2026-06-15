package com.callblocker

import android.content.Context

object BlockState {
    private const val PREFS = "callblocker_prefs"
    private const val KEY_ENABLED = "blocking_enabled"

    fun isEnabled(ctx: Context): Boolean =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, false)

    fun setEnabled(ctx: Context, on: Boolean) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, on)
            .apply()
    }

    fun toggle(ctx: Context): Boolean {
        val now = !isEnabled(ctx)
        setEnabled(ctx, now)
        return now
    }
}
