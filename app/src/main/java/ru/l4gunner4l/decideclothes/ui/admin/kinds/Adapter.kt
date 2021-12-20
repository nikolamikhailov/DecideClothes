package ru.l4gunner4l.decideclothes.ui.admin.kinds

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_good.*
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.entity.KindEntity

fun kindsAdapterDelegate(
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
): AdapterDelegate<List<ListItem>> {
    return adapterDelegateLayoutContainer<KindEntity, ListItem>(
        R.layout.item_kind
    ) {
        containerView.setOnClickListener {
            onClick(adapterPosition)
        }
        containerView.setOnLongClickListener {
            onLongClick(adapterPosition)
            return@setOnLongClickListener true
        }
        bind {
            with(item) {
                nameTV.text = name
            }
        }
    }
}