/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.aosip.device.DeviceSettings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.TwoStatePreference;

import com.aosip.device.DeviceSettings.ModeSwitch.*;

public class DeviceSettings extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String KEY_HBM_SWITCH = "hbm";
    public static final String KEY_DC_SWITCH = "dc";

    private TwoStatePreference mHBMModeSwitch;
    private TwoStatePreference mDCModeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main);

        mDCModeSwitch = findPreference(KEY_DC_SWITCH);
        mDCModeSwitch.setEnabled(DCModeSwitch.isSupported());
        mDCModeSwitch.setChecked(DCModeSwitch.isCurrentlyEnabled());
        mDCModeSwitch.setOnPreferenceChangeListener(new DCModeSwitch());

        mHBMModeSwitch = findPreference(KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled());
        mHBMModeSwitch.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHBMModeSwitch) {
            Boolean enabled = (Boolean) newValue;
            Utils.writeValue(HBMModeSwitch.getFile(), enabled ? "5" : "0");
            Intent hbmIntent = new Intent(this.getContext(),
                    com.aosip.device.DeviceSettings.HBMModeService.class);
            if (enabled) {
                this.getContext().startService(hbmIntent);
            } else {
                this.getContext().stopService(hbmIntent);
            }
        }
        return true;
    }
}
