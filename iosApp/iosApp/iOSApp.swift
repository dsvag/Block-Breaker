import SwiftUI
import Shared
#if canImport(FirebaseCore) && canImport(FirebaseCrashlytics)
import FirebaseCore
import FirebaseCrashlytics
#endif

@main
struct iOSApp: App {
    init() {
        #if canImport(FirebaseCore) && canImport(FirebaseCrashlytics)
        if Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil {
            FirebaseApp.configure()
            MainViewControllerKt.registerCrashReporter(crashReporter: IOSCrashReporter())
        }
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

#if canImport(FirebaseCore) && canImport(FirebaseCrashlytics)
private final class IOSCrashReporter: CrashReporter {
    func recordException(throwable: KotlinThrowable) {
        let message = throwable.message ?? String(describing: throwable)
        let error = NSError(
            domain: "dev.vpestryakov.block_breaker",
            code: 0,
            userInfo: [NSLocalizedDescriptionKey: message]
        )
        Crashlytics.crashlytics().record(error: error)
    }

    func log(message: String) {
        Crashlytics.crashlytics().log(message)
    }

    func setCustomKey(key: String, value: String) {
        Crashlytics.crashlytics().setCustomValue(value, forKey: key)
    }
}
#endif
