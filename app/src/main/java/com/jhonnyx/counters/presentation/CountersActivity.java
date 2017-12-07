package com.jhonnyx.counters.presentation;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.core.presentation.activity.BaseActivity;
import com.core.util.AndroidUtils;
import com.jhonnyx.counters.R;
import com.jhonnyx.counters.databinding.ActivityCountersBinding;
import com.jhonnyx.counters.di.DaggerCountersComponent;
import com.jhonnyx.counters.domain.model.Counter;
import com.jhonnyx.counters.presentation.adapter.CounterAdapter;
import com.jhonnyx.counters.presentation.contract.Counters;
import java.util.List;
import javax.inject.Inject;

public class CountersActivity extends BaseActivity<ActivityCountersBinding> implements Counters.View {

    @Inject Counters.Presenter presenter;
    @Inject CounterAdapter adapter;

    @Override protected int getLayoutId() {return R.layout.activity_counters;}

    @Override protected void injectDependencies() {
        DaggerCountersComponent.builder().build().inject(this);
    }

    @Override protected void initView() {
        binder.rvCounters.setAdapter(adapter);
        binder.rvCounters.setNestedScrollingEnabled(false);
        presenter.initialize(this);
        adapter.setListener(presenter);binder.tilNewCounterName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AndroidUtils.hideSoftKeyboard(CountersActivity.this);
                    presenter.createCounter();
                    return true;
                }
                return false;
            }
        });
    }

    @Override public void updateCounters(List<Counter> counters) {
        adapter.setList(counters);
    }

    @Override public void updateTotal(String totalCount) {
        binder.tvTotal.setText(totalCount);
    }

    @Override public String getNewCounterTitle() {
        return binder.tilNewCounterName.getText().toString().trim();
    }

    @Override public void showMessage(int idString) {showMessage(getString(idString));}

    @Override public void clearTextInput() {
        binder.tilNewCounterName.setText("");
    }

    @Override public void showProgress(boolean show) {
        binder.progressBar.setVisibility(show?View.VISIBLE:View.GONE);
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}