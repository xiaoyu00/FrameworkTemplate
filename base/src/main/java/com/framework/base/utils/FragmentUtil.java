package com.framework.base.utils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class FragmentUtil {

	// public static int mStackLevel;

	@SuppressLint("NewApi")
	public static void addFragmentToStack(Fragment fragment, FragmentTransaction ft, int fragmentLayoutId) {
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//		 ft.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in,
//		 R.animator.slide_fragment_horizontal_left_out,
//		 R.animator.slide_fragment_horizontal_left_in,
//		 R.animator.slide_fragment_horizontal_right_out);
		ft.add(fragmentLayoutId, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	@SuppressLint("NewApi")
	public static void replaceFragmentToStack(Fragment fragment, FragmentTransaction ft, int fragmentLayoutId) {
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//		 ft.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in,
//		 R.animator.slide_fragment_horizontal_left_out,
//		 R.animator.slide_fragment_horizontal_left_in,
//		 R.animator.slide_fragment_horizontal_right_out);
		ft.replace(fragmentLayoutId, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}
}
