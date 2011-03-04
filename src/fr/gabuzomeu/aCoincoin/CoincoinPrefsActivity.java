package fr.gabuzomeu.aCoincoin;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CoincoinPrefsActivity extends PreferenceActivity {

	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			PreferenceManager.setDefaultValues( this, R.xml.mainpreferences , true);
			addPreferencesFromResource(R.xml.mainpreferences);
		
			
		
			//lv.setA
			//Preference prefs = (Preference) findPreference("AcoincoinPrefs");
			/*prefs.setOnPreferenceClickListener( new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					//prefs.getPreferenceManager()
					return false;
				}
			});
			*/
			

		
		}
	


}
