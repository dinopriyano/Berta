package com.dino.berta.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.dino.berta.R
import com.dino.berta.utils.BertaNavigationContentPosition

@Composable
fun BertaBottomNavigationBar(
  selectedDestination: String,
  navigateToTopLevelDestination: (BertaBottomNavDestination) -> Unit
) {
  NavigationBar(modifier = Modifier.fillMaxWidth()) {
    BOTTOM_NAV_DESTINATION.forEach { destination ->
      NavigationBarItem(
        selected = selectedDestination == destination.route,
        onClick = { navigateToTopLevelDestination(destination) },
        icon = {
          Icon(
            imageVector = destination.selectedIcon,
            contentDescription = stringResource(id = destination.iconTextId)
          )
        }
      )
    }
  }
}

@Composable
fun BertaNavigationRail(
  selectedDestination: String,
  navigationContentPosition: BertaNavigationContentPosition,
  navigateToTopLevelDestination: (BertaBottomNavDestination) -> Unit,
  onDrawerClicked: () -> Unit = {},
) {
  NavigationRail(
    modifier = Modifier.fillMaxHeight(),
    containerColor = MaterialTheme.colorScheme.inverseOnSurface
  ) {
    Layout(
      modifier = Modifier.widthIn(max = 80.dp),
      content = {
        Column(
          modifier = Modifier.layoutId(LayoutType.HEADER),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          NavigationRailItem(
            selected = false,
            onClick = onDrawerClicked,
            icon = {
              Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
              )
            }
          )
          Spacer(Modifier.height(8.dp)) // NavigationRailHeaderPadding
          Spacer(Modifier.height(4.dp)) // NavigationRailVerticalPadding
        }

        Column(
          modifier = Modifier.layoutId(LayoutType.CONTENT),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          BOTTOM_NAV_DESTINATION.forEach { destination ->
            NavigationRailItem(
              selected = selectedDestination == destination.route,
              onClick = { navigateToTopLevelDestination(destination) },
              icon = {
                Icon(
                  imageVector = destination.selectedIcon,
                  contentDescription = stringResource(
                    id = destination.iconTextId
                  )
                )
              }
            )
          }
        }
      },
      measurePolicy = { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach {
          when (it.layoutId) {
            LayoutType.HEADER -> headerMeasurable = it
            LayoutType.CONTENT -> contentMeasurable = it
            else -> error("Unknown layoutId encountered!")
          }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
          constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
          // Place the header, this goes at the top
          headerPlaceable.placeRelative(0, 0)

          // Determine how much space is not taken up by the content
          val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

          val contentPlaceableY = when (navigationContentPosition) {
            // Figure out the place we want to place the content, with respect to the
            // parent (ignoring the header for now)
            BertaNavigationContentPosition.TOP -> 0
            BertaNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
          }
            // And finally, make sure we don't overlap with the header.
            .coerceAtLeast(headerPlaceable.height)

          contentPlaceable.placeRelative(0, contentPlaceableY)
        }
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawerContent(
  selectedDestination: String,
  navigationContentPosition: BertaNavigationContentPosition,
  navigateToTopLevelDestination: (BertaBottomNavDestination) -> Unit,
) {
  PermanentDrawerSheet(modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp)) {
    Layout(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.inverseOnSurface)
        .padding(16.dp),
      content = {
        Text(
          modifier = Modifier
            .layoutId(LayoutType.HEADER)
            .padding(16.dp),
          text = stringResource(id = R.string.app_name).uppercase(),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.primary
        )

        Column(
          modifier = Modifier
            .layoutId(LayoutType.CONTENT)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          BOTTOM_NAV_DESTINATION.forEach { destination ->
            NavigationDrawerItem(
              selected = selectedDestination == destination.route,
              label = {
                Text(
                  text = stringResource(id = destination.iconTextId),
                  modifier = Modifier.padding(horizontal = 16.dp)
                )
              },
              icon = {
                Icon(
                  imageVector = destination.selectedIcon,
                  contentDescription = stringResource(
                    id = destination.iconTextId
                  )
                )
              },
              colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
              ),
              onClick = { navigateToTopLevelDestination(destination) }
            )
          }
        }
      },
      measurePolicy = { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach {
          when (it.layoutId) {
            LayoutType.HEADER -> headerMeasurable = it
            LayoutType.CONTENT -> contentMeasurable = it
            else -> error("Unknown layoutId encountered!")
          }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
          constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
          // Place the header, this goes at the top
          headerPlaceable.placeRelative(0, 0)

          // Determine how much space is not taken up by the content
          val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

          val contentPlaceableY = when (navigationContentPosition) {
            // Figure out the place we want to place the content, with respect to the
            // parent (ignoring the header for now)
            BertaNavigationContentPosition.TOP -> 0
            BertaNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
          }
            // And finally, make sure we don't overlap with the header.
            .coerceAtLeast(headerPlaceable.height)

          contentPlaceable.placeRelative(0, contentPlaceableY)
        }
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerContent(
  selectedDestination: String,
  navigationContentPosition: BertaNavigationContentPosition,
  navigateToTopLevelDestination: (BertaBottomNavDestination) -> Unit,
  onDrawerClicked: () -> Unit = {}
) {
  ModalDrawerSheet {
    Layout(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.inverseOnSurface)
        .padding(16.dp),
      content = {
        Column(
          modifier = Modifier.layoutId(LayoutType.HEADER),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = stringResource(id = R.string.app_name).uppercase(),
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onDrawerClicked) {
              Icon(
                imageVector = Icons.Default.MenuOpen,
                contentDescription = null
              )
            }
          }
        }

        Column(
          modifier = Modifier
            .layoutId(LayoutType.CONTENT)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          BOTTOM_NAV_DESTINATION.forEach { destination ->
            NavigationDrawerItem(
              selected = selectedDestination == destination.route,
              label = {
                Text(
                  text = stringResource(id = destination.iconTextId),
                  modifier = Modifier.padding(horizontal = 16.dp)
                )
              },
              icon = {
                Icon(
                  imageVector = destination.selectedIcon,
                  contentDescription = stringResource(
                    id = destination.iconTextId
                  )
                )
              },
              colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
              ),
              onClick = { navigateToTopLevelDestination(destination) }
            )
          }
        }
      },
      measurePolicy = { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach {
          when (it.layoutId) {
            LayoutType.HEADER -> headerMeasurable = it
            LayoutType.CONTENT -> contentMeasurable = it
            else -> error("Unknown layoutId encountered!")
          }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
          constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
          // Place the header, this goes at the top
          headerPlaceable.placeRelative(0, 0)

          // Determine how much space is not taken up by the content
          val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

          val contentPlaceableY = when (navigationContentPosition) {
            // Figure out the place we want to place the content, with respect to the
            // parent (ignoring the header for now)
            BertaNavigationContentPosition.TOP -> 0
            BertaNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
          }
            // And finally, make sure we don't overlap with the header.
            .coerceAtLeast(headerPlaceable.height)

          contentPlaceable.placeRelative(0, contentPlaceableY)
        }
      }
    )
  }
}

enum class LayoutType {
  HEADER, CONTENT
}