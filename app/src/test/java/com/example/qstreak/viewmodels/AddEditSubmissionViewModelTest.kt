package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.qstreak.CoroutineTestRule
import com.example.qstreak.TestFixtures.activities1
import com.example.qstreak.TestFixtures.date1
import com.example.qstreak.TestFixtures.date2
import com.example.qstreak.TestFixtures.dateWithNoSubmission
import com.example.qstreak.TestFixtures.submissionWithActivities1
import com.example.qstreak.TestFixtures.submissionWithActivities2
import com.example.qstreak.TestFixtures.uid
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.utils.UID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditSubmissionViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    // Dependencies
    private val submissionRepository = mockk<SubmissionRepository>()
    private val activitiesRepository = mockk<ActivitiesRepository>()
    private val sharedPreferences = mockk<SharedPreferences>()

    // LiveData objects
    private val repoActivities = MutableLiveData<List<Activity>>()

    private lateinit var sut: AddEditSubmissionViewModel

    @Before
    fun setup() {
        setUid(uid)

        every { activitiesRepository.activities } returns repoActivities as LiveData<List<Activity>>
        coEvery { submissionRepository.getSubmissionWithActivitiesByDate(date1) } returns submissionWithActivities1
        coEvery { submissionRepository.getSubmissionWithActivitiesByDate(date2) } returns submissionWithActivities2
        coEvery { submissionRepository.getSubmissionWithActivitiesByDate(dateWithNoSubmission) } returns null

        sut = AddEditSubmissionViewModel(
            submissionRepository,
            activitiesRepository,
            sharedPreferences
        )
    }

    /**
     * Formatting of dates tested in [DateUtilsTest.kt]
     */
    @Test
    fun onInit_setsDate() {
        assertNotNull(sut.selectedDateString.value)
    }

    @Test
    fun onInit_setsDateDisplayString() {
        assertNotNull(sut.selectedDateDisplayString.value)
    }

    @Test
    fun onInit_setsEmptyCheckedActivityList() {
        assertThat(sut.checkedActivities.value, `is`(emptyList()))
    }

    @Test
    fun onInit_setsSubmissionCompleteToFalse() {
        assertFalse(sut.submissionComplete.value!!)
    }

    @Test
    fun onNewActivitiesFromRepo_setsActivityList() {
        repoActivities.value = activities1

        assertEquals(sut.activities.value, activities1)
    }

    @Test
    fun refreshActivities_givenNonNullUid_refreshesActivities() {
        sut.refreshActivities()
        coVerify { activitiesRepository.refreshActivities(uid) }
    }

    @Test
    fun refreshActivities_givenNullUid_doesNotRefreshActivities() {
        setUid(null)

        sut.refreshActivities()
        coVerify(exactly = 0) { activitiesRepository.refreshActivities(any()) }
    }

    @Test
    fun onLoadDate_givenExistingSubmission_loadsRecord() {
        sut.loadDate(date1)

        coVerify { submissionRepository.getSubmissionWithActivitiesByDate(date1) }
        assertThat(sut.existingSubmission.value?.submission?.date, `is`(date1))
        assertEquals(sut.selectedDateString.value, date1)
        assertEquals(sut.contactCount.value, "1")
        assertEquals(sut.checkedActivities.value, activities1)
    }

    @Test
    fun onLoadDate_givenNullSubmission_setsEmptyRecord() {
        sut.loadDate(dateWithNoSubmission)

        coVerify { submissionRepository.getSubmissionWithActivitiesByDate(dateWithNoSubmission) }
        assertNull(sut.existingSubmission.value)
        assertEquals(sut.contactCount.value, "0")
        assertEquals(sut.checkedActivities.value, emptyList<Activity>())
    }

    fun onIncrementContactCount_contactCountIncrements() {
        // TODO
    }

    fun onDecrementContactCount_contactCountDecrements() {
        // TODO
    }

    fun onSaveSubmission_givenExistingSubmission_updatesWithValues() {
        // TODO
    }

    fun onSaveSubmission_givenNewSubmission_insertsWithValues() {
        // TODO
    }

    fun onActivityCheckboxToggled_givenNotChecked_addsToList() {
        // TODO
    }

    fun onActivityCheckboxToggled_givenChecked_removesFromList() {
        // TODO
    }

    private fun setUid(uid: String?) {
        every { sharedPreferences.getString(UID, null) } returns uid
    }
}
