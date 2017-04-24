package android.com.omdb.helper;

import android.com.omdb.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;


/**
 * Created by Rahul D on 4/25/17.
 */
public class OmdbTextView extends android.support.v7.widget.AppCompatTextView {

    public OmdbTextView(Context context) {
        super(context);
    }

    public OmdbTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.OmdbTextView);
        String fontName = styledAttrs.getString(R.styleable.OmdbTextView_omdb_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            switch (fontName) {
                case "regular":
                    setTypeface(CustomTypeface.getFontRegular(context));
                    break;
                case "bold":
                    setTypeface(CustomTypeface.getFontBold(context));
                    break;
            }
        }

    }
}