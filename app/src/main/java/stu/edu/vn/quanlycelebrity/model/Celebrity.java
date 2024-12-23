package stu.edu.vn.quanlycelebrity.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

public class Celebrity implements Serializable {
    int ma;
    String ten;
    int phanloai;
    byte[] hinh;
    String quequan;
    int namsinh;

    // Phương thức chuyển mảng byte thành Bitmap để hiển thị hình ảnh
    public Bitmap getHinhAsBitmap() {
        if (hinh != null) {
            return BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        }
        return null;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(int phanloai) {
        this.phanloai = phanloai;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    public String getQuequan() {
        return quequan;
    }

    public void setQuequan(String quequan) {
        this.quequan = quequan;
    }

    public int getNamsinh() {
        return namsinh;
    }

    public void setNamsinh(int namsinh) {
        this.namsinh = namsinh;
    }

    public Celebrity(int ma, String ten, int phanloai, String quequan, int namsinh) {
        this.ma = ma;
        this.ten = ten;
        this.phanloai = phanloai;
        this.quequan = quequan;
        this.namsinh = namsinh;
    }

    public Celebrity(int ma, String ten, int phanloai, byte[] hinh, String quequan, int namsinh) {
        this.ma = ma;
        this.ten = ten;
        this.phanloai = phanloai;
        this.hinh = hinh;
        this.quequan = quequan;
        this.namsinh = namsinh;
    }

    public Celebrity() {
    }
}
