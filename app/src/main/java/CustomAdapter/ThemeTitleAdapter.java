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

import Model.ThemeTitleModel;

/**
 * Created by shituocheng on 16/5/5.
 */
public class ThemeTitleAdapter extends ArrayAdapter<ThemeTitleModel> {
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public ThemeTitleAdapter(Context context, int resource, List<ThemeTitleModel> objects) {
        super(context, resource, objects);
    }
    @Override
    public int getPosition(ThemeTitleModel item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ThemeTitleModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.themetitlelayout,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)view.findViewById(R.id.themeTitle_textView);
            viewHolder.description = (TextView)view.findViewById(R.id.themeDescription_textView);
            viewHolder.thumbnail = (NetworkImageView)view.findViewById(R.id.thumbnail);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mThemeTitleModel = getItem(position);
        viewHolder.title.setText(viewHolder.mThemeTitleModel.getTheme_name());
        viewHolder.description.setText(viewHolder.mThemeTitleModel.getDescription());
        viewHolder.thumbnail.setImageUrl(viewHolder.mThemeTitleModel.getThumbnail(),mImageLoader);
        return view;
    }
    class ViewHolder{
        ThemeTitleModel mThemeTitleModel;
        TextView title;
        TextView description;
        NetworkImageView thumbnail;
    }

}
