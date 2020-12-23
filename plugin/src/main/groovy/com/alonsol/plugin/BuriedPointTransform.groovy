package com.alonsol.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.util.stream.Stream


/**
 * 无痕埋点插件
 * create by alonsol.yu
 */
class BuriedPointTransform extends Transform {

    private Project project
    private ClassPool pool
    private boolean isApplication
    private Map<String, List<ConfigBean>> sMap
    private ExtensionConfig extensionConfig

    BuriedPointTransform(Project project, boolean isApplication, ExtensionConfig extensionConfig) {
        this.project = project
        this.isApplication = isApplication
        this.extensionConfig = extensionConfig
    }


    void configMapping(Map<String, List<ConfigBean>> map) {
        this.sMap = map
    }

    @Override
    String getName() {
        return "PerformanceTransform1"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return isApplication ?
                TransformManager.SCOPE_FULL_PROJECT
                : Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        def start = System.currentTimeMillis()
        pool = new ClassPool(true)
        project.android.bootClasspath.each { it ->
            pool.appendClassPath(it.absolutePath)
        }


        def dirStream = transformInvocation.referencedInputs
                .parallelStream()
                .flatMap { it.directoryInputs.parallelStream() }
                .filter { it.file.exists() }

        def jarStream = transformInvocation.referencedInputs
                .parallelStream()
                .flatMap { it.jarInputs.parallelStream() }
                .filter { it.file.exists() }

        Stream.concat(dirStream, jarStream).forEach {
            pool.appendClassPath(it.file)
        }
        project.logger.error("extensionConfig.enable ->${extensionConfig.enable}")
        if (extensionConfig.enable) {
            FileParser.parseXml(project, this, extensionConfig)
        }


        def outProvider = transformInvocation.getOutputProvider()
        def list = new ArrayList<JarInput>()
        transformInvocation.inputs.stream().each { input ->
            input.directoryInputs.each { it ->
                processDirectoryInput(it, outProvider)
            }

            input.jarInputs.each { it ->
                list.add(it)
            }
        }

        list.each {
            processJarInput(it, outProvider)
        }

        sMap?.clear()

        project.logger.error("================插件耗时=============${System.currentTimeMillis() - start}")
    }


    /**
     * 处理文件
     * @param input
     * @param outputProvider
     */
    private void processDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        project.logger.error("processDirectoryInput extensionConfig.enable : ${extensionConfig.enable} ")
        if (extensionConfig.enable) {
            try {
                DirFileHandler.inject(pool, sMap, directoryInput.getFile(), directoryInput.getFile().getAbsolutePath(), project)
            } catch (Exception e) {
                println(e.getMessage())
            }
        }
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.getFile(), dest);


    }

    /**
     * 处理jar包
     * @param jarInput
     * @param outputProvider
     */
    private void processJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        String name = jarInput.getName()
        String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath())
        if (name.endsWith(".jar")) {
            name = name.substring(0, name.length() - 4)
        }
        File dest = outputProvider.getContentLocation(name + md5Name, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR)
        project.logger.error("processJarInput extensionConfig.enable : ${extensionConfig.enable} ")
        if (extensionConfig.enable) {
            def fixFile = JarFileHandler.handleJar(pool, jarInput.file, sMap, project)

            if (fixFile != null && fixFile.exists()) {
                FileUtils.copyFile(fixFile, dest)
                fixFile.delete()
            } else {
                FileUtils.copyFile(jarInput.getFile(), dest)
            }
        } else {
            FileUtils.copyFile(jarInput.getFile(), dest)
        }
    }


}