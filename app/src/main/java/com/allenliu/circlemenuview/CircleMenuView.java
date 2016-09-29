
package com.allenliu.circlemenuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import static android.R.attr.path;
import static android.os.Build.VERSION_CODES.M;


public class CircleMenuView extends View {
    private Canvas cancas;
    /**
     * 绘制图片的paint
     */
    private Paint textPaint;
    /**
     * 绘制内圆的画笔
     */
    private Paint mCirclePaint;
    /**
     * 绘制外圆的画笔
     */
    private Paint mWaiCirclePaint;
    /**
     * 最外层园的半径
     */
    private int radius;
    /**
     * 外圆半径
     */
    private int waiRaidus;
    /**
     * 内圆半径
     */
    private int neiRadius;
    /**
     * 弧形的开始角度
     */
    private int startangle = 0;

    /**
     * 盘块的个数
     */
    private int mcount = 6;
    /**
     * 角度增量
     */
    private int deltaAngle = 60;
    /**
     * 盘块的范围
     */
    RectF range;
    /**
     * 边距
     */
    private int minpadding;
    /**
     * 绘制弧形的画笔
     */
    private Paint mArcPaint;
    /**
     * 背景图
     */
    /**
     * 绘制分割矩形
     */
    private float textsize = sp2px(getContext(), 12);
    /**
     * 分割矩形的画笔
     */
    private Paint rectPaint;
    /**
     * 中心点X，Y坐标
     */
    private int x;
    private int y;
    private int bitMap[];
    private CharSequence text[];
    /**
     * 分割的偏移量
     */
    private int deltaPadding = dip2px(getContext(), 10);
    /**
     * 画布宽度
     */
    private int width;
    /**
     * 扫边画笔
     *
     * @param context
     * @param attrs
     */
    private Paint strokePaint;
    /**
     * 每个盘块的背景色
     */
    private int menuItemBackground;
    /**
     * 扫边颜色
     */
    private int strokeColor;
    /**
     * 空隙背景颜色
     */
    private int gapColor;
    /**
     * 扫边宽度
     */
    private float strokeWidth;
    private String centerText;
    private Drawable centerIcon;
    /**
     * 菜单字体颜色
     */
    private int menuTextColor;
    private onYuanPanClickListener listener;

