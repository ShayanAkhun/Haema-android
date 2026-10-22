package com.haema.app.data

enum class BloodGroup(val label: String) {
    O_POS("O+"), O_NEG("O-"),
    A_POS("A+"), A_NEG("A-"),
    B_POS("B+"), B_NEG("B-"),
    AB_POS("AB+"), AB_NEG("AB-")
}

enum class Urgency { LOW, MEDIUM, CRITICAL }

data class Donor(
    val id: String,
    val name: String,
    val bloodGroup: BloodGroup,
    val avatarUrl: String,
    val totalDonations: Int,
    val livesSaved: Int,
    val nextEligibleInDays: Int,
    val city: String,
    val email: String,
    val phone: String,
    val twoFactorEnabled: Boolean = false
)

data class BloodCamp(
    val id: String,
    val name: String,
    val hospital: String,
    val address: String,
    val bannerUrl: String,
    val date: String,
    val distanceKm: Double,
    val unitsCollected: Int,
    val unitsGoal: Int,
    val registeredDonorAvatars: List<String>
)

data class BloodRequest(
    val id: String,
    val patientName: String,
    val hospital: String,
    val bloodGroup: BloodGroup,
    val unitsNeeded: Int,
    val urgency: Urgency,
    val postedMinutesAgo: Int,
    /** Minutes left to fulfil this request before it expires. */
    val expiresInMinutes: Int,
    /** Who posted the request (relative, hospital staff, self). */
    val requestedBy: String = "",
    /** Optional free-text note from the requester. */
    val note: String = "",
    /** Donors who have offered to help so far. */
    val respondents: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

/** A donor shown in the "Nearby donors" list / map on the Emergency screen. */
data class NearbyDonor(
    val id: String,
    val name: String,
    val bloodGroup: BloodGroup,
    val avatarUrl: String,
    val verified: Boolean,
    /** How long ago they last donated, e.g. "3 months ago". */
    val lastDonatedAgo: String,
    val eligible: Boolean,
    val phone: String,
    val distanceKm: Double,
    val latitude: Double,
    val longitude: Double
)


data class DonationRecord(
    val id: String,
    val date: String,
    val location: String,
    val units: Int
)

data class FaqItem(
    val question: String,
    val answer: String
)
