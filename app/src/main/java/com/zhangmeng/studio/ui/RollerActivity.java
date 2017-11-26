package com.zhangmeng.studio.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.beans.Preferences;
import com.zhangmeng.studio.view.RollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/25.
 */

public class RollerActivity extends AppCompatActivity {
    @BindView(R.id.roller)
    RollerTextView rollerTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller);
        ButterKnife.bind(this);
        if(!TextUtils.isEmpty(Preferences.getRollerText()))
        {
            rollerTextView.setRollerText(Preferences.getRollerText());
        }
    }
    @OnClick(R.id.roller)
    public void onClick(View v) {
        rollerTextView.setPause(true);
        //弹出AlertDialog设置文字
        new MaterialDialog.Builder(this)
                .title(R.string.btn_roller)
                .content(R.string.dialog_roller_content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        rollerTextView.setRollerText(input.toString());
                        Preferences.saveRollerText(input.toString());
                    }
                }).cancelListener(new MaterialDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(rollerTextView.isPause())
                {
                    rollerTextView.setPause(false);
                    rollerTextView.postInvalidate();
                }
            }
        }).show();
    }
}
