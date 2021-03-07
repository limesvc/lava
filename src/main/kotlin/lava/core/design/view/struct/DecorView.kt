package lava.core.design.view.struct

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import lava.core.R
import lava.core.ext.gone
import lava.core.ext.just
import lava.core.net.LoadingState
import lava.core.util.LPUtil
import lava.core.widget.list.page.IDecorView


class DecorView: DecorStructX() {
    private var layout: ConstraintLayout? = null
    private lateinit var host: StructHost
    private var contentView: View? = null

    override fun install(struct: StructView, host: StructHost): View {
        this.host = host
        val context = requireNotNull(getContext(host))

        val decor = getView(context, host)
        decor.id = View.generateViewId()
        decor.layoutParams = LPUtil.viewGroup()

        var titleExist = false
        struct[TitleStruct]?.just {
            titleExist = true
            val constraint = LPUtil.constraint(height = 48)
            constraint.startToStart = R.id.parent
            constraint.topToTop = R.id.parent
            val view = getView(context, host)
            view.id = R.id.struct_title
            decor.addView(view, constraint)
        }

        struct[ErrorStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = R.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
            val view = getView(context, host)
            view.id = R.id.struct_error
            decor.addView(view, constraint)
            view.gone()
        }

        val binding = host.binding()
        val contentConstraint = LPUtil.constraint()
        contentConstraint.startToStart = R.id.parent
        contentConstraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
        if (binding.root.id == View.NO_ID) binding.root.id = View.generateViewId()
        contentView = binding.root
        decor.addView(binding.root, contentConstraint)

        struct[LoadingStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = R.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
            val view = getView(context, host)
            view.id = R.id.struct_loading
            decor.addView(view, constraint)

            view.gone()
        }

        host.onSetup(decor, binding)

        layout = decor

        return layout ?: LinearLayout(context)
    }

    override fun onLifeCycleEvent(event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            // too lazy to impl lazy mode
            host.onViewStateChanged(OnLoaded)
        }
    }

    override fun getView(context: Context, host: StructHost): ConstraintLayout {
        return layout ?: ConstraintLayout(context).apply {
            setBackgroundColor(Color.parseColor("#2f2f2f"))
        }
    }

    override fun updateState(state: LoadingState) {
        contentView?.isVisible = state != LoadingState.ERROR
    }
}