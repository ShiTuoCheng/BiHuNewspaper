package CustomAdapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shituocheng.bihunewspaper.com.bihunewspaper.AppController;
import com.shituocheng.bihunewspaper.com.bihunewspaper.R;

import java.util.ArrayList;
import java.util.List;

import Model.ThemeTitleModel;

/**
 * Created by shituocheng on 16/5/5.
 */
public class ThemeTitleAdapter extends RecyclerView.Adapter<ThemeTitleAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view, int position);
    }

    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
    private List<ThemeTitleModel> mThemeTitleModels = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private NetworkImageView mNetworkImageView;
        private TextView title_textView;
        private TextView content_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
            title_textView = (TextView)itemView.findViewById(R.id.themeTitle_textView);
            content_textView = (TextView)itemView.findViewById(R.id.themeDescription_textView);

        }
    }

    public ThemeTitleAdapter(List<ThemeTitleModel> themeTitleModels) {
        mThemeTitleModels = themeTitleModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.themetitlelayout,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ThemeTitleModel themeTitleModel = mThemeTitleModels.get(position);

        holder.mNetworkImageView.setImageUrl(themeTitleModel.getThumbnail(),mImageLoader);

        holder.title_textView.setText(themeTitleModel.getTheme_name());

        holder.content_textView.setText(themeTitleModel.getDescription());

        if (mOnItemClickListener != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.OnItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mThemeTitleModels.size();
    }


}
