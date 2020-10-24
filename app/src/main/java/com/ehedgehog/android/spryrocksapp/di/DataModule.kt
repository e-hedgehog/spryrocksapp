package com.ehedgehog.android.spryrocksapp.di

import com.ehedgehog.android.spryrocksapp.BuildConfig
import com.ehedgehog.android.spryrocksapp.network.AuthInterceptor
import com.ehedgehog.android.spryrocksapp.network.TrelloApiService
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import com.ehedgehog.android.spryrocksapp.screens.TaskTimerManager
import com.ehedgehog.android.spryrocksapp.screens.TimerUseCase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): TrelloApiService {
        return retrofit.create(TrelloApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(client)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRealmInstance(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseManager(realm: Realm): DatabaseManager {
        return DatabaseManager(realm)
    }

    @Provides
    fun provideTimerUseCase(): TimerUseCase {
        return TimerUseCase()
    }

    @Provides
    fun provideTaskTimerManager(timerUseCase: TimerUseCase, databaseManager: DatabaseManager): TaskTimerManager {
        return TaskTimerManager(timerUseCase, databaseManager)
    }

}