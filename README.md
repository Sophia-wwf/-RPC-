# -RPC-

## RPC是什么？
**RPC(Remote Procedure Call)** 即远程过程调用。

## 为什么要RPC？

两个不同的服务器上的服务不在一个内存空间，所以需要通过网络编程来传递方法所需要的参数，并且方法调用的结果也需要网络编程来来接收。

而RPC可以帮助我们调用远程计算机上某个服务的方法，并且过程就像调用本地方法一样简单，同时也需要我们了解底层网络的细节。

**简而言之，就是为了让我们调用远程方法像调用本地方法一样简单。**

## RPC原理：

* 6个核心部件：

  1. **客户端（服务消费端）**：调用远程方法的一端。

  2. **客户端 Stub（桩）**：代理类——把调用方法、类、方法参数等信息传递到服务端。

    - 为什么要使用代理类？

      屏蔽细节，比如负载均衡、容错、序列化与反序列化等一系列操作。

  3. **网络传输**：将调用的方法信息传输到服务端，当服务端处理完之后，再将返回结果通过网络传输传输回来。

  4. **服务端 Stub（桩）**：不是代理类。根据客户端传来的RPC请求去找到对应的目标类和目标方法执行，然后将执行结果通过网络传输返回给客户端。

  5. **服务端（服务提供端）**：提供远程方法的一端。

## RPC工作流程：
  1. 客户端（client）以本地调用的方式调用远程服务。
  2. 客户端 Stub（client stub）接收调用，将调用信息入方法名、参数等组装成能够网络传输的消息体（序列化）。
  3. 客户端 Stub（client stub）找到远程服务的地址，将消息体发送到服务端。
  4. 服务端 Stub（桩）接收消息体，将其反序列化为Java对象。
  5. 服务端 Stub（桩）根据对象中的类、方法、方法参数等信息调用本地方法。
  6. 服务端执行本地方法，将执行结果返回给服务端 Stub（桩）
  7. 服务端 Stub（桩）接收方法执行结果，并将其封装成能够进行网络传输的消息体，发送给客户端 Stub（桩）。
  8. 客户端 Stub（桩）接收到消息体，并将其反序列化成Java对象。
  9. 完成！

## 不同部分的功能：

* **gk-proto**: 封装基础协议

    * Peer 类：表示网络传输的一个端点

        * 成员变量

          host:String 域名

          port:int 端口

    * Request 类：表示RPC的一个请求

        * 成员变量

            service:ServiceDescriptor 服务描述

            parameters:Object[] 请求参数数组

    * Response 类：表示RPC的返回结果

        * 成员变量

            code:int 服务返回编码，0 表示成功，非 0 表示失败

            message:String 具体的错误信息

            data:Object 返回的数据

    * ServiceDescriptor 类：服务描述

        * 成员变量

            clazz:String 需要调用的类的 Class 对象

            method:String 需要调用的方法名

            returnType:String 返回结果类型

            parameterTypes:String[] 请求参数类型数组

        - 方法

            public static ServiceDescriptor from(Class clazz, Method method) 根据方法参数初始化一个 ServiceDescriptor 对象，最后返回该对象。

                clazz:Class 需要调用的类的 Class 对象

                method:Method 需要调用的方法名

            public boolean equals(Object o) 重写equal()方法：是否和当前类的实例相等

            public int hashCode() 重写 hashCode() 方法

            public String toString() 重写 toString() 方法

* **gk-rpc-transport**：封装网络服务

    * RequestHandler 接口：封装对请求的处理

        - 方法

            void onRequest(InputStream recive, OutputStream toResp); 处理请求

    * TransportClient 接口：封装客户端对于传输进行的操作。

        - 方法

            void connect(Peer peer); 连接服务端

            InputStream write(InputStream data); 将数据写入输入流

            void close(); 关闭

    * TransportServer 接口：封装服务端

        - 方法

            void init(int port, RequestHandler handler); 初始化端口号，以及请求处理对象 RequestHandler handler

            void start(); 开启服务

            void stop(); 暂停服务

    * HTTPTransportClient 类：实现 TransportClient 接口，封装客户端

        - 成员变量

            url:String 连接的服务端url

        - 方法

            public void connect(Peer peer) 对 url 进行初始化

            public InputStream write(InputStream data)

                1. 创建 HttpURLConnection 对象 httpConn 并初始化。

                2. httpConn.connect(); 连接服务端。

                3. IOUtils.copy(data, httpConn.getOutputStream()); 将数据写入到输出流。

                4. 获取响应码，判断是否传输成功。

                5. 如果传输成功，返回输入流，否则返回错误流。

            public void close()


    * HTTPTransportServer 类：实现 TransportServer 接口

        - 成员变量

            handler:RequestHandler 请求处理对象

            server:Server 服务端对象

        - 方法

            public void init(int port, RequestHandler handler)

                1. 初始化 handler, server

                2. 启动监听：servlet 接收请求

            public void start()

                1. 启动服务端
                2. server.join(); 调用的jetty的线程池

            public void stop() 暂停服务端

            - 内部类：RequestServlet 继承了 HttpServlet，内部封装了输入流输出流，以及对输入输出流的处理。

