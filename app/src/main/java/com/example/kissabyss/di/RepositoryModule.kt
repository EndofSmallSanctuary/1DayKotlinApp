package com.example.kissabyss.di

import com.example.kissabyss.repositories.EditImageRepository
import com.example.kissabyss.repositories.impls.EditImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
}