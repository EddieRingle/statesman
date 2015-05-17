package io.ringle.statesman.sample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import io.ringle.statesman.Stateful
import io.ringle.statesman.StatesmanContextWrapper
import io.ringle.statesman.statesman
import io.ringle.statesman.store
import kotlin.properties.Delegates

public open class StatefulActivity : Activity(), Stateful {

    override val key: Int = 1

    override val ctx: Context = this

    var countObj: Counter? = null

    var counter: TextView by Delegates.notNull()

    var clicker: TextView by Delegates.notNull()

    override fun attachBaseContext(newBase: Context) {
        super<Activity>.attachBaseContext(StatesmanContextWrapper(newBase))
    }

    fun createView(): View {
        val container = LinearLayout(this)
        counter = TextView(this)
        clicker = Button(this)
        container.setGravity(Gravity.CENTER)
        container.setOrientation(VERTICAL)
        container.addView(counter, WRAP_CONTENT, WRAP_CONTENT)
        container.addView(clicker, WRAP_CONTENT, WRAP_CONTENT)
        clicker.setOnClickListener {
            countObj!!.count++
            updateCounterText()
        }
        clicker.setText("Click me!")
        return container
    }

    fun updateCounterText() {
        counter.setText("Clicked ${countObj!!.count} time(s)")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        statesman.attachTo(this)
        super<Activity>.onCreate(savedInstanceState)
        countObj = Counter(this)
        setContentView(createView())
        updateCounterText()
    }

    class Counter(ctx: Context) : Stateful {

        override val ctx = ctx

        override val key: Int = 1

        public var count: Int by store(0)
    }
}
