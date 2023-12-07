package ru.gb.zverobukvy.data.check_data

interface CheckData {
    /**
     * Метод проверяет на корректность данные в БД (слова, буквы, кард-сеты)
     */
    suspend fun checkData()
}