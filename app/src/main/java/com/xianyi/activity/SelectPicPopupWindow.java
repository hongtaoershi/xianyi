package com.xianyi.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.xianyi.R;
import com.xianyi.utils.LogUtil;
import com.xianyi.utils.PickPicture;
import com.xianyi.utils.PictureUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectPicPopupWindow extends Activity implements OnClickListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    private Intent intent;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        mContext = this;
        intent = getIntent();
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        layout = (LinearLayout) findViewById(R.id.pop_layout);

        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        LogUtil.d("##data:" + data);
        if (data == null) {
            PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
            intent.putExtra("path", mCurrentPhotoPath);
        } else {
            Uri mImageCaptureUri = data.getData();
            String pathString = PickPicture.getImageAbsolutePath(mContext, mImageCaptureUri);
            intent.putExtra("path", pathString);
            //选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
//			if (data.getExtras() != null)
//				intent.putExtras(data.getExtras());
//			if (data.getData() != null)
//				intent.setData(data.getData());
        }
        setResult(1, intent);
        finish();

    }

    String mCurrentPhotoPath;


    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // 指定存放拍摄照片的位置
            File f = createImageFile();
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(takePictureIntent, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
     * 照片的命名规则为：sheqing_20130125_173729.jpg
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "topad" + timeStamp + ".jpg";

        File image = new File(PictureUtil.getAlbumDir(), imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                takePhoto();
//				try {
//					//拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
//					//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//					startActivityForResult(intent, 1);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
                break;
            case R.id.btn_pick_photo:
                try {
                    //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                    //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 2);
                } catch (ActivityNotFoundException e) {

                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }

    }

}