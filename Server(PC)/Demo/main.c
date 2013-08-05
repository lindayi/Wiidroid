#include <process.h>
#include "Demo.h"

static HANDLE sockThread;

#pragma comment (lib, "ws2_32.lib")

// 对话框处理函数
BOOL WINAPI Main_Proc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
    switch(uMsg)
    {
	// 设置消息映射，将不同的消息映射到不同的处理函数去
	HANDLE_MSG(hWnd, WM_INITDIALOG, Main_OnInitDialog);		// WM_INITDIALOG 对话框创建完成的消息
	HANDLE_MSG(hWnd, WM_COMMAND, Main_OnCommand);			// WM_COMMAND 按钮等按下的消息
	HANDLE_MSG(hWnd, WM_CLOSE, Main_OnClose);				// WM_CLOSE 对话框关闭的消息

	// 只截获处理窗口最小化消息
	case WM_SYSCOMMAND:
		if (wParam == SC_MINIMIZE || wParam == SC_CLOSE) {
			// 隐藏窗口（其实最小化到托盘区和单击显示只是在隐藏和显示窗口）
			ShowWindow(hWnd, SW_HIDE);
			// 必须返回，不能让windows再处理这个消息
			return TRUE;
		}
	}

    return FALSE;
}

// 对话框创建时执行的函数
BOOL Main_OnInitDialog(HWND hwnd, HWND hwndFocus, LPARAM lParam)
{
    // 设置程序图标
    HICON hIcon = LoadIcon((HINSTANCE)GetWindowLong(hwnd, GWL_HINSTANCE) ,MAKEINTRESOURCE(IDI_ICONAPP));
	SendMessage(hwnd, WM_SETICON, TRUE,  (LPARAM)hIcon);

	// 显示托盘图标
	ShowTaskBar(hwnd);
    
    return TRUE;
}

// 对话框按钮消息处理函数
void Main_OnCommand(HWND hwnd, int id, HWND hwndCtl, UINT codeNotify)
{
	static BOOL threadCreate = FALSE;

	switch(id)
    {
	case IDC_SOCK_CONTRAL:
		if (threadCreate == FALSE) {
			sockThread = (HANDLE)_beginthreadex(NULL, 0, ThreadNet, 0, 0, 0);
			SendMessage(GetDlgItem(hwnd, IDC_SOCK_CONTRAL), WM_SETTEXT, 0, (LPARAM)TEXT("关闭服务"));
			threadCreate = TRUE;
		} else {
			TerminateThread(sockThread, -1);
			SendMessage(GetDlgItem(hwnd, IDC_SOCK_CONTRAL), WM_SETTEXT, 0, (LPARAM)TEXT("启动服务"));
			threadCreate = FALSE;
		}
		break;
	// 托盘区的自定义消息
	case ID_TASKBAR:
		if ((UINT)hwndCtl == WM_LBUTTONDOWN) {
			// 显示出窗口
			ShowWindow(hwnd, SW_SHOW);
			// 激活该窗口
			SetForegroundWindow(hwnd);
		} else if ((UINT)hwndCtl == WM_RBUTTONDOWN)	{
			// 右键单击的话显示菜单
			ShowTaskBarMenu(hwnd);
		}
		break;
		// 恢复窗口（右键托盘区菜单 - 戳我戳我>.<）
	case ID_DEMO_SHOW:
			// 显示出窗口
			ShowWindow(hwnd, SW_SHOW);
			// 激活该窗口
			SetForegroundWindow(hwnd);
		break;
	// 退出程序
	case ID_DEMO_EXIT:
		DestroyWindow(hwnd);		// 销毁窗口
		DestroyTaskBar();			// 清理托盘区
		PostQuitMessage(0);			// 发送退出消息
		break;
	default:
		break;
    }
}

// 对话框关闭处理函数
void Main_OnClose(HWND hwnd)
{
	// 关闭操作默认终结对话框
    EndDialog(hwnd, 0);

	// 销毁托盘图标
	DestroyTaskBar();
}

