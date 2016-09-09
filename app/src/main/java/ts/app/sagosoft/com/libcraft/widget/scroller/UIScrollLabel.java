package ts.app.sagosoft.com.libcraft.widget.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import android.widget.TextView;

/**
 * Created by fred on 16/8/31.
 */
public class UIScrollLabel extends TextView {
    private final OverScroller mScroller;
    private float startX, startY, lastX, lastY;

    public UIScrollLabel(Context context) {
        this(context, null);
    }

    public UIScrollLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIScrollLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context, new LinearInterpolator());
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
                offsetTopAndBottom((int) disY);
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX),
                        -(int) (getY() - startY));
                invalidate();
                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
//            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }


}
