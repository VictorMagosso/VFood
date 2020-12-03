package com.victormagosso.vfood.helper

import android.util.Base64

class Base64Custom {
    fun encodeToBase64(text: String): String? {
        return Base64.encodeToString(text.toByteArray(), Base64.DEFAULT)
            .replace("(\\n|\\r)".toRegex(), "")
    }

    fun decodeFromBase64(codeText: String?): String? {
        return String(Base64.decode(codeText, Base64.DEFAULT))
    }
}