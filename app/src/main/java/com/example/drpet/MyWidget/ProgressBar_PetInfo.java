package com.example.drpet.MyWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/6/5 0005.
 */
public class ProgressBar_PetInfo extends ProgressBar{



    private String text_peogress;
    private Paint paint_white;

    public ProgressBar_PetInfo(Context context) {
        super(context);
        initPaint();
    }

    public ProgressBar_PetInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressBar_PetInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        setTextProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        paint_white.getTextBounds(text_peogress, 0,text_peogress.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(text_peogress, x, y, paint_white);
    }

    private void initPaint(){
        paint_white = new Paint();
        paint_white.setTextSize(25);
        paint_white.setAntiAlias(true);
        paint_white.setColor(Color.WHITE);
    }
    private void setTextProgress(int progress){
        progress = progress * 100 / getMax();
        text_peogress = String.valueOf(progress) + " / 100";
    }
}
