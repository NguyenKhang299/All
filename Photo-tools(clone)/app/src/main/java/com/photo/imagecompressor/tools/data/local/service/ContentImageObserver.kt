package com.photo.imagecompressor.tools.data.local.service

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler

class ContentImageObserver(private val contentResolver: ContentResolver) :
    ContentObserver(Handler()) {

    interface ContentChangeListener {
        fun onContentChanged()
    }

    private var listener: ContentChangeListener? = null

    fun setListener(listener: ContentChangeListener) {
        this.listener = listener
    }

    private var beforeUri: Uri? = Uri.EMPTY
    private var count = 1
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (count == 4) {
            listener?.onContentChanged()
            beforeUri = uri
            count = 1
        }
        count++
    }

    fun register(uri: Uri) {
        contentResolver.registerContentObserver(
            uri,
            true,
            this
        )
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(this)
    }
}