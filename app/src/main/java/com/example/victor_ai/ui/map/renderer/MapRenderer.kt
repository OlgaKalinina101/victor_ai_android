package com.example.victor_ai.ui.map.renderer

import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.POI

/**
 * 🏗️ Интерфейс для рендеринга карты
 *
 * Архитектура под будущее AR:
 * - Сейчас: Canvas2DMapRenderer (рисование на Canvas)
 * - Будущее: ARCoreMapRenderer (AR рендеринг через ARCore)
 *
 * Это позволит легко переключаться между режимами отображения
 */
interface MapRenderer {

    /**
     * Отрисовывает POI на карте
     *
     * @param pois Список точек интереса для отображения
     */
    fun renderPOIs(pois: List<POI>)

    /**
     * Отрисовывает путь между двумя точками
     *
     * @param from Начальная точка
     * @param to Конечная точка
     */
    fun renderPath(from: LatLng, to: LatLng)

    /**
     * Обновляет позицию пользователя на карте
     *
     * @param location Текущая GPS позиция
     */
    fun updateUserLocation(location: LatLng)

    /**
     * Центрирует карту на определенной точке
     *
     * @param location GPS координаты центра
     * @param zoom Уровень зума (опционально)
     */
    fun centerOnPoint(location: LatLng, zoom: Float? = null)

    /**
     * Очищает карту
     */
    fun clear()

    /**
     * Освобождает ресурсы
     */
    fun cleanup()
}

/**
 * 🎨 Canvas2D реализация MapRenderer
 *
 * Использует наш MapCanvasView для отрисовки
 */
class Canvas2DMapRenderer(
    private val mapView: com.example.victor_ai.ui.map.canvas.MapCanvasView
) : MapRenderer {

    override fun renderPOIs(pois: List<POI>) {
        mapView.updatePOIs(pois)
    }

    override fun renderPath(from: LatLng, to: LatLng) {
        // TODO: Реализовать отрисовку пути на Canvas
        // Можно добавить метод drawPath в MapCanvasView
    }

    override fun updateUserLocation(location: LatLng) {
        mapView.updateUserLocation(location)
    }

    override fun centerOnPoint(location: LatLng, zoom: Float?) {
        mapView.panTo(location)
        zoom?.let { mapView.zoomTo(it) }
    }

    override fun clear() {
        mapView.updatePOIs(emptyList())
    }

    override fun cleanup() {
        // MapCanvasView сам управляет своими ресурсами
    }
}

/**
 * 🥽 AR реализация MapRenderer (заглушка для будущего)
 *
 * Когда будете готовы добавить AR:
 * 1. Добавить зависимость ARCore в build.gradle
 * 2. Реализовать ARCoreMapRenderer
 * 3. Использовать AR сессию для отображения POI в AR
 */
class ARCoreMapRenderer : MapRenderer {

    override fun renderPOIs(pois: List<POI>) {
        // TODO: Реализовать AR отрисовку POI через ARCore
        // - Создать AR anchor для каждого POI
        // - Разместить 3D модели или спрайты в AR пространстве
        // - Использовать GPS координаты для позиционирования
    }

    override fun renderPath(from: LatLng, to: LatLng) {
        // TODO: Реализовать AR путь
        // - Нарисовать линию в AR пространстве
        // - Добавить анимацию
    }

    override fun updateUserLocation(location: LatLng) {
        // TODO: Обновить AR камеру
        // - Синхронизировать с GPS
        // - Обновить позицию камеры в AR сцене
    }

    override fun centerOnPoint(location: LatLng, zoom: Float?) {
        // TODO: Центрировать AR камеру на точке
    }

    override fun clear() {
        // TODO: Очистить AR сцену
        // - Удалить все AR anchor'ы
    }

    override fun cleanup() {
        // TODO: Очистить AR ресурсы
        // - Остановить AR сессию
        // - Освободить память
    }
}

/**
 * 🏭 Фабрика для создания MapRenderer
 */
object MapRendererFactory {

    enum class RendererType {
        CANVAS_2D,
        AR_CORE
    }

    /**
     * Создает MapRenderer в зависимости от типа
     *
     * @param type Тип рендерера
     * @param mapView MapCanvasView (для Canvas2D режима)
     * @return Экземпляр MapRenderer
     */
    fun create(
        type: RendererType,
        mapView: com.example.victor_ai.ui.map.canvas.MapCanvasView? = null
    ): MapRenderer {
        return when (type) {
            RendererType.CANVAS_2D -> {
                requireNotNull(mapView) { "MapCanvasView required for Canvas2D renderer" }
                Canvas2DMapRenderer(mapView)
            }
            RendererType.AR_CORE -> {
                ARCoreMapRenderer()
            }
        }
    }
}
