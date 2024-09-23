package ru.hotmule.lastik.data.sdk.packages

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import coil3.asImage
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

actual class PackageManager actual constructor(override val di: DI) : DIAware {

    private val context: Context by instance()

    actual suspend fun getApps() = withContext(AppCoroutineDispatcher.IO) {

        context.packageManager
            .queryIntentActivities(
                Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.GET_META_DATA
            )
            .map { it.activityInfo.applicationInfo }
            .filter { it.flags != ApplicationInfo.FLAG_SYSTEM }
            .map {
                Package(
                    name = it.packageName,
                    label = it.loadLabel(context.packageManager).toString(),
                    icon = it.loadIcon(context.packageManager).asImage()
                )
            }
    }
}