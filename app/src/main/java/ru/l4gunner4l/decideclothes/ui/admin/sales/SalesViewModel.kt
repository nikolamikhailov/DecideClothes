package ru.l4gunner4l.decideclothes.ui.admin.sales

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.local.entity.Sale
import ru.l4gunner4l.decideclothes.local.entity.toEntity
import ru.l4gunner4l.decideclothes.ui.DetailsSaleScreen
import ru.terrakok.cicerone.Router

class SalesViewModel(
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<SalesViewState>() {

    override fun initialViewState(): SalesViewState {
        return SalesViewState(
            STATUS.LOAD,
            emptyList()
        )
    }

    override fun reduce(event: Event, previousState: SalesViewState): SalesViewState? {
        when (event) {
            is UiEvent.AddSale -> {
                router.navigateTo(DetailsSaleScreen())
            }
            is UiEvent.OnSaleClick -> {
                router.navigateTo(DetailsSaleScreen(previousState.salesList[event.pos]))
            }
            is UiEvent.DeleteSale -> {
                viewModelScope.launch {
                    repo.deleteSaleGoodsBySaleId(previousState.salesList[event.pos].id)
                    repo.delete(previousState.salesList[event.pos].toEntity())
                    processDataEvent(
                        DataEvent.SuccessRequestSales(
                            previousState.salesList.filterIndexed { index, _ -> index != event.pos }
                        ))
                }
            }
            is DataEvent.RequestSales -> {
                viewModelScope.launch {

                    val sales = repo.readAllSales()
                        .map { saleEntity ->
                            Sale(
                                id = saleEntity.id, name = saleEntity.name,
                                kind = repo.readKindById(saleEntity.kindId),
                                goods = repo.readGoodsIdsBySaleId(saleEntity.id)
                                    .map { repo.readGoodById(it) }
                                    .map {
                                        Good(
                                            it.id, it.name,
                                            repo.readBrandById(it.brandId),
                                            repo.readKindById(it.kindId),
                                            it.price, it.image
                                        )
                                    }
                            )
                        }
                    processDataEvent(DataEvent.SuccessRequestSales(sales))
                }
            }
            is DataEvent.SearchSales -> {
                viewModelScope.launch {
                    val sales = repo.searchSales(event.query)
                        .map { saleEntity ->
                            Sale(
                                id = saleEntity.id, name = saleEntity.name,
                                kind = repo.readKindById(saleEntity.kindId),
                                goods = repo.readGoodsIdsBySaleId(saleEntity.id)
                                    .map { repo.readGoodById(it) }
                                    .map {
                                        Good(
                                            it.id, it.name,
                                            repo.readBrandById(it.brandId),
                                            repo.readKindById(it.kindId),
                                            it.price, it.image
                                        )
                                    }
                            )
                        }
                    processDataEvent(DataEvent.SuccessRequestSales(sales))
                }
            }
            is DataEvent.SuccessRequestSales -> {
                return previousState.copy(
                    status = STATUS.CONTENT,
                    salesList = event.salesList
                )
            }
        }
        return null
    }
}