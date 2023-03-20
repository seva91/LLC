package com.example.llc

import android.app.Application
import com.example.llc.data.network.ServerApi
import com.example.llc.data.network.ServerApiImpl
import com.example.llc.application.PointsConverter
import com.example.llc.application.PointsViewModel
import com.example.llc.domain.usecase.PointsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    factory<ServerApi> { ServerApiImpl() }
    factory { PointsConverter() }
    factory { PointsUseCase(get()) }
    viewModel { PointsViewModel(get(), get()) }
}