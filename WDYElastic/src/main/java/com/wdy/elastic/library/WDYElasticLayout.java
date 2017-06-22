package com.wdy.elastic.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
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
    private LinearLayout wdy_elastic_content_layout;
    private ImageView wdy_elastic_imageView;
    private RelativeLayout wdy_elastic_main_layout;
    private Animation animationOpen;
    private Animation animationClose;
    boolean isShowView;
    private int width = 0;

    public WDYElasticLayout(Context context) {
        super(context);
        initView();
    }

    public WDYElasticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WDYElasticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.elastic_layout, this, true);
        wdy_elastic_content_layout = (LinearLayout) findViewById(R.id.wdy_elastic_content_layout);
        wdy_elastic_imageView = (ImageView) findViewById(R.id.wdy_elastic_imageView);
        wdy_elastic_main_layout = (RelativeLayout) findViewById(R.id.wdy_elastic_main_layout);
        ViewTreeObserver vto2 = wdy_elastic_content_layout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                wdy_elastic_content_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = wdy_elastic_content_layout.getWidth();
            }
        });
        wdy_elastic_imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stretchAnimation();
            }
        });
    }

    public void addContent(View view) {
        wdy_elastic_content_layout.removeAllViewsInLayout();
        wdy_elastic_content_layout.addView(view);
    }

    private void stretchAnimation() {
        wdy_elastic_content_layout.clearAnimation();
        if (isShowView) {
            isShowView = false;
            if (animationClose == null) {
                animationClose = new DropTransverseAnim(wdy_elastic_content_layout, width - dip2px(50), true, dip2px(50));
                animationClose.setDuration(200); // SUPPRESS CHECKSTYLE
            }
            wdy_elastic_content_layout.startAnimation(animationClose);
        } else {
            isShowView = true;
            if (animationOpen == null) {
                animationOpen = new DropTransverseAnim(wdy_elastic_content_layout, width - dip2px(50), false, dip2px(50));
                animationOpen.setDuration(200); // SUPPRESS CHECKSTYLE
            }
            wdy_elastic_content_layout.startAnimation(animationOpen);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
