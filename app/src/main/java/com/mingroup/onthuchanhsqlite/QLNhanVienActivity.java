package com.mingroup.onthuchanhsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mingroup.onthuchanhsqlite.model.NhanVien;
import com.mingroup.onthuchanhsqlite.model.PhongBan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QLNhanVienActivity extends AppCompatActivity {
    private EditText etMaNV, etTenNV;
    private RadioGroup rgGioiTinh;
    private RadioButton rbNam, rbNu;
    private TextView tvNgaySinh;
    private Button btnThem, btnXoa, btnCapNhat, btnThemMoi;

    private Spinner spnPhongBan;
    private ArrayAdapter<PhongBan> phongBanAdapter;
    private List<PhongBan> phongBans;

    private MyDatabaseHelper dbHelper;

    private ListView lvNhanVien;
    private NhanVienAdapter nhanVienAdapter;
    private List<NhanVien> nhanViens;

    // date picker dialog
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;

    private SimpleDateFormat dateFormat;

    private int lastSelectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_l_nhan_vien);
        init();
        displayPhongBanSpinner();
        displayNhanVienListView();
        setupDatePicker();

        Calendar calendar = Calendar.getInstance();
        lastSelectedYear = calendar.get(Calendar.YEAR);
        lastSelectedMonth = calendar.get(Calendar.MONTH);
        lastSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());

        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastSelectedPosition = i;
                NhanVien nhanVien = nhanVienAdapter.getItem(i);
                etMaNV.setText(nhanVien.getMaNV());
                etTenNV.setText(nhanVien.getTenNV());

                if (nhanVien.isGioiTinh()) {
                    rgGioiTinh.check(rbNam.getId());
                } else {
                    rgGioiTinh.check(rbNu.getId());
                }

                spnPhongBan.setSelection(phongBanAdapter.getPosition(nhanVien.getPhongBan()));
                Log.d("bday", nhanVien.getNgaySinh().toString());
                tvNgaySinh.setText(dateFormat.format(nhanVien.getNgaySinh()));

                etMaNV.setEnabled(false);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhanVien nhanVien = validateForm();
                if (nhanVien != null) {
                    if (dbHelper.themNhanVien(nhanVien)) {
                        nhanVienAdapter.add(nhanVien);
                        nhanVienAdapter.notifyDataSetChanged();
                        clearForm();
                    }
                }
            }
        });

        btnXoa.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có chắn chắn muốn xoá phòng ban này?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NhanVien nv = nhanVienAdapter.getItem(lastSelectedPosition);
                            if (dbHelper.xoaNhanVien(nv.getMaNV())) {
                                nhanVienAdapter.remove(nv);
                                nhanVienAdapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        });

        btnThemMoi.setOnClickListener(view -> {
            clearForm();
            etMaNV.setEnabled(true);
        });

        btnCapNhat.setOnClickListener(view -> {
            NhanVien nv = validateUpdateForm();
            if(nv != null) {
                if(dbHelper.capNhatNhanVien(nv)) {
                    nhanVienAdapter.remove(nv);
                    nhanVienAdapter.add(nv);
                    nhanVienAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tvNgaySinh.setText(year + "-" + (month + 1) + "-" + day);
                lastSelectedYear = year;
                lastSelectedMonth = month;
                lastSelectedDay = day;
            }
        };

        tvNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(QLNhanVienActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay)
                        .show();
            }
        });
    }

    private void displayNhanVienListView() {
        nhanViens = dbHelper.getAllNhanVien();
        nhanVienAdapter = new NhanVienAdapter(this, nhanViens);
        lvNhanVien.setAdapter(nhanVienAdapter);
    }

    private void displayPhongBanSpinner() {
        phongBans = dbHelper.getAllPhongBan();
        phongBanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongBans);
        spnPhongBan.setAdapter(phongBanAdapter);
    }

    private void init() {
        etMaNV = findViewById(R.id.etMaNV);
        etTenNV = findViewById(R.id.etTenNV);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbNam = findViewById(R.id.rbMale);
        rbNu = findViewById(R.id.rbFemale);
        spnPhongBan = findViewById(R.id.spnPhongBan);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        btnThem = findViewById(R.id.btnThemNV);
        btnXoa = findViewById(R.id.btnXoaNV);
        btnCapNhat = findViewById(R.id.btnCapNhatNV);
        btnThemMoi = findViewById(R.id.btnThemMoi);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        dbHelper = new MyDatabaseHelper(this);
    }

    private void clearForm() {
        etMaNV.setText("");
        etTenNV.setText("");
        rgGioiTinh.check(rbNam.getId());
        spnPhongBan.setSelection(0);
    }

    private NhanVien validateForm() {
        String maNV = etMaNV.getText().toString();
        String tenNV = etTenNV.getText().toString();
        boolean gioiTinh = rbNam.isChecked();
        PhongBan phongBan = phongBanAdapter.getItem(spnPhongBan.getSelectedItemPosition());
        Date ngaySinh = null;
        try {
            ngaySinh = dateFormat.parse(tvNgaySinh.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (maNV.isEmpty()) {
            etMaNV.setError("Bắt buộc nhập");
            return null;
        } else {
            if (dbHelper.getNhanVienByMa(maNV) != null) {
                etMaNV.setError("Trùng mã");
                return null;
            }
        }

        if (tenNV.isEmpty()) {
            etTenNV.setError("Bắt buộc nhập");
            return null;
        }

        return new NhanVien(maNV, tenNV, gioiTinh, phongBan, ngaySinh);
    }

    private NhanVien validateUpdateForm() {
        String maNV = etMaNV.getText().toString();
        String tenNV = etTenNV.getText().toString();
        boolean gioiTinh = rbNam.isChecked();
        PhongBan phongBan = phongBanAdapter.getItem(spnPhongBan.getSelectedItemPosition());
        Date ngaySinh = null;
        try {
            ngaySinh = dateFormat.parse(tvNgaySinh.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (tenNV.isEmpty()) {
            etTenNV.setError("Bắt buộc nhập");
            return null;
        }

        return new NhanVien(maNV, tenNV, gioiTinh, phongBan, ngaySinh);
    }
}