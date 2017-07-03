package com.parfois.ClearEditText.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ShengYiZhuanJia.pad.R;

/**
 * 带清除按钮的EditText
 * Created by Parfois Meng on 2017/7/3/003.
 */

public class MyClearEditText extends EditText {

    public MyClearEditText(Context context) {
        super(context);
        init();
    }

    public MyClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean clearVisible;//清除按钮显隐

    private Paint paint;//画笔

    private Bitmap bm;//清除按钮图片bitmap
    private float top;//清除按钮图片距离控件顶部的距离，top
    private float left;//清除按钮图片距离控件左边的距离，left

    private OnTextChangedListener listener;//文字改变监听

    /**
     * 初始化
     */
    private void init() {
        clearVisible = false;//默认隐藏

        paint = new Paint();//new 画笔
        paint.setAntiAlias(true);//抗锯齿
        //drawBitmap，不需要颜色等设置

        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_close);//默认的图片bitmap
    }

    /**
     * 提供给外部设置图片的方法
     *
     * @param id 图片id
     */
    public void setClearBitMap(@IdRes int id) {
        bm = BitmapFactory.decodeResource(getResources(), id);
    }

    /**
     * 设置文字变化监听
     *
     * @param listener 监听器
     */
    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.listener = listener;
    }

    /**
     * onDraw时判断显隐，是否绘制清除图标
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 这里没有判断bitmap高大于控件高的情况
        // 有需要请加判断后做相应处理
        // PS：BitmapFactory.decodeResource(Resources res, int id, Options opts)
        //     判断请在onMeasure之后，否则getWidth()、getHeight()为0

        if (clearVisible) {//如果需要显示
            top = (getHeight() - bm.getHeight()) / 2;//上下居中
            left = getWidth() - bm.getWidth() - top;//右侧留有上下等边距
            canvas.drawBitmap(bm, left, top, paint);//绘制
        }
    }

    /**
     * 触摸监听
     * 在这里实现点击清空文字的效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {//触点抬起事件
            //X轴在图标左侧往左(上右下等边距距离)和控件最右侧之间
            //Y轴不用判断
            boolean touchable = event.getX() > left - top && event.getX() < getWidth();
            if (touchable) {//如果在响应区域内，文本置空
                setText("");
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 文字改变监听
     * 继承自TextView，protected方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (null != listener)//如果有监听器就回调
            listener.onTextChanged(s, start, count, after);

        clearVisible = s.length() > 0;//如果文字长度大于0，设置显示清除图标
        invalidate();//刷新，重走onDraw
    }

    /**
     * 文字改变监听，内部接口
     */
    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int count, int after);
    }

//    private Bitmap getBitmap(int id, int maxWidth, int maxHeight) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), id, options);
//        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(getResources(), id, options);
//    }
//
//    private int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
//        if (maxWidth == 0 || maxHeight == 0) return 1;
//        int height = options.outHeight;
//        int width = options.outWidth;
//        int inSampleSize = 1;
//        while ((height >>= 1) > maxHeight && (width >>= 1) > maxWidth) {
//            inSampleSize <<= 1;
//        }
//        return inSampleSize;
//    }
}
