package com.viewflow.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object MockData {

    var currentDonor by mutableStateOf(
        Donor(
            id = "d0",
            name = "Aarav Sharma",
            bloodGroup = BloodGroup.O_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=12",
            totalDonations = 7,
            livesSaved = 21,
            nextEligibleInDays = 0,
            city = "Lahore",
            email = "aarav.sharma@example.com",
            phone = "+92 300 1234567",
            twoFactorEnabled = false
        )
    )

    val donorAvatars = listOf(
        "https://i.pravatar.cc/150?img=5",
        "https://i.pravatar.cc/150?img=8",
        "https://i.pravatar.cc/150?img=15",
        "https://i.pravatar.cc/150?img=22",
        "https://i.pravatar.cc/150?img=33",
        "https://i.pravatar.cc/150?img=44"
    )

    val camps = listOf(
        BloodCamp(
            id = "c1",
            name = "City Central Blood Drive",
            hospital = "Al-Shifa General Hospital",
            address = "Main Boulevard, Gulberg",
            bannerUrl = "https://picsum.photos/seed/camp1/900/500",
            date = "Aug 24, 2026",
            distanceKm = 1.8,
            unitsCollected = 64,
            unitsGoal = 100,
            registeredDonorAvatars = donorAvatars.take(4)
        ),
        BloodCamp(
            id = "c2",
            name = "University Health Camp",
            hospital = "Services Institute of Medicine",
            address = "Canal Road, Township",
            bannerUrl = "https://picsum.photos/seed/camp2/900/500",
            date = "Aug 27, 2026",
            distanceKm = 4.2,
            unitsCollected = 30,
            unitsGoal = 80,
            registeredDonorAvatars = donorAvatars.take(3)
        ),
        BloodCamp(
            id = "c3",
            name = "Ramadan Relief Drive",
            hospital = "Mayo Hospital",
            address = "Old Anarkali",
            bannerUrl = "https://picsum.photos/seed/camp3/900/500",
            date = "Sep 02, 2026",
            distanceKm = 6.5,
            unitsCollected = 12,
            unitsGoal = 60,
            registeredDonorAvatars = donorAvatars.take(5)
        ),
        BloodCamp(
            id = "c4",
            name = "Corporate Donor Day",
            hospital = "Shaukat Khanum Hospital",
            address = "Johar Town",
            bannerUrl = "https://picsum.photos/seed/camp4/900/500",
            date = "Sep 10, 2026",
            distanceKm = 9.1,
            unitsCollected = 5,
            unitsGoal = 50,
            registeredDonorAvatars = donorAvatars.take(2)
        )
    )

    val requests = listOf(
        BloodRequest(
            id = "r1",
            patientName = "Fatima Noor",
            hospital = "Al-Shifa General Hospital",
            bloodGroup = BloodGroup.O_NEG,
            unitsNeeded = 3,
            urgency = Urgency.CRITICAL,
            postedMinutesAgo = 12
        ),
        BloodRequest(
            id = "r2",
            patientName = "Bilal Ahmed",
            hospital = "Mayo Hospital",
            bloodGroup = BloodGroup.B_POS,
            unitsNeeded = 2,
            urgency = Urgency.MEDIUM,
            postedMinutesAgo = 55
        ),
        BloodRequest(
            id = "r3",
            patientName = "Zainab Malik",
            hospital = "Services Institute of Medicine",
            bloodGroup = BloodGroup.AB_POS,
            unitsNeeded = 1,
            urgency = Urgency.LOW,
            postedMinutesAgo = 130
        )
    )

    val donationHistory = listOf(
        DonationRecord("h1", "Jun 14, 2026", "City Central Blood Bank", 1),
        DonationRecord("h2", "Feb 02, 2026", "Al-Shifa General Hospital", 1),
        DonationRecord("h3", "Oct 19, 2025", "University Health Camp", 1),
        DonationRecord("h4", "Jun 05, 2025", "City Central Blood Bank", 1),
        DonationRecord("h5", "Jan 22, 2025", "Mayo Hospital", 1)
    )

    fun campById(id: String): BloodCamp? = camps.find { it.id == id }
}
