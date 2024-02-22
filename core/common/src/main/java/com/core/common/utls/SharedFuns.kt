package com.core.common.utls

import android.content.Context
import android.net.Uri
import java.lang.Exception

fun Uri.toByteArray(context: Context): ByteArray? {
    return try {
        context.contentResolver.openInputStream(this)?.use {
            it.readBytes()
        }
    } catch (e: Exception) {
        null
    }
}