package com.mrtvrgn.practice.movieworld.injection

import com.mrtvrgn.practice.movieworld.network.interceptor.AuthInterceptor
import com.mrtvrgn.practice.movieworld.network.service.MovieService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideMoshi(): MoshiConverterFactory = MoshiConverterFactory.create(
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    )

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor(AuthInterceptor())
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideMovieService(
        client: OkHttpClient,
        moshi: MoshiConverterFactory,
    ): MovieService = createService(client, "https://api.themoviedb.org/3/", moshi)


    private inline fun <reified T> createService(
        client: OkHttpClient,
        baseUrl: String,
        moshi: MoshiConverterFactory,
    ): T {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(moshi)
            .build()

        return retrofit.create(T::class.java)
    }
}