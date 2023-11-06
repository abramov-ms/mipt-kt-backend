package com.mipt.ktbook

import com.mipt.ktbook.storage.Storage
import com.mipt.ktbook.storage.impl.InMemoryStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val blogpostModule = module {
    singleOf(::InMemoryStorage) bind Storage::class
}
