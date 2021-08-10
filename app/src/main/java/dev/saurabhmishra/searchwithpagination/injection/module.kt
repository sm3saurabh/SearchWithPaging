package dev.saurabhmishra.searchwithpagination.injection

import androidx.room.Room
import dev.saurabhmishra.searchwithpagination.mediator.SearchRemoteMediator
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.repo.SearchRepoImpl
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.local.dao.PhotoDao
import dev.saurabhmishra.searchwithpagination.sources.local.database.SearchWithPaginationDB
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSourceImpl
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.helper.RetrofitHelper
import dev.saurabhmishra.searchwithpagination.sources.network.interceptor.FlickrInterceptor
import dev.saurabhmishra.searchwithpagination.ui.home.HomeScreenViewModel
import dev.saurabhmishra.searchwithpagination.utils.CoroutineContextProvider
import dev.saurabhmishra.searchwithpagination.utils.DefaultCoroutineContextProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    single { DefaultCoroutineContextProvider() } bind CoroutineContextProvider::class
}

val viewModelModule = module {
    viewModel { HomeScreenViewModel(get()) }
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

    single { SearchRemoteMediator(get()) } bind SearchRemoteMediator::class
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), SearchWithPaginationDB::class.java, androidContext().packageName)
            .fallbackToDestructiveMigration()
            .build()
    } bind SearchWithPaginationDB::class

    single {
        val db = get<SearchWithPaginationDB>()
        db.photoDao()
    } bind PhotoDao::class
}

val localSourceModule = module {
    factory { SearchLocalSourceImpl(get(), get()) } bind SearchLocalSource::class
}

val networkSourceModule = module {
    factory { SearchNetworkSourceImpl(get(), get()) } bind SearchNetworkSource::class
}

val repoModule = module {
    single { SearchRepoImpl(get(), get()) } bind SearchRepo::class
}

val appWideModules = listOf(
    coreModule,
    viewModelModule,
    networkModule,
    databaseModule,
    localSourceModule,
    networkSourceModule,
    repoModule
)

