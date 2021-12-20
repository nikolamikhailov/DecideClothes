package ru.l4gunner4l.decideclothes.ui.admin.brands

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.SingleLiveEvent
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.ui.DetailsBrandScreen
import ru.terrakok.cicerone.Router

class BrandsViewModel(
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<BrandsViewState>() {

    private val deleteSingleLiveEvent = SingleLiveEvent<String>()
    val deleteErrorToast: LiveData<String>
        get() = deleteSingleLiveEvent

    override fun initialViewState(): BrandsViewState {
        return BrandsViewState(
            STATUS.LOAD,
            emptyList()
        )
    }

    override fun reduce(event: Event, previousState: BrandsViewState): BrandsViewState? {
        when (event) {
            is UiEvent.AddBrand -> {
                router.navigateTo(DetailsBrandScreen())
            }
            is UiEvent.ClickBrand -> {
                router.navigateTo(DetailsBrandScreen(previousState.brandsList[event.position]))
            }
            is UiEvent.DeleteBrand -> {
                viewModelScope.launch {
                    val isSuccess = repo.deleteBrand(previousState.brandsList[event.position])
                    if (isSuccess)
                        processDataEvent(DataEvent.SuccessRequestBrands(
                            previousState.brandsList.filterIndexed { index, _ -> index != event.position }
                        ))
                    else deleteSingleLiveEvent.postValue("Есть товары, которые являются вещами этого бренда. Сначала удалите их")
                }
            }
            is DataEvent.RequestBrands -> {
                viewModelScope.launch {
                    val brands = repo.readAllBrands()
                    processDataEvent(DataEvent.SuccessRequestBrands(brands))
                }
            }
            is DataEvent.SearchBrands -> {
                viewModelScope.launch {
                    val brands = repo.searchBrands(event.query)
                    processDataEvent(DataEvent.SuccessRequestBrands(brands))
                }
            }
            is DataEvent.SuccessRequestBrands -> {
                return previousState.copy(
                    status = STATUS.CONTENT,
                    brandsList = event.brandsList
                )
            }
        }
        return null
    }
}