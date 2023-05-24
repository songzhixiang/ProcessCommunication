## 跨进程通信的一些Demo

### AIDL

```xml

client端
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
    tools:ignore="QueryAllPackagesPermission" />
<queries>
<!-- 声明所要进行交互的应用名 -->
<package android:name="com.andysong.server" />  //服务端的包名
</queries>

<uses-permission android:name="app.service.permission"/>  //服务端定义的权限



server端

<permission android:name="app.service.permission" android:protectionLevel="normal"/> //声明权限


<service
android:permission="app.service.permission"
android:name=".MyService"
android:enabled="true"
android:exported="true">
<intent-filter>
    <action android:name="android.intent.action.MyService"/>
</intent-filter>
</service>

```

### Jar包安装
```groovy

dependencies {

    compileOnly 'androidx.appcompat:appcompat:1.4.1'
}
makeJar
def zipFile = file('build/intermediates/aar_main_jar/release/classes.jar')
task makeJar(type: Jar){
    form zipTree(zipFile)
    archiveBaseName = "sdk"
    destinationDirectory = file("build/outputs/")
    manifest {
        attributes(
                'Implementation-Title': "${project.name}",
                'Built-Data': new Date().getDateTimeString(),
                'Build-With' :
                        "gradle-${project.getGradle().getGradleVersion()},grovvy-${GroovySystem.getVersion()}",
                'Created-By' :
                        'Java ' + System.getProperty('java.version') + '(' + System.getProperty('java.vendor') + ')')
    }
}
makeJar.dependsOn(build)

```