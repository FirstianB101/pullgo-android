package com.ich.pullgo.presentation.sign_up

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.TestTags
import com.ich.pullgo.data.repository.FakeUiSignUpRepository
import com.ich.pullgo.di.*
import com.ich.pullgo.domain.repository.SignUpRepository
import com.ich.pullgo.domain.use_case.sign_up.CheckIdExistUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateStudentUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateTeacherUseCase
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import com.ich.pullgo.presentation.login.LoginActivity
import com.ich.pullgo.presentation.sign_up.components.*
import com.ich.pullgo.presentation.sign_up.util.SignUpScreen
import com.ich.pullgo.presentation.theme.PullgoTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SignUpTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Inject
    @TestUseCases
    lateinit var useCases: SignUpUseCases

    lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp(){
        hiltRule.inject()
        viewModel = SignUpViewModel(useCases)
        composeRule.setContent {
            val navController = rememberNavController()
            PullgoTheme {
                NavHost(
                    navController = navController,
                    startDestination = SignUpScreen.SignUpMainScreen.route
                ){
                    composable(route = SignUpScreen.SignUpMainScreen.route){
                        SignUpMainScreen(navController = navController)
                    }
                    composable(route = SignUpScreen.StudentIdScreen.route){
                        StudentSignUpIdScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(
                        route = SignUpScreen.StudentPwScreen.route + "/{username}",
                        arguments = listOf(navArgument("username"){ type = NavType.StringType })
                    ){
                        StudentSignUpPwScreen(
                            navController = navController,
                            username = it.arguments?.getString("username")!!
                        )
                    }
                    composable(
                        route = SignUpScreen.StudentInfoScreen.route + "?username={username}&password={password}",
                        arguments = listOf(
                            navArgument("username"){ type = NavType.StringType },
                            navArgument("password"){ type = NavType.StringType }
                        )
                    ){
                        StudentSignUpInfoScreen(
                            username = it.arguments?.getString("username")!!,
                            password = it.arguments?.getString("password")!!,
                            viewModel = viewModel
                        )
                    }
                    composable(route = SignUpScreen.TeacherIdScreen.route){
                        TeacherSignUpIdScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(
                        route = SignUpScreen.TeacherPwScreen.route + "/{username}",
                        arguments = listOf(navArgument("username"){ type = NavType.StringType })
                    ){
                        TeacherSignUpPwScreen(
                            navController = navController,
                            username = it.arguments?.getString("username")!!
                        )
                    }
                    composable(
                        route = SignUpScreen.TeacherInfoScreen.route + "?username={username}&password={password}",
                        arguments = listOf(
                            navArgument("username"){ type = NavType.StringType },
                            navArgument("password"){ type = NavType.StringType }
                        )
                    ){
                        TeacherSignUpInfoScreen(
                            username = it.arguments?.getString("username")!!,
                            password = it.arguments?.getString("password")!!,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createStudentTest() {
        // click student sign up button
        composeRule.onNodeWithTag(TestTags.STUDENT_SIGNUP).performClick()

        // input too short id
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("a")

        // check error text showing
        composeRule.onNodeWithText(Constants.ID_TOO_SHORT_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input too long id
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("aaaaaaaaaaaaaaaaaaaaaaaa")

        composeRule.onNodeWithText(Constants.ID_TOO_LONG_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input wrong char
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("aa!")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithText(Constants.ID_WRONG_CHAR_ERROR).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input start with underline
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("_")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithText(Constants.FIRST_WORD_FORMAT_ERROR).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input id which is already exist, and click check id button
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("exist_student")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        // assert next button is not exist because id was overlapped
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).assertDoesNotExist()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input id which isn't already exist, and click check id button
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("new_student")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        // assert next button is exist
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).performClick()


        // input too short password
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("aa")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        // assert too short message exist
        composeRule.onAllNodes(hasText(Constants.PW_TOO_SHORT_ERROR)).assertCountEquals(2)

        // input too long password
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("aaaaaaaaaaaaaaaaaa")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        // assert too long message exist
        composeRule.onNodeWithText(Constants.PW_TOO_LONG_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextClearance()

        // input password with no error, but check failed
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("12345678")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextInput("12345670")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        composeRule.onAllNodes(hasText(Constants.PW_CHECK_DIFFERENT_ERROR)).assertCountEquals(2)

        // input password and check with no error
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextInput("12345678")

        // check good format text exist
        composeRule.onAllNodes(hasText(Constants.PW_FORMAT_GOOD)).assertCountEquals(2)

        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_NAME).performTextInput("student_name")
        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_PHONE).performTextInput("01012341234")
        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_PHONE_VERIFY).performTextInput("1234")
        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_PARENT_PHONE).performTextInput("01043214321")
        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_SCHOOL).performTextInput("school")

        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_SUCCESS_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_STUDENT_SUCCESS_DIALOG).assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createTeacherTest() {
        // click teacher sign up button
        composeRule.onNodeWithTag(TestTags.TEACHER_SIGNUP).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).assertExists()

        // input too short id
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("a")

        // check error text showing
        composeRule.onNodeWithText(Constants.ID_TOO_SHORT_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input too long id
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("aaaaaaaaaaaaaaaaaaaaaaaa")

        composeRule.onNodeWithText(Constants.ID_TOO_LONG_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input wrong char
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("aa!")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithText(Constants.ID_WRONG_CHAR_ERROR).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input start with underline
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("_")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        composeRule.onNodeWithText(Constants.FIRST_WORD_FORMAT_ERROR).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input id which is already exist, and click check id button
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("exist_teacher")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        // assert next button is not exist because id was overlapped
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).assertDoesNotExist()

        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextClearance()

        // input id which isn't already exist, and click check id button
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_TEXT_FIELD).performTextInput("new_teacher")
        composeRule.onNodeWithTag(TestTags.ID_CHECK_BUTTON).performClick()

        // assert next button is exist
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).assertExists()
        composeRule.onNodeWithTag(TestTags.SIGNUP_ID_NEXT_BUTTON).performClick()


        // input too short password
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("aa")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        // assert too short message exist
        composeRule.onAllNodes(hasText(Constants.PW_TOO_SHORT_ERROR)).assertCountEquals(2)

        // input too long password
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("aaaaaaaaaaaaaaaaaa")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        // assert too long message exist
        composeRule.onNodeWithText(Constants.PW_TOO_LONG_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextClearance()

        // input password with no error, but check failed
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_TEXT_FIELD).performTextInput("12345678")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextInput("12345670")
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        composeRule.onAllNodes(hasText(Constants.PW_CHECK_DIFFERENT_ERROR)).assertCountEquals(2)

        // input password and check with no error
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD).performTextInput("12345678")

        // check good format text exist
        composeRule.onAllNodes(hasText(Constants.PW_FORMAT_GOOD)).assertCountEquals(2)

        composeRule.onNodeWithTag(TestTags.SIGNUP_PW_NEXT_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_TEACHER_NAME).performTextInput("teacher_name")
        composeRule.onNodeWithTag(TestTags.SIGNUP_TEACHER_PHONE).performTextInput("01012341234")
        composeRule.onNodeWithTag(TestTags.SIGNUP_TEACHER_PHONE_VERIFY).performTextInput("1234")

        composeRule.onNodeWithTag(TestTags.SIGNUP_TEACHER_SUCCESS_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.SIGNUP_TEACHER_SUCCESS_DIALOG).assertIsDisplayed()
    }
}