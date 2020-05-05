# Q-Streak Android

This is the Android app for Q-streak. It uses:
* MVVM structure
* Jetpack Architecture components (ViewModel, LiveData, Data Binding, Room)
* Retrofit/OKHttp + Moshi for API service
* Material Design components
* Koin for dependency injection

## About Q-Streak

Q-streak is a Rocket Insights project to encourage low-contact personal habits during the COVID-19 pandemic. Users of the app track their daily interactions and destinations. The app encourages consecutive "streaks" of tracking and gives users a "score" based on the level of risk they assumed on a particular date given their location and reported activities.

## Getting Started

### Clone the repository

Clone the app to your local machine.

### Set up Docker

See [q-streak-web README](https://github.com/rocketinsights/q-streak-web/blob/master/README.md) for further instructions.
`AndroidManifest.xml` is configured with a network security spec to allow emulated Android devices to query the local server in debug builds.
