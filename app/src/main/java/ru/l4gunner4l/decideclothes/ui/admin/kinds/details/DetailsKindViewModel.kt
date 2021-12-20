package ru.l4gunner4l.decideclothes.ui.admin.kinds.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.KindEntity
import ru.l4gunner4l.decideclothes.ui.admin.kinds.UiEvent
import ru.terrakok.cicerone.Router

class DetailsKindViewModel(
    private val kind: KindEntity?,
    private val repo: MainRepository,
    private val router: Router
) : BaseViewModel<KindViewState>() {

    override fun initialViewState(): KindViewState {
        return KindViewState(STATUS.CONTENT, kind)
    }

    override fun reduce(event: Event, previousState: KindViewState): KindViewState? {
        when(event) {
            is UiEvent.SaveKind -> {
                viewModelScope.launch {
                    if (kind != null)
                        repo.updateKind(event.kind)
                    else repo.addKind(event.kind)
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

data class KindViewState(
    val status: STATUS,
    val kind: KindEntity?
)