    // 回调用两參构造
    public CircleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO 自动生成的构造函数存根
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if(attrs!=null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleMenuView);
            menuItemBackground = typedArray.getColor(R.styleable.CircleMenuView_menu_item_background, getResources().getColor(android.R.color.white));
            strokeColor = typedArray.getColor(R.styleable.CircleMenuView_stroke_color, getResources().getColor(R.color.default_stroke_color));
            gapColor = typedArray.getColor(R.styleable.CircleMenuView_gap_color, getResources().getColor(R.color.default_backgrouncolor));
            strokeWidth = typedArray.getDimension(R.styleable.CircleMenuView_stroke_width, 3);
            textsize = typedArray.getDimension(R.styleable.CircleMenuView_menu_text_size, sp2px(getContext(), 12));
            text = typedArray.getTextArray(R.styleable.CircleMenuView_menu_text);
            centerText = typedArray.getString(R.styleable.CircleMenuView_center_text);
            centerIcon = typedArray.getDrawable(R.styleable.CircleMenuView_center_icon);
            neiRadius = (int) typedArray.getDimension(R.styleable.CircleMenuView_inside_cirle_radius, 0);
            menuTextColor = typedArray.getColor(R.styleable.CircleMenuView_menu_text_color, getResources().getColor(android.R.color.black));
            deltaPadding = (int) typedArray.getDimension(R.styleable.CircleMenuView_gap_size, dip2px(getContext(), 10));
            TypedArray ar = getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.CircleMenuView_menu_icon, 0));
            int len = ar.length();
            bitMap = new int[len];
            for (int i = 0; i < len; i++)
                bitMap[i] = ar.getResourceId(i, 0);
            typedArray.recycle();
            ar.recycle();
        }else{
            menuItemBackground= getResources().getColor(android.R.color.white);
            strokeColor=getResources().getColor(R.color.default_stroke_color);
            gapColor=getResources().getColor(R.color.default_backgrouncolor);
            strokeWidth=3;
            textsize=12;
            centerText="";
            centerIcon=getResources().getDrawable(R.mipmap.ic_launcher);
            neiRadius=0;
            menuTextColor= getResources().getColor(android.R.color.black);
            deltaPadding= dip2px(getContext(), 10);
        }

    }

    public CircleMenuView(Context context) {
        super(context, null);
        // TODO 自动生成的构造函数存根
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        initAttrs(context, null);
    }


    private void initSomeThing() {
        // TODO 自动生成的方法存根
        //图片和文字数组不一致 取小的
        int length = bitMap.length > text.length ? text.length : bitMap.length;
        mcount = length;
        deltaAngle = 360 / mcount;
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setColor(menuItemBackground);
        mArcPaint.setStyle(Paint.Style.FILL);// 设置画笔为填充
        range = new RectF(minpadding, minpadding, minpadding + radius,
                minpadding + radius);

        // 初始化内圆画笔

        // 初始化外圆画笔
        mWaiCirclePaint = new Paint();
        mWaiCirclePaint.setAntiAlias(true);
        mWaiCirclePaint.setDither(true);
        mWaiCirclePaint.setColor(gapColor);
        mWaiCirclePaint.setStyle(Paint.Style.FILL);
//		mWaiCirclePaint.setXfermode(new PorterDuffXfermode(
//				PorterDuff.Mode.CLEAR));

        // 绘制内圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(menuItemBackground);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint
                .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 初始化分割矩形
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setDither(true);
        rectPaint.setColor(gapColor);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 初始化绘制图片的paint
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(menuTextColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textsize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        /**
         * 初始化扫边画笔
         */
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setDither(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setColor(strokeColor);
    }

    /**
     * 绘制操作
     */
    private void adraw(Canvas cancas) {
        // dosomething
        this.cancas = cancas;
        cancas.drawColor(gapColor);
        drawCircle();
        drawWaiCicrle();
        drawNeiCicle();
        drawStrokeLine();
        drawRects();
        drawIcon();
    }

    /**
     * 绘制icon
     */
    private void drawIcon() {
        // TODO 自动生成的方法存根
        // 将旋转之后的画布恢复
        cancas.restore();
        for (int i = 0; i < mcount; i++) {
            int imgWidth = radius / 5;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    bitMap[i]);
            float angle = (float) ((startangle + 360 / mcount / 2) * Math.PI / 180);
            int x = (int) (width / 2 + radius * 3 / 4 * Math.cos(angle));
            int y = (int) (width / 2 + radius * 3 / 4 * Math.sin(angle));
            int offset = dip2px(getContext(), 5);
            Rect rect = new Rect(x - imgWidth / 2 + offset, y - imgWidth / 2 + offset - dip2px(getContext(), 10), x
                    + imgWidth / 2 - offset, y + imgWidth / 2 - offset - dip2px(getContext(), 10));
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setDither(true);
            cancas.drawBitmap(bitmap, null, rect, p);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textW = fontMetrics.descent - fontMetrics.top;
            cancas.drawText(String.valueOf(text[i]), x, (float) (y + textW * 1.5), textPaint);
            startangle = startangle + deltaAngle;
        }
        // 绘制内圆图片
        int imgwidth = radius / 5;
        Rect rects = new Rect(width / 2 - imgwidth / 2, width / 2 - imgwidth,
                width / 2 + imgwidth / 2, width / 2);

        cancas.drawBitmap(
                ((BitmapDrawable) centerIcon).getBitmap(), null,
                rects, null);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textW = fontMetrics.descent - fontMetrics.top;
        cancas.drawText(centerText,
                x, y + textW
                ,
                textPaint);
    }

    private void drawStrokeLine() {
        cancas.drawCircle(x, y, radius, strokePaint);
        cancas.drawCircle(x, y, neiRadius, strokePaint);
        cancas.drawCircle(x, y, waiRaidus, strokePaint);

    }
    /**
     *
     */
    /**
     * 绘制 圆形
     */
    private void drawCircle() {
        cancas.drawCircle(x, y, radius, mArcPaint);
    }

    /**
     * 绘制内圆
     */
    private void drawNeiCicle() {
        cancas.drawCircle(x, y, neiRadius, mCirclePaint);

    }

    /**
     * 绘制外圆 透明色
     */
    private void drawWaiCicrle() {
        cancas.drawCircle(x, y, waiRaidus, mWaiCirclePaint);
    }

    /**
     * 绘制分割矩形
     */
    private void drawRects() {
        // 绘制左边分割矩形
        cancas.save();
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(gapColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int size = mcount / 2;
        for (int i = 0; i < size; i++) {
            RectF ff = new RectF(minpadding - 4, (y - deltaPadding / 2) + 2, radius
                    - waiRaidus + minpadding + 4, (y + deltaPadding / 2) - 2);

            RectF f = new RectF(minpadding, y - deltaPadding / 2, radius
                    - waiRaidus + minpadding, y + deltaPadding / 2);

            cancas.drawRect(f, rectPaint);
            cancas.drawRect(f, strokePaint);
            cancas.drawRect(ff, paint);
            // 绘制右边分割矩形
            RectF ff2 = new RectF(x + waiRaidus - 4, y - deltaPadding / 2 + 2, width
                    - minpadding + 4, y + deltaPadding / 2 - 2);
            RectF f2 = new RectF(x + waiRaidus, y - deltaPadding / 2, width
                    - minpadding, y + deltaPadding / 2);
            cancas.drawRect(f2, rectPaint);
            cancas.drawRect(f2, strokePaint);
            cancas.drawRect(ff2, paint);
            // 将画布旋转60在绘制
            if (i != size - 1)
                cancas.rotate(deltaAngle, x, y);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //将画布设为正方形
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        if (mWidth > mHeight) {
            mWidth = mHeight;
        }
        width = mWidth;
        minpadding = getPaddingLeft();
        // 半径
        radius = (width - minpadding * 2) / 2;
        x = width / 2;
        y = x;
        if (neiRadius == 0) {
            waiRaidus = radius / 3 + deltaPadding;
            neiRadius = waiRaidus - deltaPadding;
        } else {
            waiRaidus = neiRadius + deltaPadding;
        }
        // 设置画布宽高
        setMeasuredDimension(width, width - minpadding + 3
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算坐标范围，判断坐标在那个范围内

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 内圆点击事件

                return true;
            case MotionEvent.ACTION_MOVE:

                return true;
            case MotionEvent.ACTION_UP:

                float delta = (float) (Math.PI * deltaAngle / 180);

                Region[] regions = new Region[mcount + 1];
                float startAngele = 0;
                for (int i = 0; i < mcount + 1; i++) {
                    Region re = new Region();
                    Path path = new Path();
                    if (i != 0) {
                        path.moveTo((float) (x - waiRaidus * Math.cos(startAngele)), (float) (y - waiRaidus * Math.sin(startAngele)));
                        path.lineTo((float) (x - waiRaidus * Math.cos(startAngele + delta)), (float) (y - waiRaidus * Math.sin(startAngele + delta)));
                        path.lineTo((float) (x - radius * Math.cos(startAngele + delta)), (float) (y - radius * Math.sin(startAngele + delta)));
                        path.lineTo((float) (x - radius * Math.cos(startAngele)), (float) (y - radius * Math.sin(startAngele)));
                        path.close();
                    } else {
                        path.addCircle(x, y, neiRadius, Path.Direction.CW);
                    }
                    //构造一个区域对象，左闭右开的。
                    RectF r = new RectF();
                    //计算控制点的边界
                    path.computeBounds(r, true);
                    //设置区域路径和剪辑描述的区域
                    re.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
                    regions[i] = re;
                    startAngele = startAngele + delta;
                }
                float x = event.getX();
                float y = event.getY();
                for (int i = 0; i < regions.length; i++) {
                    if (regions[i].contains((int) x, (int) y)) {
                        if (listener != null) {
                            listener.onClick(this, i);
                        }
                        //Toast.makeText(getContext(), "" + i, 0).show();
                    }
                }
                return true;
        }
        return true;
    }

    public void setOnClickListener(onYuanPanClickListener l) {
        listener = l;
    }

    public interface onYuanPanClickListener {
        void onClick(View v, int position);
    }

    /* （非 Javadoc）
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        initSomeThing();
        adraw(canvas);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int getInsideCircleRadius() {
        return neiRadius;
    }

    public CircleMenuView setInsideCircleRadius(int neiRadius) {
        this.neiRadius = neiRadius;
        postInvalidate();
        return this;
    }

    public float getMenuTextSize() {
        return textsize;
    }

    public CircleMenuView setMenuTextSize(float textsize) {
        this.textsize = textsize;
        postInvalidate();
        return this;
    }

    public int[] getMenuIcons() {
        return bitMap;
    }

    public CircleMenuView setMenuIcons(int[] bitMap) {
        this.bitMap = bitMap;
        postInvalidate();
        return this;
    }

    public CharSequence[] getMenuTexts() {
        return text;
    }

    public CircleMenuView setMenuTexts(CharSequence[] text) {
        this.text = text;
        postInvalidate();
        return this;
    }

    public int getGapSize() {
        return deltaPadding;
    }

    public CircleMenuView setGapSize(int deltaPadding) {
        this.deltaPadding = deltaPadding;
        postInvalidate();
        return this;
    }

    public int getMenuItemBackground() {
        return menuItemBackground;
    }

    public CircleMenuView setMenuItemBackground(int menuItemBackground) {
        this.menuItemBackground = menuItemBackground;
        postInvalidate();
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public CircleMenuView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        postInvalidate();
        return this;
    }

    public int getGapColor() {
        return gapColor;
    }

    public CircleMenuView setGapColor(int gapColor) {
        this.gapColor = gapColor;
        postInvalidate();
        return this;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public CircleMenuView setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();
        return this;
    }

    public String getCenterText() {
        return centerText;
    }

    public CircleMenuView setCenterText(String centerText) {
        this.centerText = centerText;
        postInvalidate();
        return this;
    }

    public Drawable getCenterIcon() {
        return centerIcon;
    }

    public CircleMenuView setCenterIcon(Drawable centerIcon) {
        this.centerIcon = centerIcon;
        postInvalidate();
        return this;
    }

    public int getMenuTextColor() {
        return menuTextColor;
    }

    public CircleMenuView setMenuTextColor(int menuTextColor) {
        this.menuTextColor = menuTextColor;
        postInvalidate();
        return this;
    }
    public CircleMenuView setWidthAndHeight(int w,int h){
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(w,h);
        setLayoutParams(params);
        postInvalidate();
        return this;
    }
}



