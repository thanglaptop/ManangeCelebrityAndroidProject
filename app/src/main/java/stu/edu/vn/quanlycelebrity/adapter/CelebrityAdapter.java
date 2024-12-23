package stu.edu.vn.quanlycelebrity.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import stu.edu.vn.quanlycelebrity.R;
import stu.edu.vn.quanlycelebrity.model.Celebrity;
import stu.edu.vn.quanlycelebrity.util.DBConfigUtil;

public class CelebrityAdapter extends ArrayAdapter<Celebrity> {
    Activity context;
    int resource;
    List<Celebrity> objects;

    public CelebrityAdapter(Activity context, int resource, List<Celebrity> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource=resource;
        this.objects=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource,null);

        ImageView imgHinh = item.findViewById(R.id.imgHinh);
        TextView txtMa = item.findViewById(R.id.txtMa);
        TextView txtTen = item.findViewById(R.id.txtTen);
        TextView txtPhanloai = item.findViewById(R.id.txtPhanloai);

        Celebrity celeb = this.objects.get(position);
        Bitmap bitmap = celeb.getHinhAsBitmap();
        if(bitmap != null){
            imgHinh.setImageBitmap(bitmap);
        }
        else{
            //co thể bỏ hình mặc định
            imgHinh.setImageResource(R.drawable.ic_launcher_foreground);
        }
        txtMa.setText(context.getString(R.string.s_id) +" " + celeb.getMa());
        txtTen.setText(context.getString(R.string.s_fullname) + " " + celeb.getTen());
        SQLiteDatabase database = context.openOrCreateDatabase(DBConfigUtil.DB_NAME, context.MODE_PRIVATE, null);
        String typename = "";
        String query = "SELECT * FROM type WHERE maloai = ?";
        String[] selectionArgs = new String[]{String.valueOf(celeb.getPhanloai())};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            typename = cursor.getString(1); // Cột "name" trong bảng type
        }
        cursor.close();
        database.close();
        txtPhanloai.setText(context.getString(R.string.s_type) + " " + typename);

        return item;
    }
}
