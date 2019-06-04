package app.marcdev.earworm.settingsscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.DARK_THEME
import app.marcdev.earworm.internal.base.EarwormActivity
import app.marcdev.earworm.utils.changeColorOfImageViewDrawable
import app.marcdev.earworm.utils.getTheme

class LicensesActivity : EarwormActivity() {

  private var isDarkMode: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    /* Theme changes must be done before super.onCreate otherwise it will be overridden with the value
      in the manifest */
    isDarkMode = if(getTheme(applicationContext) == DARK_THEME) {
      setTheme(R.style.Earworm_DarkTheme)
      true
    } else {
      setTheme(R.style.Earworm_LightTheme)
      false
    }

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_licenses)

    bindViews()
  }

  private fun bindViews() {
    val backButton = findViewById<ImageView>(R.id.img_backFromSettings)
    backButton.setOnClickListener { finish() }
    if(isDarkMode) {
      changeColorOfImageViewDrawable(applicationContext, backButton, false)
    }

    val toolbarTitle = findViewById<TextView>(R.id.txt_settingsToolbarTitle)
    toolbarTitle.text = resources.getString(R.string.licenses)

    val glideCard = findViewById<CardView>(R.id.card_glide)
    glideCard.setOnClickListener { launchUrl("https://github.com/bumptech/glide") }

    val timberCard = findViewById<CardView>(R.id.card_timber)
    timberCard.setOnClickListener { launchUrl("https://github.com/JakeWharton/timber") }

    val materialIconsCard = findViewById<CardView>(R.id.card_material_design_icons)
    materialIconsCard.setOnClickListener { launchUrl("https://github.com/google/material-design-icons") }

    val materialComponentsCard = findViewById<CardView>(R.id.card_material_design_components)
    materialComponentsCard.setOnClickListener { launchUrl("https://github.com/material-components/material-components-android") }

    val filePickerCard = findViewById<CardView>(R.id.card_android_file_picker)
    filePickerCard.setOnClickListener { launchUrl("https://github.com/DroidNinja/Android-FilePicker") }

    val kodeinCard = findViewById<CardView>(R.id.card_kodein)
    kodeinCard.setOnClickListener { launchUrl("https://github.com/Kodein-Framework/Kodein-DI") }
  }

  private fun launchUrl(url: String) {
    val uriUrl = Uri.parse(url)
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }
}
