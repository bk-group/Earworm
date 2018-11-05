package app.marcdev.earworm.mainscreen.additem

import android.content.Context
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.repository.FavouriteItemRepository
import app.marcdev.earworm.repository.FavouriteItemRepositoryImpl
import app.marcdev.earworm.utils.getArtworkDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AddItemModelImpl(private val presenter: AddItemPresenter, private val context: Context) : AddItemModel {

  private var repository: FavouriteItemRepository

  init {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun addItemAsync(item: FavouriteItem) {
    Timber.d("Log: addItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      async(Dispatchers.IO) {
        repository.insertOrUpdateItem(item)
      }.await()

      presenter.addItemCallback()
    }
  }

  override fun getItemAsync(itemId: Int) {
    Timber.d("Log: getItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      val item = async(Dispatchers.IO) {
        repository.getItem(itemId)
      }.await()

      presenter.getItemCallback(item)
    }
  }

  override fun saveFileToAppStorage(file: File) {
    Timber.d("Log: saveFileToAppStorage: Started")
    val toPath = getArtworkDirectory(context) + file.name
    Timber.d("Log: saveFileToAppStorage: fileName = ${file.name}")
    val toFile = File(toPath)
    try {
      file.copyTo(toFile, true)
      presenter.saveFileToAppStorageCallback(null)
    } catch(e: NoSuchFileException) {
      presenter.saveFileToAppStorageCallback(e)
      Timber.e("Log: saveFileToAppStorage: $e")
    }
  }
}