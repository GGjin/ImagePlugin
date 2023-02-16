package com.gg.imageplugin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * @description:
 * @author: GG
 * @createDate: 2023 2.16 0016 14:47
 * @updateUser:
 * @updateDate: 2023 2.16 0016 14:47
 */

public class MonitorImageView extends ImageView implements MessageQueue.IdleHandler {
    private final String TAG = "MonitorImageView";
    private static final int MAX_ALARM_MULTIPLE = 2;
    private static final int MAX_ALARM_IMAGE_SIZE = 2 * 1024 * 1024;
    public MonitorImageView(Context context) {
        super(context);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        // 检测图片是否合法
        // bitmap 是大图，2M ，告警加载了大图， 图片大小只有 40 dp，
        // 网络或者本地的图片加载 200dp*200dp ，告警加载不合法
        // 不要影响性能，当然不能做到完全不影响，搞死循环，搞大内存，搞内存泄漏，不要影响主业务
        // 这就是为什么很多方案，需要线上线下采取不一样的策略去实现
        addImageLegalMonitor();
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        // 检测图片是否合法
        // bitmap 是大图，2M ，告警加载了大图， 图片大小只有 40 dp，
        // 网络或者本地的图片加载 200dp*200dp ，告警加载不合法
        addImageLegalMonitor();
    }

    /**
     * 添加图片合法监控
     */
    private void addImageLegalMonitor() {
        Looper.myQueue().removeIdleHandler(this);
        Looper.myQueue().addIdleHandler(this);
    }

    @Override
    public boolean queueIdle() {
        try {
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null) {
                checkIsLegal(drawable, "图片");
            }
            if (background != null) {
                checkIsLegal(background, "背景");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查是否合法
     */
    private void checkIsLegal(Drawable drawable, String tag) {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        // 大小告警判断
        int imageSize = calculateImageSize(drawable);
        if (imageSize > MAX_ALARM_IMAGE_SIZE) {
            Log.e(TAG, "图片加载不合法，" + tag + "大小 -> " + imageSize);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
        // 宽高告警判断
        if (MAX_ALARM_MULTIPLE * viewWidth < drawableWidth) {
            Log.e(TAG, "图片加载不合法, 控件宽度 -> " + viewWidth + " , " + tag + "宽度 -> " + drawableWidth);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
        if (MAX_ALARM_MULTIPLE * viewHeight < drawableHeight) {
            Log.e(TAG, "图片加载不合法, 控件高度 -> " + viewHeight + " , " + tag + "高度 -> " + drawableHeight);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
    }

    /**
     * 处理警告
     */
    private void dealWarning(int drawableWidth, int drawableHeight, int imageSize, Drawable drawable) {
        // 线上线下处理方式需要不一致，伪代码
        // 线下弹出提示窗口把信息输出，同时提供一个关闭打开开关
        // ......
        // 线上需要搜集代码信息，代码具体在哪里，把信息上报到服务器
        // ......
    }

    /**
     * 计算 drawable 的大小
     */
    private int calculateImageSize(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap.getByteCount();
        }
        int pixelSize = drawable.getOpacity() != PixelFormat.OPAQUE ? 4 : 2;
        return pixelSize * drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight();
    }
}

