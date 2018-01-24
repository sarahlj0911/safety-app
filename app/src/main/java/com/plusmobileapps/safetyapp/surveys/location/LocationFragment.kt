package com.plusmobileapps.safetyapp.surveys.location

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.plusmobileapps.safetyapp.R

//how to setup the view bindings of the id's set in the xml
// kotlinx.android.synthetic.main.{insert name of xml file).*
import kotlinx.android.synthetic.main.fragment_survey.*

class LocationFragment : Fragment() {

    // val - you cannot reassign the field with an assignment operator =
    // mutableListOf - kotlin list you can add more elements to ex. surveys.add(LocationSurveyOverview())
    //listOf - kotlin list you cannot alter by adding or removing elements
    val surveys = mutableListOf<LocationSurveyOverview>()
    val titles = mutableListOf<String>()

    //var - field declaration that allows for mutability meaning you can use the assignment operator =
    var isCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // !! saying i know this thing is not null, otherwise will crash with null pointer exception
        val rootView = inflater?.inflate(R.layout.fragment_survey, container, false)
        rootView?.tag = TAG


        return rootView

    }

    override fun onResume() {
        super.onResume()
        //you can just call the id directly as a variable anywhere in the class
        surveyRecyclerview.adapter = LocationAdapter(surveys)
        surveyRecyclerview.layoutManager = LinearLayoutManager(context)
    }

    //this is equivalent to a static in java
    //since kotlin is statically typed, cannot use the keyword static
    companion object {
        private val TAG = "SurveyViewFragment"    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        fun newInstance(isCompleted: Boolean): LocationFragment {
            val fragment = LocationFragment()
            fragment.isCompleted = isCompleted
            fragment.populateSurveys()
            return fragment
        }

        fun newInstance(): LocationFragment {
            val fragment = LocationFragment()
            fragment.createNewSurvey()
            return fragment
        }
    }

    //populate array list
    private fun populateSurveys() {
        addLocations()

        if (isCompleted) {
            for (i in titles.indices) {
                surveys.add(LocationSurveyOverview(title = titles[i]))
                surveys[i].isFinished = true
            }
        } else {
            for (i in titles.indices) {
                surveys.add(LocationSurveyOverview(title = titles[i]))
                var progress = 90
                if (i == 0) {
                    //no op to keep progress state at 0
                } else if (i % 2 == 1) {
                    surveys[i].isFinished = true
                } else {
                    surveys[i].progress = progress
                    progress -= 10
                }
            }
        }
    }

    private fun addLocations() {
        titles.add("Bathroom")
        titles.add("Classroom 1")
        titles.add("Bathroom 2")
        titles.add("Locker Room")
        titles.add("Field")
        titles.add("Office")
        titles.add("Classroom 2")
        titles.add("Bathroom")
        titles.add("Classroom 1")
        titles.add("Bathroom 2")
        titles.add("Locker Room")
        titles.add("Field")
        titles.add("Office")
        titles.add("Classroom 2")
    }

    private fun createNewSurvey() {
        addLocations()
        for (title in titles) {
            surveys.add(LocationSurveyOverview(title = title))
        }
    }
}
