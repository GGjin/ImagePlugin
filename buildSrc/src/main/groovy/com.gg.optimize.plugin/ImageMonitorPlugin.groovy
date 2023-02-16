package com.gg.optimize.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

/**
 * @description:
 * @author: GG
 * @createDate: 2023 2.15 0015 19:27
 * @updateUser:
 * @updateDate: 2023 2.15 0015 19:27
 */
class ImageMonitorPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        //传递参数
        //这里下iu该class ， asm ， 模板，transform
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new ImageMonitorTransform())
    }
}
