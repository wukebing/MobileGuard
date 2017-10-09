package cn.edu.gdmec.android.mobileguard1.m2theftguard.dialog.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

import cn.edu.gdmec.android.mobileguard1.R;


/**
 * Created by lenovo on 2017/9/28.
 */

public class SetUpPasswordDialog extends Dialog implements View.OnClickListener {
    private TextView mTextView;
    public EditText mFirstPWDET;
    public EditText mAffirmET;
    private MyCallBack myCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.setup_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    public SetUpPasswordDialog(@NonNull Context context) {
        super(context, R.style.dialog_custom);
    }
    private void initView() {
        mTextView=(TextView) findViewById(R.id.tv_setuppwd_title);
        mFirstPWDET=(EditText) findViewById(R.id.et_firstpwd);
        mAffirmET=(EditText) findViewById(R.id.et_firstpwd);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTextView.setText(title);
        }
    }
        public void setCallBack(MyCallBack myCallBack){
            this.myCallBack = myCallBack;
        }
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.btn_ok:
                    System.out.print("SetupPasswordDialog");
                    try {
                        myCallBack.ok();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_cancel:
                    myCallBack.cancel();
                    break;
            }
        }

        public interface  MyCallBack{
            void ok() throws NoSuchAlgorithmException;
            void cancel();
        }
}