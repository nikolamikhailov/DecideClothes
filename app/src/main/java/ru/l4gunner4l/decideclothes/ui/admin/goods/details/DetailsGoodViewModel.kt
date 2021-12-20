package ru.l4gunner4l.decideclothes.ui.admin.goods.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.SingleLiveEvent
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.local.entity.GoodEntity
import ru.l4gunner4l.decideclothes.local.entity.KindEntity
import ru.l4gunner4l.decideclothes.ui.admin.goods.DataEvent
import ru.l4gunner4l.decideclothes.ui.admin.goods.UiEvent
import ru.terrakok.cicerone.Router

class DetailsGoodViewModel(
    private val good: Good?,
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<GoodViewState>() {

    private val brandsLiveData = MutableLiveData<List<BrandEntity>>(emptyList())
    val brands: LiveData<List<BrandEntity>>
        get() = brandsLiveData

    private val kindsLiveData = MutableLiveData<List<KindEntity>>(emptyList())
    val kinds: LiveData<List<KindEntity>>
        get() = kindsLiveData

    override fun initialViewState(): GoodViewState {
        return GoodViewState(STATUS.LOAD, null)
    }

    override fun reduce(event: Event, previousState: GoodViewState): GoodViewState? {
        when (event) {
            is DataEvent.InfoRequest -> {
                viewModelScope.launch {
                    brandsLiveData.postValue(repo.readAllBrands())
                    kindsLiveData.postValue(repo.readAllKinds())
                    processDataEvent(DataEvent.SuccessInfoRequest)
                }
            }
            is DataEvent.SuccessInfoRequest -> {
                return previousState.copy(
                    status = STATUS.CONTENT,
                    good = good
                )
            }
            is UiEvent.SaveClick -> {
                viewModelScope.launch { with(event) {
                    if (isEdit) {
                        GoodEntity(
                            id = good!!.id,
                            name = name,
                            brandId = brandsLiveData.value!![brandPos].id,
                            kindId = kindsLiveData.value!![kindPos].id,
                            price = price,
                            image = image
                        ).let { repo.updateGood(it) }
                    } else {
                        GoodEntity(
                            name = name,
                            brandId = brandsLiveData.value!![brandPos].id,
                            kindId = kindsLiveData.value!![kindPos].id,
                            price = price,
                            image = image
                        ).let { repo.addGood(it) }
                    }
                    router.exit()
                }}
            }
            is UiEvent.ExitClick -> {
                router.exit()
            }
        }
        return null
    }
}

data class GoodViewState(
    val status: STATUS,
    val good: Good?
)

