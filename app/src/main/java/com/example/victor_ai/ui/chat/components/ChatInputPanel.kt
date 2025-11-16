package com.example.victor_ai.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Панель ввода сообщений
 */
@Composable
fun ChatInputPanel(
    userInput: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachClick: () -> Unit
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2929))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [📎] Прикрепить
        IconButton(
            onClick = onAttachClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Прикрепить",
                tint = Color(0xFFE0E0E0)
            )
        }

        // Поле ввода
        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFFBB86FC)
            ),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text("текст...", color = Color.Gray, fontSize = 14.sp, fontFamily = didactGothicFont)
            },
            textStyle = TextStyle(fontFamily = didactGothicFont)
        )

        // [▶] Отправить
        IconButton(
            onClick = onSend,
            modifier = Modifier.size(40.dp)
        ) {
            Text("▶", fontSize = 20.sp, color = Color(0xFFE0E0E0), fontFamily = didactGothicFont)
        }
    }
}
