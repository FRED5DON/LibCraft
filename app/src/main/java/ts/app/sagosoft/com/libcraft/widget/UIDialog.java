package ts.app.sagosoft.com.libcraft.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;

public class UIDialog extends AlertDialog {


    @InjectView(R.id.layout_content_view)
    LinearLayout layoutContentView;
    @InjectView(R.id.layout_content_body)
    LinearLayout layoutContentBody;
    @InjectView(R.id.tv_negativeText)
    TextView tvNegativeText;
    @InjectView(R.id.view_negativeText)
    RelativeLayout viewNegativeText;
    @InjectView(R.id.tv_neutralText)
    TextView tvNeutralText;
    @InjectView(R.id.view_neutralText)
    RelativeLayout viewNeutralText;
    @InjectView(R.id.tv_positiveText)
    TextView tvPositiveText;
    @InjectView(R.id.view_positiveText)
    RelativeLayout viewPositiveText;
    @InjectView(R.id.tv_content_body_title)
    TextView tvContentBodyTitle;
    @InjectView(R.id.tv_content_body_content)
    TextView tvContentBodyContent;
    private Builder builder;


    /**
     * 是默认还是用户自定义
     */
    private boolean viewTypeDefault = true;

    /**
     * 使用无Builder的构造方法 创建dialog等同 AlertDialog
     *
     * @param context
     */
    public UIDialog(Context context) {
        super(context);
    }

    public UIDialog(Builder builder) {
        super(builder.context);
        this.builder = builder;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (viewTypeDefault) {
            ButterKnife.reset(this);
        }
    }

    @Override
    public void show() {
        super.show();
        init();
    }

    public void init() {
        initCustom();
        renderData(builder);
    }

    /**
     * 获取默认的主体ViewParent id
     */
    public int getBodyView() {
        if (viewTypeDefault) {
            return R.id.layout_content_body;
        } else {
            return -1;
        }
    }


    /**
     * 替换默认的主体View
     */
    public UIDialog replaceBodyView(BodyAdapter bodyAdapter) {
        if (viewTypeDefault && layoutContentView != null && bodyAdapter != null) {
            layoutContentView.removeAllViews();
            View v = bodyAdapter.getView();
            if (v != null) {
                v.setId(R.id.layout_content_body);
                layoutContentView.addView(v);
                if (bodyAdapter.titleTextViewId() != 0) {
                    tvContentBodyTitle = (TextView) layoutContentView.findViewById(bodyAdapter.titleTextViewId());
                }
                if (bodyAdapter.contentTextViewId() != 0) {
                    tvContentBodyContent = (TextView) layoutContentView.findViewById(bodyAdapter.contentTextViewId());
                }
                renderData(this.builder);
            }

        }
        return this;
    }

    public interface BodyAdapter {
        View getView();

        int titleTextViewId();

        int contentTextViewId();
    }


    /**
     * ===============================  [private]   ======================================
     */

    private void renderData(Builder builder) {
        if (viewTypeDefault && builder != null) {
            if (tvContentBodyTitle != null && builder.title != null) {
                tvContentBodyTitle.setText(builder.title);
            }
            if (tvContentBodyContent != null && builder.content != null) {
                if (builder.content instanceof Spanned) {
                    tvContentBodyContent.setText((Spanned) builder.content);
                } else {
                    tvContentBodyContent.setText(builder.content);
                }
            }
            if (tvNeutralText != null) {
                if (builder.neutralText != null) {
                    tvNeutralText.setText(builder.neutralText);
                    tvNeutralText.setVisibility(View.VISIBLE);
                } else {
                    tvNeutralText.setVisibility(View.GONE);
                }
            }
            if (tvNegativeText != null) {
                if (builder.negativeText != null) {
                    tvNegativeText.setText(builder.negativeText);
                    tvNegativeText.setVisibility(View.VISIBLE);
                } else {
                    tvNegativeText.setVisibility(View.GONE);
                }
            }
            if (tvPositiveText != null) {
                if (builder.positiveText != null) {
                    tvPositiveText.setText(builder.positiveText);
                    tvPositiveText.setVisibility(View.VISIBLE);
                } else {
                    tvPositiveText.setVisibility(View.GONE);
                }
            }


        }
    }

    //如果需要自定义全部view
    private void initCustom() {
        if (builder == null) return;
        viewTypeDefault = builder.view == null;
        if (!viewTypeDefault) {
            int count = 0;
            if (builder.buttons != null) {
                count = builder.buttons.length;
            }
            //设置按钮事件
            for (int i = 0; i < count; i++) {
                if (i == Builder.BUTTON_MAX_COUNT) {
                    break;
                }
                View button = builder.buttons[i];
                final int _count = count;
                button.setTag(i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnButtonsClick(v, _count);
                    }
                });
            }
        } else {
            //默认view
            View view = LayoutInflater.from(builder.context).inflate(R.layout.layout_dialog_custom, null);
            ButterKnife.inject(this, view);
            View v = null;
            builder.setView(view, v);
        }
        //如果需要使用setView方法 dialog布局边缘的border需要手动去掉 且在show之前调用
        //使用getWindow().setContentView则须在super.show()之后调用
        //fred @ 2016-03-01 19:31:02
        getWindow().setContentView(builder.view);
