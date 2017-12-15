package jekill.sumit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private int blockWidth;
    private int blockHeight;
    private int mWidth;
    private int mHeight;
    private Paint backgroundPaint;
    private Paint foreground;
    @SuppressWarnings("unused")
    private List<ButtonClass> myListButton;
    private Bitmap skyBitmap;
    private Bitmap yellowBitmap;
    private Bitmap greenBitmap;
    private Bitmap redBitmap;
    private Bitmap orangeBitmap;
    private Bitmap purpleBitmap;
    private Bitmap blueBitmap;
    private Bitmap overlayer;
    private Bitmap backgroundOverlayer;
    private Bitmap pauseButton;
    private Bitmap createBitmap;
    private Context context;
    private Canvas newCanvas;
    private Paint paint;
    private Paint mForeground;
    private Bitmap sky;
    private Bitmap purple;
    private Bitmap blue;
    private Bitmap red;
    private Bitmap green;
    private Bitmap yellow;
    private Bitmap orange;
    @SuppressWarnings("unused")
    private int[][] buttonValues;
    private boolean touching = false;
    @SuppressWarnings("unused")
    private ArrayList<String> allTouched = new ArrayList<String>();
    private int numberGenerated = -1;
    @SuppressWarnings("unused")
    private ButtonClass clickObject;
    @SuppressWarnings("unused")
    private Bitmap[][] mBackgroundBitmap;
    private ButtonClass[][] boardGame;
    private int currentGamePoint = 0;
    private boolean changeQuestion;
    private int startCount = 0;
    private Rect headerRect;
    private Rect questionRect;
    private Paint mPaint;
    private Rect pauseRect;
    private SoundPool makeSound;
    private int soundPoolId;
    private boolean isSoundLoaded = false;
    private boolean soundOn;
    public MyCountDownTimer countTimer;
    private final long startTime = 120 * 1000;

    public long getInterval() {
        return interval;
    }

    private final long interval = 1000;
    private long gameTimer;
    @SuppressWarnings("unused")
    private boolean controlTimer = false;
    private boolean pauseTimer = false;

    public long getInitTimer() {
        return initTimer;
    }

    public void setInitTimer(long initTimer) {
        this.initTimer = initTimer;
    }

    private long initTimer = -1;
    public GameView(Context context) {
        super(context);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        setFocusable(true);
        soundOn = true;
        myListButton = new ArrayList<ButtonClass>();
        countTimer = new MyCountDownTimer(startTime, interval);
        countTimer.start();
        makeSound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPoolId = makeSound.load(context, R.raw.clickone, 1);
        makeSound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isSoundLoaded = true;
            }
        });
        this.context = context;
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(Color.parseColor("#D6DCB8"));
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#99bdbd"));
        paint = new Paint();
        paint.setColor(Color.BLUE);
        skyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        yellowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
        redBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        greenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        purpleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.purple);
        orangeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
        blueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
        overlayer = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        backgroundOverlayer = BitmapFactory.decodeResource(getResources(), R.drawable.grey);
        pauseButton = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        mForeground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForeground.setColor(Color.parseColor("#41266b"));
        mForeground.setStrokeWidth(2);
        mForeground.setStyle(Paint.Style.FILL_AND_STROKE);
        // Draw the numbers...
        // Define color and style for numbers
        foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(Color.BLACK);
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextAlign(Paint.Align.CENTER);
        // generate and store all text contents
        buttonValues =  getWrittenValues();
        // generate a new question
        numberGenerated = generateBasicMathsNumber(17, 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gameOver();
        newCanvas.drawColor(Color.TRANSPARENT);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawColor(Color.parseColor("#9fb711"));
        newCanvas.drawColor(Color.parseColor("#9fb711"));
        foreground.setTextSize(blockHeight * 0.5f);
        Paint.FontMetrics fm = foreground.getFontMetrics();
        canvas.drawRect(headerRect, mPaint);
        canvas.drawText("Score : " + String.valueOf(currentGamePoint), mWidth - 300, 50, foreground);
        newCanvas.drawRect(headerRect, paint);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                    newCanvas.drawBitmap(overlayer, null, gameSquareObject.getCoordinate(), paint);
                }
                else if(gameSquareObject.getSelected() && gameSquareObject.getAnswer()){
                    newCanvas.drawBitmap(backgroundOverlayer, null, gameSquareObject.getCoordinate(), paint);
                }
                else{
                    newCanvas.drawBitmap(gameSquareObject.getButtonBitmap(), null, gameSquareObject.getCoordinate(), paint);
                }
            }
        }
        int x = blockHeight / 2;
        float y = blockHeight / 2 - (fm.ascent + fm.descent) / 2;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getAnswer()){
                    //newCanvas.drawText(String.valueOf(gameSquareObject.getWrittenNumber()), i * blockHeight + x, j * blockHeight + y, foreground);
                }else{
                    newCanvas.drawText(String.valueOf(gameSquareObject.getWrittenNumber()), i * blockHeight + x, j * blockHeight + y, foreground);
                }
            }
        }
        newCanvas.drawLine(0, newCanvas.getHeight(), mWidth, newCanvas.getHeight(), mForeground);
        canvas.drawBitmap(createBitmap, 0, 50, null);
        foreground.setTextSize(blockHeight);
        if(startCount == 0){
            String numberString = String.valueOf(numberGenerated);
            canvas.drawRect(questionRect, backgroundPaint);
            int xs = (30 + questionRect.left + questionRect.width()) / 2;
            int ys = questionRect.top + 15 + (questionRect.height() / 2) ;
            canvas.drawText(numberString, xs, ys, foreground);
        }

        canvas.drawBitmap(pauseButton, null, pauseRect, backgroundPaint);
        canvas.drawText(String.valueOf(gameTimer), mWidth / 2, mHeight - 50, foreground);
        if(touching){
        }
        else{
            System.out.println("It is not touching");
            if(changeQuestion){
                //display the question to be asked
                String numberString = String.valueOf(numberGenerated);
                canvas.drawRect(questionRect, backgroundPaint);
                int xs = ((questionRect.left + questionRect.width()) / 2);
                int ys = questionRect.top + 15 + (questionRect.height() / 2) ;
                foreground.setTextSize(blockHeight);
                canvas.drawText(numberString, xs, ys, foreground);
            }
        }
    }
    // get all the bitmap for each object
    private Bitmap[][] returnDrawBitmap(){
        Bitmap[] buttonBitmap = new Bitmap[7];
        buttonBitmap[0] = red;
        buttonBitmap[1] = purple;
        buttonBitmap[2] = blue;
        buttonBitmap[3] = orange;
        buttonBitmap[4] = yellow;
        buttonBitmap[5] = sky;
        buttonBitmap[6] = green;
        Bitmap[][] allTiless = new Bitmap[8][8];
        for(int i = 0; i < allTiless.length; i++){
            for(int j = 0; j < allTiless.length; j++){
                int index = generateBasicMathsNumber(6, 0);
                Bitmap mBitmap = buttonBitmap[index];
                allTiless[i][j] = mBitmap;
            }
        }
        return allTiless;
    }
    //get all the rectangle in the object class
    private Rect[][] returnRect(){
        Rect[][] allRect = new Rect[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                allRect[i][j] = new Rect(i * blockWidth - 1, j * blockWidth - 1, (1 + i) * blockWidth, (1 + j) * blockWidth);
            }
        }
        return allRect;
    }
    // Get all the written text in the button
    private int[][] getWrittenValues(){
        int[][] textValues = new int[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int newValue = generateBasicMathsNumber(9, 1);
                textValues[i][j] = newValue;
            }
        }
        return textValues;
    }
        private boolean[][] getSelectedButtons(){
        boolean[][] textValues = new boolean[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                textValues[i][j] = false;
            }
        }
        return textValues;
    }
    private boolean[][] getAnswers(){
        boolean[][] textValues = new boolean[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                textValues[i][j] = false;
            }
        }
        return textValues;
    }
    private ButtonClass[][] mathBoardValues(){
        Bitmap[][] buttonImage = returnDrawBitmap();
        int[][] text = getWrittenValues();
        Rect[][] mRect = returnRect();
        boolean[][] selected =  getSelectedButtons();
        boolean[][] answered = getAnswers();
        ButtonClass[][] mGameBoard = new ButtonClass[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Bitmap bObject = buttonImage[i][j];
                int mText = text[i][j];
                Rect rect = mRect[i][j];
                boolean select = selected[i][j];
                boolean answer = answered[i][j];
                ButtonClass mButtonClass = new ButtonClass(bObject, mText, rect, select, answer);
                mGameBoard[i][j] = mButtonClass;
            }
        }
        return mGameBoard;
    }
    private static int generateBasicMathsNumber(int max, int min){
        Random r = new Random();
        int i = r.nextInt(max - min + 1) + min;
        return i;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        blockWidth = (int) (w / 8f);
        blockHeight = blockWidth;
        mWidth = w;
        mHeight = h;
        sky = Bitmap.createScaledBitmap(skyBitmap, blockWidth, blockWidth, false);
        green = Bitmap.createScaledBitmap(greenBitmap, blockWidth, blockWidth, false);
        yellow = Bitmap.createScaledBitmap(yellowBitmap, blockWidth, blockWidth, false);
        orange = Bitmap.createScaledBitmap(orangeBitmap, blockWidth, blockWidth, false);
        red = Bitmap.createScaledBitmap(redBitmap, blockWidth, blockWidth, false);
        purple = Bitmap.createScaledBitmap(purpleBitmap, blockWidth, blockWidth, false);
        blue = Bitmap.createScaledBitmap(blueBitmap, blockWidth, blockWidth, false);
        overlayer = Bitmap.createScaledBitmap(overlayer, blockWidth, blockWidth, false);
        backgroundOverlayer = Bitmap.createScaledBitmap(backgroundOverlayer, blockWidth, blockWidth, false);
        pauseButton = Bitmap.createScaledBitmap(pauseButton, 100, 100, false);
        mBackgroundBitmap = returnDrawBitmap();
        boardGame = mathBoardValues();
        createBitmap = Bitmap.createBitmap(mWidth, blockWidth * 8, Bitmap.Config.ARGB_8888);
        newCanvas = new Canvas(createBitmap);
        headerRect = new Rect(0, 10, mWidth, 80);
        pauseRect = new Rect(800, 1700, w - 70, h - 35);
        questionRect = new Rect(10, mHeight - 20, 110,  mHeight - 100);
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height = 0;
        switch(widthMode){
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                width = widthSize;
                break;
        }
        switch(heightMode){
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                height = heightSize;
                break;
        }
        setMeasuredDimension(width, height);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        final int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        System.out.println("The coordinates are " + "Y " + y + " x " + x);
        if (action==MotionEvent.ACTION_DOWN) {
            checkCollision(x, y);
            checkSelectedAnswers();
            togglePause(x, y);
            touching = true;
        }
        if(action == MotionEvent.ACTION_UP){
            touching = false;
        }
        invalidate();
        return true;
    }
    private void checkCollision(int x, int y){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getCoordinate().contains(x, y - 50)){
                    //System.out.println("Pointers  " + y + " " + x);
                    if(gameSquareObject.getSelected() && gameSquareObject.getAnswer()){
                        return;
                    }
                    if(gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                        gameSquareObject.setSelected(false);
                    }else{
                        gameSquareObject.setSelected(true);
                    }
                }
            }
        }
    }
    private void checkSelectedAnswers(){
        int mAnswers = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                    mAnswers += gameSquareObject.getWrittenNumber();
                }
            }
        }
        if(mAnswers > numberGenerated){
            resetAllSelectedAnswers();
        }
        if(mAnswers == numberGenerated){
            currentGamePoint += 100;
            playSound();
            // delete all the current selected buttons
            System.out.println("Remaining values " + checkNumberofButtonRemaining());
            if(assignFinalNumber()){
                numberGenerated = checkNumberofButtonRemaining();
            }else{
                numberGenerated = questionNumberGeneration();
            }
            startCount = 1;
            markButtonForDeleted();
            changeQuestion = true;
        }
    }
    private void resetAllSelectedAnswers(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                    gameSquareObject.setSelected(false);
                }
            }
        }
    }
    private void markButtonForDeleted(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(gameSquareObject.getSelected()){
                    gameSquareObject.setAnswer(true);
                }
            }
        }
    }
    private void playSound(){
        if(isSoundLoaded && soundOn){
            makeSound.play(soundPoolId, 1, 1, 0, 0, 1);
        }
    }
    public void togglePause(int x, int y){
        if(pauseRect.contains(x, y)){
            if(pauseTimer){
                pauseTimer = false;
                if(initTimer == -1){
                    countTimer = new MyCountDownTimer(startTime, interval);
                    countTimer.start();
                }
                else{
                    countTimer = new MyCountDownTimer(initTimer, interval);
                    countTimer.start();
                }
            }else{
                pauseTimer = true;
                countTimer.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Paused")
                        .setCancelable(false)
                        .setNegativeButton("Unpause",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        countTimer = new MyCountDownTimer(initTimer, interval);
                                        countTimer.start();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    public class MyCountDownTimer extends CountDownTimerPausable {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            Intent intent = new Intent(context, GameScoreActivity.class);
            intent.putExtra("GAME SCORE", currentGamePoint);
            context.startActivity(intent);
    }
        @Override
        public void onTick(long millisUntilFinished) {
            initTimer = millisUntilFinished;
            gameTimer = millisUntilFinished / 1000;
            invalidate();
        }
    }
    private int checkNumberofButtonRemaining(){
        int numRemaining = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(!gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                    numRemaining += gameSquareObject.getWrittenNumber();
                }
            }
        }
        return numRemaining;
    }
    private int questionNumberGeneration(){
        int returnValue = 0;
        List<String> data = new ArrayList<String>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ButtonClass gameSquareObject = boardGame[i][j];
                if(!gameSquareObject.getSelected() && !gameSquareObject.getAnswer()){
                    String values = String.valueOf(gameSquareObject.getWrittenNumber());
                    data.add(values);
                }
            }
        }
        Collections.shuffle(data);
        returnValue = Integer.parseInt(data.get(0)) + Integer.parseInt(data.get(0));
        return returnValue;
    }
    private boolean assignFinalNumber(){
        int numLeft = checkNumberofButtonRemaining();
        if(numLeft <= 10){
            return true;
        }
        return false;
    }
    private void gameOver(){
        if(numberGenerated == 0){
            Intent intent = new Intent(context, GameScoreActivity.class);
            intent.putExtra("GAME SCORE", currentGamePoint);
            context.startActivity(intent);
        }
    }

}