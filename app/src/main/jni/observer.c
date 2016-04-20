
#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>

#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/inotify.h>
#include <stdlib.h>

#include "GUNetDef.h"
#include "Communicator.h"


#include <errno.h>



//#include "Communicator.h"
#include "GUNetDef.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <netdb.h>

#ifdef WIN32
#include <windows.h>
    #include <io.h>
    #include <winsock.h>
    #include <process.h>
#else
#include <unistd.h>
#include <sys/time.h>
#include <pthread.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>
#include <fcntl.h>
#endif

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "fred", __VA_ARGS__)


int Connect(int *sock, const char *address, unsigned short port)
{
    int _sk = socket(AF_INET, SOCK_STREAM, 0);
    if (_sk == INVALID_SOCKET)
        return NET_SOCKET_ERROR;

    struct sockaddr_in sockAddr;
    memset(&sockAddr, 0, sizeof(sockAddr));

    sockAddr.sin_family	= AF_INET;
    sockAddr.sin_addr.s_addr = inet_addr(address);
    sockAddr.sin_port = htons(port);

    if (sockAddr.sin_addr.s_addr == INADDR_NONE)
    {
        struct hostent *host = gethostbyname(address);
        if (host == NULL)
        {
            return NET_SOCKET_ERROR;
        }
        sockAddr.sin_addr.s_addr = ((struct in_addr*)host->h_addr)->s_addr;
    }

    //unsigned long ul = 1;
    //ioctlsocket(_sk, FIONBIO, &ul);
    //connect(_sk, (SOCKADDR *)&sockAddr, sizeof(SOCKADDR));
    fcntl (_sk, F_SETFL, O_NONBLOCK | fcntl (_sk, F_GETFL)); // 设置成非阻塞
    int ret = connect(_sk, (struct sockaddr*)&sockAddr, sizeof(struct sockaddr));

    fd_set fdset;
    struct timeval tmv;
    FD_ZERO(&fdset);
    FD_SET(_sk, &fdset);
    tmv.tv_sec = 15; // 设置超时时间
    tmv.tv_usec = 0;

    // 和windows下区别：
    // 第一个参数是一个整数值，是指集合中所有文件描述符的范围，即所有文件描述符的最大值加1，不能错！
    // 在Windows中这个参数的值无所谓，可以设置不正确。
    ret = select(_sk+1, 0, &fdset, 0, &tmv);
    if (ret == 0){
        return NET_CONNNECT_TIMEOUT;
    }
    else if(ret < 0){
        return NET_SOCKET_ERROR;
    }

//	ul = 0;
//	ioctlsocket(_sk, FIONBIO, &ul);
    int flags = fcntl(_sk, F_GETFL,0);
    flags &= ~ O_NONBLOCK;
    fcntl(_sk,F_SETFL, flags); // 设置成阻塞
    *sock = _sk;
    return SUCCESS;
}

int SendData(int _sk, const UInt8 *buffer, int bufferSize)
{
    int ret = send(_sk, buffer, bufferSize, 0);
    if (ret != bufferSize)
        return NET_SOCKET_ERROR;

    return SUCCESS;
}

int RecvLength(int _sk, UInt8 *buffer, int bufferLength, int needRecvLength)
{
    int nHasRecvLen = 0;
    while (nHasRecvLen < needRecvLength) {
        fd_set fdset;
        struct timeval tmv;
        FD_ZERO(&fdset);
        FD_SET(_sk, &fdset);
        tmv.tv_sec = 6; // 设定超时时间
        tmv.tv_usec = 0;

        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
        if (nRet == 0)
            return NET_RECV_TIMEOUT;
        else if(nRet < 0)
            return NET_SOCKET_ERROR;

        int nRecvLen = recv(_sk, buffer+nHasRecvLen, needRecvLength-nHasRecvLen, 0);
        if (nRecvLen <= 0)
            return NET_SOCKET_ERROR;

        nHasRecvLen += nRecvLen;
    }

    return SUCCESS;
}

