package ru.l4gunner4l.decideclothes.ui.sign

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.local.entity.UserEntity
import ru.l4gunner4l.decideclothes.data.SignRepository

class RegisterViewModel(
    private val repository: SignRepository
): BaseViewModel<RegisterViewState>() {

    override fun initialViewState(): RegisterViewState {
        return RegisterViewState(REGISTER_STATUS.START, null)
    }

    override fun reduce(event: Event, previousState: RegisterViewState): RegisterViewState? {
        when (event) {
            is UiEvent.CreateUser -> {
                viewModelScope.launch {
                    val user = UserEntity(event.email, event.password)
                    repository.createUser(user)
                    processDataEvent(DataEvent.UserCreated(user))
                }
            }
            is DataEvent.UserCreated -> {
                return previousState.copy(status = REGISTER_STATUS.REGISTERED, user = event.user)
            }
        }
        return null
    }
}

data class RegisterViewState (
    val status: REGISTER_STATUS,
    val user: UserEntity?
)

enum class REGISTER_STATUS {
    START,
    REGISTERED,
    ERROR
}
