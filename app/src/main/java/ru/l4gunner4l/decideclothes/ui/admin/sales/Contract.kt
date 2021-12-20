package ru.l4gunner4l.decideclothes.ui.admin.sales

import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.DetailsSale
import ru.l4gunner4l.decideclothes.local.entity.Sale

data class SalesViewState(
    val status: STATUS,
    val salesList: List<Sale>
)

data class DetailsSaleViewState(
    val status: STATUS,
    val sale: DetailsSale?
)

sealed class DataEvent : Event {
    object RequestSales : DataEvent()
    data class SearchSales(val query: String) : DataEvent()
    data class SuccessRequestSales(val salesList: List<Sale>) : DataEvent()
}
sealed class UiEvent : Event {
    object AddSale: UiEvent()
    data class OnSaleClick(val pos: Int): UiEvent()
    data class DeleteSale(val pos: Int): UiEvent()
}
