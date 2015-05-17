package io.ringle.statesman

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable

public trait LifecycleAdapter : Application.ActivityLifecycleCallbacks {

    var target: Activity

    /**
     * For best results, call this BEFORE super.onCreate() in your Activity
     */
    public fun attachTo(ref: Activity) {
        target = ref
        ref.getApplication().registerActivityLifecycleCallbacks(this)
    }

    fun onActivityCreated([Nullable] savedInstanceState: Bundle?) {
    }

    final override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (target.identityEquals(activity)) {
            onActivityCreated(savedInstanceState)
        }
    }

    fun onActivityDestroyed() {
    }

    final override fun onActivityDestroyed(activity: Activity?) {
        if (target.identityEquals(activity)) {
            onActivityDestroyed()
        }
    }

    fun onActivityPaused() {
    }

    final override fun onActivityPaused(activity: Activity?) {
        if (target.identityEquals(activity)) {
            onActivityPaused()
        }
    }

    fun onActivityResumed() {
    }

    final override fun onActivityResumed(activity: Activity?) {
        if (target.identityEquals(activity)) {
            onActivityResumed()
        }
    }

    fun onActivitySaveInstanceState([NonNull] outState: Bundle) {
    }

    final override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        if (target.identityEquals(activity) && outState != null) {
            onActivitySaveInstanceState(outState)
        }
    }

    fun onActivityStarted() {
    }

    final override fun onActivityStarted(activity: Activity?) {
        if (target.identityEquals(activity)) {
            onActivityStarted()
        }
    }

    fun onActivityStopped() {
    }

    final override fun onActivityStopped(activity: Activity?) {
        if (target.identityEquals(activity)) {
            onActivityStopped()
        }
    }
}
