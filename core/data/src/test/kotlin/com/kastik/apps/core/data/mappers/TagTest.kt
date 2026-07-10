package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.testdata.baseSubscribableTagsProto
import com.kastik.apps.core.datastore.testdata.baseSubscriptionsProto
import com.kastik.apps.core.network.testdata.baseTagDto
import com.kastik.apps.core.testing.testdata.baseTagEntity
import org.junit.Test

class TagTest {

    @Test
    fun announcementTagDtoMapsToTagEntity() {
        val dto = baseTagDto
        val result = dto.toTagEntity()

        assertThat(result.id).isEqualTo(dto.id)
        assertThat(result.title).isEqualTo(dto.title)
        assertThat(result.parentId).isEqualTo(dto.parentId)
        assertThat(result.isPublic).isEqualTo(dto.isPublic)
    }

    @Test
    fun tagEntityMapsToTag() {
        val entity = baseTagEntity
        val result = entity.toTag()

        assertThat(result.id).isEqualTo(entity.id)
        assertThat(result.title).isEqualTo(entity.title)
    }


    @Test
    fun subscribedTagDtoMapsToSubscribedTagProto() {
        val dto = listOf(baseTagDto)
        val result = dto.map { it.toTagProto() }

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(dto.size)


        result.zip(result).forEach { (dto, proto) ->
            assertThat(proto.id).isEqualTo(dto.id)
            assertThat(proto.title).isEqualTo(dto.title)
        }
    }

    @Test
    fun subscribedTagProtoMapsToTag() {
        val proto = baseSubscriptionsProto
        val domain = proto.tagsList.map { it.toTag() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(proto.tagsList.size)

        proto.tagsList.zip(domain).forEach { (proto, domain) ->
            assertThat(domain.id).isEqualTo(proto.id)
            assertThat(domain.title).isEqualTo(proto.title)
        }
    }

    @Test
    fun subscribableTagsDtoMapsToSubscribableTagProto() {
        val dto = baseTagDto
        val proto = dto.toTagProto()

        assertThat(proto.id).isEqualTo(dto.id)
        assertThat(proto.parentId).isEqualTo(dto.parentId ?: 0)
        assertThat(proto.title).isEqualTo(dto.title)
    }

    @Test
    fun subscribableTagsProtoMapsToSubscribableTag() {
        val proto = baseSubscribableTagsProto
        val domain = proto.tagsList.map { it.toTag() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(proto.tagsList.size)
        assertThat(domain).containsExactlyElementsIn(proto.tagsList.map { it.toTag() })
    }
}