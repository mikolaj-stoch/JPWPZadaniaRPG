package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.myapplication.GameObj.Player;
import com.example.myapplication.Items.Inventory;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    public static int screenW;
    public static int screenH;

    private Context context;
    private SurfaceHolder surfaceHolder;

    private GameThread thread;
    private Background background;

    private Player player;

    private Inventory inventory;

    private BitmapSheet playerSheet;
    private BitmapSheet itemsSheet;

    private int x;
    private int y;

    private int xNew;
    private int yNew;

    private Boolean collisionProblem = false;

    private int world;

    public GameView(Context context){
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);      //Callback intercepts events
        thread = new GameThread(surfaceHolder, this);
        setFocusable(true);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenW = size.x;
        this.screenH = size.y;

        x = size.x / 2;
        y = size.y / 2;


        initGameObjects();
    }

    private void initGameObjects() {

        Bitmap playerSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_sheet);
        this.playerSheet = new BitmapSheet(playerSheet, playerSheet.getWidth(),
                playerSheet.getHeight(), 13, 21);

        this.player = new Player(this.context, this, this.playerSheet, "Joel the Ugly");

        //sprite sheet with all the items
        Bitmap imgSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_48x48);
        this.itemsSheet = new BitmapSheet(imgSheet, imgSheet.getWidth(), imgSheet.getHeight(), 20,15);

        this.background = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.grassbackground),this,player); //Added 0.1
        this.background.setVector(4,4); //Test. Sets how long image moves when touched.

        this.player.setBackground(background);

        this.inventory = new Inventory(this.context);
        this.player.initMission();
        startingPack();
    }




    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (float) WIDTH;
        final float scaleFactorY = getHeight()/ (float) HEIGHT;
        if(canvas != null){ ;
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
            canvas.restoreToCount(savedState);
            player.draw(canvas);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        synchronized (surfaceHolder) {
            int eventAction = event.getAction();
            //TODO WSKAZÓWKA NUMER 1


            // TODO WSKAZÓWKA NUMER 1
           // Log.d("Position touch", "(x, y): " + xNew + ", " + yNew);
            switch (eventAction) {
                case MotionEvent.ACTION_DOWN: {
                    break;
                }
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    // TODO WSKAZÓWKA NUMER 2 ORAZ 3


                    //TODO WSKAZÓWKA NUMER 2 ORAZ 3
            }
        }
        return true;
        }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        if(thread.getState() == Thread.State.NEW){
            thread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (InterruptedException e ){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        background.update();
    }


    public void pause() {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (InterruptedException e ){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void resume(){
        thread = new GameThread(surfaceHolder, this);
        thread.setRunning(true);
        thread.start();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void startingPack() {
        for(int i = 0; i < 10; i++){
            inventory.addItem(inventory.getHealingPotion());
        }
        for(int i = 0; i < 10; i++){
            inventory.addItem(inventory.getStaminaPotion());
        }

        inventory.addItem(inventory.getHealingPotionM());
        inventory.addItem(inventory.getStaminaPotionM());   //enemies drops weapons
        inventory.addItem(inventory.getKey1());
        inventory.addItem(inventory.getKey1());
    }


    public int getScreenW() {
        return screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public GameThread getThread() {
        return thread;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory(){
        return inventory;
    }


}
