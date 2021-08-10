package dev.saurabhmishra.searchwithpagination.sources.network.helper

import com.squareup.moshi.Moshi
import dev.saurabhmishra.searchwithpagination.sources.network.interceptor.FlickrInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration

object RetrofitHelper {
    private const val BASE_URL = "https://api.flickr.com/services/"

    fun createRetrofit(flickrInterceptor: FlickrInterceptor, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(flickrInterceptor = flickrInterceptor))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun createOkHttpClient(flickrInterceptor: FlickrInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(flickrInterceptor)
            .readTimeout(Duration.ofMinutes(1))
            .callTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .build()
    }
}