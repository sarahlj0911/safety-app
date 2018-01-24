package com.plusmobileapps.safetyapp.surveys.location

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.plusmobileapps.safetyapp.R
import com.plusmobileapps.safetyapp.surveys.survey.SurveyActivity

import kotlinx.android.synthetic.main.viewholder_survey.*
import kotlinx.android.synthetic.main.viewholder_survey.view.*

class LocationAdapter(private val surveys: List<LocationSurveyOverview>) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { view ->
                val intent = Intent(view.context, SurveyActivity::class.java)
                val location = view.title.text.toString()
                intent.putExtra(EXTRA_LOCATION, location)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_survey, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        val survey = surveys[position]
        
        holder.itemView.title.text = survey.title
        holder.itemView.checkmark.visibility = if (survey.isFinished) View.VISIBLE else View.INVISIBLE
        if (survey.progress > 0) {
            holder.itemView.progressBar.visibility = View.VISIBLE
            holder.itemView.progressBar.progress = survey.progress
        } else {
            holder.itemView.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return surveys.size
    }

    companion object {
        private val TAG = "LocationAdapter"
        val EXTRA_LOCATION = "com.plusmobileapps.safetyapp.survey.overview.LOCATION"
    }
}
