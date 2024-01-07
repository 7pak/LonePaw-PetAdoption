package com.abdts.petadoption.auth_screens

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abdts.petadoption.MainActivity
import com.abdts.petadoption.navigation.AppNavigation
import com.auth.domain.di.DomainLayerModule
import com.auth.ui.ui.theme.PetAdoptionTheme
import com.core.common.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(DomainLayerModule::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            PetAdoptionTheme {
                PetAdoptionTheme {
                    AppNavigation(navController = navController, modifier = Modifier.fillMaxSize(),"")
                }
            }
        }
    }

    @Test
    fun testErrorMessageDisplayed() {
        //make sure if the error message template exist
        composeTestRule.onNodeWithTag(TestTags.AUTH_ERROR_MESSAGE).assertExists()
        //check if there is no error message yet
        composeTestRule.onNodeWithTag(TestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertDoesNotExist()

        //insert invalid username to display the error message
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT).performTextInput("az")
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).performTextInput("12345678a")
        //check if the error message appears
        composeTestRule.onNodeWithTag(TestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertExists()

        //updating to valid input
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT)
            .performTextInput("azb")
        //check if the error message disappears
        composeTestRule.onNodeWithTag(TestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertDoesNotExist()
    }

    @Test
    fun testButtonEnableBeforeAndAfterTextInput() {
        //check if the button is not enabled
        composeTestRule.onNodeWithTag(TestTags.AUTH_CONFIRMATION_BUTTON).assertIsNotEnabled()
        //insert input
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT)
            .performTextInput("adam")
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).performTextInput("12345678a")
        //check if the button is enabled
        composeTestRule.onNodeWithTag(TestTags.AUTH_CONFIRMATION_BUTTON).assertIsEnabled()
    }

    @Test
    fun testPasswordVisibility() {
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).performTextInput("12345678a")
        //check if the password is hidden
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).assert(hasText("•••••••••"))
        //click on the show password toggle
        composeTestRule.onNodeWithTag(TestTags.PASSWORD_VISIBILITY_TOGGLE).performClick()
        //check if the password is appears
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).assert(hasText("12345678a"))
        //hide the password again..
        composeTestRule.onNodeWithTag(TestTags.PASSWORD_VISIBILITY_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).assert(hasText("•••••••••"))
    }

    @Test
    fun testNavigatingToAndFromSignUpScreen() {
        // Check that the login screen is initially displayed
        composeTestRule
            .onNodeWithTag(TestTags.LOGIN_SCREEN)
            .assertExists()

        // Perform click to navigate to the signup screen
        composeTestRule
            .onNodeWithTag(TestTags.NAVIGATION_BETWEEN_LOGIN_SIGNUP)
            .performClick()

        // Check that the signup screen is displayed after the click
        composeTestRule
            .onNodeWithTag(TestTags.SIGN_UP_SCREEN)
            .assertExists()

        // Perform click to navigate back to the login screen
        composeTestRule
            .onNodeWithTag(TestTags.NAVIGATION_BETWEEN_LOGIN_SIGNUP)
            .performClick()

        // Check that the login screen is displayed again
        composeTestRule
            .onNodeWithTag(TestTags.LOGIN_SCREEN)
            .assertExists()
    }

    @Test
    fun testSuccessfulLoginNavigatesToHomeScreen() {

        //inserting correct username and password
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT)
            .performTextInput("az_bod")
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).performTextInput("12345678a")
        Espresso.pressBack()

        //click on the login button
        composeTestRule.onNodeWithTag(TestTags.AUTH_CONFIRMATION_BUTTON).performClick()

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN).assertExists()
    }

    @Test
    fun testErrorLoginDoesNotNavigate() {
        //inserting incorrect username and password
        composeTestRule.onNodeWithTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT)
            .performTextInput("az_bod")
        composeTestRule.onNodeWithTag(TestTags.AUTH_PASSWORD_INPUT).performTextInput("12345678abd")
        Espresso.pressBack()
        //click on the login button
        composeTestRule.onNodeWithTag(TestTags.AUTH_CONFIRMATION_BUTTON).performClick()

        //check if the error message appears
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN).assertDoesNotExist()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(TestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertTextEquals("error while logging in: incorrect username or password")
    }
}