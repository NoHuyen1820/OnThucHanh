package com.mingroup.onthuchanhsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mingroup.onthuchanhsqlite.model.NhanVien;
import com.mingroup.onthuchanhsqlite.model.PhongBan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //Database
    private static final String DATABASE_NAME = "nhanviendb";
    private static final int DATABASE_VERSION = 1;

    // Table PhongBan
    private static final String TABLE_PHONG_BAN = "phongbans";
    private static final String KEY_PHONG_BAN_MA = "maPB";
    private static final String KEY_PHONG_BAN_TENPB = "tenPB";

    // TABLE NHANVIEN
    private static final String TABLE_NHANVIEN = "nhanviens";
    private static final String KEY_NHANVIEN_MA = "maNV";
    private static final String KEY_NHANVIEN_TEN = "tenNV";
    private static final String KEY_NHANVIEN_GIOI_TINH = "gioiTinh";
    private static final String KEY_NHANVIEN_PHONG_BAN = "phongBan";
    private static final String KEY_NHANVIEN_NGAY_SINH = "ngaySinh";

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // bật khoá ngoại
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Tạo bảng PHONGBAN: CREATE TABLE phongbans(maPB Text Primary Key, tenPB Text)
        String CREATE_TABLE_PHONG_BAN = "CREATE TABLE " + TABLE_PHONG_BAN + "(" +
                KEY_PHONG_BAN_MA + " TEXT PRIMARY KEY, " +
                KEY_PHONG_BAN_TENPB + " TEXT" + ")";

        String CREATE_TABLE_NHAN_VIEN = "CREATE TABLE " + TABLE_NHANVIEN + "(" +
                KEY_NHANVIEN_MA + " TEXT PRIMARY KEY, " +
                KEY_NHANVIEN_TEN + " TEXT, " +
                KEY_NHANVIEN_GIOI_TINH + " BOOLEAN, " +
                KEY_NHANVIEN_PHONG_BAN + " TEXT REFERENCES " + TABLE_PHONG_BAN + ", " +
                KEY_NHANVIEN_NGAY_SINH + " DATE )";

        sqLiteDatabase.execSQL(CREATE_TABLE_PHONG_BAN);
        sqLiteDatabase.execSQL(CREATE_TABLE_NHAN_VIEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONG_BAN);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NHANVIEN);
            onCreate(sqLiteDatabase);
        }
    }

    // Get danh sách tất cả phòng ban
    public List<PhongBan> getAllPhongBan() {
        List<PhongBan> phongBanList = new ArrayList<>();
        String PHONG_BAN_SELECT_QUERY = "SELECT * FROM " + TABLE_PHONG_BAN;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PHONG_BAN_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    PhongBan phongBan = new PhongBan();
                    phongBan.setMaPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_MA)));
                    phongBan.setTenPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_TENPB)));
                    phongBanList.add(phongBan);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return phongBanList;
    }

    public PhongBan getPhongBanByMa(String maPB) {
        PhongBan phongBan = null;
        String sqlQuery = "SELECT * FROM " + TABLE_PHONG_BAN + " WHERE " + KEY_PHONG_BAN_MA + " = '" + maPB + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);
        try {
            if(cursor.moveToFirst()){
                phongBan = new PhongBan();
                phongBan.setMaPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_MA)));
                phongBan.setTenPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_TENPB)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return phongBan;
    }

    // Thêm phòng ban mới
    public boolean themPhongBan(PhongBan phongBan) {
        SQLiteDatabase db = getWritableDatabase();
        boolean isInsertSuccessful = false;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PHONG_BAN_MA, phongBan.getMaPB());
            values.put(KEY_PHONG_BAN_TENPB, phongBan.getTenPB());
            isInsertSuccessful = db.insertOrThrow(TABLE_PHONG_BAN, null, values) > 0;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return isInsertSuccessful;
    }

    // Xoá phòng ban
    public boolean xoaPhongBan(String maPB) {
        SQLiteDatabase db = getWritableDatabase();
        boolean isDeleteSuccessful = false;
        db.beginTransaction();
        try {
            isDeleteSuccessful = db.delete(TABLE_PHONG_BAN, KEY_PHONG_BAN_MA + "= ?", new String[]{maPB}) > 0;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return isDeleteSuccessful;
    }

    // Cập nhật phòng ban
    public boolean capNhatPhongBan(PhongBan phongBan) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHONG_BAN_MA, phongBan.getMaPB());
        values.put(KEY_PHONG_BAN_TENPB, phongBan.getTenPB());
        return db.update(TABLE_PHONG_BAN, values, KEY_PHONG_BAN_MA + "= ?",
                new String[]{phongBan.getMaPB()}) > 0;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> listNV = new ArrayList<>();
        // SELECT * FROM nhanviens LEFT OUTER JOIN phongbans ON nhanviens.phongBan = phongbans.maPB
        String NHANVIEN_SELECT_QUERY = String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                TABLE_NHANVIEN, TABLE_PHONG_BAN, TABLE_NHANVIEN, KEY_NHANVIEN_PHONG_BAN, TABLE_PHONG_BAN, KEY_PHONG_BAN_MA);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(NHANVIEN_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    PhongBan phongBan = new PhongBan();
                    phongBan.setMaPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_MA)));
                    phongBan.setTenPB(cursor.getString(cursor.getColumnIndex(KEY_PHONG_BAN_TENPB)));

                    NhanVien nhanVien = new NhanVien();
                    nhanVien.setMaNV(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_MA)));
                    nhanVien.setTenNV(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_TEN)));
                    nhanVien.setGioiTinh(cursor.getInt(cursor.getColumnIndex(KEY_NHANVIEN_GIOI_TINH)) > 0);
                    nhanVien.setPhongBan(phongBan);
                    try {
                        nhanVien.setNgaySinh(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_NGAY_SINH))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listNV.add(nhanVien);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null & !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listNV;
    }

    // thêm nhân viên mới
    public boolean themNhanVien(NhanVien nhanVien) {
        SQLiteDatabase db = getWritableDatabase();
        boolean isThemThanhCong = false;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NHANVIEN_MA, nhanVien.getMaNV());
            values.put(KEY_NHANVIEN_TEN, nhanVien.getTenNV());
            values.put(KEY_NHANVIEN_GIOI_TINH, nhanVien.isGioiTinh());
            values.put(KEY_NHANVIEN_PHONG_BAN, nhanVien.getPhongBan().getMaPB());
            values.put(KEY_NHANVIEN_NGAY_SINH, dateFormat.format(nhanVien.getNgaySinh()));
            isThemThanhCong = db.insertOrThrow(TABLE_NHANVIEN, null, values) > 0;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return isThemThanhCong;
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NHANVIEN_MA, nhanVien.getMaNV());
        values.put(KEY_NHANVIEN_TEN, nhanVien.getTenNV());
        values.put(KEY_NHANVIEN_GIOI_TINH, nhanVien.isGioiTinh());
        values.put(KEY_NHANVIEN_PHONG_BAN, nhanVien.getPhongBan().getMaPB());
        values.put(KEY_NHANVIEN_NGAY_SINH, dateFormat.format(nhanVien.getNgaySinh()));
        return db.update(TABLE_NHANVIEN, values, KEY_NHANVIEN_MA + "=?", new String[]{nhanVien.getMaNV()}) > 0;
    }

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = getWritableDatabase();
        boolean isXoaThanhCong = false;
        db.beginTransaction();
        try {
            isXoaThanhCong = db.delete(TABLE_NHANVIEN, KEY_NHANVIEN_MA + " =?", new String[]{maNV}) > 0;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return isXoaThanhCong;
    }

    public NhanVien getNhanVienByMa(String maNV) {
        NhanVien nhanVien = null;
        String GET_NHANVIEN_QUERY = "SELECT * FROM " + TABLE_NHANVIEN + " WHERE " + KEY_NHANVIEN_MA + " = '" + maNV + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(GET_NHANVIEN_QUERY, null);
        try {
            if(cursor.moveToFirst()) {
                nhanVien = new NhanVien();
                nhanVien.setMaNV(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_MA)));
                nhanVien.setTenNV(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_TEN)));
                nhanVien.setGioiTinh(cursor.getInt(cursor.getColumnIndex(KEY_NHANVIEN_GIOI_TINH)) > 0);
                nhanVien.setPhongBan(getPhongBanByMa(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_PHONG_BAN))));
                nhanVien.setNgaySinh(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_NHANVIEN_NGAY_SINH))));
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nhanVien;
    }
}
