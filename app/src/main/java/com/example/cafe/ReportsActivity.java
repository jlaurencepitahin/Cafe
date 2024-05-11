package com.example.cafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class ReportsActivity extends AppCompatActivity {

    TextView txtTotalSales, txtMonthlySales, txtYearlySales, txtDailySales;
    ImageButton btnBack, btnFilter;

    String chosenMonth = "04";

    String[] years;
    DBHelper dbHelper;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        String totalSales = "$" + dbHelper.getTotalSales();
        txtTotalSales.setText(totalSales);
        dbHelper = new DBHelper(this);

        years = dbHelper.getYearsWithSale();


        //DROPDOWN FUNCTION

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ReportsActivity.this);
                View view = LayoutInflater.from(ReportsActivity.this).inflate(R.layout.item_filter, null);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

                MaterialAutoCompleteTextView ddMonth = view.findViewById(R.id.ddMonth);
                MaterialAutoCompleteTextView ddYear = view.findViewById(R.id.ddYear);
                Button btnApply = view.findViewById(R.id.btnApply);

                if(years.length > 0)
                    ddYear.setSimpleItems(years);
                ddMonth.setSimpleItems(months);
                ddMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position < 9){
                            chosenMonth = "0" + (position + 1);
                        }else {
                            chosenMonth = String.valueOf(position + 1);
                        }

                    }
                });

                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chosenYear = ddYear.getText().toString();
                        String dailySales = "$" + dbHelper.getDailySales(chosenYear + "-"+chosenMonth);
                        String monthlySales = "$" + dbHelper.getMonthlySales(chosenYear);
                        String yearlySales = "$" + dbHelper.getYearlySales();

                        txtDailySales.setText(dailySales);
                        txtMonthlySales.setText(monthlySales);
                        txtYearlySales.setText(yearlySales);
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        txtTotalSales = findViewById(R.id.txtTotal);
        txtMonthlySales = findViewById(R.id.txtMonthly);
        txtYearlySales = findViewById(R.id.txtYearly);
        txtDailySales = findViewById(R.id.txtDaily);
        btnBack = findViewById(R.id.btnBack);
        btnFilter = findViewById(R.id.btnFilter);
    }

}