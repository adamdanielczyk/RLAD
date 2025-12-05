package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

@Inject
@ContributesBinding(AppScope::class)
class GetItemByIdUseCaseImpl(
    private val getCommonRepositoryUseCase: GetCommonRepositoryUseCase,
) : GetItemByIdUseCase {

    override operator fun invoke(id: String): Flow<ItemUiModel> =
        getCommonRepositoryUseCase().flatMapConcat { commonRepository -> commonRepository.getItemById(id) }
}
