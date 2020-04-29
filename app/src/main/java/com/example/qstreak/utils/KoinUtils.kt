package com.example.qstreak.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.qstreak.BuildConfig
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.db.UserRepository
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.QstreakApiSignupService
import com.example.qstreak.ui.*
import com.example.qstreak.viewmodels.*
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val USER_PREFS = "user_prefs"
const val UID = "uid"

fun Application.initKoin() {

    startKoin {
        androidContext(this@initKoin)
        modules(
            listOf(
                sharedPrefsModule(),
                networkModule(),
                databaseModule(),
                repositoryModule(),
                scopeModules()
            )
        )
    }
}

private fun sharedPrefsModule() = module {
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            USER_PREFS,
            Context.MODE_PRIVATE
        )
    }
    single<SharedPreferences.Editor> {
        androidApplication().getSharedPreferences(
            USER_PREFS,
            Context.MODE_PRIVATE
        ).edit()
    }
}

private fun networkModule() = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { QstreakApiSignupService.getQstreakApiSignupService(get()) }
    single { QstreakApiService.getQstreakApiService() }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
}

private fun databaseModule() = module {
    single { QstreakDatabase.getInstance(androidApplication()).activitiesDao() }
    single { QstreakDatabase.getInstance(androidApplication()).submissionDao() }
    single { QstreakDatabase.getInstance(androidApplication()).submissionWithActivityDao() }
    single { QstreakDatabase.getInstance(androidApplication()).userDao() }
}

private fun repositoryModule() = module {
    single { UserRepository(get(), get(), get(), Dispatchers.IO) }
    single { SubmissionRepository(get(), get(), get(), get(), Dispatchers.IO) }
    single { ActivitiesRepository(get(), get()) }
}

private fun scopeModules() = module {
    scope(named<SplashActivity>()) {
        viewModel { SplashViewModel(get()) }
    }
    scope(named<OnboardingSignupFragment>()) {
        viewModel { OnboardingViewModel(get(), get()) }
    }
    scope(named<AddEditSubmissionFragment>()) {
        viewModel { AddSubmissionViewModel(get(), get(), get()) }
    }
    scope(named<DashboardFragment>()) {
        viewModel { DashboardViewModel() }
    }
    scope(named<MainActivity>()) {
        viewModel { SubmissionsViewModel(get(), get()) }
    }
}
