package dev.saurabhmishra.searchwithpagination.injection

import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.repo.SearchRepoImpl
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkMediator
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.helper.RetrofitHelper
import dev.saurabhmishra.searchwithpagination.ui.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { SearchRepoImpl(get()) } bind SearchRepo::class
    factory {SearchLocalSourceImpl()} bind SearchLocalSource::class
    factory { SearchNetworkMediator(get()) }
}

val viewModelModule = module {
    viewModel{ HomeScreenViewModel() }
}

val networkModule = module {
    single { RetrofitHelper.createRetrofit() }
    single {
        val retrofit = get<Retrofit>()
        retrofit.create(Api::class.java)
    }
}

