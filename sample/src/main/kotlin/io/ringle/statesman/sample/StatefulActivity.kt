package io.ringle.statesman.sample

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import io.ringle.statesman.StateHost
import io.ringle.statesman.Stateful
import io.ringle.statesman.Statesman
import io.ringle.statesman.store
import kotlin.properties.Delegates

public open class StatefulActivity : Activity(), Stateful {

    override var stateHost: StateHost by Delegates.notNull()

    override val key: Int = 1

    var countObj: Counter? = null

    var counter: TextView by Delegates.notNull()

    var clicker: TextView by Delegates.notNull()

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
        super<Activity>.onCreate(savedInstanceState)
        stateHost = Statesman.of(this)
        countObj = Counter(this)
        setContentView(createView())
        updateCounterText()
    }

    class Counter(override var stateHost: StateHost) : Stateful {

        override val key: Int = 1

        public var count: Int by store(0)
    }
}
