package jekill.sumit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
public class GameScoreActivity extends AppCompatActivity{
    protected TextView thisgamescore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.fragment_game_score);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       int game_score=getGameScore();
        thisgamescore= (TextView) findViewById(R.id.score);
        thisgamescore.setText(String.valueOf(game_score));
    }

    private int getGameScore(){

        int newIntentValue = -1;
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        if(mBundle != null){

            newIntentValue = mBundle.getInt("GAME SCORE");
        }
        return newIntentValue;
    }

    @Override
    public void onBackPressed() {

        Intent mIntent = new Intent(GameScoreActivity.this, MainMenu.class);
        startActivity(mIntent);
    }
    public void clickMainMenu(View view){
        Intent mIntent = new Intent(GameScoreActivity.this, MainMenu.class);
        startActivity(mIntent);
    }
}