package com.nhahv.speechrecognitionpoint.ui.subjects

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhahv.speechrecognitionpoint.R

class SubjectsFragment : Fragment() {

    companion object {
        fun newInstance() = SubjectsFragment()
    }

    private lateinit var viewModel: SubjectsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.subjects_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
