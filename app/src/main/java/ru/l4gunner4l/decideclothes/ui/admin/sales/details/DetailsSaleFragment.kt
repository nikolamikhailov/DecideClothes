package ru.l4gunner4l.decideclothes.ui.admin.sales.details

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_admin_details_sale.*
import kotlinx.android.synthetic.main.fragment_admin_details_sale.btnBack
import kotlinx.android.synthetic.main.fragment_admin_details_sale.kindSpinner
import kotlinx.android.synthetic.main.fragment_admin_details_sale.nameET
import org.koin.core.parameter.parametersOf
import ru.l4gunner4l.decideclothes.R
import org.koin.android.ext.android.inject
import ru.l4gunner4l.decideclothes.base.setAdapterAndCleanupOnDetachFromWindow
import ru.l4gunner4l.decideclothes.local.entity.DetailsSale
import ru.l4gunner4l.decideclothes.local.entity.Sale
import ru.l4gunner4l.decideclothes.ui.admin.sales.Adapter

class DetailsSaleFragment : Fragment(R.layout.fragment_admin_details_sale) {

    companion object {
        private const val KEY_SALE = "KEY_SALE"
        fun newInstance(sale: Sale?): DetailsSaleFragment =
            DetailsSaleFragment().apply {
                arguments = bundleOf(KEY_SALE to sale)
                isEditMode = sale != null
            }
    }

    private var isEditMode = false
    private val viewModel: DetailsSaleViewModel by inject() {
        parametersOf(requireArguments().getParcelable<Sale>(KEY_SALE))
    }
    private val adapter: Adapter by lazy {
        Adapter(
            onCheckBoxClicked = { position, isChecked ->
                viewModel.onGoodCheckClicked(position, isChecked)
            }
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.kinds.observe(viewLifecycleOwner, { list ->
            kindSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list.map { kind -> kind.name }
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            val indexOf = list.map { it.id }
                .indexOf(viewModel.sale.value?.id)
            if (indexOf >=0 ) kindSpinner.setSelection(indexOf)
        })
        viewModel.sale.observe(viewLifecycleOwner, { sale: DetailsSale? ->
            nameET.setText(sale?.name ?: "")
            saleGoodsEmptyTV.isVisible = sale?.goods?.isEmpty() ?: true
            adapter.setData(sale?.goods ?: emptyList())
        })
        viewModel.errorToast.observe(
            viewLifecycleOwner,
            { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() })
        viewModel.requestInfo()
        initView()
    }

    private fun initView() {
        adapter
        labelSaleMode.text = if (isEditMode) "Измененение распродажи" else "Добавление распродажи"
        rvSaleGoods.setAdapterAndCleanupOnDetachFromWindow(adapter)
        rvSaleGoods.addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        rvSaleGoods.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        kindSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.kindSelected(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        btnSaveSale.setOnClickListener {
            viewModel.onSaveClick(name = nameET.text.toString())
        }
        btnBack.setOnClickListener { viewModel.onExitClick() }
    }


}