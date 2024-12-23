package stu.edu.vn.quanlycelebrity.adapter;

import android.app.Activity;
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
import stu.edu.vn.quanlycelebrity.model.Type;

public class TypeAdapter  extends ArrayAdapter<Type> {
    Activity context;
    int resource;
    List<Type> objects;

    public TypeAdapter(Activity context, int resource, List<Type> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource=resource;
        this.objects=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource,null);

        TextView txtMaloai = item.findViewById(R.id.txtMaloai);
        TextView txtTenloai = item.findViewById(R.id.txtTenloai);

        Type t = this.objects.get(position);
        txtMaloai.setText(context.getString(R.string.s_typeid) +" " + t.getMaloai());
        txtTenloai.setText(context.getString(R.string.s_typename) + " " + t.getTenloai());

        return item;
    }
}
