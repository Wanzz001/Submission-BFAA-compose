package org.d3if0080.bfaa_compose.response

import com.google.gson.annotations.SerializedName

data class ListFollowResponse(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
)
