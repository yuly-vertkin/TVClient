package com.example.tvclient.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.tvclient.R

class SimpleDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val titleId = arguments?.getInt(TITLE_KEY) ?: 0
        val textId = arguments?.getInt(TEXT_KEY) ?: 0
        val okId = arguments?.getInt(OK_BUTTON) ?: 0
        val cancelId = arguments?.getInt(CANCEL_BUTTON) ?: 0
        return AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(textId)
            .setPositiveButton(okId) { _, _ ->
                setFragmentResult(SIMPLE_DIALOG_REQUEST_KEY, bundleOf(SIMPLE_DIALOG_RESULT_KEY to true))
            }
            .setNegativeButton(cancelId) {_, _ ->
                setFragmentResult(SIMPLE_DIALOG_REQUEST_KEY, bundleOf(SIMPLE_DIALOG_RESULT_KEY to false))
            }
            .create()
    }

// TODO: can create from layout
/*
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_dialog, container, false)
    }
*/

    companion object {
        private const val TITLE_KEY = "title"
        private const val TEXT_KEY = "text"
        private const val OK_BUTTON = "ok"
        private const val CANCEL_BUTTON = "cancel"

        fun show(fragment: Fragment, titleId: Int, textId: Int,
                 okId: Int = R.string.ok_button, cancelId: Int = R.string.cancel_button,
                 onCancelClick: (() -> Unit)? = null, onOkClick: () -> Unit) {
            val fragmentManager = fragment.childFragmentManager
            SimpleDialog().apply {
                arguments = bundleOf(TITLE_KEY to titleId, TEXT_KEY to textId, OK_BUTTON to okId, CANCEL_BUTTON to cancelId)
            }.show(fragmentManager, null)

            fragmentManager.setFragmentResultListener(SIMPLE_DIALOG_REQUEST_KEY, fragment) { requestKey, bundle ->
                val isOk = bundle.getBoolean(SIMPLE_DIALOG_RESULT_KEY)
                if (isOk) {
                    onOkClick()
                } else onCancelClick?.let {
                    it()
                }
            }
        }
    }
}