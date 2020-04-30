package com.doordash.interview

import java.io.ByteArrayOutputStream
import java.io.InputStream

object Utils {
    @Throws(Exception::class)
    fun readTextStream(inputStream: InputStream): String? {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int = 0
        while (inputStream.read(buffer).also({ length = it }) != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }
}