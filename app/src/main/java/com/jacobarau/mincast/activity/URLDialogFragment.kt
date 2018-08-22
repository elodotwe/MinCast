package com.jacobarau.mincast.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.jacobarau.mincast.R

/**
 * Use the [URLDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class URLDialogFragment : DialogFragment() {
    private var listener: OnFragmentInteractionListener? = null

    // It's a DialogFragment. We can ONLY pass null into setView.
    // I contend that warning for this here is an Android Studio bug.
    // See https://stackoverflow.com/questions/26404951/avoid-passing-null-as-the-view-root-warning-when-inflating-view-for-use-by-ale
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        // Get the layout inflater
        val inflater = activity!!.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_urldialog, null))
                // Add action buttons
                .setPositiveButton(R.string.add_button) { dialog, id ->
                    listener?.onAddURL(
                            this.dialog.findViewById<EditText>(R.id.podcast_url_entry)
                                    .text.toString())
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onAddURL(url: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         */
        @JvmStatic
        fun newInstance() =
                URLDialogFragment()
    }
}
