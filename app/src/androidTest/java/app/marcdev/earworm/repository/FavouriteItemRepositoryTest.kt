package app.marcdev.earworm.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.DAO
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.SONG
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FavouriteItemRepositoryTest {

  private var database: AppDatabase? = null
  private var repository: FavouriteItemRepository? = null
  private var dao: DAO? = null

  // Default values
  private val testName = "Test Song Name"
  private val testAlbum = "Test Album Name"
  private val testArtist = "Test Artist Name"
  private val testGenre = "Test Genre Name"
  private val testImageName = "testimagename.jpg"
  private val testDay = 1
  private val testMonth = 1
  private val testYear = 2018
  private val testType: Int = SONG

  @Before
  fun setUp() {
    database =
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, AppDatabase::class.java)
          .allowMainThreadQueries().build()

    dao = database!!.dao()
    repository = FavouriteItemRepositoryImpl(dao!!)
  }

  @After
  fun tearDown() {
    database!!.close()
  }

  private fun createTestItem(): FavouriteItem {
    return FavouriteItem(testName, testAlbum, testArtist, testGenre, testDay, testMonth, testYear, testType, testImageName)
  }

  @Test
  fun insertMultipleSongs_getAllItems() = runBlocking {
    val testItem1 = createTestItem()
    val testItem2 = createTestItem()

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenOneInserted.size)
  }

  @Test
  fun insertOneSong_getItemById_deleteSong() = runBlocking {
    val testId = 1

    val testItem = createTestItem()
    testItem.id = testId

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem)

    val returnedItemsWhenOneInserted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(1, returnedItemsWhenOneInserted.size)

    repository!!.deleteItem(testId)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = repository!!.getItem(testId)
    Assert.assertEquals(0, returnedItemsWhenOneDeleted.size)
  }

  @Test
  fun insertTwoSongs_deleteOneSong() = runBlocking {
    val testId1 = 1
    val testId2 = 2

    val testItem1 = createTestItem()
    testItem1.id = testId1
    val testItem2 = createTestItem()
    testItem2.id = testId2

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedItemsWhenTwoInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedItemsWhenTwoInserted.size)

    repository!!.deleteItem(testId1)

    val returnedItemsWhenOneDeleted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(1, returnedItemsWhenOneDeleted.size)

    val returnedItemsWhenDeletedOneSearched: MutableList<FavouriteItem> = repository!!.getItem(testId1)
    Assert.assertEquals(0, returnedItemsWhenDeletedOneSearched.size)
  }

  @Test
  fun insertMultipleSongs_getOneById() = runBlocking {
    val testId1 = 1
    val testId2 = 2

    val testItem1 = createTestItem()
    testItem1.id = testId1
    val testItem2 = createTestItem()
    testItem2.id = testId2

    val returnedItemsWhenNothingInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(0, returnedItemsWhenNothingInserted.size)

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)

    val returnedAllItemsWhenTwoInserted: MutableList<FavouriteItem> = repository!!.getAllItems()
    Assert.assertEquals(2, returnedAllItemsWhenTwoInserted.size)

    val returnedItemsWhenSearchedById1: MutableList<FavouriteItem> = repository!!.getItem(testId1)
    Assert.assertEquals(1, returnedItemsWhenSearchedById1.size)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEach() = runBlocking {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)
    repository!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = repository!!.countUsesOfImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = repository!!.countUsesOfImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEachAndDelete() = runBlocking {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"
    val testId1 = 1
    val testId2 = 2
    val testId3 = 3

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1
    testItem1.id = testId1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2
    testItem2.id = testId2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2
    testItem3.id = testId3

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)
    repository!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = repository!!.countUsesOfImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = repository!!.countUsesOfImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)

    repository!!.deleteItem(testId2)

    val returnedValueWhenSearchedForTestImage1AfterDelete: Int = repository!!.countUsesOfImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1AfterDelete)

    val returnedValueWhenSearchedForTestImage2AfterDelete: Int = repository!!.countUsesOfImage(testImage2)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage2AfterDelete)
  }

  @Test
  fun insertMultipleItemsUsingDifferentImages_countEachAndDeleteOneCompletely() = runBlocking {
    val testImage1 = "testImage1.jpg"
    val testImage2 = "testImage2.jpg"
    val testId1 = 1
    val testId2 = 2
    val testId3 = 3

    val testItem1 = createTestItem()
    testItem1.imageName = testImage1
    testItem1.id = testId1

    val testItem2 = createTestItem()
    testItem2.imageName = testImage2
    testItem2.id = testId2

    val testItem3 = createTestItem()
    testItem3.imageName = testImage2
    testItem3.id = testId3

    repository!!.insertOrUpdateItem(testItem1)
    repository!!.insertOrUpdateItem(testItem2)
    repository!!.insertOrUpdateItem(testItem3)

    val returnedValueWhenSearchedForTestImage1: Int = repository!!.countUsesOfImage(testImage1)
    Assert.assertEquals(1, returnedValueWhenSearchedForTestImage1)

    val returnedValueWhenSearchedForTestImage2: Int = repository!!.countUsesOfImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2)

    repository!!.deleteItem(testId1)

    val returnedValueWhenSearchedForTestImage1AfterDelete: Int = repository!!.countUsesOfImage(testImage1)
    Assert.assertEquals(0, returnedValueWhenSearchedForTestImage1AfterDelete)

    val returnedValueWhenSearchedForTestImage2AfterDelete: Int = repository!!.countUsesOfImage(testImage2)
    Assert.assertEquals(2, returnedValueWhenSearchedForTestImage2AfterDelete)
  }
}