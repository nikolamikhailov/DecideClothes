package ru.l4gunner4l.decideclothes.ui.admin.brands

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
import kotlinx.android.synthetic.main.fragment_admin_brands.*
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.setAdapterAndCleanupOnDetachFromWindow
import ru.l4gunner4l.decideclothes.base.setData

class BrandsFragment : Fragment(R.layout.fragment_admin_brands) {

    private val handler: Handler by lazy { Handler(Looper.myLooper()!!) }
    private val viewModel: BrandsViewModel by inject()
    private val adapter = ListDelegationAdapter(
        brandsAdapterDelegate(
            { viewModel.processUiEvent(UiEvent.ClickBrand(it)) },
            { viewModel.processUiEvent(UiEvent.DeleteBrand(it)) }
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
        viewModel.deleteErrorToast.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
        viewModel.processDataEvent(DataEvent.RequestBrands)
    }

    private fun initView() {
        rvBrands.setAdapterAndCleanupOnDetachFromWindow(adapter)
        rvBrands.addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        rvBrands.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        btnAddBrand.setOnClickListener { viewModel.processDataEvent(UiEvent.AddBrand) }
        searchBrand.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    searchBrands(newText)
                }, 1000)
                return true
            }

        })
    }

    private fun searchBrands(newText: String?) {
        if (newText != null && newText.isNotBlank())
            viewModel.processDataEvent(DataEvent.SearchBrands(newText))
        else viewModel.processDataEvent(DataEvent.RequestBrands)
    }

    private fun render(viewState: BrandsViewState) {
        when (viewState.status) {
            STATUS.CONTENT -> {
                brandsEmptyTV.isVisible = viewState.brandsList.isEmpty()
                adapter.setData(viewState.brandsList)
            }
        }
    }
}