package lava.core.design.view.struct

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import lava.core.net.LoadingState

/**
 * Created by svc on 2021/2/12
 */
class TitleView : TitleStructX() {
    override fun getView(context: Context, host: StructHost): View {
        return Toolbar(context)
    }

    override fun updateState(state: LoadingState) {

    }
}