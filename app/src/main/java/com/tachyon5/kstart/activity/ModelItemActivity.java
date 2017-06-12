package com.tachyon5.kstart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.chart.LineChartActivity;
import com.tachyon5.kstart.utils.ToastUtil;
import com.tachyon5.kstart.view.CustomDialog;
import com.tachyon5.kstart.view.CustomDialogNew;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.Call;
import okhttp3.Response;

public class ModelItemActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_model_pic;
    private TextView tv_model_desc, tv_model_state, tv_model_collect, tv_model_build, tv_model_name,
            tv_model_type;
    private CustomDialogNew customDialogNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initData();
        //获取传递的信息
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        tv_model_name.setText(mbundle.getString("modelName"));
        tv_model_type.setText(mbundle.getString("modelType"));
        tv_model_desc.setText(mbundle.getString("modelDesc"));
        tv_model_state.setText(mbundle.getString("modelState"));
        String path = mbundle.getString("modelPicPath");
        File mfile = null;
        if (path != null) {
          /*  mfile=new File(path);
            Logger.d(path);
            Bitmap bitmap = null;
            if(mfile.exists()){
                Logger.d("文件存在");
                bitmap = BitmapFactory.decodeFile(path);
            }else{
                Logger.e("文件不存在");
            }
            if(bitmap!=null){
                // Drawable drawable=new BitmapDrawable(null,bitmap);
                //iv_model_pic.setBackground(drawable);
                iv_model_pic.setImageBitmap(bitmap);
            }*/
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(path)));
                iv_model_pic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        iv_model_pic = (ImageView) findViewById(R.id.activity_model_item_iv_model_pic);
        ViewGroup.LayoutParams ps = iv_model_pic.getLayoutParams();
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point piont = new Point();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ps.width = (int) (dm.widthPixels) * 3 / 4;
        Logger.d("宽度" + dm.heightPixels + "长度：" + dm.widthPixels);
        ps.height = ps.width * 340 / 420;
        iv_model_pic.setLayoutParams(ps);
        tv_model_name = (TextView) findViewById(R.id.activity_model_item_tv_title);
        tv_model_desc = (TextView) findViewById(R.id.activity_model_item_tv_model_desc);
        tv_model_state = (TextView) findViewById(R.id.activity_model_item_tv_model_state);
        tv_model_type = (TextView) findViewById(R.id.activity_model_item_tv_model_type);
        tv_model_collect = (TextView) findViewById(R.id.activity_model_item_tv_model_collect);
        tv_model_build = (TextView) findViewById(R.id.activity_model_item_tv_model_build);
    }

    private void initData() {
        tv_model_build.setOnClickListener(this);
        tv_model_collect.setOnClickListener(this);
        tv_model_type.setOnClickListener(this);
        tv_model_state.setOnClickListener(this);
        tv_model_desc.setOnClickListener(this);
        tv_model_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_model_item_tv_title:
                break;
            case R.id.activity_model_item_tv_model_desc:
                break;
            case R.id.activity_model_item_tv_model_state:
                break;
            case R.id.activity_model_item_tv_model_type:
                break;
            case R.id.activity_model_item_tv_model_collect:
                //采集数据
                /*
                Intent intent=new Intent(ModelItemActivity.this,CollectActivity.class);
                startActivity(intent);*/
               /* new CustomDialog.Builder(this)
                                 .setTitle("测试")
                                  .setMessage("这是副标题")
                                   .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {

                                       }
                                   })
                                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                           .create().show();*/
            /*   CustomDialog.Builder builder = new CustomDialog.Builder(this);

               builder.setMessage("设备未连接");
               builder.setTitle("是否现在连接设备");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();*/
                CustomDialogNew.Builder builder = new CustomDialogNew.Builder(this);
                customDialogNew =
                        builder.cancelTouchout(false)
                                .view(R.layout.layout_custom_dialog)
                                .style(R.style.MyDialog)
                                .addViewOnclick(R.id.tv_navigation, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ToastUtil.showToast(getApplicationContext(), "这是消极");
                                        customDialogNew.dismiss();
                                    }
                                })
                                .addViewOnclick(R.id.tv_positive, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), ScanBtActivity.class);
                                        startActivity(intent);
                                        customDialogNew.dismiss();
                                    }
                                })
                                .build();
                customDialogNew.show();
                Window dialogWindow = customDialogNew.getWindow();
                WindowManager.LayoutParams p = dialogWindow.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                p.width = (int) (dm.widthPixels) * 3 / 4;
                Logger.d("宽度" + dm.heightPixels + "长度：" + dm.widthPixels);
                p.height = p.width * 125 / 225;
                dialogWindow.setAttributes(p);
                break;
            case R.id.activity_model_item_tv_model_build:
              /*  OkGo.get("http://123.56.229.50/download/application/firmware/Firmware_S1A2015FW00_V2.7.bin")
                    .execute(new FileCallback() {
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                        }
                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            Logger.d("当前大小"+currentSize+"总大小："+totalSize+"进度："+progress+"网速："
                            +networkSpeed
                            );
                            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        }
                    });*/
                Intent intent = new Intent(ModelItemActivity.this, LineChartActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
