package ts.app.sagosoft.com.libcraft.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ts.app.sagosoft.com.libcraft.UINoticeViewBuilder;

/**
 * Created by FRED_angejia on 2016/1/21.
 */
public class BaseActivity extends AppCompatActivity {
    protected UINoticeViewBuilder uITopToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uITopToast = UINoticeViewBuilder.getInstance();
    }


    protected void showCustomToast(String title, String msg) {
        if (uITopToast == null) {
            return;
        }
        uITopToast.build(this, null, "",
                title, msg)
                .showWithAnimation().hideWithMills(5000);
    }


    @Override
    public void onBackPressed() {
        if (uITopToast.isActive()) {
            uITopToast.hideWithMills(50);
            return;
        }
        super.onBackPressed();
    }
}
