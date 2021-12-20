package ru.l4gunner4l.decideclothes.ui.admin.brands

import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity

data class BrandsViewState(
    val status: STATUS,
    val brandsList: List<BrandEntity>
)

sealed class DataEvent: Event {
    object RequestBrands: DataEvent()
    data class SearchBrands(val query: String) : DataEvent()
    data class SuccessRequestBrands(val brandsList: List<BrandEntity>) : DataEvent()
}
sealed class UiEvent: Event {
    data class SaveClick(val brand: BrandEntity) : UiEvent()
    data class ClickBrand(val position: Int) : UiEvent()
    data class DeleteBrand(val position: Int) : UiEvent()
    object ExitClick : UiEvent()
    object AddBrand : UiEvent()
}