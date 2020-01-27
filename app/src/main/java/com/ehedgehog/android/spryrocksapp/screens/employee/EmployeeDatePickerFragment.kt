package com.ehedgehog.android.spryrocksapp.screens.employee

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ehedgehog.android.spryrocksapp.R
import kotlinx.android.synthetic.main.fragment_dialog_date.view.*
import java.text.SimpleDateFormat
import java.util.*

class EmployeeDatePickerFragment : DialogFragment() {

    companion object {
        const val EXTRA_DATE = "pickedDate"
        private const val ARG_DATE = "date"

        fun newInstance(dateString: String): EmployeeDatePickerFragment {
            val args = Bundle()
            args.putString(ARG_DATE, dateString)

            val fragment = EmployeeDatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var datePicker: DatePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_date, null)

        datePicker = view.dialog_date_picker

        val dateString = arguments?.getString(ARG_DATE)
        val format = SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = format.parse(dateString)

        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )

        return AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle("Set you birth date")
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .setPositiveButton(android.R.string.ok) { _, _ ->  pickDate() }
            .create()
    }

    private fun pickDate() {
        val pickedDate = GregorianCalendar(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth
        ).time

        sendResult(Activity.RESULT_OK, pickedDate)
    }

    private fun sendResult(resultCode:Int, date: Date) {
        if (targetFragment == null)
            return

        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)

        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

}