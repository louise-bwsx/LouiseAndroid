package com.louise.singlewebview.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.louise.singlewebview.manager.AppManager

@Composable
fun BottomBar() {
    val navItemList = getAllNavItem()

    // 教學影片中說 NavigationBarItem最少需要三個 最多只能五個
    // 但是在build的時候可以大於小於
    NavigationBar {
        navItemList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = AppManager.inst.path.value == item.label,
                onClick = { AppManager.inst.showWebView(item.label) },
                icon = {
                    // icon顯示小圓點
                    BadgedBox(badge = {
                        if (item.badgeCount > 0) {
                            Badge() {
                                Text(item.badgeCount.toString())
                            }
                        }
                    }) {
                        Icon(imageVector = item.icon, contentDescription = "Icon")
                    }
                },
                label = { Text(item.label) },
            )
        }
    }
}
