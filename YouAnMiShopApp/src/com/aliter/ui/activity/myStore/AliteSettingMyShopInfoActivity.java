package com.aliter.ui.activity.myStore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliter.base.BaseActivity;
import com.aliter.entity.ShopInfoBase;
import com.aliter.entity.ShopUpdate;
import com.aliter.injector.component.SettingShopNameHttpModule;
import com.aliter.injector.component.activity.DaggerSettingShopNameComponent;
import com.aliter.presenter.SettingShopNamePresenter;
import com.aliter.presenter.impl.SettingShopNamePresenterImpl;
import com.aliter.ui.activity.login.AliteCheckProvinceActivity;
import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.cropView.Crop;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by sayid on 2017/6/23.
 */

public class AliteSettingMyShopInfoActivity extends BaseActivity<SettingShopNamePresenterImpl> implements SettingShopNamePresenter.View {
    @BindView(R.id.rl_user_head)
    RelativeLayout rlUserHead;
    @BindView(R.id.rl_shop_name)
    RelativeLayout rlShopName;
    @BindView(R.id.rl_shop_slogan)
    RelativeLayout rlShopSlogan;
    @BindView(R.id.rl_consumer_hotline)
    RelativeLayout rlConsumerHotline;
    @BindView(R.id.rl_region)
    RelativeLayout rlRegion;
    @BindView(R.id.rl_details)
    RelativeLayout rlDetails;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_top)
    TextView tvShopTop;
    @BindView(R.id.tv_consumer_hotline)
    TextView tvConsumerHotline;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_details)
    TextView tvDetails;


    private FileUploadRequest fileUploadRequest;
    private String provinceId, cityName, districtId, districtName, cityId, provinceName, GeneralizeCode;
    private Intent i;
    private ShopInfoBase shopInfoBase;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constants.GET_PIC_FROM_CELLPHONE:
                    Crop.pickImage(AliteSettingMyShopInfoActivity.this);
                    break;
                case Constants.GET_PIC_FROM_CAMERA:
                    Crop.cameraImage(AliteSettingMyShopInfoActivity.this);
                    break;
                case Constants.MSG_SUCCEED:
                    ViewUtils.showToast("上传成功!");
                    String data = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(data);
                        String url = object.getString("thumHeadUrl");
                        String originUrl = object.getString("originHeadUrl");
                        IMUserInfoVO user = Account.readLoginUser(context);
                        user.setThumHeadUrl(url);
                        user.setOriginHeadUrl(originUrl);
                        Account.user = user;
                        Account.saveLoginUser(context, user);
