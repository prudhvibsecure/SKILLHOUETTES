package com.sharmastech.skillhouettes.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.sharmastech.skillhouettes.R;

public class ParentFragment extends Fragment {

    public boolean back() {
        return false;
    }

    public String getFragmentName() {
        return getString(R.string.app_name);
    }

    public void onFragmentChildClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int colorId, int secondaryColorId, int stringid, boolean blockMenu, boolean showTitle, int icon);
    }

}
