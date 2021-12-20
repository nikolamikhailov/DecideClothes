package ru.l4gunner4l.decideclothes.ui.admin.goods

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_admin_goods.*
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.setAdapterAndCleanupOnDetachFromWindow
import ru.l4gunner4l.decideclothes.base.setData

class GoodsFragment : Fragment(R.layout.fragment_admin_goods) {

    private val handler: Handler by lazy { Handler(Looper.myLooper()!!) }
    private val viewModel: GoodsViewModel by inject()
    private val adapter = ListDelegationAdapter(
        goodsAdapterDelegate(
            { viewModel.processUiEvent(UiEvent.GoodClick(it)) },
            { viewModel.processUiEvent(UiEvent.DeleteGood(it)) }
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
        viewModel.deleteErrorToast.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        viewModel.processDataEvent(DataEvent.RequestGoods)
    }

    private fun initView() {
        rvGoods.setAdapterAndCleanupOnDetachFromWindow(adapter)
        rvGoods.addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        rvGoods.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        btnAddGood.setOnClickListener { viewModel.processDataEvent(UiEvent.AddGood) }
        searchGood.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    searchGoods(newText)
                }, 1000)
                return true
            }

        })
    }

    private fun searchGoods(newText: String?) {
        if (newText != null && newText.isNotBlank())
            viewModel.processDataEvent(DataEvent.SearchGoods(newText))
        else viewModel.processDataEvent(DataEvent.RequestGoods)
    }

    private fun render(viewState: GoodsViewState) {
        when (viewState.status) {
            STATUS.LOAD -> {
            }
            STATUS.CONTENT -> {
                goodsEmptyTV.isVisible = viewState.goodsList.isEmpty()
                adapter.setData(viewState.goodsList)
            }
            STATUS.ERROR -> {
            }
        }
    }
}