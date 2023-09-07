package uz.mobile.tracker.presentation.ui.screen.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.mobile.tracker.R
import uz.mobile.tracker.data.local.room.dao.RunDao
import uz.mobile.tracker.databinding.ActivityMainBinding
import javax.inject.Inject
@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.navController
            bottomNavigationView.setupWithNavController(navController)

            navHostFragment.findNavController()
                .addOnDestinationChangedListener{_,destination, _ ->
                    when(destination.id){
                        R.id.settingsFragment, R.id.runFragment,R.id.statisticFragment ->{
                            bottomNavigationView.visibility = View.VISIBLE
                        }
                        else ->{
                            bottomNavigationView.visibility = View.GONE
                        }
                    }
                }


        }


    }
}