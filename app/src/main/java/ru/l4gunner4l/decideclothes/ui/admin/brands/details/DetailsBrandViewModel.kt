package ru.l4gunner4l.decideclothes.ui.admin.brands.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity
import ru.l4gunner4l.decideclothes.ui.admin.brands.DataEvent
import ru.l4gunner4l.decideclothes.ui.admin.brands.UiEvent
import ru.terrakok.cicerone.Router

class DetailsBrandViewModel(
    private val brand: BrandEntity?,
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<BrandViewState>() {

    override fun initialViewState(): BrandViewState {
        return BrandViewState(STATUS.CONTENT, brand)
    }

    override fun reduce(event: Event, previousState: BrandViewState): BrandViewState? {
        when (event) {
            is UiEvent.SaveClick -> {
                viewModelScope.launch {
                    event.brand.let {
                        if (brand != null)
                            repo.updateBrand(it)
                        else repo.addBrand(it)
                    }
                    router.exit()
                }
            }
            is UiEvent.ExitClick -> {
                router.exit()
            }
        }
        return null
    }

}

data class BrandViewState(
    val status: STATUS,
    val brand: BrandEntity?
)