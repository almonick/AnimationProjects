package android.example.com.testanim1;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class BubblesView extends SurfaceView implements Callback {
    private LinkedList<Bubble> bubbles = new LinkedList<Bubble>();

    private Paint backgroundPaint = new Paint();
    private Bitmap bubbleBitmap;
    private SurfaceHolder surfaceHolder;
    private GameLoop gameLoop;

    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback((Callback) this);
        backgroundPaint.setColor(Color.BLUE);
        bubbleBitmap =
                BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.bubble);
    }

    public void surfaceChanged(
            SurfaceHolder holder,
            int format,
            int width,
            int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        startAnimation();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopAnimation();
    }

    public void startAnimation() {
        synchronized (this) {
            if (gameLoop == null) {
                gameLoop = new GameLoop();
                gameLoop.start();
            }
        }
    }

    public void stopAnimation() {
        synchronized (this) {
            boolean retry = true;
            if (gameLoop != null) {
                gameLoop.running = false;
                while (retry) {
                    try {
                        gameLoop.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            gameLoop = null;
        }
    }

    private class GameLoop extends Thread {
        private long msPerFrame = 1000 / 25;
        public boolean running = true;
        long frameTime = 0;

        public void run() {
            Canvas canvas = null;
            frameTime = System.currentTimeMillis();
            final SurfaceHolder surfaceHolder = BubblesView.this.surfaceHolder;
            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        calculateDisplay(canvas);
                        drawScreen(canvas);
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                waitTillNextFrame();
            }
        }

        private void waitTillNextFrame() {
            long nextSleep = 0;
            frameTime += msPerFrame;
            nextSleep = frameTime - System.currentTimeMillis();
            if (nextSleep > 0) {
                try {
                    sleep(nextSleep);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    private void drawScreen(Canvas c) {
        c.drawRect(
                0,
                0,
                c.getWidth(),
                c.getHeight(),
                backgroundPaint);

        for (Bubble bubble : bubbles) {
            bubble.draw(c);
        }
    }


    private void calculateDisplay(Canvas c, float numberOfFrames) {
        randomlyAddBubbles(c.getWidth(), c.getHeight(), numberOfFrames);
        LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();
        for (Bubble bubble : bubbles) {
            bubble.move(numberOfFrames);
            if (bubble.outOfRange())
                bubblesToRemove.add(bubble);
        }
        for (Bubble bubble : bubblesToRemove) {
            bubbles.remove(bubble);
        }
    }

    private void calculateDisplay(Canvas c) {
        randomlyAddBubbles(c.getWidth(), c.getHeight());
        LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();
        for (Bubble bubble : bubbles) {
            bubble.move();
            if (bubble.outOfRange())
                bubblesToRemove.add(bubble);
        }
        for (Bubble bubble : bubblesToRemove) {
            bubbles.remove(bubble);
        }
    }

    private float BUBBLE_FREQUENCY = 0.3f;

    public void randomlyAddBubbles(int screenWidth, int screenHeight, float numFrames) {
        if (Math.random() > BUBBLE_FREQUENCY * numFrames) return;
        bubbles.add(
                new Bubble(
                        (int) (screenWidth * Math.random()),
                        (int) (screenHeight) + Bubble.RADIUS,
                        (int) ((Bubble.MAX_SPEED - 0.1) * Math.random() + 0.1),
                        bubbleBitmap));

    }

    public void randomlyAddBubbles(int screenWidth, int screenHeight) {
        if (Math.random() > BUBBLE_FREQUENCY) return;
        bubbles.add(
                new Bubble(
                        (int) (screenWidth * Math.random()),
                        (int) (screenHeight) + Bubble.RADIUS,
                        (int) ((Bubble.MAX_SPEED - 0.1) * Math.random() + 0.1),
                        bubbleBitmap));

    }


    private class GameLoop2 extends Thread {
        private long msPerFrame = 1000 / 25;
        public boolean running = true;
        long frameTime = 0;

        public void run() {
            Canvas canvas = null;
            long thisFrameTime;
            long lastFrameTime = System.currentTimeMillis();
            float framesSinceLastFrame;
            final SurfaceHolder surfaceHolder =
                    BubblesView.this.surfaceHolder;
            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        drawScreen(canvas);
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                thisFrameTime = System.currentTimeMillis();
                framesSinceLastFrame = (float)
                        (thisFrameTime - lastFrameTime) / msPerFrame;
                lastFrameTime = thisFrameTime;
                calculateDisplay(canvas, framesSinceLastFrame);
            }
        }

        private void waitTillNextFrame() {
            long nextSleep = 0;

            long frameTime = (Long) msPerFrame;
            nextSleep = frameTime - System.currentTimeMillis();
            if (nextSleep > 0) {
                try {
                    sleep(nextSleep);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void sleep(long nextSleep) {
        // TODO Auto-generated method stub

    }








}
