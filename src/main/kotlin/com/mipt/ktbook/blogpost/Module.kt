package com.mipt.ktbook.blogpost

import com.mipt.ktbook.blogpost.storage.memory.InMemoryStorage
import com.mipt.ktbook.blogpost.storage.Storage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val blogpostModule = module {
    singleOf(::InMemoryStorage) bind Storage::class
}
