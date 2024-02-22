package com.home.ui.screens.home.items

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.home.ui.screens.home.HomeModel
import com.home.ui.ui.theme.PetAdoptionTheme

@Composable
fun CategoryItem(modifier: Modifier = Modifier,homeModel: HomeModel,onSelectedItem:()->Unit) {
    val scrollStata = rememberScrollState()

    val state  = homeModel.state

    val categoryProperty = mapOf(
        "Cat" to R.drawable.ic_cat,
        "Dog" to R.drawable.ic_dog,
        "Bird" to R.drawable.ic_bird,
        "Hamster" to R.drawable.ic_hamster,
        "Rabbit" to R.drawable.ic_rabbit,
        "Others" to R.drawable.ic_others,
    )

    val categoryPets =
        mapOf(1 to "Cat", 2 to "Dog", 3 to "Bird", 4 to "Hamster", 5 to "Rabbit",6 to "Others")



    var index = 0

    var activeCategory by remember {
        mutableStateOf(state.selectedCategory.isNotEmpty())
    }

    var selectedItem by remember {
        mutableStateOf(homeModel.state.selectedCategory)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollStata),
        verticalAlignment = Alignment.CenterVertically
    ) {

        categoryProperty.forEach { (pet, icon) ->
            val startPadding = if (index == 0) 30.dp else 10.dp
            val endPadding = if (index == 5) 30.dp else 10.dp

            Column(
                modifier = Modifier
                    .padding(start = startPadding, end = endPadding)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(15.dp),
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            color = if (activeCategory && state.selectedCategory == pet) MaterialTheme.colorScheme.tertiary else Beige
                        )
                        .clickable {
                            activeCategory = state.selectedCategory != pet
                            selectedItem = if (activeCategory) pet else ""
                            homeModel.updateState(state.copy(selectedCategory = selectedItem))
                            val id =
                                categoryPets.entries.find { it.value == selectedItem}?.key ?: -1

                            homeModel.updateCategory(categoryId = id)

                            onSelectedItem()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = if (activeCategory && state.selectedCategory == pet) Color.White else Color.Black,
                        modifier = Modifier.size(30.dp)
                    )

                }
                Text(
                    text = pet,
                    color = if (activeCategory && state.selectedCategory == pet) MaterialTheme.colorScheme.tertiary else Color.Black
                )
            }
            index++
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    PetAdoptionTheme {
      //  CategoryItem(){}
    }
}