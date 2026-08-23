package com.viewflow.app.data

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
    val postedMinutesAgo: Int
)

data class DonationRecord(
    val id: String,
    val date: String,
    val location: String,
    val units: Int
)
