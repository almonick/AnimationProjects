package android.example.com.testanim1;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;


public class HanoiActivity extends Activity {
	/** Called when the activity is first created. */
	private static int[] towers = {
		R.id.tower_1,
		R.id.tower_2,
		R.id.tower_3
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hanoi);
//		TextView block1 = (TextView) findViewById(R.id.block_1);
//		Animation blockDrop = AnimationUtils.loadAnimation(this, R.anim.block_drop);
//        final Animation blockMove = AnimationUtils.loadAnimation(this, R.anim.block_move_right);
//        block1.setOnClickListener(new View.OnClickListener() {
//                                      @Override
//                                      public void onClick(View v) {
//                                          v.startAnimation(blockMove);
//                                      }
//                                  }
//        );

        for (int i = 0; i < towers.length; ++i) {
            ViewGroup tower = (ViewGroup) findViewById(towers[i]);
            tower.setOnClickListener(new TowerPicker(i));
        }


	}
	private class BlockMover implements Animation.AnimationListener {
		private int to, from;
		View block;
		protected BlockMover (View block, int from, int to) {
			this.block = block;
			this.to = to;
			this.from = from;
		}
		public void onAnimationRepeat(Animation animation) {}
		public void onAnimationStart(Animation animation) {}
		public void onAnimationEnd(Animation animation) {
			// We will fill this in in a minute
			block.post(new Runnable() {
				public void run() {
					ViewGroup toTower = (ViewGroup) findViewById(towers[to]);
					ViewGroup fromTower = (ViewGroup) block.getParent();
					fromTower.removeView(block);
					fromTower.clearDisappearingChildren();//<---- very important!!!!!

					toTower.addView(block, 0);

					Animation addAnimation =
							AnimationUtils.loadAnimation(HanoiActivity.this,
									R.anim.block_drop);
					block.setAnimation(addAnimation);

				}
			});

		}

		public void move() {

            int block_anim_id;
            if (to>from) {
                block_anim_id = R.anim.block_move_left;
            } else {
                block_anim_id = R.anim.block_move_right;
            }
            Animation removeAnimation =
                    AnimationUtils.loadAnimation(
                            HanoiActivity.this, block_anim_id);
			block.startAnimation(removeAnimation);
            removeAnimation.setAnimationListener(this);

		}
	}
	private class TowerPicker implements View.OnClickListener {
		private int towerIndex;
		public TowerPicker (int towerIndex) {
			this.towerIndex = towerIndex;
		}
		public void onClick(View v) {
			// We will fill this in in a minute
			if (fromTower == UNDECIDED) {
				ViewGroup tower = 
						(ViewGroup) findViewById(towers[towerIndex]);
				if (tower.getChildCount()>0) {
					fromTower = towerIndex;
					Animation glowAnimation = 
							AnimationUtils.loadAnimation(
									HanoiActivity.this,
									R.anim.tower_glow
									);
					tower.startAnimation(glowAnimation);

				}
			} else {
				ViewGroup fromTowerView = 
						(ViewGroup) findViewById(towers[fromTower]);
				if (fromTower != towerIndex) {
					ViewGroup toTowerView = 
							(ViewGroup) findViewById(towers[towerIndex]);
					View block = fromTowerView.getChildAt(0);
					View supportingBlock = toTowerView.getChildAt(0);


					if (supportingBlock == null 
							|| supportingBlock.getWidth()>block.getWidth()) {
						(new BlockMover (block,fromTower, towerIndex)).move();
					}
				}


				fromTowerView.clearAnimation();
				fromTower = UNDECIDED;
			}
		}
	}
	private static final int UNDECIDED = -1;
	private int fromTower = UNDECIDED;



}