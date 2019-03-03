// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.jene.cognitive.utils.source;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @uthor Jorge Nieves
 */

public abstract class PeriodicEmittingDataSourceFunction<TEventType> implements SourceFunction<TEventType> {

    private CompositeDisposable disposables;

    @Override
    public void run(SourceContext<TEventType> sourceContext) throws Exception {

        disposables = new CompositeDisposable();

        Observable
                // We zip two Streams: The Iterable and an Interval emitting data stream:
                .zip(
                        Observable.fromIterable(iterable()),
                        Observable.interval(interval().toMillis(), TimeUnit.MILLISECONDS),
                        new BiFunction<TEventType, Long, TEventType>() {
                            @Override
                            public TEventType apply(@NonNull TEventType obs, @NonNull Long timer) throws Exception {
                                return obs;
                            }
                        }
                )
                // When the Subscription happens, add it to the list of Disposables:
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        disposables.add(disposable);
                    }
                })
                // We want this to be synchronous on the current thread, so do all this in a blocking subscribe:
                .blockingSubscribe(new Consumer<TEventType>() {
                    @Override
                    public void accept(TEventType event) throws Exception {
                        sourceContext.collect(event);
                    }
                });
    }

    @Override
    public void cancel() {
        if(disposables != null) {
            disposables.clear();
        }
    }

    protected abstract Iterable<TEventType> iterable();

    protected abstract Duration interval();
}
