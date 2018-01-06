package jekill.sumit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.content.SharedPreferences;

public class SettingsScreen extends AppCompatActivity {

    private CheckBox musicCheck;
    private CheckBox effectsCheck;
    protected SeekBar musicBar;
    protected SeekBar effectsBar;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_MusicVolume = "MusicVolume";
    public static final String APP_PREFERENCES_EffectsVolume = "EffectsVolume";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_settings_screen);
        getSupportActionBar().hide();
        musicCheck = (CheckBox) findViewById(R.id.musicCheck);
        musicCheck.setChecked(true);
        musicCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onMusicCheckClicked();
            }
        });
        effectsCheck = (CheckBox) findViewById(R.id.effectsCheck);
        effectsCheck.setChecked(true);
        effectsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEffectsCheckClicked();
            }
        });
        musicBar = (SeekBar) findViewById(R.id.musicBar);
        musicBar.setProgress(100);
        effectsBar = (SeekBar) findViewById(R.id.effectsBar);
        effectsBar.setProgress(100);
        if(mSettings.contains(APP_PREFERENCES_MusicVolume)) {
            int volume=0;
            musicBar.setProgress(mSettings.getInt(APP_PREFERENCES_MusicVolume, volume));
        }
        if(mSettings.contains(APP_PREFERENCES_EffectsVolume)) {
            int volume=0;
            effectsBar.setProgress(mSettings.getInt(APP_PREFERENCES_EffectsVolume, volume));
        }
    }
    public void onMusicCheckClicked(){
        if(musicBar.getProgress()>0) {
            musicBar.setProgress(0);
            musicBar.setEnabled(false);
        }
        else{
            musicBar.setProgress(100);
            musicBar.setEnabled(true);
        }
    }
    public void onEffectsCheckClicked(){
        if(effectsBar.getProgress()>0) {
            effectsBar.setProgress(0);
            effectsBar.setEnabled(false);
        }
        else{
            effectsBar.setProgress(100);
            effectsBar.setEnabled(true);
        }
    }
    protected void onDestroy(){
        int musicVolume=musicBar.getProgress();
        int effectsVolume=effectsBar.getProgress();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_MusicVolume, musicVolume);
        editor.putInt(APP_PREFERENCES_EffectsVolume, effectsVolume);
        editor.apply();
        super.onDestroy();
    }
    protected void onResume(){
        if(mSettings.contains(APP_PREFERENCES_MusicVolume)) {
            int volume=0;
            musicBar.setProgress(mSettings.getInt(APP_PREFERENCES_MusicVolume, volume));
        }
        if(mSettings.contains(APP_PREFERENCES_EffectsVolume)) {
            int volume=0;
            effectsBar.setProgress(mSettings.getInt(APP_PREFERENCES_EffectsVolume, volume));
        }
        super.onResume();
    }
    public void FromStoMM(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
