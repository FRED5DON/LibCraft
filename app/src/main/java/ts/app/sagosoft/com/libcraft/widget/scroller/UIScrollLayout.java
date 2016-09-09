package ts.app.sagosoft.com.libcraft.widget.scroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by fred on 16/8/31.
 */
public class UIScrollLayout extends RelativeLayout {
    private final OverScroller mScroller;


    private final int mTouchSlop;                               //表示滑动的时候，手的移动要大于这个距离才开始移动控件
    private final int mMaximumVelocity, mMinimumVelocity;       //最大、最小触发速度
    private int stickyLayoutId = -1;                            //获取需要固定的layout id
    private View stickyLayout;                                  //需要固定的layout
    private float startX, startY, lastX, lastY;
    private boolean stickyLayoutState;                          //固定的layout 当前状态 true：sticky  false : free
    private int stickyIndex;
    private int beforeStickyHeight;
    private View bottomView;
    private int[] initialParams;

    public UIScrollLayout(Context context) {
        this(context, null);
    }

    public UIScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context, new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.UIScrollLayoutAttr);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.UIScrollLayoutAttr_childStickyResId:

                    this.stickyLayoutId = typedArray.getResourceId(index, -1);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (stickyLayoutId != -1) {
            stickyLayout = findViewById(stickyLayoutId);
            initialParams = ((RelativeLayout.LayoutParams) stickyLayout.getLayoutParams()).getRules();
            if (stickyLayout == null) {
                return;
            }
            stickyIndex = indexOfChild(stickyLayout);
            stickyLayoutState = false;
            bottomView = getChildAt(stickyIndex + 1);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bottomView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight() - stickyLayout.getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    private int getStickyBeforeHeight(int index) {
        int height = 0;
        for (int i = 0; i < index; i++) {
            height += getChildAt(i).getMeasuredHeight();
        }

        return height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
//                offsetLeftAndRight((int) disX);
                if (Math.abs(disY) > mTouchSlop) {
                    if (disX < beforeStickyHeight) {
                        offsetTopAndBottom((int) disY);
                    } else {
                        if (disX - beforeStickyHeight < 5) {
                            LayoutParams layoutParams = (LayoutParams) stickyLayout.getLayoutParams();
                            layoutParams.addRule(ALIGN_PARENT_TOP,TRUE);
                            stickyLayout.setLayoutParams(layoutParams);
                        }
                        return super.onInterceptTouchEvent(event);
                    }
                }

                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
//                mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX),
//                        -(int) (getY() - startY));
//                invalidate();
                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
//            setX(mScroller.getCurrX());
//            setY(mScroller.getCurrY());
//            invalidate();
        }
        super.computeScroll();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
        beforeStickyHeight = getStickyBeforeHeight(stickyIndex);
    }


}
