package com.aliter.ui.activity.login;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliter.base.BaseActivity;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

/**
 * Created by sayid on 2017/6/5.
 */

public class AliteLaunchActivity extends BaseActivity {


    @BindView(R.id.layout_phone_login)
    RelativeLayout layoutPhoneLogin;
    @BindView(R.id.layout_wchat_login)
    RelativeLayout layoutWchatLogin;
    @BindView(R.id.registered_shop_account)
    TextView registeredShopAccount;
    private LoadingDialog loadingDialog;

    @Override
    public void setState(int state) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.alite_aitivity_launch;
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void initView() {

        getSwipeBackLayout().setEnableGesture(false);
        if (Build.VERSION.SDK_INT >= 19) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initInject() {

    }

    @OnClick({R.id.layout_phone_login, R.id.layout_wchat_login, R.id.registered_shop_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_phone_login:
                ViewUtils.startActivity(new Intent(AliteLaunchActivity.this, AliteLoginActivity.class), this);
                break;
            case R.id.layout_wchat_login:

                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShortToast("网络连接已断开");
                    break;
                }

                if(StringUtil.isWeixinAvilible(this))
                    UMShareAPI.get(AliteLaunchActivity.this).getPlatformInfo(AliteLaunchActivity.this, WEIXIN, authListener);
                else
                    ToastUtils.showShortToast("请先安装微信应用");

                break;
            case R.id.registered_shop_account:
                ViewUtils.startActivity(new Intent(AliteLaunchActivity.this, AlitePhoneRegisterActivity.class), this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            loadingDialog = new LoadingDialog(AliteLaunchActivity.this);
            loadingDialog.show();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if(data!=null)
            PreferUtil.getInstance().setWeixinUserInfo(data);
            if (loadingDialog.isShow())
                loadingDialog.dismiss();
//            UMShareAPI.get(AliteLaunchActivity.this).deleteOauth(AliteLaunchActivity.this, WEIXIN, null);
            ViewUtils.startActivity(new Intent(AliteLaunchActivity.this, AliteWeixinUserPhoneActivity.class), AliteLaunchActivity.this);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if (loadingDialog.isShow())
                loadingDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            if (loadingDialog.isShow())
                loadingDialog.dismiss();
        }
    };
}
