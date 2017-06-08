package com.aliter.ui.fragment.homefragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aliter.base.BaseFragment;
import com.aliter.entity.StoreArticle;
import com.aliter.entity.StoreArticleBean;
import com.aliter.http.BaseResponse;
import com.aliter.injector.component.StoreAriclesHttpModule;
import com.aliter.injector.component.fragment.DaggerStoreArticlesComponent;
import com.aliter.injector.component.module.fragment.StoreArticlesModule;
import com.aliter.presenter.StorArticlesPresenter;
import com.aliter.presenter.impl.StorArticlesPresenterImpl;

import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.R.attr.type;

public class AliteStoreArticlesFragment extends BaseFragment<StorArticlesPresenterImpl> implements StorArticlesPresenter.View {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    protected BaseQuickAdapter mAdapter;
    private List<StoreArticleBean> data;

    private ShareDialog dialog;


    @Override
    public void onSuccessView(BaseResponse<List<StoreArticleBean>> mData) {
        data = mData.getData();
        for (int i=0;i<data.size();i++){
            if(StringUtils.isEmpty(data.get(i).getHeadUrl())){
               data.get(i).setType(StoreArticleBean.NO_ICON);
            }else
                data.get(i).setType(StoreArticleBean.ICON);
        }
        mAdapter.setNewData(data);

    }

    @Override
    public void onFailView(String errorMsg) {

    }

    @Override
    protected void loadData() {

        StoreArticle storeArticle = new StoreArticle();
        storeArticle.setPageIndex(1);
        storeArticle.setType(1);
        storeArticle.setShopId(Account.user.getShopId());
        storeArticle.setUserId(Account.user.getId());
        mPresenter.fetchData(storeArticle);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.alite_fragment_strore_articles;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ShareInfo shareInfo = new ShareInfo();
                shareInfo.setDesc(data.get(position).getDescription());
                shareInfo.setTitle(data.get(position).getTitle());
                shareInfo.setUrl(data.get(position).getShareUrl().replace("isShare=0", "isShare=1"));

                H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
                        getActivity(), data.get(position).getShareUrl() + "&from=app", "文章详情", shareInfo, false);

            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener( ) {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (dialog == null)
                    dialog = new ShareDialog();

                final StoreArticleBean article= data.get(position);
                String shareTitle = article.getTitle();
                String shareImageUrl = article.getHeadUrl();
                String shareDesc = article.getDescription();
                String shareUrl = article.getShareUrl();

                int index=StringUtil.isChinese(shareUrl);
                if(StringUtil.isChinese(shareUrl)!=000)
                  shareUrl= shareUrl.substring(0,index-1);
                dialog.show(shareTitle, shareDesc, shareUrl.replace("isShare=0", "isShare=1"), shareImageUrl, new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        switch (type) {
                            case 1:
                                new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 1, article.getTitle()).start();
                                break;
                            case 2:
                                new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 3, article.getTitle()).start();
                                break;
                            case 3:
                                new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 2, article.getTitle()).start();
                                break;
                        }
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
            }
        });
    }

    @Override
    protected void initInject() {
        DaggerStoreArticlesComponent.builder().storeAriclesHttpModule(new StoreAriclesHttpModule()).storeArticlesModule(new StoreArticlesModule())
                .build().injectData(this);
    }
}
