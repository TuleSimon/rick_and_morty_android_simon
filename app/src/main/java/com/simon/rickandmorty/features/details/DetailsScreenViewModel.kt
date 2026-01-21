package com.simon.rickandmorty.features.details

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.domain.base.BaseResult
import com.simon.domain.entities.MovieCharacter
import com.simon.domain.features.character.GetCharacterDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsScreenState())
    val state: StateFlow<DetailsScreenState> = _state

    fun initCharacterUseCase(
        characterId: Int,
        character: MovieCharacter?
    ) {
        if (character != null) {
            _state.update {
                it.copy(
                    characters = character,
                    isFetchingDetails = false,
                    hasError = false
                )
            }
            return
        }

        fetchCharacterDetails(characterId)
    }

    private fun fetchCharacterDetails(characterId: Int) {
        viewModelScope.launch {
            getCharacterDetailsUseCase.execute(characterId).collect { result ->
                when (result) {
                    is BaseResult.Loading<*> -> {
                        _state.update {
                            it.copy(
                                isFetchingDetails = true,
                                hasError = false
                            )
                        }
                    }

                    is BaseResult.Success -> {
                        _state.update {
                            it.copy(
                                characters = result.data,
                                isFetchingDetails = false,
                                hasError = false
                            )
                        }
                    }

                    is BaseResult.Error<*>,
                    is BaseResult.NetworkError,
                    is BaseResult.NoInternet -> {
                        _state.update {
                            it.copy(
                                isFetchingDetails = false,
                                hasError = true
                            )
                        }
                    }
                }
            }
        }
    }

}

@Stable
data class DetailsScreenState(
    val characters: MovieCharacter?=null,
    val isFetchingDetails: Boolean = false,
    val hasError:Boolean=false
)