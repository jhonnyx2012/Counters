package com.jhonnyx.counters.di;

import com.core.di.component.ActivityComponent;
import com.jhonnyx.counters.presentation.CountersActivity;
import dagger.Component;

@Component(modules = CountersModule.class)
public interface CountersComponent extends ActivityComponent<CountersActivity>{}