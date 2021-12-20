package ru.l4gunner4l.decideclothes.data

import ru.l4gunner4l.decideclothes.local.dao.UsersDao
import ru.l4gunner4l.decideclothes.local.entity.UserEntity

class SignRepository(
        private val usersDao: UsersDao
) {
    suspend fun getUser(email: String) : UserEntity? {
        return usersDao.read(email)
    }

    suspend fun createUser(user: UserEntity) {
        usersDao.create(user)
    }
}