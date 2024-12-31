package com.example.rewater

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class CounterIncrementWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("ReWaterPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val counter = sharedPreferences.getInt("counter", 0) + 1
        editor.putInt("counter", counter)
        editor.apply()

        return Result.success()
    }


}