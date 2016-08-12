package ts.app.sagosoft.com.libcraft.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by fred on 16/8/11.
 */
public class AnimatorActivity extends BaseActivity {

    @InjectView(R.id.tv_alert)
    TextView tvStart;


    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, AnimatorActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_activity);
        ButterKnife.inject(this);
        size = tvStart.getTextScaleX();
    }

    @OnClick(R.id.tv_start)
    void onClickTv(View v) {
        mkAnimatorToAlertView(tvStart,100F);
    }


    float size = 12F;

    private synchronized void mkAnimatorToAlertView(TextView child,float childHeight) {
        child.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(child, "translationY", - childHeight, 0);
        //动画下部的阴影这里使用ofFloat及参数scaleX来进行X轴的缩放，02f-1是缩放比例 阴影在20%到100%之间变化
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(child, "alpha", 0.5f, 1);
        ObjectAnimator textScale = ObjectAnimator.ofFloat(child, "textScaleX", 1f, 1.3f, 1f);
        textScale.setDuration(200);
        //设置animation的持续时间，通过setDuration.
        objectAnimator.setDuration(600);
//        objectAnimator1.setDuration(300);
        ObjectAnimator objectAnimatorUp = ObjectAnimator.ofFloat(child, "translationY", 0, - childHeight);
        objectAnimatorUp.setStartDelay(1200);
        objectAnimatorUp.setDuration(400);
        //设置一个减速插值器
        objectAnimator.setInterpolator(new DecelerateInterpolator(1));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        //animatorSet的方法playtogther让三个动画同时运行
        ((animatorSet.play(objectAnimator).with(scaleIndication)).before(textScale)).before(objectAnimatorUp);
        animatorSet.start();
    }
}
