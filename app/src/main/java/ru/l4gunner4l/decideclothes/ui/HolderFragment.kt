package ru.l4gunner4l.decideclothes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.core.qualifier.named
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.di.CLOTHES_QUALIFIER
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class HolderFragment : Fragment(R.layout.fragment_holder) {

    private val navigator: Navigator by lazy { createNavigator() }
    private val router: Router by inject(named(CLOTHES_QUALIFIER))
    private val navigatorHolder: NavigatorHolder by inject(named(CLOTHES_QUALIFIER))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router.navigateTo(LoginScreen())
    }

    private fun createNavigator(): Navigator {
        return SupportAppNavigator(requireActivity(), childFragmentManager, R.id.fragmentHolder)
    }
    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

}