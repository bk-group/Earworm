package com.marcdonald.earworm.settingsscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.base.EarwormActivity

class SettingsActivity : EarwormActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    bindViews()
    setFragment(SettingsFragment(), supportFragmentManager, R.id.scroll_settings)
  }

  private fun bindViews() {
    val backButton = findViewById<ImageView>(R.id.img_backFromSettings)
    backButton.setOnClickListener(backOnClickListener)
  }

  private val backOnClickListener = View.OnClickListener {
    this.finish()
  }
}