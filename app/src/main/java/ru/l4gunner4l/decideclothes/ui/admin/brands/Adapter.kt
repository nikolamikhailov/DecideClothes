package ru.l4gunner4l.decideclothes.ui.admin.brands

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_brand.*
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity

fun brandsAdapterDelegate(
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
): AdapterDelegate<List<ListItem>> {
    return adapterDelegateLayoutContainer<BrandEntity, ListItem>(
        R.layout.item_brand
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
                Glide.with(containerView.context)
                    .load(image)
                    .error(R.drawable.ic_baseline_crop_original_24)
                    .into(brandIV)
            }
        }
    }
}