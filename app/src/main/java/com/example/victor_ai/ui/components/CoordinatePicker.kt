/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

/**
 * Компонент для выбора координат на экране
 * Показывает прозрачный оверлей с перетаскиваемым кружочком
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatePicker(
    onCoordinateSelected: (x: Int?, y: Int?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialX: Int = 100,
    initialY: Int = 200,
    instruction: String = "Перетащи кружочек на нужное место"
) {
    val context = LocalContext.current
    val view = LocalView.current
    var circleX by remember { mutableStateOf(initialX.toFloat()) }
    var circleY by remember { mutableStateOf(initialY.toFloat()) }
    var boxSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    var boxPositionOnScreen by remember { mutableStateOf(IntArray(2)) } // Позиция Box на ЭКРАНЕ
    var notAvailable by remember { mutableStateOf(false) } // Галочка "этого пункта нет"
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = androidx.compose.ui.geometry.Size(
                    coordinates.size.width.toFloat(),
                    coordinates.size.height.toFloat()
                )
                // Используем getLocationOnScreen для получения РЕАЛЬНЫХ экранных координат
                // (positionInWindow не работает для Dialog!)
                view.getLocationOnScreen(boxPositionOnScreen)
                Log.d("CoordinatePicker", "Box size: ${coordinates.size.width}x${coordinates.size.height}, position on screen: (${boxPositionOnScreen[0]}, ${boxPositionOnScreen[1]})")
            }
            .background(Color.Black.copy(alpha = 0.1f)) // Полупрозрачный оверлей
            .zIndex(100f) // Поверх всего
    ) {
        // Перетаскиваемый кружочек
        if (boxSize.width > 0 && boxSize.height > 0) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(circleX.roundToInt(), circleY.roundToInt()) }
                    .size(48.dp)
                    .background(Color.Gray, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            circleX += dragAmount.x
                            circleY += dragAmount.y

                            // Ограничиваем координаты в пределах экрана
                            circleX = circleX.coerceIn(0f, boxSize.width - 144f) // 48dp * 3 (примерно для пикселей)
                            circleY = circleY.coerceIn(0f, boxSize.height - 144f)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎯",
                    fontSize = 20.sp
                )
            }
        }

        // Инструкция и галочка вверху
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f), MaterialTheme.shapes.medium)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = instruction,
                color = Color.White,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Checkbox(
                    checked = notAvailable,
                    onCheckedChange = { notAvailable = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        uncheckedColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Этого пункта нет",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Координаты в центре (для отладки)
        val density = LocalContext.current.resources.displayMetrics.density
        val circleSizePx = (48 * density).toInt()
        val screenX = boxPositionOnScreen[0] + circleX.roundToInt() + circleSizePx / 2
        val screenY = boxPositionOnScreen[1] + circleY.roundToInt() + circleSizePx / 2
        
        Text(
            text = "screen: ($screenX, $screenY)",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.7f), MaterialTheme.shapes.medium)
                .padding(8.dp)
        )

        // Кнопки управления внизу
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Отмена")
            }

            Button(
                onClick = {
                    if (notAvailable) {
                        // Если выбрано "этого пункта нет" - отправляем null
                        Log.d("CoordinatePicker", "🎯 === ПУНКТ ОТСУТСТВУЕТ ===")
                        onCoordinateSelected(null, null)
                    } else {
                        // Иначе сохраняем координаты как обычно
                        val density = context.resources.displayMetrics.density
                        
                        // Размер кружочка в пикселях (48dp * density)
                        val circleSizePx = (48 * density).toInt()
                        
                        // Получаем актуальную позицию на экране прямо перед сохранением
                        val actualPosition = IntArray(2)
                        view.getLocationOnScreen(actualPosition)
                        
                        // Центр кружочка относительно Box
                        val circleCenterX = circleX.roundToInt() + circleSizePx / 2
                        val circleCenterY = circleY.roundToInt() + circleSizePx / 2
                        
                        // НАСТОЯЩИЕ screen координаты (позиция View на экране + offset кружочка)
                        val screenX = actualPosition[0] + circleCenterX
                        val screenY = actualPosition[1] + circleCenterY
                        
                        Log.d("CoordinatePicker", "🎯 === СОХРАНЕНИЕ SCREEN КООРДИНАТ ===")
                        Log.d("CoordinatePicker", "  viewPositionOnScreen: (${actualPosition[0]}, ${actualPosition[1]})")
                        Log.d("CoordinatePicker", "  circleCenterInBox: ($circleCenterX, $circleCenterY)")
                        Log.d("CoordinatePicker", "  ✅ SCREEN координаты: ($screenX, $screenY)")
                        
                        onCoordinateSelected(screenX, screenY)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Готово")
            }
        }
    }
}

/**
 * Компонент для выбора нескольких координат (например, для кнопок "добавить в корзину")
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiCoordinatePicker(
    onCoordinatesSelected: (List<Pair<Int, Int>>?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    maxPoints: Int = 5,
    instruction: String = "Перетащи кружочки на кнопки 'добавить в корзину'"
) {
    val context = LocalContext.current
    val view = LocalView.current
    // Храним координаты и флаг "была ли перемещена точка"
    var circles by remember {
        mutableStateOf(
            List(maxPoints) { index ->
                Triple(50f + index * 60f, 150f + index * 40f, false) // x, y, wasMoved
            }
        )
    }
    var boxSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    var boxPositionOnScreen by remember { mutableStateOf(IntArray(2)) } // Позиция Box на ЭКРАНЕ
    var notAvailable by remember { mutableStateOf(false) } // Галочка "этого пункта нет"

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = androidx.compose.ui.geometry.Size(
                    coordinates.size.width.toFloat(),
                    coordinates.size.height.toFloat()
                )
                // Используем getLocationOnScreen для получения РЕАЛЬНЫХ экранных координат
                view.getLocationOnScreen(boxPositionOnScreen)
                Log.d("MultiCoordinatePicker", "Box size: ${coordinates.size.width}x${coordinates.size.height}, position on screen: (${boxPositionOnScreen[0]}, ${boxPositionOnScreen[1]})")
            }
            .background(Color.Black.copy(alpha = 0.1f))
            .zIndex(100f)
    ) {
        // Перетаскиваемые кружочки
        if (boxSize.width > 0 && boxSize.height > 0) {
            circles.forEachIndexed { index, item ->
                val (x, y, wasMoved) = item
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                        .size(40.dp)
                        .background(
                            if (wasMoved) Color(0xFF4CAF50).copy(alpha = 0.8f) else Color.Gray.copy(alpha = 0.8f),
                            CircleShape
                        )
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                circles = circles.toMutableList().apply {
                                    val currentItem = this[index]
                                    val newX = (currentItem.first + dragAmount.x).coerceIn(0f, boxSize.width - 120f)
                                    val newY = (currentItem.second + dragAmount.y).coerceIn(0f, boxSize.height - 120f)
                                    this[index] = Triple(newX, newY, true) // Помечаем как перемещенную
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Инструкция и галочка вверху
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f), MaterialTheme.shapes.medium)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = instruction,
                color = Color.White,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Checkbox(
                    checked = notAvailable,
                    onCheckedChange = { notAvailable = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        uncheckedColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Этого пункта нет",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Кнопки управления внизу
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Отмена")
            }

            Button(
                onClick = {
                    if (notAvailable) {
                        // Если выбрано "этого пункта нет" - отправляем null
                        Log.d("MultiCoordinatePicker", "🎯 === ПУНКТ ОТСУТСТВУЕТ ===")
                        onCoordinatesSelected(null)
                    } else {
                        // Иначе сохраняем координаты как обычно
                        val density = context.resources.displayMetrics.density
                        val circleSizePx = (40 * density).toInt() // Размер кружочка 40dp
                        
                        // Получаем актуальную позицию на экране прямо перед сохранением
                        val actualPosition = IntArray(2)
                        view.getLocationOnScreen(actualPosition)
                        
                        Log.d("MultiCoordinatePicker", "=== СОХРАНЕНИЕ КООРДИНАТ ===")
                        Log.d("MultiCoordinatePicker", "density: $density, circleSizePx: $circleSizePx")
                        Log.d("MultiCoordinatePicker", "viewPositionOnScreen: (${actualPosition[0]}, ${actualPosition[1]})")
                        Log.d("MultiCoordinatePicker", "boxSize: $boxSize")
                        
                        // Отправляем только перемещенные координаты в НАСТОЯЩИХ screen координатах
                        val coordinates = circles
                            .filter { (_, _, wasMoved) -> wasMoved }
                            .mapIndexed { idx, (x, y, _) -> 
                                // НАСТОЯЩИЕ screen координаты (позиция View на экране + offset кружочка)
                                val screenX = actualPosition[0] + x.roundToInt() + circleSizePx / 2
                                val screenY = actualPosition[1] + y.roundToInt() + circleSizePx / 2
                                
                                Log.d("MultiCoordinatePicker", "Точка ${idx + 1}:")
                                Log.d("MultiCoordinatePicker", "  circleOffset: (${x.roundToInt()}, ${y.roundToInt()})")
                                Log.d("MultiCoordinatePicker", "  + viewPosition: (${actualPosition[0]}, ${actualPosition[1]})")
                                Log.d("MultiCoordinatePicker", "  + circleCenter: ${circleSizePx / 2}")
                                Log.d("MultiCoordinatePicker", "  = SCREEN: ($screenX, $screenY)")
                                
                                Pair(screenX, screenY)
                            }
                        onCoordinatesSelected(coordinates)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Готово (${circles.count { it.third }})")
            }
        }
    }
}
