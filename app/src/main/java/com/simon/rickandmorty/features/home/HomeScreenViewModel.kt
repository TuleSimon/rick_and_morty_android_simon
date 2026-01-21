package com.simon.rickandmorty.features.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.features.character.GetCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    init {
        fetchInitialCharacters()
    }


    fun fetchInitialCharacters() {
        if (_state.value.isFetchingInitialData) return

        _state.update {
            it.copy(
                isFetchingInitialData = true,
                hasError = false,
                currentPage = 1,
                hasMore = true
            )
        }

        fetchCharacters(page = 1, isInitial = true)
    }


    fun fetchMoreCharacters() {
        val currentState = _state.value

        if (
            currentState.isFetchingMoreData ||
            !currentState.hasMore ||
            currentState.isFetchingInitialData
        ) return

        _state.update {
            it.copy(isFetchingMoreData = true)
        }

        fetchCharacters(
            page = currentState.currentPage + 1,
            isInitial = false
        )
    }

    private fun fetchCharacters(
        page: Int,
        isInitial: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getCharacterUseCase.execute(page).collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        val response = result.data
                        val newCharacters = response.results

                        _state.update { current ->
                            val updatedList: PersistentList<MovieCharacter> =
                                (if (isInitial) {
                                    newCharacters.toPersistentList()
                                } else {
                                    (current.characters + newCharacters).toPersistentList()
                                })

                            current.copy(
                                characters = updatedList,
                                currentPage = page,
                                hasMore = response.info?.next != null,
                                isFetchingInitialData = false,
                                isFetchingMoreData = false,
                                hasError = false
                            )
                        }
                    }

                    is BaseResult.Error, BaseResult.NetworkError, BaseResult.NoInternet -> {
                        _state.update {
                            it.copy(
                                isFetchingInitialData = false,
                                isFetchingMoreData = false,
                                hasError = true
                            )
                        }
                    }

                    is BaseResult.Loading -> {

                    }
                }
            }
        }
    }
}


@Stable
data class HomeScreenState(
    val characters: PersistentList<MovieCharacter> = persistentListOf(),
    val isFetchingInitialData: Boolean = false,
    val isFetchingMoreData: Boolean = false,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val hasError:Boolean=false
)