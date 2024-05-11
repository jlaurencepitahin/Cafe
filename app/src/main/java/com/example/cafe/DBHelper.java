package com.example.cafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cafe.DataModel.Sale;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static String SALES_TBL = "SALES_TBL";

    public DBHelper(Context context){
        super(context, "cafe.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SALES_TBL + "(" +
                "SALEID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PRODUCT_NAME TEXT," +
                "PRICE TEXT," +
                "QUANTITY TEXT," +
                "DATE TEXT," +
                "CATEGORY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SALES_TBL);
    }

    public long insertSale(Sale sale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("PRODUCT_NAME", sale.getProductName());
        cv.put("PRICE", sale.getPrice());
        cv.put("QUANTITY", sale.getQuantity());
        cv.put("DATE", sale.getDate());
        cv.put("CATEGORY", sale.getCategory());

        return db.insert(SALES_TBL, null, cv);
    }

    public List<Sale> getSales() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SALES_TBL, null);
        List<Sale> saleList = new ArrayList<>();
        while (cursor.moveToNext()){
            String saleID = String.valueOf(cursor.getString(0));
            String productName = cursor.getString(1);
            String price = cursor.getString(2);
            String quantity = cursor.getString(3);
            String date = cursor.getString(4);
            String category = cursor.getString(5);

            //Init sale object
            Sale sale = new Sale();
            sale.setSaleID(saleID);
            sale.setProductName(productName);
            sale.setPrice(price);
            sale.setQuantity(quantity);
            sale.setDate(date);
            sale.setCategory(category);

            saleList.add(sale);
        }
        cursor.close();

        return saleList;
    }

    public double getTotalSales() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(PRICE * QUANTITY) AS TOTAL FROM " + SALES_TBL, null);

        double totalSales = 0.0;
        while(cursor.moveToNext()) {
            totalSales = cursor.getDouble(0);
        }
        cursor.close();
        return totalSales;
    }

    public double getDailySales(String month) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT AVG(TOTAL) AS AVERAGE FROM (" +
                "SELECT strftime('%Y-%m-%d', DATE) AS DAY, SUM(PRICE * QUANTITY) AS TOTAL FROM " + SALES_TBL + " WHERE strftime('%Y-%m', DATE) = ? GROUP BY DAY)", new String[]{month});

        double dailySales = 0.0;
        while(cursor.moveToNext()) {
            dailySales = cursor.getDouble(0);
        }
        cursor.close();
        return dailySales;
    }

    public double getMonthlySales(String year) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT AVG(TOTAL) AS AVERAGE FROM (" +
                "SELECT strftime('%Y-%m', DATE) AS MONTH, SUM(PRICE * QUANTITY) AS TOTAL FROM " + SALES_TBL + " WHERE strftime('%Y', DATE) = ? GROUP BY MONTH)", new String[]{year});

        double monthlySales = 0.0;
        while(cursor.moveToNext()) {
            monthlySales = cursor.getDouble(0);
        }
        cursor.close();
        return monthlySales;
    }

    public double getYearlySales() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT AVG(TOTAL) AS AVERAGE FROM (" +
                "SELECT strftime('%Y', DATE) AS YEAR, SUM(PRICE * QUANTITY) AS TOTAL FROM " + SALES_TBL + " GROUP BY YEAR)", null);

        double yearlySales = 0.0;
        while(cursor.moveToNext()) {
            yearlySales = cursor.getDouble(0);
        }
        cursor.close();
        return yearlySales;
    }

    public String[] getYearsWithSale() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT strftime('%Y', DATE) AS YEAR FROM " + SALES_TBL, null);
        String[] years = new String[cursor.getCount()];
        int index = 0;
        while(cursor.moveToNext()) {
            years[index] = cursor.getString(0);
            index++;
        }
        cursor.close();
        return years;
    }
}
