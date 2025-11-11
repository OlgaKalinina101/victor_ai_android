package com.example.victor_ai.auth

/**
 * Модуль авторизации
 * Предоставляет информацию о текущем пользователе
 * TODO: Реализовать полноценную авторизацию
 */
object UserProvider {

    /**
     * Возвращает ID текущего пользователя
     * Пока возвращает заглушку "test_user"
     * TODO: В будущем заменить на реальную логику получения ID пользователя
     */
    fun getCurrentUserId(): String {
        return "test_user"
    }
}
