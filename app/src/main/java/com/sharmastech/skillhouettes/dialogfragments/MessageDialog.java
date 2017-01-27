package com.sharmastech.skillhouettes.dialogfragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;

public class MessageDialog extends DialogFragment {

    public static MessageDialog newInstance(Bundle bundle) {

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle mArgs = getArguments();

        View view = inflater.inflate(R.layout.popup_message_ok, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4)
                    return true;
                return false;
            }
        });

           ((TextView) view.findViewById(R.id.tv_title)).setText(mArgs
                   .getString("title"));

        ((TextView) view.findViewById(R.id.tv_message)).setText(mArgs
                .getString("message"));

           view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {

                   String tag = getString("tag", "", mArgs);
                   if (tag.trim().length() > 0) {
                       Intent intent = new Intent();
                       intent.setAction(tag);
                       LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                   }
                   MessageDialog.this.dismiss();

               }
           });

           view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {

                   MessageDialog.this.dismiss();
               }
           });

           view.findViewById(R.id.tv_cancel).setVisibility(mArgs.getBoolean("showcancel", false) ? View.VISIBLE : View.GONE);

           view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {

                   MessageDialog.this.dismiss();
               }
           });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public String getString(String key, String defaultValue, Bundle Bundle) {
        final String s = Bundle.getString(key);
        return (s == null) ? defaultValue : s;
    }

}
