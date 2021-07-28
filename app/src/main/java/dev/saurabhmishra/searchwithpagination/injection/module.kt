package dev.saurabhmishra.searchwithpagination.injection

import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.repo.SearchRepoImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { SearchRepoImpl() } bind SearchRepo::class
}