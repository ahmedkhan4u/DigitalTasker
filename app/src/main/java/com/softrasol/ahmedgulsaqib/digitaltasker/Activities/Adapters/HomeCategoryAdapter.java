package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.HomeCategoryModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private Context context;
    private List<HomeCategoryModel> list;
    public HomeCategoryAdapter(Context context, List<HomeCategoryModel> list) {

        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).
                inflate(R.layout.home_category_item_list, parent, false);
        return new ViewHolder(mView);
    }

    //...............................................................................................

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomeCategoryModel model = list.get(position);
        holder.mCategoryImage.setImageResource(model.getCategory_image());
        holder.mCategoryName.setText(model.getCategory_name());

    }
//...................................................................................................
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mCategoryImage;
        private TextView mCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCategoryImage = itemView.findViewById(R.id.img_home_category);
            mCategoryName = itemView.findViewById(R.id.txt_home_category);

        }
    }
}
