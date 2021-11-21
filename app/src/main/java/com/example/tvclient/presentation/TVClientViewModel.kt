package com.example.tvclient.presentation

import android.app.Activity
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.work.*
import com.example.tvclient.R
import com.example.tvclient.domain.ChannelCategoriesUseCase
import com.example.tvclient.extensions.applyNotNulNorEmpty
import com.example.tvclient.workers.MyWorker
import com.example.tvclient.workers.WORKER_DATA_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "TVClientViewModel"
const val MY_WORK_NAME = "MY_WORK_NAME"
const val MY_WORK_TAG = "MY_WORK_TAG"

@HiltViewModel
class TVClientViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val channelCategoriesUseCase: ChannelCategoriesUseCase
    ) : ViewModel() {

    val isLoggedIn: LiveData<Boolean> = channelCategoriesUseCase
        .getUserPreferences()
        .map {
            it.isLoggedIn
        }.asLiveData()

    suspend fun updateIsLoggedIn(isLoggedIn: Boolean) =
        channelCategoriesUseCase.updateIsLoggedIn(isLoggedIn)

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

    fun showSettings(activity: Activity) {
        val action = TVFragmentDirections.settingsAction()
        activity.findNavController(R.id.nav_host_fragment).navigate(action)
    }
}

