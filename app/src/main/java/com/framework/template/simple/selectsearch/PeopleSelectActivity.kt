package com.framework.template.simple.selectsearch

import android.content.Intent
import android.provider.Contacts
import com.framework.base.parent.basics.BaseBindingActivity
import com.framework.template.R
import com.framework.template.databinding.ActivityPeopleSelectBinding
import java.io.Serializable

class PeopleSelectActivity : BaseBindingActivity<ActivityPeopleSelectBinding>() {

    private val sourcePeoples = mutableListOf<Contacts.People>()
    private val selectPeoples = mutableListOf<Contacts.People>()
    private val listPeoples = mutableListOf<Contacts.People>()
    private val peopleAdapter by lazy {
        PeopleAdapter(listPeoples, selectPeoples)
    }

    override fun contextViewId(): Int = R.layout.activity_people_select

    override fun initialize() {
        dataBinding.activity = this@PeopleSelectActivity
        dataBinding.submit.setOnClickListener {
            val intent = Intent()
            intent.putExtra(INTENT_PEOPLE_INFO, selectPeoples as Serializable)
            setResult(RESULT_OK, intent)
            finish()
        }
        initData()
        initView()
    }

    private fun initData() {
        val peoples = intent.getSerializableExtra(INTENT_SELECT_PEOPLE_INFO) as List<People>?
        peoples?.let {
            selectPeoples.clear()
            selectPeoples.addAll(it)
        }
        getPeoples()
    }

    private fun initView() {
        dataBinding.rvPeople.adapter = peopleAdapter
        dataBinding.etSearch.afterTextChanged {
            filterPeople(it)
        }
        dataBinding.ivSearch.setOnClickListener {
            filterPeople(dataBinding.etSearch.text.toString())
        }
    }

    private fun filterPeople(name: String) {
        val list = if (name.isNullOrEmpty()) {
            sourcePeoples
        } else {
            sourcePeoples.filter { it.nickName?.contains(name) == true }
        }
        list?.let {
            listPeoples.clear()
            listPeoples.addAll(it)
            peopleAdapter.notifyDataSetChanged()
        }
    }

    private fun getPeoples() {
        RetrofitManager.apiService.getPeoples()
            ?.enqueue(object : WorkCallNoVerification<ResponseListBean<List<People>>> {
                override fun onSuccess(result: ResponseListBean<List<People>>?) {
                    result?.rows?.let {
                        sourcePeoples.clear()
                        sourcePeoples.addAll(it)
                        listPeoples.clear()
                        listPeoples.addAll(sourcePeoples)
                        peopleAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailed(code: Int?) {
                }

            })
    }
}