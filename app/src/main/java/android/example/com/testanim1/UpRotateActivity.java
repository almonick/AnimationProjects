package android.example.com.testanim1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class UpRotateActivity extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block_up_screen);
		final Animation blockUp3 = AnimationUtils.loadAnimation(this, R.anim.block_up3);
        final Animation blockUp2 = AnimationUtils.loadAnimation(this, R.anim.block_up2);
        findViewById(R.id.block_1).setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              v.startAnimation(blockUp3);
                                                          }
                                                      }
        );

        findViewById(R.id.block_2).setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              v.startAnimation(blockUp2);
                                                          }
                                                      }
        );




	}

}