//键盘模式
void TypeKeyboard(char recvBuf[])
{
//	POINT curpos;
	HWND hWnd = NULL;
	INT KeyCode;//虚拟键盘消息变量
	CHAR trans[4];//临时转换char_to_int的变量
	INPUT Input;//SendInput()函数的事件参数

	//GetCursorPos(&curpos);//获取当前鼠标的位置，位置将储存在curpos里。
	//hWnd = WindowFromPoint(curpos);//根据curpos所指的坐标点获取窗口句柄
	/*hWnd = FindWindow(NULL, TEXT("无标题 - 记事本"));
	if (hWnd) {
		ShowWindow(hWnd, SW_NORMAL);
		SetWindowPos(hWnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE);
		SetForegroundWindow(hWnd);
	} else {
		MessageBox(NULL, TEXT("Cannot find!"), TEXT("Error"), MB_OK | MB_ICONERROR);
	}*/
	strncpy(trans,&recvBuf[6],3);
	trans[3] = '\0';
	//将recvBuf的键码信息赋值到KeyCode
	KeyCode = atoi(trans);
	switch(recvBuf[4])//判断KeyDown或KeyUpss
	{
		case '0':
			//若SendMessage()不行，则尝试SendInput()函数
			keybd_event(KeyCode, 0, 0, 0);
			//keybd_event(KeyCode, 0, KEYEVENTF_KEYUP, 0);
			break;
		case '1':
			keybd_event(KeyCode, 0, KEYEVENTF_KEYUP, 0);
			break;
	}
}

//鼠标模式
void TypeMouse(char recvBuf[])
{
	POINT curpos;
	HWND hWnd;
	INT fMouse;

	GetCursorPos(&curpos);//获取当前鼠标的位置，位置将储存在curpos里。
	hWnd = WindowFromPoint(curpos);//根据curpos所指的坐标点获取窗口句柄
	fMouse = GetSystemMetrics (SM_MOUSEPRESENT) ;//检测鼠标是否存在
	if(fMouse == 0);
}

//后台处理函数，分析socket传来的消息
//recvBuf[]格式：模式(1,2)_消息类型(KeyDown,KeyUp)_键盘码
void InspectMsg(char recvBuf[])
{
	switch(recvBuf[2])
	{
		case '1': 
			TypeKeyboard(recvBuf);
			break;
		case '2':
			TypeMouse(recvBuf);
			break;
	}
}

//后台处理函数(处理socket发送来的字符)
/*void SendMsg(char recvBuf[])
{
	POINT curpos;//一个可储存坐标点的结构体变量，x横坐标，y,纵坐标，如curpos.x   curpos.y
	HWND hWnd;

	while(TRUE)
	{
		GetCursorPos(&curpos);//获取当前鼠标的位置，位置将储存在curpos里。
		hWnd = WindowFromPoint(curpos);//根据curpos所指的坐标点获取窗口句柄
		InspectMsg(recvBuf);//分析字符串
		//SendMessage(hWnd,WM_CHAR,(WPARAM)recvBuf,0);//发送一个字符（按键）消息给当前鼠标所指向的窗口句柄
	}
}*/

// 网络线程函数
UINT WINAPI ThreadNet(LPVOID lpParam)
{
	WSADATA				WSADa;
	SOCKADDR_IN			SockAddrIn;
	SOCKET				CSocket, SSocket;		
	INT					iAddrSize;
	CHAR				recvBuf[50];

	// 初始化数据类型
	ZeroMemory( &WSADa, sizeof(WSADATA) );

	// 加载 ws2_32.dll
	WSAStartup(MAKEWORD(2, 2), &WSADa);

	// 设置本地信息和绑定协议
	SockAddrIn.sin_family		=  AF_INET;
	SockAddrIn.sin_port			=  htons(12345);
	SockAddrIn.sin_addr.s_addr	=  INADDR_ANY;

	// 建立 Socket
	CSocket = WSASocket(AF_INET, SOCK_STREAM, IPPROTO_TCP, NULL, 0, 0);

	// 设置绑定端口
	bind(CSocket, (SOCKADDR *)&SockAddrIn, sizeof(SockAddrIn));

	// 设置服务器监听端口
	listen(CSocket, 1);

	iAddrSize = sizeof(SockAddrIn);
	

	while (TRUE)
	{

		// 等待连接
		SSocket = accept(CSocket, (SOCKADDR *)&SockAddrIn, &iAddrSize);
		ZeroMemory(recvBuf, sizeof(recvBuf));
		recv(SSocket, recvBuf, 50, 0);
		// 收到了处理就好
		// .....
		//后台处理函数
		InspectMsg(recvBuf);
		closesocket(SSocket);
		//closesocket(CSocket);
	}

	return TRUE;
}
