package com.mingroup.onthuchanhsqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingroup.onthuchanhsqlite.model.NhanVien;

import java.util.List;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {

    public NhanVienAdapter(@NonNull Context context, @NonNull List<NhanVien> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NhanVien nhanVien = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhan_vien, parent, false   );
        }
        ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
        TextView tvMaNV = convertView.findViewById(R.id.tvMaNV);
        TextView tvTenNV = convertView.findViewById(R.id.tvTenNV);
        TextView tvPhongBan = convertView.findViewById(R.id.tvPhongBan);

        if(nhanVien.isGioiTinh()) {
            ivAvatar.setImageResource(R.drawable.male);
        } else {
            ivAvatar.setImageResource(R.drawable.female);
        }
        tvMaNV.setText(nhanVien.getMaNV());
        tvTenNV.setText(nhanVien.getTenNV());
        tvPhongBan.setText(nhanVien.getPhongBan().getTenPB());

        return convertView;
    }
}
