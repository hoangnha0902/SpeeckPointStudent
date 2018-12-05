package com.nhahv.speechrecognitionpoint.ui.exam

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter

import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.ExamObject
import kotlinx.android.synthetic.main.exam_fragment.*

class ExamFragment : Fragment() {

    private lateinit var viewModel: ExamViewModel
    private val examObjectList = ArrayList<ExamObject>()
    private val examAdapter: ExamAdapter by lazy { ExamAdapter(examObjectList) { view, examObject, i -> } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exam_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        examObjectList.add(ExamObject())
        examObjectList.add(ExamObject())
        examObjectList.add(ExamObject())
        examObjectList.add(ExamObject())
        examList.adapter = examAdapter


    }

    class ExamAdapter(
            examObjects: ArrayList<ExamObject>,
            listener: ((View, ExamObject, Int) -> Unit)?
    ) : BaseRecyclerAdapter<ExamObject>(examObjects, R.layout.item_exam, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
        }
    }

}
