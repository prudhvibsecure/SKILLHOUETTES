package com.sharmastech.skillhouettes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by w7u on 1/11/2017.
 */

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.ContactViewHolder> {

    private JSONArray array = new JSONArray();

    private Context context = null;

    private View.OnClickListener onClickListener;

    private ImageLoader imageLoader;

    public FeaturesAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoader(context, false);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    @Override
    public void onBindViewHolder(FeaturesAdapter.ContactViewHolder contactViewHolder, int i) {

        try {

            JSONObject jsonObject = array.getJSONObject(i);

            String status=jsonObject.optString("status");

            String featureno=jsonObject.optString("featureno");

            contactViewHolder.tv_title.setText(Html.fromHtml(jsonObject.optString("featurename")));

            String image_url = AppSettings.getInstance(context).getPropertyValue("i_path");

            String ad_img=image_url + jsonObject.optString("fimage");

            if (status.equalsIgnoreCase("yes")) {
                imageLoader.DisplayImage(ad_img, contactViewHolder.iv_icon, 2);
                return;
            }else {
                imageLoader.DisplayImage(ad_img, contactViewHolder.iv_icon, 2);
            }


        } catch (Exception e) {
            TraceUtils.logException(e);
        }

    }

    @Override
    public FeaturesAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feature_row, viewGroup,
                false);
        itemView.setOnClickListener(onClickListener);
        return new FeaturesAdapter.ContactViewHolder(itemView);
    }

    public int getCount() {

        return array.length();
    }

    public long getItemId(int position) {

        return position + 1;
    }

    public void setItems(JSONArray aArray) {

        this.array = aArray;
    }

    public JSONArray getItems() {

        return this.array;
    }

    public void clear() {

        array = null;
        array = new JSONArray();
    }

    public void release() {

        array = null;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_title;

        protected ImageView iv_icon;


        public ContactViewHolder(View v) {

            super(v);

            tv_title = (TextView) v.findViewById(R.id.tv_title);

            iv_icon = (ImageView) v.findViewById(R.id.iv_icon);

        }
    }

}