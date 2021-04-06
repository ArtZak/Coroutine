package com.example.coroutine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.example.coroutine.databinding.FragmentMainBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var maxCount : EditText
    lateinit var count : EditText
    lateinit var submitBtn : Button
    lateinit var error : TextView

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        maxCount = binding.maxCount
        count = binding.count
        submitBtn = binding.submitBtn
        error = binding.error

        submitBtn.setOnClickListener {
            if(count.text.toString().toInt() > maxCount.text.toString().toInt()){
                error.text = "Invalid value!!!"
            }
            else{
                error.text = ""
                val bundle = Bundle()
                bundle.putInt("count", count.text.toString().toInt())
                bundle.putInt("maxCount", maxCount.text.toString().toInt())

                Log.d("test", "Main Fragment: $count , $maxCount")

                Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_actionFragment, bundle)
            }
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}