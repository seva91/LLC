package com.example.llc.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event, Effect>(
    middleware: List<Middleware<Effect, Event>>,
    middlewareDispatcher: CoroutineDispatcher = Dispatchers.IO,
    eventDispatcher: CoroutineDispatcher = Dispatchers.Default,
    initState: State
) : ViewModel() {

    /**
     * Чистая функция
     * Всегда при одних и тех же входных данных выдает одни и те же выходные данные
     * Не имеет побочных эффектов
     * Легко тестируется
     * Принимает текущее состояние системы и асинхронное событие(от UI или от сервера)
     * Отдает новое состояние системы (отрисовывается на UI) и
     * команду UI (например тост или навигация) или серверу
     */
    abstract fun reduce(state: State, event: Event): Pair<State, List<Effect>>

    private val mutableState: MutableStateFlow<State> = MutableStateFlow(initState)
    val state: StateFlow<State> = mutableState

    private val event: MutableSharedFlow<Event> = MutableSharedFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch { this@BaseViewModel.event.emit(event) }
    }

    private val effectChannel: Channel<Effect> = Channel()
    val effect = effectChannel.consumeAsFlow().shareIn(viewModelScope, SharingStarted.Lazily)

    init {
        viewModelScope.launch(eventDispatcher) {
            event.collect { event ->
                val (newState, effects) = reduce(state.value, event)
                mutableState.value = newState
                effects.forEach { effectChannel.send(it) }
            }
        }

        viewModelScope.launch {
            effect.collect { effect ->
                middleware.forEach { middleware ->
                    val command = middleware.runCommand(effect)
                    launch(middlewareDispatcher) {
                        command.collect { e -> event.emit(e) }
                    }
                }
            }
        }
    }
}