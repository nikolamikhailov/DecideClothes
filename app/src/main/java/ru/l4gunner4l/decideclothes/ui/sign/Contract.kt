package ru.l4gunner4l.decideclothes.ui.sign

import ru.l4gunner4l.decideclothes.base.Event
import ru.l4gunner4l.decideclothes.local.entity.UserEntity

sealed class UiEvent() : Event {
    data class CreateUser(val email: String, val password: String) : UiEvent()
    data class LoginUser(val email: String, val password: String) : UiEvent()
}

sealed class DataEvent() : Event {
    data class UserCreated(val user: UserEntity): DataEvent()
    data class UserFound(val user: UserEntity): DataEvent()
    data class UserFoundError(val textError: String): DataEvent()
}