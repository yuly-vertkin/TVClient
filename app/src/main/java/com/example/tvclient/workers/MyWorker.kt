package com.example.tvclient.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val inputData = inputData.getInt(WORKER_DATA_KEY, 0)

        makeStatusNotification("MyWorker$inputData", appContext)
        sleep()

        val outputData = workDataOf(WORKER_DATA_KEY to inputData + 1)
        return Result.success(outputData)
    }
}