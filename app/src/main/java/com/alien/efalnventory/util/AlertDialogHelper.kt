package com.alien.efaInventory.util

import android.app.AlertDialog
import android.content.Context
import com.alien.efaInventory.R

class AlertDialogHelper(private val context: Context) {

    fun showAlertDialog(title: String) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val checkAlertDialog: AlertDialog = builder.create()
        checkAlertDialog.setCancelable(true)
        builder.setTitle(title)
        builder.setNegativeButton(context.getString(R.string.確認)) { _, _ ->
            checkAlertDialog.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }
}
