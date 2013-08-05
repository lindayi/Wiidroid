#include "Demo.h"

// 应用程序实例
HINSTANCE hInst;

INT APIENTRY WinMain(	HINSTANCE hInstance,
						HINSTANCE hPrevInstance,
						LPSTR     lpCmdLine,
						INT       nCmdShow )
{
	hInst = hInstance;

	// 根据资源文件信息建立对话框
    DialogBox(hInstance, MAKEINTRESOURCE(IDD_MAIN), NULL, Main_Proc);

    return(0);
}
