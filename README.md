# Haema 🩸

**A free, open-source blood donation app that connects voluntary donors with patients who need blood — built for the community, by the community.**

In many regions, including Gilgit-Baltistan in northern Pakistan, finding a matching blood donor in an emergency still depends on phone calls, WhatsApp groups and word of mouth. Hospitals often have limited blood bank stock, and precious time is lost searching. Haema makes that search faster: people who need blood can find nearby donors with the right blood group, and donors can make themselves available with a few taps.

Haema is non-commercial and open source. Anyone can use it, adapt it for their own community, or contribute to it.

---
🤝 Open source, made for the community

Haema is a free, non-commercial, open-source project made for the community. It was not built to make money. It was built because people in our community needed a faster, more reliable way to find blood donors when lives are at stake.

Free forever – no fees, no ads, no premium features. Everyone gets the full app.
Open source – the complete source code is public, so anyone can see how it works, check how data is handled, and learn from it.
Community-owned – volunteers, students and developers are invited to improve it, report problems and suggest features.
Built to be shared – any city, region, hospital or organisation can fork Haema and adapt it to their own needs and language.

If you believe in what Haema does, you can help by using it, sharing it, registering as a donor, or contributing code, translations or ideas.

## Table of Contents

- [Who is it for?](#who-is-it-for)
- [Features](#features)
- [Tech stack](#tech-stack)
- [Project structure](#project-structure)
- [Setting up the Android development environment](#setting-up-the-android-development-environment)
- [Getting the project and running it](#getting-the-project-and-running-it)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [Privacy](#privacy)
- [License](#license)
- [Contact](#contact)

---

## Who is it for?

| User | How Haema helps |
|---|---|
| **Patients and their families** | Quickly find willing donors with a matching blood group in their area, instead of searching through calls and social media. |
| **Voluntary blood donors** | Register once, set their blood group and availability, and get contacted when someone nearby needs help. |
| **Hospitals, clinics and blood banks** | Reach donors faster during shortages and emergencies. |
| **Community organisations, student societies and volunteers** | Organise donation drives and maintain a reliable, searchable donor network. |
| **Developers** | Fork and adapt the app for another city, region or language. |

---

## Features

- **Donor registration** – sign up with name, blood group, location and contact details.
- **Search donors** – filter by blood group and city/area to find matching donors.
- **Blood requests** – post an urgent request with blood group, hospital, location and number of units needed.
- **Availability toggle** – donors can mark themselves available or unavailable at any time.
- **Last donation tracking** – records the donor's last donation date so donors aren't contacted before they are eligible to donate again.
- **Direct contact** – call or message a donor directly from the app.
- **Blood group compatibility** – shows which donor groups are compatible with the patient's blood group.
- **User profile** – donors can view and update their details.


---

## Tech stack

> ✏️ KOTLIN and JETPACK Compose

| Layer | Technology |
|---|---|
| Language | Kotlin  |
| UI | XML layouts / Jetpack Compose |
| Build system | Gradle (Kotlin DSL) |
| Backend / database | Firebase Authentication + Cloud Firestore + Php|
| Minimum Android version | Android 7.0 (API 24) |
| Target SDK | API 34 |

---

## Project structure

```
haema/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../haema/      # Kotlin/Java source code (activities, fragments, view models, data layer)
│   │   │   ├── res/                 # Layouts, drawables, strings, colours
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Unit tests
│   ├── build.gradle.kts             # App module build configuration
│   └── google-services.json         # Firebase config (NOT committed — see setup)
├── gradle/
├── build.gradle.kts                 # Project-level build configuration
├── settings.gradle.kts
└── README.md
```

---

## Setting up the Android development environment

These steps work on **Windows, macOS and Linux**.

### 1. Install Android Studio (the IDE)

Android Studio is the official IDE for Android development. It includes the Android SDK, an emulator and the Gradle build tools.

1. Download Android Studio from **https://developer.android.com/studio**.
2. Run the installer and keep the default options. Make sure these are selected:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device (emulator)
3. Open Android Studio. The **Setup Wizard** will download the latest SDK and tools — choose **Standard** installation.

> Android Studio comes with its own Java Development Kit (JDK), so you do **not** need to install Java separately.

**Recommended hardware:** 8 GB RAM or more (16 GB is better for running the emulator), about 10 GB of free disk space.

### 2. Install the required SDK version

1. In Android Studio, open **Settings / Preferences → Languages & Frameworks → Android SDK**  
   *(or click **More Actions → SDK Manager** on the welcome screen).*
2. On the **SDK Platforms** tab, tick the Android version matching the project's `compileSdk` (see the Tech stack table).
3. On the **SDK Tools** tab, make sure these are ticked:
   - Android SDK Build-Tools
   - Android Emulator
   - Android SDK Platform-Tools
4. Click **Apply** and wait for the download to finish.

### 3. Set up a device to run the app

You can run Haema on an **emulator** (virtual phone) or a **real Android phone**.

**Option A – Emulator**

1. Open **Tools → Device Manager**.
2. Click **Create Device** (➕), choose a phone such as *Pixel 7*, and click **Next**.
3. Select a system image (the recommended one is fine), download it, and click **Finish**.
4. Press ▶ next to the device to start it.

> On Windows, if the emulator is slow or won't start, enable **virtualization (VT-x / AMD-V)** in your BIOS settings.

**Option B – Real Android phone**

1. On the phone, go to **Settings → About phone** and tap **Build number** 7 times to enable Developer Options.
2. Go to **Settings → System → Developer options** and turn on **USB debugging**.
3. Connect the phone to your computer with a USB cable and accept the "Allow USB debugging?" prompt on the phone.
4. The phone will appear in the device list at the top of Android Studio.

---

## Getting the project and running it

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/haema.git
cd haema
```

Or, in Android Studio: **File → New → Project from Version Control**, paste the repository URL, and click **Clone**.

### 2. Open the project

1. In Android Studio, choose **File → Open** and select the `haema` folder.
2. Wait for **Gradle sync** to finish (progress is shown at the bottom of the window). The first sync downloads dependencies and can take a few minutes.

For security, the Firebase configuration file is not included in the repository. To run the app you need your own:

1. Go to the **[Firebase Console](https://console.firebase.google.com/)** and create a new project.
2. Click **Add app → Android** and enter the package name (found as `applicationId` in `app/build.gradle.kts`).
3. Download the `google-services.json` file and place it in the **`app/`** folder.
4. In the Firebase Console, enable:
   - **Authentication** → Email/Password *(and/or Phone)*
   - **Cloud Firestore** → create a database (start in test mode for local development)
5. Sync Gradle again (**File → Sync Project with Gradle Files**).

### 4. Run the app

1. Select your emulator or connected phone from the device dropdown in the toolbar.
2. Click the green **Run ▶** button (or press `Shift + F10` on Windows/Linux, `Ctrl + R` on macOS).
3. Android Studio builds the app, installs it on the device and opens it.

### 5. Build an APK (optional)

To create an installable APK file:

```bash
./gradlew assembleDebug        # macOS / Linux
gradlew.bat assembleDebug      # Windows
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

You can also use **Build → Build App Bundle(s) / APK(s) → Build APK(s)** in Android Studio.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| Gradle sync fails | Check your internet connection, then **File → Invalidate Caches → Invalidate and Restart**. |
| `google-services.json is missing` | Add your own Firebase config file to the `app/` folder (see step 3). |
| `SDK location not found` | Open the project once in Android Studio — it creates `local.properties` with the SDK path automatically. |
| Emulator won't start or is very slow | Enable hardware virtualization in BIOS, or use a real phone instead. |
| Phone not detected | Re-enable USB debugging, try another cable, and accept the debugging prompt on the phone. |

---

## Contributing

Contributions are welcome — whether it's fixing a bug, improving the design, adding a translation (e.g. Urdu) or suggesting a feature.

1. Fork the repository.
2. Create a branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Add: short description"`
4. Push the branch: `git push origin feature/your-feature-name`
5. Open a **Pull Request** explaining what you changed and why.

Please open an **Issue** first for larger changes so we can discuss them.

---

## Privacy

Haema handles personal information such as names, phone numbers, locations and blood groups. Donor details are only used to connect donors with people who need blood, and are never sold or shared for any other purpose. Donors can update their details or mark themselves unavailable at any time.

If you deploy your own version of Haema, please protect your users' data with proper database security rules and do not commit API keys or configuration files to public repositories.

> ⚠️ Haema helps people find donors. It does not replace medical advice. All blood donations should take place at a hospital or licensed blood bank, where donors are screened and blood is tested.

---

## License

This project is open source under the **Apache 2.0 License** — see the [LICENSE](LICENSE) file for details.


---

## Contact

**Shayan Ghori**  
Developer, Haema  
GitHub: (@ShayanAkhun)(https://github.com/<your-ShayanAkhun>)
Email: <shayanghouri911@gmail.com>

*If Haema helps you or your community, please give the repository a ⭐ and share it with others.*
