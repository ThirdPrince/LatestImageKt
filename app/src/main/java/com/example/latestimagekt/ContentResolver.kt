package com.example.latestimagekt

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler

/**
 * 监听媒体数据的变化
 */
fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    // notifyForDescendants true 华为手机上 传值 false 会有延迟
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}