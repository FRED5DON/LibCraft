package ts.app.sagosoft.com.libcraft.activities.motion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ts.app.sagosoft.com.libcraft.activities.BaseActivity;

/**
 * Created by fred on 16/8/31.
 */
public class MotionEventActivity extends BaseActivity {


    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, MotionEventActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewRoot viewroot = new ViewRoot(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewroot.setLayoutParams(params);

        ViewText vt = new ViewText(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vt.setLayoutParams(param);
        vt.setText("TEXT\nTEXT\nTEXT\nTEXT");
        vt.setPadding(30, 30, 30, 30);
        viewroot.addView(vt);
        setContentView(viewroot);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("fred", "Activity dispatchTouchEvent :: " + Tool.actionToString(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("fred", "Activity onTouchEvent :: " + Tool.actionToString(event.getAction()));
        return super.onTouchEvent(event);
    }
}


class ViewRoot extends LinearLayout {

    public ViewRoot(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.BLACK);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("fred", "LinearLayout dispatchTouchEvent:: " + Tool.actionToString(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("fred", "LinearLayout onTouchEvent:: " + Tool.actionToString(event.getAction()));
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d("fred", "LinearLayout onInterceptTouchEvent:: " + Tool.actionToString(event.getAction()));
        return super.onInterceptTouchEvent(event);
    }
}

class ViewText extends TextView {

    public ViewText(Context context) {
        super(context);
        setBackgroundColor(Color.RED);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("fred", "TextView dispatchTouchEvent:: " + Tool.actionToString(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("fred", "TextView onTouchEvent:: " + Tool.actionToString(event.getAction()));
        return false;
    }

}

class Tool {
    public static String actionToString(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
            case MotionEvent.ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case MotionEvent.ACTION_SCROLL:
                return "ACTION_SCROLL";
            case MotionEvent.ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case MotionEvent.ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
            case MotionEvent.ACTION_BUTTON_PRESS:
                return "ACTION_BUTTON_PRESS";
            case MotionEvent.ACTION_BUTTON_RELEASE:
                return "ACTION_BUTTON_RELEASE";
        }
        int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN(" + index + ")";
            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP(" + index + ")";
            default:
                return Integer.toString(action);
        }
    }
}
 