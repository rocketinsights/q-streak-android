package com.example.qstreak.utils

import android.app.Application
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.db.UserRepository
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.QstreakApiSignupService
import com.example.qstreak.ui.OnboardingSignupFragment
import com.example.qstreak.ui.SubmissionsActivity
import com.example.qstreak.viewmodels.OnboardingViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initKoin() {

    startKoin {
        androidContext(this@initKoin)
        modules(
            listOf(
                networkModule(),
                databaseModule(),
                repositoryModule(),
                scopeModules(this@initKoin)
            )
        )
    }
}

private fun networkModule() = module {
    single { QstreakApiSignupService.getQstreakApiSignupService() }
    single { QstreakApiService.getQstreakApiService() }
}

private fun databaseModule() = module {
    single { QstreakDatabase.getInstance(androidApplication()).activitiesDao() }
    single { QstreakDatabase.getInstance(androidApplication()).submissionDao() }
    single { QstreakDatabase.getInstance(androidApplication()).submissionWithActivityDao() }
    single { QstreakDatabase.getInstance(androidApplication()).userDao() }
}

private fun repositoryModule() = module {
    single { UserRepository(get()) }
    single { SubmissionRepository(get(), get()) }
    single { ActivitiesRepository(get()) }
}

private fun scopeModules(application: Application) = module {
    scope(named<SubmissionsActivity>()) {
        viewModel { SubmissionsViewModel(application, get(), get()) }
    }
    scope(named<OnboardingSignupFragment>()) {
        viewModel { OnboardingViewModel(get()) }
    }
}
