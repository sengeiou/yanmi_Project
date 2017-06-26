package com.aliter.ui.activity.login;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aliter.base.BaseActivity;
import com.aliter.entity.MobileExist;
import com.aliter.entity.MobileExistBean;
import com.aliter.injector.component.AlitePhoneRegisterHttpModule;
import com.aliter.injector.component.activity.DaggerAlitePhoneRegisterComponent;
import com.aliter.presenter.AlitePhoneRegisterPresenter;
import com.aliter.presenter.impl.AlitePhoneRegisterPresenterImpl;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sayid on 2017/6/6.
 */

public class AlitePhoneRegisterActivity extends BaseActivity<AlitePhoneRegisterPresenterImpl> implements AlitePhoneRegisterPresenter.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_clean_name)
    ImageView btnCleanPhone;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.edit_phone)
    EditText editPhone;


    @Override
    public int getLayoutId() {
        return R.layout.alite_aitivity_phone_register;
    }

    @Override
    public void setToolBar() {

        setToolBar(toolbar, "");
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.al_login_return));
    }

    @Override
    public void initView() {

        initListener();
        btnRegister.setEnabled(false);
        btnRegister.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected void initInject() {
        DaggerAlitePhoneRegisterComponent.builder().alitePhoneRegisterHttpModule(new AlitePhoneRegisterHttpModule()).build().injectData(this);
    }

    @OnClick({R.id.btn_clean_name, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clean_name:
                editPhone.setText("");
                break;
            case R.id.btn_register:
                //  1. 先查询手机是否注册过
                MobileExist mobileExist = new MobileExist();
                mobileExist.setMobile(editPhone.getText().toString());
                mPresenter.ShopAppisMobileExist(mobileExist);
                ShowLoadingDialog();
                break;
        }
    }


    private void initListener() {
        editPhone.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanPhone.setVisibility(View.GONE);
                    btnRegister.setBackgroundResource(R.drawable.alite_btn_login_default);
                    btnRegister.setEnabled(false);
                } else {
                    if (!s.toString().startsWith("1")) {
                        editPhone.setText("");
                        ViewUtils.showToast("请输入正确的电话号码！");
                        btnCleanPhone.setVisibility(View.GONE);
                        btnRegister.setBackgroundResource(R.drawable.alite_btn_login_default);
                        btnRegister.setEnabled(false);
                        btnRegister.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        btnCleanPhone.setVisibility(View.VISIBLE);
                        btnRegister.setBackgroundResource(R.drawable.alite_btn_login_phone);
                        btnRegister.setEnabled(true);
                    }
                }
                if (temp.length() > 11) {
                    s.delete(11, s.length());
                    editPhone.setText(s);
                    editPhone.setSelection(s.length());
                }
            }
        });
    }

    @Override
    public void onShopAppisMobileExistSuccessView(MobileExistBean mobileExistBean) {

        //    1.先查询手机号是否注册过
       DismissLoadingDialog();
        if (mobileExistBean.isExist()) {
            ViewUtils.showToast("该手机号已经注册过");
        } else {
            PreferUtil.getInstance().setRegisterPhonenum(editPhone.getText().toString());
            ViewUtils.startActivity(new Intent(AlitePhoneRegisterActivity.this, AliteSMSVerificationActivity.class), this);
        }
    }

    @Override
    public void onFailView(String errorMsg) {
    }
}
