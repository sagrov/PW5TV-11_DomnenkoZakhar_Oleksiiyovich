package com.example.task_5_new.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task_5_new.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCalculateTask1.setOnClickListener { Task1() }

        return root
    }

    fun readUserInput(): List<String> {
        val userSelectedKeys = mutableListOf<String>()
        val indexToElement = mapOf(
            1 to "ПЛ-110 кВ",
            2 to "ПЛ-35 кВ",
            3 to "ПЛ-10 кВ",
            4 to "КЛ-10 кВ (траншея)",
            5 to "КЛ-10 кВ (кабельний канал)",
            6 to "Т-110 кВ",
            7 to "Т-35 кВ",
            8 to "Т-10 кВ (кабельна мережа 10 кВ)",
            9 to "Т-10 кВ (повітряна мережа 10 кВ)",
            10 to "В-110 кВ (елегазовий)",
            11 to "В-10 кВ (малооливний)",
            12 to "В-10 кВ (вакуумний)",
            13 to "АВ-0.38 кВ",
            14 to "ЕД 6, 10 кВ",
            15 to "ЕД 0,38 кВ"
        )

        while (true) {
            val inputEP = binding.userKeys.text.toString()

            if (!inputEP.isNullOrBlank()) {
                val selectedOptions = inputEP.split(" ")
                var allValid = true

                for (option in selectedOptions) {
                    val index = option.toIntOrNull() ?: -1
                    if (indexToElement.containsKey(index)) {
                        val key = indexToElement[index]!!
                        userSelectedKeys.add(key)
                    } else {
                        allValid = false
                        break
                    }
                }
                if (allValid) break
            }
        }

        println(userSelectedKeys)
        return userSelectedKeys
    }

    private fun round(num: Double) = "%.5f".format(num)

    private fun Task1()
    {
        val epsElements = mapOf(
            "ПЛ-110 кВ" to mapOf("omega" to 0.07, "tv" to 10.0, "mu" to 0.167, "tp" to 35.0),
            "ПЛ-35 кВ" to mapOf("omega" to 0.02, "tv" to 8.0, "mu" to 0.167, "tp" to 35.0),
            "ПЛ-10 кВ" to mapOf("omega" to 0.02, "tv" to 10.0, "mu" to 0.167, "tp" to 35.0),
            "КЛ-10 кВ (траншея)" to mapOf("omega" to 0.03, "tv" to 44.0, "mu" to 1.0, "tp" to 9.0),
            "КЛ-10 кВ (кабельний канал)" to mapOf("omega" to 0.005, "tv" to 17.5, "mu" to 1.0, "tp" to 9.0),
            "Т-110 кВ" to mapOf("omega" to 0.015, "tv" to 100.0, "mu" to 1.0, "tp" to 43.0),
            "Т-35 кВ" to mapOf("omega" to 0.02, "tv" to 80.0, "mu" to 1.0, "tp" to 28.0),
            "Т-10 кВ (кабельна мережа 10 кВ)" to mapOf("omega" to 0.005, "tv" to 60.0, "mu" to 0.5, "tp" to 10.0),
            "Т-10 кВ (повітряна мережа 10 кВ)" to mapOf("omega" to 0.05, "tv" to 60.0, "mu" to 0.5, "tp" to 10.0),
            "В-110 кВ (елегазовий)" to mapOf("omega" to 0.01, "tv" to 30.0, "mu" to 0.1, "tp" to 30.0),
            "В-10 кВ (малооливний)" to mapOf("omega" to 0.02, "tv" to 15.0, "mu" to 0.33, "tp" to 15.0),
            "В-10 кВ (вакуумний)" to mapOf("omega" to 0.01, "tv" to 15.0, "mu" to 0.33, "tp" to 15.0),
            "АВ-0.38 кВ" to mapOf("omega" to 0.05, "tv" to 4.0, "mu" to 0.33, "tp" to 10.0),
            "ЕД 6, 10 кВ" to mapOf("omega" to 0.1, "tv" to 160.0, "mu" to 0.5, "tp" to 0.0),
            "ЕД 0,38 кВ" to mapOf("omega" to 0.1, "tv" to 50.0, "mu" to 0.5, "tp" to 0.0)
        )

        val userKeys = readUserInput()
        if (userKeys.isEmpty()) {
            binding.outputTask1.text = "Введені некоректні дані. Спробуйте ще раз."
            return
        }

        val n = binding.n.text.toString().toDouble()
        var omegaSum = 0.0
        var tRecovery = 0.0
        var maxTp = 0.0

        for (key in userKeys) {
            val element = epsElements[key]
            omegaSum += element?.get("omega") ?: 0.0
            tRecovery += (element?.get("omega") ?: 0.0) * (element?.get("tv") ?: 0.0)
            val tp = element?.get("tp") ?: 0.0
            if (tp > maxTp) maxTp = tp
        }

        omegaSum += 0.03 * n
        tRecovery += 0.06 * n
        tRecovery /= omegaSum

        val kAP = omegaSum * tRecovery / 8760
        val kPP = 1.2 * maxTp / 8760
        val omegaDK = 2 * 0.295 * (kAP + kPP)
        val omegaDKS = omegaDK + 0.02

        var output = "Частота відмов одноколової системи: ${round(omegaSum)} рік^-1\n" +
                "Середня тривалість відновлення: ${round(tRecovery)} год\n" +
                "Коефіцієнт аварійного простою: ${round(kAP)}\n" +
                "Коефіцієнт планового простою: ${round(kPP)}\n" +
                "Частота відмов одночасно двох кіл двоколової системи: ${round(omegaDK)} рік^-1\n" +
                "Частота відмов двоколової системи з урахуванням секційного вимикача: ${round(omegaDKS)} рік^-1\n"

        binding.outputTask1.text = output;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}