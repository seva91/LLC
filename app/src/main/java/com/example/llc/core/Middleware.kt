package com.example.llc.core

import kotlinx.coroutines.flow.Flow

interface Middleware<in Effect, out Event> {
    suspend fun runCommand(effect: Effect): Flow<Event>
}