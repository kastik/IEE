package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.StageProto
import com.kastik.apps.core.model.onboard.OnboardStage

fun StageProto.toStage() =
    when (this) {
        StageProto.Welcome -> OnboardStage.Welcome
        StageProto.SignIn -> OnboardStage.SignIn
        StageProto.Notifications -> OnboardStage.Notifications
        StageProto.Appearance -> OnboardStage.Appearance
        StageProto.Preferences -> OnboardStage.Preferences
        StageProto.Finish -> OnboardStage.Finish
        StageProto.UNRECOGNIZED -> OnboardStage.Welcome
    }

fun OnboardStage.toStageProto() =
    when (this) {
        OnboardStage.Welcome -> StageProto.Welcome
        OnboardStage.SignIn -> StageProto.SignIn
        OnboardStage.Notifications -> StageProto.Notifications
        OnboardStage.Appearance -> StageProto.Appearance
        OnboardStage.Preferences -> StageProto.Preferences
        OnboardStage.Finish -> StageProto.Finish
    }
