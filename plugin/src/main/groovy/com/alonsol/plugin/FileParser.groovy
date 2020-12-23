package com.alonsol.plugin

import org.gradle.api.Project

/**
 * 配置文件扫描工具类
 * create by alonsol.yu
 */
class FileParser {

    public static final ARRAY_SIZE = 2

    /**
     * 解析xml配置文件
     * @param project
     * @param transform
     */
    protected static void parseXml(Project project, BuriedPointTransform transform ,ExtensionConfig extensionConfig) {


        def parser = new XmlParser().parse(extensionConfig.mapping)
        Map<String, List<ConfigBean>> sMap = new HashMap<>()
        /**
         * 替换到方法体前面
         */
        parser.before.each {
            // source -> 被替换的源码
            assembleData(it, BuriedConst.TYPE_INSET_BEFORE, project, sMap)
        }

        /**
         * 替换到方法体后面
         */
        parser.after.each {
            assembleData(it, BuriedConst.TYPE_INSET_AFTER, project, sMap)
        }

        /**
         * 替换方法提
         */
        parser.replace.each {
            assembleData(it, BuriedConst.TYPE_REPLACE, project, sMap)
        }

        parser.fix.each {
            assembleData(it, BuriedConst.TYPE_FIX, project, sMap)
        }
        transform.configMapping(sMap)

    }

    /**
     * 组装数据
     * @param project
     * @param transform
     * @param type
     */
    private static void assembleData(it, int type, Project project, Map<String, List<ConfigBean>> sMap) {
        def source = (it.source[0].text() as String).trim()
        //target -> 替换的代码
        def target = (it.target[0].text() as String).trim()
        if (!source.isEmpty() && !target.isEmpty()) {

            def sourceBean = new SourceBean()
            def sourceArray = source.trim().replaceAll("\\s{1,}", " ").split(' ')
            project.logger.error("sourceArray -->$sourceArray  size ->${sourceArray.size()}")

            if (sourceArray.size() == ARRAY_SIZE && !sourceArray[0].isEmpty() && !sourceArray[1].isEmpty()) {
                sourceBean.className = sourceArray[0]
                sourceBean.funcName = sourceArray[1]
                def targetArray = target.replaceAll("\\s{1,}", " ").split(' ')
                project.logger.error("targetArray -->$targetArray  size ->${targetArray.size()}")
                def targetBean = new TargetBean()
                if (targetArray.size() == ARRAY_SIZE && !targetArray[0].isEmpty() && !targetArray[1].isEmpty()) {
                    targetBean.className = targetArray[0]
                    targetBean.funcName = targetArray[1]
                } else {
                    targetBean.body = target
                }
                def configBean = new ConfigBean()
                configBean.sourceBean = sourceBean
                configBean.targetBean = targetBean
                configBean.type = type
                if (sMap.containsKey(sourceBean.className)) {
                    sMap.get(sourceBean.className).add(configBean)
                } else {
                    def list = new ArrayList<ConfigBean>()
                    list.add(configBean)
                    sMap.put(sourceBean.className, list)
                }

                project.logger.error(""" type : $type
                       ${sourceBean.className} ${sourceBean.funcName}
                       ${targetBean.className} ${targetBean.funcName} ${targetBean.body}
                    """)
            }

        }
    }

}