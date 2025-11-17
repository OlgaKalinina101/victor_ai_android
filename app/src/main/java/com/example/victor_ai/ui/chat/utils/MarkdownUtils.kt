package com.example.victor_ai.ui.chat.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Форматирование timestamp в HH:mm
 * timestamp приходит с бэкенда в секундах (Unix timestamp)
 */
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    // Конвертируем секунды в миллисекунды для Date()
    return sdf.format(Date(timestamp * 1000))
}

/**
 * Подсветка найденного текста желтым цветом
 */
fun highlightSearchText(annotatedString: AnnotatedString, searchQuery: String): AnnotatedString {
    if (searchQuery.isBlank()) return annotatedString

    return buildAnnotatedString {
        append(annotatedString)

        // Ищем все вхождения searchQuery (регистронезависимо)
        val text = annotatedString.text
        val query = searchQuery.lowercase()
        var startIndex = text.lowercase().indexOf(query)

        while (startIndex >= 0) {
            val endIndex = startIndex + searchQuery.length

            // Применяем желтый фон к найденному тексту
            addStyle(
                style = SpanStyle(
                    background = Color(0xFFFFEB3B), // Желтый фон
                    color = Color(0xFF000000) // Черный текст для контраста
                ),
                start = startIndex,
                end = endIndex
            )

            // Ищем следующее вхождение
            startIndex = text.lowercase().indexOf(query, endIndex)
        }
    }
}

/**
 * Парсинг markdown в AnnotatedString с поддержкой:
 * - **жирный текст**
 * - *курсив*
 * - [текст](url)
 * - **[жирная ссылка](url)**
 */
fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.split("\n")

        lines.forEachIndexed { lineIndex, line ->

            // Регулярки (порядок важен!)
            val boldLinkRegex = """\*\*\[([^\]]+)\]\(([^\)]+)\)\*\*""".toRegex()  // **[text](url)**
            val linkRegex = """\[([^\]]+)\]\(([^\)]+)\)""".toRegex()  // [text](url)
            val boldRegex = """\*\*(.+?)\*\*""".toRegex()  // **text**
            val italicRegex = """\*([^*]+?)\*""".toRegex()  // *text*

            // Находим все совпадения
            val matches = mutableListOf<Triple<IntRange, String, MatchResult>>()

            // Важно: сначала bold+link, потом просто ссылки, потом жирный, потом курсив
            boldLinkRegex.findAll(line).forEach {
                matches.add(Triple(it.range, "boldlink", it))
            }
            linkRegex.findAll(line).forEach {
                matches.add(Triple(it.range, "link", it))
            }
            boldRegex.findAll(line).forEach { matches.add(Triple(it.range, "bold", it)) }
            italicRegex.findAll(line).forEach { matches.add(Triple(it.range, "italic", it)) }

            // Убираем пересекающиеся совпадения
            val filteredMatches = mutableListOf<Triple<IntRange, String, MatchResult>>()
            matches.sortedBy { it.first.first }.forEach { current ->
                val hasOverlap = filteredMatches.any { existing ->
                    current.first.first < existing.first.last && current.first.last > existing.first.first
                }
                if (!hasOverlap) {
                    filteredMatches.add(current)
                }
            }

            var lastIndex = 0
            filteredMatches.forEach { (range, type, match) ->
                // Добавляем текст до совпадения
                if (lastIndex < range.first) {
                    withStyle(SpanStyle(color = Color(0xFFE0E0E0))) {
                        append(line.substring(lastIndex, range.first))
                    }
                }

                when (type) {
                    "boldlink" -> {
                        // **[text](url)** - жирная ссылка
                        val label = match.groupValues[1]
                        val url = match.groupValues[2]
                        val start = length

                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBB86FC),
                            textDecoration = TextDecoration.Underline
                        )) {
                            append(label)
                        }

                        addStringAnnotation(
                            tag = "URL",
                            annotation = url,
                            start = start,
                            end = start + label.length
                        )
                    }
                    "bold" -> {
                        val innerText = match.groupValues[1]
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE0E0E0)
                        )) {
                            append(innerText)
                        }
                    }
                    "italic" -> {
                        val innerText = match.groupValues[1]
                        withStyle(SpanStyle(
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFA0A0A0)
                        )) {
                            append(innerText)
                        }
                    }
                    "link" -> {
                        val label = match.groupValues[1]
                        val url = match.groupValues[2]
                        val start = length

                        withStyle(SpanStyle(
                            color = Color(0xFFBB86FC),
                            textDecoration = TextDecoration.Underline
                        )) {
                            append(label)
                        }

                        addStringAnnotation(
                            tag = "URL",
                            annotation = url,
                            start = start,
                            end = start + label.length
                        )
                    }
                }

                lastIndex = range.last + 1
            }

            // Остаток строки
            if (lastIndex < line.length) {
                withStyle(SpanStyle(color = Color(0xFFE0E0E0))) {
                    append(line.substring(lastIndex))
                }
            }

            // Перенос строки (кроме последней)
            if (lineIndex < lines.size - 1) {
                append("\n")
            }
        }
    }
}
