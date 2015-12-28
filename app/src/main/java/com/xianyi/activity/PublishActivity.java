package com.xianyi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xianyi.R;
import com.xianyi.customviews.TitleView;
import com.xianyi.interfaces.IRecordFinish;
import com.xianyi.localalbum.common.ImageUtils;
import com.xianyi.localalbum.common.LocalImageHelper;
import com.xianyi.localalbum.ui.DynamicPost;
import com.xianyi.localalbum.ui.LocalAlbumDetail;
import com.xianyi.localalbum.widget.FilterImageView;
import com.xianyi.utils.LogUtil;
import com.xianyi.utils.PictureUtil;
import com.xianyi.utils.RecordMediaPlayer;
import com.xianyi.utils.RecordTools;
import com.xianyi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends BaseActivity implements IRecordFinish {
    /**
     * 上下文
     **/
    private Context mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    MediaAdapter adapter;
    GridView add_detail_gridview;
    TextView fenlei_info, zuodiansha_info;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_layout);
        mContext = this;
        mTitleView = (TitleView) findViewById(R.id.title);
        mTitleView.setTitle("发布闲置");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        add_detail_gridview = (GridView) findViewById(R.id.add_detail_gridview);
        adapter = new MediaAdapter(this);
        add_detail_gridview.setAdapter(adapter);
        add_detail_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeidaType meidaType = (MeidaType) adapter.getItem(position);
                if (meidaType.type.equals("1")) {//图片
                    Intent intent = new Intent(PublishActivity.this, PicLookActivity.class);
                    intent.putExtra("picpath", meidaType.pathString);
                    startActivity(intent);
                } else if (meidaType.type.equals("2")) {//语音
                    RecordMediaPlayer player = RecordMediaPlayer.getInstance();
                    player.play(meidaType.pathString);
                } else if (meidaType.type.equals("3")) {//添加图片
//                    //使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
//                    Intent intent1 = new Intent(mContext,
//                            SelectPicPopupWindow.class);
//                    startActivityForResult(intent1, PICKPHOTO);
                    Intent intent = new Intent(mContext, LocalAlbumDetail.class);
                    startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
                } else if (meidaType.type.equals("4")) {//添加语音
//                    RecordMediaPlayer player = RecordMediaPlayer.getInstance();
//                    player.play(meidaType.pathString);
                    RecordTools recordTools = new RecordTools(mContext, PublishActivity.this);
                    recordTools.showVoiceDialog();
                }
            }
        });
        add_detail_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MeidaType meidaType = (MeidaType) adapter.getItem(position);
                meidaType.isShowDeleteed = true;
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        fenlei_info= (TextView) findViewById(R.id.fenlei_info);
        zuodiansha_info= (TextView) findViewById(R.id.zuodiansha_info);
        //设置ImageLoader参数
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageForEmptyUri(R.drawable.dangkr_no_picture_small)
                .showImageOnFail(R.drawable.dangkr_no_picture_small)
                .showImageOnLoading(R.drawable.dangkr_no_picture_small)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        initPicData();
    }

    public void initPicData() {
        MeidaType meidaType_pic = new MeidaType();
        meidaType_pic.type = "3";
        MeidaType meidaType_voice = new MeidaType();
        meidaType_voice.type = "4";
        adapter.getData().add(meidaType_pic);
        adapter.getData().add(meidaType_voice);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fenlei:
                Intent intent = new Intent(mContext, PublishSelectTypeActivity.class);
                intent.putExtra("from","type");
                startActivityForResult(intent, SELECTTYPE);
                break;
            case R.id.zuodiansha:
                Intent mIntent = new Intent(mContext, PublishSelectTypeActivity.class);
                mIntent.putExtra("from","want");
                startActivityForResult(mIntent, TOGET);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("PublishActivity--onDestroy");
    }

    @Override
    public void RecondSuccess(String voicePath) {
        MeidaType meidaType = adapter.getData().get(adapter.getData().size() - 1);
        meidaType.type = "2";
        meidaType.pathString = voicePath;
        adapter.notifyDataSetChanged();

    }
    final int SELECTTYPE = 0;
    final int PICKPHOTO = 1;
    final int TOGET = 3;
    String selectedData;
    String huandiansha;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKPHOTO:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            MeidaType meidaType = new MeidaType();
                            meidaType.type = "1";
                            meidaType.pathString = picPath;
                            adapter.getData().add(0, meidaType);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case SELECTTYPE:

                if (data != null && resultCode == RESULT_OK) {
                    selectedData = data.getStringExtra("data");
                    LogUtil.d("###111selectedData:"+selectedData);
                    if(!TextUtils.isEmpty(selectedData)){
                        fenlei_info.setText(selectedData);
                    }else{
                        fenlei_info.setText("");
                    }


                }

                break;
            case TOGET:

                if (data != null && resultCode == RESULT_OK) {
                    huandiansha = data.getStringExtra("data");
                    LogUtil.d("###huandiansha:"+huandiansha);
                    if(!TextUtils.isEmpty(huandiansha)){
                        zuodiansha_info.setText(huandiansha);
                    }else{
                        zuodiansha_info.setText("");
                    }


                }

                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if (LocalImageHelper.getInstance().isResultOk()) {
                    LocalImageHelper.getInstance().setResultOk(false);
                    //获取选中的图片
                    List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
                    for (int i = 0; i < files.size(); i++) {
                        MeidaType meidaType = new MeidaType();
                        meidaType.type = "1";
                        meidaType.pathString = files.get(i).getThumbnailUri();
                        adapter.getData().add(0, meidaType);
                    }
                    adapter.notifyDataSetChanged();
                    //清空选中的图片
                    files.clear();
//                    //设置当前选中的图片数量
                    LocalImageHelper.getInstance().setCurrentSize(files.size());
                }
                //清空选中的图片
                LocalImageHelper.getInstance().getCheckedItems().clear();
                break;
            default:
                break;

        }
    }

    /**
     * 顶部布局--左按钮事件监听
     */
    public class TitleLeftOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }

    }

    //自定义适配器
    class MediaAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        int width;
        List<MeidaType> meidaTypeList = new ArrayList<MeidaType>();

        public MediaAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
            int[] screenSize = Utils.getScreenDispaly(mContext);
            width = screenSize[0];
        }

        public void setData(List<MeidaType> meidaTypeList) {
            meidaTypeList.clear();
            meidaTypeList.addAll(meidaTypeList);
            notifyDataSetChanged();
        }

        public List<MeidaType> getData() {
            return meidaTypeList;
        }

        @Override
        public int getCount() {
            if (null != meidaTypeList) {
                return meidaTypeList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return meidaTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.media_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.play = (ImageView) convertView.findViewById(R.id.play);
                viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
                viewHolder.tv_info = (TextView) convertView.findViewById(R.id.info_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MeidaType meidaType = meidaTypeList.get(position);
            if (meidaType.type.equals("1")) {//图片
                ImageLoader.getInstance().displayImage(meidaType.pathString, viewHolder.play, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }
                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        viewHolder.play.setImageResource(R.drawable.dangkr_no_picture_small);
                    }
                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }
                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                });
                viewHolder.tv_info.setVisibility(View.GONE);
                viewHolder.play.setScaleType(ImageView.ScaleType.FIT_XY);
            } else if (meidaType.type.equals("2")) {//语音
                ImageLoader.getInstance().displayImage(meidaType.pathString, viewHolder.play, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.voice_play);
                    }
                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        viewHolder.play.setImageResource(R.drawable.voice_play);
                    }
                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        viewHolder.play.setImageResource(R.drawable.voice_play);
                    }
                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.voice_play);
                    }
                });
                viewHolder.tv_info.setVisibility(View.GONE);
                viewHolder.play.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (meidaType.type.equals("3")) {//添加图片
//                viewHolder.play.setImageResource(R.drawable.btn_addpic);
                ImageLoader.getInstance().displayImage(meidaType.pathString, viewHolder.play, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.btn_addpic);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        viewHolder.play.setImageResource(R.drawable.btn_addpic);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        viewHolder.play.setImageResource(R.drawable.btn_addpic);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.btn_addpic);
                    }
                });
                viewHolder.tv_info.setVisibility(View.VISIBLE);
                viewHolder.play.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (meidaType.type.equals("4")) {//添加语音
                ImageLoader.getInstance().displayImage(meidaType.pathString, viewHolder.play, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.btn_record);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        viewHolder.play.setImageResource(R.drawable.btn_record);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        viewHolder.play.setImageResource(R.drawable.btn_record);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        viewHolder.play.setImageResource(R.drawable.btn_record);
                    }
                });
                viewHolder.tv_info.setVisibility(View.VISIBLE);
                viewHolder.play.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.play.getLayoutParams();
            LogUtil.d("##params.width:" + params.width);
            params.height = (width - 100) / 5;
            if (meidaType.isShowDeleteed) {
                viewHolder.delete.setVisibility(View.VISIBLE);
                viewHolder.delete.setTag(meidaType.pathString);

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String) v.getTag();
                        int index = -1;
                        MeidaType curType = null;
                        for (int i = 0; i < meidaTypeList.size(); i++) {
                            if (tag.equals(meidaTypeList.get(i).pathString)) {
                                curType = meidaTypeList.get(i);
                                index = i;

                                break;
                            }
                        }
                        if (curType != null && index >= 0 && curType.type.equals("2")) {
                            RecordMediaPlayer player = RecordMediaPlayer.getInstance();
                            player.deleteFile(curType.pathString);
                            MeidaType meidaType1 = meidaTypeList.get(index);
                            meidaType1.isShowDeleteed = false;
                            meidaType1.type = "4";
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        meidaTypeList.remove(index);
                        adapter.notifyDataSetChanged();


                    }
                });
            } else {
                viewHolder.delete.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    class ViewHolder {
        public ImageView play;
        public ImageView delete;
        public TextView tv_info;
    }

    class MeidaType {
        /**
         * 1：图片  2：语音  3:添加图片  4：添加语音
         */
        String type;
        String pathString;
        boolean isShowDeleteed;
    }

}
