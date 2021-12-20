package ru.l4gunner4l.decideclothes.ui.admin.kinds.details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_admin_details_kind.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.KindEntity
import ru.l4gunner4l.decideclothes.ui.admin.kinds.UiEvent

class DetailsKindFragment : Fragment(R.layout.fragment_admin_details_kind) {

    companion object {
        private const val KEY_KIND = "KEY_KIND"
        fun newInstance(kind: KindEntity? = null) =
            DetailsKindFragment().apply {
                arguments = bundleOf(KEY_KIND to kind)
                isEditMode = kind != null
            }
    }

    private var isEditMode = false
    private val viewModel: DetailsKindViewModel by inject() {
        parametersOf(requireArguments().getParcelable<KindEntity>(KEY_KIND))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    private fun render(viewState: KindViewState) {
        when(viewState.status) {
            STATUS.CONTENT -> {
                if (isEditMode)
                    with(viewState.kind!!) {
                        nameET.setText(name)
                    }
            }
        }
    }

    private fun initView() {
        labelKindMode.text = if (isEditMode) "Изменение стиля" else "Добавление стиля"
        btnSave.setOnClickListener {
            viewModel.processUiEvent(
                UiEvent.SaveKind(
                    if (isEditMode)
                        KindEntity(
                            id = requireArguments().getParcelable<KindEntity>(KEY_KIND)!!.id,
                            name = nameET.text.toString()
                        )
                    else KindEntity(name = nameET.text.toString())
                )
            )
        }
        btnBack.setOnClickListener { viewModel.processUiEvent(UiEvent.ExitClick) }
    }

}