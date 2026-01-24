package com.rlad.core.infrastructure.common.usecase

import androidx.paging.PagingData
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemsUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

@ContributesBinding(AppScope::class)
class GetItemsUseCaseImpl(
    private val getCommonRepositoryUseCase: GetCommonRepositoryUseCase,
) : GetItemsUseCase {

    override operator fun invoke(query: String?): Flow<PagingData<ItemUiModel>> =
        getCommonRepositoryUseCase().flatMapConcat { commonRepository ->
            if (query != null) {
                commonRepository.getSearchItems(query)
            } else {
                commonRepository.getAllItems()
            }
        }
}
