package com.kastik.apps.feature.licenses

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


@Composable
internal fun LicensesRoute() {
    hiltViewModel<LicensesScreenViewModel>()


}