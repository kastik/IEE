package com.kastik.appsaboard.data.mappers

import com.kastik.appsaboard.data.datasource.remote.dto.PublisherDto
import com.kastik.appsaboard.domain.models.Publisher


fun Publisher.toPublisherDto(): PublisherDto = PublisherDto(
    id = this.id,
    name = this.name,
)

fun PublisherDto.toPublisher(): Publisher = Publisher(
    id = this.id,
    name = this.name,
)

