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

import Model.MainStoryModel;

/**
 * Created by shituocheng on 16/5/2.
 */
public class MyCustomAdapter extends ArrayAdapter<MainStoryModel> {
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public MyCustomAdapter(Context context, int resource, List<MainStoryModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getPosition(MainStoryModel item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public MainStoryModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.mainStory_textView = (TextView)view.findViewById(R.id.storyTitle);
            viewHolder.mNetworkImageView = (NetworkImageView)view.findViewById(R.id.networkImageView);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.mainStoryModel = getItem(position);
        viewHolder.mainStory_textView.setText(viewHolder.mainStoryModel.getTitle());
        viewHolder.mNetworkImageView.setImageUrl(viewHolder.mainStoryModel.getImage(),mImageLoader);
        return view;
    }

    class ViewHolder{
        MainStoryModel mainStoryModel;
        TextView mainStory_textView;
        NetworkImageView mNetworkImageView;
    }
}
