package com.xianyi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xianyi.R;

/**
 * ${todo}<全部分类右列表适配器>
 *
 * @author lht
 * @data: on 15/11/25 16:21
 */
public class ClassifyAllRightAdapter extends BaseAdapter {
	private Context ctx;
	private String[] text;
	private int position = 0;
	private int layout;

	public ClassifyAllRightAdapter(Context ctx, String[] text) {
		this.ctx = ctx;
		this.text = text;
	}

	public ClassifyAllRightAdapter(Context ctx, String[] text, int layout) {
		this.ctx = ctx;
		this.text = text;
		this.layout = layout;
	}

	public int getCount() {
		return text.length;
	}

	public Object getItem(int arg0) {
		return text[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, layout, null);
			hold.txt = (TextView) arg1.findViewById(R.id.right_item_txt);
			hold.layout = (LinearLayout) arg1.findViewById(R.id.right_item_list);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.txt.setText(text[arg0]);
		hold.layout.setBackgroundResource(R.drawable.my_list_txt_background);
//		hold.txt.setTextColor(Color.parseColor("#FF666666"));
//		if (arg0 == position) {
//			hold.layout.setBackgroundResource(R.drawable.search_more_morelisttop_bkg);
//			hold.txt.setTextColor(Color.parseColor("#FFFF8C00"));
//		}
		return arg1;
	}

	public void setSelectItem(int i) {
		position = i;
	}

	private static class Holder {
		LinearLayout layout;
		TextView txt;
	}
}
