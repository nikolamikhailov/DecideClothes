package ru.l4gunner4l.decideclothes.ui

import androidx.fragment.app.Fragment
import ru.l4gunner4l.decideclothes.local.entity.*
import ru.l4gunner4l.decideclothes.ui.sign.LoginFragment
import ru.l4gunner4l.decideclothes.ui.sign.RegisterFragment
import ru.l4gunner4l.decideclothes.ui.admin.MainFragment
import ru.l4gunner4l.decideclothes.ui.admin.brands.details.DetailsBrandFragment
import ru.l4gunner4l.decideclothes.ui.admin.goods.details.DetailsGoodFragment
import ru.l4gunner4l.decideclothes.ui.admin.kinds.details.DetailsKindFragment
import ru.l4gunner4l.decideclothes.ui.admin.sales.details.DetailsSaleFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class LoginScreen : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return LoginFragment.newInstance()
    }
}

class RegisterScreen : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return RegisterFragment()
    }
}



class AdminMainScreen(val user: UserEntity) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return MainFragment.newInstance(user)
    }
}
class MainScreen(val user: UserEntity) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return MainFragment.newInstance(user)
    }
}



class DetailsGoodScreen(private val good: Good? = null) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return DetailsGoodFragment.newInstance(good)
    }
}
class DetailsBrandScreen(private val brand: BrandEntity? = null) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return DetailsBrandFragment.newInstance(brand)
    }
}
class DetailsKindScreen(private val kind: KindEntity? = null) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return DetailsKindFragment.newInstance(kind)
    }
}
class DetailsSaleScreen(private val sale: Sale? = null) : SupportAppScreen() {

    override fun getFragment(): Fragment? {
        return DetailsSaleFragment.newInstance(sale)
    }
}