package com.example.llc.domain.usecase

import com.example.llc.core.Middleware
import com.example.llc.application.PointsEffect
import com.example.llc.application.PointsEvent
import com.example.llc.data.network.ServerApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class PointsUseCase(
    private val serverApi: ServerApi
) : Middleware<PointsEffect, PointsEvent> {
    override suspend fun runCommand(
        effect: PointsEffect
    ): Flow<PointsEvent> {
        val arg = effect as? PointsEffect.Domain.LoadPoints ?: return emptyFlow()
        return flow<PointsEvent.Domain> {
            if (arg.count !in 1..100) throw java.lang.IllegalArgumentException()
            delay(2000) // имитируем выполнение запроса
            val points = serverApi.getPoints(arg.count).points
            emit(PointsEvent.Domain.PointsLoadedSuccess(points))
        }.catch {
            if (it is CancellationException) throw it
            emit(PointsEvent.Domain.PointsLoadedFailure(it))
        }
    }
}