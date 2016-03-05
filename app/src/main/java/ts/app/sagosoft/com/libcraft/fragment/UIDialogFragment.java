package ts.app.sagosoft.com.libcraft.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by FRED_angejia on 2016/3/4.
 */
public class UIDialogFragment extends DialogFragment {

    @InjectView(R.id.tv_content_body_title)
    TextView tvContentBodyTitle;
    @InjectView(R.id.tv_content_body_content)
    TextView tvContentBodyContent;
    @InjectView(R.id.layout_content_body)
    LinearLayout layoutContentBody;
    @InjectView(R.id.layout_content_view)
    LinearLayout layoutContentView;
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
    private Builder builder;
    private boolean viewTypeDefault;

    public UIDialogFragment() {
    }

    public UIDialogFragment initBuilder(Builder builder) {
        this.builder = builder;
        return this;
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View loading = LayoutInflater.from(getActivity()).inflate(R.layout.view_loading_sample, null);
        UIDialog.Builder builder = new UIDialog.Builder(getActivity()).setView(loading)
                .cancelable(true).canceledOnTouchOutside(true);
        dialog = builder.build();
        return dialog;
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewTypeDefault) {
            ButterKnife.reset(this);
        }
        this.builder = null;
//        System.gc();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.builder != null) {
            if (this.builder.fullScreen) {
                setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_NoActionBar_Fullscreen);
            } else {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            if (this.builder.clearBack) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        View rootView = initCustom(inflater, container);
        renderData(builder);
        DisplayMetrics om = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(om);
        if (this.builder != null && this.builder.view != null) {
            final int margin = getActivity().getResources().getDimensionPixelSize(R.dimen.md_dialog_frame_margin);
            this.builder.view.setMinimumWidth(om.widthPixels - margin);
        }
        /*View parent=(View)rootView.getParent();
        int padding = getResources().getDimensionPixelSize(R.dimen.md_dialog_frame_margin);
        parent.setPadding(padding,parent.getPaddingTop(),padding,parent.getPaddingBottom());*/
        return rootView;
    }


    //如果需要自定义全部view
    private View initCustom(LayoutInflater inflater, ViewGroup container) {
        if (builder == null) return null;
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
            View view = inflater.inflate(R.layout.layout_dialog_custom, container);
            ButterKnife.inject(this, view);
            View v = null;
            builder.setView(view, v);
        }
        setCancelable(builder.cancelable);
        return builder.view;
    }


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
                    viewNeutralText.setVisibility(View.VISIBLE);
                } else {
                    viewNeutralText.setVisibility(View.GONE);
                }
            }
            if (tvNegativeText != null) {
                if (builder.negativeText != null) {
                    tvNegativeText.setText(builder.negativeText);
                    viewNegativeText.setVisibility(View.VISIBLE);
                } else {
                    viewNegativeText.setVisibility(View.GONE);
                }
            }
            if (tvPositiveText != null) {
                if (builder.positiveText != null) {
                    tvPositiveText.setText(builder.positiveText);
                    viewPositiveText.setVisibility(View.VISIBLE);
                } else {
                    viewPositiveText.setVisibility(View.GONE);
                }
            }


        }
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
        private boolean clearBack = true;

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

        public Builder clearBack(boolean b) {
            this.clearBack = b;
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

        public UIDialogFragment build() {
            return new UIDialogFragment().initBuilder(this);
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

        public void onPositive(UIDialogFragment dialog) {
        }

        public void onNegative(UIDialogFragment dialog) {
        }

        public void onNeutral(UIDialogFragment dialog) {
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
