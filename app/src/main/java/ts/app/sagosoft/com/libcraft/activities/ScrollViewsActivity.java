package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.activities.motion.MotionEventActivity;
import ts.app.sagosoft.com.libcraft.activities.scroll.ScrollerLayoutActivity;
import ts.app.sagosoft.com.libcraft.activities.scroll.ScrollersActivity;

/**
 * Created by FRED on 2016/1/21.
 */
public class ScrollViewsActivity extends BaseActivity {


    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, ScrollViewsActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollers);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.motion_event,R.id.button_sticky, R.id.button_absorb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.motion_event:
                startActivity(MotionEventActivity.mkIntent(this));
                break;
            case R.id.button_sticky:
                startActivity(ScrollersActivity.mkIntent(this));
                break;
            case R.id.button_absorb:
                startActivity(ScrollerLayoutActivity.mkIntent(this));
                break;
        }
    }
}
