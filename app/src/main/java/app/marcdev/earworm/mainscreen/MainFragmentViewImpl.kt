package app.marcdev.earworm.mainscreen

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.mainscreen.additem.AddItemBottomSheet
import app.marcdev.earworm.mainscreen.additem.RecyclerUpdateView
import app.marcdev.earworm.mainscreen.mainrecycler.MainRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainFragmentViewImpl : Fragment(), MainFragmentView, RecyclerUpdateView {

  private lateinit var fab: FloatingActionButton
  private lateinit var noEntriesWarning: TextView
  private lateinit var progressBar: ProgressBar
  private lateinit var searchInput: EditText
  private lateinit var recyclerAdapter: MainRecyclerAdapter
  private lateinit var presenter: MainFragmentPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_mainscreen, container, false)
    presenter = MainFragmentPresenterImpl(this, activity!!.applicationContext)
    bindViews(view)
    setupRecycler(view)
    fillData()

    return view
  }

  private fun bindViews(view: View) {
    Timber.v("Log: bindViews: Started")
    this.fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabOnClickListener)

    this.noEntriesWarning = view.findViewById(R.id.txt_noEntries)
    this.progressBar = view.findViewById(R.id.prog_main)

    this.searchInput = view.findViewById(R.id.edt_filter_input)
    searchInput.setOnKeyListener(searchOnEnterListener)

    val searchButton: ImageView = view.findViewById(R.id.img_search)
    searchButton.setOnClickListener(searchOnClickListener)

    val filterButton: ImageView = view.findViewById(R.id.img_filter)
    filterButton.setOnClickListener(filterOnClickListener)

    val nestedScrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    nestedScrollView.setOnScrollChangeListener(scrollViewOnScrollChangeListener)
  }

  private val fabOnClickListener = View.OnClickListener {
    Timber.d("Log: Fab Clicked")
    val addDialog = AddItemBottomSheet()
    addDialog.bindRecyclerUpdateView(this)
    addDialog.show(fragmentManager, "Add Item Bottom Sheet Dialog")
  }

  private val searchOnEnterListener: View.OnKeyListener = View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
    testIfSubmitButtonClicked(keyEvent, keyCode)
  }

  private fun testIfSubmitButtonClicked(keyEvent: KeyEvent, keyCode: Int): Boolean {
    Timber.d("Log: testIfSubmitButtonClicked: Submit button clicked")
    if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
      presenter.search(searchInput.text.toString())
      return true
    }
    return false
  }

  private val searchOnClickListener = View.OnClickListener {
    Timber.d("Log: Search Clicked")
    presenter.search(searchInput.text.toString())
  }

  private val filterOnClickListener = View.OnClickListener {
    Timber.d("Log: Filter Clicked")
    // TODO
  }

  private var scrollViewOnScrollChangeListener = { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int -> hideFabOnScroll(scrollY, oldScrollY) }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int) {
    if(scrollY > oldScrollY) {
      fab.hide()
    } else {
      fab.show()
    }
  }

  private fun setupRecycler(view: View) {
    Timber.v("Log: setupRecycler: Started")
    val recycler: RecyclerView = view.findViewById(R.id.recycler_main)
    this.recyclerAdapter = MainRecyclerAdapter(context, presenter)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = LinearLayoutManager(context)
  }

  override fun fillData() {
    Timber.d("Log: fillData: Started")
    displayProgress(true)
    displayNoEntriesWarning(false)
    presenter.getAllItems()
  }

  override fun displayNoEntriesWarning(display: Boolean) {
    Timber.d("Log: displayNoEntriesWarning: Started")

    if(display) {
      Timber.d("Log: displayNoEntriesWarning: Displaying")
      noEntriesWarning.visibility = View.VISIBLE
    } else {
      Timber.d("Log: displayNoEntriesWarning: Hiding")
      noEntriesWarning.visibility = View.GONE
    }
  }

  override fun updateRecycler(items: List<FavouriteItem>) {
    Timber.d("Log: updateRecycler: Started")
    recyclerAdapter.updateItems(items)
  }

  override fun displayAddedToast() {
    Timber.d("Log: displayAddedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_added), Toast.LENGTH_SHORT).show()
  }

  override fun displayItemDeletedToast() {
    Timber.d("Log: displayItemDeletedToast: Started")
    Toast.makeText(activity, resources.getString(R.string.item_deleted), Toast.LENGTH_SHORT).show()
  }

  override fun displayProgress(isVisible: Boolean) {
    Timber.d("Log: displayProgress: Started with isVisible = $isVisible")
    if(isVisible) {
      progressBar.visibility = View.VISIBLE
    } else {
      progressBar.visibility = View.GONE
    }
  }

  override fun displayEditItemSheet(itemId: Int) {
    Timber.d("Log: displayEditItemSheet: Started")
    val addDialog = AddItemBottomSheet()
    addDialog.bindRecyclerUpdateView(this)

    val args = Bundle()
    args.putInt("item_id", itemId)
    addDialog.arguments = args

    addDialog.show(fragmentManager, "Add Item Bottom Sheet Dialog")
  }

  override fun displayEmptySearchToast() {
    Timber.d("Log: displayEmptySearchToast: Started")
    Toast.makeText(activity, resources.getString(R.string.empty_search), Toast.LENGTH_SHORT).show()
  }
}