package com.mingroup.onthuchanhsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mingroup.onthuchanhsqlite.model.PhongBan;

import java.util.ArrayList;
import java.util.List;

public class QLPhongBanActivity extends AppCompatActivity {
    private EditText etMaPB, etTenPB;
    private Button btnThem, btnXoa, btnCapNhat, btnThemMoi;

    private ListView lvPhongBan;
    private ArrayAdapter<PhongBan> phongBanAdapter;
    private List<PhongBan> phongBans;
    private int lastPosition = -1;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_l_phong_ban);
        init();
        displayPhongBanListView();
    }

    private void displayPhongBanListView() {
        phongBans = dbHelper.getAllPhongBan();
        phongBanAdapter = new ArrayAdapter<PhongBan>(this, android.R.layout.simple_list_item_single_choice, phongBans);
        lvPhongBan.setAdapter(phongBanAdapter);

        lvPhongBan.setItemsCanFocus(false);
        lvPhongBan.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        lvPhongBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                lastPosition = position;
                PhongBan phongBan = phongBanAdapter.getItem(position);
                etMaPB.setText(phongBan.getMaPB());
                etTenPB.setText(phongBan.getTenPB());
                etMaPB.setEnabled(false);
            }
        });
    }

    private void init() {
        etMaPB = findViewById(R.id.etMaNV);
        etTenPB = findViewById(R.id.etTenNV);
        btnThem = findViewById(R.id.btnThemNV);
        btnXoa = findViewById(R.id.btnXoaNV);
        btnCapNhat = findViewById(R.id.btnCapNhatNV);
        lvPhongBan = findViewById(R.id.lvNhanVien);
        dbHelper = new MyDatabaseHelper(this);
        btnThemMoi = findViewById(R.id.btnThemMoi);
    }

    public void themPhongBan(View view) {
        PhongBan pb = validateForm();
        if(pb != null) {
            if(dbHelper.themPhongBan(pb)) {
                phongBanAdapter.add(pb);
                phongBanAdapter.notifyDataSetChanged();
                clearForm();
            }
        }
    }

    private void clearForm() {
        etTenPB.setText("");
        etMaPB.setText("");
    }

    private PhongBan validateForm() {
        etMaPB.setError(null);
        etTenPB.setError(null);
        String maPB = etMaPB.getText().toString();
        String tenPB = etTenPB.getText().toString();

        if(maPB.isEmpty()) {
            etMaPB.setError("Bắt buộc nhập");
            return null;
        } else {
            for(PhongBan pb : phongBans) {
                if(pb.getMaPB().equalsIgnoreCase(maPB)){
                    etMaPB.setError("Trùng mã. Vui lòng nhập lại!");
                    return null;
                }
            }
        }

        if(tenPB.isEmpty()) {
            etTenPB.setError("Bắt buộc nhập");
            return null;
        }

        return new PhongBan(maPB, tenPB);
    }

    private PhongBan validateFormUpdate() {
        etMaPB.setError(null);
        etTenPB.setError(null);

        String maPB = etMaPB.getText().toString();
        String tenPB = etTenPB.getText().toString();

        if(tenPB.isEmpty()) {
            etTenPB.setError("Bắt buộc nhập");
            return null;
        }

        return new PhongBan(maPB, tenPB);
    }

    public void xoaPhongBan(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắn chắn muốn xoá phòng ban này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhongBan pb = phongBanAdapter.getItem(lastPosition);
                        if(dbHelper.xoaPhongBan(pb.getMaPB())) {
                            phongBanAdapter.remove(pb);
                            phongBanAdapter.notifyDataSetChanged();
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
    }

    public void capNhatPhongBan(View view) {
        PhongBan pb = validateFormUpdate();
        if(pb != null) {
            if(dbHelper.capNhatPhongBan(pb)) {
                phongBanAdapter.remove(pb);
                phongBanAdapter.add(pb);
                phongBanAdapter.notifyDataSetChanged();
            }
        }
    }

    public void themMoi(View view) {
        clearForm();
        etMaPB.setEnabled(true);
    }
}