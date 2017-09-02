package com.treenear.treewebserver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.treenear.treewebserver.R;
import com.treenear.treewebserver.control.Utils;
import com.treenear.treewebserver.models.ColDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richardus on 8/25/17.
 */


public class AdpDocuments extends RecyclerView.Adapter<AdpDocuments.MyViewHolder> {
    private List<ColDocument> moviesList;
    private Context context;
    private ArrayList<ColDocument> columlistflig;
    private Intent i;
    private String ssite="";
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvname, tvdate,tvid;
        public LinearLayout lnrlayout;
        public ImageView imgproses;

        public MyViewHolder(View view) {
            super(view);
            tvname   			= 	 (TextView)view.findViewById(R.id.TvColDocName);
            tvdate			    =	 (TextView)view.findViewById(R.id.TvColDocDate);
            lnrlayout			=	 (LinearLayout)view.findViewById(R.id.LnrColDocument);

        }
    }


    public AdpDocuments(List<ColDocument> moviesList, Context context) {
        this.moviesList = new ArrayList<>(moviesList);
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.col_documents, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ColDocument ColDocument = moviesList.get(position);
        holder.tvname.setText(ColDocument.getDescription());
        holder.tvdate.setText(Utils.Date(ColDocument.getEntryDate()));



        holder.lnrlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                i = new Intent(context,DetailDocuments.class);
//                i.putExtra("Id",ColDocument.getId());
//                i.putExtra("Document",ColDocument.getDescription());
//                context.startActivity(i);


            }
        });



    }

    @Override
    public int getItemCount() {
        return moviesList.size();

    }

    public void animateTo(List<ColDocument> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ColDocument> newModels) {
        for (int i = moviesList.size() - 1; i >= 0; i--) {
            final ColDocument model = moviesList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ColDocument> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ColDocument model = newModels.get(i);
            if (!moviesList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ColDocument> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ColDocument model = newModels.get(toPosition);
            final int fromPosition = moviesList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private ColDocument removeItem(int position) {
        final ColDocument model = moviesList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, ColDocument model) {
        moviesList.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final ColDocument model = moviesList.remove(fromPosition);
        moviesList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        int size = moviesList.size();
        moviesList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
