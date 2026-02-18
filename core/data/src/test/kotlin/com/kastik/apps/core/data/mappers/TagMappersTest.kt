package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.testdata.subscribableTagProtoTestData
import com.kastik.apps.core.datastore.testdata.subscribedTagProtoTestData
import com.kastik.apps.core.network.testdata.announcementTagDtoTestData
import com.kastik.apps.core.network.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.network.testdata.subscribedTagDtoTestData
import com.kastik.apps.core.testing.testdata.announcementTagEntityTestData
import org.junit.Test

class TagMappersTest {

    @Test
    fun announcementTagDtoMapsToTagEntity() {
        val dto = announcementTagDtoTestData
        val result = dto.map { it.toTagEntity() }

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(dto.size)

        dto.zip(result).forEach { (dto, entity) ->
            assertThat(entity.id).isEqualTo(dto.id)
            assertThat(entity.title).isEqualTo(dto.title)
            assertThat(entity.parentId).isEqualTo(dto.parentId)
            assertThat(entity.isPublic).isEqualTo(dto.isPublic)
        }
    }

    @Test
    fun tagEntityMapsToTag() {
        val entity = announcementTagEntityTestData
        val result = entity.map { it.toTag() }

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(entity.size)

        entity.zip(result).forEach { (entity, domain) ->
            assertThat(domain.id).isEqualTo(entity.id)
            assertThat(domain.title).isEqualTo(entity.title)
        }
    }


    @Test
    fun subscribedTagDtoMapsToSubscribedTagProto() {
        val dto = subscribedTagDtoTestData
        val result = dto.map { it.toSubscribedTagProto() }

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(dto.size)

        dto.zip(result).forEach { (dto, proto) ->
            assertThat(proto.id).isEqualTo(dto.id)
            assertThat(proto.title).isEqualTo(dto.title)
        }
    }

    @Test
    fun subscribedTagProtoMapsToTag() {
        val proto = subscribedTagProtoTestData
        val domain = proto.map { it.toTag() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(proto.size)

        proto.zip(domain).forEach { (proto, domain) ->
            assertThat(domain.id).isEqualTo(proto.id)
            assertThat(domain.title).isEqualTo(proto.title)
        }
    }

    @Test
    fun subscribableTagsDtoMapsToSubscribableTagProto() {
        val dto = subscribableTagsDtoTestData
        val proto = dto.map { it.toSubscribableTagProto() }

        assertThat(proto).isNotEmpty()
        assertThat(proto.size).isEqualTo(dto.size)

        dto.zip(proto).forEach { (dto, proto) ->
            assertThat(proto.id).isEqualTo(dto.id)
            assertThat(proto.parentId).isEqualTo(dto.parentId ?: 0)
            assertThat(proto.title).isEqualTo(dto.title)
            assertThat(proto.deletedAt).isEqualTo(dto.deletedAt)
        }
    }

    @Test
    fun subscribableTagsProtoMapsToSubscribableTag() {
        val proto = subscribableTagProtoTestData
        val domain = proto.map { it.toSubscribableTag() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(proto.size)
        assertThat(domain).containsExactlyElementsIn(proto.map { it.toSubscribableTag() })
    }
}