* **gk-rpc-common**：封装工具类

    * ReflectionUtils 类：反射

        public static <T> T newInstance(Class<T> clazz) 根据class创建对象

            clazz 待创建对象的类

            <T> 对象类型

            return 创建好的对象

        public static Method[] getPublicMethods(Class clazz) 获取某个class的共有方法 —— 用 public 修饰的成员变量和方法称为共有变量和共有方法。

        public static Object invoke(Object obj,
                                Method method,
                                Object... args) 调用指定对象的指定方法

* **gk-rpc-codec**：序列化与反序列化包

    * Encoder 接口：序列化接口

        byte[] encode(Object obj);

    * Decoder 接口：反序列化接口

        <T> T decode(byte[] bytes, Class<T> clazz);

    * JSONEncoder 类：实现 Encoder 接口

        public byte[] encode(Object obj) 对传入的 obj 对象进行序列化，转为二进制数组，最后返回序列化后的结果。

    * JSONDecoder 类：实现 Decoder 接口

        public <T> T decode(byte[] bytes, Class<T> clazz) 对序列化结果，转化为 clazz 对象的对象，返回对象。

* **gk-rpc-server**：封装服务端

     * RpcServerConfig 类：定义 server 配置

         - 成员变量

            transportClass:Class<? extends TransportServer> 初始化为 HTTPTransportServer 的 class 对象

            encoderClass:Class<? extends Encoder> 初始化为 JSONEncoder 的 class 对象

            decoderClass:Class<? extends Decoder> 初始化为 JSONDecoder 的 class 对象

            port:int 端口号

   * ServiceInstance 类：表示一个具体的服务，封装了需要调用服务的具体信息

          - 成员变量

              target:Object 调用服务的类对象

              method:Method 调用服务的方法

      * ServiceInvoker 类：封装调用具体的服务的工具，服务实例

          - 方法

          public Object invoke(ServiceInstance service,
                     Request request) 通过 request 的 ServiceDescriptor 找到服务的实例，通过反射调用方法，传入参数

      * ServiceManager 类：管理 RPC 所暴露的服务

          - 成员变量

              services:Map<ServiceDescriptor, ServiceInstance> 封装调用服务所需要的 ServiceDescriptor 和 ServiceInstance。将服务描述，服务实例作为 key-value 存储，便于客户端传来时，能够找到准确的实例，调用正确的方法。

                   ServiceDescriptor ：服务描述

                      > clazz:String 需要调用的类的 Class 对象
                      > method:String 需要调用的方法名
                      > returnType:String 返回结果类型
                      > parameterTypes:String[] 请求参数类型数组

                   ServiceInstance ：服务实例

                      > target:Object 调用服务的类对象
                      > method:Method 调用服务的方法


          - 方法

              public ServiceManager() 构造方法，初始化this.services = new ConcurrentHashMap<>();

              public <T> void register(Class<T> interfaceClass, T bean) 服务注册

                  1. 获取 interfaceClass 的公共方法数组。

                  2. 遍历数组中的公共方法，每次创建并初始化一个 ServiceDescriptor 对象以及一个 ServiceInstance 对象注册进 services 中。

              public ServiceInstance lookup(Request request) 查找服务

                  1. 通过 request 获取 ServiceDescriptor 服务描述对象。

                  2. 利用 services 获取对应的服务实例。

      * RpcService 类：封装服务端

          - 成员变量

              config:RpcServerConfig RPC配置信息

              net:TransportServer 服务器网络服务

              encoder:Encoder 序列化

              decoder:Decoder 反序列化

              serviceManager:ServiceManager 服务管理

              serviceInvoker:ServiceInvoker 服务实例

              handler:RequestHandler 请求处理器

                  1. 同时实现 RequestHandler 接口，实现其中的 onRequest() 方法。读取输入流中的二进制数组，对其进行反序列化，转换成 request 对象。

                  2. serviceManager.lookup(request) 获得服务实例，再利用 serviceInvoker 调用 invoke() 方法，获取执行服务后获得的返回结果。

                  3. 将结果写入到响应对象 resp 中。

                  4. 最后将响应对象序列化，转换为二进制写入输出流，方便后续进行网络传输。


          - 方法

              public RpcServer(RpcServerConfig config) 构造函数，初始化成员变量

              public <T> void register(Class<T> interfaceClass, T bean) 利用 serviceManager 调用该服务注册方法

              public void start() 启动 Jetty

              public void stop() 暂停 Jetty

