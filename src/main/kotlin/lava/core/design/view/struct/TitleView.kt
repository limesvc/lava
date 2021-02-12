package lava.core.design.view.struct

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar

/**
 * Created by svc on 2021/2/12
 */
class TitleView : TitleStructX() {
    override fun getView(context: Context): View {
        return Toolbar(context)
    }
}