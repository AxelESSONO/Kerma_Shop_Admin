package com.obiangetfils.kermashopadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.obiangetfils.kermashopadmin.R;
import com.obiangetfils.kermashopadmin.controller.AddCategoryActivity;
import com.obiangetfils.kermashopadmin.controller.AddNewProductActivity;
import com.obiangetfils.kermashopadmin.controller.AdminAccountingActivity;
import com.obiangetfils.kermashopadmin.controller.AdminHandleNewProductActivity;
import com.obiangetfils.kermashopadmin.controller.AdminHandleNewProductsActivity;
import com.obiangetfils.kermashopadmin.controller.AdminHandleSlideActivity;
import com.obiangetfils.kermashopadmin.controller.AdminHandleUsersActivity;
import com.obiangetfils.kermashopadmin.controller.AdminMaintainProductsActivity;
import com.obiangetfils.kermashopadmin.model.HomeObject;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private List<HomeObject> homeObjectList;
    private Context mContext;

    public HomeAdapter(List<HomeObject> homeObjectList, Context mContext) {
        this.homeObjectList = homeObjectList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_action, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        final HomeObject homeObject = homeObjectList.get(position);
        holder.homeName.setText(homeObject.getNameHome());
        holder.homeImage.setImageResource(homeObject.getHomeImage());
        holder.new_product_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] homeItemList = {
                        "Ajouter un nouveau produit", "Ajouter une nouvelle catégorie", "Modifier un produit", "Gestion des commandes",
                        "Gestion des nouveaux produits", "Gestion des utilisateurs", "Gestion des Slides", "Gestions des recettes"
                };

                if (homeObject.getNameHome().equals(homeItemList[0])) {
                    Intent newProductIntent = new Intent(mContext, AddNewProductActivity.class);
                    mContext.startActivity(newProductIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[1])) {
                    Intent newCategoryIntent = new Intent(mContext, AddCategoryActivity.class);
                    mContext.startActivity(newCategoryIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[2])) {
                    Intent newMaintainIntent = new Intent(mContext, AdminMaintainProductsActivity.class);
                    mContext.startActivity(newMaintainIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[3])) {
                    Intent newHandleNewProductIntent = new Intent(mContext, AdminHandleNewProductActivity.class);
                    mContext.startActivity(newHandleNewProductIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[4])) {
                    Intent handleNewProductsIntent = new Intent(mContext, AdminHandleNewProductsActivity.class);
                    mContext.startActivity(handleNewProductsIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[5])) {
                    Intent handleUsersIntent = new Intent(mContext, AdminHandleUsersActivity.class);
                    mContext.startActivity(handleUsersIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[6])) {
                    Intent handleSlideIntent = new Intent(mContext, AdminHandleSlideActivity.class);
                    mContext.startActivity(handleSlideIntent);
                }
                else if (homeObject.getNameHome().equals(homeItemList[7])) {
                    Intent adminAccountingIntent = new Intent(mContext, AdminAccountingActivity.class);
                    mContext.startActivity(adminAccountingIntent);
                }
                else {
                    Toast.makeText(mContext, "Veuillez choisir une action", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeObjectList.size();
    }


    public class HomeHolder extends RecyclerView.ViewHolder {

        CardView new_product_cardView;
        ImageView homeImage;
        TextView homeName;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);

            new_product_cardView = (CardView) itemView.findViewById(R.id.new_product_cardView);
            homeImage = (ImageView) itemView.findViewById(R.id.homeImage);
            homeName = (TextView) itemView.findViewById(R.id.homeName);

        }
    }
}
