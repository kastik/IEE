package com.kastik.apps.core.common.extensions

import java.text.Normalizer

fun String.removeAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("\\p{M}"), "")
