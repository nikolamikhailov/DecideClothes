package ru.l4gunner4l.decideclothes.ui.admin.goods

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_good.*
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.local.entity.GoodEntity

fun goodsAdapterDelegate(
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
): AdapterDelegate<List<ListItem>> {
    return adapterDelegateLayoutContainer<Good, ListItem>(
        R.layout.item_good
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
                priceTV.text = price.toString()
                brandTV.text = brand.name
                kindTV.text = kind.name
                Glide.with(containerView.context)
                    .load(image)
                    .error(R.drawable.ic_baseline_crop_original_24)
                    .into(goodIV)
            }
        }
    }
}

