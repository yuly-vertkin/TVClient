package com.example.tvclient.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.tvclient.R
import com.example.tvclient.databinding.FragmentTvDetailBinding

private const val TAG = "TVClientDetailFragment"
private const val CHANNEL_ID = "TVClient channel 1"
private const val NOTIFICATION_ID = 1

class TVDetailFragment : Fragment() {

    private lateinit var binding: FragmentTvDetailBinding
    private val viewModel: TvDetailViewModeL by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.detail.setOnClickListener {
            // show notification
            showNotification()
            // or show simple dialog
//            SimpleDialog.show( this@TVDetailFragment,
//                titleId = R.string.dialog_title,
//                textId = R.string.dialog_text,
//                onCancelClick = { Log.d(TAG,"Cancel pressed") }
//            ) {
//                Log.d(TAG,"OK pressed")
//            }
        }
    }

    override fun onDestroyView() {
        setFragmentResult(TV_DETAIL_REQUEST_KEY, bundleOf(TV_DETAIL_RESULT_KEY to "TVDetailFragment success"))
        super.onDestroyView()
    }

    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(requireActivity().applicationContext)
        createNotificationChannel(notificationManager)

        val intent = Intent(requireContext(), TVClientActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_nav_tv)
            .setContentTitle("Notification title")
            .setContentText("Notification small text")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Notification biggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg text"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManagerCompat) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.notif_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}