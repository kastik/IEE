package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.proto.SearchScopeProto
import com.kastik.apps.core.datastore.proto.SortTypeProto
import com.kastik.apps.core.datastore.proto.ThemeProto
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import org.junit.Test

class PreferencesTest {

    @Test
    fun themeMapsToDomainTheme() {
        assertThat(ThemeProto.System.toTheme()).isEqualTo(Theme.FOLLOW_SYSTEM)
        assertThat(ThemeProto.Light.toTheme()).isEqualTo(Theme.LIGHT)
        assertThat(ThemeProto.Dark.toTheme()).isEqualTo(Theme.DARK)
        assertThat(ThemeProto.UNRECOGNIZED.toTheme()).isEqualTo(Theme.FOLLOW_SYSTEM)
    }

    @Test
    fun themeMapsToThemeProto() {
        assertThat(Theme.FOLLOW_SYSTEM.toThemeProto()).isEqualTo(ThemeProto.System)
        assertThat(Theme.LIGHT.toThemeProto()).isEqualTo(ThemeProto.Light)
        assertThat(Theme.DARK.toThemeProto()).isEqualTo(ThemeProto.Dark)
    }

    @Test
    fun sortTypeMapsToSortTypeProto() {
        assertThat(SortType.ASC.toSortTypeProto()).isEqualTo(SortTypeProto.ASC)
        assertThat(SortType.DESC.toSortTypeProto()).isEqualTo(SortTypeProto.DESC)
        assertThat(SortType.Priority.toSortTypeProto()).isEqualTo(SortTypeProto.Priority)
    }

    @Test
    fun sortTypeProtoMapsToSortType() {
        assertThat(SortTypeProto.ASC.toSortType()).isEqualTo(SortType.ASC)
        assertThat(SortTypeProto.DESC.toSortType()).isEqualTo(SortType.DESC)
        assertThat(SortTypeProto.Priority.toSortType()).isEqualTo(SortType.Priority)
        assertThat(SortTypeProto.UNRECOGNIZED.toSortType()).isEqualTo(SortType.ASC)
    }

    @Test
    fun searchScopeMapsToQueryScopeProto() {
        assertThat(SearchScope.Title.toSearchScopeProto()).isEqualTo(SearchScopeProto.Title)
        assertThat(SearchScope.Body.toSearchScopeProto()).isEqualTo(SearchScopeProto.Body)
        assertThat(SearchScope.TitleAndBody.toSearchScopeProto())
            .isEqualTo(SearchScopeProto.TITLE_AND_BODY)
    }

    @Test
    fun searchScopeProtoMapsToSearchScope() {
        assertThat(SearchScopeProto.Title.toSearchScope()).isEqualTo(SearchScope.Title)
        assertThat(SearchScopeProto.Body.toSearchScope()).isEqualTo(SearchScope.Body)
        assertThat(SearchScopeProto.TITLE_AND_BODY.toSearchScope())
            .isEqualTo(SearchScope.TitleAndBody)
        assertThat(SearchScopeProto.UNRECOGNIZED.toSearchScope()).isEqualTo(SearchScope.Title)
    }
}
