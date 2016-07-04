package anddev68.jp.gnctnotif;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.Date;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
        setupActionBar();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String gakkaStr = pref.getString("gakka",null);
        String gakunenStr = pref.getString("gakunen",null);
        String autoStr = pref.getString("auto",null);

        final DailyScheduler scheduler = new DailyScheduler(getApplicationContext());

        //  今すぐ取得ボタン
        final Preference rightNow = findPreference("right_now");
        rightNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getBaseContext(),"押されたよ！",Toast.LENGTH_SHORT).show();
                startService(new Intent(getApplicationContext(),NotificationService.class));
                return true;
            }
        });

        //  自動取得
        final ListPreference auto = (ListPreference) findPreference("auto");
        if(autoStr!=null) auto.setSummary(autoStr);
        auto.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                auto.setSummary(o.toString());
                switch(o.toString()){
                    case "自動取得しない":
                        scheduler.cancel(NotificationService.class,0,0);
                        break;
                    case "毎日 AM7:00":
                        scheduler.setByTime(NotificationService.class,7,0,0);
                        break;
                    case "毎週月曜日 AM7:00":
                        break;
                }

                return true;
            }
        });



        //  学科選択
        final ListPreference gakka = (ListPreference) findPreference("gakka");
        if(gakkaStr!=null) gakka.setSummary(gakkaStr);
        gakka.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                gakka.setSummary(o.toString());
                return true;
            }
        });

        //  学年選択
        final ListPreference gakunen = (ListPreference) findPreference("gakunen");
        if(gakunenStr!=null) gakunen.setSummary(gakunenStr);
        gakunen.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                gakunen.setSummary(o.toString());
                return true;
            }
        });


    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }



}
