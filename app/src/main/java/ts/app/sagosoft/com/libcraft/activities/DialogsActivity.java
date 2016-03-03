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
    private void showDefaultDialog() {
        UIDialog.Builder builder = new UIDialog.Builder(this);
        String description = "请摆好姿势";
        builder.title("来自Fred的重要通知").content(Html.fromHtml(description))
                .positiveText("知道了")
                .negativeText("放弃").
                cancelable(true).canceledOnTouchOutside(false)
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
    private void showCustomDialog() {
        View loading = LayoutInflater.from(this).inflate(R.layout.view_loading_sample, null);
        UIDialog.Builder builder = new UIDialog.Builder(this).setView(loading)
                .cancelable(true).canceledOnTouchOutside(true);
        builder.build().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
