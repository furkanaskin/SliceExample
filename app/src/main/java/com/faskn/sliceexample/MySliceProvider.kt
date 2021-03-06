package com.faskn.sliceexample

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.core.SliceHints

class MySliceProvider : SliceProvider() {
    /**
     * Instantiate any required objects. Return true if the provider was successfully created,
     * false otherwise.
     */
    override fun onCreateSliceProvider(): Boolean {
        return true
    }

    /**
     * Converts URL to content URI (i.e. content://com.faskn.sliceexample...)
     */
    override fun onMapIntentToUri(intent: Intent?): Uri {
        // Note: implementing this is only required if you plan on catching URL requests.
        // This is an example solution.
        var uriBuilder: Uri.Builder = Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
        if (intent == null) return uriBuilder.build()
        val data = intent.data
        if (data != null && data.path != null) {
            val path = data.path.replace("/", "")
            uriBuilder = uriBuilder.path(path)
        }
        val context = context
        if (context != null) {
            uriBuilder = uriBuilder.authority(context.packageName)
        }
        return uriBuilder.build()
    }

    /**
     * Construct the Slice and bind data if available.
     */
    override fun onBindSlice(sliceUri: Uri): Slice? {
        return when {
            sliceUri.path == "/hello" -> createBasicRowSlice(sliceUri)
            else -> null
        }
    }

    private fun createBasicRowSlice(sliceUri: Uri): Slice {
        return ListBuilder(context, sliceUri, ListBuilder.INFINITY)
            .addRow {
                it.title = "GDG Istanbul"
                it.subtitle = "Basit Slice örneği"
                it.setTitleItem(
                    this.createActivityAction(
                        Intent(context, MainActivity::class.java),
                        R.drawable.ic_baseline_open_in_new_24px,
                        SliceHints.ICON_IMAGE
                    )!!
                )
            }
            .build()
    }

    private fun createActivityAction(
        actionIntent: Intent,
        drawableInt: Int,
        imageMode: Int
    ): SliceAction? {
        return SliceAction.create(
            PendingIntent.getActivity(context, 0, actionIntent, 0),
            IconCompat.createWithResource(context, drawableInt),
            imageMode,
            "MainActivity'yi açar."
        )
    }

    /**
     * Slice has been pinned to external process. Subscribe to data source if necessary.
     */
    override fun onSlicePinned(sliceUri: Uri?) {
        // When data is received, call context.contentResolver.notifyChange(sliceUri, null) to
        // trigger MySliceProvider#onBindSlice(Uri) again.
    }

    /**
     * Unsubscribe from data source if necessary.
     */
    override fun onSliceUnpinned(sliceUri: Uri?) {
        // Remove any observers if necessary to avoid memory leaks.
    }
}
