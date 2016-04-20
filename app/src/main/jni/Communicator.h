#ifndef COMMUNICATOR_H_
#define COMMUNICATOR_H_

// 网络通信类
typedef unsigned char UInt8;

    
	  int Connect(int *sock, const char *address, unsigned short port);
extern  int SendData(int _sk,const UInt8 *buffer, int bufferSize);
    
    // 接收指定长度
    extern  int RecvLength(int _sk,UInt8*buffer, int bufferLength, int needRecvLength);
    
//    int RecvData(int _sk,UInt8* buffer, int bufferLength, int & dataLength);
//    int RecvAliveData(int _sk,UInt8* buffer, int bufferLength, int& dataLength);
extern  int DisConnect(int _sk);

extern   int RecvHttpData(int _sk,UInt8 *buffer, int bufferSize, int *recvSize);


//int _sk; // socket handle

#endif /*COMMUNICATOR_H_*/
