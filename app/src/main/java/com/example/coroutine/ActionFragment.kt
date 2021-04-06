package com.example.coroutine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.coroutine.databinding.FragmentActionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActionFragment : Fragment() {
    lateinit var binding: FragmentActionBinding
    lateinit var countView : TextView
    lateinit var addBtn : Button
    lateinit var takeBtn : Button
    lateinit var resetBtn : Button

    var baseCount = 0
    var count = 0
    var maxCount = 0

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentActionBinding.inflate(inflater, container, false)
        countView = binding.count
        addBtn = binding.addBtn
        takeBtn = binding.takeBtn
        resetBtn = binding.resetBtn

        baseCount = arguments?.getInt("count", 0) ?: 0
        count = baseCount
        maxCount = arguments?.getInt("maxCount", 0) ?: 0

        countView.text = count.toString()

        if(count == maxCount){
            resetBtn.visibility = View.VISIBLE
        }

        addBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default){
                Log.d("test", "Add Button launched in thread ${Thread.currentThread()}")
                if(count < maxCount - 1){
                    count++
                    withContext(Dispatchers.Main){
                        Log.d("test", "Add Button with context started in thread ${Thread.currentThread()}")
                        countView.text = count.toString()
                        resetBtn.visibility = View.GONE
                    }
                }
                else if(count < maxCount){
                    count++
                    withContext(Dispatchers.Main){
                        countView.text = count.toString()
                        resetBtn.visibility = View.VISIBLE
                    }
                }
            }
        }

        takeBtn.setOnClickListener {
            GlobalScope.launch (Dispatchers.Default){
                if(count > 1){
                    count--
                    withContext(Dispatchers.Main){
                        countView.text = count.toString()
                        resetBtn.visibility = View.GONE
                    }
                }
                else if(count > 0){
                    count--
                    withContext(Dispatchers.Main){
                        countView.text = count.toString()
                        resetBtn.visibility = View.VISIBLE
                    }
                }
            }

        }

        resetBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                count = baseCount
                withContext(Dispatchers.Main){
                    countView.text = count.toString()
                    if(count != maxCount) {
                        resetBtn.visibility = View.GONE
                    }
                }
            }

        }
        return binding.root
    }

    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                ActionFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}