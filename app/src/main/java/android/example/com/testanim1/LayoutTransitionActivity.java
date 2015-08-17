package android.example.com.testanim1;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class LayoutTransitionActivity extends Activity {

    private ViewGroup viewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anim_screen);
        LayoutTransition l = new LayoutTransition();
        l.setDuration(LayoutTransition.APPEARING, 500);
        l.setDuration(LayoutTransition.DISAPPEARING, 400);
        AnimatorSet animatorX = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.x_animator);
        l.setAnimator(LayoutTransition.DISAPPEARING, animatorX);
       // l.enableTransitionType(LayoutTransition.CHANGING);
        viewGroup = (ViewGroup) findViewById(R.id.container);
        viewGroup.setLayoutTransition(l);

    }

    static int i = 77;

    public void onClick(View view) {
        Button btn = new Button(this);
        btn.setText(String.valueOf(i++));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                viewGroup.removeView(btn);
            }
        });
        viewGroup.addView(btn);
    }

}
