package android.example.com.testanim1;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;


public class InteractiveBook extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.book_screen);
        final ViewAnimator pages =
                (ViewAnimator) findViewById(R.id.pages);
        Button prev = (Button) findViewById(R.id.prev);
        Button next = (Button) findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pages.showPrevious();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pages.showNext();
            }
        });

        AnimationSet slideAndScale = new AnimationSet(true);
        TranslateAnimation slide = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0.75f,
                Animation.RELATIVE_TO_PARENT, 0
        );
        ScaleAnimation scale = new ScaleAnimation(
                10,
                1,
                10,
                1
        );
        slideAndScale.addAnimation(slide);
        slideAndScale.addAnimation(scale);
        slideAndScale.setDuration(1000);
        pages.setInAnimation(slideAndScale);

        View rollingBall = findViewById(R.id.rollingball);
        ObjectAnimator ballRoller =
                ObjectAnimator.ofFloat(
                        rollingBall,
                        "TranslationX",
                        0,
                        400
                );
        ballRoller.setDuration(2000);
        ballRoller.setRepeatMode(ObjectAnimator.REVERSE);
        ballRoller.setRepeatCount(ObjectAnimator.INFINITE);
        ballRoller.start();
        final View bouncingBall = findViewById(R.id.bouncingball);
        ValueAnimator ballBouncer = ValueAnimator.ofInt(0, 40);
        ballBouncer.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator ballBouncer) {
                        final int animatedValue = (Integer) ballBouncer.getAnimatedValue();
                        bouncingBall.post(new Runnable() {
                                              public void run() {
                                                  Log.d("", "####ball update " + animatedValue);
                                                  bouncingBall.setPadding(
                                                          bouncingBall.getPaddingLeft(),
                                                          40 - animatedValue,
                                                          bouncingBall.getPaddingRight(),
                                                          animatedValue);
                                                  bouncingBall.invalidate();
                                              }
                                          }
                        );

                    }
                }
        );
        ballBouncer.setDuration(2000);
        ballBouncer.setRepeatMode(ValueAnimator.REVERSE);
        ballBouncer.setRepeatCount(ValueAnimator.INFINITE);
        ballBouncer.setInterpolator(new BounceInterpolator());


        ValueAnimator.setFrameDelay(100);
        ballBouncer.start();

    }
}