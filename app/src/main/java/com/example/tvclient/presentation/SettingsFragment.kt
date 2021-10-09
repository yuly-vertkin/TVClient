package com.example.tvclient.presentation

import android.os.Bundle
import android.text.InputType
import androidx.preference.*
import com.example.tvclient.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val maxNumSwitch = findPreference<SwitchPreferenceCompat>(getString(R.string.max_items_switch_key))
        maxNumSwitch?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                isMaxNumShowing(newValue as Boolean)
                true
            }
        isMaxNumShowing(maxNumSwitch?.isChecked)
    }

    private fun isMaxNumShowing(isShowing: Boolean?) {
        val maxNumPref: EditTextPreference? = findPreference(getString(R.string.max_items_key))
        maxNumPref?.isVisible = isShowing ?: false
        maxNumPref?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }
}