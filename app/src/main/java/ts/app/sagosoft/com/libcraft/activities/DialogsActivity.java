package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.fragment.UIDialogFragment;
import ts.app.sagosoft.com.libcraft.widget.UIDialog;

/**
 * Created by FRED_angejia on 2016/3/3.
 */
public class DialogsActivity extends BaseActivity {


    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, DialogsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        ButterKnife.inject(this);

    }


    @OnClick(R.id.button_dialog_default)
    public void showDefaultDialog() {
        UIDialog.Builder builder = new UIDialog.Builder(this);
        String description = "请摆好姿势";
        builder.title("来自Fred的重要通知").content(Html.fromHtml(description))
                .positiveText("知道了")
                .negativeText("放弃").
                cancelable(true)
                .callback(new UIDialog.ButtonCallback() {
                    @Override
                    public void onPositive(UIDialog dialog) {
                        showCustomToast("UIDialog", "点击了Positive");
                    }

                    @Override
                    public void onNegative(UIDialog dialog) {
                        showCustomToast("UIDialog", "点击了Negative");
                    }
                });
        builder.build().show();
        //如果需要替换body  在此调用UIDialog replaceBody
    }

    @OnClick(R.id.button_dialog_custom)
    public void showCustomDialog() {
        View loading = LayoutInflater.from(this).inflate(R.layout.view_loading_sample, null);
        UIDialog.Builder builder = new UIDialog.Builder(this).setView(loading).fullScreen(true)
                .cancelable(true).canceledOnTouchOutside(true);
        builder.build().show();
    }

    @OnClick(R.id.button_dialog_fragment)
    public void showFragmentDialog() {
        UIDialogFragment.Builder builder = new UIDialogFragment.Builder(this);
        String description = "请摆好姿势";
        builder.title("来自Fred的重要通知").content(Html.fromHtml(description))
                .positiveText("知道了")
                .negativeText("放弃")
                .cancelable(true)
                .callback(new UIDialogFragment.ButtonCallback() {
                    @Override
                    public void onPositive(UIDialogFragment dialog) {
                        showCustomToast("UIDialog", "点击了FragmentDialog Positive");
                    }

                    @Override
                    public void onNegative(UIDialogFragment dialog) {
                        showCustomToast("UIDialog", "点击了FragmentDialog Negative");
                    }
                });
        builder.build().show(getSupportFragmentManager(), "showFragmentDialog");
    }

    @OnClick(R.id.button_dialog_fragment_fullscreen)
    public void showFragmentFullscreenDialog() {
        View loading = LayoutInflater.from(this).inflate(R.layout.view_loading_sample, null);
        UIDialogFragment.Builder builder = new UIDialogFragment.Builder(this).setView(loading)//.fullScreen(true)
                .cancelable(true).clearBack(true);
        builder.build().show(getSupportFragmentManager(), "showFragmentFullscreenDialog");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
