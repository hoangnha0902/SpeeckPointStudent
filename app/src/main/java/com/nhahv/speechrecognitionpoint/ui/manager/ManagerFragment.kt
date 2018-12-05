package com.nhahv.speechrecognitionpoint.ui.manager

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.util.navigate
import kotlinx.android.synthetic.main.manager_fragment.*

class ManagerFragment : Fragment() {

    private lateinit var viewModel: ManagerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.manager_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managerClass.setOnClickListener {
            navigate(R.id.action_managerFragment_to_classStudentFragment, Bundle.EMPTY)
        }
        managerExam.setOnClickListener {
            navigate(R.id.action_managerFragment_to_examFragment, Bundle.EMPTY)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManagerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
