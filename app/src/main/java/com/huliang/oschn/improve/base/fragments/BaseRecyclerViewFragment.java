package com.huliang.oschn.improve.base.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.bean.base.PageBean;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.widget.RecyclerRefreshLayout;
import com.huliang.oschn.ui.empty.EmptyLayout;
import com.huliang.oschn.util.TLog;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * 基本列表类 RecyclerView，重写getLayoutId()自定义界面
 * <p>
 * Created by huliang on 17/3/19.
 */
public abstract class BaseRecyclerViewFragment<T> extends BaseFragment implements
        RecyclerRefreshLayout.SuperRefreshLayoutListener,
        BaseRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "BaseRecyclerView";

    protected BaseRecyclerAdapter<T> mAdapter;
    protected RecyclerView mRecyclerView;
    protected RecyclerRefreshLayout mRefreshLayout;
    protected EmptyLayout mErrorLayout;

    protected TextHttpResponseHandler mHandler;
    protected boolean isRefreshing;
    protected PageBean<T> mBean; // 页面bean, 用来控制翻页

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRefreshLayout = (RecyclerRefreshLayout) root.findViewById(R.id.refreshLayout);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
    }

    @Override
    protected void initData() {
        super.initData();
        mBean = new PageBean<>();

        mAdapter = getRecyclerAdapter();
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRefreshLayout.setSuperRefreshLayoutListener(this); // 添加刷新加载监听
        mRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TLog.log(responseString);
                try {
                    ResultBean<PageBean<T>> resultBean = AppOperator.createGson().fromJson(
                            responseString, getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        setListData(resultBean);
                        onRequestSuccess(resultBean.getCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onRequestFinish();
            }
        };

        // 空页面, 首先加载本地缓存; 本地缓存为空, 则加载网络数据
        if (isNeedEmptyView()) {
            // frameLayout -- mErrorLayout 和 mRefreshLayout 二者只有一个可见
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mRefreshLayout.setVisibility(View.GONE);

            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            // 添加一条任务到 UI 线程, 即将执行
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true); // 下拉刷新动画
                    onRefreshing(); // 加载网络数据
                }
            });
        } else {

        }
    }

    @Override
    public void onRefreshing() {
        isRefreshing = true;
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, true); // adapter加载状态
        requestData();
    }

    @Override
    public void onLoadMore() {
        mAdapter.setState(isRefreshing ? BaseRecyclerAdapter.STATE_HIDE :
                BaseRecyclerAdapter.STATE_LOADING, true); // adapter加载状态
        requestData();
    }

    /**
     * 网络请求结束
     */
    protected void onRequestFinish() {
        onComplete();
    }

    /**
     * 传递状态给RecyclerRefreshLayout,停止刷新
     */
    protected void onComplete() {
        mRefreshLayout.onComplete();
        isRefreshing = false;
    }

    protected void setListData(ResultBean<PageBean<T>> resultBean) {
        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
        if (isRefreshing) {
            mAdapter.clear(); // 清空数据
            mBean.setItems(resultBean.getResult().getItems());
            mAdapter.addAll(mBean.getItems());
            mRefreshLayout.setCanLoadMore(true);
        } else {
            mAdapter.addAll(resultBean.getResult().getItems());
        }

        // 如果没有Item或个数小于20, 则表示数据加载完毕
        if (resultBean.getResult().getItems() == null ||
                resultBean.getResult().getItems().size() < 20) {
            mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
        }

        // 如果数据大于0, 则隐藏 errorLayout
        if (mAdapter.getItems().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开始获取网络数据
     */
    protected void requestData() {

    }

    /**
     * 网络加载成功
     *
     * @param code
     */
    protected void onRequestSuccess(int code) {

    }

    /**
     * 需要空的View
     *
     * @return
     */
    protected boolean isNeedEmptyView() {
        return true;
    }

    /**
     * 由具体页面传入adapter以填充recycleView
     *
     * @return
     */
    protected abstract BaseRecyclerAdapter<T> getRecyclerAdapter();

    /**
     * 由具体页面传入类型 <T> 以解析json字符串
     *
     * @return
     */
    protected abstract Type getType();

    @Override
    public void onItemClick(int position, long itemId) {

    }
}
