package com.example.cafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.Adapter.SaleAdapter;
import com.example.cafe.DataModel.Sale;
import com.example.cafe.Interface.SaleCallback;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity implements SaleCallback {


    RecyclerView rvSales;
    List<Sale> saleList;
    SaleAdapter salesAdapter;
    DBHelper dbHelper;

    Button btnAdd;
    ImageButton btnBack;

    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_sales);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saleList = new ArrayList<>();
        saleList = dbHelper.getSales();

        initViews();
        initData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch(new Intent(SalesActivity.this, AddSaleActivity.class));
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == RESULT_OK){
                    Intent data = o.getData();
                    if(data != null) {
                        Sale sale = (Sale) data.getSerializableExtra("sale");
                        saleList.add(sale);
                        salesAdapter.notifyItemInserted(saleList.size()-1);
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        rvSales = findViewById(R.id.rvList);
        rvSales.setLayoutManager(new GridLayoutManager(this, 2));
        salesAdapter = new SaleAdapter(this, this, saleList);
        rvSales.setAdapter(salesAdapter);
    }

    @Override
    public void OnSaleClick(int pos) {
        Sale sale = saleList.get(pos);
        Intent intent = new Intent(SalesActivity.this, SummaryActivity.class);
        intent.putExtra("sale", sale);
        startActivity(intent);
    }
}