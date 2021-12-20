package ru.l4gunner4l.decideclothes.ui.admin.kinds

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.base.SingleLiveEvent
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.ui.DetailsKindScreen
import ru.terrakok.cicerone.Router

class KindsViewModel(
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<KindsViewState>() {

    private val deleteSingleLiveEvent = SingleLiveEvent<String>()
    val deleteErrorToast: LiveData<String>
        get() = deleteSingleLiveEvent

    override fun initialViewState(): KindsViewState {
        return KindsViewState(
            STATUS.LOAD,
            emptyList()
        )
    }

    override fun reduce(event: Event, previousState: KindsViewState): KindsViewState? {
        when (event) {
            is UiEvent.AddKind -> {
                router.navigateTo(DetailsKindScreen())
            }
            is UiEvent.ClickKind -> {
                router.navigateTo(DetailsKindScreen(previousState.kindsList[event.position]))
            }
            is UiEvent.DeleteKind -> {
                viewModelScope.launch {
                    val isSuccess = repo.deleteKind(previousState.kindsList[event.position])
                    if (isSuccess)
                        processDataEvent(DataEvent.SuccessRequestKinds(
                            previousState.kindsList.filterIndexed { index, _ ->
                                index != event.position
                            }
                        ))
                    else deleteSingleLiveEvent.postValue("Есть товары, которые являются вещами этого стиля, либо распродажи этого стиля\nСначала удалите их")
                }
            }
            is DataEvent.RequestKinds -> {
                viewModelScope.launch {
                    val kinds = repo.readAllKinds()
                    processDataEvent(DataEvent.SuccessRequestKinds(kinds))
                }
            }
            is DataEvent.SearchKinds -> {
                viewModelScope.launch {
                    val kinds = repo.searchKinds(event.query)
                    processDataEvent(DataEvent.SuccessRequestKinds(kinds))
                }
            }
            is DataEvent.SuccessRequestKinds -> {
                return previousState.copy(
                    status = STATUS.CONTENT,
                    kindsList = event.kindsList
                )
            }
        }
        return null
    }


}