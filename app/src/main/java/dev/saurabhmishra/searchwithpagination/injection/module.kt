package dev.saurabhmishra.searchwithpagination.injection

import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.repo.SearchRepoImpl
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.helper.RetrofitHelper
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { SearchRepoImpl(get()) } bind SearchRepo::class
    factory {SearchLocalSourceImpl()} bind SearchLocalSource::class
    factory { SearchNetworkSourceImpl() } bind SearchNetworkSource::class
}

val viewModelModule = module {  }

val networkModule = module {
    single { RetrofitHelper.createRetrofit() }
    single {
        val retrofit = get<Retrofit>()
        retrofit.create(Api::class.java)
    }
}

