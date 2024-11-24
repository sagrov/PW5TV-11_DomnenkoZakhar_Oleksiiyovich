package com.example.task_5_new.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task_5_new.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCalculateTask2.setOnClickListener { Task2() }

        return root
    }

    private fun round(num: Double) = "%.5f".format(num)

    private fun Task2()
    {
        val t = binding.t.text.toString().toDouble()
        val kp = binding.kp.text.toString().toDouble()
        val Pm = binding.Pm.text.toString().toDouble()
        val Tm = binding.Tm.text.toString().toDouble()
        val zPerA = binding.zPerA.text.toString().toDouble()
        val zPerP = binding.zPerP.text.toString().toDouble()
        val omega = binding.omega.text.toString().toDouble()
        val MWA = omega * t * Pm * Tm
        val MWP = kp * Pm * Tm
        val M = zPerA * MWA + zPerP * MWP
        var output = "Математичне сподівання аварійного недовідпущення: ${round(MWA)} кВт * год\n" +
                     "Математичне сподівання планового недовідпущення: ${round(MWP)} кВт * год\n" +
                     "Математичне сподівання збитків від перервання електропостачання: ${round(M)} грн\n"
        binding.outputTask2.text = output;
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}