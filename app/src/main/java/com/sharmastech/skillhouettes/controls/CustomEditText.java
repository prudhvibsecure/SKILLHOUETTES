package com.sharmastech.skillhouettes.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.common.AppFonts;

import java.lang.reflect.Field;

public class CustomEditText extends EditText {
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	/*public CustomTextView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		
		init(context, attrs);
	}*/

	boolean canPaste()
	{
		return false;
	}

	@Override
	public boolean isSuggestionsEnabled()
	{
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// setInsertionDisabled when user touches the view
			this.setInsertionDisabled();
		}
		return super.onTouchEvent(event);
	}

	private void setInsertionDisabled() {
		try {
			Field editorField = TextView.class.getDeclaredField("mEditor");
			editorField.setAccessible(true);
			Object editorObject = editorField.get(this);

			Class editorClass = Class.forName("android.widget.Editor");
			Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
			mInsertionControllerEnabledField.setAccessible(true);
			mInsertionControllerEnabledField.set(editorObject, false);
		}
		catch (Exception ignored) {
			// ignore exception here
		}
	}

	public void init(Context context, AttributeSet attrs) {

		this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
		this.setLongClickable(false);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTextView);

		int fontType = a.getInteger(R.styleable.CustomTextView_fontType, -1);
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

		default:
			break;
		}
	}
	private class ActionModeCallbackInterceptor implements ActionMode.Callback
	{
		public boolean onCreateActionMode(ActionMode mode, Menu menu) { return false; }
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) { return false; }
		public void onDestroyActionMode(ActionMode mode) {}
	}

}
