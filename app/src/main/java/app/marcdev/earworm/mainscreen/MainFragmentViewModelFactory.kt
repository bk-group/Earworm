package app.marcdev.earworm.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.utils.FileUtils

class MainFragmentViewModelFactory(private val favouriteItemRepository: FavouriteItemRepository, private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainFragmentViewModel(favouriteItemRepository, fileUtils) as T
  }
}