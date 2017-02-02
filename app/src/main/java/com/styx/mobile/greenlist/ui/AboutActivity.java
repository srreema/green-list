package com.styx.mobile.greenlist.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.utils.Utils;

/**
 *
 */

public class AboutActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    private int numTimesVersionClicked;
    boolean isLoading = false;
    private static final String TAG = "AboutPreferenceFragment";
    int MAX_CLICKS_TO_UNLOCK_REG = 9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_about);
        Preference prefVersion = findPreference("version");

        PackageManager pm = getPackageManager();
        try {
            prefVersion.setSummary(pm.getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(TAG, "Error getting our own package name");
        }

        // Set an OnPreferenceClickListener on all Preferences.
        // Just set the OnPreferenceClickListener for the prefVersion in a normal project.
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
        if (preference == findPreference("copyright")) {
            if (++numTimesVersionClicked == MAX_CLICKS_TO_UNLOCK_REG && !isLoading) {
                numTimesVersionClicked = 0;
                Toast.makeText(AboutActivity.this, preference.getTitle().toString() + " Being written.", Toast.LENGTH_LONG).show();

            }
        } else if ((preference == findPreference("legal")) || (preference == findPreference("open_source_licenses")) || (preference == findPreference("terms_of_service")) || (preference == findPreference("privacy_policy"))) {
            Toast.makeText(AboutActivity.this, preference.getTitle().toString() + " Being written.", Toast.LENGTH_LONG).show();
        } else if (preference == findPreference("facebook")) {
            Intent facebookIntent = Utils.getFacebookIntent(getString(R.string.social_facebook));
            startActivity(facebookIntent);
        } else if (preference == findPreference("github")) {
            Intent facebookIntent = Utils.getGenericIntent(getString(R.string.social_github));
            startActivity(facebookIntent);
        } else if (preference == findPreference("youtube")) {
            Intent facebookIntent = Utils.getGenericIntent(getString(R.string.social_youtube));
            startActivity(facebookIntent);
        } else if (preference == findPreference("twitter")) {
            Intent facebookIntent = Utils.getGenericIntent(getString(R.string.social_twitter));
            startActivity(facebookIntent);
        } else if (preference == findPreference("rate_the_app")) {
            Intent facebookIntent = Utils.getMarketIntent(AboutActivity.this);
            startActivity(facebookIntent);
        }
        return true;
    }

}
