package ru.l4gunner4l.decideclothes.ui.admin.goods

import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.local.entity.GoodEntity
import ru.l4gunner4l.decideclothes.local.entity.KindEntity

data class GoodsViewState(
    val status: STATUS,
    val goodsList: List<Good>
)

sealed class DataEvent : Event {
    object InfoRequest : DataEvent()
    object RequestGoods : DataEvent()
    data class SearchGoods(val query: String) : DataEvent()
    data class SuccessRequestGoods(val goodsList: List<Good>) : DataEvent()
    object SuccessInfoRequest : DataEvent()
}
sealed class UiEvent : Event {
    data class SaveClick(
        val isEdit: Boolean,
        val name: String,
        val kindPos: Int, val brandPos: Int,
        val price: Int, val image: String
    ): UiEvent()
    object ExitClick: UiEvent()

    object AddGood: UiEvent()
    data class GoodClick(val pos: Int): UiEvent()
    data class DeleteGood(val pos: Int): UiEvent()
}