#if 0
int RecvData(int _sk, UInt8* buffer, int bufferLength, int& dataLength)
{
    // 先接收头，再接收数据
    t_net_header header;
    int nHeaderSize = sizeof(t_net_header);
    int nHasRecvLen = 0;
    while (nHasRecvLen < nHeaderSize) {
        fd_set fdset;
		timeval tmv;
		FD_ZERO(&fdset);
		FD_SET(_sk, &fdset);
		tmv.tv_sec = 3; // 设定超时时间
		tmv.tv_usec = 0;

        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
		if (nRet == 0)
			return NET_RECV_TIMEOUT;
		else if(nRet < 0)
			return NET_SOCKET_ERROR;

        int nRecvLen = recv(_sk, &header+nHasRecvLen, nHeaderSize-nHasRecvLen, 0);
		if (nRecvLen <= 0)
			return NET_SOCKET_ERROR;

        nHasRecvLen += nRecvLen;
    }

    // 校验
    if (header.head4[0] != NET_Header_ID) {
        return NET_DATA_ERROR;
    }

    // 转化为小端
    header.dataLen = ntohl(header.dataLen);

    // 接收数据
    int nDataLen = header.dataLen + 8; // 最后有8个字节的crc
    nHasRecvLen = 0;
    while (nHasRecvLen < nDataLen) {
        fd_set fdset;
		timeval tmv;
		FD_ZERO(&fdset);
		FD_SET(_sk, &fdset);
		tmv.tv_sec = 3; // 设定超时时间
		tmv.tv_usec = 0;

        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
		if (nRet == 0)
			return NET_RECV_TIMEOUT;
		else if(nRet < 0)
			return NET_SOCKET_ERROR;

        int nRecvLen = recv(_sk, buffer+nHasRecvLen, nDataLen-nHasRecvLen, 0);
		if (nRecvLen <= 0)
			return NET_SOCKET_ERROR;

        nHasRecvLen += nRecvLen;
    }

    dataLength = nDataLen;
    return SUCCESS;
}

int RecvAliveData(int _sk, UInt8* buffer, int bufferLength, int& dataLength)
{
    fd_set fdset;
    timeval tmv;
    FD_ZERO(&fdset);
    FD_SET(_sk, &fdset);
    tmv.tv_sec = 3; // 设定超时时间
    tmv.tv_usec = 0;

    int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
    if (nRet == 0)
        return NET_RECV_TIMEOUT;
    else if(nRet < 0)
        return NET_SOCKET_ERROR;

    int nRecvLen = recv(_sk, buffer, 1, 0);
    if (nRecvLen <= 0)
        return NET_SOCKET_ERROR;

    dataLength = 1;
    return SUCCESS;
}
#endif

int DisConnect(int _sk)
{
    if (_sk != INVALID_SOCKET)
    {
        close(_sk);
        _sk = INVALID_SOCKET;
    }
    return SUCCESS;
}

