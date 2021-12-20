package ru.l4gunner4l.decideclothes.ui.admin.kinds

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_admin_kinds.*
import ru.l4gunner4l.decideclothes.R
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.setAdapterAndCleanupOnDetachFromWindow
import ru.l4gunner4l.decideclothes.base.setData

class KindsFragment : Fragment(R.layout.fragment_admin_kinds) {

    private val handler: Handler by lazy { Handler(Looper.myLooper()!!) }
    private val viewModel: KindsViewModel by inject()
    private val adapter = ListDelegationAdapter(
        kindsAdapterDelegate(
            { viewModel.processUiEvent(UiEvent.ClickKind(it)) },
            { viewModel.processUiEvent(UiEvent.DeleteKind(it)) }
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
        viewModel.deleteErrorToast.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
        viewModel.processDataEvent(DataEvent.RequestKinds)
    }

    private fun initView() {
        rvKinds.setAdapterAndCleanupOnDetachFromWindow(adapter)
        rvKinds.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        btnAddKind.setOnClickListener { viewModel.processDataEvent(UiEvent.AddKind) }
        searchKind.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(
                    {
                        searchKinds(newText)
                    }, 1000
                )
                return true
            }

        })
    }

    private fun searchKinds(newText: String?) {
        if (newText != null && newText.isNotBlank())
            viewModel.processDataEvent(DataEvent.SearchKinds(newText))
        else viewModel.processDataEvent(DataEvent.RequestKinds)
    }

    private fun render(viewState: KindsViewState) {
        when (viewState.status) {
            STATUS.CONTENT -> {
                kindEmptyTV.isVisible = viewState.kindsList.isEmpty()
                adapter.setData(viewState.kindsList)
            }
        }
    }
}