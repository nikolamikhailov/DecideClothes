package ru.l4gunner4l.decideclothes.ui.admin.kinds

import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.KindEntity

data class KindsViewState(
    val status: STATUS,
    val kindsList: List<KindEntity>
)

sealed class DataEvent : Event {
    object RequestKinds : DataEvent()
    data class SearchKinds(val query: String) : DataEvent()
    data class SuccessRequestKinds(val kindsList: List<KindEntity>) : DataEvent()
}
sealed class UiEvent : Event {
    object AddKind : UiEvent()
    data class DeleteKind(val position: Int) : UiEvent()
    data class ClickKind(val position: Int) : UiEvent()

    data class SaveKind(val kind: KindEntity) : UiEvent()
    object ExitClick : UiEvent()
}
