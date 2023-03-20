package com.example.llc.application

import com.example.llc.core.BaseViewModel
import com.example.llc.data.network.ServerPoint
import com.example.llc.domain.models.Point
import com.example.llc.domain.usecase.PointsUseCase
import java.lang.Integer.max

class PointsViewModel(
    private val pointsConverter: PointsConverter,
    pointsUseCase: PointsUseCase,
) : BaseViewModel<PointsState, PointsEvent, PointsEffect>(
    middleware = listOf(pointsUseCase),
    initState = PointsState.InitContent()
) {
    override fun reduce(
        state: PointsState,
        event: PointsEvent
    ): Pair<PointsState, List<PointsEffect>> {
        return when (event) {
            is PointsEvent.Domain.PointsLoadedSuccess ->
                loadedSuccess(event.points)
            is PointsEvent.Domain.PointsLoadedFailure ->
                loadedFailure(state, event.error)
            is PointsEvent.Ui.InputCount ->
                inputCount(state, event.count)
            is PointsEvent.Ui.GoClick ->
                goClick(state)
            is PointsEvent.Ui.ZoomIn ->
                zoomIn(state)
            is PointsEvent.Ui.ZoomOut ->
                zoomOut(state)
            is PointsEvent.Ui.SaveClick ->
                saveClick(state)
        }
    }

    private fun loadedSuccess(
        points: List<ServerPoint>
    ): Pair<PointsState, List<PointsEffect>> {
        return PointsState.PointsContent(
            points = points.map { pointsConverter.convert(it) },
            unitPoint = 15
        ) to emptyList()
    }

    private fun loadedFailure(
        state: PointsState,
        error: Throwable
    ): Pair<PointsState, List<PointsEffect>> {
        val initState = state as? PointsState.InitContent ?: return state to emptyList()
        return initState.copy(buttonLoading = false) to
                listOf(PointsEffect.Ui.ShowErrorToast)
    }

    private fun inputCount(
        state: PointsState,
        rawCount: String
    ): Pair<PointsState, List<PointsEffect>> {
        val initState = state as? PointsState.InitContent ?: return state to emptyList()
        val count = if (rawCount.isBlank()) {
            0
        } else {
            rawCount.filter { it.isDigit() }.toInt()
        }
        return initState.copy(count = count) to emptyList()
    }

    private fun goClick(
        state: PointsState
    ): Pair<PointsState, List<PointsEffect>> {
        val initState = state as? PointsState.InitContent
        if (initState == null || initState.buttonLoading) return state to emptyList()
        val count = initState.count
        return initState.copy(buttonLoading = true) to
                listOf(PointsEffect.Domain.LoadPoints(count))
    }

    private fun zoomIn(
        state: PointsState
    ): Pair<PointsState, List<PointsEffect>> {
        val pointsContent = state as? PointsState.PointsContent ?: return state to emptyList()
        val unitPoint = pointsContent.unitPoint + 1
        return pointsContent.copy(unitPoint = unitPoint) to emptyList()
    }

    private fun zoomOut(
        state: PointsState
    ): Pair<PointsState, List<PointsEffect>> {
        val pointsContent = state as? PointsState.PointsContent ?: return state to emptyList()
        val unitPoint = max(pointsContent.unitPoint - 1, 1)
        return pointsContent.copy(unitPoint = unitPoint) to emptyList()
    }

    private fun saveClick(
        state: PointsState
    ): Pair<PointsState, List<PointsEffect>> {
        val pointsContent = state as? PointsState.PointsContent ?: return state to emptyList()
        return pointsContent to listOf(PointsEffect.Ui.SaveFile)
    }
}

class PointsConverter {
    fun convert(serverPoint: ServerPoint): Point {
        return Point(serverPoint.x, serverPoint.y)
    }
}