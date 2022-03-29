#pragma once
using HRESULT_ = int;
using IID_ = int;
using CLSID_ = int;
using ULONG_ = int;

const IID_ ID_IUnknown_ = 0; // id iunknown
const IID_ ID_Interface_1_ = 1; 
const IID_ ID_Interface_2_ = 2;
const IID_ ID_ICLassFactory_ = 10;


const HRESULT_ RESULT_OK_ = 0;
const HRESULT_ RESULT_NOINTERFACE_ = 1;
const HRESULT_ RESULT_NOSERVER_ = 10;

const CLSID_ Server_id_1 = 0; // id сервера 1
const CLSID_ Server_id_2 = 1; // id сервера 2





//-------------------------IUnknown----------------------------
class IUnknown_
{
private:
public:
	virtual void Addref() = 0;
	virtual	void Release() = 0;
	virtual ULONG_ GetRef() = 0;
	virtual HRESULT_ QueryInterface(IID_ IID, void** pointer) = 0;
};

//-------------------------Interfaces--------------------------
class ICLassFactory_ : public IUnknown_ {
private:
	int var = 0;
public:
	virtual HRESULT_ CreateInstance_factory(IID_ IID, void** pointer) = 0;
	virtual HRESULT_ QueryInterface(IID_ IID, void** pointer) = 0;
};

class Interface_1: public IUnknown_ {
public:
	virtual void Funx_Interface_1() = 0;
	
};

class Interface_2: public IUnknown_ {
public:
	virtual void Funx_Interface_2() = 0;

};

int CreateServer(CLSID_ id_server, IID_ IID, void** pointer);

HRESULT_ GetClassObject(CLSID_ id_server, IID_ IID, void** pointer);


                                          

//--------------------------Components-----------------------------
class Server : public Interface_1, public Interface_2, public ICLassFactory_{
private:
	int test_var;
	int reference = 0;
public:
	Server();
	~Server();

	HRESULT_ QueryInterface(IID_ IID, void** pointer);
	HRESULT_ CreateInstance_factory(IID_ IID, void** pointer);
	void Addref();
	void Release();
	ULONG_ GetRef();
	void Funx_Interface_1();
	void Funx_Interface_2();
};

class Server_2 : public Interface_2, public ICLassFactory_ {
private:
	int test_var;
	int reference = 0;
public:
	Server_2();
	~Server_2();

	HRESULT_ QueryInterface(IID_ IID, void** pointer);
	HRESULT_ CreateInstance_factory(IID_ IID, void** pointer);
	void Addref();
	void Release();
	ULONG_ GetRef();
	void Funx_Interface_2();
};


HRESULT_ CreateInstance(CLSID_ id_server, IID_ IID, void** pointer);

