package ts.app.sagosoft.com.libcraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UINoticeViewBuilder {

    private View msgView;
    private static UINoticeViewBuilder uITopToast;
    /**
     * 包含所有基于msgView 子view id的view引用
     */
    private ViewHolder childrenView;

    private static float lastY;

    private static final int DURATION_IN = 400;
    private static final int DURATION_OUT = 400;

    /**
     * 默认持续显示时间
     */
    private static final long IOS_NOTICE_DURATION = 8000;
    /**
     * 手动干扰后，默认下一次动画的等待时间系数 值越大 时间越短
     */
    private final int HalfWaitPara = 1;

    private final int OFFSET = 20;


    private boolean isActive = false;
    private boolean outofCtrl = false;
    private long real_notice_duration = IOS_NOTICE_DURATION;

    private UINoticeViewBuilder() {

    }

    public static UINoticeViewBuilder getInstance() {
        if (uITopToast == null) {
            uITopToast = new UINoticeViewBuilder();
        }
        return uITopToast;
    }


    /**
     * 卸载msgView
     * 当启动新的Activity时（并且没有使用者持有msgView）建议先调用此方法
     */
    public void uninstall() {
        if(msgView!=null ){
            ((ViewGroup) msgView.getParent()).removeView(msgView);
        }
    }


    public void destroy() {
        if (msgView != null) {
            msgView.clearAnimation();
            msgView.removeCallbacks(runnable);
            uninstall();
        }
        uITopToast = null;
        childrenView = null;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 创建UITopToast
     *
     * @param context
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     * @return
     */
    public UINoticeViewBuilder build(final Activity context, final Intent intent, Drawable icon, String title, String content) {
        if (context == null) {
            return null;
        }
        if (msgView != null) {
            uninstall();
        } else {
            //FIXME 暂时无法缓存msgView
        }
        msgView = LayoutInflater.from(context).inflate(R.layout.top_toast, null);
        msgView.setVisibility(View.GONE);
        childrenView = new ViewHolder(msgView);
        ((View) childrenView.btToastContentPushHandle.getParent()).setVisibility(View.VISIBLE);
        ((View) childrenView.btToastContentPushHandle.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getRawY();
                float dis = Math.abs(lastY - y);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastY = y;
                    msgView.removeCallbacks(runnable);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (lastY > y) {
                        msgView.setY(-dis);
                        if (dis > 2.5 * OFFSET) {
                            outofCtrl = true;
                        } else {
                            outofCtrl = false;
                        }
                        return true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (outofCtrl) {
                        hideWithAnimation();
                    } else {
                        msgView.setTranslationY(0);
                        hideWithMills(real_notice_duration / HalfWaitPara);
                    }
                }
                return false;
            }
        });
        msgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uITopToast.onUITopToastAnimationEvent != null) {
                    uITopToast.onUITopToastAnimationEvent.OnUITopToastClickEvent(msgView);
                }
                hideWithoutAnimation();
                if (intent != null) {
                    context.startActivity(intent);
                }

            }
        });
        /*msgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uITopToast.onUITopToastAnimationEvent != null) {
                    uITopToast.onUITopToastAnimationEvent.OnUITopToastClickEvent(msgView);
                }
                hideWithAnimation();
            }
        });*/
        if (icon != null) {
            childrenView.ivTopToastIcon.setImageDrawable(icon);
        }
        if (title != null) {
            childrenView.tvTopToastTitle.setText(title);
        }
        if (content != null) {
            childrenView.tvTopToastContent.setText(content);
        }
        ViewGroup.LayoutParams layoutparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        context.getWindow().addContentView(msgView, layoutparams);
        return this;
    }


    /**
     * UINoticeView 【2】
     *
     * @param context
     * @param intent
     * @param icon
     * @param title
     * @param content
     * @return
     */
    public UINoticeViewBuilder build(final Activity context, final Intent intent, String icon, String title, final String content) {
        if (context == null) {
            return null;
        }
        if (msgView != null) {
            uninstall();
        } else {
            //FIXME 暂时无法缓存msgView
        }
        this.msgView = LayoutInflater.from(context).inflate(R.layout.top_toast, null);
        childrenView = new ViewHolder(msgView);
        Log.d("TAG", "on1: "+msgView);
        ViewGroup.LayoutParams layoutparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        context.getWindow().addContentView(msgView, layoutparams);
        Log.d("TAG", "on2: " + msgView);
        ((View) childrenView.btToastContentPushHandle.getParent()).setVisibility(View.VISIBLE);
        ((View) childrenView.btToastContentPushHandle.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getRawY();
                float dis = Math.abs(lastY - y);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastY = y;
                    msgView.removeCallbacks(runnable);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (lastY > y) {
                        msgView.setY(-dis);
                        if (dis > 2.5 * OFFSET) {
                            outofCtrl = true;
                        } else {
                            outofCtrl = false;
                        }
                        return true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (outofCtrl) {
                        hideWithAnimation();
                    } else {
                        msgView.setTranslationY(0);
                        hideWithMills(real_notice_duration / HalfWaitPara);
                    }
                }
                return false;
            }
        });
        msgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uITopToast.onUITopToastAnimationEvent != null) {
                    uITopToast.onUITopToastAnimationEvent.OnUITopToastClickEvent(msgView);
                }
                hideWithoutAnimation();
                if (intent != null) {
                    context.startActivity(intent);
                }
            }
        });
        if (icon != null && icon.length() > 0) {
//            ImageLoader.getInstance().displayImage(icon, childrenView.ivTopToastIcon);
        }
        if (title != null) {
            childrenView.tvTopToastTitle.setText(title);
        }
        if (content != null) {
            childrenView.tvTopToastContent.setText(content);
        }
        return this;
    }


    /**
     * 显示下拉中隐藏的内容
     */
    private void showMoreContent() {
        ((View) childrenView.btToastContentPushHandle.getParent()).setVisibility(View.GONE);
    }


    /**
     * 显示顶部toast
     */
    public synchronized UINoticeViewBuilder showWithAnimation() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f
        );
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                msgView.setVisibility(View.VISIBLE);
                if (onUITopToastAnimationEvent != null) {
                    onUITopToastAnimationEvent.OnUITopToastAnimationEventStart(msgView, View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onUITopToastAnimationEvent != null) {
                    onUITopToastAnimationEvent.OnUITopToastAnimationEventEnd(msgView, View.VISIBLE);
                }
                isActive = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        animation.setDuration(DURATION_IN);
        animation.setFillAfter(true);
        msgView.startAnimation(animation);
        return uITopToast;
    }

    /**
     * 隐藏顶部toast
     */
    public synchronized void hideWithAnimation() {
        msgView.removeCallbacks(runnable);
        if (msgView.getVisibility() == View.GONE) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f
        );
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (onUITopToastAnimationEvent != null) {
                    onUITopToastAnimationEvent.OnUITopToastAnimationEventStart(msgView, View.GONE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onUITopToastAnimationEvent != null) {
                    onUITopToastAnimationEvent.OnUITopToastAnimationEventEnd(msgView, View.GONE);
                }
                msgView.setVisibility(View.GONE);
                isActive = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        animation.setDuration(DURATION_OUT);
        msgView.startAnimation(animation);
    }

    public synchronized void hideWithoutAnimation() {
        msgView.clearAnimation();
        msgView.setVisibility(View.GONE);
    }

    public synchronized void showWithoutAnimation() {
        msgView.setVisibility(View.VISIBLE);
    }


    /**
     * mills 后隐藏[设置0为默认时间10s]
     *
     * @param mills
     */
    public void hideWithMills(long mills) {
        if (msgView == null) {
            return;
        }
        if (mills == 0) {
            mills = IOS_NOTICE_DURATION;
        }
        if (mills == -1) {
            mills = Long.MAX_VALUE;
        }
        real_notice_duration = mills;
        msgView.removeCallbacks(runnable);
        msgView.postDelayed(runnable, real_notice_duration);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hideWithAnimation();
        }
    };

    public OnUITopToastAnimationEvent onUITopToastAnimationEvent;

    public interface OnUITopToastAnimationEvent {

        /**
         * @param root
         * @param type VISIBLE||GONE
         */
        void OnUITopToastAnimationEventStart(View root, int type);


        /**
         * @param root
         * @param type VISIBLE||GONE
         */
        void OnUITopToastAnimationEventEnd(View root, int type);

        /**
         * 点击整个view
         *
         * @param root
         */
        void OnUITopToastClickEvent(View root);

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'top_toast.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.iv_top_toast_icon)
        ImageView ivTopToastIcon;
        @InjectView(R.id.tv_top_toast_title)
        TextView tvTopToastTitle;
        @InjectView(R.id.tv_top_toast_content)
        TextView tvTopToastContent;
        @InjectView(R.id.bt_toast_content_push_handle)
        TextView btToastContentPushHandle;


        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}