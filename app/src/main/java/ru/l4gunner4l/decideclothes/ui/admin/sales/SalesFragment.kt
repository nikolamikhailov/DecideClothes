package ru.l4gunner4l.decideclothes.ui.admin.sales

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_admin_sales.*
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.setAdapterAndCleanupOnDetachFromWindow
import ru.l4gunner4l.decideclothes.base.setData

class SalesFragment : Fragment(R.layout.fragment_admin_sales) {

    private val handler: Handler by lazy { Handler(Looper.myLooper()!!) }
    private val viewModel: SalesViewModel by inject()
    private val adapter = ListDelegationAdapter(
        salesAdapterDelegate(
            { viewModel.processUiEvent(UiEvent.OnSaleClick(it)) },
            { viewModel.processUiEvent(UiEvent.DeleteSale(it)) }
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
        viewModel.processDataEvent(DataEvent.RequestSales)
        initView()
    }

    private fun render(viewState: SalesViewState) {
        when(viewState.status) {
            STATUS.CONTENT -> {
                salesEmptyTV.isVisible = viewState.salesList.isEmpty()
                adapter.setData(viewState.salesList)
            }
        }
    }

    private fun initView() {
        rvSales.setAdapterAndCleanupOnDetachFromWindow(adapter)
        rvSales.addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        rvSales.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        btnAddSale.setOnClickListener {
            viewModel.processUiEvent(UiEvent.AddSale)
        }
        searchSale.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    searchSales(newText)
                }, 1000)
                return true
            }
        })
    }

    private fun searchSales(newText: String?) {
        if (newText != null && newText.isNotBlank())
            viewModel.processDataEvent(DataEvent.SearchSales(newText))
        else viewModel.processDataEvent(DataEvent.RequestSales)
    }

}