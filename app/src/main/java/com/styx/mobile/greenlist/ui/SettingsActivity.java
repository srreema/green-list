package com.styx.mobile.greenlist.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.styx.mobile.greenlist.R;

/**
 *
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setttings);


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isPasswordEnabled = sharedPreferences.getBoolean("isPasswordEnabled", false);
        Preference prefAppKey = findPreference("appPasskey");
        prefAppKey.setEnabled(isPasswordEnabled);


        PreferenceScreen screen = getPreferenceScreen();
        for (int i = 0; i < screen.getPreferenceCount(); i++) {
            Preference preference = screen.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); j++) {
                    preferenceGroup.getPreference(j).setOnPreferenceClickListener(this);
                }
            } else {
                preference.setOnPreferenceClickListener(this);
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == findPreference("isPasswordEnabled")) {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            findPreference("appPasskey").setEnabled(sharedPreferences.getBoolean("isPasswordEnabled", false));
        }
        return true;
    }

}
