package android.com.omdb.helper;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Rahul D on 4/24/17.
 */

public class CustomTypeface {
    private static Typeface mFontRegular;
    private static Typeface mFontMedium;

    public static Typeface getFontRegular(Context context) {
        if (mFontRegular == null) {
            mFontRegular = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_regular.ttf");
        }
        return mFontRegular;
    }

    public static Typeface getFontBold(Context context) {
        if (mFontMedium == null) {
            mFontMedium = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_bold.ttf");
        }
        return mFontMedium;
    }


}

