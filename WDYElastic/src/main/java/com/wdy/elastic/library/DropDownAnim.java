package com.wdy.elastic.library;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 作者：王东一
 * 创建时间：2017/6/26.
 */

public class DropDownAnim extends Animation {
    private final String TAG = "DropDownAnim";
    /**
     * 目标的高度
     */
    private int targetHeight;
    private int miniHeight;
    /**
     * 目标view
     */
    private View view;
    /**
     * 是否向下展开
     */
    private boolean down;

    /**
     * 构造方法
     * <p>
     * param targetview需要被展现的view
     * <p>
     * param viewHeight目的高
     *
     * @aram isdown true:向下展开，false:收起
     */
    public DropDownAnim(View targetView, int viewHeight, int miniHeight, boolean isDown) {
        this.view = targetView;
        this.targetHeight = viewHeight;
        this.down = isDown;
        this.miniHeight = miniHeight;
    }

    //down的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (down) {
            newHeight = (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = (int) (targetHeight * (1 - interpolatedTime));
        }
        Log.i(TAG + down, "newHeight:" + newHeight + "---targetHeight:" + targetHeight + "---interpolatedTime:" + interpolatedTime);
        view.getLayoutParams().height = newHeight + miniHeight;
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
