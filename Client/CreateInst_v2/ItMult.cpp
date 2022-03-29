#include <iostream>
using namespace std;
#include "ItMult.h"

//---------------------------------------------------------------------------------------------------------------
HRESULT_ CreateServer(CLSID_ id_server, IID_ IID, void** pointer) {
	HRESULT_ res;
	if (id_server == Server_id_1) {
		cout << "Function CreateServer for server 1" << endl;
		Server* s = new Server();
		if (IID == ID_Interface_1_) {
			*pointer = (IUnknown_*)(Interface_1*)s;
			cout << "Creating interface 1" << endl;
			return RESULT_OK_;
		}
		else if (IID == ID_Interface_2_) {
			*pointer = (IUnknown_*)(Interface_2*)s;
			cout << "Creating interface 2" << endl;
			return RESULT_OK_;
		}
		else {
			return RESULT_NOINTERFACE_;
		}
	}
		

		if (id_server == Server_id_2) {
		cout << "Function CreateServer for server 2" << endl;
		Server_2* s = new Server_2();
		if (IID == ID_Interface_2_) {
			*pointer = (IUnknown_*)(Interface_2*)s;
			cout << "Creating interface 2" << endl;
			return RESULT_OK_;
		}
		else {
			return RESULT_NOINTERFACE_;
		}
	}

	else {
		return RESULT_NOSERVER_;
	}
	
}
HRESULT_ GetClassObject(CLSID_ id_server, IID_ IID, void** pointer) {
	HRESULT_ hr = NULL;
	if (IID == ID_ICLassFactory_)
	{
		cout << "Get Factory\n";
		hr = CreateInstance(id_server, IID, pointer);
		if (hr == RESULT_OK_)
		{
			return hr;
		}
	}
	else {
		cout << "Get Server\n";
		hr = CreateInstance(id_server, IID, pointer);
		if (hr == RESULT_OK_)
		{
			return hr;
		}
	}
}

//---------------------------------------------------------------------------------------------------------------

//---------------------------Реализация под сервер 1----------------------------
Server::Server() {
	test_var = 100;
}

void Server::Addref() {
	reference++;
}

void Server::Release() {
	reference--;
	if (reference == 0) {
		cout << "reference server 1 = 0\n";
		delete this;
	}
}

ULONG_ Server::GetRef() {
	return reference;
}

void Server::Funx_Interface_1() {
	test_var += 1;
	cout <<test_var <<" Test out function of Interface_1 and server 1\n\n";
}

void Server::Funx_Interface_2(){
	test_var += 10;
	cout << test_var << " Test out function of Interface_2 and server 1\n\n";
}

Server::~Server() {
	cout << "Server 1 Destructor\n";
}

HRESULT_ Server::QueryInterface(IID_ IID, void** pointer)
{
	cout << "Server 1 QueryInterface" << endl;

	if (IID == ID_IUnknown_){
		this->Addref();
		*pointer = (IUnknown_*)(Interface_1*)this;
	}

	else if (IID == ID_Interface_1_){
		this->Addref();
		*pointer = static_cast<Interface_1*>(this);
	}

	else if (IID == ID_Interface_2_){
		this->Addref();
		*pointer = (Interface_2*)this;
	}
	else if (IID == ID_ICLassFactory_) {
		this->Addref();
		*pointer = (ICLassFactory_*)this;
	}

	else{
		*pointer = NULL;
		return RESULT_NOINTERFACE_;
	}

	return RESULT_OK_;
}

HRESULT_ Server::CreateInstance_factory(IID_ IID, void** pointer) {
	HRESULT_ hr = CreateInstance(Server_id_1, IID, pointer);
	return hr;
}


//---------------------------Реализация под сервер 2----------------------------
Server_2::Server_2() {
	test_var = 200;
}

void Server_2::Addref() {
	reference++;
}

void Server_2::Release() {
	reference--;
	if (reference == 0) {
		cout << "reference server 2 = 0\n";
		delete this;
	}
}

ULONG_ Server_2::GetRef() {
	return reference;
}

void Server_2::Funx_Interface_2() {
	test_var += 1;
	cout << test_var << " Test out function of Interface_2 and server 2\n\n";
}

Server_2::~Server_2() {
	cout << "Server 2 Destructor\n";
}

HRESULT_ Server_2::QueryInterface(IID_ IID, void** pointer)
{
	cout << "Server 2 QueryInterface" << endl;

	if (IID == ID_IUnknown_){
		this->Addref();
		*pointer = (IUnknown_*)(Interface_1*)this;
	}
	else if (IID == ID_Interface_2_){
		this->Addref();
		*pointer = (Interface_2*)this;
	}
	else if (IID == ID_ICLassFactory_) {
		this->Addref();
		*pointer = (ICLassFactory_*)this;
	}
	else{
		*pointer = NULL;
		return RESULT_NOINTERFACE_;
	}

	return RESULT_OK_;
}
HRESULT_ Server_2::CreateInstance_factory(IID_ IID, void** pointer) {
	HRESULT_ hr = CreateInstance(Server_id_2, IID, pointer);
	return hr;
}

HRESULT_ CreateInstance(CLSID_ id_server, IID_ IID, void** pointer)
{
	IUnknown_* I_Server;
	{
			if (IID == ID_Interface_1_ && id_server == Server_id_1) {
				I_Server = (IUnknown_*)(Interface_1*) new Server();
			}
			else
				if (IID == ID_Interface_2_ && id_server == Server_id_1) {
					I_Server = (IUnknown_*)(Interface_2*) new Server();
				}
				else if (IID == ID_ICLassFactory_ && id_server == Server_id_1) {
					I_Server = (IUnknown_*)(ICLassFactory_*) new Server();
				}else
					if (IID == ID_Interface_2_ && id_server == Server_id_2) {
						I_Server = (IUnknown_*)(Interface_2*) new Server_2();
					}
					else if (IID == ID_ICLassFactory_ && id_server == Server_id_2) {
						I_Server = (IUnknown_*)(ICLassFactory_*) new Server();
					}
					else
					{
						return RESULT_NOSERVER_;
					}
	}
	if (I_Server->QueryInterface(IID, pointer) != RESULT_OK_) return 2;

	return RESULT_OK_;
}
