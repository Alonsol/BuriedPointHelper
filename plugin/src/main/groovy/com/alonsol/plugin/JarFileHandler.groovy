package com.alonsol.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile


/**
 * jar包处理
 * create by alonsol.yu
 */
class JarFileHandler {


    static File handleJar(ClassPool pool,File jarFile,Map<String, ConfigBean> sMap,Project project) {
        def file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()
        File destFile = null
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()

            def filterName = entryName
                    .replace("\\", ".")
                    .replace("/", ".")
            //获取 类似com.sunny.file.MainActivity
            def className = filterName.replace(".class", "").substring(0)
            project.logger.error("JarFileHandler className-> $className")
            //是否要过滤这个类，这个可配置
            if (sMap.containsKey(className)) {
                println("containname ->${jarFile.getAbsolutePath()}")
                def jarName = jarFile.name.substring(0, jarFile.name.length() - ".jar".length())
                def baseDir = new StringBuilder().append(project.projectDir.absolutePath)
                        .append(File.separator).append("build")
                        .append(File.separator).append("plugin")
                        .append(File.separator).append("intermediates")
                        .append(File.separator).append(jarName).toString()

                println("baseDir ->$baseDir")
                File rootFile = new File(baseDir)
                PluginUtils.clearFile(rootFile)
                if (!rootFile.mkdirs()) {
                    println("mkdirs ${rootFile.absolutePath} failure")
                }

                File unzipDir = new File(rootFile, "classes")
                File jarDir = new File(rootFile, "jar")


                PluginUtils.unzipJarFile(file, unzipDir)
                pool.insertClassPath(unzipDir.absolutePath)

                CtClass ctClass = pool.getCtClass(className)
                //解冻
                if (ctClass.isFrozen())
                    ctClass.defrost()
                sMap.get(className).each {

                    try {
                        def methodName = ""
                        def funcName = it.sourceBean.funcName
                        if (funcName.contains("(") && funcName.contains(")")) {
                            methodName = funcName.substring(0, funcName.indexOf("("))
                        } else {
                            methodName = funcName
                        }
                        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName)
                        println("方法名 = " + ctMethod)
                        def body = ""
                        if (it.targetBean.body != null && !it.targetBean.body.isEmpty()) {
                            body = "${it.targetBean.body};"
                        } else {
                            body = """${it.targetBean.className}.${it.targetBean.funcName};"""
                        }
                        project.logger.error("type->${it.type} $body")
                        switch (it.type) {
                            case BuriedConst.TYPE_REPLACE:
                                ctMethod.setBody(body)
                                break

                            case BuriedConst.TYPE_INSET_BEFORE:
                                ctMethod.insertBefore(body)
                                break

                            case BuriedConst.TYPE_INSET_AFTER:
                                ctMethod.insertAfter(body)
                                break

                            case BuriedConst.TYPE_FIX:
                                ctMethod.insertAt(it.targetBean.line, body)
                                break
                        }


                    } catch (Exception e) {
                        project.logger.error("插桩失败 ->" + e.getMessage())
                    }
                }

                ctClass.writeFile(unzipDir.absolutePath)
                ctClass.detach()//释放
                if (PluginUtils.hasFiles(unzipDir)) {
                    println("unzipDir ->${unzipDir.absolutePath}")
                    destFile = new File(jarDir, jarFile.name)
                    println("destFile ->${destFile.absolutePath}")
                    PluginUtils.clearFile(destFile)
                    PluginUtils.zipJarFile(unzipDir, destFile)
                    PluginUtils.clearFile(unzipDir)
                } else {
                    PluginUtils.clearFile(rootFile)
                }
                project.logger.error("=========================插桩成功=======================")
            }
        }
        if (null != file) {
            file.close()
        }
        return destFile
    }

}
