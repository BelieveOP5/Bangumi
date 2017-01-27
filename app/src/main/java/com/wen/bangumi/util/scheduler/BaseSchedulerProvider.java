package com.wen.bangumi.util.scheduler;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * 这里提供三种线程
 * Allow providing different types of {@link io.reactivex.Scheduler}s.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
