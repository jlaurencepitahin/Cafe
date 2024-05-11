package com.example.cafe.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.DataModel.Sale;
import com.example.cafe.Environment;
import com.example.cafe.Interface.SaleCallback;
import com.example.cafe.R;

import java.util.ArrayList;
import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder>{

    List<Sale> saleList = new ArrayList<>();
    SaleCallback saleCallback;
    Context context;
    public SaleAdapter(Context context,  SaleCallback saleCallback, List<Sale> saleList) {
        this.context = context;
        this.saleList = saleList;
        this.saleCallback = saleCallback;
    }
    @NonNull
    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.ViewHolder holder, int position) {
        Sale sale = saleList.get(position);

        holder.txtProductName.setText(sale.getProductName());
        double price = Double.parseDouble(sale.getPrice());
        int quantity = Integer.parseInt(sale.getQuantity());
        holder.txtTotal.setText("$"+String.valueOf( price * quantity));
        holder.txtDate.setText(sale.getDate());

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_snacks);
        if(sale.getCategory().equals(Environment.HOT_BEVERAGES)){
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_hot);
        }
        else if(sale.getCategory().equals(Environment.COLD_BEVERAGES)){
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_cold);
        }
        holder.imgSale.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSale;
        TextView txtTotal, txtDate, txtProductName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSale = itemView.findViewById(R.id.imgIcon);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtProductName = itemView.findViewById(R.id.txtProductName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saleCallback.OnSaleClick(getAdapterPosition());
                }
            });
        }
    }
}
