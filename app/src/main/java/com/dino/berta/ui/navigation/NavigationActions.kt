package com.dino.berta.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dino.berta.R

enum class TopLevelNavRoute(val routeName: String) {
  HOME("home"),
  TRENDING("trending"),
  BOOKMARK("bookmark"),
  NEWS_DETAIL("news-detail")
}

data class BertaBottomNavDestination(
  val route: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  @StringRes val iconTextId: Int
)

class BertaNavigationActions(private val navController: NavHostController) {

  fun navigateTo(destination: BertaBottomNavDestination) {
    navController.navigate(destination.route) {
      // Pop up to the start destination of the graph to
      // avoid building up a large stack of destinations
      // on the back stack as users select items
      popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
      }
      // Avoid multiple copies of the same destination when
      // re-selecting the same item
      launchSingleTop = true
      // Restore state when re-selecting a previously selected item
      restoreState = true
    }
  }
}

val BOTTOM_NAV_DESTINATION = listOf(
  BertaBottomNavDestination(
    route = TopLevelNavRoute.HOME.routeName,
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Rounded.Home,
    iconTextId = R.string.tab_home
  ),
  BertaBottomNavDestination(
    route = TopLevelNavRoute.TRENDING.routeName,
    selectedIcon = Icons.Rounded.LocalFireDepartment,
    unselectedIcon = Icons.Rounded.LocalFireDepartment,
    iconTextId = R.string.tab_trending
  ),
  BertaBottomNavDestination(
    route = TopLevelNavRoute.BOOKMARK.routeName,
    selectedIcon = Icons.Rounded.Bookmark,
    unselectedIcon = Icons.Rounded.Bookmark,
    iconTextId = R.string.tab_bookmark
  )
)