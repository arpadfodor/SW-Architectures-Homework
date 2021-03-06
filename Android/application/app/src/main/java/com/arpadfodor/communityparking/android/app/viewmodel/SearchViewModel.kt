package com.arpadfodor.communityparking.android.app.viewmodel

import com.arpadfodor.communityparking.android.app.model.LocationService
import com.arpadfodor.communityparking.android.app.model.repository.ReportRepository
import com.arpadfodor.communityparking.android.app.viewmodel.utils.AppViewModel

class SearchViewModel : AppViewModel(){

    private fun getLocationFromAddress(address: String, resultCallback: (Double, Double) -> Unit){
        return LocationService.getLocationFromAddress(address, resultCallback)
    }

    fun getClosestReportToDeviceLocation(resultIdCallback: (Int) -> Unit){
        LocationService.getLocation { currentLocation ->
            ReportRepository.getClosestReportIdToLocation(currentLocation[0], currentLocation[1]) { reportId ->
                if(reportId == 0){
                    return@getClosestReportIdToLocation
                }
                resultIdCallback(reportId)
            }
        }
    }

    fun getClosestReportIdToAddress(address: String, resultIdCallback: (Int) -> Unit){
        getLocationFromAddress(address) { lat, long ->
            ReportRepository.getClosestReportIdToLocation(lat, long) { reportId ->
                if(reportId == 0){
                    return@getClosestReportIdToLocation
                }
                resultIdCallback(reportId)
            }
        }
    }

}