package ru.l4gunner4l.decideclothes.ui.sign

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.BaseViewModel
import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.local.entity.UserEntity
import ru.l4gunner4l.decideclothes.data.SignRepository

class LoginViewModel(
        private val repository: SignRepository
) : BaseViewModel<LoginViewState>() {

    override fun initialViewState(): LoginViewState {
        return LoginViewState(LOGIN_STATUS.START, null)
    }

    override fun reduce(event: Event, previousState: LoginViewState): LoginViewState? {
        when (event) {
            is UiEvent.LoginUser -> {
                viewModelScope.launch {
                    val user = repository.getUser(event.email)
                    if (user == null) processDataEvent(DataEvent.UserFoundError("Вы не зарегистрированы!"))
                    else processDataEvent(DataEvent.UserFound(user))
                }
            }
            is DataEvent.UserFound -> {
                return previousState.copy(
                    status = LOGIN_STATUS.LOGINED,
                    user = event.user
                )
            }
            is DataEvent.UserFoundError -> {
                return previousState.copy(
                    status = LOGIN_STATUS.ERROR,
                    user = null
                )
            }
        }
        return null
    }
}

data class LoginViewState(
    val status: LOGIN_STATUS,
    val user: UserEntity?
)
enum class LOGIN_STATUS {
    START,
    LOGINED,
    ERROR
}