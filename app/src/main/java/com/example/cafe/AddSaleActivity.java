package com.example.cafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cafe.DataModel.Sale;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSaleActivity extends AppCompatActivity {


    TextInputLayout ledProductName, ledPrice, ledDate;
    TextInputEditText edProductName, edPrice, edDate;
    RadioButton rbHot, rbCold, rbSnacks;
    TextView txtQuantity, txtSubtotal;

    String selectedDate;

    Button btnCheckout;
    String selectedCategory;
    ImageButton btnBack, btnAddQ, btnSubtractQ;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_sale);
        dbHelper = new DBHelper(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select a date").build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long aLong) {
                        edDate.setText(materialDatePicker.getHeaderText());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(aLong);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate = simpleDateFormat.format(calendar.getTime());

                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_HANZ_DIALER");
            }
        });

        rbHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = Environment.HOT_BEVERAGES;
            }
        });

        rbCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = Environment.COLD_BEVERAGES;
            }
        });

        rbSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = Environment.SNACKS;
            }
        });

        btnSubtractQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if(quantity > 0){
                    txtQuantity.setText(String.valueOf(quantity-1));
                    updateTotal();
                }
            }
        });

        edPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAddQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                txtQuantity.setText(String.valueOf(quantity+1));
                updateTotal();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = edProductName.getText().toString();
                String quantity = txtQuantity.getText().toString();
                String price = edPrice.getText().toString();
                if(validateForm(productName, quantity, price, selectedDate, selectedCategory)) {
                    Sale sale = new Sale();
                    sale.setProductName(productName);
                    sale.setQuantity(quantity);
                    sale.setPrice(price);
                    sale.setDate(selectedDate);
                    sale.setCategory(selectedCategory);

                    int saleId = (int) dbHelper.insertSale(sale);
                    if(saleId > -1) {
                        sale.setSaleID(String.valueOf(saleId));

                        Intent prevIntent = new Intent(AddSaleActivity.this, SalesActivity.class);
                        Intent intent = new Intent(AddSaleActivity.this, SummaryActivity.class);
                        intent.putExtra("sale", sale);
                        prevIntent.putExtra("sale", sale);
                        startActivity(intent);

                        setResult(RESULT_OK, prevIntent);
                        finish();
                    }else{
                        new MaterialAlertDialogBuilder(AddSaleActivity.this)
                                .setTitle("Add Sale Error")
                                .setMessage("There is a problem adding a new sale, pleasee try again.")
                                .setNeutralButton("Ok", null)
                                .show();
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

    private boolean validateForm(String productName, String quantity, String price, String selectedDate, String selectedCategory) {

        if(productName.isEmpty()){
            ledProductName.setError("Enter the product name");
            return false;
        }
        if(quantity.isEmpty()){
            return false;
        }
        if(price.isEmpty()){
            ledPrice.setError("Enter the product's price");
            return false;
        }
        if(selectedCategory.isEmpty()){
            return false;
        }
        if(selectedDate.isEmpty()){
            return false;
        }
        return true;
    }

    private void updateTotal() {

        if(!edPrice.getText().toString().isEmpty()) {
            double total = Integer.parseInt(txtQuantity.getText().toString()) * Double.parseDouble(edPrice.getText().toString());

            txtSubtotal.setText("$" + String.valueOf(total));
        }
    }

    private void initViews() {
        ledProductName = findViewById(R.id.ledProductName);
        ledPrice = findViewById(R.id.ledPrice);
        ledDate = findViewById(R.id.ledDate);
        edProductName = findViewById(R.id.edProductName);
        edPrice = findViewById(R.id.edPrice);
        edDate = findViewById(R.id.edDate);

        txtQuantity = findViewById(R.id.txtQuantity);
        txtSubtotal = findViewById(R.id.txtSubtotal);

        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        btnAddQ = findViewById(R.id.btnAddQ);
        btnSubtractQ = findViewById(R.id.btnSubtractQ);

        rbCold = findViewById(R.id.rbCold);
        rbHot = findViewById(R.id.rbHot);
        rbSnacks = findViewById(R.id.rbSnacks);
    }

}