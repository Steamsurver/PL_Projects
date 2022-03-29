#include <iostream>
using namespace std;
#include "ItMult.h"

int main() {
	cout << "Client is starting\n";
	//IUnknown_* server_2 = CreateServer(2); //сервер 2
	Interface_1* pointerInterface1 = NULL; //указатель на первый интерфейс
	Interface_2* pointerInterface2 = NULL; //указатель на второй интерфейс
	ICLassFactory_* pointerICF = NULL;//указатель на фабрику
	HRESULT_ res = NULL;
	char key = NULL;



	cout << "\n";
	cout << "Enter key(1, 2): ";
	cin >> key;


	switch (key)
	{
	case('1'):
		//-----------------------Сервер 1 на первом интерфейсе------------------------------------
		cout << "\n";
		res = CreateInstance(Server_id_1, ID_Interface_1_, (void**)&pointerInterface1);

		if (res == RESULT_OK_) {
			pointerInterface1->Funx_Interface_1();
			cout<<"References of pointerInterface1 = " << pointerInterface1->GetRef()<<"\n\n";
		}
		else
			cout << "Error Server 1\n";
		cout << "\n\n\n";


		//-----------------------Сервер 1 на втором интерфейсе------------------------------------
		res = pointerInterface1->QueryInterface(ID_Interface_2_, (void**)&pointerInterface2);
		if (res == RESULT_OK_) {
			pointerInterface2->Funx_Interface_2();
			cout << "References of pointerInterface2 = " << pointerInterface2->GetRef() << "\n\n";
		}
		else
			cout << "Error server 1 I2\n";
		cout << "\n\n\n";
		
		pointerInterface1->Release();
		pointerInterface2->Release();
		
		//-----------------------Сервер 2 на втором интерфейсе------------------------------------
		res = CreateInstance(Server_id_2, ID_Interface_2_, (void**)&pointerInterface2);

		if (res == RESULT_OK_) {
			pointerInterface2->Funx_Interface_2();
			cout << "References of pointerInterface2 server 2 = " << pointerInterface2->GetRef() << "\n\n";
		}
		else
			cout << "Client Main Error Interface_2 for server 2\n";
		cout << "\n\n\n";
		pointerInterface2->Release();
		break;
		
		
		case('2'):
			res = GetClassObject(Server_id_1, ID_ICLassFactory_, (void**)&pointerICF);
			if (res == RESULT_OK_) {
				res = pointerICF->CreateInstance_factory(ID_Interface_1_, (void**)&pointerInterface1);
				if (res == RESULT_OK_) {
					pointerInterface1->Funx_Interface_1();
					cout << "pointerICF server 1 = " << pointerICF->GetRef() << "\n";
					cout << "References of pointerInterface1 server 1 = " << pointerInterface1->GetRef() << "\n\n";
				}
			}
			pointerICF->Release();
			pointerInterface1->Release();
		break;
		
	}

return 0;
}





/*
1)переделать криэйтер
2)GUID (UUID) - прикрутить вместо интовых id (наверное)
HRESULT_ - int CRINS
IID_ - int id iterface
CLSID_ - int classid 
константы для номеров ID-ов

*/