#include "Demo.h"

static NOTIFYICONDATA nid;

// 显示托盘图标
VOID ShowTaskBar(HWND hWnd)
{
	nid.cbSize = sizeof(nid);
	nid.hWnd = hWnd;
	nid.uID = ID_TASKBAR;
	nid.uCallbackMessage = WM_COMMAND;
	nid.hIcon = LoadIcon(hInst, MAKEINTRESOURCE(IDI_ICONAPP)); 
	lstrcpy(nid.szTip, TEXT("Demo"));
	nid.uFlags = NIF_ICON | NIF_MESSAGE | NIF_TIP;
	Shell_NotifyIcon(NIM_ADD, &nid);
}

// 销毁托盘图标
VOID DestroyTaskBar()
{
	Shell_NotifyIcon(NIM_DELETE, &nid);
}

// 显示托盘菜单
VOID ShowTaskBarMenu(HWND hWnd)
{
	HMENU		hmenu;				// 顶级菜单
	HMENU		hmenuTrackPopup;	// 弹出菜单	
	POINT		pt;

	// 获取鼠标当前位置以供弹出菜单函数定位
	GetCursorPos(&pt);

	//解决在菜单外单击左键菜单不消失的问题
	SetForegroundWindow(hWnd);

	// 加载菜单资源，获得菜单句柄
	if ((hmenu = LoadMenu(hInst, MAKEINTRESOURCE(IDR_MENU_BAR))) == NULL) {
		return;
	}

	// 获取子菜单句柄
	hmenuTrackPopup = GetSubMenu(hmenu, 0);

	// 弹出菜单
	TrackPopupMenuEx(hmenuTrackPopup, TPM_LEFTALIGN | TPM_RIGHTBUTTON,
		pt.x, pt.y, hWnd, NULL);

	// 完成后需要释放相关资源
	DestroyMenu(hmenu);
}