int RecvHttpData(int _sk, UInt8 *buffer, int bufferSize, int *recvSize)
{

    // 1. 接收HTTP包头
    const int httpHead = 4096;
    char httpheadBuff[4096] = {0};

    int recvHeadLen = 0;
    int recvDataLen = 0;
    int dataLen = 0;
    bool recvHttpheadOK = false;


    while(recvHeadLen < httpHead)
    {
        fd_set fdset;
        struct timeval tmv;
        FD_ZERO(&fdset);
        FD_SET(_sk, &fdset);
        tmv.tv_sec = 3; // 设定超时时间
        tmv.tv_usec = 0;

        int ret = select(_sk+1, &fdset, 0, 0, &tmv);
        if (ret == 0)
            return NET_RECV_TIMEOUT;
        else if(ret < 0)
            return NET_SOCKET_ERROR;

        int rlen = recv(_sk, httpheadBuff+recvHeadLen, httpHead-recvHeadLen, 0);
        LOGI("%s", httpheadBuff);
        if (rlen <= 0)
            return NET_SOCKET_ERROR;

        recvHeadLen += rlen;

        char* headEnd = strstr(httpheadBuff, "\r\n\r\n"); // 查找数据开始（头结束）位置
        if (headEnd != NULL)
        {
            headEnd += 4;
            char* start = strstr(httpheadBuff, "Content-Length:"); // 数据部分长度
            if (start == NULL)
                return NET_COMMUNICATOR_ERROR;

            start += 15;
            char* end = strstr(start, "\r\n");
            if (end == NULL)
                return NET_COMMUNICATOR_ERROR;

            // 求数据长度
            char contentLen[64] = {0};
            memcpy(contentLen, start, end - start);
            dataLen = atoi(contentLen);
            LOGI("数据长度 %d\n", dataLen);

            recvDataLen = recvHeadLen - (int)(headEnd - httpheadBuff); // 计算已接收的数据长度
            if (recvDataLen >= dataLen) // 已包含完整数据
            {
                memcpy(buffer, headEnd, dataLen);
                buffer[dataLen] = '\0';
                *recvSize = recvDataLen;

                // 打印头信息
                httpheadBuff[recvHeadLen - recvDataLen] = '\0';
                LOGI("%s\n", httpheadBuff);

                return SUCCESS;
            }
            if (recvDataLen > 0)
            {
                memcpy(buffer, headEnd, recvDataLen); // 拷贝已接收的数据
            }

            recvHttpheadOK = true;
            break; // http 头接收完毕
        }
    }

    // 打印头信息
    httpheadBuff[recvHeadLen - recvDataLen] = '\0';
    LOGI("%s\n", httpheadBuff);

    if (!recvHttpheadOK)
        return NET_COMMUNICATOR_ERROR;

    // 2. 接收数据
    while(recvDataLen < dataLen)
    {
        fd_set fdset;
        struct timeval tmv;

        FD_ZERO(&fdset);
        FD_SET(_sk, &fdset);

        tmv.tv_sec = 3;
        tmv.tv_usec = 0;

        int ret = select(_sk+1, &fdset, 0, 0, &tmv);
        if (ret == 0)
            return NET_RECV_TIMEOUT;
        else if(ret < 0)
            return NET_SOCKET_ERROR;

        ret = recv(_sk, buffer + recvDataLen, dataLen - recvDataLen, 0);
        if (ret <= 0)
            return NET_SOCKET_ERROR;

        recvDataLen += ret;
    }
    buffer[recvDataLen] = '\0';
    *recvSize = recvDataLen;
    return SUCCESS;
}









/* 宏定义begin */
//清0宏
#define MEM_ZERO(pDest, destSize) memset(pDest, 0, destSize)

//LOG宏定义
#define LOG_INFO(tag, msg) __android_log_write(ANDROID_LOG_INFO, tag, msg)
#define LOG_DEBUG(tag, msg) __android_log_print(ANDROID_LOG_INFO, tag, msg)
#define LOG_WARN(tag, msg) __android_log_write(ANDROID_LOG_WARN, tag, msg)
#define LOG_ERROR(tag, msg) __android_log_write(ANDROID_LOG_ERROR, tag, msg)

/* 内全局变量begin */
static char c_TAG[] = "onEvent";
static jboolean b_IS_COPY = JNI_TRUE;

#define MAXLINE 1024

#define SERVPORT 3333
#define MAXDATASIZE 80



void testHttp(){

//    signal(SIGPIPE,SIG_IGN);

    int sk = INVALID_SOCKET;
    int ret = Connect(&sk, "www.freddon.com", 80);
    LOGI("::::%d",sk);
    if (ret!=SUCCESS)
        return;

    char *reqHead = "GET /app.html HTTP/1.1\r\n"
            "Host: www.freddon.com\r\n\r\n";
    ret = SendData(sk, reqHead, strlen(reqHead));
    LOGI("SendData TRACE");
    if (ret!=SUCCESS){
        LOGI("send data error");
        return;
    }
    char buff[81920]={0};
    int recvLength = 0;
    ret = RecvHttpData(sk, buff, 81920, &recvLength);
    LOGI("RecvHttpData TRACE");
    if(ret){
        LOGI("::DATA::%s",buff);

    }


}

void request(char *host,int port, char *reqHead){

//    signal(SIGPIPE,SIG_IGN);

    int sk = INVALID_SOCKET;
    int ret = Connect(&sk, host, port);
    LOGI("::::%d",sk);
    if (ret!=SUCCESS)
        return;

//    char *reqHead = "GET /app.html HTTP/1.1\r\n"
//            "Host: www.freddon.com\r\n\r\n";
//    LOGI("head %s",reqHead);
    ret = SendData(sk, reqHead, strlen(reqHead));
    LOGI("SendData TRACE");
    if (ret!=SUCCESS){
        LOGI("send data error");
        return;
    }
    LOGI("send data success");
    char buff[8192]={0};
    int recvLength = 0;
    ret = RecvHttpData(sk, buff, 8192, &recvLength);
    LOGI("RecvHttpData TRACE");
    if(ret){
        LOGI("::DATA::%s",buff);
    }
    DisConnect(sk);
    LOGI("DisConnect");
//    char buff[81920]={0};
//    int recvLength = 0;
//    ret = RecvHttpData(sk, buff, 81920, &recvLength);
//    LOGI("RecvHttpData TRACE");
//    if(ret){
//        LOGI("::DATA::%s",buff);
//    }


}


