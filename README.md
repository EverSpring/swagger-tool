# swagger-tool

#### 介绍
springfox-swagger工具，目的是减少swagger注解生成。当前只支持IDEA
1、把model中的/**xxx*/注解转换成@ApiModelProperty("xxx")

#### 安装教程
##### 方法一：在release中下载
下载地址：https://gitee.com/EverSpring007/swagger-tool/releases/v1.1
##### 方法二：自己编译生成jar文件
1.  导入IDEA
2.  ![输入图片说明](https://images.gitee.com/uploads/images/2020/1112/144700_cf1efea6_68525.png "屏幕截图.png")
3.  将打包后jar以本地插件方式安装
![输入图片说明](https://images.gitee.com/uploads/images/2020/1112/144908_31814aac_68525.png "屏幕截图.png")

#### 使用方式
右键->“转换swagger"即可

#### 下版功能
字段没有注释的，根据字段名翻译生成注解