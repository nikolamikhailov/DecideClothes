package ru.l4gunner4l.decideclothes.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.local.entity.UserEntity
import ru.l4gunner4l.decideclothes.ui.admin.brands.BrandsFragment
import ru.l4gunner4l.decideclothes.ui.admin.goods.GoodsFragment
import ru.l4gunner4l.decideclothes.ui.admin.kinds.KindsFragment
import ru.l4gunner4l.decideclothes.ui.admin.sales.SalesFragment

class MainFragment : Fragment(R.layout.fragment_admin_main) {

    companion object {
        private const val KEY_USER = "KEY_USER"
        fun newInstance(user: UserEntity) = MainFragment().apply {
            arguments = bundleOf(KEY_USER to user)
        }
    }

    private var isFirstLaunch = true
    private val goodsFragment = GoodsFragment()
    private val salesFragment = SalesFragment()
    private val brandsFragment = BrandsFragment()
    private val kindsFragment = KindsFragment()
    private var activeFragment: Fragment = goodsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("M_MAIN", requireArguments().getParcelable<UserEntity>(KEY_USER).toString())
        initUi()
    }

    private fun initUi() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_brands -> {
                    changeFragment(brandsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_goods -> {
                    changeFragment(goodsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_sales -> {
                    changeFragment(salesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_kinds -> {
                    changeFragment(kindsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        if (isFirstLaunch) {
            bottomNav.selectedItemId = R.id.menu_goods
            childFragmentManager.beginTransaction().apply {
                add(R.id.mainFragmentHolder, salesFragment).hide(salesFragment)
                add(R.id.mainFragmentHolder, brandsFragment).hide(brandsFragment)
                add(R.id.mainFragmentHolder, kindsFragment).hide(kindsFragment)
                add(R.id.mainFragmentHolder, goodsFragment)
            }.commit()
            isFirstLaunch = false
        }

    }

    private fun changeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commitNow()
        activeFragment = fragment
    }

}