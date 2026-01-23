package com.kastik.apps.core.model.user

enum class SearchScope {
    Title,
    Body,
    TitleAndBody;

    val includesTitle: Boolean
        get() = this == Title || this == TitleAndBody

    val includesBody: Boolean
        get() = this == Body || this == TitleAndBody

}