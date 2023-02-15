package com.dino.berta.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.dino.berta.ui.navigation.BertaBottomNavDestination
import com.dino.berta.ui.navigation.BertaBottomNavigationBar
import com.dino.berta.ui.navigation.BertaNavigationActions
import com.dino.berta.ui.navigation.BertaNavigationRail
import com.dino.berta.ui.navigation.ModalNavigationDrawerContent
import com.dino.berta.ui.navigation.PermanentNavigationDrawerContent
import com.dino.berta.ui.navigation.TopLevelNavRoute
import com.dino.berta.ui.screen.home.HomeScreen
import com.dino.berta.utils.BertaContentType
import com.dino.berta.utils.BertaNavigationContentPosition
import com.dino.berta.utils.BertaNavigationType
import com.dino.berta.utils.DevicePosture
import com.dino.berta.utils.isBookPosture
import com.dino.berta.utils.isSeparating
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api @Composable fun BertaApp(
  windowSize: WindowSizeClass,
  displayFeatures: List<DisplayFeature>
) {
  /**
   * This will help us select type of navigation and content type depending on window size and
   * fold state of the device.
   */
  val navigationType: BertaNavigationType
  val contentType: BertaContentType

  /**
   * We are using display's folding features to map the device postures a fold is in.
   * In the state of folding device If it's half fold in BookPosture we want to avoid content
   * at the crease/hinge
   */
  val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
  val foldingDevicePosture = when {
    isBookPosture(foldingFeature) ->
      DevicePosture.BookPosture(foldingFeature.bounds)

    isSeparating(foldingFeature) ->
      DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

    else -> DevicePosture.NormalPosture
  }

  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact -> {
      navigationType = BertaNavigationType.BOTTOM_NAVIGATION
      contentType = BertaContentType.SINGLE_PANE
    }
    WindowWidthSizeClass.Medium -> {
      navigationType = BertaNavigationType.NAVIGATION_RAIL
      contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
        BertaContentType.DUAL_PANE
      } else {
        BertaContentType.SINGLE_PANE
      }
    }
    WindowWidthSizeClass.Expanded -> {
      navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
        BertaNavigationType.NAVIGATION_RAIL
      } else {
        BertaNavigationType.PERMANENT_NAVIGATION_DRAWER
      }
      contentType = BertaContentType.DUAL_PANE
    }
    else -> {
      navigationType = BertaNavigationType.BOTTOM_NAVIGATION
      contentType = BertaContentType.SINGLE_PANE
    }
  }

  /**
   * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
   * ergonomics and reachability depending upon the height of the device.
   */
  val navigationContentPosition = when (windowSize.heightSizeClass) {
    WindowHeightSizeClass.Compact -> {
      BertaNavigationContentPosition.TOP
    }
    WindowHeightSizeClass.Medium,
    WindowHeightSizeClass.Expanded -> {
      BertaNavigationContentPosition.CENTER
    }
    else -> {
      BertaNavigationContentPosition.TOP
    }
  }

  BertaNavigationWrapper(
    navigationType = navigationType,
    contentType = contentType,
    displayFeatures = displayFeatures,
    navigationContentPosition = navigationContentPosition
  )
}

@ExperimentalMaterial3Api @Composable fun BertaNavigationWrapper(
  navigationType: BertaNavigationType,
  contentType: BertaContentType,
  displayFeatures: List<DisplayFeature>,
  navigationContentPosition: BertaNavigationContentPosition
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  val navController = rememberNavController()
  val navigationActions = remember(navController) {
    BertaNavigationActions(navController)
  }
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val selectedDestination = navBackStackEntry?.destination?.route ?: TopLevelNavRoute.HOME.routeName

  if (navigationType == BertaNavigationType.PERMANENT_NAVIGATION_DRAWER) {
    PermanentNavigationDrawer(drawerContent = {
      PermanentNavigationDrawerContent(
        selectedDestination = selectedDestination,
        navigationContentPosition = navigationContentPosition,
        navigateToTopLevelDestination = navigationActions::navigateTo,
      )
    }) {
      BertaAppContent(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo
      )
    }
  } else {
    ModalNavigationDrawer(
      drawerContent = {
        ModalNavigationDrawerContent(
          selectedDestination = selectedDestination,
          navigationContentPosition = navigationContentPosition,
          navigateToTopLevelDestination = { destination ->
            navigationActions.navigateTo(destination)
            scope.launch {
              drawerState.close()
            }
          },
          onDrawerClicked = {
            scope.launch {
              drawerState.close()
            }
          }
        )
      },
      drawerState = drawerState,
      gesturesEnabled = navigationType != BertaNavigationType.BOTTOM_NAVIGATION
    ) {
      BertaAppContent(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo
      ) {
        scope.launch {
          drawerState.open()
        }
      }
    }
  }
}

@Composable fun BertaAppContent(
  modifier: Modifier = Modifier,
  navigationType: BertaNavigationType,
  contentType: BertaContentType,
  displayFeatures: List<DisplayFeature>,
  navigationContentPosition: BertaNavigationContentPosition,
  navController: NavHostController,
  selectedDestination: String,
  navigateToTopLevelDestination: (BertaBottomNavDestination) -> Unit,
  onDrawerClicked: () -> Unit = {}
) {
  Row(modifier = modifier.fillMaxSize()) {
    AnimatedVisibility(visible = navigationType == BertaNavigationType.NAVIGATION_RAIL) {
      BertaNavigationRail(
        selectedDestination = selectedDestination,
        navigationContentPosition = navigationContentPosition,
        navigateToTopLevelDestination = navigateToTopLevelDestination,
        onDrawerClicked = onDrawerClicked
      )
    }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
      BertaNavHost(
        navController = navController,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
        modifier = Modifier.weight(1f)
      )
      AnimatedVisibility(visible = navigationType == BertaNavigationType.BOTTOM_NAVIGATION) {
        BertaBottomNavigationBar(
          selectedDestination = selectedDestination,
          navigateToTopLevelDestination = navigateToTopLevelDestination
        )
      }
    }
  }
}

@Composable
private fun BertaNavHost(
  navController: NavHostController,
  contentType: BertaContentType,
  displayFeatures: List<DisplayFeature>,
  navigationType: BertaNavigationType,
  modifier: Modifier = Modifier,
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = TopLevelNavRoute.HOME.routeName,
  ) {
    composable(TopLevelNavRoute.HOME.routeName) {
      HomeScreen()
    }
    composable(TopLevelNavRoute.TRENDING.routeName) {
      Text(text = "Trending", modifier = Modifier.fillMaxSize())
    }
    composable(TopLevelNavRoute.BOOKMARK.routeName) {
      Text(text = "Bookmark", modifier = Modifier.fillMaxSize())
    }
  }
}