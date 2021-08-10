package dev.saurabhmishra.searchwithpagination.sources.network.helper

import dev.saurabhmishra.searchwithpagination.sources.network.interceptor.FlickrInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.time.Duration

object RetrofitHelper {
    private const val BASE_URL = "https://api.flickr.com/services/rest/"

    fun createRetrofit(flickrInterceptor: FlickrInterceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(flickrInterceptor = flickrInterceptor))
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