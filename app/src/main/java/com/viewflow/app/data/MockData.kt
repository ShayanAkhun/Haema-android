package com.viewflow.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object MockData {

    var currentDonor by mutableStateOf(
        Donor(
            id = "DNR-2049",
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
            postedMinutesAgo = 12,
            expiresInMinutes = 45,
            requestedBy = "Imran Noor (brother)",
            note = "Post-surgery bleeding, ICU. Please come to the blood bank on the 2nd floor.",
            respondents = 2,
            latitude = 31.5325,
            longitude = 74.3400
        ),
        BloodRequest(
            id = "r4",
            patientName = "Hassan Raza",
            hospital = "Jinnah Hospital",
            bloodGroup = BloodGroup.A_POS,
            unitsNeeded = 4,
            urgency = Urgency.CRITICAL,
            postedMinutesAgo = 3,
            expiresInMinutes = 30,
            requestedBy = "Dr. Ayesha (attending)",
            note = "Road accident, active transfusion. Urgent.",
            respondents = 1,
            latitude = 31.5100,
            longitude = 74.3600
        ),
        BloodRequest(
            id = "r2",
            patientName = "Bilal Ahmed",
            hospital = "Mayo Hospital",
            bloodGroup = BloodGroup.B_POS,
            unitsNeeded = 2,
            urgency = Urgency.MEDIUM,
            postedMinutesAgo = 55,
            expiresInMinutes = 180,
            requestedBy = "Sana Ahmed (wife)",
            note = "Scheduled surgery tomorrow morning, building up reserves.",
            respondents = 3,
            latitude = 31.5450,
            longitude = 74.3500
        ),
        BloodRequest(
            id = "r5",
            patientName = "Ali Kamran",
            hospital = "Shalamar Hospital",
            bloodGroup = BloodGroup.O_POS,
            unitsNeeded = 2,
            urgency = Urgency.MEDIUM,
            postedMinutesAgo = 90,
            expiresInMinutes = 240,
            requestedBy = "Kamran Sheikh (father)",
            respondents = 0,
            latitude = 31.4950,
            longitude = 74.3300
        ),
        BloodRequest(
            id = "r3",
            patientName = "Zainab Malik",
            hospital = "Services Institute of Medicine",
            bloodGroup = BloodGroup.AB_POS,
            unitsNeeded = 1,
            urgency = Urgency.LOW,
            postedMinutesAgo = 130,
            expiresInMinutes = 360,
            requestedBy = "Zainab Malik (self)",
            note = "Thalassemia, routine monthly transfusion.",
            respondents = 5,
            latitude = 31.5200,
            longitude = 74.3800
        )
    )

    /** Roughly the centre of the nearby-donors map (Lahore). */
    const val cityLatitude = 31.5204
    const val cityLongitude = 74.3587

    val nearbyDonors = listOf(
        NearbyDonor(
            id = "d1",
            name = "Maria Garcia",
            bloodGroup = BloodGroup.A_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=5",
            verified = true,
            lastDonatedAgo = "3 months ago",
            eligible = true,
            phone = "+92 300 1112222",
            distanceKm = 1.2,
            latitude = 31.5290,
            longitude = 74.3450
        ),
        NearbyDonor(
            id = "d2",
            name = "Nadia Khan",
            bloodGroup = BloodGroup.O_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=8",
            verified = true,
            lastDonatedAgo = "2 months ago",
            eligible = true,
            phone = "+92 301 3334444",
            distanceKm = 2.0,
            latitude = 31.5075,
            longitude = 74.3720
        ),
        NearbyDonor(
            id = "d3",
            name = "Ghum Ashe",
            bloodGroup = BloodGroup.O_NEG,
            avatarUrl = "https://i.pravatar.cc/150?img=15",
            verified = true,
            lastDonatedAgo = "9 months ago",
            eligible = true,
            phone = "+92 302 5556666",
            distanceKm = 3.4,
            latitude = 31.5410,
            longitude = 74.3310
        ),
        NearbyDonor(
            id = "d4",
            name = "Usman Tariq",
            bloodGroup = BloodGroup.B_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=22",
            verified = false,
            lastDonatedAgo = "5 weeks ago",
            eligible = false,
            phone = "+92 303 7778888",
            distanceKm = 4.1,
            latitude = 31.4880,
            longitude = 74.3400
        ),
        NearbyDonor(
            id = "d5",
            name = "Hina Aslam",
            bloodGroup = BloodGroup.AB_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=33",
            verified = true,
            lastDonatedAgo = "6 months ago",
            eligible = true,
            phone = "+92 304 9990000",
            distanceKm = 5.6,
            latitude = 31.5560,
            longitude = 74.3900
        ),
        NearbyDonor(
            id = "d6",
            name = "Bilal Chaudhry",
            bloodGroup = BloodGroup.O_POS,
            avatarUrl = "https://i.pravatar.cc/150?img=51",
            verified = true,
            lastDonatedAgo = "4 months ago",
            eligible = true,
            phone = "+92 305 2223333",
            distanceKm = 2.7,
            latitude = 31.5015,
            longitude = 74.3150
        ),
        NearbyDonor(
            id = "d7",
            name = "Sara Iqbal",
            bloodGroup = BloodGroup.A_NEG,
            avatarUrl = "https://i.pravatar.cc/150?img=45",
            verified = false,
            lastDonatedAgo = "7 weeks ago",
            eligible = true,
            phone = "+92 306 4445555",
            distanceKm = 6.3,
            latitude = 31.4740,
            longitude = 74.3850
        ),
        NearbyDonor(
            id = "d8",
            name = "Kamran Malik",
            bloodGroup = BloodGroup.B_NEG,
            avatarUrl = "https://i.pravatar.cc/150?img=60",
            verified = true,
            lastDonatedAgo = "5 months ago",
            eligible = true,
            phone = "+92 307 6667777",
            distanceKm = 3.9,
            latitude = 31.5480,
            longitude = 74.3600
        )
    )

    val donationHistory = listOf(
        DonationRecord("h1", "Jun 14, 2026", "City Central Blood Bank", 1),
        DonationRecord("h2", "Feb 02, 2026", "Al-Shifa General Hospital", 1),
        DonationRecord("h3", "Oct 19, 2025", "University Health Camp", 1),
        DonationRecord("h4", "Jun 05, 2025", "City Central Blood Bank", 1),
        DonationRecord("h5", "Jan 22, 2025", "Mayo Hospital", 1)
    )

    val faqs = listOf(
        FaqItem(
            question = "Who is eligible to donate blood?",
            answer = "Most healthy adults aged 18-65 weighing at least 50kg can donate. You should be well-rested, " +
                "not have donated in the last 3 months, and be free of fever, infection, or recent tattoos/piercings."
        ),
        FaqItem(
            question = "How often can I donate blood?",
            answer = "Whole blood donors can typically donate every 3 months (12 weeks). The app tracks your next " +
                "eligible date on your profile so you'll always know when you're cleared to donate again."
        ),
        FaqItem(
            question = "How do I respond to an emergency blood request?",
            answer = "Open the Emergency tab, find a request matching your blood group, and tap it to see the " +
                "patient's hospital and contact details. Tap \"I can help\" to notify the requester that you're on the way."
        ),
        FaqItem(
            question = "Is my personal information kept private?",
            answer = "Yes. Your contact details are only shared with a requester once you choose to respond to their " +
                "request. You can manage what's visible to other donors from Profile > Privacy & security."
        ),
        FaqItem(
            question = "How do I register for a blood camp?",
            answer = "Go to the Find tab, open any camp's details, and tap \"Register to donate\". You'll get a " +
                "reminder before the camp date."
        ),
        FaqItem(
            question = "What should I do before and after donating?",
            answer = "Eat a good meal and drink plenty of water before donating. Afterwards, rest for a few minutes, " +
                "avoid heavy lifting or strenuous exercise for 24 hours, and keep the bandage on for a few hours."
        ),
        FaqItem(
            question = "How do I update my blood group or contact details?",
            answer = "Go to Profile > Edit profile to update your name, contact details, city, and blood group at any time."
        ),
        FaqItem(
            question = "How can I contact support?",
            answer = "Use the contact options on the Help & support screen to call, email, or chat with our support team."
        )
    )

    fun campById(id: String): BloodCamp? = camps.find { it.id == id }
}
