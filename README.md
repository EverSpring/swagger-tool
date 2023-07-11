# 别被标题误导，综合性工具

#### 介绍
swagger生成、一键生成对象的set方法、一键copy完整的restful地址。
刚开始只定义为springfox-swagger工具，目的是减少swagger注解生成。当前只支持IDEA。本来打算直接修改springfox源码，但是编译后的class文件没有注释，要实现需要把
源码打进包里，这种方式个人觉得不妥，所以改为了自动生成@ApiModelProperty的方案  

#### 功能
1、 **把model中的/**xxx*/注解转换成@ApiModelProperty("xxx") ，可以自定义转换词组，也可以使用翻译**  
![输入图片说明](https://images.gitee.com/uploads/images/2020/1223/233320_9979316e_68525.gif "change.gif")  
2、 **一键生成对象的set方法**   
![输入图片说明](https://images.gitee.com/uploads/images/2020/1229/101725_e78f5e1d_68525.gif "set.gif")  
3、 **一键copy完整的restful地址，支持SpringMvc和Feign**  
![输入图片说明](https://images.gitee.com/uploads/images/2021/0223/104520_2097e9eb_68525.png "屏幕截图.png")

#### 安装教程
##### 方法一：在IDEA marketplace中搜索swagger-annotation-tool安装。IDEA-->settings-->Marketplace中搜索swagger-annotation-tool
##### 方法二：在release中下载
下载地址：https://gitee.com/EverSpring007/swagger-tool/releases
##### 方法三：自己编译生成jar文件
1.  导入IDEA
2.  ![输入图片说明](https://images.gitee.com/uploads/images/2020/1112/144700_cf1efea6_68525.png "屏幕截图.png")
3.  将打包后jar以本地插件方式安装
![输入图片说明](https://images.gitee.com/uploads/images/2020/1112/144908_31814aac_68525.png "屏幕截图.png")

#### 使用方式
1. 右键->“转换swagger"，选中需要的转换方式即可。
**转换翻译：**在没有/** xx */注释的情况下，根据字段名翻译后生成，如果有注释则不会自动翻译
2. Alt+Insert，生成对象的set方法
3、Alt+Shift+U，拷贝完整restful接口地址

#### 感谢
感谢easy-javadoc作者[wangchao]，翻译功能借用了他部分代码(https://gitee.com/starcwang/easy_javadoc.git)
