package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.NavigationTab
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel

@Composable
fun MainScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    onRequestGpsLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface.copy(alpha = 0.95f),
                contentColor = TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = DarkCardBorder.copy(alpha = 0.5f)
                    )
            ) {
                // Tab 1: Weather Details
                val weatherSelected = uiState.activeTab == NavigationTab.WEATHER
                NavigationBarItem(
                    selected = weatherSelected,
                    onClick = { viewModel.selectTab(NavigationTab.WEATHER) },
                    icon = {
                        Icon(
                            imageVector = if (weatherSelected) Icons.Filled.Cloud else Icons.Outlined.Cloud,
                            contentDescription = "Weather",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Weather",
                            fontWeight = if (weatherSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        selectedTextColor = AccentCyan,
                        indicatorColor = AccentCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_weather")
                )

                // Tab 2: World Clocks
                val clocksSelected = uiState.activeTab == NavigationTab.WORLD_CLOCKS
                NavigationBarItem(
                    selected = clocksSelected,
                    onClick = { viewModel.selectTab(NavigationTab.WORLD_CLOCKS) },
                    icon = {
                        Icon(
                            imageVector = if (clocksSelected) Icons.Filled.Public else Icons.Outlined.Public,
                            contentDescription = "World Clocks",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "World Clocks",
                            fontWeight = if (clocksSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        selectedTextColor = AccentCyan,
                        indicatorColor = AccentCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_world_clocks")
                )

                // Tab 3: Search / Add Places
                val searchSelected = uiState.activeTab == NavigationTab.SEARCH
                NavigationBarItem(
                    selected = searchSelected,
                    onClick = { viewModel.selectTab(NavigationTab.SEARCH) },
                    icon = {
                        Icon(
                            imageVector = if (searchSelected) Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Explore",
                            fontWeight = if (searchSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        selectedTextColor = AccentCyan,
                        indicatorColor = AccentCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_search")
                )

                // Tab 4: Settings
                val settingsSelected = uiState.activeTab == NavigationTab.SETTINGS
                NavigationBarItem(
                    selected = settingsSelected,
                    onClick = { viewModel.selectTab(NavigationTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = if (settingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Settings",
                            fontWeight = if (settingsSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        selectedTextColor = AccentCyan,
                        indicatorColor = AccentCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_settings")
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.activeTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    NavigationTab.WEATHER -> WeatherDetailScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToSearch = { viewModel.selectTab(NavigationTab.SEARCH) },
                        onRequestGpsLocation = onRequestGpsLocation
                    )
                    NavigationTab.WORLD_CLOCKS, NavigationTab.TIME_CONVERTER -> WorldClocksScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToSearch = { viewModel.selectTab(NavigationTab.SEARCH) }
                    )
                    NavigationTab.SEARCH -> SearchLocationsScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.selectTab(NavigationTab.WEATHER) }
                    )
                    NavigationTab.SETTINGS -> SettingsScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
