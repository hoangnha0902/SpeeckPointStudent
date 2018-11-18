package com.nhahv.speechrecognitionpoint.ui.subjectList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nhahv.speechrecognitionpoint.R

class SubjectListFragment : Fragment() {

    companion object {
        fun newInstance() = SubjectListFragment()
    }

    private lateinit var viewModel: SubjectListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.subject_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubjectListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
