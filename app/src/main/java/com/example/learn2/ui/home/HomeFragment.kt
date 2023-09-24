package com.example.learn2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.learn2.R
import com.example.learn2.bottomnavigationfragments.chats
import com.example.learn2.bottomnavigationfragments.doubts
import com.example.learn2.bottomnavigationfragments.tutors
import com.example.learn2.databinding.FragmentHomeBinding

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
        replaceFragment(tutors())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chats->replaceFragment(chats())
                R.id.doubts->replaceFragment(doubts())
                R.id.tutor->replaceFragment(tutors())

                else-> {

                }
            }
            true
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.frame_layout,fragment)
        fragmentManager.commit()
    }
}