package com.xianyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xianyi.R;
import com.xianyi.bean.HistoryListBean;
import com.xianyi.bean.MyCollectListBean;

import java.util.ArrayList;

/**
 * ${todo}<我的浏览历史列表Adapter>
 *
 * @author lht
 * @data: on 15/12/22 16:55
 */
public class HistoryListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater mInflater;
    private ArrayList<HistoryListBean> bankList;
    private int currentType;//当前item类型

    public HistoryListAdapter(Context ctx, ArrayList<HistoryListBean> bankList) {
        this.ctx = ctx;
        this.bankList = bankList;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bankList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return bankList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public int getViewTypeCount() {
        return bankList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder oneHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate((R.layout.activity_history_item), null);
            oneHolder = new ViewHolder();

            oneHolder.ly_title = (RelativeLayout) convertView.findViewById(R.id.ly_title);
            oneHolder.tv_state = (TextView) convertView .findViewById(R.id.tv_state);
            oneHolder.im_icon = (ImageView) convertView .findViewById(R.id.im_icon);
            oneHolder.gv_main_list_one_price_now = (TextView) convertView .findViewById(R.id.gv_main_list_one_price_now);
            oneHolder.gv_main_list_one_price_old = (TextView) convertView .findViewById(R.id.gv_main_list_one_price_old);
            oneHolder.tv_my_collect_context = (TextView) convertView .findViewById(R.id.tv_my_collect_context);

            convertView.setTag(oneHolder);
        } else {
            oneHolder = (ViewHolder) convertView.getTag();
        }
        // 0-没有顶部布局，1没有底部布局
        if("1".equals(bankList.get(position).getType())){
            oneHolder.ly_title.setVisibility(View.VISIBLE);
            oneHolder.tv_state.setVisibility(View.VISIBLE);

            oneHolder.tv_state.setText(bankList.get(position).getState());
        }else{
            oneHolder.ly_title.setVisibility(View.GONE);
            oneHolder.tv_state.setVisibility(View.GONE);
        }

        oneHolder.gv_main_list_one_price_now.setText(bankList.get(position).getPrice_now());
        oneHolder.gv_main_list_one_price_old.setText(bankList.get(position).getPrice_old());
        oneHolder.tv_my_collect_context.setText(bankList.get(position).getContext());

        return convertView;
    }

    class ViewHolder {
        RelativeLayout ly_title;
        TextView tv_state;
        ImageView im_icon;
        TextView gv_main_list_one_price_now;
        TextView gv_main_list_one_price_old;
        TextView tv_my_collect_context;
    }

}
