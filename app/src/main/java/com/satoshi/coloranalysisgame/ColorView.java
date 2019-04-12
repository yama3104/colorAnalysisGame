package com.satoshi.coloranalysisgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ColorView extends View {
    Paint paint;
    int dispWidth, dispHeight, dispCenterX;
    int r_now=0, g_now=0, b_now=0;
    int mode=1;

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setWillNotDraw(false);
        paint = new Paint();
        //paint1 = new Paint();
        dispWidth = getDispSize(context).x;
        dispHeight = getDispSize(context).y;
        dispCenterX = dispWidth/2;
    }


    @Override
    public void onDraw(Canvas canvas) {
        Log.d("ondraw", "ondraw");

        canvas.drawColor(Color.WHITE);

        canvas.drawColor(paint.getColor());

        //paint.setStyle(Paint.Style.FILL);
        /*switch (mode){
            case 1:
                canvas.drawRect(dispCenterX - dispWidth/3, dispHeight/10,
                        dispCenterX + dispWidth/3, dispHeight/10 + dispWidth/2, paint);
                break;
            case 2:
                canvas.drawRect(dispCenterX/2 - dispWidth/6, dispHeight/10,
                        dispCenterX/2 + dispWidth/6, dispHeight/10 + dispWidth/2, paint);
                canvas.drawRect(dispCenterX*3/2 - dispWidth/6, dispHeight/10,
                        dispCenterX*3/2 + dispWidth/6, dispHeight/10 + dispWidth/2, paint1);
                break;
        }*/

        //canvas.drawRect(500, 100, 800, 400, paint1);
        //drawRect(canvas,paint);
    }

    public void setColor(int r, int g, int b){
        //Log.d("getcolor",""+paint.getColor());
        r_now = r;
        g_now = g;
        b_now = b;
        paint.setColor(Color.rgb(r, g, b));
        invalidate();
    }

    public int[] getColor(){
        return new int[]{r_now, g_now, b_now};
    }

    public void setRed(int r){
        r_now = r;
        paint.setColor(Color.rgb(r, g_now, b_now));
        invalidate();
    }

    public void setGreen(int g){
        g_now = g;
        paint.setColor(Color.rgb(r_now, g, b_now));
        invalidate();
    }

    public void setBlue(int b){
        b_now = b;
        paint.setColor(Color.rgb(r_now, g_now, b));
        invalidate();
    }

    public void setMode(int m){
        mode = m;
        invalidate();
    }

    public Point getDispSize(Context context){
        WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(winMan == null) return new Point(0,0);
        Display disp = winMan.getDefaultDisplay();

        Point point = new Point();
        disp.getSize(point);

        return point;
    }

    public void drawRect(Canvas canvas, Paint paint){
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(100, 100, 400, 400, paint);

    }
}
