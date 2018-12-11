package com.nhahv.speechrecognitionpoint.ui.mainExam

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.adapters.PointOfSubjectAdapter
import com.nhahv.speechrecognitionpoint.data.models.MarmotExam
import com.nhahv.speechrecognitionpoint.data.models.MarmotExamItem
import com.nhahv.speechrecognitionpoint.data.models.MarmotExamPointItem
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.main_exam_fragment.*

class MainExamFragment : Fragment() {

    private lateinit var viewModel: MainExamViewModel
    private var idExamObject: String? = null
    private var idGroupExam: String? = null
    private var idSubjectExam: String? = null
    private var nameSubjectExam: String? = null
    private var marmotExamPointItem: MarmotExamPointItem? = null
    private lateinit var speechPoint: SpeechPoint
    private val textCheckList = arrayListOf<String>("Dừng", "dùng", "Đừng", "chừng", "bừng", "từng", "vừng", "xừng", "sừng", "sùng")
    private var isPauseSpeech = true
    private var indexUpdatePoint = -1

    private val marmotExamItems = ArrayList<MarmotExamItem>()
    private val marmotExamAdapter: PointOfSubjectAdapter by lazy {
        PointOfSubjectAdapter(marmotExamItems) { _, marmotExamItem, i ->
            showInputExamPoint("Nhập điểm mã phách ${marmotExamItem.idMarmot}", i)
        }
    }

    private val spinnerMarmotAdapter: ArrayAdapter<MarmotExam> by lazy {
        ArrayAdapter<MarmotExam>(requireContext(), android.R.layout.simple_list_item_1)
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

        speechPoint = SpeechPoint(requireContext())
        speechPoint.speechPointCallback { textSpeech ->
            doSomeThingWithTextSpeech(textSpeech)
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

        marmotExamPointItem?.marmotExams?.let {
            spinnerMarmotAdapter.addAll(it)
        }
        spMarmot.adapter = spinnerMarmotAdapter
        spMarmot.setSelection(0)
        spMarmot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerMarmotAdapter.getItem(position)?.idMarmot?.let {
                    marmotExamAdapter.filter.filter(it)
                }
            }
        }

        textSearchExamExam.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                marmotExamAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        startMic.setOnClickListener {
            if (!speechPoint.speechStarted) {
                (requireActivity() as MainActivity).startSpeech { speechPoint.startSpeech() }
                startMic.setImageResource(R.drawable.ic_stop)
            } else {
                speechPoint.cancel()
                textSpeech.text = ""
                startMic.setImageResource(R.mipmap.ic_mic_white_24dp)
            }
        }
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
                navigate(R.id.action_mainExamFragment_to_exportExcelFragment, Bundle().apply {
                    putString(Constant.BUNDLE_ID_EXAM, idExamObject)
                    putString(Constant.BUNDLE_ID_GROUP_EXAM, idGroupExam)
                    putString(Constant.BUNDLE_ID_SUBJECT_EXAM, idSubjectExam)
                    putString(Constant.BUNDLE_NAME_SUBJECT_EXAM, nameSubjectExam)
                    putBoolean(Constant.BUNDLE_IS_MAIN_EXAM, true)
                })
            }
            R.id.logout -> {
                SharedPrefs.getInstance(requireContext()).put(Constant.IS_LOGIN, false)
                navigateClearStack(R.id.action_mainExamFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        speechPoint.cancel()
        updateMarmotExamPointToSharePref(marmotExamPointItem)
        super.onStop()
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
                position?.let {
                    marmotExamAdapter.marmotFilter[it].pointOfMarmot = pointValue.toString()
                    marmotExamItems.forEach { child ->
                        if (child.idMarmot == marmotExamAdapter.marmotFilter[it].idMarmot) {
                            child.pointOfMarmot = pointValue.toString()
                        }
                    }
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

    private fun doSomeThingWithTextSpeech(text: String) {
        println("============= $text")
        if (isPauseSpeechPoint(text)) {
            isPauseSpeech = true
            return
        }
        if (isInMarmotExamItem(text)) {
            isPauseSpeech = false
            return
        }
        if (isPauseSpeech) {
            if (isInMarmotExamItem(text)) {
                isPauseSpeech = false
            }
            return
        }
        val textSpeech = replaceNumber(text)
        try {
            val point = CommonUtils.round(textSpeech.toDouble(), 2)
            val item = marmotExamAdapter.marmotFilter[indexUpdatePoint]
            item.pointOfMarmot = point.toString()
            marmotExamItems.forEach { if (it.idMarmot == item.idMarmot) it.pointOfMarmot = point.toString() }
            marmotExamAdapter.notifyDataSetChanged()
            indexUpdatePoint += 1
            marmotExamPointItem?.marmotExamItems = marmotExamItems
            updateMarmotExamPointToSharePref(marmotExamPointItem)
        } catch (ex: NumberFormatException) {
            ex.printStackTrace()
            toast("Định dạng không đúng, nhập lai điểm")
        }
    }

    private fun isInMarmotExamItem(text: String): Boolean {
        marmotExamAdapter.marmotFilter.forEachIndexed { index, marmotExamItem ->
            if (marmotExamItem.idMarmot.trim().toUpperCase() == text.trim().toUpperCase()) {
                indexUpdatePoint = index
                return true
            }
        }
        return false
    }

    private fun isPauseSpeechPoint(text: String): Boolean {
        textCheckList.forEach { if (it.trim().toUpperCase().contains(text.trim().toUpperCase())) return true }
        return false
    }

    private fun replaceNumber(textReplace: String): String {
        return textReplace.toLowerCase()
                .replace(" ", "")
                .replace(",", ".")
                .replace("một", "1")
                .replace("hai", "2")
                .replace("hay", "2")
                .replace("hài", "2")
                .replace("bai", "3")
                .replace("ba", "3")
                .replace("bốn", "4")
                .replace("năm", "5")
                .replace("sáu", "6")
                .replace("bảy", "7")
                .replace("tám", "8")
                .replace("chín", "9")
                .replace("mườimột", "11")
                .replace("mười", "10")
    }
}
