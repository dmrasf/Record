package com.dmrasf.record.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.dmrasf.record.R;

public class TextDialog extends Dialog {
    public TextDialog(@NonNull Context context) {
        super(context);
    }

    public static class Builder {
        private final Context context;
        private TextView textViewCancel;
        private TextView textViewConfirm;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTextCancel(View.OnClickListener clickListener){
            textViewCancel.setOnClickListener(clickListener);
            return this;
        }

        public Builder setTextConfirm(View.OnClickListener clickListener){
            textViewConfirm.setOnClickListener(clickListener);
            return this;
        }

        public TextDialog create() {
            final TextDialog dialog = new TextDialog(context);

            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_text);

            textViewCancel = (TextView) ((Activity) context).findViewById(R.id.dialog_cancel);
            textViewConfirm = (TextView) ((Activity) context).findViewById(R.id.dialog_confirm);

            return dialog;
        }
    }
}
