package com.dino.core.domain.model

data class News(
  val publishedAt: String,
  val author: String,
  val urlToImage: String,
  val description: String,
  val title: String,
  val url: String,
  val content: String
)
