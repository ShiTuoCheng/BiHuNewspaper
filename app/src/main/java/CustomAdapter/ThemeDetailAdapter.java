package CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shituocheng.bihunewspaper.com.bihunewspaper.AppController;
import com.shituocheng.bihunewspaper.com.bihunewspaper.R;

import java.util.List;

import Model.ThemeDetailModel;

/**
 * Created by shituocheng on 16/5/8.
 */
public class ThemeDetailAdapter extends ArrayAdapter<ThemeDetailModel> {
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public ThemeDetailAdapter(Context context, int resource, List<ThemeDetailModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public ThemeDetailModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(ThemeDetailModel item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.theme_detail_item_layout,null);
            viewHolder = new ViewHolder();
           // viewHolder.mNetworkImageView = (NetworkImageView)view.findViewById(R.id.themeDetail_networkImageView);
            viewHolder.theme_detail_title = (TextView)view.findViewById(R.id.theme_detail_title_textView);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.mThemeDetailModel = getItem(position);
        viewHolder.theme_detail_title.setText(viewHolder.mThemeDetailModel.getTitle());
       // viewHolder.mNetworkImageView.setImageUrl(viewHolder.mThemeDetailModel.getImage(),mImageLoader);
        return view;
    }
    class ViewHolder{
        NetworkImageView mNetworkImageView;
        ThemeDetailModel mThemeDetailModel;
        TextView theme_detail_title;
    }
}
