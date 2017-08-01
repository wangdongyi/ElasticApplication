package com.wdy.elastic.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
    private Animation animationDown;
    private Animation animationUp;
    private boolean isShowView;
    private int width = 0;
    private int height = 0;
    private int imageButtonBackground;
    private int imageButtonIcon;
    private int layoutBackground;
    private int imageButtonGravity = 2;
    private boolean isPlaying = false;
    private int miniWidth = 0;

    public boolean isOpen() {
        return isShowView;
    }

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
            imageButtonBackground = attr.getResourceId(R.styleable.WDYElasticLayout_imageButtonBackground, R.drawable.wdy_oval_image);
            imageButtonIcon = attr.getResourceId(R.styleable.WDYElasticLayout_imageButtonIcon, R.drawable.wdy_elastic_add);
            layoutBackground = attr.getResourceId(R.styleable.WDYElasticLayout_layoutBackground, R.drawable.wdy_round_background);
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
                    i--;
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
        if (layoutBackground != 0) {
            wdy_elastic_main_layout.setBackgroundResource(layoutBackground);
        }
        if (imageButtonBackground != 0) {
            wdy_elastic_imageView.setBackgroundResource(imageButtonBackground);
        }
        if (imageButtonIcon != 0) {
            wdy_elastic_imageView.setImageResource(imageButtonIcon);
        }
        ViewTreeObserver vto2 = wdy_elastic_imageView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                wdy_elastic_imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = wdy_elastic_imageView.getMeasuredHeight();
                RelativeLayout.LayoutParams params = (LayoutParams) wdy_elastic_imageView.getLayoutParams();
                params.width = height;
                miniWidth = height;
                wdy_elastic_imageView.setLayoutParams(params);
            }
        });
        ViewTreeObserver vto1 = wdy_elastic_main_layout.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                wdy_elastic_main_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = wdy_elastic_main_layout.getMeasuredWidth();
                height = wdy_elastic_main_layout.getMeasuredHeight();
                initAnimation();
            }
        });
        wdy_elastic_imageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                stretchAnimation();
                addZoomAnimation(view);
            }
        });
    }

    //开始动画
    public void stretchAnimation() {
        if (isPlaying) {
            return;
        }
        wdy_elastic_main_layout.clearAnimation();
        if (isShowView) {
            isShowView = false;
            wdy_elastic_main_layout.startAnimation(animationClose);
            isPlaying = true;
        } else {
            isShowView = true;
            wdy_elastic_main_layout.startAnimation(animationOpen);
            isPlaying = true;
        }
    }

    private void initAnimation() {
        if (animationUp == null) {
            animationUp = new DropDownAnim(wdy_elastic_main_layout, height - dip2px(50), dip2px(50), true);
            animationUp.setDuration(200); // SUPPRESS CHECKSTYLE
            animationUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isPlaying = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (animationClose == null) {
            animationClose = new DropTransverseAnim(wdy_elastic_main_layout, width, true, dip2px(50));
            animationClose.setDuration(200); // SUPPRESS CHECKSTYLE
            animationClose.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    wdy_elastic_content_layout.setVisibility(VISIBLE);
                    wdy_elastic_main_layout.startAnimation(animationUp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (animationDown == null) {
            animationDown = new DropDownAnim(wdy_elastic_main_layout, height - dip2px(50), dip2px(50), false);
            animationDown.setDuration(200); // SUPPRESS CHECKSTYLE
            animationDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isPlaying = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (animationOpen == null) {
            animationOpen = new DropTransverseAnim(wdy_elastic_main_layout, width, false, dip2px(50));
            animationOpen.setDuration(200); // SUPPRESS CHECKSTYLE
            animationOpen.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    wdy_elastic_content_layout.setVisibility(GONE);
                    wdy_elastic_main_layout.startAnimation(animationDown);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void addZoomAnimation(View view) {
        if (isShowView) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(150);
            rotateAnimation.setFillAfter(true);
            view.setAnimation(rotateAnimation);
            view.startAnimation(rotateAnimation);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(150);
            rotateAnimation.setFillAfter(true);
            view.setAnimation(rotateAnimation);
            view.startAnimation(rotateAnimation);
        }
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
