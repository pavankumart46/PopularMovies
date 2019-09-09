package com.blogspot.pavankreddytadi.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.pavankreddytadi.popularmovies.R;
import com.blogspot.pavankreddytadi.popularmovies.models.ReviewDataModel;

import java.util.List;

public class ReviewsDataAdapter extends RecyclerView.Adapter<ReviewsDataAdapter.ViewInformation>
{

    private Context context;
    private List<ReviewDataModel> list;

    public ReviewsDataAdapter(Context context, List<ReviewDataModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.one_review_view,parent,false);
        return new ViewInformation(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInformation holder, int position)
    {
        holder.review.setText(list.get(position).getContent());
        holder.reviewedBy.setText(list.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewInformation extends RecyclerView.ViewHolder
    {
        TextView review,reviewedBy;
        public ViewInformation(View itemView)
        {
            super(itemView);
            review = itemView.findViewById(R.id.review_of_movie);
            reviewedBy = itemView.findViewById(R.id.review_by_tv);
        }
    }
}
