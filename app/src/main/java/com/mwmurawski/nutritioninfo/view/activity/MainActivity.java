package com.mwmurawski.nutritioninfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.mwmurawski.nutritioninfo.R;
import com.mwmurawski.nutritioninfo.model.search.SearchItem;
import com.mwmurawski.nutritioninfo.presenter.component.DaggerMainActivityComponent;
import com.mwmurawski.nutritioninfo.presenter.component.MainActivityComponent;
import com.mwmurawski.nutritioninfo.presenter.presenter.MainActivityPresenter;
import com.mwmurawski.nutritioninfo.view.interfaces.ItemAdapterView;
import com.mwmurawski.nutritioninfo.view.interfaces.MainActivityView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainActivityPresenter> implements MainActivityView {

    @BindView(R.id.floating_search_view)    FloatingSearchView   searchView;
    @BindView(R.id.main_recyclerview)       RecyclerView         recyclerView;
    @BindView(R.id.progress_bar)            ProgressBar          progressBar;
    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout   swipeRefreshLayout;

    @Inject ItemAdapterView itemAdapter;

    @Override
    protected int getLayoutFile() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject() {
        presenterProviderInterface = DaggerMainActivityComponent.builder().applicationComponent(getApplicationComponent()).build();
        ((MainActivityComponent)presenterProviderInterface).inject(this);
    }

    @Override
    public void assignPresenterValuesToViewAfterRestore() {
        itemAdapter.setPresenter(presenter);
        itemAdapter.setData(presenter.getItemList());
        searchView.setSearchText(presenter.getQueryString());

    }

    /*
    ________LIFE CYCLE METHODS________
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //bindView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter((RecyclerView.Adapter) itemAdapter);

        setSearchListener();
        setSwipeSearchListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.bindView(this);
        presenter.startObserveFoodItemsClick(itemAdapter.getNdbnoClickObservable());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    //onRestart

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
    ________ CUSTOM METHODS ________
     */

    private void setSearchListener() {
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {}

            @Override
            public void onFocusCleared() {
                presenter.loadSearchResponse();
            }
        });

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                presenter.setQueryString(newQuery);
            }
        });
    }


    private void setSwipeSearchListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadSearchResponse();
            }
        });
    }

    /*
    ________PRESENTER METHODS________
     */

    @Override
    public void startDetailsActivity(final String ndbno) {
        Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra("ndbno", ndbno);
        startActivity(intent);
    }

    @Override
    public void putListToAdapter(List<SearchItem> searchItems) {
        itemAdapter.setData(searchItems);
        recyclerView.setAdapter((RecyclerView.Adapter) itemAdapter);
    }

    @Override
    public void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
