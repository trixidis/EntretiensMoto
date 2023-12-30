package fr.nextgear.mesentretiensmoto.presentation.manageMaintenancesOfBike

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import fr.nextgear.mesentretiensmoto.model.MaintenanceDomain
import fr.nextgear.mesentretiensmoto.model.Result
import fr.nextgear.mesentretiensmoto.presentation.BaseTest
import fr.nextgear.mesentretiensmoto.use_cases.AddMaintenanceForBikeUseCase
import fr.nextgear.mesentretiensmoto.use_cases.GetMaintenancesForBikeUseCase
import fr.nextgear.mesentretiensmoto.use_cases.RemoveMaintenanceUseCase
import fr.nextgear.mesentretiensmoto.use_cases.UpdateMaintenanceToDoneUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import java.util.Date

class ManageMaintenancesViewModelTest : BaseTest() {

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: ManageMaintenancesViewModel

    private var savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf(Pair("bikeId", "12")))

    @MockK
    private lateinit var getMaintenancesForBikeUseCase: GetMaintenancesForBikeUseCase

    @MockK
    private lateinit var addMaintenanceForBikeUseCase: AddMaintenanceForBikeUseCase

    @MockK
    private lateinit var removeMaintenanceUseCase: RemoveMaintenanceUseCase

    @MockK
    private lateinit var updateMaintenanceToDoneUseCase: UpdateMaintenanceToDoneUseCase

    private val testMaintenance = MaintenanceDomain("test", 127F, true, Date(1703867804))
    private val testException = NullPointerException()

    @Before
    fun setup() {
        viewModel = ManageMaintenancesViewModel(
            savedStateHandle = savedStateHandle,
            getMaintenancesForBikeUseCase = getMaintenancesForBikeUseCase,
            addMaintenanceForBikeUseCase = addMaintenanceForBikeUseCase,
            removeMaintenanceUseCase = removeMaintenanceUseCase,
            updateMaintenanceToDoneUseCase = updateMaintenanceToDoneUseCase
        )

    }

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun `test get maintenances on init`() = runTest {
        coEvery { getMaintenancesForBikeUseCase.invoke(any()) } returns flowOf(Result.Success(listOf(testMaintenance)))
        viewModel.getMaintenances()
        coVerify(exactly = 1) { getMaintenancesForBikeUseCase(any()) }
        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is ManageMaintenancesUiState.GotResults)
            assert((item as ManageMaintenancesUiState.GotResults).results.contains(testMaintenance))
        }
    }

    @Test
    fun `test add maintenance is successful`() = runTest {
        coEvery { addMaintenanceForBikeUseCase(any(), any()) } returns (flowOf(Result.Success(testMaintenance)))
        viewModel.addMaintenance(testMaintenance)
        coVerify(exactly = 1) { addMaintenanceForBikeUseCase(any(), any()) }
    }

    @Test
    fun `test get maintenances failed`() = runTest {
        coEvery { getMaintenancesForBikeUseCase(any()) } returns (flowOf(Result.Failure(testException)))
        viewModel.getMaintenances()
        coVerify(exactly = 1) { getMaintenancesForBikeUseCase(any()) }
        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is ManageMaintenancesUiState.GotError)
            assert((item as ManageMaintenancesUiState.GotError).error == testException)
        }
    }

    @Test
    fun `test add maintenance failed`() = runTest {
        coEvery { addMaintenanceForBikeUseCase(any(), any()) } returns (flowOf(Result.Failure(testException)))
        viewModel.addMaintenance(testMaintenance)
        coVerify(exactly = 1) { addMaintenanceForBikeUseCase(any(), any()) }
        viewModel.uiEvents.test {
            val item = awaitItem()
            assert(item is ManageMaintenancesUiEvents.AddFailed)
        }
    }

}
