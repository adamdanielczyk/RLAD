package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rlad.core.domain.model.DataSourceUiModel
import com.rlad.feature.search.R

@Composable
internal fun DataSourceBottomSheetContent(
    availableDataSources: List<DataSourceUiModel>,
    onDataSourceClicked: (DataSourceUiModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.data_source_picker_title),
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp,
            ),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        availableDataSources.forEach { uiDataSource ->
            DataSourceSheetItem(
                text = uiDataSource.pickerText,
                isSelected = uiDataSource.isSelected,
                onSheetItemClicked = { onDataSourceClicked(uiDataSource) },
            )
        }
    }
}

@Composable
private fun DataSourceSheetItem(
    text: String,
    isSelected: Boolean,
    onSheetItemClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onSheetItemClicked)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth(),
    ) {
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (!isSelected) {
            Spacer(modifier = Modifier.width(32.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        )
    }
}
