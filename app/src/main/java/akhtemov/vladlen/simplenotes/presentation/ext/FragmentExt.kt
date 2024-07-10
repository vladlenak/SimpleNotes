package akhtemov.vladlen.simplenotes.presentation.ext

import android.widget.EditText
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard(editText: EditText) {
    view?.let { activity?.showKeyboard(editText) }
}