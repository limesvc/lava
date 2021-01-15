package lava.core.design.view.decor

/**
 * Created by svc on 2021/01/11
 */
interface StructView {

}

interface ContentStruct : StructView

class DefaultStructView : ContentStruct {
}

interface NavigationStruct : StructView

class DefaultNavigationView : NavigationStruct