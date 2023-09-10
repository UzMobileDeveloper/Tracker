package uz.mobile.tracker.presentation.ui.screen.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import uz.mobile.tracker.R
import uz.mobile.tracker.databinding.FragmentRunBinding
import uz.mobile.tracker.databinding.FragmentTrackingBinding
import uz.mobile.tracker.presentation.viewModel.MainViewModel
import uz.mobile.tracker.services.Polyline
import uz.mobile.tracker.services.TrackingService
import uz.mobile.tracker.util.Constance
import uz.mobile.tracker.util.Constance.ACTION_PAUSE_SERVICE
import uz.mobile.tracker.util.Constance.ACTION_START_OR_RESUME_SERVICE

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentTrackingBinding
    var isTracking = false
    var pathPoints = mutableListOf<Polyline>()
    private var map: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            btnToggleRun.setOnClickListener {
                toggleRun()
            }
            mapView.onCreate(savedInstanceState)

            mapView.getMapAsync {
                map = it
                addAllPolylines()
            }
            subscribeToObserve()
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.GREEN)
                .width(10f)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun toggleRun() {
        if (isTracking) {
            sendCommandService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun subscribeToObserve() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoint.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCamera()
        })
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        binding.apply {
            if (!isTracking) {
                btnToggleRun.text = "Start"
                btnFinishRun.visibility = View.VISIBLE
            } else {
                btnToggleRun.text = "Stop"
                btnFinishRun.visibility = View.GONE
            }

        }
    }

    private fun moveCamera() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    15f
                )
            )
        }

    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLang = pathPoints.last()[pathPoints.last().size - 2]
            val lastLang = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.GREEN)
                .width(10f)
                .add(preLastLang)
                .add(lastLang)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView?.onSaveInstanceState(outState)
    }

}