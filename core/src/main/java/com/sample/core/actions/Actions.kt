package com.sample.core.actions

import android.content.Context
import android.content.Intent

const val EXTRA_CHARACTER_ID = "EXTRA_CHARACTER_ID"

object Actions {

    fun openSearchIntent(context: Context): Intent {
        return internalIntent(context, "com.sample.features.search.open")
    }

    fun openDetailsIntent(context: Context, characterId: Int): Intent {
        return internalIntent(context, "com.sample.features.details.open")
            .putExtra(EXTRA_CHARACTER_ID, characterId)
    }

    private fun internalIntent(context: Context, action: String) =
        Intent(action).setPackage(context.packageName)
}