* **gk-rpc-client**：封装客户端

    * TransportSelector 接口：选择哪个 server 连接

        init(List<Peer> peers,
              int count,
              Class<? extends TransportClient> clazz); 初始化 selector

              peers 可以连接的 server 端点信息

              count client 与 server 建立多少个连接

              clazz client 实现 class

        TransportClient select(); 选择一个 transport 与 server 做交互，返回一个 TransportClient

        void release(TransportClient client); 释放用完的 client

        void close();

    * RandomTransportSelector 类：随机选择一个 server 进行连接，实现 TransportSelector 接口

        - 成员变量

            clients:List<TransportClient> 已经连接好的 client

        - 方法（由于 List 用 ArrayList 实现，线程不安全，所以除构造方法外其他方法加 synchronized

            public RandomTransportSelector() 构造方法，初始化 clients

            public synchronized void init(List<Peer> peers,
                     int count,
                     Class<? extends TransportClient> clazz) 初始化

                     1. 连接数初始化，最小为 1

                     2. peers 初始化，通过 ReflectionUtils 工具类根据 clazz 创建 TransportClient 对象

                     3. TransportClient 对象连接好后，再加入 clients

            public synchronized TransportClient select() 从 clients 随机取出一个 client 用于连接

            public synchronized void release(TransportClient client) 使用完的 client 放回 clients

            public synchronized void close() 关闭 client 的连接，清除 clients 缓存

    * RpcClientConfig 类：RpcClient配置

        - 成员变量

            transportClass:Class<? extends TransportClient> 用于连接client对象

            encoderClass:Class<? extends Encoder> 序列化对象

            decoderClass:Class<? extends Decoder> 反序列化对象

            selectorClass:Class<? extends TransportSelector> server选择器对象

            connectCount:int 连接数

            servers:List<Peer> 存放监听端口的链表

    * RemoteInvoker 类：调用远程服务的代理类，实现了 InvocationHandler 接口

        - 成员变量

            clazz:Class 远程服务的Class对象

            encoder:Encoder 序列化对象

            decoder:Decoder 反序列化对象

            selector:TransportSelector 选择器对象

        - 方法

            public RemoteInvoker(Class clazz,
                  Encoder encoder,
                  Decoder decoder,
                  TransportSelector selector) 初始化

            public Object invoke(Object proxy,
                   Method method,
                   Object[] args) 调用远程服务的方法

                   1. 创建 Request 对象 request

                   2. 设置 request 采用的服务描述以及方法参数

                   3. 创建 Response 对象 resp

                   4. 通过 request 中的服务调用信息，调用远程方法，返回结果赋值给 resp

                   5. 返回 resp 中的数据

            private Response invokeRemote(Request request) 调用远程服务

                1. 通过选择器获取已经连接好的 client

                2. 将 request 序列化，再写入到输入流

                3. 从输出流中读取执行完的二进制代码，反序列化为 resp 对象

                4. 最后，释放 client，并返回 resp 对象

    * RpcClient 类：

        - 成员变量

            config:RpcClientConfig 配置信息

            encoder:Encoder 序列化

            decoder:Decoder 反序列化

            selector:TransportSelector 选择器

        - 方法

            public RpcClient() 无参构造方法

            public RpcClient(RpcClientConfig config) 有参构造方法

            public <T> T  getProxy(Class<T> clazz) 获取动态代理

                
