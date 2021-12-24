package com.framework.base.utils

/**
 * @author yu
 * @version
 * @date 2021/4/29
 * @description
 */
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.Socket

/*
Author：星辰玄光
Tips：仅适用于客户端
 */

class Connect {
    //普通数据交互接口
    private val sc: Socket? = null

//    //图片交互接口
//    private val ImageSocket: Socket? = null

    //普通交互流
    private var dout: OutputStream? = null
    private var din: InputStreamReader? = null

//    //图片交互流
//    private val imageInputStream: InputStream? = null
//    private val imageFileOutputSteam: DataOutputStream? = null

    //已连接标记
    var isConnect = false
//    var ImageConncet = false

    /**
     * 初始化普通交互连接
     */
    fun initConnect(){
        try {
            val sc:Socket = Socket(ip, port) //通过socket连接服务器
            val din = InputStreamReader(sc.getInputStream(), "gb2312")  //获取输入流并转换为StreamReader，约定编码格式
            val dout =OutputStreamWriter(sc.getOutputStream())    //获取输出流
            sc.setSoTimeout(10000)  //设置连接超时限制
            if (sc != null && din != null && dout != null) {    //判断一下是否都连上，避免NullPointException
                isConnect = true
                System.out.println("connect server successful")
            } else {
                System.out.println("connect server failed,now retry...")
                initConnect()
            }
        } catch (e: IOException) {      //获取输入输出流是可能报IOException的，所以必须try-catch
            e.printStackTrace()
        }
    }

    /**
     * 发送数据至服务器
     * @param message 要发送至服务器的字符串
     */
    fun sendMessage(message: String?) {
        var message = message
        try {
            if (isConnect) {
                if (dout != null && message != null) {        //判断输出流或者消息是否为空，为空的话会产生nullpoint错误
                    message = """
                        $message

                        """.trimIndent() //末尾加上换行让服务器端有消息返回
                    val me = message.toByteArray()
                    dout!!.write(me)
                    dout!!.flush()
                } else {
                    System.out.println("The message to be sent is empty or have no connect")
                }
                System.out.println("send message successful")
            } else {
                System.out.println("no connect to send message")
            }
        } catch (e: IOException) {
            System.out.println("send message to cilent failed")
            e.printStackTrace()
        }
    }

    fun receiveMessage(): String? {
        var message: String? = ""
        try {
            if (isConnect) {
                System.out.println("开始接收服务端信息")
                val inMessage = CharArray(1024)     //设置接受缓冲，避免接受数据过长占用过多内存
                val a = din!!.read(inMessage) //a存储返回消息的长度
                if (a <= -1) {
                    return null
                }
                System.out.println("reply length:$a")
                message = String(inMessage, 0, a) //必须要用new string来转换
                System.out.println(message)
            } else {
                System.out.println("no connect to receive message")
            }
        } catch (e: IOException) {
            System.out.println("receive message failed")
            e.printStackTrace()
        }
        return message
    }
    /**
     * 登陆方法
     * @param name 用户名
     * @param password  登陆密码
     */
    fun login(name: String?, password: String?) {
//        InitConnect()		//仅是演示的时候将初始化和关闭放在具体方法中，实际使用中需要多次重复使用Socket，所以不能这样使用
//        val job = JSONObject() //创建一个json对象存放login信息
//        job.put("name", name)   //装填数据
//        job.put("password", password)
//        job.put("login", "1")
//        job.put("type", "search")
//        var Msend: String = ""
//        Msend = job.toJSONString(); //转换为jsonstring
//        System.out.println("编码成功，编码结果为："+Msend);
//        sendMessage(Msend)  //发送消息
//        var reply: String? = ""
//        reply = receiveMessage() //接收服务器返回信息
//        /**
//         * @TODO 收到服务端返回的信息后进行相关处理
//         */
//        CloseConnect()
    }

    /**
     * 关闭连接
     */
    public fun CloseConnect() {
        try {
            if (din != null) {
                din!!.close()
            }
            if (dout != null) {
                dout!!.close()
            }
            if (sc != null) {
                sc!!.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        isConnect = false
        System.out.println("关闭连接")
    }

    companion object {
        //服务端地址及接口
        private const val ip = "127.0.0.1"  //本地服务端地址
        private const val port = 2333
    }

    fun step(){
//        步骤1：创建客户端 & 服务器的连接
//
//        // 创建Socket对象 & 指定服务端的IP及端口号
//        Socket socket = new Socket("192.168.1.32", 1989);
//
//        // 判断客户端和服务器是否连接成功
//        socket.isConnected());
//
//
//// 步骤2：客户端 & 服务器 通信
//// 通信包括：客户端 接收服务器的数据 & 发送数据 到 服务器
//
//        <-- 操作1：接收服务器的数据 -->
//
//        // 步骤1：创建输入流对象InputStream
//        InputStream is = socket.getInputStream()
//
//        // 步骤2：创建输入流读取器对象 并传入输入流对象
//        // 该对象作用：获取服务器返回的数据
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//
//        // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
//        br.readLine()；
//
//
//        <-- 操作2：发送数据 到 服务器 -->
//
//        // 步骤1：从Socket 获得输出流对象OutputStream
//        // 该对象作用：发送数据
//        OutputStream outputStream = socket.getOutputStream();
//
//        // 步骤2：写入需要发送的数据到输出流对象中
//        outputStream.write（（"Carson_Ho"+"\n"）.getBytes("utf-8")）；
//        // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
//
//        // 步骤3：发送数据到服务端
//        outputStream.flush();
//
//
//// 步骤3：断开客户端 & 服务器 连接
//
//        os.close();
//        // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
//
//        br.close();
//        // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
//
//        socket.close();
        // 最终关闭整个Socket连接


//        if (socket == null) socket = new Socket();
//        try {
//            socket.connect(new InetSocketAddress(IP, Integer.valueOf(PORT)), 5 * 1000);
//            if (socket.isConnected()) {
//                out = socket.getOutputStream();
//                InputStream in = socket.getInputStream();
//                dis = new DataInputStream(in);
//                String s = "服务器连接成功!";
//                Log.d(TAG, s);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            if (e instanceof SocketTimeoutException) {
////                    toastMsg("连接超时，正在重连");
//
//            } else if (e instanceof NoRouteToHostException) {
////                    toastMsg("该地址不存在，请检查");
//            } else if (e instanceof ConnectException) {
////                    toastMsg("连接异常或被拒绝，请检查");
//            } else if (e instanceof SocketException){
////                    if (TextUtils.equals(e.getMessage(),"already connected"))
////                        toastMsg("当前已连接，请勿再次连接");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}