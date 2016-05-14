package CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.shituocheng.bihunewspaper.com.bihunewspaper.AppController;
import com.shituocheng.bihunewspaper.com.bihunewspaper.CircularNetworkImageView;
import com.shituocheng.bihunewspaper.com.bihunewspaper.R;

import java.util.List;

import Model.ThemeEditorModel;

/**
 * Created by shituocheng on 16/5/9.
 */
public class ThemeEditorAdapter extends ArrayAdapter<ThemeEditorModel> {
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public ThemeEditorAdapter(Context context, int resource, List<ThemeEditorModel> objects) {
        super(context, resource, objects);
    }


    @Override
    public int getPosition(ThemeEditorModel item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ThemeEditorModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.editor_item_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.editor_name);
            viewHolder.avatar_ImageView = (CircularNetworkImageView)view.findViewById(R.id.editor_avatar);
            viewHolder.bio = (TextView)view.findViewById(R.id.editor_bio);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.mThemeEditorModel = getItem(position);
        viewHolder.name.setText(viewHolder.mThemeEditorModel.getEditor_name());
        viewHolder.avatar_ImageView.setImageUrl(viewHolder.mThemeEditorModel.getAvatar(),mImageLoader);
        viewHolder.bio.setText(viewHolder.mThemeEditorModel.getBio());
        return view;
    }
    class ViewHolder{
        ThemeEditorModel mThemeEditorModel;
        TextView name;
        TextView bio;
        CircularNetworkImageView avatar_ImageView;
    }
}
