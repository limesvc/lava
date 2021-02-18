package lava.core.design.view.struct

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import lava.core.R
import lava.core.ext.gone
import lava.core.ext.just
import lava.core.util.LPUtil

class DecorView: DecorStructX() {
    private var layout: ConstraintLayout? = null

    override fun install(struct: StructView, host: StructHost): View {
        val context = requireNotNull(getContext(host))

        val decor = getView(context)
        decor.layoutParams = LPUtil.viewGroup()

        var titleExist = false
        struct[TitleStruct]?.just {
            titleExist = true
            val constraint = LPUtil.constraint(height = 48)
            constraint.startToStart = R.id.parent
            constraint.topToTop = R.id.parent
            val view = getView(context)
            view.id = R.id.struct_title
            decor.addView(view, constraint)
        }

        struct[ErrorStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = R.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
            val view = getView(context)
            view.id = R.id.struct_error
            decor.addView(view, constraint)
            view.gone()
        }

        val binding = host.binding()
        val contentConstraint = LPUtil.constraint()
        contentConstraint.startToStart = R.id.parent
        contentConstraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
        decor.addView(binding.root, contentConstraint)

        struct[LoadingStruct]?.just {
            val constraint = LPUtil.constraint()
            constraint.startToStart = R.id.parent
            constraint.topToTop = if (titleExist) R.id.struct_title else R.id.parent
            val view = getView(context)
            view.id = R.id.struct_loading
            decor.addView(view, constraint)
            view.gone()
        }

        host.onSetup(decor, binding)

        layout = decor

        return layout ?: LinearLayout(context)
    }

    override fun getView(context: Context): ConstraintLayout {
        return layout ?: ConstraintLayout(context)
    }
}