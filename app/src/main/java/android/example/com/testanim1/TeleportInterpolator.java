package android.example.com.testanim1;

import android.view.animation.Interpolator;

public class TeleportInterpolator implements Interpolator
{
	public float getInterpolation(float input)
	{

		return (Math.random()<input)?0.9f:0.1f;
	}
}
