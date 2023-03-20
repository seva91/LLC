package com.example.llc.application

import com.example.llc.data.network.ServerPoint
import com.example.llc.domain.models.Point

sealed interface PointsState {
    data class InitContent(
        val count: Int = 0,
        val buttonLoading: Boolean = false
    ) : PointsState

    data class PointsContent(
        val points: List<Point>,
        val unitPoint: Int
    ) : PointsState
}

sealed interface PointsEvent {
    sealed interface Ui : PointsEvent {
        data class InputCount(val count: String) : Ui
        object GoClick : Ui
        object ZoomIn : Ui
        object ZoomOut : Ui
        object SaveClick : Ui
    }

    sealed interface Domain : PointsEvent {
        data class PointsLoadedSuccess(val points: List<ServerPoint>) : Domain
        data class PointsLoadedFailure(val error: Throwable) : Domain
    }
}

sealed interface PointsEffect {
    sealed interface Ui : PointsEffect {
        object ShowErrorToast : Ui
        object SaveFile : Ui
    }

    sealed interface Domain : PointsEffect {
        data class LoadPoints(val count: Int) : Domain
    }
}