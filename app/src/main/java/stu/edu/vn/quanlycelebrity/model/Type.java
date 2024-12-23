package stu.edu.vn.quanlycelebrity.model;

import java.io.Serializable;

public class Type implements Serializable {
    int maloai;
    String tenloai;

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public Type() {
    }

    public Type(int maloai, String tenloai) {

        this.maloai = maloai;
        this.tenloai = tenloai;
    }

    @Override
    public String toString() {
        return tenloai;
    }
}
