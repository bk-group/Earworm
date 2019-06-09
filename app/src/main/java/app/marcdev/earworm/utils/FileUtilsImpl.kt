package app.marcdev.earworm.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import app.marcdev.earworm.internal.DATABASE_NAME
import app.marcdev.earworm.internal.PACKAGE
import timber.log.Timber
import java.io.File

class FileUtilsImpl(private val context: Context) : FileUtils {

  override fun deleteImage(imageName: String) {
    val filePath = artworkDirectory + imageName
    val file = File(filePath)
    if(file.exists()) {
      file.delete()
    } else {
      Timber.w("Log: deleteImage: File doesn't exist")
    }
  }

  override fun saveImage(file: File) {
    val toPath = artworkDirectory + file.name
    val toFile = File(toPath)
    if(toFile.compareTo(file) != 0) {
      try {
        file.copyTo(toFile, true)
      } catch(e: NoSuchFileException) {
        Timber.e("Log: saveImage: $e")
      }
    } else {
      Timber.d("Log: saveImage: No need to save as file already exists in storage")
    }
  }

  override fun getUriForFilePath(filePath: String): Uri {
    val file = File(filePath)
    return FileProvider.getUriForFile(context, "$PACKAGE.FileProvider", file)
  }

  override val artworkDirectory: String
    get() = context.filesDir.path + "/artwork/"

  override val localBackupDirectory: String
    get() = context.filesDir.path + "/backup/"

  override val databaseDirectory: String
    get() = context.getDatabasePath(DATABASE_NAME).path
}