需求：为了获取端口采用代理，启动前修改代码切入获取端口，然后把信息封装注册MXBean

前置条件：
  需要把springboot-agent-premain.jar 放到配置目录的jarManage/agent/premain目录下
  然后在每个需要获取端口的jar管理编辑页面的jvm参数添加 -javaagent:springboot-agent-premain.jar路径

例子
-javaagent:D:/jarManage/agent/premain/springboot-agent-premain.jar

逻辑、处理过程：
   由代理包（springboot-agent-premain.jar）处理，获取ip端口注册ServerIpPortObject对象(MXBean)，但是存在两种处理方式
   注：通过premain方法实现，类似AOP,启动前对加载的类操作。
   第一种：-javaagent配置时候添加参数追加端口
          如-javaagent:D:/jarManage/agent/premain/springboot-agent-premain.jar=9095（注：这个端口是启动jar对应的端口，不是本系统，即启动就知道端口）
          此时，代理包直接获取端口号，注册ServerIpPortObject对象到JMX上，然后本系统通过连接对应JMX获取该对象信息。
   第二种：-javaagent配置时候没有参数，即-javaagent:D:/jarManage/agent/premain/springboot-agent-premain.jar。
          此时会走另外一个逻辑，通过Instrumentation对象的appendToBootstrapClassLoaderSearch先把springboot-listener-support.jar包添加到类加载器，
          然后调用addTransformer找到spring的FileEncodingApplicationListener.clas，把本地的FileEncodingApplicationListener.class替换上去。
          此时，代理包的工作处理完毕。在springboot启动时候会调用FileEncodingApplicationListener监听器，从而执行里面修改到的逻辑，注册ServerIpPortObject对象。
   注册ServerIpPortObject成功后，Jar管理系统可以通过每个jar对应的pid，由JMX远程获取该对象，获取对应的ip、port信息。