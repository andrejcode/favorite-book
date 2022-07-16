package com.andrejmilanovic.favoritebook.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun showToast(context: Context, @StringRes stringId: Int) {
    Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT).show()
}