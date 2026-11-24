package com.example.cs412androidapp

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern

@RunWith(AndroidJUnit4::class)
class CS412ComposeUiAutomatorTest {

    private lateinit var device: UiDevice
    private lateinit var appPackageName: String

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        device = UiDevice.getInstance(instrumentation)
        val context = ApplicationProvider.getApplicationContext<Context>()
        appPackageName = context.packageName

        device.pressHome()
        device.waitForIdle()
    }

    @Test
    fun launchFromLauncher_clickExplicitButton_andVerifyComposeChallenge() {
        // Step 1 & 2 follow below
    }

    private fun getLauncherLabel(): String {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(appPackageName)
        }
        return context.packageManager
            .resolveActivity(launcherIntent, 0)
            ?.loadLabel(context.packageManager)
            ?.toString()
            ?: "CS412AndroidApp"
    }

    private fun findLauncherIcon(appLabel: String): UiObject2? {
        device.findObject(By.text(appLabel))?.let { return it }

        val width = device.displayWidth
        val height = device.displayHeight
        device.swipe(width / 2, (height * 0.85).toInt(), width / 2, (height * 0.2).toInt(), 15)
        return device.wait(Until.findObject(By.text(appLabel)), 3_000)
    }
}