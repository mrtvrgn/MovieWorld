package com.mrtvrgn.practice.movieworld

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrtvrgn.practice.movieworld.model.Movie
import com.mrtvrgn.practice.movieworld.model.MovieSearchResponse
import com.mrtvrgn.practice.movieworld.repository.MovieSearchRepository
import com.mrtvrgn.practice.movieworld.screen.search.MovieSearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieSearchViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: MovieSearchRepository

    @Mock
    private lateinit var response: MovieSearchResponse
    private lateinit var viewModel: MovieSearchViewModel

    private val sampleQuery = "Morpheus"
    private val sampleMovie1 = Movie(
        1, "Matrix",
        "Science Fiction Desc",
        8.9, "/somme", "1992-10-10"
    )
    private val sampleMovie2 = Movie(
        3, "Iron Man",
        "Science Fiction Like",
        8.4, "/effect", "1992-10-10"
    )

    @Before
    fun setUp() {
        viewModel = spy(MovieSearchViewModel(repository))
    }


    @Test
    fun `WHEN response is empty VERIFY no result set`() {

        `when`(response.results).thenReturn(emptyList())

        testCoroutineRule.runBlockingTest {
            `when`(repository.searchMovies(query = anyString(), page = Mockito.anyInt()))
                .thenReturn(response)

            viewModel.search(sampleQuery)

            assert(viewModel.results.value.isNullOrEmpty())
        }
    }

    @Test
    fun `WHEN response provide a movie VERIFY result has same item with id`() {

        `when`(response.results).thenReturn(listOf(sampleMovie1))

        testCoroutineRule.runBlockingTest {
            `when`(repository.searchMovies(query = anyString(), page = Mockito.anyInt()))
                .thenReturn(response)

            viewModel.search(sampleQuery)

            assert(viewModel.results.value?.first()?.id == sampleMovie1.id)
        }
    }

    @Test
    fun `WHEN there is a single page VERIFY scrolling does NOT call search`() {
        testCoroutineRule.runBlockingTest {
            `when`(response.totalPages).thenReturn(1)

            //First feed
            `WHEN response provide a movie VERIFY result has same item with id`()

            viewModel.listScrolled(true, 5, 15, 12)

            verify(viewModel, never()).search(sampleQuery, nextPage = true)
        }
    }

    @Test
    fun `WHEN response provide a movie IF paging occurs VERIFY results data contains old and new movies`() {
        testCoroutineRule.runBlockingTest {
            //First to be sure there are more to load
            `when`(response.totalPages).thenReturn(4)

            //First feed
            `WHEN response provide a movie VERIFY result has same item with id`()

            //Now the list is set, to cover test: a new movie is provided
            `when`(response.results).thenReturn(listOf(sampleMovie2))

            viewModel.listScrolled(true, 5, 15, 12)

            verify(viewModel).search(sampleQuery, nextPage = true)

            assert(viewModel.results.value?.get(0)?.id == sampleMovie1.id)
            assert(viewModel.results.value?.get(1)?.id == sampleMovie2.id)
        }
    }

    @Test
    fun `WHEN response provide a movie IF paging occurs VERIFY results data contains only old one`() {
        testCoroutineRule.runBlockingTest {
            //First to be sure there are more to load but empty list returned
            `when`(response.totalPages).thenReturn(4)

            //First feed
            `WHEN response provide a movie VERIFY result has same item with id`()

            //Now the list is set, to cover test: a new movie is provided
            `when`(response.results).thenReturn(listOf())

            viewModel.listScrolled(true, 5, 15, 12)

            verify(viewModel).search(sampleQuery, nextPage = true)

            assert(viewModel.results.value?.get(0)?.id == sampleMovie1.id)
            assert(viewModel.results.value?.size == 2)
        }
    }


}