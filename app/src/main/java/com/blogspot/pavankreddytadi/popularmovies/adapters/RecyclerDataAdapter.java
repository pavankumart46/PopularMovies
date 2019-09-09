package com.blogspot.pavankreddytadi.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.pavankreddytadi.popularmovies.DetailActivity;
import com.blogspot.pavankreddytadi.popularmovies.R;
import com.blogspot.pavankreddytadi.popularmovies.models.DataModelClass;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewInformation>
{
    private Context context;
    private List<DataModelClass> list;

    public RecyclerDataAdapter(Context context, List<DataModelClass> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.single_row_design,parent,false);
        return new ViewInformation(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInformation holder, int position)
    {
        holder.movie_title.setText(list.get(position).getMovieTitle());
        Glide.with(context).load("http://image.tmdb.org/t/p/w185"+list.get(position).getImageLink()).into(holder.poster);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewInformation extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView poster;
        TextView movie_title;
        ViewInformation(View itemView)
        {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            movie_title = itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int pos = getAdapterPosition();
            Intent intent = new Intent(context,DetailActivity.class);
            intent.putExtra(DetailActivity.MOVIE_ID,list.get(pos).getId());
            intent.putExtra(DetailActivity.TITLE_KEY,list.get(pos).getMovieTitle());
            intent.putExtra(DetailActivity.DESCRIPTION_KEY,list.get(pos).getPlotSynopsis());
            intent.putExtra(DetailActivity.IMAGE_LINK_KEY,list.get(pos).getImageLink());
            intent.putExtra(DetailActivity.RATING_KEY,list.get(pos).getVote_average());
            intent.putExtra(DetailActivity.RELEASE_DATE_KEY,list.get(pos).getReleaseDate());
            intent.putExtra(DetailActivity.BACKDROP_PATH,list.get(pos).getBackdrop());
            context.startActivity(intent);
        }
    }
}
