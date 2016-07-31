package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.adapter.FrescoAdapter;

/**
 * Created by fred on 16/7/30.
 */
public class SimpleFrescoActivity extends BaseActivity {

    @InjectView(R.id.lv_image_view)
    ListView lvImageView;
    private ArrayList<String> list;

    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, SimpleFrescoActivity.class);
        return intent;
    }

    @InjectView(R.id.my_image_view)
    SimpleDraweeView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fresco);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        myImageView.setVisibility(View.GONE);
//        myImageView.setImageURI(Uri.parse("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg"));
        list = new ArrayList<>();
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");
        list.add("http://cdn1.dooioo.com/fetch/vp/yishou/ptgi/300x225/20160714/05e8d743-ce20-48ce-a663-5bf71c12691a.jpg");

        ListAdapter adapter = new FrescoAdapter(this,list);
        lvImageView.setAdapter(adapter);

    }



}
