package com.aliter.ui.activity.login;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aliter.base.BaseActivity;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sayid on 2017/6/6.
 */

public class AliteSMSVerificationActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_clean_name)
    ImageView btnCleanName;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @Override
    public void setState(int state) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.alite_aitivity_sms_verification;
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initInject() {

    }

    @OnClick({R.id.btn_clean_name, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clean_name:
                break;
            case R.id.btn_ok:

                ViewUtils.startActivity(new Intent(AliteSMSVerificationActivity.this,AliteShopRegisterActivity.class), this);
                break;
        }
    }
}