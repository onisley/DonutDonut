package com.mobile.donut;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class GraphView extends View {

    // 수입그래프에 대한 변수들
    private ShapeDrawable mLineShape;
    private Paint mPointPaint;
    private float mThickness;
    private int[] mPoints, mPointX, mPointY;
    private int mPointSize, mPointRadius, mLineColor, mUnit, mOrigin, mDivide;

    // 지출그래프에 대한 변수들 - 식비, 여가비, 기타
    private int[] nPoints, nPointX, nPointY;
    private int nPointSize, nPointRadius, nLineColor, nUnit, nOrigin, nDivide;
    private float nThickness;
    private ShapeDrawable nLineShape;
    private Paint nPointPaint;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypes(context, attrs);
    }

    //그래프 옵션을 받는다
    private void setTypes(Context context, AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.GraphView);

        // 수입그래프에 대한 옵션
        mPointPaint = new Paint();
        mPointPaint.setColor(types.getColor(R.styleable.GraphView_pointColor_max, Color.BLACK));
        mPointSize = (int)types.getDimension(R.styleable.GraphView_pointSize, 10);
        mPointRadius = mPointSize / 2;

        mLineColor = types.getColor(R.styleable.GraphView_lineColor_max, Color.BLACK);
        mThickness = types.getDimension(R.styleable.GraphView_lineThickness, 1);

        // 수출그래프에 대한 옵션
        nPointPaint = new Paint();
        nPointPaint.setColor(types.getColor(R.styleable.GraphView_pointColor_min, Color.CYAN));
        nPointSize = (int)types.getDimension(R.styleable.GraphView_pointSize, 10);
        nPointRadius = mPointSize / 2;

        nLineColor = types.getColor(R.styleable.GraphView_lineColor_min, Color.CYAN);
        nThickness = types.getDimension(R.styleable.GraphView_lineThickness, 1);
    }

    // 수입그래프 정보를 받는다
    public void setPointsMax(int[] points, int unit, int origin, int divide) {
        mPoints = points;   //y축 값 배열

        mUnit = unit;       //y축 단위
        mOrigin = origin;   //y축 원점
        mDivide = divide;   //y축 값 갯수
    }
    // 수출그래프 정보를 받는다
    public void setPointsMin(int[] points, int unit, int origin, int divide) {
        nPoints = points;   //y축 값 배열

        nUnit = unit;       //y축 단위
        nOrigin = origin;   //y축 원점
        nDivide = divide;   //y축 값 갯수
    }

    //그래프를 만든다
    private void draw() {
        Path pathMax = new Path();  // 수입그래프
        Path pathMin = new Path();  // 수출그래프

        // 수입에 대한
        int height_max = getHeight();
        int[] points_max = mPoints;
        // 수출에 대한
        int height_min = getHeight();
        int[] points_min = nPoints;

        //x축 점 사이의 거리
        // 수입
        float gapx_max = (float)getWidth() / points_max.length;
        // 지출
        float gapx_min = (float)getWidth() / points_min.length;

        //y축 단위 사이의 거리
        // 수입
        float gapy_max = (height_max - mPointSize) / mDivide;
        float halfgab_max = gapy_max / 2;
        // 지출
        float gapy_min = (height_max - nPointSize) / nDivide;
        float halfgab_min = gapy_min / 2;

        // 수입
        int length_max = points_max.length;
        mPointX = new int[length_max];
        mPointY = new int[length_max];
        for(int i = 0 ; i < length_max ; i++) {
            //점 좌표를 구한다
            int x = (int)(halfgab_max + (i * gapx_max));
            int y = (int)(height_max - mPointRadius - (((points_max[i] / mUnit) - mOrigin) * gapy_max));

            mPointX[i] = x;
            mPointY[i] = y;

            //선을 그린다
            if(i == 0)
                pathMax.moveTo(x, y);
            else
                pathMax.lineTo(x, y);
        }
        // 지출
        int length_min = points_min.length;
        nPointX = new int[length_min];
        nPointY = new int[length_min];
        for(int i = 0 ; i < length_min ; i++) {
            //점 좌표를 구한다
            int x = (int)(halfgab_min + (i * gapx_min));
            int y = (int)(height_min - nPointRadius - (((points_min[i] / nUnit) - nOrigin) * gapy_min));

            nPointX[i] = x;
            nPointY[i] = y;

            //선을 그린다
            if(i == 0)
                pathMin.moveTo(x, y);
            else
                pathMin.lineTo(x, y);
        }

        //그려진 선으로 shape을 만든다
        // 수입
        ShapeDrawable shape_max = new ShapeDrawable(new PathShape(pathMax, 1, 1));
        shape_max.setBounds(0, 0, 1, 1);

        Paint paint_max = shape_max.getPaint();
        paint_max.setStyle(Paint.Style.STROKE);
        paint_max.setColor(mLineColor);
        paint_max.setStrokeWidth(mThickness);
        paint_max.setAntiAlias(true);

        mLineShape = shape_max;

        // 지출
        ShapeDrawable shape_min = new ShapeDrawable(new PathShape(pathMin, 1, 1));
        shape_min.setBounds(0, 0, 1, 1);

        Paint paint_min = shape_min.getPaint();
        paint_min.setStyle(Paint.Style.STROKE);
        paint_min.setColor(nLineColor);
        paint_min.setStrokeWidth(nThickness);
        paint_min.setAntiAlias(true);

        nLineShape = shape_min;
    }

    //그래프를 그린다(onCreate 등에서 호출시)
    // 수입
    public void drawForBeforeDrawViewMax() {
        //뷰의 크기를 계산하여 그래프를 그리기 때문에 뷰가 실제로 만들어진 시점에서 함수를 호출해 준다
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                draw();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    // 지출
    public void drawForBeforeDrawViewMin() {
        //뷰의 크기를 계산하여 그래프를 그리기 때문에 뷰가 실제로 만들어진 시점에서 함수를 호출해 준다
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                draw();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 수입
        //선을 그린다
        if(mLineShape != null)
            mLineShape.draw(canvas);

        //점을 그린다
        if(mPointX != null && mPointY != null) {
            int length = mPointX.length;
            for (int i = 0; i < length; i++)
                canvas.drawCircle(mPointX[i], mPointY[i], mPointRadius, mPointPaint);
        }
        // 지출
        //선을 그린다
        if(nLineShape != null)
            nLineShape.draw(canvas);

        //점을 그린다
        if(nPointX != null && mPointY != null) {
            int length = nPointX.length;
            for (int i = 0; i < length; i++)
                canvas.drawCircle(nPointX[i], nPointY[i], nPointRadius, nPointPaint);
        }
    }
}