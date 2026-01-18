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

package com.example.victor_ai.ui.map.canvas.gestures

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.victor_ai.ui.map.canvas.controllers.MapController
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.renderer.POIMarkerRenderer
import com.example.victor_ai.ui.map.utils.CoordinateConverter

/**
 * 🎮 Обработчик жестов для карты (скролл, зум, клики)
 */
class MapGestureHandler(
    context: Context,
    private val mapController: MapController,
    private val markerRenderer: POIMarkerRenderer
) {
    private val gestureDetector = GestureDetector(context, GestureListener())
    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())

    var onPOIClicked: ((POI) -> Unit)? = null
    var pois: List<POI> = emptyList()

    /**
     * Обрабатывает touch события
     */
    fun handleTouchEvent(event: MotionEvent): Boolean {
        var handled = scaleDetector.onTouchEvent(event)
        handled = gestureDetector.onTouchEvent(event) || handled
        return handled
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            mapController.applyScroll(distanceX, distanceY)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            handleClick(e.x, e.y)
            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            mapController.applyScale(scaleFactor)
            return true
        }
    }

    /**
     * Обрабатывает клик на карте
     */
    private fun handleClick(x: Float, y: Float) {
        val converter = mapController.coordinateConverter ?: return

        // Находим POI, на который кликнули
        val clickedPOI = markerRenderer.findClickedPOI(pois, x, y, converter)

        if (clickedPOI != null) {
            onPOIClicked?.invoke(clickedPOI)
        }
    }
}

