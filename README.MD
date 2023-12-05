#  android 集成

## 分支 
master 集成方案
flutter 自用于调试
## 注意

高版本集成方案参考

https://github.com/flutter/flutter/issues/99735


降级7.4.2 语法修改

app/build.gradle

```
    sourceSets {
        main {
            res {
                excludes += '/META-INF/{AL2.0,LGPL2.1}'
            }
        }
    }
//    packaging {
//        resources {
//            excludes += '/META-INF/{AL2.0,LGPL2.1}'
//        }
//    }
```