package com.example.famouspeople.di.modules

import android.content.Context
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.util.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule(private val context: Context) {


    @Provides
    @Singleton
    fun context(): Context {
        return context
    }

    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideLoggingInterceptor(): HttpLoggingInterceptor{
            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY
            return  logging
        }




        @Provides
        @Singleton
        @JvmStatic
        fun providesOkHttpClient(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            val client = OkHttpClient.Builder()
            client
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
            return client.build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun providesConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun providesRetrofit(
            gsonConverterFactory: GsonConverterFactory,
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideWebService(retrofit: Retrofit): WebService =
            retrofit.create(WebService::class.java)
    }


}