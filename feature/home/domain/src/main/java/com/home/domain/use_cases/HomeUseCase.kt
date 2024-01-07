package com.home.domain.use_cases

data class HomeUseCase(
    val getPosts: GetPosts,
    val getSearchResult: GetSearchResult,
    val getPostsCategory: GetPostsCategory,
    val getPet: GetPet,
    val addFavorite: AddFavorite,
    val removeFavoriteHome: RemoveFavoriteHome,
    val getProfileHome: GetProfileHome
)
