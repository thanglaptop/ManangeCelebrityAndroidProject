package stu.edu.vn.quanlycelebrity.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import stu.edu.vn.quanlycelebrity.MainActivity;
import stu.edu.vn.quanlycelebrity.R;

public class DBConfigUtil {
    public static final String DB_NAME ="dbcelebrity.sqlite";
    public static final String DB_PATH_SUFFIX ="/databases/";

    public static void copyFileFromAssets(Context context){
        File dbFile = context.getDatabasePath(DB_NAME);
        if(!dbFile.exists()){
            File dbDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!dbDir.exists()){
                dbDir.mkdir();
            }
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                String outputFilePath = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0){
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            }catch (Exception e){
                Log.e(context.getString(R.string.s_error), e.toString());
            }
        }
    }

}
