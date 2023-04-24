package lava.core.design.view.struct

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.R as CR
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import lava.core.R
import lava.core.ext.gone
import lava.core.ext.just
import lava.core.net.LoadingState
import lava.core.util.LPUtil
import lava.core.util.dp


class DecorView: DecorStructX() {
    private var layout: ConstraintLayout? = null
    private lateinit var host: StructHost
    private var contentView: View? = null
    private var notLoad = true

    override fun install(struct: StructView, host: StructHost): View {
        this.host = host
        val context = requireNotNull(getContext(host))

        val decor = getView(context, host)
        decor.id = View.generateViewId()
        decor.layoutParams = LPUtil.viewGroup()

        var titleExist = false
        struct[TitleStruct]?.just {
            titleExist = true
            val constraint = LPUtil.constraint(height = 48.dp())
            constraint.startToStart = CR.id.parent
            constraint.topToTop = CR.id.parent
            val view = getView(context, host)
            view.id = R.id.struct_title
            decor.addView(view, constraint)
        }

        struct[ErrorStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = CR.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else CR.id.parent
            val view = getView(context, host)
            view.id = R.id.struct_error
            decor.addView(view, constraint)
            view.gone()
        }

        val binding = host.binding()
        val contentConstraint = LPUtil.constraint()
        contentConstraint.startToStart = CR.id.parent
        contentConstraint.topToTop = if (titleExist) R.id.struct_title else CR.id.parent
        if (binding.root.id == View.NO_ID) binding.root.id = View.generateViewId()
        contentView = binding.root
        decor.addView(binding.root, contentConstraint)

        struct[LoadingStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = CR.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else CR.id.parent
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
        if (event == Lifecycle.Event.ON_RESUME && notLoad) {
            // too lazy to impl lazy mode
            host.onViewStateChanged(OnLoaded)
            notLoad = false
        }
    }

    override fun getView(context: Context, host: StructHost): ConstraintLayout {
        return layout ?: ConstraintLayout(context)
    }

    override fun setBackgroundColor(color: Int) {
        layout?.setBackgroundColor(color)
    }

    override fun updateState(state: LoadingState) {
        contentView?.isVisible = state != LoadingState.ERROR
    }
}