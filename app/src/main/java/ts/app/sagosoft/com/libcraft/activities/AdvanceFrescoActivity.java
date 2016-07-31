package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by fred on 16/7/30.
 */
public class AdvanceFrescoActivity extends BaseActivity {

    @InjectView(R.id.my_image_view)
    SimpleDraweeView myImageView;

    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, AdvanceFrescoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_fresco);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        myImageView.setImageURI("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
    }
}
