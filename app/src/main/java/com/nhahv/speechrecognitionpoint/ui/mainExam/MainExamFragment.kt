package com.nhahv.speechrecognitionpoint.ui.mainExam

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.item_group_exam.*
import kotlinx.android.synthetic.main.main_exam_fragment.*

class MainExamFragment : Fragment() {

    private lateinit var viewModel: MainExamViewModel
    private var idExamObject: String? = null
    private var idGroupExam: String? = null
    private var idSubjectExam: String? = null
    private var nameSubjectExam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            with(it) {
                idExamObject = getString(Constant.BUNDLE_ID_EXAM)
                idGroupExam = getString(Constant.BUNDLE_ID_GROUP_EXAM)
                idSubjectExam = getString(Constant.BUNDLE_ID_SUBJECT_EXAM)
                nameSubjectExam = getString(Constant.BUNDLE_NAME_SUBJECT_EXAM)
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_exam_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar2, "Nhập điểm môn thi $nameSubjectExam")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainExamViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main_exam, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.fileExcelFragment -> {
                navigate(R.id.action_mainExamFragment_to_fileExcelFragment, Bundle().apply {

                })
            }
            R.id.exportExcelFragment -> {
                navigate(R.id.action_mainExamFragment_to_exportExcelFragment, arguments!!)
            }
            R.id.logout -> {
                SharedPrefs.getInstance(requireContext()).put(Constant.IS_LOGIN, false)
                navigateClearStack(R.id.action_mainExamFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
