package com.kastik.apps.core.data.util

import com.kastik.apps.core.data.utils.Base64ImageExtractor

class FakeBase64ImageExtractor : Base64ImageExtractor {
    override suspend fun process(html: String): String {
        return html
    }
}