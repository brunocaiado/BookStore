package pt.brunocaiado.datalibrary.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.brunocaiado.datalibrary.data.localdata.BooksDao
import pt.brunocaiado.datalibrary.data.localdata.StoreDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Provides
    @Singleton
    fun provideBookStoreDatabase(app: Application): StoreDatabase {
        return Room.databaseBuilder(
            app,
            StoreDatabase::class.java,
            "bookstore.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBooksDao(storeDatabase: StoreDatabase): BooksDao {
        return storeDatabase.booksDao
    }

}