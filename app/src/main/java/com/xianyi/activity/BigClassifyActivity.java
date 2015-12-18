package com.xianyi.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xianyi.R;
import com.xianyi.adapter.ClassifyAllBeiJingAdapter;
import com.xianyi.adapter.ClassifyAllLeftListAdapter;
import com.xianyi.adapter.ClassifyAllRightAdapter;
import com.xianyi.adapter.ClassifyMainListAdapter;
import com.xianyi.bean.ClassifyMainListBean;
import com.xianyi.customviews.ClassifyAllBeiJingPageControlView;
import com.xianyi.customviews.ClassifyAllBeiJingScrollLayout;
import com.xianyi.customviews.TitleView;
import com.xianyi.customviews.mylist.MyListView;
import com.xianyi.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${todo}<大类别列表页>
 *
 * @author lht
 * @data: on 15/11/25 14:52
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BigClassifyActivity extends BaseActivity implements Animator.AnimatorListener {
    private static final String LTAG = BigClassifyActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 隐藏布局 **/
    private LinearLayout mLyClass;

    /*************** 全部分类布局 ***************/
    /** 全部分类布局 **/
    private LinearLayout mLyAllClass;
    /** 全部分类 **/
    private TextView mTvALLClassify;
    /** 是否显示全部分类布局 **/
    private boolean mAllClassListview = false;
    /** 全部分类，左列表 **/
    private ListView mLeftlist;
    /** 全部分类，右列表 **/
    private ListView mRightlist;
    /** 全部分类，左列表Adapter **/
    private ClassifyAllLeftListAdapter leftAdapter = null;
    /** 全部分类，右列表Adapter **/
    private ClassifyAllRightAdapter rightAdapter = null;
    /** 全部分类，数据源 **/
    private List<Map<String, Object>> mAllList;
    /*** 全部分类，左布局图片 **/
    private int[] listviewImg = new int[]{
            R.drawable.classify_all_left_clothes, R.drawable.classify_all_left_living,
            R.drawable.classify_all_left_toy, R.drawable.classify_all_left_teaching,
            R.drawable.classify_all_left_mama};
    /** 全部分类，左布局文案 **/
    private String[] leftListviewText = new String[]{"婴童服饰", "起居用品", "童趣玩具",
            "文体教具", "妈咪专项"};
    /** 全部分类，右部布局文案 **/
    public static String[][] rightListviewText = new String[][]{
            {"我我我我我", "我我我我我", "我我我我我", "我我我我我", "我我我我我"},
            {"你你你你你", "你你你你你", "你你你你你", "你你你你你", "你你你你你"},
            {"他他他他他", "他他他他他", "他他他他他", "他他他他他", "他他他他他"},
            {"它它它它它", "它它它它它", "它它它它它", "它它它它它", "它它它它它"},
            {"她她她她她", "她她她她她", "她她她她她", "她她她她她", "她她她她她"}};

    /*************** 北京分类布局 ***************/
    /** 北京分类布局 **/
    private LinearLayout mLyAllBeiJing;
    /** 北京分类 **/
    private TextView mTvALLBeijing;
    /** 是否显示全部分类布局 **/
    private boolean mAllBeiJingListview = false;
    /** 左右滑动切换屏幕的类 **/
    private ClassifyAllBeiJingScrollLayout mScrollLayout;
    /** 页面控制类 **/
    private ClassifyAllBeiJingPageControlView pageControl;
    /** 页面数 **/
    private static final float APP_PAGE_SIZE = 6.0f;
    /** 分页数据 **/
    private DataLoading dataLoad;
    /** Adapter **/
    private ClassifyAllBeiJingAdapter classifyAllBeiJingAdapter;

    /*************** 主列表 ***************/
    /** listView **/
    private MyListView mListView;
    /** 适配器 **/
    private ClassifyMainListAdapter adapter;
    /** 数据源 **/
    private ArrayList<ClassifyMainListBean> bankList = new ArrayList<ClassifyMainListBean>();

    /** 滑动隐藏监听 **/
    private MyOnTouchListener myOnTouchListener;
    private boolean mIsTitleHide = false;
    private boolean mIsAnim = false;
    private boolean mIsRoll = true;
    private float lastX = 0;
    private float lastY = 0;
    private boolean isDown = false;
    private boolean isUp = false;

    private final int MSG_REFRESH = 1000;
    private final int MSG_LOADMORE = 2000;
    protected android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:

                    break;

                case MSG_LOADMORE:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_classify);
        mContext = this;

        initViews();
    }

    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mLyClass = (LinearLayout) findViewById(R.id.layout);
        mLyAllClass = (LinearLayout) findViewById(R.id.ly_all_class);
        mLyAllBeiJing = (LinearLayout) findViewById(R.id.ly_all_beiing);
        mTvALLClassify = (TextView) findViewById(R.id.tv_all_classify);
        mTvALLBeijing = (TextView) findViewById(R.id.tv_all_beijing);

        initTitle();
        initMainList();
        initAllClassList();
        initAllBeiJingGradView();

        mTvALLClassify.setOnClickListener(this);
        mTvALLBeijing.setOnClickListener(this);
        mLyAllClass.setOnClickListener(this);
        mLyAllBeiJing.setOnClickListener(this);

        initListener();
    }


    /**
     * 初始化主列表
     */
    private void initTitle() {
        mTitleView.setTitle("");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
    }

    /**
     * 初始化主列表
     */
    private void initMainList() {
        mListView = (MyListView) findViewById(R.id.listview);
        setData();

        // 设置listview可以加载、刷新
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        adapter = new ClassifyMainListAdapter(mContext, bankList);
        mListView.setAdapter(adapter);

        // listview单击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                // 模拟刷新数据，1s之后停止刷新
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.stopRefresh();
                        Toast.makeText(BigClassifyActivity.this, "refresh",
                                Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(MSG_REFRESH);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    // 模拟加载数据，1s之后停止加载
                    @Override
                    public void run() {
                        mListView.stopLoadMore();
                        Toast.makeText(BigClassifyActivity.this, "loadMore",
                                Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(MSG_LOADMORE);
                    }
                }, 1000);
            }
        });

    }

    /**
     * 初始化全部布局类别
     */
    private void initAllClassList() {
        mLeftlist = (ListView) findViewById(R.id.lv_left);
        mRightlist = (ListView) findViewById(R.id.lv_right);

        initAllClassData();
        initAllClassLeftListAdapter();
        initAllClassRightListAdapter(rightListviewText[0]);

        Leftlistclick leftListcLick = new Leftlistclick();
        Rightlistclick rightListcLick = new Rightlistclick();

        mLeftlist.setOnItemClickListener(leftListcLick);
        mRightlist.setOnItemClickListener(rightListcLick);
    }

    /**
     * 初始化全部分类
     */
    private void initAllClassData() {
        mAllList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < leftListviewText.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", listviewImg[i]);
            map.put("txt", leftListviewText[i]);
            mAllList.add(map);
        }
    }

    /**
     * 适配全部分类，左列表
     */
    private void initAllClassLeftListAdapter() {
        leftAdapter = new ClassifyAllLeftListAdapter(BigClassifyActivity.this,
                mAllList, R.layout.classify_left_list_item, true);
        leftAdapter.setSelectItem(0);
        mLeftlist.setAdapter(leftAdapter);
    }

    /**
     * 适配全部分类，右列表
     *
     * @param array
     */
    private void initAllClassRightListAdapter(String[] array) {
        rightAdapter = new ClassifyAllRightAdapter(BigClassifyActivity.this,
                array, R.layout.classify_right_list_item);
        mRightlist.setAdapter(rightAdapter);
        rightAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 全部分类－左列表点击事件
     */
    private class Leftlistclick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            initAllClassRightListAdapter(rightListviewText[arg2]);
            leftAdapter.setSelectItem(arg2);
            leftAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 全部分类－右列表点击事件
     */
    private class Rightlistclick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            rightAdapter.setSelectItem(arg2);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLClassify.setCompoundDrawables(null, null, drawable, null);
            int position = leftAdapter.getSelectItem();
            mTvALLClassify.setText(rightListviewText[position][arg2]);
            mLyAllClass.setVisibility(View.GONE);
            mAllClassListview = false;
        }
    }

    /**
     * 初始化全北京布局类别
     */
    private void initAllBeiJingGradView() {
        dataLoad = new DataLoading();
        mScrollLayout = (ClassifyAllBeiJingScrollLayout) findViewById(R.id.ScrollLayout);
//        myHandler = new MyHandler(this,1);
//
//        //起一个线程更新数据
//        MyThread m = new MyThread();
//        new Thread(m).start();

        // 初始化数据－全北京
        List<Map> list = new ArrayList<Map>();
        for (int i = 0; i < 16; i++) {
            Map map = new HashMap();
            map.put("name", "青年湖西里");
            map.put("pop", "15");
            map.put("enthusiasm", "12");
            list.add(map);
        }

        int pageNo = (int) Math.ceil(list.size() / APP_PAGE_SIZE);
        for (int i = 0; i < pageNo; i++) {
            /** gridview **/
            GridView allBeiJingPage = new GridView(mContext);
            // get the "i" page data
            ClassifyAllBeiJingAdapter classifyAllBeiJingAdapter = new ClassifyAllBeiJingAdapter(mContext, list, i);
            allBeiJingPage.setAdapter(classifyAllBeiJingAdapter);
            allBeiJingPage.setNumColumns(2);
            allBeiJingPage.setVerticalSpacing(20);
            allBeiJingPage.setOnItemClickListener(listener);
            allBeiJingPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            mScrollLayout.addView(allBeiJingPage);
        }
        //加载分页
        pageControl = (ClassifyAllBeiJingPageControlView) findViewById(R.id.pageControl);// 取消被选中色
        pageControl.bindScrollViewGroup(mScrollLayout);
        //加载分页数据
        dataLoad.bindScrollViewGroup(mScrollLayout);

    }

    /**
     * 分页数据
     */
    class DataLoading {
        private int count;

        public void bindScrollViewGroup(ClassifyAllBeiJingScrollLayout scrollViewGroup) {
            this.count = scrollViewGroup.getChildCount();
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new ClassifyAllBeiJingScrollLayout.OnScreenChangeListenerDataLoad() {
                public void onScreenChange(int currentIndex) {
                    // TODO Auto-generated method stub
                    generatePageControl(currentIndex);
                }
            });
        }

        private void generatePageControl(int currentIndex) {
            //如果到最后一页，就加载16条记录
//            if(count==currentIndex + 1){
//
//            }
        }
    }

    /**
     * gridView的onItemLick响应事件
     */
    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            LogUtil.d("position = " + position);
        }

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;

        int mID = v.getId();
        // 全部分类
        if (mID == R.id.tv_all_classify) {
            Drawable drawable = null;
            if (!mAllClassListview) {
                drawable = getResources().getDrawable(R.drawable.ic_arrow_up_black);
                mLyAllClass.setVisibility(View.VISIBLE);
                leftAdapter.notifyDataSetChanged();
                mAllClassListview = true;
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
                mLyAllClass.setVisibility(View.GONE);
                mAllClassListview = false;
            }
            //
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLClassify.setCompoundDrawables(null, null, drawable, null);
            mListView.setEnabled(false);
            mIsRoll = false;
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLClassify.setCompoundDrawables(null, null, drawable, null);
            mLyAllClass.setVisibility(View.GONE);
            mAllClassListview = false;
            mListView.setEnabled(true);
            mIsRoll = true;
        }

        // 隐藏全部分类
        if (mID == R.id.ly_all_class) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLClassify.setCompoundDrawables(null, null, drawable, null);
            mLyAllClass.setVisibility(View.GONE);
            mAllClassListview = false;
            mListView.setEnabled(true);
            mIsRoll = true;
        }

        // 全北京
        if (mID == R.id.tv_all_beijing) {
            Drawable drawableBJ = null;
            if (!mAllBeiJingListview) {
                drawableBJ = getResources().getDrawable(R.drawable.ic_arrow_up_black);
                mLyAllBeiJing.setVisibility(View.VISIBLE);
                mAllBeiJingListview = true;
            } else {
                drawableBJ = getResources().getDrawable(R.drawable.ic_arrow_down_black);
                mLyAllBeiJing.setVisibility(View.GONE);
                mAllBeiJingListview = false;
            }

            drawableBJ.setBounds(0, 0, drawableBJ.getMinimumWidth(), drawableBJ.getMinimumHeight());
            mTvALLBeijing.setCompoundDrawables(null, null, drawableBJ, null);
            mListView.setEnabled(false);
            mIsRoll = false;
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLBeijing.setCompoundDrawables(null, null, drawable, null);
            mLyAllBeiJing.setVisibility(View.GONE);
            mAllBeiJingListview = false;
            mListView.setEnabled(true);
            mIsRoll = true;
        }

        // 隐藏全部分类
        if (mID == R.id.ly_all_beiing) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvALLBeijing.setCompoundDrawables(null, null, drawable, null);
            mLyAllBeiJing.setVisibility(View.GONE);
            mAllBeiJingListview = false;
            mListView.setEnabled(true);
            mIsRoll = true;
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

    /**
     * 隐藏布局事件监听
     */
    private void initListener() {
        myOnTouchListener = new MyOnTouchListener() {
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                return dispathTouchEvent(ev);
            }
        };
        registerMyOnTouchListener(myOnTouchListener);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean dispathTouchEvent(MotionEvent event){

        if (!mIsRoll) {
            return false;
        }
        if (mIsAnim) {
            return false;
        }
        final int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                lastX = x;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dY = Math.abs(y - lastY);
                float dX = Math.abs(x - lastX);
                boolean down = y > lastY ? true : false;
                lastY = y;
                lastX = x;
                isUp = dX < 8 && dY > 8 && !mIsTitleHide && !down ;
                isDown = dX < 8 && dY > 8 && mIsTitleHide && down;
                   if (isUp) {
                    View view = this.mLyClass;
                    float[] f = new float[2];
                    f[0] = 0.0F;
                    f[1] = -mTitleView.getHeight();
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", f);
                    animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator1.setDuration(200);
                    animator1.start();
                    animator1.addListener(this);
                    setMarginTop(mLyClass.getHeight() - mTitleView.getHeight());
                } else if (isDown) {
                    View view = this.mLyClass;
                    float[] f = new float[2];
                    f[0] = -mTitleView.getHeight();
                    f[1] = 0F;
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", f);
                    animator1.setDuration(200);
                    animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator1.start();
                    animator1.addListener(this);
                } else {
                    return false;
                }
                mIsTitleHide = !mIsTitleHide;
                mIsAnim = true;
                break;
            default:
                return false;
        }
        return false;

    }

    @Override
    public void onAnimationCancel(Animator arg0) {

    }


    @Override
    public void onAnimationEnd(Animator arg0) {
        if(isDown){
            setMarginTop(mLyClass.getHeight());
        }
        mIsAnim = false;
    }


    @Override
    public void onAnimationRepeat(Animator arg0) {

    }


    @Override
    public void onAnimationStart(Animator arg0) {

    }

    public void setMarginTop(int page){
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        mListView.setLayoutParams(layoutParam);
        mListView.invalidate();

        mLyAllClass.setLayoutParams(layoutParam);
        mLyAllClass.invalidate();

        mLyAllBeiJing.setLayoutParams(layoutParam);
        mLyAllBeiJing.invalidate();
    }

    /**
     * 设置数据--测试
     */
    private void setData() {
        ClassifyMainListBean bModel0 = new ClassifyMainListBean();
        bModel0.type = "01";
        bModel0.price_now = "￥500";
        bModel0.price_old = "￥600";
        bModel0.time = "1小时前";
        bModel0.address = "朝阳区安贞胜古北里";
        bModel0.description = "中广托普广告传媒有限公司";
        bModel0.collectNum = "34";
        bModel0.roll_outNum = "2";
        bModel0.wordsNum = "12";
        bankList.add(bModel0);

        ClassifyMainListBean bModel1 = new ClassifyMainListBean();
        bModel1.type = "02";
        bModel1.price_now = "￥500";
        bModel1.price_old = "￥600";
        bModel1.time = "1小时前";
        bModel1.address = "朝阳区安贞胜古北里";
        bModel1.description = "中广托普广告传媒有限公司";
        bModel1.collectNum = "34";
        bModel1.roll_outNum = "2";
        bModel1.wordsNum = "12";
        bankList.add(bModel1);

        ClassifyMainListBean bModel2 = new ClassifyMainListBean();
        bModel2.type = "03";
        bankList.add(bModel2);

        ClassifyMainListBean bModel3 = new ClassifyMainListBean();
        bModel3.type = "01";
        bModel3.price_now = "￥500";
        bModel3.price_old = "￥600";
        bModel3.time = "1小时前";
        bModel3.address = "朝阳区安贞胜古北里";
        bModel3.description = "中广托普广告传媒有限公司";
        bModel3.collectNum = "34";
        bModel3.roll_outNum = "2";
        bModel3.wordsNum = "12";
        bankList.add(bModel3);

        ClassifyMainListBean bModel4 = new ClassifyMainListBean();
        bModel4.type = "02";
        bModel4.price_now = "￥500";
        bModel4.price_old = "￥600";
        bModel4.time = "1小时前";
        bModel4.address = "朝阳区安贞胜古北里";
        bModel4.description = "中广托普广告传媒有限公司";
        bModel4.collectNum = "34";
        bModel4.roll_outNum = "2";
        bModel4.wordsNum = "12";
        bankList.add(bModel4);

        ClassifyMainListBean bModel5 = new ClassifyMainListBean();
        bModel5.type = "03";
        bankList.add(bModel5);
    }

}
