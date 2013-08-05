#ifndef _MAIN_H
#define _MAIN_H

extern HINSTANCE hInst;

// 对话框处理函数
BOOL WINAPI Main_Proc(HWND, UINT, WPARAM, LPARAM);

// 对话框创建时执行的函数
BOOL Main_OnInitDialog (HWND, HWND, LPARAM);

// 对话框按钮消息处理函数
void Main_OnCommand(HWND, INT, HWND, UINT);

// 对话框关闭处理函数
void Main_OnClose(HWND);

// 网络线程函数
UINT WINAPI ThreadNet(LPVOID lpParam);

#endif
