package com.ehedgehog.android.spryrocksapp.screens.employee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentEmployeeInfoBinding
import com.ehedgehog.android.spryrocksapp.network.EmployeeInfo
import com.ehedgehog.android.spryrocksapp.screens.CardsPreferences
import com.ehedgehog.android.spryrocksapp.setImageWithGlide
import java.util.*

class EmployeeInfoFragment : Fragment() {

    companion object {
        private const val REQUEST_DATE = 0
        private const val DIALOG_DATE = "dialogDate"
    }

    private lateinit var binding: FragmentEmployeeInfoBinding
    private lateinit var viewModel: EmployeeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee_info, container, false)
        viewModel = ViewModelProviders.of(this).get(EmployeeInfoViewModel::class.java)

        binding.headerImage.setImageWithGlide(R.drawable.company_logo, true)

        binding.sendButton.setOnClickListener {
            createNewCard()
        }

        binding.dateButton.setOnClickListener {
            selectBirthDate()
        }

        viewModel.eventInfoSent.observe(this, Observer {
            if (it) {
                Toast.makeText(context, "Your info has been sent to HR", Toast.LENGTH_SHORT).show()
                context?.let { context -> CardsPreferences.setStoredCardId(context, viewModel.cardId.value!!) }
                viewModel.onInfoSendComplete()
            }
        })

        viewModel.storedEmployeeInfo.observe(this, Observer { updateEmployeeInfo(it) })

        viewModel.birthDate.observe(this, Observer {
            if (it != null) {
                val dateString = DateFormat.format(getString(R.string.date_format), it).toString()
                binding.dateButton.text = dateString
            }
        })

        viewModel.updateEmployeeInfoIfStored()
        viewModel.loadBoardLists()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(EmployeeDatePickerFragment.EXTRA_DATE)
            viewModel.setBirthDate(date as Date)
        }
    }

    private fun updateEmployeeInfo(employeeInfo: EmployeeInfo) {
        with(binding) {
            nameField.setText(employeeInfo.name, TextView.BufferType.EDITABLE)
            if (employeeInfo.birthDate.isNotEmpty()) {
                dateButton.text = employeeInfo.birthDate
            }
            positionField.setText(employeeInfo.position, TextView.BufferType.EDITABLE)
            phoneField.setText(employeeInfo.phone, TextView.BufferType.EDITABLE)
            telegramField.setText(employeeInfo.telegram, TextView.BufferType.EDITABLE)
            gmailField.setText(employeeInfo.gmail, TextView.BufferType.EDITABLE)
            githubField.setText(employeeInfo.github, TextView.BufferType.EDITABLE)
            gitlabField.setText(employeeInfo.gitlab, TextView.BufferType.EDITABLE)
        }
    }

    private fun createNewCard() {
        val info = with(binding) {
            EmployeeInfo(
                nameField.text.toString(),
                dateButton.text.toString(),
                positionField.text.toString(),
                phoneField.text.toString(),
                telegramField.text.toString(),
                gmailField.text.toString(),
                githubField.text.toString(),
                gitlabField.text.toString()
            )
        }

        if (info.name.isEmpty() || info.birthDate.isEmpty() || info.position.isEmpty() || info.phone.isEmpty() ||
            info.telegram.isEmpty() || info.gmail.isEmpty() || info.github.isEmpty() || info.gitlab.isEmpty()
        ) {
            Toast.makeText(context, "Please, fill in all fields", Toast.LENGTH_SHORT).show()
        } else {
            val storedCardId = context?.let { CardsPreferences.getStoredCardId(it) }
            viewModel.createCardInBoardList(info, storedCardId)
        }
    }

    private fun selectBirthDate() {
        val dialog = EmployeeDatePickerFragment.newInstance(binding.dateButton.text.toString())
        dialog.setTargetFragment(this, REQUEST_DATE)

        if (fragmentManager != null) {
            dialog.show(fragmentManager!!, DIALOG_DATE)
        }
    }

}