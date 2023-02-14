package com.dino.core.data.source.remote.dto.response

import com.dino.core.domain.model.News
import com.github.yanneckreiss.kconmapper.annotations.KConMapper
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
	val totalResults: Int,
	val articles: List<ArticlesItem>,
	val status: String
)

@Serializable
@KConMapper(toClasses = [News::class])
data class ArticlesItem(
	val publishedAt: String,
	val author: String?,
	val urlToImage: String,
	val description: String,
	val source: Source,
	val title: String,
	val url: String,
	val content: String
)

@Serializable
data class Source(
	val name: String,
	val id: String?
)

