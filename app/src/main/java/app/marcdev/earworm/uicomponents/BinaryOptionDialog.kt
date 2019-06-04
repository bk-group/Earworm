package app.marcdev.earworm.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.base.EarwormDialogFragment
import com.google.android.material.button.MaterialButton

class BinaryOptionDialog : EarwormDialogFragment() {

  // UI Components
  private lateinit var negativeButton: MaterialButton
  private lateinit var positiveButton: MaterialButton
  private lateinit var titleDisplay: TextView
  private lateinit var messageDisplay: TextView

  // To set
  private var negativeButtonText = ""
  private var positiveButtonText = ""
  private var titleText = ""
  private var messageText = ""
  private var negativeButtonOnClick: () -> Unit = {}
  private var dismissAfterNegativeClick = true
  private var positiveButtonOnClick: () -> Unit = {}
  private var dismissAfterPositiveClick = true
  private var isTitleVisible = true
  private var isMessageVisible = true

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_binary_option, container, false)
    bindViews(view)
    setContent()
    return view
  }

  private fun bindViews(view: View) {
    negativeButton = view.findViewById(R.id.btn_binary_dialog_negative)
    positiveButton = view.findViewById(R.id.btn_binary_dialog_positive)

    titleDisplay = view.findViewById(R.id.txt_binary_dialog_title)
    messageDisplay = view.findViewById(R.id.txt_binary_dialog_message)
  }

  private fun setContent() {
    if(titleText.isNotBlank()) {
      titleDisplay.text = titleText
    }

    if(messageText.isNotBlank()) {
      messageDisplay.text = messageText
    }

    if(negativeButtonText.isNotBlank()) {
      negativeButton.text = negativeButtonText
    }

    if(positiveButtonText.isNotBlank()) {
      positiveButton.text = positiveButtonText
    }

    negativeButton.setOnClickListener {
      negativeButtonOnClick()
      if(dismissAfterNegativeClick)
        dismiss()
    }

    positiveButton.setOnClickListener {
      positiveButtonOnClick()
      if(dismissAfterPositiveClick)
        dismiss()
    }

    if(isTitleVisible)
      titleDisplay.visibility = View.VISIBLE
    else
      titleDisplay.visibility = View.GONE

    if(isMessageVisible)
      messageDisplay.visibility = View.VISIBLE
    else
      messageDisplay.visibility = View.GONE
  }

  private fun setNegativeButton(text: String, onClick: () -> Unit, dismissAfter: Boolean) {
    negativeButtonText = text
    negativeButtonOnClick = onClick
    dismissAfterNegativeClick = dismissAfter
  }

  private fun setPositiveButton(text: String, onClick: () -> Unit, dismissAfter: Boolean) {
    positiveButtonText = text
    positiveButtonOnClick = onClick
    dismissAfterPositiveClick = dismissAfter
  }

  private fun setTitle(text: String) {
    if(view != null) {
      titleDisplay.text = text
    } else {
      titleText = text
    }
  }

  private fun setMessage(text: String) {
    if(view != null) {
      messageDisplay.text = text
    } else {
      messageText = text
    }
  }

  private fun setTitleVisibility(isVisible: Boolean) {
    isTitleVisible = isVisible
  }

  private fun setMessageVisibility(isVisible: Boolean) {
    isMessageVisible = isVisible
  }

  class Builder {
    // To set
    private var negativeButtonText = ""
    private var positiveButtonText = ""
    private var titleText = ""
    private var messageText = ""
    private var negativeButtonOnClick: () -> Unit = {}
    private var dismissAfterNegativeClick = true
    private var positiveButtonOnClick: () -> Unit = {}
    private var dismissAfterPositiveClick = true
    private var isTitleVisible = true
    private var isMessageVisible = true

    fun setTitle(text: String): Builder {
      titleText = text
      return this
    }

    fun setMessage(text: String): Builder {
      messageText = text
      return this
    }

    fun setNegativeButton(text: String, onClick: () -> Unit): Builder {
      negativeButtonText = text
      negativeButtonOnClick = onClick
      return this
    }

    fun setPositiveButton(text: String, onClick: () -> Unit): Builder {
      positiveButtonText = text
      positiveButtonOnClick = onClick
      return this
    }

    fun setNegativeButton(text: String, onClick: () -> Unit, dismissAfter: Boolean): Builder {
      negativeButtonText = text
      negativeButtonOnClick = onClick
      dismissAfterNegativeClick = dismissAfter
      return this
    }

    fun setPositiveButton(text: String, onClick: () -> Unit, dismissAfter: Boolean): Builder {
      positiveButtonText = text
      positiveButtonOnClick = onClick
      dismissAfterPositiveClick = dismissAfter
      return this
    }

    fun setTitleVisible(isVisible: Boolean): Builder {
      isTitleVisible = isVisible
      return this
    }

    fun setMessageVisible(isVisible: Boolean): Builder {
      isMessageVisible = isVisible
      return this
    }

    fun build(): BinaryOptionDialog {
      val dialog = BinaryOptionDialog()
      dialog.setTitle(titleText)
      dialog.setMessage(messageText)
      dialog.setNegativeButton(negativeButtonText, negativeButtonOnClick, dismissAfterNegativeClick)
      dialog.setPositiveButton(positiveButtonText, positiveButtonOnClick, dismissAfterPositiveClick)
      dialog.setTitleVisibility(isTitleVisible)
      dialog.setMessageVisibility(isMessageVisible)
      return dialog
    }
  }
}