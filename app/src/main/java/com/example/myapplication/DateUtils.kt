package com.example.myapplication

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    /**
     * Devuelve el inicio y fin (inclusive) del mes actual, en milisegundos epoch.
     */
    fun currentMonthBoundsMillis(): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()

        val firstDay = LocalDate.now().withDayOfMonth(1)
        val startMs = firstDay.atStartOfDay(zone).toInstant().toEpochMilli()

        // comienzo del mes siguiente - 1 ms  -> fin inclusivo del mes actual
        val startNextMonthMs = firstDay.plusMonths(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
        val endMs = startNextMonthMs - 1

        return startMs to endMs
    }

    /**
     * Etiqueta legible del mes actual, ej. "Octubre 2025".
     */
    fun currentMonthLabel(locale: Locale = Locale("es", "ES")): String {
        val now = LocalDate.now()
        val month = now.month.getDisplayName(TextStyle.FULL, locale)
        val monthCap = month.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(locale) else it.toString()
        }
        return "$monthCap ${now.year}"
    }
}