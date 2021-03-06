package hu.gyeben.communityparking.server.models.db

import hu.gyeben.communityparking.server.models.api.ApiReport
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Reports : IntIdTable() {
    val reporterEmail = varchar("reporterEmail", 255)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val timestampUTC = varchar("timestampUTC", 255)
    val message = varchar("message", 1000)
    val reservedByEmail = varchar("reservedByEmail", 255)
    val feePerHour = double("feePerHour").nullable()
    val imagePath = varchar("imagePath", 255)
}

class ReportEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReportEntity>(Reports)

    private var _id by Reports.id
    var reporterEmail by Reports.reporterEmail
    var latitude by Reports.latitude
    var longitude by Reports.longitude
    var timestampUTC by Reports.timestampUTC
    var message by Reports.message
    var reservedByEmail by Reports.reservedByEmail
    var feePerHour by Reports.feePerHour
    var imagePath by Reports.imagePath

    fun toReport() =
        Report(reporterEmail, latitude, longitude, timestampUTC, message, reservedByEmail, feePerHour, imagePath)

    fun toApiReport(): ApiReport =
        ApiReport(
            _id.value,
            reporterEmail,
            latitude,
            longitude,
            timestampUTC,
            message,
            reservedByEmail,
            feePerHour,
            imagePath
        )
}

data class Report(
    val reporterEmail: String,
    val latitude: Double,
    val longitude: Double,
    val timestampUTC: String,
    val message: String,
    val reservedByEmail: String,
    val feePerHour: Double?,
    val imagePath: String
)

fun ApiReport.toDbReport(uploadedImagePath: String? = null): Report {
    uploadedImagePath?.let {
        return Report(
            reporterEmail,
            latitude,
            longitude,
            timestampUTC,
            message,
            reservedByEmail,
            feePerHour,
            uploadedImagePath
        )
    }
    return Report(reporterEmail, latitude, longitude, timestampUTC, message, reservedByEmail, feePerHour, imagePath)
}