package com.sharmastech.skillhouettes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class StudentsSkillsAdapter extends RecyclerView.Adapter<StudentsSkillsAdapter.ContactViewHolder> {

    private JSONArray array = new JSONArray();

    private Context context = null;

    private OnClickListener onClickListener;

    private ImageLoader imageLoader;

    public StudentsSkillsAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoader(context, false);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {

        try {

            JSONObject jsonObject = array.getJSONObject(i);

            contactViewHolder.tv_title.setText(Html.fromHtml(jsonObject.optString("title")));

            imageLoader.DisplayImage( jsonObject.optString("img").trim(), contactViewHolder.iv_icon, 2);


        } catch (Exception e) {
            TraceUtils.logException(e);
        }

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_studentsskills_frag, viewGroup,
                false);

        return new ContactViewHolder(itemView);
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