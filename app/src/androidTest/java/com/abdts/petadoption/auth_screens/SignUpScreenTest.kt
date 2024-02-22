package com.abdts.petadoption.auth_screens

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abdts.petadoption.MainActivity
import com.abdts.petadoption.navigation.AppNavigation
import com.auth.domain.di.DomainLayerModule
import com.auth.ui.screens.destinations.SignUpScreenDestination
import com.auth.ui.ui.theme.PetAdoptionTheme
import com.core.common.test_tags.AuthTestTags
import com.core.common.test_tags.HomeTestTags
import com.ramcosta.composedestinations.navigation.navigate
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
class SignUpScreenTest {

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
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        ""
                    )
                    navController.navigate(SignUpScreenDestination)
                }
            }
        }
    }

    @Test
    fun testErrorMessageDisplayed() {
        //make sure if the error message template exist
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_ERROR_MESSAGE).assertExists()
        //check if there is no error message yet
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertDoesNotExist()

        //insert invalid email to display the error message
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_USERNAME_INPUT).performTextInput("azs")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_FULLNAME_INPUT)
            .performTextInput("abo adm")
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_COUNTRY_INPUT).performTextInput("turkey")
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_CONTACT_NUMBER_INPUT)
            .performTextInput("+905567525888")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_ADDRESS_INPUT)
            .performTextInput("Istanbul Uskudar")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
            .performTextInput("adam.com@gmail")
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT)
            .performTextInput("12345678aA&")
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
            .performTextInput("12345678aA&")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGN_UP_SCREEN).performScrollToNode(hasTestTag(
            AuthTestTags.AUTH_ERROR_MESSAGE))
        //check if the error message appears
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertExists()

        //updating to valid input
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
            .performTextInput("adam@gmail.com")
        //check if the error message disappears
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertDoesNotExist()
    }

    @Test
    fun testButtonEnableBeforeAndAfterTextInput() {
        //check if the button is not enabled
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_CONFIRMATION_BUTTON).assertIsNotEnabled()
        //insert inputs
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_USERNAME_INPUT)
            .performTextInput("adam")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_FULLNAME_INPUT)
            .performTextInput("abo adam")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_COUNTRY_INPUT).performTextInput("turkey")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_CONTACT_NUMBER_INPUT)
            .performTextInput("+905567525888")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_ADDRESS_INPUT)
            .performTextInput("Istanbul Uskudar")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
            .performTextInput("adam@gmail.com")

        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT)
            .performTextInput("12345678aA&")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
            .performTextInput("12345678aA&")

        //check if the button is enabled
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_CONFIRMATION_BUTTON).assertIsEnabled()
    }

    @Test
    fun testPasswordVisibility() {
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT)
            .performTextInput("12345678aA&")
        //check if the password is hidden
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT).assert(hasText("•••••••••••"))
        //click on the show password toggle
        composeTestRule.onNodeWithTag(AuthTestTags.PASSWORD_VISIBILITY_TOGGLE).performClick()
        //check if the password is appears
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT).assert(hasText("12345678aA&"))
        //hide the password again..
        composeTestRule.onNodeWithTag(AuthTestTags.PASSWORD_VISIBILITY_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT).assert(hasText("•••••••••••"))
    }

    @Test
    fun testSuccessfulSignUpNavigatesToHomeScreen() {

        //inserting new user
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_USERNAME_INPUT)
            .performTextInput("adam")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_FULLNAME_INPUT)
            .performTextInput("abo adam")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_COUNTRY_INPUT).performTextInput("turkey")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_CONTACT_NUMBER_INPUT)
            .performTextInput("+905567525888")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_ADDRESS_INPUT)
            .performTextInput("Istanbul Uskudar")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
            .performTextInput("adam@gmail.com")

        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT)
            .performTextInput("12345678aA&")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
            .performTextInput("12345678aA&")

        Espresso.pressBack()
        //click on the login button
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_CONFIRMATION_BUTTON).performClick()
        composeTestRule.onNodeWithTag(HomeTestTags.HOME_SCREEN).assertExists()
    }

    @Test
    fun testErrorSignUpDoesNotNavigate() {
        //inserting registered user
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_USERNAME_INPUT)
            .performTextInput("az_bod")
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_FULLNAME_INPUT).performTextInput("abz")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_COUNTRY_INPUT).performTextInput("turkey")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_CONTACT_NUMBER_INPUT)
            .performTextInput("+905567525888")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_ADDRESS_INPUT)
            .performTextInput("Istanbul Uskudar")

        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
            .performTextInput("az@gamil.com")

        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_PASSWORD_INPUT)
            .performTextInput("12345678aA&")
        composeTestRule.onNodeWithTag(AuthTestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
            .performTextInput("12345678aA&")

        Espresso.pressBack()
        //click on the login button
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_CONFIRMATION_BUTTON).performClick()

        //check if the error message appears
        composeTestRule.onNodeWithTag(HomeTestTags.HOME_SCREEN).assertDoesNotExist()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag(AuthTestTags.AUTH_ERROR_MESSAGE + "error_message_text")
            .assertExists()
    }
}