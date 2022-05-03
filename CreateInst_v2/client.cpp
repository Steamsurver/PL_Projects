#include "InterfacesCalculator.h"
#include <iostream>
#include <SFML/Graphics.hpp>
#include <SFML/Network.hpp>
#include "windows.h"
#include <fstream>


using namespace std;
using namespace sf;
#include "FormSFML.h"



int main() {

	cout << "Client is starting\n";
	HINSTANCE load = LoadLibrary(L"../Calculator/DLL_LIB/Dll_MANAGER/x64/Debug/Dll_MANAGER.dll");//дескриптор
	InterfaceSimpleCalculator* pointerSimple = NULL; //указатель на первый интерфейс
	InterfaceFunclionalCalculator* pointerFunctional = NULL; //указатель на второй интерфейс
	InterfaceConverterCalculator* pointerConverter = NULL; //указатель на первый интерфейс
	ICLassFactory_* pointerICF = NULL;//указатель на фабрику
	HRESULT_ res = NULL;
	char key = NULL;
	double first = 0;
	double second = 0;


	
	cout << "\n";
	cout << "Enter key(1, 2): ";
	cin >> key;
	switch (key)
	{

		case('1'):
			
			if (!load) {
				cout << "Error loading dll_manager!\n";
			}

			HRESULT_ (*FuncCreateFactory) (CLSID_ id_server, void** pointer);
			FuncCreateFactory = (HRESULT_(*) (CLSID_ id_server, void** pointer)) GetProcAddress(load, "GetFactoryObject");
	

			if (!FuncCreateFactory)
			{
				cout<<"Error to load DLL func!\n";
			}


			res = FuncCreateFactory(ServerCalculator_id, (void**)&pointerICF);
			if (res != RESULT_OK_)
			{
				cout << "Error to call getClassObject! " <<res<<endl;
				return 0;
			}


			res = pointerICF->CreateInstance_factory(ID_InterfaceSimpleCalculator, (void**)&pointerSimple);
			if (res != RESULT_OK_) {
				cout << "Error to create simple calculator\n";
			}

			cout << endl << "Plus: " << pointerSimple->Plus(2, 2) << endl;


			break;



		case('2'):
			SimpleCalculatorForm* sCalculator = new SimpleCalculatorForm();
			sCalculator->init();
			sCalculator->update();


			break;
	}




	system("pause");
	return 0;
}
