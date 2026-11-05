package com.example.catalogone.di

import com.example.catalogone.data.repository.EnquiryRepositoryImpl
import com.example.catalogone.data.repository.ProductRepositoryImpl
import com.example.catalogone.domain.repository.EnquiryRepository
import com.example.catalogone.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindEnquiryRepository(
        impl: EnquiryRepositoryImpl
    ): EnquiryRepository
}
