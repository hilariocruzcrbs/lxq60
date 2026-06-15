package com.callblocker

import android.net.Uri
import android.provider.ContactsContract
import android.telecom.Call.Details
import android.telecom.CallScreeningService

class CallBlockerService : CallScreeningService() {

    override fun onScreenCall(details: Details) {
        if (!BlockState.isEnabled(this)) return

        val number = details.handle?.schemeSpecificPart ?: return
        if (number.isBlank()) return

        if (!isInContacts(number)) {
            val response = CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipCallLog(false)
                .setSkipNotification(false)
                .build()
            respondToCall(details, response)
        }
    }

    private fun isInContacts(number: String): Boolean {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use { it.count > 0 } ?: false
    }
}
