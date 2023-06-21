package pt.brunocaiado.datalibrary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.brunocaiado.datalibrary.data.repository.BooksRepository
import pt.brunocaiado.datalibrary.data.repository.BooksRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun getBooksRepository(repo: BooksRepositoryImpl): BooksRepository



}