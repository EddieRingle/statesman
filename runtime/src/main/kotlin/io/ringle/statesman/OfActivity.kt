package io.ringle.statesman

import android.app.Activity
import android.content.Context

interface OfActivity : Contextual {

    var activity: Activity

    override var ctx: Context
        get() = activity
        set(c) {
            activity = c as Activity
        }
}
