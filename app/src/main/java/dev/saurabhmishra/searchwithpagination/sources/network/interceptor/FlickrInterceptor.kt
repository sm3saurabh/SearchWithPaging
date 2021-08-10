package dev.saurabhmishra.searchwithpagination.sources.network.interceptor

import dev.saurabhmishra.searchwithpagination.utils.AppConstants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class FlickrInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newUrl = request.url.newBuilder()
            .addDefaultParameters()
            .build()

        val newRequest = request.newBuilder().url(newUrl).build()

        return chain.proceed(request = newRequest)
    }

    private fun HttpUrl.Builder.addDefaultParameters() = apply {
        DefaultParameters.values()
            .forEach { param ->
                addQueryParameter(
                    param.key, param.value
                )
            }
    }

    private enum class DefaultParameters(val key: String, val value: String) {
        // method -> flickr.photos.search does not work, switching to getRecent instead
        Method("method", "flickr.photos.getRecent"),
        ApiKey("api_key", "062a6c0c49e4de1d78497d13a7dbb360"),
        Format("format", "json"),
        NoJsonCallback("nojsoncallback", "1"),
        PerPage("per_page", AppConstants.OBJECTS_PER_PAGE.toString())
    }
}