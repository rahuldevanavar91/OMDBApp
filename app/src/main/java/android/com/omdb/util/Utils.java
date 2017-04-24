package android.com.omdb.util;

import android.com.omdb.R;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Rahul D on 4/25/17.
 */

public class Utils {
    public static void setStatusBarColor(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }
}
