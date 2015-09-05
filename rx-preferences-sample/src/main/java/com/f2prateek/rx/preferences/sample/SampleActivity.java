package com.f2prateek.rx.preferences.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import rx.android.schedulers.AndroidSchedulers;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SampleActivity extends Activity {

  Preference<Boolean> fooPreference;

  @InjectView(R.id.foo_1) CheckBox foo1Checkbox;
  @InjectView(R.id.foo_2) CheckBox foo2Checkbox;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Views
    setContentView(R.layout.sample_activity);
    ButterKnife.inject(this);

    // Preferences
    SharedPreferences preferences = getDefaultSharedPreferences(this);
    RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

    // foo
    fooPreference = rxPreferences.getBoolean("foo", Boolean.FALSE);

    bindPreference(foo1Checkbox, fooPreference);
    bindPreference(foo2Checkbox, fooPreference);
  }

  void bindPreference(CheckBox checkBox, Preference<Boolean> preference) {
    // bind the preference to the checkbox
    preference.asObservable() //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(RxCompoundButton.checked(checkBox));
    // bind the checkbox to the preference
    RxCompoundButton.checkedChanges(checkBox).skip(1).subscribe(preference.asAction());
  }
}
