package com.example.myapplication

class Repository(private val dao: CarPassDao) {

    // Inserta un evento con el timestamp actual
    suspend fun insertNow() =
        dao.insert(CarPassEvent(timestampMillis = System.currentTimeMillis()))

    // Devuelve el conteo del mes actual
    suspend fun countMonth(): Int {
        val (s, e) = DateUtils.currentMonthBoundsMillis()
        return dao.countBetween(s, e)
    }
}
