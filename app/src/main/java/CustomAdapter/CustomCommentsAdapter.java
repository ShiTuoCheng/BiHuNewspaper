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

import org.w3c.dom.Text;

import java.util.List;

import Model.LongCommentModel;

/**
 * Created by shituocheng on 16/5/4.
 */
public class CustomCommentsAdapter extends ArrayAdapter<LongCommentModel> {
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public CustomCommentsAdapter(Context context, int resource, List<LongCommentModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(LongCommentModel item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public LongCommentModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.long_comment_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.author_textView = (TextView)view.findViewById(R.id.author_textView);
            viewHolder.mNetworkImageView = (NetworkImageView)view.findViewById(R.id.avatar_networkImageView);
            viewHolder.content_textView = (TextView)view.findViewById(R.id.content_textView);
            viewHolder.likes_textView = (TextView)view.findViewById(R.id.like_textView);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mLongCommentModel = getItem(position);
        viewHolder.author_textView.setText(viewHolder.mLongCommentModel.getAuthor());
        viewHolder.mNetworkImageView.setImageUrl(viewHolder.mLongCommentModel.getAvatar(),mImageLoader);
        viewHolder.content_textView.setText(viewHolder.mLongCommentModel.getContent());
        viewHolder.likes_textView.setText("赞："+viewHolder.mLongCommentModel.getLikes());
        return view;
    }

    class ViewHolder{

        LongCommentModel mLongCommentModel;
        NetworkImageView mNetworkImageView;
        TextView author_textView;
        TextView content_textView;
        TextView likes_textView;
    }
}
