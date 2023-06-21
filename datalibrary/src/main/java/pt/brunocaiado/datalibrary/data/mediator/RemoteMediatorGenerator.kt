package pt.brunocaiado.datalibrary.data.mediator

import pt.brunocaiado.datalibrary.data.localdata.BooksDao
import pt.brunocaiado.datalibrary.data.localdata.StoreDatabase
import pt.brunocaiado.datalibrary.data.remotedatasource.BookStoreRemoteDataSource
import javax.inject.Inject

class RemoteMediatorGenerator @Inject constructor(
    private val storeDatabase: StoreDatabase,
    private val booksDao: BooksDao,
    private val bookStoreRemoteDataSource: BookStoreRemoteDataSource
) {

    fun getBooksRemoteMediator(search: String): BooksRemoteMediator{
        return BooksRemoteMediator(
            search = search,
            bookStoreRemoteDataSource = bookStoreRemoteDataSource,
            storeDatabase = storeDatabase,
            booksDao = booksDao,
        )
    }

}