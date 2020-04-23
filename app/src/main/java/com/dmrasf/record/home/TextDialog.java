package com.dmrasf.record.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.dmrasf.record.R;

public class TextDialog extends Dialog {
    private Context mContext;

    public TextDialog(@NonNull Context context) {
        super(context);
    }

    public static class Builder {
        private final TextDialog mDialog;
        private TextView mTextViewCancel;
        private TextView mTextViewConfirm;
        private EditText mEditText;
        private View.OnClickListener mCancelListener;
        private View.OnClickListener mConfirmListener;
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
            mDialog = new TextDialog(context);
        }

        public Builder setLayout(View view) {
            //添加布局文件到 Dialog
            mDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            Window dialogWindow = mDialog.getWindow();
            // 设置dialog的大小
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 获取屏幕实际大小
            Point outSize = new Point();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getRealSize(outSize);
            int x = outSize.x;
            int y = outSize.y;
            // 根据屏幕大小设置dialog
            lp.width = (int) (x * 0.8);
            lp.height = (int) (y * 0.5);
            lp.alpha = 0.7f;
            dialogWindow.setAttributes(lp);
            // 设置动画
            dialogWindow.setWindowAnimations(R.style.textDialog);

            mEditText = (EditText) view.findViewById(R.id.dialog_edit);
            mTextViewCancel = (TextView) view.findViewById(R.id.dialog_cancel);
            mTextViewConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
            return this;
        }

        public Builder setConfirmButton(View.OnClickListener onClickListener) {
            mConfirmListener = onClickListener;
            return this;
        }

        public TextDialog create() {
            // 按下取消是可以关闭dialog
            mTextViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mTextViewConfirm.setOnClickListener(mConfirmListener);

            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);

            // 弹出键盘
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return mDialog;
        }
    }
}
