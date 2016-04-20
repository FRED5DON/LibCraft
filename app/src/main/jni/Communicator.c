//#include "Communicator.h"
//#include "GUNetDef.h"
//
//#include <stdio.h>
//#include <stdlib.h>
//#include <stdbool.h>
//#include <string.h>
//#include <netdb.h>
//
//#ifdef WIN32
//    #include <windows.h>
//    #include <io.h>
//    #include <winsock.h>
//    #include <process.h>
//#else
//    #include <unistd.h>
//    #include <sys/time.h>
//    #include <pthread.h>
//    #include <errno.h>
//    #include <sys/types.h>
//    #include <sys/socket.h>
//    #include <arpa/inet.h>
//    #include <netinet/in.h>
//    #include <netdb.h>
//   #include <fcntl.h>
//#endif
//
//
//
//int Connect(int *sock, const char *address, unsigned short port)
//{
//	int _sk = socket(AF_INET, SOCK_STREAM, 0);
//	if (_sk == INVALID_SOCKET)
//		return NET_SOCKET_ERROR;
//
//	struct sockaddr_in sockAddr;
//	memset(&sockAddr, 0, sizeof(sockAddr));
//
//	sockAddr.sin_family	= AF_INET;
//	sockAddr.sin_addr.s_addr = inet_addr(address);
//	sockAddr.sin_port = htons(port);
//
//    if (sockAddr.sin_addr.s_addr == INADDR_NONE)
//    {
//        struct hostent *host = gethostbyname(address);
//        if (host == NULL)
//        {
//            return NET_SOCKET_ERROR;
//        }
//        sockAddr.sin_addr.s_addr = ((struct in_addr*)host->h_addr)->s_addr;
//    }
//
//	//unsigned long ul = 1;
//	//ioctlsocket(_sk, FIONBIO, &ul);
//	//connect(_sk, (SOCKADDR *)&sockAddr, sizeof(SOCKADDR));
//    fcntl (_sk, F_SETFL, O_NONBLOCK | fcntl (_sk, F_GETFL)); // 设置成非阻塞
//    int ret = connect(_sk, (struct sockaddr*)&sockAddr, sizeof(struct sockaddr));
//
//	fd_set fdset;
//	struct timeval tmv;
//	FD_ZERO(&fdset);
//	FD_SET(_sk, &fdset);
//	tmv.tv_sec = 15; // 设置超时时间
//	tmv.tv_usec = 0;
//
//    // 和windows下区别：
//    // 第一个参数是一个整数值，是指集合中所有文件描述符的范围，即所有文件描述符的最大值加1，不能错！
//    // 在Windows中这个参数的值无所谓，可以设置不正确。
//	ret = select(_sk+1, 0, &fdset, 0, &tmv);
//	if (ret == 0){
//        return NET_CONNNECT_TIMEOUT;
//    }
//	else if(ret < 0){
//        return NET_SOCKET_ERROR;
//    }
//
////	ul = 0;
////	ioctlsocket(_sk, FIONBIO, &ul);
//    int flags = fcntl(_sk, F_GETFL,0);
//    flags &= ~ O_NONBLOCK;
//    fcntl(_sk,F_SETFL, flags); // 设置成阻塞
//    *sock = _sk;
//	return SUCCESS;
//}
//
//int SendData(int _sk, const UInt8 *buffer, int bufferSize)
//{
//    int ret = send(_sk, buffer, bufferSize, 0);
//    if (ret != bufferSize)
//		return NET_SOCKET_ERROR;
//
//	return SUCCESS;
//}
//
//int RecvLength(int _sk, UInt8 *buffer, int bufferLength, int needRecvLength)
//{
//    int nHasRecvLen = 0;
//    while (nHasRecvLen < needRecvLength) {
//        fd_set fdset;
//		struct timeval tmv;
//		FD_ZERO(&fdset);
//		FD_SET(_sk, &fdset);
//		tmv.tv_sec = 6; // 设定超时时间
//		tmv.tv_usec = 0;
//
//        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
//		if (nRet == 0)
//			return NET_RECV_TIMEOUT;
//		else if(nRet < 0)
//			return NET_SOCKET_ERROR;
//
//        int nRecvLen = recv(_sk, buffer+nHasRecvLen, needRecvLength-nHasRecvLen, 0);
//		if (nRecvLen <= 0)
//			return NET_SOCKET_ERROR;
//
//        nHasRecvLen += nRecvLen;
//    }
//
//    return SUCCESS;
//}
//
//#if 0
//int RecvData(int _sk, UInt8* buffer, int bufferLength, int& dataLength)
//{
//    // 先接收头，再接收数据
//    t_net_header header;
//    int nHeaderSize = sizeof(t_net_header);
//    int nHasRecvLen = 0;
//    while (nHasRecvLen < nHeaderSize) {
//        fd_set fdset;
//		timeval tmv;
//		FD_ZERO(&fdset);
//		FD_SET(_sk, &fdset);
//		tmv.tv_sec = 3; // 设定超时时间
//		tmv.tv_usec = 0;
//
//        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
//		if (nRet == 0)
//			return NET_RECV_TIMEOUT;
//		else if(nRet < 0)
//			return NET_SOCKET_ERROR;
//
//        int nRecvLen = recv(_sk, &header+nHasRecvLen, nHeaderSize-nHasRecvLen, 0);
//		if (nRecvLen <= 0)
//			return NET_SOCKET_ERROR;
//
//        nHasRecvLen += nRecvLen;
//    }
//
//    // 校验
//    if (header.head4[0] != NET_Header_ID) {
//        return NET_DATA_ERROR;
//    }
//
//    // 转化为小端
//    header.dataLen = ntohl(header.dataLen);
//
//    // 接收数据
//    int nDataLen = header.dataLen + 8; // 最后有8个字节的crc
//    nHasRecvLen = 0;
//    while (nHasRecvLen < nDataLen) {
//        fd_set fdset;
//		timeval tmv;
//		FD_ZERO(&fdset);
//		FD_SET(_sk, &fdset);
//		tmv.tv_sec = 3; // 设定超时时间
//		tmv.tv_usec = 0;
//
//        int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
//		if (nRet == 0)
//			return NET_RECV_TIMEOUT;
//		else if(nRet < 0)
//			return NET_SOCKET_ERROR;
//
//        int nRecvLen = recv(_sk, buffer+nHasRecvLen, nDataLen-nHasRecvLen, 0);
//		if (nRecvLen <= 0)
//			return NET_SOCKET_ERROR;
//
//        nHasRecvLen += nRecvLen;
//    }
//
//    dataLength = nDataLen;
//    return SUCCESS;
//}
//
//int RecvAliveData(int _sk, UInt8* buffer, int bufferLength, int& dataLength)
//{
//    fd_set fdset;
//    timeval tmv;
//    FD_ZERO(&fdset);
//    FD_SET(_sk, &fdset);
//    tmv.tv_sec = 3; // 设定超时时间
//    tmv.tv_usec = 0;
//
//    int nRet = select(_sk+1, &fdset, 0, 0, &tmv);
//    if (nRet == 0)
//        return NET_RECV_TIMEOUT;
//    else if(nRet < 0)
//        return NET_SOCKET_ERROR;
//
//    int nRecvLen = recv(_sk, buffer, 1, 0);
//    if (nRecvLen <= 0)
//        return NET_SOCKET_ERROR;
//
//    dataLength = 1;
//    return SUCCESS;
//}
//#endif
//
//int DisConnect(int _sk)
//{
//    if (_sk != INVALID_SOCKET)
//	{
//		close(_sk);
//		_sk = INVALID_SOCKET;
//	}
//	return SUCCESS;
//}
//
//int RecvHttpData(int _sk, UInt8 *buffer, int bufferSize, int *recvSize)
//{
//
//    // 1. 接收HTTP包头
//	const int httpHead = 4096;
//	char httpheadBuff[4096] = {0};
//
//	int recvHeadLen = 0;
//	int recvDataLen = 0;
//	int dataLen = 0;
//	bool recvHttpheadOK = false;
//
//
//	while(recvHeadLen < httpHead)
//	{
//		fd_set fdset;
//		struct timeval tmv;
//		FD_ZERO(&fdset);
//		FD_SET(_sk, &fdset);
//		tmv.tv_sec = 3; // 设定超时时间
//		tmv.tv_usec = 0;
//
//		int ret = select(_sk+1, &fdset, 0, 0, &tmv);
//		if (ret == 0)
//			return NET_RECV_TIMEOUT;
//		else if(ret < 0)
//			return NET_SOCKET_ERROR;
//
//		int rlen = recv(_sk, httpheadBuff+recvHeadLen, httpHead-recvHeadLen, 0);
//		if (rlen <= 0)
//			return NET_SOCKET_ERROR;
//
//		recvHeadLen += rlen;
//
//		char* headEnd = strstr(httpheadBuff, "\r\n\r\n"); // 查找数据开始（头结束）位置
//		if (headEnd != NULL)
//		{
//			headEnd += 4;
//			char* start = strstr(httpheadBuff, "Content-Length:"); // 数据部分长度
//			if (start == NULL)
//				return NET_COMMUNICATOR_ERROR;
//
//			start += 15;
//			char* end = strstr(start, "\r\n");
//			if (end == NULL)
//				return NET_COMMUNICATOR_ERROR;
//
//            // 求数据长度
//			char contentLen[64] = {0};
//			memcpy(contentLen, start, end - start);
//			dataLen = atoi(contentLen);
//            printf("数据长度 %d\n", dataLen);
//
//			recvDataLen = recvHeadLen - (int)(headEnd - httpheadBuff); // 计算已接收的数据长度
//			if (recvDataLen >= dataLen) // 已包含完整数据
//			{
//				memcpy(buffer, headEnd, dataLen);
//				buffer[dataLen] = '\0';
//				*recvSize = recvDataLen;
//
//                // 打印头信息
//                httpheadBuff[recvHeadLen - recvDataLen] = '\0';
//                printf("%s\n", httpheadBuff);
//
//				return SUCCESS;
//			}
//			if (recvDataLen > 0)
//			{
//				memcpy(buffer, headEnd, recvDataLen); // 拷贝已接收的数据
//			}
//
//			recvHttpheadOK = true;
//			break; // http 头接收完毕
//		}
//	}
//
//    // 打印头信息
//    httpheadBuff[recvHeadLen - recvDataLen] = '\0';
//    printf("%s\n", httpheadBuff);
//
//	if (!recvHttpheadOK)
//		return NET_COMMUNICATOR_ERROR;
//
//	// 2. 接收数据
//	while(recvDataLen < dataLen)
//	{
//		fd_set fdset;
//		struct timeval tmv;
//
//		FD_ZERO(&fdset);
//		FD_SET(_sk, &fdset);
//
//		tmv.tv_sec = 3;
//		tmv.tv_usec = 0;
//
//		int ret = select(_sk+1, &fdset, 0, 0, &tmv);
//		if (ret == 0)
//			return NET_RECV_TIMEOUT;
//		else if(ret < 0)
//			return NET_SOCKET_ERROR;
//
//		ret = recv(_sk, buffer + recvDataLen, dataLen - recvDataLen, 0);
//		if (ret <= 0)
//			return NET_SOCKET_ERROR;
//
//		recvDataLen += ret;
//	}
//	buffer[recvDataLen] = '\0';
//	*recvSize = recvDataLen;
//	return SUCCESS;
//}
//
//
//
