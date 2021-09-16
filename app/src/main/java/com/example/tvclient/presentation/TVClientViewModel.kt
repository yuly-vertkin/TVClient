package com.example.tvclient.presentation

import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.work.*
import com.example.tvclient.extensions.applyNotNulNorEmpty
import com.example.tvclient.workers.MyWorker
import com.example.tvclient.workers.WORKER_DATA_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val MY_WORK_NAME = "MY_WORK_NAME"
const val MY_WORK_TAG = "MY_WORK_TAG"

@HiltViewModel
class TVClientViewModel @Inject constructor(
    private val workManager: WorkManager
    ) : ViewModel() {
    lateinit var workMenuItem: MenuItem

    val isWorkRuning: LiveData<Boolean> =
        workManager.getWorkInfosByTagLiveData(MY_WORK_TAG)
            .map { infoList ->
                infoList.applyNotNulNorEmpty {
                    !it[it.lastIndex].state.isFinished
                } ?: false
            }.distinctUntilChanged()

    fun startStopWorker(isRunning: Boolean) {
        Log.d(TAG, "startStopWorker isRunning: $isRunning")
        if (isRunning) {
            workManager.cancelUniqueWork(MY_WORK_NAME)
        } else {
            workManager.beginUniqueWork(
                MY_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                createWorkRequest(true)
            ).then(createWorkRequest(false))
                .then(createWorkRequest(false))
                .enqueue()
        }
    }

    private fun createWorkRequest(withInputData: Boolean) : OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        val myWorkerBuilder = OneTimeWorkRequestBuilder<MyWorker>()
            .addTag(MY_WORK_TAG)
            .setConstraints(constraints)

        if (withInputData) {
            myWorkerBuilder.setInputData(createInputData())
        }

        return myWorkerBuilder.build()
    }

    private fun createInputData() : Data {
        return Data.Builder()
            .putInt(WORKER_DATA_KEY, 1)
            .build()
    }
}

