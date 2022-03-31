package com.senlasy.dhammapada.utility

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutTransformation : ViewPager2.PageTransformer {

    private var MINSCALE = 0.65f;
    private var MINALPHA = 0.3f;

    override fun transformPage(page: View, position: Float) {
        if (position <-1){  // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.alpha = 0f;

        }
        else if (position <=1){ // [-1,1]

            page.scaleX = Math.max(MINSCALE,1-Math.abs(position));
            page.scaleY = Math.max(MINSCALE,1-Math.abs(position));
            page.alpha = Math.max(MINALPHA,1-Math.abs(position));

        }
        else {  // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0f;

        }
    }

}