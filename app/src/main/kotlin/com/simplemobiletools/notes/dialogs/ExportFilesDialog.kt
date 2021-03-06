package com.simplemobiletools.notes.dialogs

import android.support.v7.app.AlertDialog
import com.simplemobiletools.commons.dialogs.FilePickerDialog
import com.simplemobiletools.commons.extensions.humanizePath
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.commons.extensions.showKeyboard
import com.simplemobiletools.commons.extensions.value
import com.simplemobiletools.notes.R
import com.simplemobiletools.notes.activities.SimpleActivity
import com.simplemobiletools.notes.extensions.config
import com.simplemobiletools.notes.models.Note
import kotlinx.android.synthetic.main.dialog_export_files.view.*

class ExportFilesDialog(val activity: SimpleActivity, val notes: ArrayList<Note>, val callback: (parent: String, extension: String) -> Unit) {
    init {
        var realPath = activity.config.lastUsedSavePath
        val view = activity.layoutInflater.inflate(R.layout.dialog_export_files, null).apply {
            folder_path.text = activity.humanizePath(realPath)

            file_extension.setText(activity.config.lastUsedExtension)
            folder_path.setOnClickListener {
                FilePickerDialog(activity, realPath, false, false, true) {
                    folder_path.text = activity.humanizePath(it)
                    realPath = it
                }
            }
        }

        AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
                    activity.setupDialogStuff(view, this, R.string.export_as_file) {
                        showKeyboard(view.file_extension)
                        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            activity.handleSAFDialog(realPath) {
                                val extension = view.file_extension.value
                                activity.config.lastUsedExtension = extension
                                activity.config.lastUsedSavePath = realPath
                                callback(realPath, extension)
                                dismiss()
                            }
                        }
                    }
                }
    }
}
