package com.ShengYiZhuanJia.pad.view;

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

    private void init() {
        clearVisible = false;

        paint = new Paint();
        paint.setAntiAlias(true);

        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_close);
    }

    private boolean clearVisible;
    private Bitmap bm;
    private float top;
    private float left;
    private Paint paint;

    public void setClearBitMap(@IdRes int id) {
        bm = BitmapFactory.decodeResource(getResources(), id);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (clearVisible) {
            top = (getHeight() - bm.getHeight()) / 2;
            left = getWidth() - bm.getWidth() - top;
            canvas.drawBitmap(bm, left, top, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > left - top && event.getX() < getWidth();
            if (touchable)
                setText("");
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (null != listener)
            listener.onTextChanged(s, start, count, after);

        clearVisible = s.length() > 0;
        invalidate();
    }

    private OnTextChangedListener listener;

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.listener = listener;
    }

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