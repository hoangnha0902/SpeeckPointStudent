package com.nhahv.speechrecognitionpoint.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.nhahv.speechrecognitionpoint.FileExcelActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.model.Student
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_import -> {
                requireActivity().startActivity(Intent(activity, FileExcelActivity::class.java))
            }
            R.id.menu_export -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val items = SharedPrefs.getInstance(activity!!.applicationContext).get(PREF_STUDENT, ArrayList<Student>())
        println("===================== $items")
    }

}
