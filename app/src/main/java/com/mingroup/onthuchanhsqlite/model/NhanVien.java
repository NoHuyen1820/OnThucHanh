package com.mingroup.onthuchanhsqlite.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class NhanVien implements Serializable {
    private String maNV;
    private String tenNV;
    private boolean gioiTinh;
    private PhongBan phongBan;
    private Date ngaySinh;

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNV, nhanVien.maNV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNV);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", phongBan=" + phongBan +
                '}';
    }

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, boolean gioiTinh, PhongBan phongBan, Date ngaySinh) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.phongBan = phongBan;
        this.ngaySinh = ngaySinh;
    }

    public NhanVien(String maNV, String tenNV, boolean gioiTinh, PhongBan phongBan) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.phongBan = phongBan;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public PhongBan getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(PhongBan phongBan) {
        this.phongBan = phongBan;
    }
}
