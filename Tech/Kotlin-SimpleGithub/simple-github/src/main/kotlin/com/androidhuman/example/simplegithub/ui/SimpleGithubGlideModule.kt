package com.androidhuman.example.simplegithub.ui

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/*
 * Glide 4.0 은 기존 버전과 달리 이미지 표시 요청 API 와
 * 요청 시 추가 옵션을 선택하는 API 가 분리되었습니다.
 *
 * 따라서 기존과 같이 이미지 요청과 추가 옵션을 한번에 사용하려면 어노테이션 프로세서를 통해
 * GlideApp 클래스를 생성하도록 설정해야 합니다.
 *
 * GlideApp 클래스를 생성하려면 다음과 같이 AppGlideModule 을 상속한 클래스에
 * GlideModule 어노테이션을 추가하기만 하면 됩니다.
 */
@GlideModule
class SimpleGithubGlideModule : AppGlideModule() {
    // Intentionally left blank
}