package br.com.google.android.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

public class AnimationUtil {
	
	public static void animaFotoSlideLeftToRight(ImageView imageView) {
		int x = (int) imageView.getX() + 250;
		
		ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "x", 0, x);
		animation.setInterpolator(new BounceInterpolator());
		animation.setDuration(1500);
		animation.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
			}
		});
		animation.start();
	}
	
	public static void animaFotoFadeIn(ImageView imageView) {
		Animation animation = new AlphaAnimation(0.0F, 1.0F);
		animation.setDuration(1500);
		imageView.setAnimation(animation);
	}
}