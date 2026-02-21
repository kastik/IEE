package com.kastik.apps.core.domain.service

interface FileDownloader {
    suspend fun downloadAttachment(
        url: String,
        fileName: String,
        mimeType: String,
    )
}