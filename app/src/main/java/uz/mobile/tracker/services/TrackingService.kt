package uz.mobile.tracker.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.database.Observable
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import pub.devrel.easypermissions.EasyPermissions
import uz.mobile.tracker.R
import uz.mobile.tracker.presentation.ui.screen.activity.MainActivity
import uz.mobile.tracker.util.Constance.ACTION_PAUSE_SERVICE
import uz.mobile.tracker.util.Constance.ACTION_SHOW_TRACKING_FRAGMENT
import uz.mobile.tracker.util.Constance.ACTION_START_OR_RESUME_SERVICE
import uz.mobile.tracker.util.Constance.ACTION_STOP_SERVICE
import uz.mobile.tracker.util.Constance.NOTIFICATION_CHANNEL_ID
import uz.mobile.tracker.util.Constance.NOTIFICATION_CHANNEL_NAME
import uz.mobile.tracker.util.Constance.NOTIFICATION_ID
import uz.mobile.tracker.util.TrackingUtil

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {

    var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoint = MutableLiveData<Polylines>()
    }

    private fun pauseService(){
        isTracking.postValue(false)
    }
    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoint.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        startForegroundService()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            if (isTracking.value!!) {
                result?.locations?.let {
                    for (i in it) {
                        addPathPoint(i)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtil.hasLocationPermission(this)) {
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = 5000L
                    fastestInterval = 1500L
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request, locationCallback, Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    private fun addPathPoint(location: Location) {
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoint.value?.apply {
                last().add(position)
                pathPoint.postValue(this)
            }
        }
    }

    private fun startForegroundService() {
        isTracking.postValue(true)
        addEmptyPolyLine()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running...")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }


    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        1,
        Intent(this, MainActivity::class.java)
            .also {
                it.action = ACTION_SHOW_TRACKING_FRAGMENT
            },
        FLAG_IMMUTABLE
    )

    private fun addEmptyPolyLine() {
        pathPoint.value?.apply {
            add(mutableListOf())
            pathPoint.postValue(this)
        } ?: pathPoint.postValue(mutableListOf(mutableListOf()))
    }

}









