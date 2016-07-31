package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by fred on 16/7/30.
 */
public class FrescoActivity extends BaseActivity {


    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, FrescoActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frescos);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.button_Fresco_Simple)
    public void onSimpleClick() {
        startActivity(SimpleFrescoActivity.mkIntent(getBaseContext()));
    }

    @OnClick(R.id.button_Fresco_Advance)
    public void onAdvanceClick() {
        startActivity(AdvanceFrescoActivity.mkIntent(getBaseContext()));
    }
}
