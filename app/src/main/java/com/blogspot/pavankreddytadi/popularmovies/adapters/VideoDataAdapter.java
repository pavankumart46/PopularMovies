package com.blogspot.pavankreddytadi.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.pavankreddytadi.popularmovies.R;
import com.blogspot.pavankreddytadi.popularmovies.models.VideoDataModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoDataAdapter extends RecyclerView.Adapter<VideoDataAdapter.ViewInformation>
{
    private Context context;
    private List<VideoDataModel> list;

    public VideoDataAdapter(Context context, List<VideoDataModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.one_video_view,parent,false);
        return new ViewInformation(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInformation holder, int position)
    {
        holder.video_title.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewInformation extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView video_title;
        ViewInformation(View itemView) {
            super(itemView);
            video_title = itemView.findViewById(R.id.video_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
              int pos = getAdapterPosition();
              String key = list.get(pos).getKey();
              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+key));
              context.startActivity(intent);
        }
    }
}
