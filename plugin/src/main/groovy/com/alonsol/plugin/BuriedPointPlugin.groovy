package com.alonsol.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 无痕埋点插件
 * create by alonsol.yu
 */
class BuriedPointPlugin implements Plugin<Project> {


    public static final String EXTENSION_NAME = "buriedPoint"


    void apply(Project project) {

        project.logger.error("-------------------BuriedPointPlugin  start---------------------")

        project.extensions.create(EXTENSION_NAME, ExtensionConfig)
        if (project.plugins.hasPlugin(AppPlugin.class)) {
            //找到配置信息
            def extensionConfig = project.extensions.findByName(EXTENSION_NAME) as ExtensionConfig
            def classTransform = new BuriedPointTransform(project, true, extensionConfig)
            def appExtension = project.extensions.getByType(AppExtension)
            appExtension.registerTransform(classTransform)

        }
        project.logger.error("------------------BuriedPointPlugin  over----------------------")
    }


}
