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