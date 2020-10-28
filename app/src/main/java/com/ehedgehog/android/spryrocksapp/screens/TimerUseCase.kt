package com.ehedgehog.android.spryrocksapp.screens

import com.ehedgehog.android.spryrocksapp.network.Time
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class TimerUseCase {

    private var timerDisposable: Disposable? = null

    fun startTimer(storedTime: Time?, interval: Long?, onSubscribe: (time: Time) -> Unit) {
        timerDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
            .flatMap { time -> Observable.just(convertTime(time, storedTime, interval)) }
            .subscribe(onSubscribe)
    }

    fun stopTimer() {
        if (timerDisposable != null) {
            timerDisposable!!.dispose()
            timerDisposable = null
        }
    }

    private fun convertTime(time: Long, storedTime: Time?, interval: Long?): Time {
        var newTime = time
        if (interval != null && interval != 0L) {
            newTime += interval
        }

        if (storedTime != null) {
            newTime += storedTime.totalSeconds
        }

        val hours = newTime / 3600
        val minutes = (newTime / 60) % 60
        val seconds = newTime % 60

        return Time(hours, minutes, seconds, newTime)
    }

}