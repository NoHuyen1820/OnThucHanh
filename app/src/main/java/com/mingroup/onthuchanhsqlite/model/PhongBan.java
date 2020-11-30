package com.mingroup.onthuchanhsqlite.model;

import java.io.Serializable;
import java.util.Objects;

public class PhongBan implements Serializable {
    private String maPB;
    private String tenPB;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhongBan phongBan = (PhongBan) o;
        return Objects.equals(maPB, phongBan.maPB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPB);
    }

    // [333] Phòng nhân sự
    @Override
    public String toString() {
        return "[" + maPB + "] " + tenPB;
    }

    public PhongBan() {
    }

    public PhongBan(String maPB, String tenPB) {
        this.maPB = maPB;
        this.tenPB = tenPB;
    }

    public String getMaPB() {
        return maPB;
    }

    public void setMaPB(String maPB) {
        this.maPB = maPB;
    }

    public String getTenPB() {
        return tenPB;
    }

    public void setTenPB(String tenPB) {
        this.tenPB = tenPB;
    }
}
