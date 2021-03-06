package com.mwmurawski.nutritioninfo.ui.main.adapter;

import com.mwmurawski.nutritioninfo.data.db.model.search.SearchItem;
import com.mwmurawski.nutritioninfo.ui.main.MainPresenter;

import java.util.List;

public interface ItemAdapterView {
    void setData(List<SearchItem> listOfItems);
    void setPresenter(MainPresenter presenter);
}
