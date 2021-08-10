package dev.saurabhmishra.searchwithpagination.injection

import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.repo.SearchRepoImpl
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.helper.RetrofitHelper
import dev.saurabhmishra.searchwithpagination.sources.network.interceptor.FlickrInterceptor
import dev.saurabhmishra.searchwithpagination.ui.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { SearchRepoImpl(get(), get()) } bind SearchRepo::class
    factory {SearchLocalSourceImpl()} bind SearchLocalSource::class
}

val viewModelModule = module {
    viewModel{ HomeScreenViewModel(get()) }
}

val networkModule = module {
    single {
        FlickrInterceptor()
    }

    single { RetrofitHelper.createRetrofit(get()) }

    single {
        val retrofit = get<Retrofit>()
        retrofit.create(Api::class.java)
    } bind Api::class


}

