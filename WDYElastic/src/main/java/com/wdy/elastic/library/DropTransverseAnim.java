package com.wdy.elastic.library;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * 作者：王东一
 * 创建时间：2017/6/22.
 */

public class DropTransverseAnim extends Animation {
    /**
     * 目标的高度
     */
    private int targetWidth;
    /**
     * 目标view
     */
    private View view;
    /**
     * 是否展开
     */
    private boolean isOpen;
    /**
     * 保留的宽度
     */
    private int miniWidth;

    /**
     * 构造方法
     * param targetview需要被展现的view
     * param viewHeight目的高
     */
    public DropTransverseAnim(View targetView, int targetWidth, boolean isOpen, int miniWidth) {
        this.view = targetView;
        this.targetWidth = targetWidth;
        this.isOpen = isOpen;
        this.miniWidth = miniWidth;
    }

    //移动的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth;
        if (isOpen) {
            newWidth = (int) (targetWidth * interpolatedTime);
        } else {
            newWidth = (int) (targetWidth * (1 - interpolatedTime));
        }
        view.getLayoutParams().width = newWidth + miniWidth;
        view.requestLayout();
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
