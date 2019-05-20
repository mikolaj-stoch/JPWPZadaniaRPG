package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private Context context;
    private GameView gameView;
    private Canvas canvas;


    boolean running = false;
    // desired fps
    private final static int    MAX_FPS = 40;
    // maximum number of frames to be skipped
    private final static int    MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        //this.context = context;
        this.gameView = gameView;
    }

//    @Override
//    public void run(){      //remember to create more efficient game loop (update()/ render())
//        while(running){
//            canvas = null;
//
//            try {
//                canvas = this.surfaceHolder.lockCanvas();       //freeze the canvas to allow us to draw on it
//                synchronized (surfaceHolder) {
//                    this.gameView.update();
//                    this.gameView.draw(canvas);
//                }
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//            finally {
//                if(canvas != null){
//                    try {
//                        surfaceHolder.unlockCanvasAndPost(canvas);    //stop editing, time to show
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

//    }



    @Override
    public void run() {
        Canvas canvas;
        Log.d("GAMETHREAD", "Starting game loop");

        long beginTime;     // the time when the cycle begun
        long timeDiff;      // the time it took for the cycle to execute
        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped

        long lastTime = System.nanoTime();
        int ups = 40;
        double nanoSecondConversion = 1000000000/ups;       //1 second [ns] / fps
        double changeInSeconds = 0;

        sleepTime = 0;

        while (running) {
            canvas = null;

            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                //Log.d("Thread", "LOCK");

                synchronized (surfaceHolder) {
                    // game loop that holds 30 fps
                    long now = System.nanoTime();

                    changeInSeconds += (now - lastTime) / nanoSecondConversion;
                    while (changeInSeconds >= 1) {
                        this.gameView.update();
                        changeInSeconds--;
                    }

                    if (canvas != null) {
                        this.gameView.draw(canvas);
                    }
                    lastTime = now;
                }
            }
            catch (Exception e){
                Log.d("Thread", "Excpetion");
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
            finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);      //2 times canvas is locked, when switch between activities !
                    //Log.d("Thread", "UNLOCK");
                }
                }
            }
        }


//                    beginTime = System.currentTimeMillis();
//                    framesSkipped = 0;  // resetting the frames skipped
//                    // update game state
//                    this.gameView.update();
//                    // render state to the screen
//                    // draws the canvas on the panel
//                    if(canvas != null) {
//                        this.gameView.draw(canvas);
//                    }
//                    // calculate how long did the cycle take
//                    timeDiff = System.currentTimeMillis() - beginTime;
//                    // calculate sleep time
//                    sleepTime = (int)(FRAME_PERIOD - timeDiff);
//
//                    if (sleepTime > 0) {
//                        // if sleepTime > 0 we're OK
//                        try {
//                            // send the thread to sleep for a short period
//                            // very useful for battery saving
//                            Thread.sleep(sleepTime);
//                        } catch (InterruptedException e) {}
//                    }
//
//                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
//                        // we need to catch up
//                        // update without rendering
//                        this.gameView.update();
//                        // add frame period to check if in next frame
//                        sleepTime += FRAME_PERIOD;
//                        framesSkipped++;
//                    }
//                    if (canvas != null) {
//                        surfaceHolder.unlockCanvasAndPost(canvas);      //2 times canvas is locked, when switch between activities !
//                        //Log.d("Thread", "UNLOCK");
//                    }
//                }
//            }
//            catch (Exception e){
//                Log.d("Thread", "Excpetion");
//                surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//            finally {
//                // in case of an exception the surface is not left in
//                // an inconsistent state
//                if (canvas != null) {
//                    surfaceHolder.unlockCanvasAndPost(canvas);      //2 times canvas is locked, when switch between activities !
//                    //Log.d("Thread", "UNLOCK");
//                }
//            }   // end finally
//        }
//    }

    public void setRunning(boolean b){
        this.running = b;
    }

    public boolean isRunning(){
        return running;
    }
}
