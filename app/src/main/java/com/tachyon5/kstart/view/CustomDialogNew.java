package com.tachyon5.kstart.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tachyon5.kstart.utils.Utils;

/**
 * Created by guofe on 2017/2/26 0026.
 */

public class CustomDialogNew extends Dialog {
    private Context context;
    private int height, width;
    private boolean cancelTouchout;
    private View view;

    private CustomDialogNew(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }


    private CustomDialogNew(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);

        setCanceledOnTouchOutside(cancelTouchout);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = height;
        lp.width = width;
        win.setAttributes(lp);
    }

    public static final class Builder {

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder heightpx(int val) {
            height = val;
            return this;
        }

        public Builder widthpx(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = (int) Utils.dp2px(context, val);
            return this;
        }

        public Builder widthdp(int val) {
            width = (int) Utils.dp2px(context, val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }


        public CustomDialogNew build() {
            if (resStyle != -1) {
                return new CustomDialogNew(this, resStyle);
            } else {
                return new CustomDialogNew(this);
            }
        }
    }
}
