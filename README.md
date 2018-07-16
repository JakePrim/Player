# PrimRoute 路由



## 组件化实现方案

### 组件化配置
1. 在项目根部新建 config.build

```
ext {
    // false 集成模式

    // true 组件模式

    isComponent = false

    androidConfig = [
            compileSdkVersion: 27,
            minSdkVersion    : 19,
            targetSdkVersion : 27,
            versionCode      : 1,
            versionName      : "1.0"
    ]

    appIdConfig = [
            app   : "com.prim.component.demo",
            moudle1: "demo.prim.com.moudle1"
    ]

    supportLibrary = "27.1.1"

    dependencies = [
            appcompatv7: "com.android.support:appcompat-v7:${supportLibrary}"
    ]
}
```
2. 主build中加入配置
```
apply from: "config.gradle"

```
3. moudle 配置

```
//配置apply
if (isComponent) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}

//获取config文件中的配置 rootProject 项目的主对象
def config = rootProject.ext.androidConfig

def appIdConfig = rootProject.ext.appIdConfig

def dependenciesConfig = rootProject.ext.dependencies

android {
    compileSdkVersion config.compileSdkVersion

    defaultConfig {
        minSdkVersion config.minSdkVersion
        targetSdkVersion config.targetSdkVersion
        versionCode config.versionCode
        versionName config.versionName

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"


        //如果moudle为组件  配置组件的app ID
        if (isComponent) {
            applicationId appIdConfig.app
        }

        //配置BuildConfig 代码中可以调用判断moudle是否为组件
        buildConfigField("boolean","isComponent",String.valueOf(isComponent))

        //配置资源文件
        sourceSets {
            main {
                if (isComponent) {//如果moudle为组件则配置 AndroidManifest 和java代码主文件
                    manifest.srcFile 'src/main/moudle/AndroidManifest.xml'
                    java.srcDirs 'src/main/java','src/main/moudle/java'
                } else {
                    manifest.srcFile 'src/main/AndroidManifest.xml'
                }
            }
        }
    }



    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation dependenciesConfig.appcompatv7
    implementation 'com.android.support.constraint:constraint-layout:+'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:+'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
}

```



4. app 配置

```
apply plugin: 'com.android.application'

def config = rootProject.ext.androidConfig

def appIdConfig = rootProject.ext.appIdConfig

def dependenciesConfig = rootProject.ext.dependencies

android {
    compileSdkVersion config.compileSdkVersion
    defaultConfig {
        applicationId appIdConfig.app
        minSdkVersion config.minSdkVersion
        targetSdkVersion config.targetSdkVersion
        versionCode config.versionCode
        versionName config.versionName
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
         //配置BuildConfig 代码中可以调用判断moudle是否为组件
        buildConfigField("boolean","isComponent",String.valueOf(isComponent))
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation dependenciesConfig.appcompatv7
    implementation 'com.android.support.constraint:constraint-layout:+'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

    if (!isComponent){//当moudle1 不为组件时才允许导入
        implementation project(':moudle1')
    }
}

```
