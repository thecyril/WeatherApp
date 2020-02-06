package com.example.meteochallenge

import android.util.Log
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeInteractorUnitTest {

    @Test
    fun fetchHomeData_with_validInput_shouldCall_presentHomeData() {
        // Given
        val interactor = HomeInteractor()
        val request = HomeRequest()
        //homeRequest.isFutureTrips = true
        val presenterInputSpy = HomePresenterInputSpy()
        interactor.output = presenterInputSpy
        // When
        interactor.fetchHomeData(request)

        // Then
        Assert.assertTrue(
            "When the valid input is passed to HomeInteractor "
                    + "Then presentHomeData should be called",
            presenterInputSpy.presentHomeDataIsCalled
        )
    }

    private inner class HomePresenterInputSpy : HomePresenterInput {

        internal var presentHomeDataIsCalled = false
        internal var responseCopy: HomeResponse? = null
        override fun presentHomeData(response: HomeResponse) {
            presentHomeDataIsCalled = true
            responseCopy = response
        }
    }


}
