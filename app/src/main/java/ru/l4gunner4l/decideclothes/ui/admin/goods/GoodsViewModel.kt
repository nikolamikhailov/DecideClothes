package ru.l4gunner4l.decideclothes.ui.admin.goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.SingleLiveEvent
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.local.entity.GoodEntity
import ru.l4gunner4l.decideclothes.local.entity.toEntity
import ru.l4gunner4l.decideclothes.ui.DetailsGoodScreen
import ru.terrakok.cicerone.Router

class GoodsViewModel(
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<GoodsViewState>() {

    private val deleteSingleLiveEvent = SingleLiveEvent<String>()
    val deleteErrorToast: LiveData<String>
        get() = deleteSingleLiveEvent

    override fun initialViewState(): GoodsViewState {
        return GoodsViewState(
            STATUS.LOAD,
            emptyList()
        )
    }

    override fun reduce(event: Event, previousState: GoodsViewState): GoodsViewState? {
        when (event) {
            is UiEvent.AddGood -> {
                router.navigateTo(DetailsGoodScreen())
            }
            is UiEvent.GoodClick -> {
                router.navigateTo(DetailsGoodScreen(previousState.goodsList[event.pos]))
            }
            is UiEvent.DeleteGood -> {
                viewModelScope.launch {
                    val isSuccess: Boolean = repo.deleteGood(previousState.goodsList[event.pos].toEntity())
                    if (isSuccess) {
                        processDataEvent(DataEvent.SuccessRequestGoods(
                            previousState.goodsList.filterIndexed { index, _ -> index != event.pos }
                        ))
                    } else deleteSingleLiveEvent.postValue("Этот товар находится в распродаже,\nсначала удалите его в распродаже")
                }
            }
            is DataEvent.RequestGoods -> {
                viewModelScope.launch {
                    val goods = repo.readAllGoods().map {
                        Good(
                            it.id, it.name,
                            repo.readBrandById(it.brandId),
                            repo.readKindById(it.kindId),
                            it.price, it.image
                        )
                    }
                    processDataEvent(DataEvent.SuccessRequestGoods(goods))
                }
            }
            is DataEvent.SearchGoods -> {
                viewModelScope.launch {
                    val goods = repo.searchGoods(event.query).map {
                        Good(
                            it.id, it.name,
                            repo.readBrandById(it.brandId),
                            repo.readKindById(it.kindId),
                            it.price, it.image
                        )
                    }
                    processDataEvent(DataEvent.SuccessRequestGoods(goods))
                }
            }
            is DataEvent.SuccessRequestGoods -> {
                return previousState.copy(
                    status = STATUS.CONTENT,
                    goodsList = event.goodsList
                )
            }
        }
        return null
    }
}


