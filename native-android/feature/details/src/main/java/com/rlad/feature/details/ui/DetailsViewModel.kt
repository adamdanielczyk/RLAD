package com.rlad.feature.details.ui

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.imageLoader
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.Flow
import java.io.File

@AssistedInject
@OptIn(ExperimentalCoilApi::class)
class DetailsViewModel(
    getItemByIdUseCase: GetItemByIdUseCase,
    @Assisted id: String,
) : ViewModel() {

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(id: String): DetailsViewModel
    }

    val item: Flow<ItemUiModel> = getItemByIdUseCase(id)

    fun onShareItemClicked(context: Context, item: ItemUiModel) {
        context.imageLoader.diskCache?.openSnapshot(item.imageUrl)?.use { snapshot ->
            val mimeType = getMimeType(snapshot)
            val sharingFile = getSharingFile(snapshot, mimeType)

            val sharingFileUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName,
                sharingFile,
            )

            val intent = Intent(Intent.ACTION_SEND)
                .setType(mimeType)
                .putExtra(Intent.EXTRA_SUBJECT, item.name)
                .putExtra(Intent.EXTRA_STREAM, sharingFileUri)

            context.startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun getMimeType(snapshot: DiskCache.Snapshot): String {
        val metadata = snapshot.metadata.toFile().readText()
        return metadata.substringAfter("content-type: ").substringBefore("\n")
    }

    private fun getSharingFile(snapshot: DiskCache.Snapshot, mimeType: String): File {
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType).orEmpty()

        val snapshotDataFile = snapshot.data.toFile()
        val sharingFile = File(snapshotDataFile.parent, "${snapshotDataFile.name}.$extension")
        snapshotDataFile.copyTo(sharingFile, overwrite = true)

        return sharingFile
    }
}
