package com.mipt.ktbook

import com.mipt.ktbook.storage.memory.InMemoryStorage
import com.mipt.ktbook.storage.Storage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val blogpostModule = module {
    singleOf(::InMemoryStorage) bind Storage::class
}
