package com.feature.profile.domain.use_cases

data class ProfileUseCase(
    val getMyPets: GetMyPets,
    val getFavoritePets: GetFavoritePets,
    val removeFavorite: RemoveFavorite,
    val addPost: AddPost,
    val updatePost: UpdatePost,
    val deletePost: DeletePost,
    val getProfile: GetProfile,
    val updateProfile: UpdateProfile
)
