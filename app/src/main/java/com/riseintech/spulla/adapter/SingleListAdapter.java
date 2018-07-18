package com.riseintech.spulla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.SingleListCheckBox;

import java.util.List;



public class SingleListAdapter extends BaseAdapter{
	Context ctx;
	LayoutInflater lInflater;
	List<String> data;

	public SingleListAdapter(Context context, List<String> data) {
		ctx = context;
		this.data = data;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

		if (convertView == null) {
            convertView = lInflater.inflate(R.layout.sort_single_list_choice_items, parent, false);

            holder = new ViewHolder();
            holder.checkBox = (SingleListCheckBox) convertView.findViewById(R.id.singleitemCheckBox);
            //holder.checkBox.setTypeface(FontTypefaceUtils.getRobotoCondensedRegular(ctx));
            convertView.setTag(holder);
		}else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(data.get(position));
		
		return convertView;
	}
    public class ViewHolder {
        SingleListCheckBox checkBox;
    }

}