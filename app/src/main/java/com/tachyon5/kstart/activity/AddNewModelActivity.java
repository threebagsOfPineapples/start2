package com.tachyon5.kstart.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.utils.ImageThumbnail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

public class AddNewModelActivity extends MPermissionsActivity implements View.OnClickListener {
    private EditText edt_model_type, edt_model_name, edt_model_desc;
    private TextView tv_create_model;
    private ImageView iv_model_pic;
    private static String photoflage = ""; // 标记是哪一个照片
    private Bitmap bitMap; // 照片 bitmap
    private String photopath; // 图片保存地址 绝对路径
    private String photo1path, photo2path;
    private boolean ispicture1, isIspicture2;//是否拍照
    private String photoId1, photoId2;
    private Uri uritempFile;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_model);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tv_title = (TextView) findViewById(R.id.activity_add_new_model_tv_title);
        tv_title.setText(R.string.add_model);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initData();
    }

    public void back(View view) {
        onBackPressed();
    }

    private void initView() {
        edt_model_type = (EditText) findViewById(R.id.activity_add_new_model_et_model_types);
        edt_model_name = (EditText) findViewById(R.id.activity_add_new_model_et_model_name);
        edt_model_desc = (EditText) findViewById(R.id.activity_add_new_model_et_model_description);
        tv_create_model = (TextView) findViewById(R.id.activity_add_new_model_tv_create_model);
        iv_model_pic = (ImageView) findViewById(R.id.activity_add_new_model_iv_model_pic);
    }

    private void initData() {
        edt_model_type.setOnClickListener(this);
        edt_model_name.setOnClickListener(this);
        edt_model_desc.setOnClickListener(this);
        tv_create_model.setOnClickListener(this);
        iv_model_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_new_model_et_model_types:
                break;
            case R.id.activity_add_new_model_et_model_name:
                break;
            case R.id.activity_add_new_model_et_model_description:
                break;
            case R.id.activity_add_new_model_tv_create_model:
                Intent intent = new Intent(AddNewModelActivity.this, ModelItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("modelName", edt_model_name.getText().toString());
                bundle.putString("modelType", edt_model_type.getText().toString());
                bundle.putString("modelDesc", edt_model_desc.getText().toString());
                bundle.putString("modelState", "新建");
                bundle.putString("modelPicPath", String.valueOf(uritempFile));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.activity_add_new_model_iv_model_pic:
                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);
             /*   photoflage = "photo1";
                Intent cameraIntent = null;
                Uri imageUri = null;
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
                // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 2);
                photoflage = "photo1";
                Intent iintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                iintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(iintent, CAMERA_REQUEST_CODE);*/
                break;
            default:
                break;
        }
    }

    /**
     * 对byte[]进行压缩
     *
     * @param
     * @return 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        GZIPOutputStream gzip = null;
        ByteArrayOutputStream baos = null;
        byte[] newData = null;
        try {
            baos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(baos);
            gzip.write(data);
            gzip.close();
            // gzip.flush();
            newData = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newData;
    }

    // 下载图片 转为byte
    private byte[] loadFile(String filepath) {
        File file = new File(filepath);
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream((int) file.length());
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 开始裁剪
     *
     * @param uri
     */
    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片 剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 300);
        intent.putExtra("aspectY", 300);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    // 保存图片
    private void savePhoto(Bitmap bitmap, String phot) {
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        String imagename = "" + month + day + minute + hour + minute + sec;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Logger.v("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        FileOutputStream b = null;
        // String filename=getResources().getString(R.string.filename);
        String filename = "/sdcard/photograph/" + phot + "/";
        File file = new File(filename);
        file.mkdirs();// 创建文件夹
        photopath = filename + imagename + ".jpg";
        try {
            b = new FileOutputStream(photopath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Toast.makeText(HomePageActivity.this, "照片保存成功", 1).show();
        }
    }

    // 拍照回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("9999999999999999999" + requestCode);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                // Log.i(TAG, "相机, 开始裁剪");
                if (resultCode == RESULT_OK) {
                    File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                    startPhotoZoom(Uri.fromFile(picture), 640);
                }
                break;
            case CROP_REQUEST_CODE:
                // Log.i(TAG, "相册裁剪成功");
                Logger.i("裁剪以后 [ " + data + " ]");
            /*    if (data == null) {
                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    bitMap = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                    // 此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    BitmapDrawable bd = new BitmapDrawable(null,bitMap); // bitmap 转换成
                    // 将处理过的图片显示在界面上
                    if (photoflage.equals("photo1")) {
                        iv_model_pic.setImageBitmap(bitMap);
                        ispicture1 = true;
                        savePhoto(bitMap, "photo1");
                        photo1path = photopath;
                        // Toast.makeText(getApplicationContext(), strPhono_1,
                        // 1).show();
                    }
                }*/
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    iv_model_pic.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 判断图片是否旋转
     *
     * @param path 图片路径
     * @return 旋转角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        Log.i("UserInfoActivity", "onConfigurationChanged");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("UserInfoActivity", "横屏");
            Configuration o = newConfig;
            o.orientation = Configuration.ORIENTATION_PORTRAIT;
            newConfig.setTo(o);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("UserInfoActivity", "竖屏");
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 权限申请成功后进行操作
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x001: {
                photoflage = "photo1";
                Intent iintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                iintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(iintent, CAMERA_REQUEST_CODE);
            }
            break;
        }
    }

    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
    }

    /**
     * 裁剪图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 480);
        intent.putExtra("aspectY", 340);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 340);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        //uritempFile=  Uri.parse(Environment.getExternalStorageDirectory() + "/temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 4);
    }
}