//        setView(builder.view,0,0,0,0);
        setCancelable(builder.cancelable);
        setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
    }

    //设置自定义底部button的点击事件
    private void setOnButtonsClick(View v, int _count) {
        if (v.getTag() != null && v.getTag() instanceof Integer && builder.buttonCallback != null) {
            int position = (Integer) v.getTag();
            if (_count == 1) {
                if (position == 0) {
                    onClickPositiveText();
                }
            } else if (_count == 2) {
                if (position == 0) {
                    onClickNegativeText();
                } else if (position == 1) {
                    onClickPositiveText();
                }
            } else if (_count == 3) {
                if (position == 0) {
                    onClickNegativeText();
                } else if (position == 1) {
                    onClickNeutralText();
                } else if (position == 2) {
                    onClickPositiveText();
                }
            }
        }
    }

    /**
     * ===============================  [private]  END  ======================================
     */


    /**
     * ************** ************** ************** ************** **************  UI Component Set && Event
     */


    @OnClick(R.id.view_negativeText)
    public void onClickNegativeText() {
        if (builder.buttonCallback != null) {
            builder.buttonCallback.onNegative(this);
        }
        if (builder.dismissAfterClick) {
            dismiss();
        }
    }

    @OnClick(R.id.view_neutralText)
    public void onClickNeutralText() {
        if (builder.buttonCallback != null) {
            builder.buttonCallback.onNeutral(this);
        }
        if (builder.dismissAfterClick) {
            dismiss();
        }
    }

    @OnClick(R.id.view_positiveText)
    public void onClickPositiveText() {
        if (builder.buttonCallback != null) {
            builder.buttonCallback.onPositive(this);
        }
        if (builder.dismissAfterClick) {
            dismiss();
        }
    }

    /**
     * ************** ************** ************** ************** **************  UI Component Set && Event end
     */

    public static class Builder {
        protected Context context;
        protected boolean cancelable = true;
        protected String title;
        protected CharSequence content;
        protected String positiveText;
        protected String neutralText;
        protected String negativeText;
        protected ButtonCallback buttonCallback;
        protected View view;
        protected View[] buttons;
        public final static int BUTTON_MAX_COUNT = 3;
        /**
         * 点击dialog上的按钮是否dismiss
         */
        private boolean dismissAfterClick = true;
        protected boolean canceledOnTouchOutside = true;
        private boolean clearBackground;
        private boolean fullScreen;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * 【自定义全部View】如果设置自定义View需要自定义按钮样式及传入在此传入按钮引用id 最多3个
         *
         * @param view    若为空 返回默认样式
         * @param buttons 上限3个
         * @optional [empty] | onNegative | onNegative onPositive | onNegative onNeutral onPositive
         */
        public Builder setView(View view, Object... buttons) {
            this.view = view;
            if (buttons instanceof View[]) {
                this.buttons = (View[]) buttons;
            } else if (buttons instanceof Integer[]) {
                View[] btns = null;
                for (int i = 0, count = buttons.length; i < count; i++) {
                    if (btns == null) {
                        btns = new View[buttons.length];
                    }
                    btns[i] = view.findViewById((Integer) buttons[i]);
                    if (btns[i] == null) {
                        throw new NullPointerException("Button-id not found");
                    }
                }
                this.buttons = btns;
            }
            return this;
        }

        public Builder dismissAfterClick(boolean b) {
            this.dismissAfterClick = b;
            return this;
        }

        public Builder cancelable(boolean b) {
            this.cancelable = b;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean b) {
            this.canceledOnTouchOutside = b;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder neutralText(String neutralText) {
            this.neutralText = neutralText;
            return this;
        }

        public Builder negativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public void callback(ButtonCallback buttonCallback) {
            this.buttonCallback = buttonCallback;
        }

        public UIDialog build() {
            return new UIDialog(this);
        }


        public Builder clearBackground(boolean b) {
            this.clearBackground = b;
            return this;
        }

        public Builder fullScreen(boolean b) {
            this.fullScreen = b;
            return this;
        }
    }

    /**
     * Override these as needed, so no needing to sub empty methods from an interface
     */
    public static abstract class ButtonCallback {

        public void onPositive(UIDialog dialog) {
        }

        public void onNegative(UIDialog dialog) {
        }

        public void onNeutral(UIDialog dialog) {
        }

        public ButtonCallback() {
            super();
        }

        @Override
        protected final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public final boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        protected final void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public final int hashCode() {
            return super.hashCode();
        }

        @Override
        public final String toString() {
            return super.toString();
        }
    }

}
