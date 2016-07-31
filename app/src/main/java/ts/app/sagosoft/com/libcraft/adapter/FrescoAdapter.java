package ts.app.sagosoft.com.libcraft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ts.app.sagosoft.com.libcraft.R;

/**
 * Created by fred on 16/7/30.
 */
public class FrescoAdapter extends BaseAdapter {

    private final Context context;
    private List<String> list;

    public FrescoAdapter(Context context, List<String> list) {
        this.context = context;
        if (list == null) {
            list = new ArrayList();
        }
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fresco, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.itemImageView.setImageURI(getItem(position));
        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.item_image_view)
        SimpleDraweeView itemImageView;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
