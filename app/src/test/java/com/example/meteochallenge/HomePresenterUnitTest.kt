package com.example.meteochallenge

import android.util.Log
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class HomePresenterUnitTest {

    @Test
    fun presentHomeData_with_validInput_shouldCall_displayHomeData() {
        // Given
        val presenter = HomePresenter()
        val response = HomeResponse()
        // Set up the Spy or Mocks
        // response.listOfFlights = FlightWorker().futureFlights


        // When
        presenter.presentHomeData(response)

        // Then
        // Assert.assertTrue("When the valid input is passed to presenter" +
        //        "Then displayHomeData should be called",
        //         homeFragmentInputSpy.isDisplayHomeMetaDataCalled)
    }

    companion object {
        const val TAG = "HomePresenterUnitTest"
    }
}
