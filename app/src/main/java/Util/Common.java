package Util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.k9_pxz.R;

import java.io.File;


public class Common {
    private static final String TAG = "Common";

    public static String getAppPath(Context context) {
        //android.os.Environment.getExternalStorageDirectory()
        /*File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File dir = new File(path, "DemoPicture.jpg");*/

        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );
        if (!dir.exists())
            Log.d(TAG, "getAppPath:  create directory");
        //dir.mkdir();
        dir.mkdirs();

        Log.d(TAG, "getAppPath:" + dir.getPath());
        return dir.getPath() + File.separator;
    }
}
