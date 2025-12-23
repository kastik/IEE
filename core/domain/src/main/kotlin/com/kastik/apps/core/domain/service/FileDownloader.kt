package com.kastik.apps.core.domain.service

interface FileDownloader {
    fun downloadAttachment(
        url: String,
        fileName: String,
        mimeType: String,
        authToken: String?
    )
}