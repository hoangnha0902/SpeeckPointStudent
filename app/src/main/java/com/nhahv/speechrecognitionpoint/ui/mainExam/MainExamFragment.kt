package com.nhahv.speechrecognitionpoint.ui.mainExam

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.*
import com.nhahv.speechrecognitionpoint.ui.pointInput.PointInputFragment
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.item_group_exam.*
import kotlinx.android.synthetic.main.item_main_exam.view.*
import kotlinx.android.synthetic.main.main_exam_fragment.*

class MainExamFragment : Fragment() {

    private lateinit var viewModel: MainExamViewModel
    private var idExamObject: String? = null
    private var idGroupExam: String? = null
    private var idSubjectExam: String? = null
    private var nameSubjectExam: String? = null
    private var marmotExamPointItem: MarmotExamPointItem? = null
    private val marmotExamItems = ArrayList<MarmotExamItem>()
    private val marmotExamAdapter: PointOfSubjectAdapter by lazy {
        PointOfSubjectAdapter(marmotExamItems) { view, marmotExamItem, i ->
            showInputExamPoint("Nhập điểm mã phách ${marmotExamItem.idMarmot}", i)
        }
    }

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

        pointOfMarmotList.adapter = marmotExamAdapter
        fetchPointOfSubject()
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
                    putString(Constant.BUNDLE_ID_EXAM, idExamObject)
                    putString(Constant.BUNDLE_ID_GROUP_EXAM, idGroupExam)
                    putString(Constant.BUNDLE_ID_SUBJECT_EXAM, idSubjectExam)
                    putString(Constant.BUNDLE_NAME_SUBJECT_EXAM, nameSubjectExam)
                    putBoolean(Constant.BUNDLE_IS_MAIN_EXAM, true)
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


    private fun fetchPointOfSubject() {
        marmotExamItems.clear()
        marmotExamPointItem = getMarmotExamItems()
        if (marmotExamPointItem != null) {

        }
        marmotExamPointItem?.let {
            marmotExamItems.addAll(it.marmotExamItems)
            marmotExamAdapter.notifyDataSetChanged()
        }
    }

    private fun getMarmotExamItems(): MarmotExamPointItem? {
        val value = sharePrefs().get(prefMarmotName(idExamObject, idGroupExam, idSubjectExam), "")
        if (TextUtils.isEmpty(value)) {
            return null
        }
        return Gson().fromJson(value, MarmotExamPointItem::class.java)
    }


    private fun showInputExamPoint(label: String, position: Int) {
        fragmentManager?.let {
            val fm = it.beginTransaction()
            val prev = it.findFragmentByTag("inputPointExam")
            if (prev != null) {
                fm.remove(prev)
            }
            fm.addToBackStack(null)
            val dialog = InputPointExamFragment.newInstance(label, position)
            dialog.show(fm, "inputPointExam")
            dialog.inputExamPointCallback { pointValue, position ->
                println("=========== $pointValue  = $position")
                position?.let {
                    marmotExamItems[it].pointOfMarmot = pointValue.toString()
                    marmotExamPointItem?.marmotExamItems = marmotExamItems
                    updateMarmotExamPointToSharePref(marmotExamPointItem)
                    marmotExamAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateMarmotExamPointToSharePref(marmotExamPointItem: MarmotExamPointItem?) {
        sharePrefs().put(prefMarmotName(idExamObject, idGroupExam, idSubjectExam), marmotExamPointItem)
    }

    class PointOfSubjectAdapter(val marmotExams: ArrayList<MarmotExamItem>,
                                listener: ((View, MarmotExamItem, Int) -> Unit)?
    ) : BaseRecyclerAdapter<MarmotExamItem>(marmotExams, R.layout.item_main_exam, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            with(marmotExams[position]) {
                holder.itemView.apply {
                    idMarmotExam.text = idMarmot
                    pointOfMarmotExam.text = pointOfMarmot
                }
            }
        }
    }
}
