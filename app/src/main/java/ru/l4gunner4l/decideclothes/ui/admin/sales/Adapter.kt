package ru.l4gunner4l.decideclothes.ui.admin.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_good.goodIV
import kotlinx.android.synthetic.main.item_sale.*
import kotlinx.android.synthetic.main.item_sale_good.*
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.entity.CheckedGood
import ru.l4gunner4l.decideclothes.local.entity.Sale
import ru.l4gunner4l.decideclothes.local.entity.SaleGood

fun salesAdapterDelegate(
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
): AdapterDelegate<List<ListItem>> {
    return adapterDelegateLayoutContainer<Sale, ListItem>(
        R.layout.item_sale
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
                saleNameTV.text = name
                Glide.with(containerView)
                    .load(goods.getOrNull(0)?.image ?: "")
                    .error(R.drawable.ic_error_24)
                    .into(imageView1)
                Glide.with(containerView)
                    .load(goods.getOrNull(1)?.image ?: "")
                    .error(R.drawable.ic_error_24)
                    .into(imageView2)
                Glide.with(containerView)
                    .load(goods.getOrNull(2)?.image ?: "")
                    .error(R.drawable.ic_error_24)
                    .into(imageView3)
            }
        }
    }
}



class Adapter(var goods: List<CheckedGood> = emptyList(), val onCheckBoxClicked: (Int, Boolean) -> Unit) : RecyclerView.Adapter<Adapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_sale_good, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(goods[position])
    }

    override fun getItemCount(): Int = goods.size

    fun setData(new: List<CheckedGood>) {
        goods = new
        notifyDataSetChanged()
    }

    inner class VH(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            selectCB.setOnCheckedChangeListener { _, isChecked ->
                onCheckBoxClicked(adapterPosition, isChecked)
            }
        }

        fun bind(good: CheckedGood) {
            saleGoodNameTV.text = good.name
            selectCB.isChecked = good.isChecked
            Glide.with(containerView)
                .load(good.image)
                .error(R.drawable.ic_error_24)
                .into(goodIV)
        }
    }
}