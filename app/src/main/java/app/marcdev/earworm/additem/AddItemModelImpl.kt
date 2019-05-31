package app.marcdev.earworm.additem

import android.content.Context
import app.marcdev.earworm.data.database.ProductionAppDatabase
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.data.repository.FavouriteItemRepositoryImpl
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
    val db: ProductionAppDatabase = ProductionAppDatabase.invoke(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun addItemAsync(item: FavouriteItem) {
    Timber.d("Log: addItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      async(Dispatchers.IO) {
        repository.addItem(item)
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
    if(toFile.compareTo(file) != 0) {
      Timber.d("Log: saveFileToAppStorage: External file, saving")
      try {
        file.copyTo(toFile, true)
        presenter.saveFileToAppStorageCallback(file.name, null)
      } catch(e: NoSuchFileException) {
        presenter.saveFileToAppStorageCallback("", e)
        Timber.e("Log: saveFileToAppStorage: $e")
      }
    } else {
      Timber.d("Log: saveFileToAppStorage: Internal file of same path, not saving")
      presenter.saveFileToAppStorageCallback(file.name, null)
    }
  }

  override fun countUsesOfImage(filePath: String) {
    Timber.d("Log: countUsesOfImage: Started with filePath = $filePath")
    val file = File(filePath)
    val fileName = file.name

    GlobalScope.launch(Dispatchers.Main) {
      val uses = async(Dispatchers.IO) {
        repository.countUsesOfImage(fileName)
      }.await()

      presenter.countUsesOfImageCallback(filePath, uses)
    }
  }

  override fun deleteImage(filePath: String) {
    Timber.d("Log: deleteImage: Started with filePath = $filePath")

    val file = File(filePath)
    if(file.exists()) {
      Timber.d("Log: deleteImage: File exists, deleting")
      val deletionStatus = file.delete()
      Timber.d("Log: deleteImage: Deletion: $deletionStatus")
    } else {
      Timber.w("Log: deleteImage: File doesn't exist")
    }
  }
}