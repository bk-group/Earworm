package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentView {

  fun displayNoEntriesWarning(display: Boolean)

  fun displayAddedToast()

  fun displayItemDeletedToast()

  fun updateRecycler(items: List<FavouriteItem>)

  fun displayProgress(isVisible: Boolean)

  fun displayEditItemSheet(itemId: Int)

  fun displayEmptySearchToast()
}