//                        isSetUserHead=true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    setUserHead();
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.alite_aitivity_setting_shopinfo;
    }

    @Override
    public void setToolBar() {
    }

    @Override
    public void initView() {


    }


    @Override
    protected void onResume() {
        super.onResume();
        shopInfoBase = PreferUtil.getInstance().getShopInfo();
        if (StringUtil.isNull(shopInfoBase.getName()))
            tvShopName.setText("未设置");
        else
            tvShopName.setText(shopInfoBase.getName());

        if (StringUtil.isNull(shopInfoBase.getSlogan()))
            tvShopTop.setText("未设置");
        else
            tvShopTop.setText(shopInfoBase.getSlogan());

        if (StringUtil.isNull(shopInfoBase.getServerPhone()))
            tvConsumerHotline.setText("未设置");
        else
            tvConsumerHotline.setText(shopInfoBase.getServerPhone());
        if (StringUtil.isNull(shopInfoBase.getDetailedAddress()))
            tvDetails.setText("未设置");
        else
            tvDetails.setText(shopInfoBase.getDetailedAddress());

        cityName=shopInfoBase.getCityName();
        districtName=shopInfoBase.getAreaName();
        provinceName=shopInfoBase.getProvinceName();

        if(!StringUtil.isNull(provinceName)){
            tvRegion.setText(provinceName+" "+ cityName+" "+districtName);
        }else {
            tvRegion.setText("未设置");
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initInject() {
        DaggerSettingShopNameComponent.builder().settingShopNameHttpModule(new SettingShopNameHttpModule()).build().injectWeChat(this);
    }


    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(PicTools.getOutputPhotoFile());
        new Crop(source).output(outputUri).asSquare().withMaxSize(800, 800).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            File tmpFile = PicTools.getOutputPhotoFile();
            postFile(tmpFile);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void postFile(File file) {

//    file	图片	string	图片数据流
//    isThum	是否要缩略图	number	Integer类型，1：不要，2：要，默认1
//    thumHeight	缩略图高度	number	Integer类型，范围：0<thumHeight<500
//    thumWidth	缩略图宽度	number	Integer类型，范围：0<thumHeight<500



        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        mPresenter.CommonImageUpload(filePart);


//        Map<String, Object> params = new HashMap<String, Object>();
//        fileUploadRequest = new FileUploadRequest(file, params,
//                "shop/photo/edit", handler);
//        fileUploadRequest.startUpload();


    }


    @OnClick({R.id.rl_user_head, R.id.rl_shop_name, R.id.rl_shop_slogan, R.id.rl_consumer_hotline, R.id.rl_region,
            R.id.rl_details, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_head:    //   商户头像
                new GetPictureDialog(false).show(handler);


                break;
            case R.id.btn_back:    //   返回
                finish();
                finishActivity();


                break;
            case R.id.rl_shop_name:    ///   门店名称
                i = new Intent(AliteSettingMyShopInfoActivity.this, AliteSettingShopNameActivity.class);
                i.putExtra("TYPE", 1);
                i.putExtra("Name",shopInfoBase.getName());
                ViewUtils.startActivity(i, AliteSettingMyShopInfoActivity.this);
                break;
            case R.id.rl_shop_slogan:   ///  门店标语
                i = new Intent(AliteSettingMyShopInfoActivity.this, AliteSettingShopNameActivity.class);
                i.putExtra("TYPE", 2);
                i.putExtra("Name",shopInfoBase.getSlogan());
                ViewUtils.startActivity(i, AliteSettingMyShopInfoActivity.this);

                break;
            case R.id.rl_consumer_hotline:   //  客服电话
                i = new Intent(AliteSettingMyShopInfoActivity.this, AliteSettingShopNameActivity.class);
                i.putExtra("TYPE", 3);
                i.putExtra("Name",shopInfoBase.getServerPhone());
                ViewUtils.startActivity(i, AliteSettingMyShopInfoActivity.this);
                break;
            case R.id.rl_region:    //   地区
                Intent intent = new Intent(AliteSettingMyShopInfoActivity.this, AliteCheckProvinceActivity.class);
                AliteSettingMyShopInfoActivity.this.startActivityForResult(intent, 1);
                startActivityIn();
                break;


            case R.id.rl_details:    //   详情地址
                i = new Intent(AliteSettingMyShopInfoActivity.this, AliteSettingShopNameActivity.class);
                i.putExtra("TYPE", 4);
                i.putExtra("Name",shopInfoBase.getDetailedAddress());
                ViewUtils.startActivity(i, AliteSettingMyShopInfoActivity.this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CAMERA && resultCode == RESULT_OK) {
            beginCrop(Uri.fromFile(PicTools.getOutputPhotoFile()));
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        if (requestCode == 1) {
            if (data != null) {
                Bundle mBundle = data.getExtras();
                switch (resultCode) {
                    case 0:
                        districtName = mBundle.getString("districtName");
                        districtId = mBundle.getString("districtId");
                        provinceName = mBundle.getString("provinceName");
                        provinceId = mBundle.getString("provinceId");
                        cityName = mBundle.getString("cityName");
                        cityId = mBundle.getString("cityId");
                        tvRegion.setText(provinceName + " " + districtName);

                        break;
                    case 1000:
                        districtName = mBundle.getString("districtName");
                        districtId = mBundle.getString("districtId");
                        provinceName = mBundle.getString("provinceName");
                        provinceId = mBundle.getString("provinceId");
                        cityName = mBundle.getString("cityName");
                        cityId = mBundle.getString("cityId");
                        tvRegion.setText(provinceName + " " + cityName + " " + districtName);
                        break;
                    case 1001:
                        provinceName = mBundle.getString("provinceName");
                        provinceId = mBundle.getString("provinceId");
                        cityName = mBundle.getString("cityName");
                        cityId = mBundle.getString("cityId");
                        districtName = mBundle.getString("districtName");
                        districtId = mBundle.getString("districtId");
                        tvRegion.setText(provinceName + " " + cityName);
                        break;
                }
                PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_CITYNAME,cityName);
                PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_AREANAME,districtName);
                PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_PROVINCENAME,provinceName);
                ShopUpdate shopUpdate = new ShopUpdate();
                shopUpdate.setType(4);
                shopUpdate.setShopId(Account.user.getShopId());
                shopUpdate.setProvinceId(Integer.valueOf(provinceId));
                shopUpdate.setCityId(Integer.valueOf(cityId));
                shopUpdate.setAreaId(Integer.valueOf(districtId));
                mPresenter.ShopUpdate(shopUpdate);
                ShowLoadingDialog();
            }
        }
    }


    @Override
    public void onCommonImageUploadSuccessView() {
        //  上传图片到服务器 返回


    }

    @Override
    public void onShopUpdateSuccessView() {
        //  更新地址返回
        PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_CITYNAME,cityName);
        PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_AREANAME,districtName);
        PreferUtil.getInstance().putString(PreferUtil.SHOP_INFO_PROVINCENAME,provinceName);

        tvRegion.setText(provinceName+" "+ cityName+" "+districtName);
        DismissLoadingDialog();
        ViewUtils.showToast("更新地址成功");


    }

    @Override
    public void onFailView(String errorMsg) {
        DismissLoadingDialog();
    }

}
