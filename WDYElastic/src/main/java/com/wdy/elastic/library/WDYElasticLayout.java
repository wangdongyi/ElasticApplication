package com.wdy.elastic.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 作者：王东一
 * 创建时间：2017/6/22.
 */

public class WDYElasticLayout extends RelativeLayout {
    private static final String TAG = "WDYElasticLayout";
    private RelativeLayout wdy_elastic_content_layout;
    private RelativeLayout wdy_elastic_main_layout;
    private RelativeLayout wdy_elastic_parent_layout = null;
    private ImageView wdy_elastic_imageView;
    private Animation animationOpen;
    private Animation animationClose;
    boolean isShowView;
    private int width = 0;
    private int imageButtonBackground;
    private int imageButtonGravity = 2;

    public WDYElasticLayout(Context context) {
        super(context);
        initView();
    }

    public WDYElasticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
        initView();
    }

    public WDYElasticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
        initView();
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.WDYElasticLayout);
        if (attr == null) {
            return;
        }
        try {
            imageButtonBackground = attr.getResourceId(R.styleable.WDYElasticLayout_imageButtonBackground, R.drawable.wdy_round_background);
            imageButtonGravity = attr.getInt(R.styleable.WDYElasticLayout_imageButtonGravity, 2);
        } finally {
            attr.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (wdy_elastic_parent_layout == null) {
            getView();
        }
    }

    /**
     * 获取对象
     */
    private void getView() {
        int children = getChildCount();
        if (children > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                wdy_elastic_parent_layout = (RelativeLayout) getChildAt(0);
                if (i > 0) {
                    View view = getChildAt(i);
                    removeView(view);
                    wdy_elastic_content_layout.addView(view);
                }
            }
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.elastic_layout, this, true);
        wdy_elastic_main_layout = findViewById(R.id.wdy_elastic_main_layout);
        wdy_elastic_content_layout = findViewById(R.id.wdy_elastic_content_layout);
        wdy_elastic_imageView = findViewById(R.id.wdy_elastic_imageView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) wdy_elastic_main_layout.getLayoutParams();
        switch (imageButtonGravity) {
//            <enum name="center" value="0"/>
//            <enum name="left" value="1"/>
//            <enum name="right" value="2"/>
            case 0:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
            case 1:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case 2:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
        }
        wdy_elastic_main_layout.setLayoutParams(params);
        if (imageButtonBackground != 0) {
            wdy_elastic_main_layout.setBackgroundResource(imageButtonBackground);
        }
        Log.i(TAG, "wdy_elastic_content_layout:" + wdy_elastic_content_layout.getId() +
                "\nwdy_elastic_main_layout" + 0 +
                "\nwdy_elastic_imageView" + wdy_elastic_imageView.getId());
        ViewTreeObserver vto2 = wdy_elastic_main_layout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                wdy_elastic_main_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = wdy_elastic_main_layout.getMeasuredWidth();
            }
        });
        wdy_elastic_imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "点击了");
                stretchAnimation();
                addZoomAnimation(view);
            }
        });
    }

    private void stretchAnimation() {
        wdy_elastic_main_layout.clearAnimation();
        if (isShowView) {
            isShowView = false;
            if (animationClose == null) {
                animationClose = new DropTransverseAnim(wdy_elastic_main_layout, width, true, dip2px(50));
                animationClose.setDuration(200); // SUPPRESS CHECKSTYLE
            }
            wdy_elastic_content_layout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wdy_elastic_content_layout.setVisibility(VISIBLE);
                }
            }, 100);
            wdy_elastic_main_layout.startAnimation(animationClose);
        } else {
            isShowView = true;
            if (animationOpen == null) {
                animationOpen = new DropTransverseAnim(wdy_elastic_main_layout, width, false, dip2px(50));
                animationOpen.setDuration(200); // SUPPRESS CHECKSTYLE
            }
            wdy_elastic_content_layout.setVisibility(GONE);
            wdy_elastic_main_layout.startAnimation(animationOpen);
        }
    }

    public static void addZoomAnimation(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(150);
        rotateAnimation.setFillAfter(false);
        view.setAnimation(rotateAnimation);
        view.startAnimation(rotateAnimation);
    }

    public ImageView getBtnImageView() {
        return wdy_elastic_imageView;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
