package com.plusmobileapps.safetyapp.surveys.location


data class LocationSurveyOverview(
        var isFinished: Boolean = false,
        var title: String = "",
        var progress: Int = 0)
