package jekill.sumit;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
public class GameActivity extends AppCompatActivity {
    private static GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameView = new GameView(GameActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(gameView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
    public void onBackPressed(){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Quit Game");
        String messagebox = "You are sure you want to quit game";
        alertBox.setMessage(messagebox);
        //alertBox.setButton
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                gameView.countTimer.cancel();
                finish();
                Intent mIntent = new Intent(GameActivity.this, MainMenu.class);
                startActivity(mIntent);

        }
        });
        alertBox.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertBox.show();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
}