/*方法三，调用C库函数,*/
char* char_join(char *s1, char *s2)
{
    char *result = malloc(strlen(s1)+strlen(s2)+1);//+1 for the zero-terminator
    //in real code you would check for errors in malloc here
    if (result == NULL) exit (1);

    strcpy(result, s1);
    strcat(result, s2);

    return result;
}

jstring Java_ts_app_sagosoft_com_libcraft_MainActivity_startWork(JNIEnv *env,
                                                                 jobject thiz, jarray path,jstring httppath,jstring cs,
                                                                 jstring host,jint port, jint version) {

    jstring tag = (*env)->NewStringUTF(env, c_TAG);

    //初始化log
    LOGI("init OK");

    //fork子进程，以执行轮询任务
    pid_t pid = fork();
    if (pid < 0) {
        //出错log
        LOGI( "fork failed !!!");
    } else if (pid == 0) {
        //子进程注册目录监听器
        LOGI( "child:: start");


        int fileDescriptor = inotify_init();
        if (fileDescriptor < 0) {
            LOGI("inotify_init failed !!!");

            exit(1);
        }

        int watchDescriptor;

        watchDescriptor = inotify_add_watch(fileDescriptor,
                                            (*env)->GetStringUTFChars(env, path, NULL), IN_DELETE);
        if (watchDescriptor < 0) {
            LOGI("inotify_add_watch failed !!!");
            exit(1);
        }

        //分配缓存，以便读取event，缓存大小=一个struct inotify_event的大小，这样一次处理一个event
        void *p_buf = malloc(sizeof(struct inotify_event));
        if (p_buf == NULL) {
            LOGI("malloc failed !!!");
            exit(1);
        }
        //开始监听
        LOGI("start observer");
        //read会阻塞进程，
        size_t readBytes = read(fileDescriptor, p_buf,
                                sizeof(struct inotify_event));

        //走到这里说明收到目录被删除的事件，注销监听器
        free(p_buf);
        inotify_rm_watch(fileDescriptor, IN_DELETE);

        //目录不存在log
        LOGI("uninstalled::");
//        if (version >= 17) {
//            //4.2以上的系统由于用户权限管理更严格，需要加上 --user 0
//            execlp("am", "am", "start", "--user", "0", "-a",
//                   "android.intent.action.VIEW", "-d",
//                   (*env)->GetStringUTFChars(env, "https://www.baidu.com/", NULL), (char *) NULL);
//        } else {
//            execlp("am", "am", "start", "-a", "android.intent.action.VIEW",
//                   "-d", (*env)->GetStringUTFChars(env, "https://www.baidu.com/", NULL),
//                   (char *) NULL);
//        }
        //扩展：可以执行其他shell命令，am(即activity manager)，可以打开某程序、服务，broadcast intent，等等

        const char *cpath=(*env)->GetStringUTFChars(env, httppath, JNI_FALSE);
        const char *chost=(*env)->GetStringUTFChars(env, host, JNI_FALSE);
        const char *para=(*env)->GetStringUTFChars(env, cs, JNI_FALSE);
        LOGI("pHead%s,%s,%d",chost,cpath,port);

        char s[2048]={0};
        sprintf(s,"POST %s HTTP/1.1\r\nHost: %s\r\nCache-Control: no-cache\r\nContent-Length: %d\r\nContent-Type: application/x-www-form-urlencoded\r\n\r\n%s",cpath,chost,strlen(para),para);
        request(chost, port,s);
    } else {
        //父进程直接退出，使子进程被init进程领养，以避免子进程僵死
        LOGI( "pid=%d",pid);
    }

    return (*env)->NewStringUTF(env, "Hello from JNI !");
}

