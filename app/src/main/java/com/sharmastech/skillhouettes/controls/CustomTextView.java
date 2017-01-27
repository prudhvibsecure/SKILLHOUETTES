package com.sharmastech.skillhouettes.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.common.AppFonts;

public class CustomTextView extends TextView {

    private int fontType = -1;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

	/*public CustomTextView(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		
		init(context, attrs);
	}*/

    public void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);

        fontType = a.getInteger(R.styleable.CustomTextView_fontType, -1);
        a.recycle();

        switch (fontType) {
            case 1:
                //this.setTypeface(AppFonts.getInstance(context).getLightFont());
                break;

            case 2:
                //this.setTypeface(AppFonts.getInstance(context).getMediumFont());
                break;

            case 3:
                this.setTypeface(AppFonts.getInstance(context).getOpificioBoldWebfont());
                break;

            case 4:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont());
                break;

            case 5:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont(),
                        Typeface.BOLD);
                break;

            case 6:
                this.setTypeface(AppFonts.getInstance(context).getZawgyiFont());
                break;

            /*case 7:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFontZawgyiWeb(), Typeface.BOLD);
                break;

            case 8:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFontZawgyiWeb());
                break;*/

            default:
                break;
        }

    }

}
