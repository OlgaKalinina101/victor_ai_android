package com.example.victor_ai.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.data.network.dto.MemoryResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesSheet(
    memories: List<MemoryResponse>,
    loading: Boolean,
    error: String?,
    onDelete: (String) -> Unit,
    onUpdate: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val grayText = Color(0xFFE0E0E0)
    val backgroundCard = Color.Transparent
    val barFilled = Color(0xFFCCCCCC)
    val barEmpty = Color(0xFF555555)
    val fontSize = 18.sp

    // Состояния для фильтров
    var hasCriticalFilter by remember { mutableStateOf<Boolean?>(null) }
    var categoryFilter by remember { mutableStateOf<String?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // Состояния для сортировки
    var sortBy by remember { mutableStateOf("last_used") }
    var showSortDropdown by remember { mutableStateOf(false) }

    // Состояние списка для скролла
    val listState = rememberLazyListState()

    // Уникальные категории для фильтра
    val categories = memories.map { it.metadata["category"]?.toString() ?: "Без категории" }
        .distinct()
        .sorted()

    // Фильтрация и сортировка воспоминаний
    val filteredAndSortedMemories = memories
        .filter { memory ->
            val hasCritical = memory.metadata["has_critical"] as? Boolean ?: false
            val category = memory.metadata["category"]?.toString() ?: "Без категории"
            (hasCriticalFilter == null || hasCritical == hasCriticalFilter) &&
                    (categoryFilter == null || category == categoryFilter)
        }
        .sortedByDescending { memory ->
            when (sortBy) {
                "impressive" -> {
                    val value = memory.metadata["impressive"]
                    when (value) {
                        is Int -> value.toLong()
                        is Double -> value.toLong()
                        is String -> value.toDoubleOrNull()?.toLong() ?: 0L
                        else -> 0L
                    }
                }
                "frequency" -> {
                    val value = memory.metadata["frequency"]
                    when (value) {
                        is Int -> value.toLong()
                        is Double -> value.toLong()
                        is String -> value.toDoubleOrNull()?.toLong() ?: 0L
                        else -> 0L
                    }
                }
                "last_used" -> {
                    val lastUsed = memory.metadata["last_used"]?.toString()
                    if (lastUsed != null) {
                        try {
                            ZonedDateTime.parse(lastUsed).toEpochSecond()
                        } catch (e: Exception) {
                            0L
                        }
                    } else {
                        0L
                    }
                }
                else -> 0L
            }
        }

    // Автоскролл при смене сортировки
    LaunchedEffect(sortBy) {
        if (filteredAndSortedMemories.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .nestedScroll(rememberNestedScrollInteropConnection()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Заголовок
        Text(
            text = "Воспоминания",
            fontSize = 20.sp,
            color = grayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Фильтры и сортировка — два уровня
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Первая строка: чекбокс + категория
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Фильтр по has_critical
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = hasCriticalFilter == true,
                        onCheckedChange = { checked ->
                            hasCriticalFilter = if (checked) true else null
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF77FF77),
                            uncheckedColor = barEmpty,
                            checkmarkColor = Color.Black
                        )
                    )
                    Text("Критичные", fontSize = 14.sp, color = grayText)
                }

                // Фильтр по категории
                Box {
                    OutlinedButton(
                        onClick = { showCategoryDropdown = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = grayText
                        ),
                        border = BorderStroke(1.dp, barEmpty),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = categoryFilter ?: "Все категории",
                            fontSize = 14.sp,
                            color = grayText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все категории", fontSize = 14.sp) },
                            onClick = {
                                categoryFilter = null
                                showCategoryDropdown = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, fontSize = 14.sp) },
                                onClick = {
                                    categoryFilter = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Вторая строка: сортировка
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { showSortDropdown = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = grayText
                    ),
                    border = BorderStroke(1.dp, barEmpty),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = when (sortBy) {
                            "impressive" -> "Сортировка: По значимости"
                            "frequency" -> "Сортировка: По частоте"
                            "last_used" -> "Сортировка: По дате"
                            else -> "Сортировка: По дате"
                        },
                        fontSize = 14.sp,
                        color = grayText
                    )
                }
                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("По значимости", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "impressive"
                            showSortDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("По частоте", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "frequency"
                            showSortDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("По дате", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "last_used"
                            showSortDropdown = false
                        }
                    )
                }
            }
        }

        // Список воспоминаний
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = barFilled
            )
        } else if (error != null) {
            Text(
                text = "Ошибка: $error",
                fontSize = fontSize,
                color = Color(0xFFFF7777),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (filteredAndSortedMemories.isEmpty()) {
            Text(
                text = "Нет воспоминаний",
                fontSize = fontSize,
                color = barEmpty,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                state = listState, // ← добавлено
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAndSortedMemories, key = { it.id }) { memory ->
                    MemoryItem(
                        memory = memory,
                        onDelete = { recordId ->
                            onDelete(recordId)
                        },
                        onUpdate = { id, newText -> onUpdate(id, newText) },
                        fontSize = fontSize,
                        grayText = grayText,
                        barEmpty = barEmpty,
                        backgroundCard = backgroundCard,
                    )
                }
            }
        }
    }
}

@Composable
fun MemoryItem(
    memory: MemoryResponse,
    onDelete: (String) -> Unit,
    onUpdate: (String, String) -> Unit, // ← новый коллбэк (id, новый текст)
    fontSize: TextUnit,
    grayText: Color,
    barEmpty: Color,
    backgroundCard: Color
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(memory.text) }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundCard),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, barEmpty),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Текст воспоминания (редактируемый или обычный)
            if (isEditing) {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = fontSize,
                        color = grayText
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF77FF77),
                        unfocusedBorderColor = barEmpty,
                        cursorColor = grayText,
                        focusedTextColor = grayText,
                        unfocusedTextColor = grayText
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            } else {
                Text(
                    text = memory.text,
                    fontSize = fontSize,
                    color = grayText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { isEditing = true } // ← клик для редактирования
                )
            }

            // Разделитель
            HorizontalDivider(
                thickness = 1.dp,
                color = barEmpty.copy(alpha = 0.3f)
            )

            // Метаданные + действия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Метаданные (только если не редактируем)
                if (!isEditing) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "📁 ${memory.metadata["category"]?.toString() ?: "Без категории"}",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "⭐ ${memory.metadata["impressive"]?.toString() ?: "0"} | 🔄 ${memory.metadata["frequency"]?.toString() ?: "0"}",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "🕒 ${
                                memory.metadata["last_used"]?.toString()?.let {
                                    try {
                                        ZonedDateTime.parse(it).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                                    } catch (e: Exception) {
                                        "—"
                                    }
                                } ?: "—"
                            }",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                    }

                    // Кнопка удаления
                    IconButton(
                        onClick = { onDelete(memory.id) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = Color(0xFFFF7777)
                        )
                    }
                } else {
                    // Кнопки сохранить/отменить при редактировании
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Отмена
                        IconButton(
                            onClick = {
                                editedText = memory.text
                                isEditing = false
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Отмена",
                                tint = Color(0xFFFF7777)
                            )
                        }

                        // Сохранить
                        IconButton(
                            onClick = {
                                if (editedText.isNotBlank() && editedText != memory.text) {
                                    onUpdate(memory.id, editedText)
                                }
                                isEditing = false
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Сохранить",
                                tint = Color(0xFF77FF77)
                            )
                        }
                    }
                }
            }
        }
    }
}