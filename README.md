# BuriedPointHelper
android真正的无痕埋点方案，通过配置XML的形式在工程代码中进行字节码插桩，将埋点跟业务代码进行隔离，配置方便，还可以修复第三方sdk


# 特性
 1. 支持xml脚本配置，有before、after、fix三种标签，分表代表在方法前、后以及整体替换方法体三种方式


### 1.初始化悬浮窗控件
``` kotlin
        <plugin>

    <fix>
        <source>com.example.performancehelper.MainActivity initEvent()</source>
        <target>{boolean b = true;
            if (b) {
                    android.widget.Toast.makeText(this,"我被修复了！！！", android.widget.Toast.LENGTH_LONG).show();
                }
            }</target>
    </fix>

    <fix>
        <source>com.lalamove.lib.MyClass test()</source>
        <target>{boolean b = true;
            if (b) {
            System.out.println("111111");
            }
            }</target>
    </fix>

    <before>
        <source>com.example.performancehelper.MainActivity onCreate()</source>
        <target>com.example.performancehelper.BuryingPoint testBuryingPoint1()</target>
    </before>

    <before>
        <source>com.example.performancehelper.MainActivity initData()</source>
        <target>
            com.example.performancehelper.BuryingPoint testBuryingPoint2($1)
        </target>
    </before>

    <after>
        <source>com.example.performancehelper.MainActivity insertClickPoint()</source>
        <target>
            android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show()
        </target>
    </after>

</plugin>
```


### 结语
    代码我就不提供仓库远程依赖，你可以自己修改后上传
