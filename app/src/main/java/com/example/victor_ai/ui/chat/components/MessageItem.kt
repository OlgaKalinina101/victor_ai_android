package com.example.victor_ai.ui.chat.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.ui.chat.utils.formatTimestamp
import com.example.victor_ai.ui.chat.utils.parseMarkdown
import com.example.victor_ai.ui.common.LongClickableText

/**
 * Элемент сообщения в чате
 */
@Composable
fun MessageItem(
    message: ChatMessage,
    isEditing: Boolean,
    editingText: String,
    currentMode: String,
    onEditingTextChange: (String) -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCopy: () -> Unit,
    onTapOutsideLink: () -> Unit = {},
    onLongPressOutsideLink: () -> Unit = {}
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))
    val context = LocalContext.current

    // User-сообщения справа и светлее фона
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUser) Color(0xFF3A3838) else Color.Transparent
    val annotatedText = parseMarkdown(message.text)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = alignment
    ) {
        if (isEditing) {
            // Режим редактирования
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFBB86FC),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color(0xFFBB86FC)
                    ),
                    textStyle = TextStyle(fontSize = 15.sp, fontFamily = didactGothicFont),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelEdit) {
                        Text("Отмена", color = Color.Gray, fontSize = 14.sp, fontFamily = didactGothicFont)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onSaveEdit) {
                        Text("✓", color = Color(0xFFBB86FC), fontSize = 18.sp, fontFamily = didactGothicFont)
                    }
                }
            }
        } else {
            // Обычный режим
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(
                            max = if (message.isUser) 320.dp else 380.dp
                        )
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {

                    // Текст сообщения
                    if (currentMode == "edit mode") {
                        // В edit mode включаем долгий тап для редактирования
                        LongClickableText(
                            text = parseMarkdown(message.text),
                            onLongClick = onStartEdit,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color(0xFFE0E0E0),
                                fontFamily = didactGothicFont
                            )
                        )
                    } else {
                        // В production mode: ссылки кликабельны + жесты ChatBox работают
                        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

                        BasicText(
                            text = annotatedText,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color(0xFFE0E0E0),
                                fontFamily = didactGothicFont
                            ),
                            onTextLayout = { layoutResult.value = it },
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { offset ->
                                        layoutResult.value?.let { layout ->
                                            val position = layout.getOffsetForPosition(offset)
                                            val annotations = annotatedText.getStringAnnotations(
                                                tag = "URL",
                                                start = position,
                                                end = position
                                            )

                                            if (annotations.isNotEmpty()) {
                                                // Клик на ссылку - открываем URL
                                                val url = annotations.first().item
                                                val intent = if (url.contains("openstreetmap.org")) {
                                                    val latRegex = """mlat=([-\d.]+)""".toRegex()
                                                    val lonRegex = """mlon=([-\d.]+)""".toRegex()
                                                    val lat = latRegex.find(url)?.groupValues?.get(1)
                                                    val lon = lonRegex.find(url)?.groupValues?.get(1)

                                                    if (lat != null && lon != null) {
                                                        Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lon?q=$lat,$lon"))
                                                    } else {
                                                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                    }
                                                } else {
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                }
                                                context.startActivity(intent)
                                            } else {
                                                // Клик вне ссылки - закрываем чат
                                                onTapOutsideLink()
                                            }
                                        }
                                    },
                                    onLongPress = {
                                        // Долгий тап - включаем микрофон
                                        onLongPressOutsideLink()
                                    }
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Timestamp + кнопка копирования
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTimestamp(message.timestamp),
                            fontSize = 12.sp,
                            color = Color(0xFF888888),
                            fontFamily = didactGothicFont
                        )

                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Копировать",
                            modifier = Modifier
                                .size(18.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onCopy()
                                },
                            tint = Color(0xFF666666)
                        )
                    }
                }
            }
        }
    }
}
