package com.alonsol.plugin

import groovy.io.FileType
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project


/**
 * class文件处理
 * create by alonsol.yu
 */
class DirFileHandler {

    static void inject(ClassPool pool, Map<String, ConfigBean> sMap, File file0, String originFilepath, Project project) {
        pool.insertClassPath(originFilepath)
        pool.importPackage("android.os.Bundle")

        if (file0.isDirectory()) {

            file0.eachFileRecurse(FileType.FILES) { File file ->
                String filePath = file.absolutePath
                def className = filePath.replace(originFilepath, "")
                        .replace("\\", ".")
                        .replace("/", ".")
                def name = className.replace(".class", "").substring(1)

                project.logger.error("DirFileHandler className-> $name")
                if (checkFile(file) && sMap.containsKey(name)) {

                    CtClass ctClass = pool.getCtClass(name)
                    println("ctClass = " + ctClass)
                    if (ctClass.isFrozen())
                        ctClass.defrost()

                    sMap.get(name).each {
                        println("after filePath = " + filePath)

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
                            if (it.type == BuriedConst.TYPE_FIX) {
                                if (it.targetBean.body != null && !it.targetBean.body.isEmpty()) {
                                    body = "${it.targetBean.body}"
                                } else {
                                    body = """${it.targetBean.className}.${it.targetBean.funcName};"""
                                }
                            } else {

                                if (it.targetBean.body != null && !it.targetBean.body.isEmpty()) {
                                    body = "${it.targetBean.body};"
                                } else {
                                    body = """${it.targetBean.className}.${it.targetBean.funcName};"""
                                }
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
                                    ctMethod.setBody(body)
                                    break
                            }
                        } catch (Exception e) {
                            project.logger.error("插桩失败 ->" + e.getMessage())
                        }
                    }

                    ctClass.writeFile(originFilepath)
                    ctClass.detach()//释放
                    project.logger.error("=========================插桩成功=======================")
                }
            }
        }

    }


    /**
     * 检查是否是文件
     * @param file class文件
     * @return
     */
    private static boolean checkFile(File file) {
        if (file.isDirectory()) {
            return false
        }
        def filePath = file.path

        def flag = !filePath.contains('R$') &&
                !filePath.contains('R.class') &&
                !filePath.contains('BuildConfig.class')
        return flag
    }
}