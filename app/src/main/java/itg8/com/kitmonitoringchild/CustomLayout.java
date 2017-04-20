package itg8.com.kitmonitoringchild;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Android itg 8 on 2/10/2017.
 */

public class CustomLayout extends RelativeLayout {

    private Canvas canvas;
    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomLayout drawCurvedArrow(int startX, int startY, float endX, float endY, int curveRadius, int padding, int color) {
        PointF mPoint1 = new PointF(startX, startY);
        PointF mPoint2 = new PointF(endX, endY);
        Paint paint  = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(12);
        paint.setColor(color);
        Path myPath = new Path();

        myPath.moveTo(startX, startY);
        myPath.quadTo(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y);
        canvas.drawPath(myPath, paint);

        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        int startX = getWidth();
//        int startY= getHeight();
//        float middleX=startX/2;
//        float  middleY= startY/2;
//        PointF mPoint1 = new PointF(startX, startY);
//        PointF mPoint2 = new PointF(middleX, middleY);
//        Paint paint  = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(12);
//        paint.setColor(Color.BLUE);
//        Path myPath = new Path();
//
//        myPath.moveTo(startX, startY);
//        myPath.quadTo(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y);
//        canvas.drawPath(myPath, paint);


        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }

        Path path = new Path();
        path.addCircle(width / 2,
                height / 2, radius,
                Path.Direction.CW);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        float center_x, center_y;
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);

        center_x = width / 2;
        center_y = height / 2;

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, 90, 180, true, paint);


    }
}
