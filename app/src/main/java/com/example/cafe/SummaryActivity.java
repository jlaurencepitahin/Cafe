package com.example.cafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cafe.DataModel.Sale;

public class SummaryActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnContinue;
    Sale sale;
    TextView txtSubTotal, txtTax, txtTotal;
    LinearLayout layoutContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sale = (Sale) getIntent().getSerializableExtra("sale");

        initViews();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        String[][] list = {
                {"Sale ID", sale.getSaleID()},
                {"Product Name", sale.getProductName()},
                {"Price", "$"+sale.getPrice()},
                {"Quantity", sale.getQuantity()},
                {"Date", sale.getDate()},
                {"Category", sale.getCategory()}
        };
        for(String[] s: list){
            View vi = layoutInflater.inflate(R.layout.item_summary, null);
            TextView txtLabel = vi.findViewById(R.id.txtLabel);
            TextView txtValue = vi.findViewById(R.id.txtValue);
            txtLabel.setText(s[0]);
            txtValue.setText(s[1]);

            layoutContainer.addView(vi, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        double total = Integer.parseInt(sale.getQuantity()) * Double.parseDouble(sale.getPrice());
        txtTotal.setText("$" + total);
        txtSubTotal.setText("$" + total);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);

        txtSubTotal = findViewById(R.id.txtSubtotal);
        txtTotal = findViewById(R.id.txtTotal);
        txtTax = findViewById(R.id.txtTax);
        layoutContainer = findViewById(R.id.layoutContainer);
    }
}