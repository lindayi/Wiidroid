#ifndef TASKBAR_H_
#define TASKBAR_H_

#define ID_TASKBAR	0x0401		// 托盘消息标记（用户自定义消息编号建议从 0x0400 - 0x07FF ）

// 显示托盘图标
VOID ShowTaskBar(HWND hWnd);

// 销毁托盘图标
VOID DestroyTaskBar();

// 显示托盘菜单
VOID ShowTaskBarMenu(HWND hWnd);

#endif