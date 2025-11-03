#include "pch-cpp.hpp"

#ifndef _MSC_VER
# include <alloca.h>
#else
# include <malloc.h>
#endif


#include <limits>

#include "icalls/System/System.Net.Sockets/SocketException.h"

struct VirtualActionInvoker0
{
	typedef void (*Action)(void*, const RuntimeMethod*);

	static inline void Invoke (Il2CppMethodSlot slot, RuntimeObject* obj)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		((Action)invokeData.methodPtr)(obj, invokeData.method);
	}
};
template <typename T1>
struct VirtualActionInvoker1
{
	typedef void (*Action)(void*, T1, const RuntimeMethod*);

	static inline void Invoke (Il2CppMethodSlot slot, RuntimeObject* obj, T1 p1)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		((Action)invokeData.methodPtr)(obj, p1, invokeData.method);
	}
};
template <typename T1, typename T2, typename T3>
struct VirtualActionInvoker3
{
	typedef void (*Action)(void*, T1, T2, T3, const RuntimeMethod*);

	static inline void Invoke (Il2CppMethodSlot slot, RuntimeObject* obj, T1 p1, T2 p2, T3 p3)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		((Action)invokeData.methodPtr)(obj, p1, p2, p3, invokeData.method);
	}
};
template <typename R>
struct VirtualFuncInvoker0
{
	typedef R (*Func)(void*, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeObject* obj)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		return ((Func)invokeData.methodPtr)(obj, invokeData.method);
	}
};
template <typename R, typename T1>
struct VirtualFuncInvoker1
{
	typedef R (*Func)(void*, T1, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeObject* obj, T1 p1)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		return ((Func)invokeData.methodPtr)(obj, p1, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3>
struct VirtualFuncInvoker3
{
	typedef R (*Func)(void*, T1, T2, T3, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeObject* obj, T1 p1, T2 p2, T3 p3)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct VirtualFuncInvoker4
{
	typedef R (*Func)(void*, T1, T2, T3, T4, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeObject* obj, T1 p1, T2 p2, T3 p3, T4 p4)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_virtual_invoke_data(slot, obj);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, p4, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3>
struct GenericVirtualFuncInvoker3
{
	typedef R (*Func)(void*, T1, T2, T3, const RuntimeMethod*);

	static inline R Invoke (const RuntimeMethod* method, RuntimeObject* obj, T1 p1, T2 p2, T3 p3)
	{
		VirtualInvokeData invokeData;
		il2cpp_codegen_get_generic_virtual_invoke_data(method, obj, &invokeData);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct GenericVirtualFuncInvoker4
{
	typedef R (*Func)(void*, T1, T2, T3, T4, const RuntimeMethod*);

	static inline R Invoke (const RuntimeMethod* method, RuntimeObject* obj, T1 p1, T2 p2, T3 p3, T4 p4)
	{
		VirtualInvokeData invokeData;
		il2cpp_codegen_get_generic_virtual_invoke_data(method, obj, &invokeData);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, p4, invokeData.method);
	}
};
struct InterfaceActionInvoker0
{
	typedef void (*Action)(void*, const RuntimeMethod*);

	static inline void Invoke (Il2CppMethodSlot slot, RuntimeClass* declaringInterface, RuntimeObject* obj)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_interface_invoke_data(slot, obj, declaringInterface);
		((Action)invokeData.methodPtr)(obj, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3>
struct InterfaceFuncInvoker3
{
	typedef R (*Func)(void*, T1, T2, T3, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeClass* declaringInterface, RuntimeObject* obj, T1 p1, T2 p2, T3 p3)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_interface_invoke_data(slot, obj, declaringInterface);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct InterfaceFuncInvoker4
{
	typedef R (*Func)(void*, T1, T2, T3, T4, const RuntimeMethod*);

	static inline R Invoke (Il2CppMethodSlot slot, RuntimeClass* declaringInterface, RuntimeObject* obj, T1 p1, T2 p2, T3 p3, T4 p4)
	{
		const VirtualInvokeData& invokeData = il2cpp_codegen_get_interface_invoke_data(slot, obj, declaringInterface);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, p4, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3>
struct GenericInterfaceFuncInvoker3
{
	typedef R (*Func)(void*, T1, T2, T3, const RuntimeMethod*);

	static inline R Invoke (const RuntimeMethod* method, RuntimeObject* obj, T1 p1, T2 p2, T3 p3)
	{
		VirtualInvokeData invokeData;
		il2cpp_codegen_get_generic_interface_invoke_data(method, obj, &invokeData);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, invokeData.method);
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct GenericInterfaceFuncInvoker4
{
	typedef R (*Func)(void*, T1, T2, T3, T4, const RuntimeMethod*);

	static inline R Invoke (const RuntimeMethod* method, RuntimeObject* obj, T1 p1, T2 p2, T3 p3, T4 p4)
	{
		VirtualInvokeData invokeData;
		il2cpp_codegen_get_generic_interface_invoke_data(method, obj, &invokeData);
		return ((Func)invokeData.methodPtr)(obj, p1, p2, p3, p4, invokeData.method);
	}
};
template <typename R, typename T1>
struct InvokerFuncInvoker1;
template <typename R, typename T1>
struct InvokerFuncInvoker1<R, T1*>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1)
	{
		R ret;
		void* params[1] = { p1 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2>
struct InvokerFuncInvoker2;
template <typename R, typename T1, typename T2>
struct InvokerFuncInvoker2<R, T1*, T2*>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2)
	{
		R ret;
		void* params[2] = { p1, p2 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct InvokerFuncInvoker4;
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct InvokerFuncInvoker4<R, T1*, T2*, T3*, T4>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2, T3* p3, T4 p4)
	{
		R ret;
		void* params[4] = { p1, p2, p3, &p4 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4>
struct InvokerFuncInvoker4<R, T1*, T2*, T3*, T4*>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2, T3* p3, T4* p4)
	{
		R ret;
		void* params[4] = { p1, p2, p3, p4 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4, typename T5>
struct InvokerFuncInvoker5;
template <typename R, typename T1, typename T2, typename T3, typename T4, typename T5>
struct InvokerFuncInvoker5<R, T1*, T2*, T3*, T4*, T5>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2, T3* p3, T4* p4, T5 p5)
	{
		R ret;
		void* params[5] = { p1, p2, p3, p4, &p5 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4, typename T5>
struct InvokerFuncInvoker5<R, T1*, T2*, T3*, T4*, T5*>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2, T3* p3, T4* p4, T5* p5)
	{
		R ret;
		void* params[5] = { p1, p2, p3, p4, p5 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};
template <typename R, typename T1, typename T2, typename T3, typename T4, typename T5, typename T6>
struct InvokerFuncInvoker6;
template <typename R, typename T1, typename T2, typename T3, typename T4, typename T5, typename T6>
struct InvokerFuncInvoker6<R, T1*, T2*, T3*, T4*, T5*, T6*>
{
	static inline R Invoke (Il2CppMethodPointer methodPtr, const RuntimeMethod* method, void* obj, T1* p1, T2* p2, T3* p3, T4* p4, T5* p5, T6* p6)
	{
		R ret;
		void* params[6] = { p1, p2, p3, p4, p5, p6 };
		method->invoker_method(methodPtr, method, obj, params, &ret);
		return ret;
	}
};

// System.Action`1<System.IAsyncResult>
struct Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780;
// System.Action`1<System.Object>
struct Action_1_t6F9EB113EB3F16226AEF811A2744F4111C116C87;
// System.Threading.AsyncLocal`1<System.Globalization.CultureInfo>
struct AsyncLocal_1_t1D3339EA4C8650D2DEDDF9553E5C932B3DC2CCFD;
// System.Collections.Generic.Dictionary`2<System.Int32,System.Threading.Tasks.Task>
struct Dictionary_2_t403063CE4960B4F46C688912237C6A27E550FF55;
// System.Collections.Generic.Dictionary`2<System.Object,System.Object>
struct Dictionary_2_t14FE4A752A83D53771C584E4C8D14E01F2AFD7BA;
// System.Collections.Generic.Dictionary`2<System.String,System.Int32>
struct Dictionary_2_t5C8F46F5D57502270DD9E1DA8303B23C7FE85588;
// System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>
struct Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED;
// System.EventHandler`1<System.Object>
struct EventHandler_1_tD8C4A5BE1F7C91B1A7E99AE87AFD2F5432C38746;
// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs>
struct EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED;
// System.Func`1<System.Threading.Tasks.Task/ContingentProperties>
struct Func_1_tD59A12717D79BFB403BF973694B1BE5B85474BD1;
// System.Func`5<System.Object,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>
struct Func_5_tA07BEDDD813173420A179C25AEDEA235CEB634BB;
// System.Func`5<System.Object,System.Int32,System.Object,System.Object,System.Object>
struct Func_5_tD02E129BCD67EF3C2C268C78FD4614374A3597CA;
// System.Func`5<System.String,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>
struct Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818;
// System.Collections.Generic.IEqualityComparer`1<System.Threading.Thread>
struct IEqualityComparer_1_tDDAAA260364110462C075E50E4623348C5AA1268;
// System.Collections.Generic.IList`1<System.ArraySegment`1<System.Byte>>
struct IList_1_t80970C26B61C899A37E146F69BC51E2443270FC1;
// System.Collections.Generic.Dictionary`2/KeyCollection<System.Threading.Thread,System.Diagnostics.StackTrace>
struct KeyCollection_t680178B5E16E74DDA4B763692AF82BE84A13BE1C;
// System.Collections.Generic.List`1<System.Object>
struct List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D;
// System.Collections.Generic.List`1<System.Threading.Thread>
struct List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D;
// System.Predicate`1<System.Object>
struct Predicate_1_t8342C85FF4E41CD1F7024AC0CDC3E5312A32CB12;
// System.Predicate`1<System.Threading.Tasks.Task>
struct Predicate_1_t7F48518B008C1472339EEEBABA3DE203FE1F26ED;
// System.Collections.ObjectModel.ReadOnlyCollection`1<System.Exception>
struct ReadOnlyCollection_1_t0A3EEA206F04C2F9E8303E24D863C2E7FD258886;
// System.Threading.Tasks.TaskFactory`1<System.Net.IPAddress[]>
struct TaskFactory_1_tDD05F826FE9D42F2F143BC685A7396C663E6CFE4;
// System.Threading.Tasks.TaskFactory`1<System.Int32>
struct TaskFactory_1_t0BEF06D58E44525B9135AB0B22D016856EE69FF3;
// System.Net.Sockets.Socket/TaskSocketAsyncEventArgs`1<System.Net.Sockets.Socket>
struct TaskSocketAsyncEventArgs_1_tEB937620E5B15D91E5BFEFFA707CF800930F8401;
// System.Threading.Tasks.Task`1<System.Net.IPAddress[]>
struct Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670;
// System.Threading.Tasks.Task`1<System.Boolean>
struct Task_1_t824317F4B958F7512E8F7300511752937A6C6043;
// System.Threading.Tasks.Task`1<System.Int32>
struct Task_1_t4C228DE57804012969575431CFF12D57C875552D;
// System.Threading.Tasks.Task`1<System.Object>
struct Task_1_t0C4CD3A5BB93A184420D73218644C56C70FDA7E2;
// System.Collections.Generic.Dictionary`2/ValueCollection<System.Threading.Thread,System.Diagnostics.StackTrace>
struct ValueCollection_tCC2B72D213C2E613E730B1FE330436D4E958A8F4;
// System.Collections.Generic.Dictionary`2/Entry<System.Threading.Thread,System.Diagnostics.StackTrace>[]
struct EntryU5BU5D_t3390CB5BCEED5561F8F18C9F69C877CD34A46B21;
// System.Byte[]
struct ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031;
// System.Char[]
struct CharU5BU5D_t799905CF001DD5F13F7DBB310181FC4D8B7D0AAB;
// Mono.Security.Interface.CipherSuiteCode[]
struct CipherSuiteCodeU5BU5D_t61EC0E6F53394985FFC36DEB587C70F4EE26D435;
// System.Delegate[]
struct DelegateU5BU5D_tC5AB7E8F745616680F337909D3A8E6C722CDF771;
// System.Net.IPAddress[]
struct IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D;
// System.Int32[]
struct Int32U5BU5D_t19C97395396A72ECAF310612F0760F165060314C;
// System.IntPtr[]
struct IntPtrU5BU5D_tFD177F8C806A6921AD7150264CCC62FA00CAD832;
// System.Object[]
struct ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918;
// System.Diagnostics.StackFrame[]
struct StackFrameU5BU5D_tF4310E8FAB8C2853A3F0843921BF9FBEC18ABBF7;
// System.Diagnostics.StackTrace[]
struct StackTraceU5BU5D_t32FBCB20930EAF5BAE3F450FF75228E5450DA0DF;
// System.String[]
struct StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248;
// System.Threading.Thread[]
struct ThreadU5BU5D_t9D34A6A5BE8998FAD6B8556B1E16BF0C836572B2;
// System.Type[]
struct TypeU5BU5D_t97234E1129B564EB38B8D85CAC2AD8B5B9522FFB;
// System.UInt16[]
struct UInt16U5BU5D_tEB7C42D811D999D2AA815BADC3FCCDD9C67B3F83;
// System.AggregateException
struct AggregateException_t51B6205846DFB356B94452702201DD239D44422F;
// System.ArgumentException
struct ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263;
// System.ArgumentNullException
struct ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129;
// System.ArgumentOutOfRangeException
struct ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F;
// System.Collections.ArrayList
struct ArrayList_t7A8E5AF0C4378015B5731ABE2BED8F2782FEEF8A;
// System.AsyncCallback
struct AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C;
// Mono.Net.Security.AsyncProtocolRequest
struct AsyncProtocolRequest_tF31CF30F8C57A3D5DC10A9FB0A5568FA254DFE86;
// System.Net.Security.AuthenticatedStream
struct AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39;
// System.Reflection.Binder
struct Binder_t91BFCE95A7057FADF4D8A1A342AFE52872246235;
// Mono.Net.Security.BufferOffsetSize2
struct BufferOffsetSize2_t2C3F7F42F64D84F357DC24AFCD44AC4679B20629;
// System.Net.Configuration.BypassElementCollection
struct BypassElementCollection_t15465BC07F8DC8BABA6953182904AF762D638DBE;
// System.Threading.CancellationTokenSource
struct CancellationTokenSource_tAAE1E0033BCFC233801F8CB4CED5C852B350CB7B;
// System.Configuration.ConfigurationElement
struct ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E;
// System.Configuration.ConfigurationPropertyCollection
struct ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93;
// System.Net.Configuration.ConnectionManagementElementCollection
struct ConnectionManagementElementCollection_tEEB0BEE8289364CCA1D76DDA3F862429612DE0F1;
// System.Net.Configuration.ConnectionManagementSection
struct ConnectionManagementSection_t8F4D45BE147F24F54F2124CC800B4594031F41C2;
// System.Threading.ContextCallback
struct ContextCallback_tE8AFBDBFCC040FDA8DA8C1EEFE9BD66B16BDA007;
// System.Globalization.CultureInfo
struct CultureInfo_t9BA817D41AD55AC8BD07480DD8AC22F8FFA378E0;
// System.Net.Configuration.DefaultProxySection
struct DefaultProxySection_tC0247A534955DC9D887B03AA4EFB1C1E01AD1E34;
// System.Delegate
struct Delegate_t;
// System.DelegateData
struct DelegateData_t9B286B493293CD2D23A5B2B5EF0E5B1324C2B77E;
// System.Diagnostics.DiagnosticsConfigurationHandler
struct DiagnosticsConfigurationHandler_t0C4B611E2C6EA17B288C43070FAF65F96DCA96E7;
// System.Net.EndPoint
struct EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564;
// System.EventArgs
struct EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377;
// System.Exception
struct Exception_t;
// System.Runtime.ExceptionServices.ExceptionDispatchInfo
struct ExceptionDispatchInfo_tD7AF19E75FEC22F4A8329FD1E9EDF96615CB2757;
// System.Threading.ExecutionContext
struct ExecutionContext_t9D6EDFD92F0B2D391751963E2D77A8B03CB81710;
// System.Net.Configuration.HttpWebRequestElement
struct HttpWebRequestElement_t8D2FEEC531D1FBE1B6E3D1077350A1EBDCB77DC3;
// System.IAsyncResult
struct IAsyncResult_t7B9B5A0ECB35DCEC31B8A8122C37D687369253B5;
// Mono.Security.Interface.ICertificateValidator
struct ICertificateValidator_t8ECF48A37EC708C8A16305F9B6B44C50676AA854;
// System.Collections.IDictionary
struct IDictionary_t6D03155AF1FA9083817AA5B6AD7DEEACC26AB220;
// System.Runtime.Serialization.IFormatterConverter
struct IFormatterConverter_t726606DAC82C384B08C82471313C340968DDB609;
// System.IOAsyncCallback
struct IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388;
// System.IOAsyncResult
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374;
// System.IOSelectorJob
struct IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31;
// System.Net.IPAddress
struct IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484;
// System.Net.IPEndPoint
struct IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB;
// System.Security.Principal.IPrincipal
struct IPrincipal_tE7AF5096287F6C3472585E124CB38FF2A51EAB5F;
// System.Threading.InternalThread
struct InternalThread_tF40B7BFCBD60C82BD8475A22FF5186CA10293687;
// System.InvalidOperationException
struct InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB;
// System.Net.Configuration.Ipv6Element
struct Ipv6Element_t480208D0E23E612DC1FD1CE19D9FB491F0F66932;
// System.Net.Sockets.LingerOption
struct LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98;
// System.Net.Security.LocalCertSelectionCallback
struct LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2;
// System.Net.Security.LocalCertificateSelectionCallback
struct LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859;
// System.LocalDataStoreHolder
struct LocalDataStoreHolder_t789DD474AE5141213C2105CE57830ECFC2D3C03F;
// System.LocalDataStoreMgr
struct LocalDataStoreMgr_t205F1783D5CC2B148E829B5882E5406FF9A3AC1E;
// System.Threading.ManualResetEvent
struct ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158;
// System.Reflection.MemberFilter
struct MemberFilter_tF644F1AE82F611B677CE1964D5A3277DDA21D553;
// System.Reflection.MethodInfo
struct MethodInfo_t;
// Mono.Net.Security.MobileAuthenticatedStream
struct MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E;
// Mono.Net.Security.MobileTlsContext
struct MobileTlsContext_t456DDC89866EE34EF32EEF959AD92C9F17684476;
// Mono.Net.Security.MobileTlsProvider
struct MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017;
// Mono.Security.Interface.MonoLocalCertificateSelectionCallback
struct MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3;
// Mono.Security.Interface.MonoRemoteCertificateValidationCallback
struct MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0;
// Mono.Security.Interface.MonoTlsProvider
struct MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E;
// Mono.Security.Interface.MonoTlsSettings
struct MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0;
// System.MulticastDelegate
struct MulticastDelegate_t;
// System.Net.Configuration.NetSectionGroup
struct NetSectionGroup_tA83DCAF89773087D0E921B92F67441132B71D52F;
// System.Net.Sockets.NetworkStream
struct NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0;
// System.NotSupportedException
struct NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A;
// System.ObjectDisposedException
struct ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB;
// System.OperationCanceledException
struct OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662;
// System.Net.Configuration.PerformanceCountersElement
struct PerformanceCountersElement_tCB62C8CA7C218E35653DC5BB690A5FFD6D608B30;
// System.PlatformNotSupportedException
struct PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A;
// System.Net.Configuration.ProxyElement
struct ProxyElement_t4D277820F9C77333BE8CC6498DE7E06C36868C65;
// System.Net.Security.RemoteCertificateValidationCallback
struct RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955;
// System.Runtime.InteropServices.SafeHandle
struct SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7;
// Microsoft.Win32.SafeHandles.SafeHandleMinusOneIsInvalid
struct SafeHandleMinusOneIsInvalid_t85F57717BC66C06B714910FA13BF2E49AA5BD1A6;
// System.Runtime.Serialization.SafeSerializationManager
struct SafeSerializationManager_tCBB85B95DFD1634237140CD892E82D06ECB3F5E6;
// System.Net.Sockets.SafeSocketHandle
struct SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE;
// Microsoft.Win32.SafeHandles.SafeWaitHandle
struct SafeWaitHandle_t58F5662CD56F6462A687198A64987F8980804449;
// System.Threading.SemaphoreSlim
struct SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2;
// System.Runtime.Serialization.SerializationInfo
struct SerializationInfo_t3C47F63E24BEB9FCE2DC6309E027F238DC5C5E37;
// System.Net.Security.ServerCertSelectionCallback
struct ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654;
// System.Net.Configuration.ServicePointManagerElement
struct ServicePointManagerElement_t98ED8374270537E77E57D014ECEE8B1A3A8EEFF2;
// System.Net.Configuration.SettingsSection
struct SettingsSection_tC931BA69B22D5E0ED07698F02778482987319A12;
// System.Net.Sockets.Socket
struct Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E;
// System.Net.Sockets.SocketAsyncEventArgs
struct SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC;
// System.Net.Sockets.SocketAsyncResult
struct SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340;
// System.Net.Configuration.SocketElement
struct SocketElement_tDC83FCE3C280D8D590972F989292C30E2B7F6CC6;
// System.Net.Sockets.SocketException
struct SocketException_t6D10102A62EA871BD31748E026A372DB6804083B;
// System.Net.Security.SslClientAuthenticationOptions
struct SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9;
// System.Net.Security.SslStream
struct SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27;
// System.Threading.Tasks.StackGuard
struct StackGuard_tACE063A1B7374BDF4AD472DE4585D05AD8745352;
// System.Diagnostics.StackTrace
struct StackTrace_t7C150C7C14136F985311A83A93524B1019F70853;
// System.IO.Stream
struct Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE;
// System.String
struct String_t;
// System.Text.StringBuilder
struct StringBuilder_t;
// System.Threading.Tasks.Task
struct Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572;
// System.Threading.Tasks.TaskFactory
struct TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0;
// System.Threading.Tasks.TaskScheduler
struct TaskScheduler_t3F0550EBEF7C41F74EC8C08FF4BED0D8CE66006E;
// System.Net.Sockets.TcpClient
struct TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58;
// System.Threading.Thread
struct Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F;
// System.Type
struct Type_t;
// System.Void
struct Void_t4861ACF8F4594C3437BB48B6E56783494B843915;
// System.Threading.WaitCallback
struct WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3;
// System.Net.Configuration.WebProxyScriptElement
struct WebProxyScriptElement_t0C0E2338F7F42D5D386F5AE68FF7F870FA6E7167;
// System.Net.Configuration.WebRequestModuleElementCollection
struct WebRequestModuleElementCollection_t5979A19A48B822831C1D351EB2AA25058C7F7463;
// System.Net.Configuration.WebRequestModulesSection
struct WebRequestModulesSection_t4099F9890D7851314C5528C577E4C51B36C97D00;
// System.ComponentModel.Win32Exception
struct Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9;
// System.Security.Cryptography.X509Certificates.X509Certificate
struct X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4;
// System.Security.Cryptography.X509Certificates.X509CertificateCollection
struct X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE;
// System.Security.Cryptography.X509Certificates.X509CertificateImpl
struct X509CertificateImpl_tF590E81705CE1FE152C590E5A875D4FE3BE348EF;
// System.Security.Cryptography.X509Certificates.X509Chain
struct X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5;
// System.Security.Cryptography.X509Certificates.X509ChainImpl
struct X509ChainImpl_tBA1BF154DB9DC321EE068BBC53C7CF43CAA2621F;
// System.Xml.XmlNode
struct XmlNode_t3180B9B3D5C36CD58F5327D9F13458E3B3F030AF;
// System.Threading.SemaphoreSlim/TaskNode
struct TaskNode_t082B7F2BBC9759BA9B6AD5F3760FE76BD5C6BA24;
// System.Net.Sockets.Socket/<>c__DisplayClass240_0
struct U3CU3Ec__DisplayClass240_0_t99833891E615E48FC65509B7FAFD7A266C1515F3;
// System.Net.Sockets.Socket/<>c__DisplayClass298_0
struct U3CU3Ec__DisplayClass298_0_t508378F2011A6BCD457AA7612AAD1D4AF537825E;
// System.Net.Sockets.Socket/<>c__DisplayClass355_0
struct U3CU3Ec__DisplayClass355_0_tE1E6F6152220887E65219B2C9CA1073F6F5D8819;
// System.Net.Sockets.Socket/CachedEventArgs
struct CachedEventArgs_tF0692E89962FD1A045B17BC985F838C11FB6822C;
// System.Net.Sockets.Socket/Int32TaskSocketAsyncEventArgs
struct Int32TaskSocketAsyncEventArgs_t36C5FC82499ED9DAFE7F05C38EF92D77A0B248E9;
// System.Net.Sockets.SocketAsyncResult/<>c
struct U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45;
// System.Net.Security.SslStream/<>c__DisplayClass21_0
struct U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C;
// System.IO.Stream/ReadWriteTask
struct ReadWriteTask_t0821BF49EE38596C7734E86E1A6A39D769BE2C05;
// System.Threading.Tasks.Task/ContingentProperties
struct ContingentProperties_t3FA59480914505CEA917B1002EC675F29D0CB540;

IL2CPP_EXTERN_C RuntimeClass* Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Exception_t_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* GC_t920F9CF6EBB7C787E5010A4352E1B587F356DC58_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* IDisposable_t030E0496B4E0E4E4F086825007979AF51F7248C5_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* IntPtr_t_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* StackTrace_t7C150C7C14136F985311A83A93524B1019F70853_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* StringBuilder_t_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* ValidationHelper_tA6AE681FCDFA9BE8E3EF31C28A624AADF973A25C_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeClass* WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3_il2cpp_TypeInfo_var;
IL2CPP_EXTERN_C RuntimeField* SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var;
IL2CPP_EXTERN_C String_t* _stringLiteral143956766707E0543589DAB7DBE2742B2B993D2E;
IL2CPP_EXTERN_C String_t* _stringLiteral1F40F39B9F659411903AC93DF93E8658291E5522;
IL2CPP_EXTERN_C String_t* _stringLiteral215B9008FEB54933AF219588889451A0CB610D34;
IL2CPP_EXTERN_C String_t* _stringLiteral2386E77CF610F786B06A91AF2C1B3FD2282D2745;
IL2CPP_EXTERN_C String_t* _stringLiteral319D1EB2B79ACDA1B65E4BA4BA666745E69FEECF;
IL2CPP_EXTERN_C String_t* _stringLiteral46F273EF641E07D271D91E0DC24A4392582671F8;
IL2CPP_EXTERN_C String_t* _stringLiteral49FB20AF7D832AF20AD2F4170339D473684018D4;
IL2CPP_EXTERN_C String_t* _stringLiteral670E6E5CD786FBA46F50395F2FE281B1DF2C9985;
IL2CPP_EXTERN_C String_t* _stringLiteral6C52C8D495F05CFD54188F5CD5E5C6E98067B112;
IL2CPP_EXTERN_C String_t* _stringLiteral6F581DC31922CB8BA16D8D1268547106C0D2A599;
IL2CPP_EXTERN_C String_t* _stringLiteral6FE6AA6EF03FB6550152903A7B201A6108F728DB;
IL2CPP_EXTERN_C String_t* _stringLiteral73E7438EC9EFF139A2E1CEA81F03B87F9EF5CF0A;
IL2CPP_EXTERN_C String_t* _stringLiteral7432BC139FBCA09AC74A0F2BAE3FBB728A59E72D;
IL2CPP_EXTERN_C String_t* _stringLiteral7A62F4EEAA031F30B545A398D12F81D6413DE012;
IL2CPP_EXTERN_C String_t* _stringLiteral8F37F8E27E5857460347BD55FFBFB98FCC58AC10;
IL2CPP_EXTERN_C String_t* _stringLiteral94EC39D9CD015C12CA91AF28F761F4059A1C6437;
IL2CPP_EXTERN_C String_t* _stringLiteral9A1C57A14DBAA067A98C8B33FA6B28D793AFE67D;
IL2CPP_EXTERN_C String_t* _stringLiteralA6EAFAF4139980ACB770E237272125EB9180B7A6;
IL2CPP_EXTERN_C String_t* _stringLiteralC346DF19BCA13733A62F157B7D32A1ADA5023CC6;
IL2CPP_EXTERN_C String_t* _stringLiteralCB9340620E0F239BC39D9B1AA9CCF6119A0BB3FF;
IL2CPP_EXTERN_C String_t* _stringLiteralCDBA3FD0CA21972DFCCD218E38E1454CA7EEE93B;
IL2CPP_EXTERN_C String_t* _stringLiteralD6B94B0798130EA7E48D45E575293E70EDAC7151;
IL2CPP_EXTERN_C String_t* _stringLiteralED7D39EDF70E08FE57B2522D102BF72B9E5D5331;
IL2CPP_EXTERN_C String_t* _stringLiteralF17F1002CEBE6D8756D7A588CEF423B082021CD0;
IL2CPP_EXTERN_C String_t* _stringLiteralF95A5F2D2E318732DB7B00E08A46A31CDE16171E;
IL2CPP_EXTERN_C String_t* _stringLiteralF9714C70F08B15BEF4414BD33A16130BD670348F;
IL2CPP_EXTERN_C String_t* _stringLiteralFCC60170DC9ECA079BBFC781ACD49B93D0E8C4AE;
IL2CPP_EXTERN_C const RuntimeMethod* AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* BypassElementCollection__ctor_m344EC8CD2EACA36E1213C5914037BB133E87FE54_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ConnectionManagementElementCollection__ctor_m4698342A715032A762FA04B2745BFCA46263BCB3_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ConnectionManagementSection__ctor_m2FA1D29F887BD6498FBEF692BBC0AA727E38E802_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ConnectionManagementSection_get_Properties_mFCE0F9418E9E8CD2E1301815CC4D548261F58E95_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* DefaultProxySection_Reset_m0B9A1155840E7C55FC24AEDB23FB17A2815EEB77_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* DefaultProxySection__ctor_m962B68CEFCDE7DFB7646EF16297CA4BE0097284D_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* DefaultProxySection_get_Properties_m5186F1A2734EB96F065C7BC14A215D17F8325418_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* DiagnosticsConfigurationHandler_Create_mF21B1CC68B87DE078690B2B493FCDC5F728CBAAB_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* DiagnosticsConfigurationHandler__ctor_mDFB0000E6218840F66D21BB6C9BF3F647CCBC5A3_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Dictionary_2_Add_m021BDE42D4C8CFA7729AE1FA4645832CB8510A45_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Dictionary_2_Remove_m43BD8FD04F14C57EA9B7F4017447E918FA3B2557_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Dictionary_2__ctor_m50735B02E2653B889CED4DFEFA0C22917F620C78_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Dictionary_2_get_Item_m5B51F010936EF5BBF6820068B9143D37CB2D2D84_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* HttpWebRequestElement__ctor_mB9DDAC7FF431D4AF4CAFCBC96E1A3572A1F83717_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* HttpWebRequestElement_get_Properties_mAA0F11EA73B5397C50809EAC679F2C564F9EC7CA_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Ipv6Element__ctor_m869AC261E0994A8B3D83CEE25C6CB99E6A5EA4C1_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Ipv6Element_get_Properties_m382D5B5B098EFF19C2A4AC1889CD1C8EAB918322_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_Add_m5F800AC3A9BEE2C4459A42828FB152CD7A848A8C_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_IndexOf_m005D5A092A56062F7E23CE7234826D77A54AAFC2_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_Remove_m6BC6C5F209825F38868B72599159757677635886_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1__ctor_m1104DB0524C8D81E9C97C6068CC22A52CCE146EC_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* List_1_get_Item_m60371F3C4A4481F126B9BDBFDD9D69F464780693_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* NetSectionGroup__ctor_mCD2F0DC2B9F0C6C346BC91BD8750C7111A9528AC_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* PerformanceCountersElement__ctor_m9537195CBC7DC607F7253D06E7B6B1090456E68F_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* PerformanceCountersElement_get_Properties_m7C16FBC3E915E8384FB4409209628737B1537C9B_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ProxyElement__ctor_mC1BDF426F97F4F84CB5A34B423F59EC8748EBD70_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ProxyElement_get_Properties_mBFE6E283FE7E9329B586ACAADC8F2E047C7ABB2A_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SafeSocketHandle_RegisterForBlockingSyscall_mFCDDEAB42C472D9A8E46B76C0BAECEB7D1832A98_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SafeSocketHandle_ReleaseHandle_m6861A54261EEC98F3DB4BB27251AA7ED6BD4DCE3_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ServicePointManagerElement__ctor_m0F1CF21589CA283DA734A472930A9665698B2D36_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ServicePointManagerElement_get_Properties_m70469523AAD1A2C2E9D4F90DDA1BA619FCE68B58_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SettingsSection__ctor_mBD94E7E098221DC09775A3AE1713930F6820D242_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SettingsSection_get_Properties_mA774C9C884F4AD5DEC2BBE31DF7E35CAC71EBF8F_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SocketAsyncEventArgs_SetBuffer_m442BE1EFE47E011A747645F5A1F31F0702852EF7_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SocketAsyncEventArgs_SetLastOperation_m73C3FAA754E365C569C956149E6E10E9739DBB17_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SocketAsyncResult_CheckIfThrowDelayedException_m25275FD1C77F37FB2CFDE46BDC669CF9B31031C9_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SocketElement__ctor_m4FB4C851F1BB20CED74C206EFB1BA9DDFBE024D2_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SocketElement_get_Properties_m8AF94DEEDA0EB4092F59FBDC361592E1B9EBDEC3_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslClientAuthenticationOptions_set_CertificateRevocationCheckMode_m5DB15346690DAC5A8F610AA8C7DEC7F2DFC29DE9_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslClientAuthenticationOptions_set_EncryptionPolicy_m008E6F28D3C4BFEFFB32A9BEA6AEF1C2B53F1C77_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslStream_Seek_mFEDBF392998DDDB0F7A71F25D1BB821EA1463B25_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslStream_SetAndVerifySelectionCallback_m8B82CA433CF92AD113F606ACE7229D442587DA0B_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslStream_SetAndVerifyValidationCallback_m6FA4BE931FA1C7BC5D175B4E1CA2959742FA9C9D_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* SslStream_set_Position_m70987A87C7DB346D2FD447EA11E96774105EE5D2_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TaskFactory_FromAsync_TisString_t_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mD129D3A417906F121881179FDB395C2AC54E94BC_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* Task_1_get_Result_m0F2300B3BCA792B7EFFAF002F88E53F5DFC64394_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient_BeginConnect_mBE3D6DA720BCE275EABB903E75D8CD19BB52083B_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient_EndConnect_m7A72422D31A93819CAAAA16FC663F729D2B56300_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient_GetStream_mDD54336B17D1267BD593E0A1EB9EDF3E9506AEBA_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* TcpClient__ctor_m94DC92A387808F584010C708C071197B52B48B55_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* ThrowStub_ThrowNotSupportedException_m14168B43936EF78B7B8AC08BD815254DAA1D9FA2_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* U3CU3Ec_U3CCompleteU3Eb__27_0_m267CAA8BCD77C0263EF3DAC0336FC29A937D2C76_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* U3CU3Ec__DisplayClass21_0_U3CSetAndVerifySelectionCallbackU3Eb__0_mADCFBC85C66A800C57CD2CECA4CDB6A9E5BB020D_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* WebProxyScriptElement__ctor_mB613D71DE569317E40726CD8DB9A6ABA0A302821_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* WebProxyScriptElement_get_Properties_mF5188225C0C1D64BC136465BB4B0E962FFE9240B_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* WebRequestModuleElementCollection__ctor_m62C156D78336B2E37EA6F10D477FDEAE4161F99E_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* WebRequestModulesSection__ctor_mF25CD80A5111A4F87B7FBF7663A3442470B97C0E_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeMethod* WebRequestModulesSection_get_Properties_mBDBDED569E677717FB617CD36B94AE34BF4208F6_RuntimeMethod_var;
IL2CPP_EXTERN_C const RuntimeType* SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var;
struct Delegate_t_marshaled_com;
struct Delegate_t_marshaled_pinvoke;
struct Exception_t;;
struct Exception_t_marshaled_com;
struct Exception_t_marshaled_com;;
struct Exception_t_marshaled_pinvoke;
struct Exception_t_marshaled_pinvoke;;
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_com;
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_pinvoke;

struct ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031;
struct DelegateU5BU5D_tC5AB7E8F745616680F337909D3A8E6C722CDF771;
struct IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D;
struct ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918;
struct StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248;

IL2CPP_EXTERN_C_BEGIN
IL2CPP_EXTERN_C_END

#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif

// System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>
struct Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED  : public RuntimeObject
{
	// System.Int32[] System.Collections.Generic.Dictionary`2::_buckets
	Int32U5BU5D_t19C97395396A72ECAF310612F0760F165060314C* ____buckets_0;
	// System.Collections.Generic.Dictionary`2/Entry<TKey,TValue>[] System.Collections.Generic.Dictionary`2::_entries
	EntryU5BU5D_t3390CB5BCEED5561F8F18C9F69C877CD34A46B21* ____entries_1;
	// System.Int32 System.Collections.Generic.Dictionary`2::_count
	int32_t ____count_2;
	// System.Int32 System.Collections.Generic.Dictionary`2::_freeList
	int32_t ____freeList_3;
	// System.Int32 System.Collections.Generic.Dictionary`2::_freeCount
	int32_t ____freeCount_4;
	// System.Int32 System.Collections.Generic.Dictionary`2::_version
	int32_t ____version_5;
	// System.Collections.Generic.IEqualityComparer`1<TKey> System.Collections.Generic.Dictionary`2::_comparer
	RuntimeObject* ____comparer_6;
	// System.Collections.Generic.Dictionary`2/KeyCollection<TKey,TValue> System.Collections.Generic.Dictionary`2::_keys
	KeyCollection_t680178B5E16E74DDA4B763692AF82BE84A13BE1C* ____keys_7;
	// System.Collections.Generic.Dictionary`2/ValueCollection<TKey,TValue> System.Collections.Generic.Dictionary`2::_values
	ValueCollection_tCC2B72D213C2E613E730B1FE330436D4E958A8F4* ____values_8;
	// System.Object System.Collections.Generic.Dictionary`2::_syncRoot
	RuntimeObject* ____syncRoot_9;
};

// System.Collections.Generic.List`1<System.Object>
struct List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D  : public RuntimeObject
{
	// T[] System.Collections.Generic.List`1::_items
	ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* ____items_1;
	// System.Int32 System.Collections.Generic.List`1::_size
	int32_t ____size_2;
	// System.Int32 System.Collections.Generic.List`1::_version
	int32_t ____version_3;
	// System.Object System.Collections.Generic.List`1::_syncRoot
	RuntimeObject* ____syncRoot_4;
};

// System.Collections.Generic.List`1<System.Threading.Thread>
struct List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D  : public RuntimeObject
{
	// T[] System.Collections.Generic.List`1::_items
	ThreadU5BU5D_t9D34A6A5BE8998FAD6B8556B1E16BF0C836572B2* ____items_1;
	// System.Int32 System.Collections.Generic.List`1::_size
	int32_t ____size_2;
	// System.Int32 System.Collections.Generic.List`1::_version
	int32_t ____version_3;
	// System.Object System.Collections.Generic.List`1::_syncRoot
	RuntimeObject* ____syncRoot_4;
};

// <PrivateImplementationDetails>
struct U3CPrivateImplementationDetailsU3E_t56F624E1051A2E261613B6A81CA2333397F49CB3  : public RuntimeObject
{
};

// System.Collections.CollectionBase
struct CollectionBase_t44F966CC555C87F2815D668FB4586526E1C2383F  : public RuntimeObject
{
	// System.Collections.ArrayList System.Collections.CollectionBase::_list
	ArrayList_t7A8E5AF0C4378015B5731ABE2BED8F2782FEEF8A* ____list_0;
};

// System.Configuration.ConfigurationElement
struct ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E  : public RuntimeObject
{
};

// System.Configuration.ConfigurationPropertyCollection
struct ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93  : public RuntimeObject
{
};

// System.Configuration.ConfigurationSectionGroup
struct ConfigurationSectionGroup_tE7948C2D31B193F4BA8828947ED3094B952C7863  : public RuntimeObject
{
};

// System.Runtime.ConstrainedExecution.CriticalFinalizerObject
struct CriticalFinalizerObject_t1DCAB623CAEA6529A96F5F3EDE3C7048A6E313C9  : public RuntimeObject
{
};

// System.Diagnostics.DiagnosticsConfigurationHandler
struct DiagnosticsConfigurationHandler_t0C4B611E2C6EA17B288C43070FAF65F96DCA96E7  : public RuntimeObject
{
};

// System.Net.EndPoint
struct EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564  : public RuntimeObject
{
};

// System.EventArgs
struct EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377  : public RuntimeObject
{
};

// System.IOAsyncResult
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374  : public RuntimeObject
{
	// System.AsyncCallback System.IOAsyncResult::async_callback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___async_callback_0;
	// System.Object System.IOAsyncResult::async_state
	RuntimeObject* ___async_state_1;
	// System.Threading.ManualResetEvent System.IOAsyncResult::wait_handle
	ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158* ___wait_handle_2;
	// System.Boolean System.IOAsyncResult::completed_synchronously
	bool ___completed_synchronously_3;
	// System.Boolean System.IOAsyncResult::completed
	bool ___completed_4;
};
// Native definition for P/Invoke marshalling of System.IOAsyncResult
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_pinvoke
{
	Il2CppMethodPointer ___async_callback_0;
	Il2CppIUnknown* ___async_state_1;
	ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158* ___wait_handle_2;
	int32_t ___completed_synchronously_3;
	int32_t ___completed_4;
};
// Native definition for COM marshalling of System.IOAsyncResult
struct IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_com
{
	Il2CppMethodPointer ___async_callback_0;
	Il2CppIUnknown* ___async_state_1;
	ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158* ___wait_handle_2;
	int32_t ___completed_synchronously_3;
	int32_t ___completed_4;
};

// System.Net.IPAddress
struct IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484  : public RuntimeObject
{
	// System.UInt32 System.Net.IPAddress::_addressOrScopeId
	uint32_t ____addressOrScopeId_8;
	// System.UInt16[] System.Net.IPAddress::_numbers
	UInt16U5BU5D_tEB7C42D811D999D2AA815BADC3FCCDD9C67B3F83* ____numbers_9;
	// System.String System.Net.IPAddress::_toString
	String_t* ____toString_10;
	// System.Int32 System.Net.IPAddress::_hashCode
	int32_t ____hashCode_11;
};

// System.Net.Sockets.LingerOption
struct LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98  : public RuntimeObject
{
	// System.Boolean System.Net.Sockets.LingerOption::enabled
	bool ___enabled_0;
	// System.Int32 System.Net.Sockets.LingerOption::lingerTime
	int32_t ___lingerTime_1;
};

// System.MarshalByRefObject
struct MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE  : public RuntimeObject
{
	// System.Object System.MarshalByRefObject::_identity
	RuntimeObject* ____identity_0;
};
// Native definition for P/Invoke marshalling of System.MarshalByRefObject
struct MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE_marshaled_pinvoke
{
	Il2CppIUnknown* ____identity_0;
};
// Native definition for COM marshalling of System.MarshalByRefObject
struct MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE_marshaled_com
{
	Il2CppIUnknown* ____identity_0;
};

// System.Reflection.MemberInfo
struct MemberInfo_t  : public RuntimeObject
{
};

// Mono.Security.Interface.MonoTlsProvider
struct MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E  : public RuntimeObject
{
};

// System.Net.Sockets.MulticastOption
struct MulticastOption_t6DBC11706A7AE3B49A790A67619A09268D7EB4A1  : public RuntimeObject
{
};

// System.Threading.SemaphoreSlim
struct SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2  : public RuntimeObject
{
	// System.Int32 modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.SemaphoreSlim::m_currentCount
	int32_t ___m_currentCount_0;
	// System.Int32 System.Threading.SemaphoreSlim::m_maxCount
	int32_t ___m_maxCount_1;
	// System.Int32 modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.SemaphoreSlim::m_waitCount
	int32_t ___m_waitCount_2;
	// System.Object System.Threading.SemaphoreSlim::m_lockObj
	RuntimeObject* ___m_lockObj_3;
	// System.Threading.ManualResetEvent modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.SemaphoreSlim::m_waitHandle
	ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158* ___m_waitHandle_4;
	// System.Threading.SemaphoreSlim/TaskNode System.Threading.SemaphoreSlim::m_asyncHead
	TaskNode_t082B7F2BBC9759BA9B6AD5F3760FE76BD5C6BA24* ___m_asyncHead_5;
	// System.Threading.SemaphoreSlim/TaskNode System.Threading.SemaphoreSlim::m_asyncTail
	TaskNode_t082B7F2BBC9759BA9B6AD5F3760FE76BD5C6BA24* ___m_asyncTail_6;
};

// System.Runtime.Serialization.SerializationInfo
struct SerializationInfo_t3C47F63E24BEB9FCE2DC6309E027F238DC5C5E37  : public RuntimeObject
{
	// System.String[] System.Runtime.Serialization.SerializationInfo::m_members
	StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___m_members_3;
	// System.Object[] System.Runtime.Serialization.SerializationInfo::m_data
	ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* ___m_data_4;
	// System.Type[] System.Runtime.Serialization.SerializationInfo::m_types
	TypeU5BU5D_t97234E1129B564EB38B8D85CAC2AD8B5B9522FFB* ___m_types_5;
	// System.Collections.Generic.Dictionary`2<System.String,System.Int32> System.Runtime.Serialization.SerializationInfo::m_nameToIndex
	Dictionary_2_t5C8F46F5D57502270DD9E1DA8303B23C7FE85588* ___m_nameToIndex_6;
	// System.Int32 System.Runtime.Serialization.SerializationInfo::m_currMember
	int32_t ___m_currMember_7;
	// System.Runtime.Serialization.IFormatterConverter System.Runtime.Serialization.SerializationInfo::m_converter
	RuntimeObject* ___m_converter_8;
	// System.String System.Runtime.Serialization.SerializationInfo::m_fullTypeName
	String_t* ___m_fullTypeName_9;
	// System.String System.Runtime.Serialization.SerializationInfo::m_assemName
	String_t* ___m_assemName_10;
	// System.Type System.Runtime.Serialization.SerializationInfo::objectType
	Type_t* ___objectType_11;
	// System.Boolean System.Runtime.Serialization.SerializationInfo::isFullTypeNameSetExplicit
	bool ___isFullTypeNameSetExplicit_12;
	// System.Boolean System.Runtime.Serialization.SerializationInfo::isAssemblyNameSetExplicit
	bool ___isAssemblyNameSetExplicit_13;
	// System.Boolean System.Runtime.Serialization.SerializationInfo::requireSameTokenInPartialTrust
	bool ___requireSameTokenInPartialTrust_14;
};

// System.Diagnostics.StackTrace
struct StackTrace_t7C150C7C14136F985311A83A93524B1019F70853  : public RuntimeObject
{
	// System.Diagnostics.StackFrame[] System.Diagnostics.StackTrace::frames
	StackFrameU5BU5D_tF4310E8FAB8C2853A3F0843921BF9FBEC18ABBF7* ___frames_0;
	// System.Diagnostics.StackTrace[] System.Diagnostics.StackTrace::captured_traces
	StackTraceU5BU5D_t32FBCB20930EAF5BAE3F450FF75228E5450DA0DF* ___captured_traces_1;
	// System.Boolean System.Diagnostics.StackTrace::debug_info
	bool ___debug_info_2;
};

// System.String
struct String_t  : public RuntimeObject
{
	// System.Int32 System.String::_stringLength
	int32_t ____stringLength_4;
	// System.Char System.String::_firstChar
	Il2CppChar ____firstChar_5;
};

// System.Text.StringBuilder
struct StringBuilder_t  : public RuntimeObject
{
	// System.Char[] System.Text.StringBuilder::m_ChunkChars
	CharU5BU5D_t799905CF001DD5F13F7DBB310181FC4D8B7D0AAB* ___m_ChunkChars_0;
	// System.Text.StringBuilder System.Text.StringBuilder::m_ChunkPrevious
	StringBuilder_t* ___m_ChunkPrevious_1;
	// System.Int32 System.Text.StringBuilder::m_ChunkLength
	int32_t ___m_ChunkLength_2;
	// System.Int32 System.Text.StringBuilder::m_ChunkOffset
	int32_t ___m_ChunkOffset_3;
	// System.Int32 System.Text.StringBuilder::m_MaxCapacity
	int32_t ___m_MaxCapacity_4;
};

// System.Threading.Tasks.Task
struct Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572  : public RuntimeObject
{
	// System.Int32 modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.Tasks.Task::m_taskId
	int32_t ___m_taskId_1;
	// System.Delegate System.Threading.Tasks.Task::m_action
	Delegate_t* ___m_action_2;
	// System.Object System.Threading.Tasks.Task::m_stateObject
	RuntimeObject* ___m_stateObject_3;
	// System.Threading.Tasks.TaskScheduler System.Threading.Tasks.Task::m_taskScheduler
	TaskScheduler_t3F0550EBEF7C41F74EC8C08FF4BED0D8CE66006E* ___m_taskScheduler_4;
	// System.Threading.Tasks.Task System.Threading.Tasks.Task::m_parent
	Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* ___m_parent_5;
	// System.Int32 modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.Tasks.Task::m_stateFlags
	int32_t ___m_stateFlags_6;
	// System.Object modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.Tasks.Task::m_continuationObject
	RuntimeObject* ___m_continuationObject_7;
	// System.Threading.Tasks.Task/ContingentProperties modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.Tasks.Task::m_contingentProperties
	ContingentProperties_t3FA59480914505CEA917B1002EC675F29D0CB540* ___m_contingentProperties_10;
};

// System.ValueType
struct ValueType_t6D9B272BD21782F0A9A14F2E41F85A50E97A986F  : public RuntimeObject
{
};
// Native definition for P/Invoke marshalling of System.ValueType
struct ValueType_t6D9B272BD21782F0A9A14F2E41F85A50E97A986F_marshaled_pinvoke
{
};
// Native definition for COM marshalling of System.ValueType
struct ValueType_t6D9B272BD21782F0A9A14F2E41F85A50E97A986F_marshaled_com
{
};

// System.Security.Cryptography.X509Certificates.X509Chain
struct X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5  : public RuntimeObject
{
	// System.Security.Cryptography.X509Certificates.X509ChainImpl System.Security.Cryptography.X509Certificates.X509Chain::impl
	X509ChainImpl_tBA1BF154DB9DC321EE068BBC53C7CF43CAA2621F* ___impl_0;
};

// System.Xml.XmlNode
struct XmlNode_t3180B9B3D5C36CD58F5327D9F13458E3B3F030AF  : public RuntimeObject
{
	// System.Xml.XmlNode System.Xml.XmlNode::parentNode
	XmlNode_t3180B9B3D5C36CD58F5327D9F13458E3B3F030AF* ___parentNode_0;
};

// System.Net.Sockets.Socket/<>c__DisplayClass240_0
struct U3CU3Ec__DisplayClass240_0_t99833891E615E48FC65509B7FAFD7A266C1515F3  : public RuntimeObject
{
	// System.Net.Sockets.SocketAsyncResult System.Net.Sockets.Socket/<>c__DisplayClass240_0::sockares
	SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* ___sockares_0;
};

// System.Net.Sockets.Socket/<>c__DisplayClass298_0
struct U3CU3Ec__DisplayClass298_0_t508378F2011A6BCD457AA7612AAD1D4AF537825E  : public RuntimeObject
{
	// System.Int32 System.Net.Sockets.Socket/<>c__DisplayClass298_0::sent_so_far
	int32_t ___sent_so_far_0;
};

// System.Net.Sockets.SocketAsyncResult/<>c
struct U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45  : public RuntimeObject
{
};

// System.Net.Security.SslStream/<>c__DisplayClass21_0
struct U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C  : public RuntimeObject
{
	// System.Net.Security.LocalCertificateSelectionCallback System.Net.Security.SslStream/<>c__DisplayClass21_0::callback
	LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___callback_0;
	// System.Net.Security.SslStream System.Net.Security.SslStream/<>c__DisplayClass21_0::<>4__this
	SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* ___U3CU3E4__this_1;
};

// System.Collections.Generic.List`1/Enumerator<System.Object>
struct Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A 
{
	// System.Collections.Generic.List`1<T> System.Collections.Generic.List`1/Enumerator::_list
	List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* ____list_0;
	// System.Int32 System.Collections.Generic.List`1/Enumerator::_index
	int32_t ____index_1;
	// System.Int32 System.Collections.Generic.List`1/Enumerator::_version
	int32_t ____version_2;
	// T System.Collections.Generic.List`1/Enumerator::_current
	RuntimeObject* ____current_3;
};

// System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>
struct Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 
{
	// System.Collections.Generic.List`1<T> System.Collections.Generic.List`1/Enumerator::_list
	List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* ____list_0;
	// System.Int32 System.Collections.Generic.List`1/Enumerator::_index
	int32_t ____index_1;
	// System.Int32 System.Collections.Generic.List`1/Enumerator::_version
	int32_t ____version_2;
	// T System.Collections.Generic.List`1/Enumerator::_current
	Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ____current_3;
};

// System.Memory`1<System.Byte>
struct Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 
{
	// System.Object System.Memory`1::_object
	RuntimeObject* ____object_0;
	// System.Int32 System.Memory`1::_index
	int32_t ____index_1;
	// System.Int32 System.Memory`1::_length
	int32_t ____length_2;
};
// Native definition for P/Invoke marshalling of System.Memory`1
#ifndef Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_pinvoke_define
#define Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_pinvoke_define
struct Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_pinvoke
{
	Il2CppIUnknown* ____object_0;
	int32_t ____index_1;
	int32_t ____length_2;
};
#endif
// Native definition for COM marshalling of System.Memory`1
#ifndef Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_com_define
#define Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_com_define
struct Memory_1_t56F63672B8E752B13E0BBBBD034BA3C1F6CFDC17_marshaled_com
{
	Il2CppIUnknown* ____object_0;
	int32_t ____index_1;
	int32_t ____length_2;
};
#endif

// System.Nullable`1<System.Boolean>
struct Nullable_1_t78F453FADB4A9F50F267A4E349019C34410D1A01 
{
	// System.Boolean System.Nullable`1::hasValue
	bool ___hasValue_0;
	// T System.Nullable`1::value
	bool ___value_1;
};

// System.Threading.Tasks.Task`1<System.Net.IPAddress[]>
struct Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670  : public Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572
{
	// TResult System.Threading.Tasks.Task`1::m_result
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* ___m_result_22;
};

// System.Threading.Tasks.Task`1<System.Int32>
struct Task_1_t4C228DE57804012969575431CFF12D57C875552D  : public Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572
{
	// TResult System.Threading.Tasks.Task`1::m_result
	int32_t ___m_result_22;
};

// System.Boolean
struct Boolean_t09A6377A54BE2F9E6985A8149F19234FD7DDFE22 
{
	// System.Boolean System.Boolean::m_value
	bool ___m_value_0;
};

// System.Net.Configuration.BypassElement
struct BypassElement_t1626DF2ACB357F8677EA94142CFA984569ADB4DC  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Byte
struct Byte_t94D9231AC217BE4D2E004C4CD32DF6D099EA41A3 
{
	// System.Byte System.Byte::m_value
	uint8_t ___m_value_0;
};

// System.Threading.CancellationToken
struct CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED 
{
	// System.Threading.CancellationTokenSource System.Threading.CancellationToken::_source
	CancellationTokenSource_tAAE1E0033BCFC233801F8CB4CED5C852B350CB7B* ____source_0;
};
// Native definition for P/Invoke marshalling of System.Threading.CancellationToken
struct CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED_marshaled_pinvoke
{
	CancellationTokenSource_tAAE1E0033BCFC233801F8CB4CED5C852B350CB7B* ____source_0;
};
// Native definition for COM marshalling of System.Threading.CancellationToken
struct CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED_marshaled_com
{
	CancellationTokenSource_tAAE1E0033BCFC233801F8CB4CED5C852B350CB7B* ____source_0;
};

// System.Char
struct Char_t521A6F19B456D956AF452D926C32709DC03D6B17 
{
	// System.Char System.Char::m_value
	Il2CppChar ___m_value_0;
};

// System.Configuration.ConfigurationElementCollection
struct ConfigurationElementCollection_t56E8398661A85A59616301BADF13026FB1492606  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Configuration.ConfigurationSection
struct ConfigurationSection_t0BC609F0151B160A4FAB8226679B62AF22539C3E  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.Configuration.ConnectionManagementElement
struct ConnectionManagementElement_t44321336DB0AC70D7B85FAB146D7F66036D51353  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.DateTime
struct DateTime_t66193957C73913903DDAD89FEDC46139BCA5802D 
{
	// System.UInt64 System.DateTime::_dateData
	uint64_t ____dateData_46;
};

// System.Enum
struct Enum_t2A1A94B24E3B776EEF4E5E485E290BB9D4D072E2  : public ValueType_t6D9B272BD21782F0A9A14F2E41F85A50E97A986F
{
};
// Native definition for P/Invoke marshalling of System.Enum
struct Enum_t2A1A94B24E3B776EEF4E5E485E290BB9D4D072E2_marshaled_pinvoke
{
};
// Native definition for COM marshalling of System.Enum
struct Enum_t2A1A94B24E3B776EEF4E5E485E290BB9D4D072E2_marshaled_com
{
};

// System.Net.Configuration.HttpWebRequestElement
struct HttpWebRequestElement_t8D2FEEC531D1FBE1B6E3D1077350A1EBDCB77DC3  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.IPEndPoint
struct IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB  : public EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564
{
	// System.Net.IPAddress System.Net.IPEndPoint::_address
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ____address_0;
	// System.Int32 System.Net.IPEndPoint::_port
	int32_t ____port_1;
};

// System.Int32
struct Int32_t680FF22E76F6EFAD4375103CBBFFA0421349384C 
{
	// System.Int32 System.Int32::m_value
	int32_t ___m_value_0;
};

// System.Int64
struct Int64_t092CFB123BE63C28ACDAF65C68F21A526050DBA3 
{
	// System.Int64 System.Int64::m_value
	int64_t ___m_value_0;
};

// System.IntPtr
struct IntPtr_t 
{
	// System.Void* System.IntPtr::m_value
	void* ___m_value_0;
};

// System.Net.Configuration.Ipv6Element
struct Ipv6Element_t480208D0E23E612DC1FD1CE19D9FB491F0F66932  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// Mono.Net.Security.MobileTlsProvider
struct MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017  : public MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E
{
};

// System.Net.Configuration.NetSectionGroup
struct NetSectionGroup_tA83DCAF89773087D0E921B92F67441132B71D52F  : public ConfigurationSectionGroup_tE7948C2D31B193F4BA8828947ED3094B952C7863
{
};

// System.Net.Configuration.PerformanceCountersElement
struct PerformanceCountersElement_tCB62C8CA7C218E35653DC5BB690A5FFD6D608B30  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.Configuration.ProxyElement
struct ProxyElement_t4D277820F9C77333BE8CC6498DE7E06C36868C65  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.Configuration.ServicePointManagerElement
struct ServicePointManagerElement_t98ED8374270537E77E57D014ECEE8B1A3A8EEFF2  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.Configuration.SocketElement
struct SocketElement_tDC83FCE3C280D8D590972F989292C30E2B7F6CC6  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.IO.Stream
struct Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE  : public MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE
{
	// System.IO.Stream/ReadWriteTask System.IO.Stream::_activeReadWriteTask
	ReadWriteTask_t0821BF49EE38596C7734E86E1A6A39D769BE2C05* ____activeReadWriteTask_2;
	// System.Threading.SemaphoreSlim System.IO.Stream::_asyncActiveSemaphore
	SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* ____asyncActiveSemaphore_3;
};

// System.Threading.Thread
struct Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F  : public CriticalFinalizerObject_t1DCAB623CAEA6529A96F5F3EDE3C7048A6E313C9
{
	// System.Threading.InternalThread System.Threading.Thread::internal_thread
	InternalThread_tF40B7BFCBD60C82BD8475A22FF5186CA10293687* ___internal_thread_6;
	// System.Object System.Threading.Thread::m_ThreadStartArg
	RuntimeObject* ___m_ThreadStartArg_7;
	// System.Object System.Threading.Thread::pending_exception
	RuntimeObject* ___pending_exception_8;
	// System.MulticastDelegate System.Threading.Thread::m_Delegate
	MulticastDelegate_t* ___m_Delegate_10;
	// System.Threading.ExecutionContext System.Threading.Thread::m_ExecutionContext
	ExecutionContext_t9D6EDFD92F0B2D391751963E2D77A8B03CB81710* ___m_ExecutionContext_11;
	// System.Boolean System.Threading.Thread::m_ExecutionContextBelongsToOuterScope
	bool ___m_ExecutionContextBelongsToOuterScope_12;
	// System.Security.Principal.IPrincipal System.Threading.Thread::principal
	RuntimeObject* ___principal_13;
	// System.Int32 System.Threading.Thread::principal_version
	int32_t ___principal_version_14;
};

// System.UInt32
struct UInt32_t1833D51FFA667B18A5AA4B8D34DE284F8495D29B 
{
	// System.UInt32 System.UInt32::m_value
	uint32_t ___m_value_0;
};

// System.Void
struct Void_t4861ACF8F4594C3437BB48B6E56783494B843915 
{
	union
	{
		struct
		{
		};
		uint8_t Void_t4861ACF8F4594C3437BB48B6E56783494B843915__padding[1];
	};
};

// System.Net.Configuration.WebProxyScriptElement
struct WebProxyScriptElement_t0C0E2338F7F42D5D386F5AE68FF7F870FA6E7167  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Net.Configuration.WebRequestModuleElement
struct WebRequestModuleElement_t9856A4B94D713299F842F672DFD60557C38349FD  : public ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E
{
};

// System.Security.Cryptography.X509Certificates.X509CertificateCollection
struct X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE  : public CollectionBase_t44F966CC555C87F2815D668FB4586526E1C2383F
{
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=12
struct __StaticArrayInitTypeSizeU3D12_t32C4FAAB522AC6551334F4E397A28A8CF95552DD 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D12_t32C4FAAB522AC6551334F4E397A28A8CF95552DD__padding[12];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=128
struct __StaticArrayInitTypeSizeU3D128_tCC4BD0E4F5977323EDA6FFF6CD1484F1848F5B0E 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D128_tCC4BD0E4F5977323EDA6FFF6CD1484F1848F5B0E__padding[128];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=14
struct __StaticArrayInitTypeSizeU3D14_t5A7362C844CE0A2E27C9434CD029FA82339CFF26 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D14_t5A7362C844CE0A2E27C9434CD029FA82339CFF26__padding[14];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=256
struct __StaticArrayInitTypeSizeU3D256_tD401FB34D66ACCFAD94A32F8001469D038F350C8 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D256_tD401FB34D66ACCFAD94A32F8001469D038F350C8__padding[256];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32
struct __StaticArrayInitTypeSizeU3D32_t543FA0727A92B3B7D8A0EB86D73AB8F35AB0CF48 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D32_t543FA0727A92B3B7D8A0EB86D73AB8F35AB0CF48__padding[32];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=44
struct __StaticArrayInitTypeSizeU3D44_tDBE82A359520A01FA0C117855F1C3185BA1AEB0F 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D44_tDBE82A359520A01FA0C117855F1C3185BA1AEB0F__padding[44];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=6
struct __StaticArrayInitTypeSizeU3D6_tA4881AF2477A5E66902A636B307315D4FA964D8D 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D6_tA4881AF2477A5E66902A636B307315D4FA964D8D__padding[6];
	};
};

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=9
struct __StaticArrayInitTypeSizeU3D9_tE2A1104C752B51004E61139D4615CC263948C4A7 
{
	union
	{
		struct
		{
			union
			{
			};
		};
		uint8_t __StaticArrayInitTypeSizeU3D9_tE2A1104C752B51004E61139D4615CC263948C4A7__padding[9];
	};
};

// System.Nullable`1<System.DateTime>
struct Nullable_1_tEADC262F7F8B8BC4CC0A003DBDD3CA7C1B63F9AC 
{
	// System.Boolean System.Nullable`1::hasValue
	bool ___hasValue_0;
	// T System.Nullable`1::value
	DateTime_t66193957C73913903DDAD89FEDC46139BCA5802D ___value_1;
};

// System.Net.Sockets.AddressFamily
struct AddressFamily_t01AA8C9FD15E4727B241F1F453D88444337C7524 
{
	// System.Int32 System.Net.Sockets.AddressFamily::value__
	int32_t ___value___2;
};

// System.Net.Security.AuthenticatedStream
struct AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39  : public Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE
{
	// System.IO.Stream System.Net.Security.AuthenticatedStream::_InnerStream
	Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ____InnerStream_4;
	// System.Boolean System.Net.Security.AuthenticatedStream::_LeaveStreamOpen
	bool ____LeaveStreamOpen_5;
};

// System.Net.Security.AuthenticationLevel
struct AuthenticationLevel_tD91F6CE700057352B4F45FC290E35B9E936DECAF 
{
	// System.Int32 System.Net.Security.AuthenticationLevel::value__
	int32_t ___value___2;
};

// System.Net.Configuration.BypassElementCollection
struct BypassElementCollection_t15465BC07F8DC8BABA6953182904AF762D638DBE  : public ConfigurationElementCollection_t56E8398661A85A59616301BADF13026FB1492606
{
};

// System.Net.Configuration.ConnectionManagementElementCollection
struct ConnectionManagementElementCollection_tEEB0BEE8289364CCA1D76DDA3F862429612DE0F1  : public ConfigurationElementCollection_t56E8398661A85A59616301BADF13026FB1492606
{
};

// System.Net.Configuration.ConnectionManagementSection
struct ConnectionManagementSection_t8F4D45BE147F24F54F2124CC800B4594031F41C2  : public ConfigurationSection_t0BC609F0151B160A4FAB8226679B62AF22539C3E
{
};

// System.Net.Configuration.DefaultProxySection
struct DefaultProxySection_tC0247A534955DC9D887B03AA4EFB1C1E01AD1E34  : public ConfigurationSection_t0BC609F0151B160A4FAB8226679B62AF22539C3E
{
};

// System.Delegate
struct Delegate_t  : public RuntimeObject
{
	// System.IntPtr System.Delegate::method_ptr
	intptr_t ___method_ptr_0;
	// System.IntPtr System.Delegate::invoke_impl
	intptr_t ___invoke_impl_1;
	// System.Object System.Delegate::m_target
	RuntimeObject* ___m_target_2;
	// System.IntPtr System.Delegate::method
	intptr_t ___method_3;
	// System.IntPtr System.Delegate::delegate_trampoline
	intptr_t ___delegate_trampoline_4;
	// System.IntPtr System.Delegate::extra_arg
	intptr_t ___extra_arg_5;
	// System.IntPtr System.Delegate::method_code
	intptr_t ___method_code_6;
	// System.IntPtr System.Delegate::interp_method
	intptr_t ___interp_method_7;
	// System.IntPtr System.Delegate::interp_invoke_impl
	intptr_t ___interp_invoke_impl_8;
	// System.Reflection.MethodInfo System.Delegate::method_info
	MethodInfo_t* ___method_info_9;
	// System.Reflection.MethodInfo System.Delegate::original_method_info
	MethodInfo_t* ___original_method_info_10;
	// System.DelegateData System.Delegate::data
	DelegateData_t9B286B493293CD2D23A5B2B5EF0E5B1324C2B77E* ___data_11;
	// System.Boolean System.Delegate::method_is_virtual
	bool ___method_is_virtual_12;
};
// Native definition for P/Invoke marshalling of System.Delegate
struct Delegate_t_marshaled_pinvoke
{
	intptr_t ___method_ptr_0;
	intptr_t ___invoke_impl_1;
	Il2CppIUnknown* ___m_target_2;
	intptr_t ___method_3;
	intptr_t ___delegate_trampoline_4;
	intptr_t ___extra_arg_5;
	intptr_t ___method_code_6;
	intptr_t ___interp_method_7;
	intptr_t ___interp_invoke_impl_8;
	MethodInfo_t* ___method_info_9;
	MethodInfo_t* ___original_method_info_10;
	DelegateData_t9B286B493293CD2D23A5B2B5EF0E5B1324C2B77E* ___data_11;
	int32_t ___method_is_virtual_12;
};
// Native definition for COM marshalling of System.Delegate
struct Delegate_t_marshaled_com
{
	intptr_t ___method_ptr_0;
	intptr_t ___invoke_impl_1;
	Il2CppIUnknown* ___m_target_2;
	intptr_t ___method_3;
	intptr_t ___delegate_trampoline_4;
	intptr_t ___extra_arg_5;
	intptr_t ___method_code_6;
	intptr_t ___interp_method_7;
	intptr_t ___interp_invoke_impl_8;
	MethodInfo_t* ___method_info_9;
	MethodInfo_t* ___original_method_info_10;
	DelegateData_t9B286B493293CD2D23A5B2B5EF0E5B1324C2B77E* ___data_11;
	int32_t ___method_is_virtual_12;
};

// System.Net.Security.EncryptionPolicy
struct EncryptionPolicy_t5BCDD1A5A1B42E3843DBD8B55A1BECBD2A523D99 
{
	// System.Int32 System.Net.Security.EncryptionPolicy::value__
	int32_t ___value___2;
};

// System.Exception
struct Exception_t  : public RuntimeObject
{
	// System.String System.Exception::_className
	String_t* ____className_1;
	// System.String System.Exception::_message
	String_t* ____message_2;
	// System.Collections.IDictionary System.Exception::_data
	RuntimeObject* ____data_3;
	// System.Exception System.Exception::_innerException
	Exception_t* ____innerException_4;
	// System.String System.Exception::_helpURL
	String_t* ____helpURL_5;
	// System.Object System.Exception::_stackTrace
	RuntimeObject* ____stackTrace_6;
	// System.String System.Exception::_stackTraceString
	String_t* ____stackTraceString_7;
	// System.String System.Exception::_remoteStackTraceString
	String_t* ____remoteStackTraceString_8;
	// System.Int32 System.Exception::_remoteStackIndex
	int32_t ____remoteStackIndex_9;
	// System.Object System.Exception::_dynamicMethods
	RuntimeObject* ____dynamicMethods_10;
	// System.Int32 System.Exception::_HResult
	int32_t ____HResult_11;
	// System.String System.Exception::_source
	String_t* ____source_12;
	// System.Runtime.Serialization.SafeSerializationManager System.Exception::_safeSerializationManager
	SafeSerializationManager_tCBB85B95DFD1634237140CD892E82D06ECB3F5E6* ____safeSerializationManager_13;
	// System.Diagnostics.StackTrace[] System.Exception::captured_traces
	StackTraceU5BU5D_t32FBCB20930EAF5BAE3F450FF75228E5450DA0DF* ___captured_traces_14;
	// System.IntPtr[] System.Exception::native_trace_ips
	IntPtrU5BU5D_tFD177F8C806A6921AD7150264CCC62FA00CAD832* ___native_trace_ips_15;
	// System.Int32 System.Exception::caught_in_unmanaged
	int32_t ___caught_in_unmanaged_16;
};
// Native definition for P/Invoke marshalling of System.Exception
struct Exception_t_marshaled_pinvoke
{
	char* ____className_1;
	char* ____message_2;
	RuntimeObject* ____data_3;
	Exception_t_marshaled_pinvoke* ____innerException_4;
	char* ____helpURL_5;
	Il2CppIUnknown* ____stackTrace_6;
	char* ____stackTraceString_7;
	char* ____remoteStackTraceString_8;
	int32_t ____remoteStackIndex_9;
	Il2CppIUnknown* ____dynamicMethods_10;
	int32_t ____HResult_11;
	char* ____source_12;
	SafeSerializationManager_tCBB85B95DFD1634237140CD892E82D06ECB3F5E6* ____safeSerializationManager_13;
	StackTraceU5BU5D_t32FBCB20930EAF5BAE3F450FF75228E5450DA0DF* ___captured_traces_14;
	Il2CppSafeArray/*NONE*/* ___native_trace_ips_15;
	int32_t ___caught_in_unmanaged_16;
};
// Native definition for COM marshalling of System.Exception
struct Exception_t_marshaled_com
{
	Il2CppChar* ____className_1;
	Il2CppChar* ____message_2;
	RuntimeObject* ____data_3;
	Exception_t_marshaled_com* ____innerException_4;
	Il2CppChar* ____helpURL_5;
	Il2CppIUnknown* ____stackTrace_6;
	Il2CppChar* ____stackTraceString_7;
	Il2CppChar* ____remoteStackTraceString_8;
	int32_t ____remoteStackIndex_9;
	Il2CppIUnknown* ____dynamicMethods_10;
	int32_t ____HResult_11;
	Il2CppChar* ____source_12;
	SafeSerializationManager_tCBB85B95DFD1634237140CD892E82D06ECB3F5E6* ____safeSerializationManager_13;
	StackTraceU5BU5D_t32FBCB20930EAF5BAE3F450FF75228E5450DA0DF* ___captured_traces_14;
	Il2CppSafeArray/*NONE*/* ___native_trace_ips_15;
	int32_t ___caught_in_unmanaged_16;
};

// System.Net.Sockets.IOControlCode
struct IOControlCode_t56C9B4178CCED52718BD5083105BB263B6BD04B9 
{
	// System.Int64 System.Net.Sockets.IOControlCode::value__
	int64_t ___value___2;
};

// System.IOOperation
struct IOOperation_tD09DB9BCCD9A9FE48E5606CE703934EEB95A67B2 
{
	// System.Int32 System.IOOperation::value__
	int32_t ___value___2;
};

// System.Net.Sockets.IPProtectionLevel
struct IPProtectionLevel_tC1667B58C88F0B5AA83711D9A7BD20A4F6483A72 
{
	// System.Int32 System.Net.Sockets.IPProtectionLevel::value__
	int32_t ___value___2;
};

// Mono.Security.Interface.MonoSslPolicyErrors
struct MonoSslPolicyErrors_t064CCA79859C39247FA94E7DA8DCBD327C650BB7 
{
	// System.Int32 Mono.Security.Interface.MonoSslPolicyErrors::value__
	int32_t ___value___2;
};

// System.Net.Sockets.NetworkStream
struct NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0  : public Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE
{
	// System.Net.Sockets.Socket System.Net.Sockets.NetworkStream::_streamSocket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ____streamSocket_4;
	// System.Boolean System.Net.Sockets.NetworkStream::_ownsSocket
	bool ____ownsSocket_5;
	// System.Boolean System.Net.Sockets.NetworkStream::_readable
	bool ____readable_6;
	// System.Boolean System.Net.Sockets.NetworkStream::_writeable
	bool ____writeable_7;
	// System.Int32 System.Net.Sockets.NetworkStream::_closeTimeout
	int32_t ____closeTimeout_8;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.NetworkStream::_cleanedUp
	bool ____cleanedUp_9;
	// System.Int32 System.Net.Sockets.NetworkStream::_currentReadTimeout
	int32_t ____currentReadTimeout_10;
	// System.Int32 System.Net.Sockets.NetworkStream::_currentWriteTimeout
	int32_t ____currentWriteTimeout_11;
};

// System.Net.Sockets.ProtocolType
struct ProtocolType_t104D087F8C40460E0FE8D38659949AEA910CD20A 
{
	// System.Int32 System.Net.Sockets.ProtocolType::value__
	int32_t ___value___2;
};

// System.RuntimeTypeHandle
struct RuntimeTypeHandle_t332A452B8B6179E4469B69525D0FE82A88030F7B 
{
	// System.IntPtr System.RuntimeTypeHandle::value
	intptr_t ___value_0;
};

// System.Runtime.InteropServices.SafeHandle
struct SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7  : public CriticalFinalizerObject_t1DCAB623CAEA6529A96F5F3EDE3C7048A6E313C9
{
	// System.IntPtr System.Runtime.InteropServices.SafeHandle::handle
	intptr_t ___handle_0;
	// System.Int32 System.Runtime.InteropServices.SafeHandle::_state
	int32_t ____state_1;
	// System.Boolean System.Runtime.InteropServices.SafeHandle::_ownsHandle
	bool ____ownsHandle_2;
	// System.Boolean System.Runtime.InteropServices.SafeHandle::_fullyInitialized
	bool ____fullyInitialized_3;
};

// System.IO.SeekOrigin
struct SeekOrigin_t7EB9AD0EDF26368A40F48FA2098F02160B1E8000 
{
	// System.Int32 System.IO.SeekOrigin::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SelectMode
struct SelectMode_t79471F8C16DDDCE101CD4E6F76FEC01110E5CA1E 
{
	// System.Int32 System.Net.Sockets.SelectMode::value__
	int32_t ___value___2;
};

// System.Net.Configuration.SettingsSection
struct SettingsSection_tC931BA69B22D5E0ED07698F02778482987319A12  : public ConfigurationSection_t0BC609F0151B160A4FAB8226679B62AF22539C3E
{
};

// System.Net.Sockets.SocketAsyncOperation
struct SocketAsyncOperation_t1A91B184611796EEBB53B09D34C3C90F85861B40 
{
	// System.Int32 System.Net.Sockets.SocketAsyncOperation::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketError
struct SocketError_t4AD3BECF393E3FD8C5238C4AE47B768B3ABC07B8 
{
	// System.Int32 System.Net.Sockets.SocketError::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketFlags
struct SocketFlags_t48F675629008EF0627E5937E1B078C1456A6E707 
{
	// System.Int32 System.Net.Sockets.SocketFlags::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketOperation
struct SocketOperation_t079BACC8E39933EA4885F16B31A82D08C4DAEA1E 
{
	// System.Int32 System.Net.Sockets.SocketOperation::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketOptionLevel
struct SocketOptionLevel_t2B232DDE7F90010547798E56A15F7303B6A663A7 
{
	// System.Int32 System.Net.Sockets.SocketOptionLevel::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketOptionName
struct SocketOptionName_t064FACD89D727E52DDE3E939D14912D3057FA45B 
{
	// System.Int32 System.Net.Sockets.SocketOptionName::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketShutdown
struct SocketShutdown_t6EA59AFAC48EDA342EB102BE4950BFAC3FF2E9E1 
{
	// System.Int32 System.Net.Sockets.SocketShutdown::value__
	int32_t ___value___2;
};

// System.Net.Sockets.SocketType
struct SocketType_tEFAA48791CC7B43043CE5B1DE7A54F15DCFE3C52 
{
	// System.Int32 System.Net.Sockets.SocketType::value__
	int32_t ___value___2;
};

// System.Net.Security.SslPolicyErrors
struct SslPolicyErrors_t92DF45EC5BA5BEFF38EF53C2C8488B27385EBB08 
{
	// System.Int32 System.Net.Security.SslPolicyErrors::value__
	int32_t ___value___2;
};

// System.Security.Authentication.SslProtocols
struct SslProtocols_t21FADB874FCAEC5039AE593AA3544639C938C77E 
{
	// System.Int32 System.Security.Authentication.SslProtocols::value__
	int32_t ___value___2;
};

// System.Runtime.Serialization.StreamingContextStates
struct StreamingContextStates_t5EE358E619B251608A9327618C7BFE8638FC33C1 
{
	// System.Int32 System.Runtime.Serialization.StreamingContextStates::value__
	int32_t ___value___2;
};

// System.Threading.Tasks.TaskContinuationOptions
struct TaskContinuationOptions_tF334758E3027B6DCFA9A814B17CE878029537814 
{
	// System.Int32 System.Threading.Tasks.TaskContinuationOptions::value__
	int32_t ___value___2;
};

// System.Threading.Tasks.TaskCreationOptions
struct TaskCreationOptions_tB15CB42D61B8958640A7C702A79097B56D5C7ABA 
{
	// System.Int32 System.Threading.Tasks.TaskCreationOptions::value__
	int32_t ___value___2;
};

// Mono.Security.Interface.TlsProtocols
struct TlsProtocols_tCC009DAEEFCA5769448145AD24A1193CD5E8541B 
{
	// System.Int32 Mono.Security.Interface.TlsProtocols::value__
	int32_t ___value___2;
};

// System.Threading.WaitHandle
struct WaitHandle_t08F8DB54593B241FE32E0DD0BD3D82785D3AE3D8  : public MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE
{
	// System.IntPtr System.Threading.WaitHandle::waitHandle
	intptr_t ___waitHandle_3;
	// Microsoft.Win32.SafeHandles.SafeWaitHandle modreq(System.Runtime.CompilerServices.IsVolatile) System.Threading.WaitHandle::safeWaitHandle
	SafeWaitHandle_t58F5662CD56F6462A687198A64987F8980804449* ___safeWaitHandle_4;
	// System.Boolean System.Threading.WaitHandle::hasThreadAffinity
	bool ___hasThreadAffinity_5;
};
// Native definition for P/Invoke marshalling of System.Threading.WaitHandle
struct WaitHandle_t08F8DB54593B241FE32E0DD0BD3D82785D3AE3D8_marshaled_pinvoke : public MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE_marshaled_pinvoke
{
	intptr_t ___waitHandle_3;
	void* ___safeWaitHandle_4;
	int32_t ___hasThreadAffinity_5;
};
// Native definition for COM marshalling of System.Threading.WaitHandle
struct WaitHandle_t08F8DB54593B241FE32E0DD0BD3D82785D3AE3D8_marshaled_com : public MarshalByRefObject_t8C2F4C5854177FD60439EB1FCCFC1B3CFAFE8DCE_marshaled_com
{
	intptr_t ___waitHandle_3;
	void* ___safeWaitHandle_4;
	int32_t ___hasThreadAffinity_5;
};

// System.Net.Configuration.WebRequestModuleElementCollection
struct WebRequestModuleElementCollection_t5979A19A48B822831C1D351EB2AA25058C7F7463  : public ConfigurationElementCollection_t56E8398661A85A59616301BADF13026FB1492606
{
};

// System.Net.Configuration.WebRequestModulesSection
struct WebRequestModulesSection_t4099F9890D7851314C5528C577E4C51B36C97D00  : public ConfigurationSection_t0BC609F0151B160A4FAB8226679B62AF22539C3E
{
};

// System.Security.Cryptography.X509Certificates.X509Certificate
struct X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4  : public RuntimeObject
{
	// System.Security.Cryptography.X509Certificates.X509CertificateImpl System.Security.Cryptography.X509Certificates.X509Certificate::impl
	X509CertificateImpl_tF590E81705CE1FE152C590E5A875D4FE3BE348EF* ___impl_0;
	// System.Byte[] modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazyCertHash
	ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___lazyCertHash_1;
	// System.Byte[] modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazySerialNumber
	ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___lazySerialNumber_2;
	// System.String modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazyIssuer
	String_t* ___lazyIssuer_3;
	// System.String modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazySubject
	String_t* ___lazySubject_4;
	// System.String modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazyKeyAlgorithm
	String_t* ___lazyKeyAlgorithm_5;
	// System.Byte[] modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazyKeyAlgorithmParameters
	ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___lazyKeyAlgorithmParameters_6;
	// System.Byte[] modreq(System.Runtime.CompilerServices.IsVolatile) System.Security.Cryptography.X509Certificates.X509Certificate::lazyPublicKey
	ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___lazyPublicKey_7;
	// System.DateTime System.Security.Cryptography.X509Certificates.X509Certificate::lazyNotBefore
	DateTime_t66193957C73913903DDAD89FEDC46139BCA5802D ___lazyNotBefore_8;
	// System.DateTime System.Security.Cryptography.X509Certificates.X509Certificate::lazyNotAfter
	DateTime_t66193957C73913903DDAD89FEDC46139BCA5802D ___lazyNotAfter_9;
};

// System.Security.Cryptography.X509Certificates.X509RevocationMode
struct X509RevocationMode_t952728D003111036C0DF94B0F66FF02B7DB04E62 
{
	// System.Int32 System.Security.Cryptography.X509Certificates.X509RevocationMode::value__
	int32_t ___value___2;
};

// Mono.Net.Security.MobileAuthenticatedStream/Operation
struct Operation_tC35F3423442F066D3B3CB2E5FCC61CEA2D618017 
{
	// System.Int32 Mono.Net.Security.MobileAuthenticatedStream/Operation::value__
	int32_t ___value___2;
};

// System.Net.Sockets.Socket/<>c__DisplayClass355_0
struct U3CU3Ec__DisplayClass355_0_tE1E6F6152220887E65219B2C9CA1073F6F5D8819  : public RuntimeObject
{
	// System.Net.Sockets.Socket System.Net.Sockets.Socket/<>c__DisplayClass355_0::<>4__this
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___U3CU3E4__this_0;
	// System.IOSelectorJob System.Net.Sockets.Socket/<>c__DisplayClass355_0::job
	IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31* ___job_1;
	// System.IntPtr System.Net.Sockets.Socket/<>c__DisplayClass355_0::handle
	intptr_t ___handle_2;
};

// System.Nullable`1<Mono.Security.Interface.TlsProtocols>
struct Nullable_1_t9A98093485034F2B86BC66B725022122E0E5B2A4 
{
	// System.Boolean System.Nullable`1::hasValue
	bool ___hasValue_0;
	// T System.Nullable`1::value
	int32_t ___value_1;
};

// System.AggregateException
struct AggregateException_t51B6205846DFB356B94452702201DD239D44422F  : public Exception_t
{
	// System.Collections.ObjectModel.ReadOnlyCollection`1<System.Exception> System.AggregateException::m_innerExceptions
	ReadOnlyCollection_1_t0A3EEA206F04C2F9E8303E24D863C2E7FD258886* ___m_innerExceptions_18;
};

// System.Threading.EventWaitHandle
struct EventWaitHandle_t18F2EB0161747B0646A9A406015A61A214A1EB7E  : public WaitHandle_t08F8DB54593B241FE32E0DD0BD3D82785D3AE3D8
{
};

// System.IOSelectorJob
struct IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31  : public RuntimeObject
{
	// System.IOOperation System.IOSelectorJob::operation
	int32_t ___operation_0;
	// System.IOAsyncCallback System.IOSelectorJob::callback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___callback_1;
	// System.IOAsyncResult System.IOSelectorJob::state
	IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* ___state_2;
};
// Native definition for P/Invoke marshalling of System.IOSelectorJob
struct IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31_marshaled_pinvoke
{
	int32_t ___operation_0;
	Il2CppMethodPointer ___callback_1;
	IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_pinvoke* ___state_2;
};
// Native definition for COM marshalling of System.IOSelectorJob
struct IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31_marshaled_com
{
	int32_t ___operation_0;
	Il2CppMethodPointer ___callback_1;
	IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_com* ___state_2;
};

// Mono.Net.Security.MobileAuthenticatedStream
struct MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E  : public AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39
{
	// Mono.Net.Security.MobileTlsContext Mono.Net.Security.MobileAuthenticatedStream::xobileTlsContext
	MobileTlsContext_t456DDC89866EE34EF32EEF959AD92C9F17684476* ___xobileTlsContext_6;
	// System.Runtime.ExceptionServices.ExceptionDispatchInfo Mono.Net.Security.MobileAuthenticatedStream::lastException
	ExceptionDispatchInfo_tD7AF19E75FEC22F4A8329FD1E9EDF96615CB2757* ___lastException_7;
	// Mono.Net.Security.AsyncProtocolRequest Mono.Net.Security.MobileAuthenticatedStream::asyncHandshakeRequest
	AsyncProtocolRequest_tF31CF30F8C57A3D5DC10A9FB0A5568FA254DFE86* ___asyncHandshakeRequest_8;
	// Mono.Net.Security.AsyncProtocolRequest Mono.Net.Security.MobileAuthenticatedStream::asyncReadRequest
	AsyncProtocolRequest_tF31CF30F8C57A3D5DC10A9FB0A5568FA254DFE86* ___asyncReadRequest_9;
	// Mono.Net.Security.AsyncProtocolRequest Mono.Net.Security.MobileAuthenticatedStream::asyncWriteRequest
	AsyncProtocolRequest_tF31CF30F8C57A3D5DC10A9FB0A5568FA254DFE86* ___asyncWriteRequest_10;
	// Mono.Net.Security.BufferOffsetSize2 Mono.Net.Security.MobileAuthenticatedStream::readBuffer
	BufferOffsetSize2_t2C3F7F42F64D84F357DC24AFCD44AC4679B20629* ___readBuffer_11;
	// Mono.Net.Security.BufferOffsetSize2 Mono.Net.Security.MobileAuthenticatedStream::writeBuffer
	BufferOffsetSize2_t2C3F7F42F64D84F357DC24AFCD44AC4679B20629* ___writeBuffer_12;
	// System.Object Mono.Net.Security.MobileAuthenticatedStream::ioLock
	RuntimeObject* ___ioLock_13;
	// System.Int32 Mono.Net.Security.MobileAuthenticatedStream::closeRequested
	int32_t ___closeRequested_14;
	// System.Boolean Mono.Net.Security.MobileAuthenticatedStream::shutdown
	bool ___shutdown_15;
	// Mono.Net.Security.MobileAuthenticatedStream/Operation Mono.Net.Security.MobileAuthenticatedStream::operation
	int32_t ___operation_16;
	// System.Net.Security.SslStream Mono.Net.Security.MobileAuthenticatedStream::<SslStream>k__BackingField
	SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* ___U3CSslStreamU3Ek__BackingField_18;
	// Mono.Security.Interface.MonoTlsSettings Mono.Net.Security.MobileAuthenticatedStream::<Settings>k__BackingField
	MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* ___U3CSettingsU3Ek__BackingField_19;
	// Mono.Net.Security.MobileTlsProvider Mono.Net.Security.MobileAuthenticatedStream::<Provider>k__BackingField
	MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* ___U3CProviderU3Ek__BackingField_20;
	// System.String Mono.Net.Security.MobileAuthenticatedStream::<TargetHost>k__BackingField
	String_t* ___U3CTargetHostU3Ek__BackingField_21;
	// System.Int32 Mono.Net.Security.MobileAuthenticatedStream::ID
	int32_t ___ID_23;
};

// System.MulticastDelegate
struct MulticastDelegate_t  : public Delegate_t
{
	// System.Delegate[] System.MulticastDelegate::delegates
	DelegateU5BU5D_tC5AB7E8F745616680F337909D3A8E6C722CDF771* ___delegates_13;
};
// Native definition for P/Invoke marshalling of System.MulticastDelegate
struct MulticastDelegate_t_marshaled_pinvoke : public Delegate_t_marshaled_pinvoke
{
	Delegate_t_marshaled_pinvoke** ___delegates_13;
};
// Native definition for COM marshalling of System.MulticastDelegate
struct MulticastDelegate_t_marshaled_com : public Delegate_t_marshaled_com
{
	Delegate_t_marshaled_com** ___delegates_13;
};

// Microsoft.Win32.SafeHandles.SafeHandleMinusOneIsInvalid
struct SafeHandleMinusOneIsInvalid_t85F57717BC66C06B714910FA13BF2E49AA5BD1A6  : public SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7
{
};

// System.Net.Sockets.Socket
struct Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E  : public RuntimeObject
{
	// System.Net.Sockets.Socket/CachedEventArgs System.Net.Sockets.Socket::_cachedTaskEventArgs
	CachedEventArgs_tF0692E89962FD1A045B17BC985F838C11FB6822C* ____cachedTaskEventArgs_6;
	// System.Boolean System.Net.Sockets.Socket::is_closed
	bool ___is_closed_13;
	// System.Boolean System.Net.Sockets.Socket::is_listening
	bool ___is_listening_14;
	// System.Int32 System.Net.Sockets.Socket::linger_timeout
	int32_t ___linger_timeout_15;
	// System.Net.Sockets.AddressFamily System.Net.Sockets.Socket::addressFamily
	int32_t ___addressFamily_16;
	// System.Net.Sockets.SocketType System.Net.Sockets.Socket::socketType
	int32_t ___socketType_17;
	// System.Net.Sockets.ProtocolType System.Net.Sockets.Socket::protocolType
	int32_t ___protocolType_18;
	// System.Net.Sockets.SafeSocketHandle System.Net.Sockets.Socket::m_Handle
	SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE* ___m_Handle_19;
	// System.Net.EndPoint System.Net.Sockets.Socket::seed_endpoint
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___seed_endpoint_20;
	// System.Threading.SemaphoreSlim System.Net.Sockets.Socket::ReadSem
	SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* ___ReadSem_21;
	// System.Threading.SemaphoreSlim System.Net.Sockets.Socket::WriteSem
	SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* ___WriteSem_22;
	// System.Boolean System.Net.Sockets.Socket::is_blocking
	bool ___is_blocking_23;
	// System.Boolean System.Net.Sockets.Socket::is_bound
	bool ___is_bound_24;
	// System.Boolean System.Net.Sockets.Socket::is_connected
	bool ___is_connected_25;
	// System.Int32 System.Net.Sockets.Socket::m_IntCleanedUp
	int32_t ___m_IntCleanedUp_26;
	// System.Boolean System.Net.Sockets.Socket::connect_in_progress
	bool ___connect_in_progress_27;
};

// System.Net.Sockets.SocketAsyncEventArgs
struct SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC  : public EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377
{
	// System.Boolean System.Net.Sockets.SocketAsyncEventArgs::disposed
	bool ___disposed_1;
	// System.Int32 modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.SocketAsyncEventArgs::in_progress
	int32_t ___in_progress_2;
	// System.Net.EndPoint System.Net.Sockets.SocketAsyncEventArgs::remote_ep
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___remote_ep_3;
	// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncEventArgs::current_socket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___current_socket_4;
	// System.Net.Sockets.SocketAsyncResult System.Net.Sockets.SocketAsyncEventArgs::socket_async_result
	SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* ___socket_async_result_5;
	// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncEventArgs::<AcceptSocket>k__BackingField
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___U3CAcceptSocketU3Ek__BackingField_6;
	// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::<BytesTransferred>k__BackingField
	int32_t ___U3CBytesTransferredU3Ek__BackingField_7;
	// System.Net.Sockets.SocketAsyncOperation System.Net.Sockets.SocketAsyncEventArgs::<LastOperation>k__BackingField
	int32_t ___U3CLastOperationU3Ek__BackingField_8;
	// System.Net.Sockets.SocketError System.Net.Sockets.SocketAsyncEventArgs::<SocketError>k__BackingField
	int32_t ___U3CSocketErrorU3Ek__BackingField_9;
	// System.Net.Sockets.SocketFlags System.Net.Sockets.SocketAsyncEventArgs::<SocketFlags>k__BackingField
	int32_t ___U3CSocketFlagsU3Ek__BackingField_10;
	// System.Object System.Net.Sockets.SocketAsyncEventArgs::<UserToken>k__BackingField
	RuntimeObject* ___U3CUserTokenU3Ek__BackingField_11;
	// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs> System.Net.Sockets.SocketAsyncEventArgs::Completed
	EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* ___Completed_12;
	// System.Memory`1<System.Byte> System.Net.Sockets.SocketAsyncEventArgs::_buffer
	Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 ____buffer_13;
	// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::_offset
	int32_t ____offset_14;
	// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::_count
	int32_t ____count_15;
	// System.Boolean System.Net.Sockets.SocketAsyncEventArgs::_bufferIsExplicitArray
	bool ____bufferIsExplicitArray_16;
	// System.Collections.Generic.IList`1<System.ArraySegment`1<System.Byte>> System.Net.Sockets.SocketAsyncEventArgs::_bufferList
	RuntimeObject* ____bufferList_17;
};

// System.Net.Sockets.SocketAsyncResult
struct SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340  : public IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374
{
	// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncResult::socket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___socket_5;
	// System.Net.Sockets.SocketOperation System.Net.Sockets.SocketAsyncResult::operation
	int32_t ___operation_6;
	// System.Exception System.Net.Sockets.SocketAsyncResult::DelayedException
	Exception_t* ___DelayedException_7;
	// System.Net.EndPoint System.Net.Sockets.SocketAsyncResult::EndPoint
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___EndPoint_8;
	// System.Memory`1<System.Byte> System.Net.Sockets.SocketAsyncResult::Buffer
	Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 ___Buffer_9;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::Offset
	int32_t ___Offset_10;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::Size
	int32_t ___Size_11;
	// System.Net.Sockets.SocketFlags System.Net.Sockets.SocketAsyncResult::SockFlags
	int32_t ___SockFlags_12;
	// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncResult::AcceptSocket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptSocket_13;
	// System.Net.IPAddress[] System.Net.Sockets.SocketAsyncResult::Addresses
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* ___Addresses_14;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::Port
	int32_t ___Port_15;
	// System.Collections.Generic.IList`1<System.ArraySegment`1<System.Byte>> System.Net.Sockets.SocketAsyncResult::Buffers
	RuntimeObject* ___Buffers_16;
	// System.Boolean System.Net.Sockets.SocketAsyncResult::ReuseSocket
	bool ___ReuseSocket_17;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::CurrentAddress
	int32_t ___CurrentAddress_18;
	// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncResult::AcceptedSocket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptedSocket_19;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::Total
	int32_t ___Total_20;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::error
	int32_t ___error_21;
	// System.Int32 System.Net.Sockets.SocketAsyncResult::EndCalled
	int32_t ___EndCalled_22;
};
// Native definition for P/Invoke marshalling of System.Net.Sockets.SocketAsyncResult
struct SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_pinvoke : public IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_pinvoke
{
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___socket_5;
	int32_t ___operation_6;
	Exception_t_marshaled_pinvoke* ___DelayedException_7;
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___EndPoint_8;
	Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 ___Buffer_9;
	int32_t ___Offset_10;
	int32_t ___Size_11;
	int32_t ___SockFlags_12;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptSocket_13;
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* ___Addresses_14;
	int32_t ___Port_15;
	RuntimeObject* ___Buffers_16;
	int32_t ___ReuseSocket_17;
	int32_t ___CurrentAddress_18;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptedSocket_19;
	int32_t ___Total_20;
	int32_t ___error_21;
	int32_t ___EndCalled_22;
};
// Native definition for COM marshalling of System.Net.Sockets.SocketAsyncResult
struct SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_com : public IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374_marshaled_com
{
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___socket_5;
	int32_t ___operation_6;
	Exception_t_marshaled_com* ___DelayedException_7;
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___EndPoint_8;
	Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 ___Buffer_9;
	int32_t ___Offset_10;
	int32_t ___Size_11;
	int32_t ___SockFlags_12;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptSocket_13;
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* ___Addresses_14;
	int32_t ___Port_15;
	RuntimeObject* ___Buffers_16;
	int32_t ___ReuseSocket_17;
	int32_t ___CurrentAddress_18;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___AcceptedSocket_19;
	int32_t ___Total_20;
	int32_t ___error_21;
	int32_t ___EndCalled_22;
};

// System.Net.Security.SslClientAuthenticationOptions
struct SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9  : public RuntimeObject
{
	// System.Net.Security.EncryptionPolicy System.Net.Security.SslClientAuthenticationOptions::_encryptionPolicy
	int32_t ____encryptionPolicy_0;
	// System.Security.Cryptography.X509Certificates.X509RevocationMode System.Net.Security.SslClientAuthenticationOptions::_checkCertificateRevocation
	int32_t ____checkCertificateRevocation_1;
	// System.Security.Authentication.SslProtocols System.Net.Security.SslClientAuthenticationOptions::_enabledSslProtocols
	int32_t ____enabledSslProtocols_2;
	// System.Boolean System.Net.Security.SslClientAuthenticationOptions::_allowRenegotiation
	bool ____allowRenegotiation_3;
	// System.String System.Net.Security.SslClientAuthenticationOptions::<TargetHost>k__BackingField
	String_t* ___U3CTargetHostU3Ek__BackingField_4;
	// System.Security.Cryptography.X509Certificates.X509CertificateCollection System.Net.Security.SslClientAuthenticationOptions::<ClientCertificates>k__BackingField
	X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___U3CClientCertificatesU3Ek__BackingField_5;
};

// System.Net.Security.SslStream
struct SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27  : public AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39
{
	// Mono.Net.Security.MobileTlsProvider System.Net.Security.SslStream::provider
	MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* ___provider_6;
	// Mono.Security.Interface.MonoTlsSettings System.Net.Security.SslStream::settings
	MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* ___settings_7;
	// System.Net.Security.RemoteCertificateValidationCallback System.Net.Security.SslStream::validationCallback
	RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___validationCallback_8;
	// System.Net.Security.LocalCertificateSelectionCallback System.Net.Security.SslStream::selectionCallback
	LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___selectionCallback_9;
	// Mono.Net.Security.MobileAuthenticatedStream System.Net.Security.SslStream::impl
	MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* ___impl_10;
	// System.Boolean System.Net.Security.SslStream::explicitSettings
	bool ___explicitSettings_11;
};

// System.Runtime.Serialization.StreamingContext
struct StreamingContext_t56760522A751890146EE45F82F866B55B7E33677 
{
	// System.Object System.Runtime.Serialization.StreamingContext::m_additionalContext
	RuntimeObject* ___m_additionalContext_0;
	// System.Runtime.Serialization.StreamingContextStates System.Runtime.Serialization.StreamingContext::m_state
	int32_t ___m_state_1;
};
// Native definition for P/Invoke marshalling of System.Runtime.Serialization.StreamingContext
struct StreamingContext_t56760522A751890146EE45F82F866B55B7E33677_marshaled_pinvoke
{
	Il2CppIUnknown* ___m_additionalContext_0;
	int32_t ___m_state_1;
};
// Native definition for COM marshalling of System.Runtime.Serialization.StreamingContext
struct StreamingContext_t56760522A751890146EE45F82F866B55B7E33677_marshaled_com
{
	Il2CppIUnknown* ___m_additionalContext_0;
	int32_t ___m_state_1;
};

// System.SystemException
struct SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295  : public Exception_t
{
};

// System.Threading.Tasks.TaskFactory
struct TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0  : public RuntimeObject
{
	// System.Threading.CancellationToken System.Threading.Tasks.TaskFactory::m_defaultCancellationToken
	CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED ___m_defaultCancellationToken_0;
	// System.Threading.Tasks.TaskScheduler System.Threading.Tasks.TaskFactory::m_defaultScheduler
	TaskScheduler_t3F0550EBEF7C41F74EC8C08FF4BED0D8CE66006E* ___m_defaultScheduler_1;
	// System.Threading.Tasks.TaskCreationOptions System.Threading.Tasks.TaskFactory::m_defaultCreationOptions
	int32_t ___m_defaultCreationOptions_2;
	// System.Threading.Tasks.TaskContinuationOptions System.Threading.Tasks.TaskFactory::m_defaultContinuationOptions
	int32_t ___m_defaultContinuationOptions_3;
};

// System.Net.Sockets.TcpClient
struct TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58  : public RuntimeObject
{
	// System.Net.Sockets.Socket System.Net.Sockets.TcpClient::m_ClientSocket
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___m_ClientSocket_0;
	// System.Boolean System.Net.Sockets.TcpClient::m_Active
	bool ___m_Active_1;
	// System.Net.Sockets.NetworkStream System.Net.Sockets.TcpClient::m_DataStream
	NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* ___m_DataStream_2;
	// System.Net.Sockets.AddressFamily System.Net.Sockets.TcpClient::m_Family
	int32_t ___m_Family_3;
	// System.Boolean System.Net.Sockets.TcpClient::m_CleanedUp
	bool ___m_CleanedUp_4;
};

// System.Type
struct Type_t  : public MemberInfo_t
{
	// System.RuntimeTypeHandle System.Type::_impl
	RuntimeTypeHandle_t332A452B8B6179E4469B69525D0FE82A88030F7B ____impl_8;
};

// System.Action`1<System.IAsyncResult>
struct Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780  : public MulticastDelegate_t
{
};

// System.EventHandler`1<System.Object>
struct EventHandler_1_tD8C4A5BE1F7C91B1A7E99AE87AFD2F5432C38746  : public MulticastDelegate_t
{
};

// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs>
struct EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED  : public MulticastDelegate_t
{
};

// System.Func`5<System.String,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>
struct Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818  : public MulticastDelegate_t
{
};

// System.ArgumentException
struct ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
	// System.String System.ArgumentException::_paramName
	String_t* ____paramName_18;
};

// System.AsyncCallback
struct AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C  : public MulticastDelegate_t
{
};

// System.Runtime.InteropServices.ExternalException
struct ExternalException_t419875A3CD3C551692EDBBC99E4927E69F2E1F4C  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.InvalidOperationException
struct InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.Net.Security.LocalCertSelectionCallback
struct LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2  : public MulticastDelegate_t
{
};

// System.Net.Security.LocalCertificateSelectionCallback
struct LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859  : public MulticastDelegate_t
{
};

// System.Threading.ManualResetEvent
struct ManualResetEvent_t63959486AA41A113A4353D0BF4A68E77EBA0A158  : public EventWaitHandle_t18F2EB0161747B0646A9A406015A61A214A1EB7E
{
};

// Mono.Security.Interface.MonoLocalCertificateSelectionCallback
struct MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3  : public MulticastDelegate_t
{
};

// Mono.Security.Interface.MonoRemoteCertificateValidationCallback
struct MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0  : public MulticastDelegate_t
{
};

// Mono.Security.Interface.MonoTlsSettings
struct MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0  : public RuntimeObject
{
	// Mono.Security.Interface.MonoRemoteCertificateValidationCallback Mono.Security.Interface.MonoTlsSettings::<RemoteCertificateValidationCallback>k__BackingField
	MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* ___U3CRemoteCertificateValidationCallbackU3Ek__BackingField_0;
	// Mono.Security.Interface.MonoLocalCertificateSelectionCallback Mono.Security.Interface.MonoTlsSettings::<ClientCertificateSelectionCallback>k__BackingField
	MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* ___U3CClientCertificateSelectionCallbackU3Ek__BackingField_1;
	// System.Nullable`1<System.DateTime> Mono.Security.Interface.MonoTlsSettings::<CertificateValidationTime>k__BackingField
	Nullable_1_tEADC262F7F8B8BC4CC0A003DBDD3CA7C1B63F9AC ___U3CCertificateValidationTimeU3Ek__BackingField_2;
	// System.Security.Cryptography.X509Certificates.X509CertificateCollection Mono.Security.Interface.MonoTlsSettings::<TrustAnchors>k__BackingField
	X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___U3CTrustAnchorsU3Ek__BackingField_3;
	// System.Object Mono.Security.Interface.MonoTlsSettings::<UserSettings>k__BackingField
	RuntimeObject* ___U3CUserSettingsU3Ek__BackingField_4;
	// System.String[] Mono.Security.Interface.MonoTlsSettings::<CertificateSearchPaths>k__BackingField
	StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___U3CCertificateSearchPathsU3Ek__BackingField_5;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::<SendCloseNotify>k__BackingField
	bool ___U3CSendCloseNotifyU3Ek__BackingField_6;
	// System.String[] Mono.Security.Interface.MonoTlsSettings::<ClientCertificateIssuers>k__BackingField
	StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___U3CClientCertificateIssuersU3Ek__BackingField_7;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::<DisallowUnauthenticatedCertificateRequest>k__BackingField
	bool ___U3CDisallowUnauthenticatedCertificateRequestU3Ek__BackingField_8;
	// System.Nullable`1<Mono.Security.Interface.TlsProtocols> Mono.Security.Interface.MonoTlsSettings::<EnabledProtocols>k__BackingField
	Nullable_1_t9A98093485034F2B86BC66B725022122E0E5B2A4 ___U3CEnabledProtocolsU3Ek__BackingField_9;
	// Mono.Security.Interface.CipherSuiteCode[] Mono.Security.Interface.MonoTlsSettings::<EnabledCiphers>k__BackingField
	CipherSuiteCodeU5BU5D_t61EC0E6F53394985FFC36DEB587C70F4EE26D435* ___U3CEnabledCiphersU3Ek__BackingField_10;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::cloned
	bool ___cloned_11;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::checkCertName
	bool ___checkCertName_12;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::checkCertRevocationStatus
	bool ___checkCertRevocationStatus_13;
	// System.Nullable`1<System.Boolean> Mono.Security.Interface.MonoTlsSettings::useServicePointManagerCallback
	Nullable_1_t78F453FADB4A9F50F267A4E349019C34410D1A01 ___useServicePointManagerCallback_14;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::skipSystemValidators
	bool ___skipSystemValidators_15;
	// System.Boolean Mono.Security.Interface.MonoTlsSettings::callbackNeedsChain
	bool ___callbackNeedsChain_16;
	// Mono.Security.Interface.ICertificateValidator Mono.Security.Interface.MonoTlsSettings::certificateValidator
	RuntimeObject* ___certificateValidator_17;
};

// System.NotSupportedException
struct NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.OperationCanceledException
struct OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
	// System.Threading.CancellationToken System.OperationCanceledException::_cancellationToken
	CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED ____cancellationToken_18;
};

// System.OutOfMemoryException
struct OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.Net.Security.RemoteCertificateValidationCallback
struct RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955  : public MulticastDelegate_t
{
};

// System.Net.Sockets.SafeSocketHandle
struct SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE  : public SafeHandleMinusOneIsInvalid_t85F57717BC66C06B714910FA13BF2E49AA5BD1A6
{
	// System.Collections.Generic.List`1<System.Threading.Thread> System.Net.Sockets.SafeSocketHandle::blocking_threads
	List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* ___blocking_threads_6;
	// System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace> System.Net.Sockets.SafeSocketHandle::threads_stacktraces
	Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* ___threads_stacktraces_7;
	// System.Boolean System.Net.Sockets.SafeSocketHandle::in_cleanup
	bool ___in_cleanup_8;
};

// System.Net.Security.ServerCertSelectionCallback
struct ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654  : public MulticastDelegate_t
{
};

// System.StackOverflowException
struct StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.Threading.ThreadAbortException
struct ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4  : public SystemException_tCC48D868298F4C0705279823E34B00F4FBDB7295
{
};

// System.Threading.WaitCallback
struct WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3  : public MulticastDelegate_t
{
};

// System.ArgumentNullException
struct ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129  : public ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263
{
};

// System.ArgumentOutOfRangeException
struct ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F  : public ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263
{
	// System.Object System.ArgumentOutOfRangeException::_actualValue
	RuntimeObject* ____actualValue_19;
};

// System.ObjectDisposedException
struct ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB  : public InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB
{
	// System.String System.ObjectDisposedException::_objectName
	String_t* ____objectName_18;
};

// System.PlatformNotSupportedException
struct PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A  : public NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A
{
};

// System.ComponentModel.Win32Exception
struct Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9  : public ExternalException_t419875A3CD3C551692EDBBC99E4927E69F2E1F4C
{
	// System.Int32 System.ComponentModel.Win32Exception::nativeErrorCode
	int32_t ___nativeErrorCode_18;
};

// System.Net.Sockets.SocketException
struct SocketException_t6D10102A62EA871BD31748E026A372DB6804083B  : public Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9
{
	// System.Net.EndPoint System.Net.Sockets.SocketException::m_EndPoint
	EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___m_EndPoint_19;
};

// Unity.ThrowStub
struct ThrowStub_tA028CA7941154A8E0F17F9AD0563D538617A7A47  : public ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB
{
};

// System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>

// System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>

// System.Collections.Generic.List`1<System.Object>
struct List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D_StaticFields
{
	// T[] System.Collections.Generic.List`1::s_emptyArray
	ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* ___s_emptyArray_5;
};

// System.Collections.Generic.List`1<System.Object>

// System.Collections.Generic.List`1<System.Threading.Thread>
struct List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D_StaticFields
{
	// T[] System.Collections.Generic.List`1::s_emptyArray
	ThreadU5BU5D_t9D34A6A5BE8998FAD6B8556B1E16BF0C836572B2* ___s_emptyArray_5;
};

// System.Collections.Generic.List`1<System.Threading.Thread>

// <PrivateImplementationDetails>
struct U3CPrivateImplementationDetailsU3E_t56F624E1051A2E261613B6A81CA2333397F49CB3_StaticFields
{
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=256 <PrivateImplementationDetails>::00C700F38385659BA060672F86D4A9A5376EADF9ED1CABB1C63290A0FDEFE36A
	__StaticArrayInitTypeSizeU3D256_tD401FB34D66ACCFAD94A32F8001469D038F350C8 ___00C700F38385659BA060672F86D4A9A5376EADF9ED1CABB1C63290A0FDEFE36A_0;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32 <PrivateImplementationDetails>::04D9D643E5A26924DC98C9906714082D395DCB17CEE9674D5EBFEC15D098C2B5
	__StaticArrayInitTypeSizeU3D32_t543FA0727A92B3B7D8A0EB86D73AB8F35AB0CF48 ___04D9D643E5A26924DC98C9906714082D395DCB17CEE9674D5EBFEC15D098C2B5_1;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32 <PrivateImplementationDetails>::063691760A57230E8119DF650327304D68AE1C009E476E48BE8C73DCA199D374
	__StaticArrayInitTypeSizeU3D32_t543FA0727A92B3B7D8A0EB86D73AB8F35AB0CF48 ___063691760A57230E8119DF650327304D68AE1C009E476E48BE8C73DCA199D374_2;
	// System.Int64 <PrivateImplementationDetails>::1417FCAA8455DC8E3C88BF916DD5C75746A2597CC052F212A8E4394C890E012B
	int64_t ___1417FCAA8455DC8E3C88BF916DD5C75746A2597CC052F212A8E4394C890E012B_3;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=9 <PrivateImplementationDetails>::27625E383C3A91E8B65BC745FF5D4048C82B883CCD293B07DED697BF82733811
	__StaticArrayInitTypeSizeU3D9_tE2A1104C752B51004E61139D4615CC263948C4A7 ___27625E383C3A91E8B65BC745FF5D4048C82B883CCD293B07DED697BF82733811_4;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32 <PrivateImplementationDetails>::2EF83B43314F8CD03190EEE30ECCF048DA37791237F27C62A579F23EACE9FD70
	__StaticArrayInitTypeSizeU3D32_t543FA0727A92B3B7D8A0EB86D73AB8F35AB0CF48 ___2EF83B43314F8CD03190EEE30ECCF048DA37791237F27C62A579F23EACE9FD70_5;
	// System.Int64 <PrivateImplementationDetails>::3505B8A2248AC03FE41ACADF8F29294572BBADEE1DD2E1A45D025766681C012C
	int64_t ___3505B8A2248AC03FE41ACADF8F29294572BBADEE1DD2E1A45D025766681C012C_6;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=12 <PrivateImplementationDetails>::356A582FCE68ACAE8FC944F7B4F0C0DA2BFF4A90D3767C75905F3EDE2E4E66CF
	__StaticArrayInitTypeSizeU3D12_t32C4FAAB522AC6551334F4E397A28A8CF95552DD ___356A582FCE68ACAE8FC944F7B4F0C0DA2BFF4A90D3767C75905F3EDE2E4E66CF_7;
	// System.Int64 <PrivateImplementationDetails>::6C8F647E1FCD63826D12272FF0930E9C16F1FFEF719948703A4C0F3B90052885
	int64_t ___6C8F647E1FCD63826D12272FF0930E9C16F1FFEF719948703A4C0F3B90052885_8;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=128 <PrivateImplementationDetails>::8109EF063456779751E8A4AEE94A0BE9AE32827B6EB153ABD7F40057FF2C9CE3
	__StaticArrayInitTypeSizeU3D128_tCC4BD0E4F5977323EDA6FFF6CD1484F1848F5B0E ___8109EF063456779751E8A4AEE94A0BE9AE32827B6EB153ABD7F40057FF2C9CE3_9;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=44 <PrivateImplementationDetails>::9001C3EDE1D7E939C07996B84F82F7CBE59A7266C1FC2735E959E3C8609602E8
	__StaticArrayInitTypeSizeU3D44_tDBE82A359520A01FA0C117855F1C3185BA1AEB0F ___9001C3EDE1D7E939C07996B84F82F7CBE59A7266C1FC2735E959E3C8609602E8_10;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=6 <PrivateImplementationDetails>::9F58F1261D211553DE8FBC1AFA477C84EA3974B9BFF38223D771D0CDA787E15A
	__StaticArrayInitTypeSizeU3D6_tA4881AF2477A5E66902A636B307315D4FA964D8D ___9F58F1261D211553DE8FBC1AFA477C84EA3974B9BFF38223D771D0CDA787E15A_11;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=128 <PrivateImplementationDetails>::B849624EC20707184A433E21DAAC963906D4B261A19BD8F139F0E269E59E308C
	__StaticArrayInitTypeSizeU3D128_tCC4BD0E4F5977323EDA6FFF6CD1484F1848F5B0E ___B849624EC20707184A433E21DAAC963906D4B261A19BD8F139F0E269E59E308C_12;
	// System.Int64 <PrivateImplementationDetails>::BB6BB42AC5453BD8AA992CCB8929F1CB7D4E112E3FC56E57D711E106D1F72859
	int64_t ___BB6BB42AC5453BD8AA992CCB8929F1CB7D4E112E3FC56E57D711E106D1F72859_13;
	// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=14 <PrivateImplementationDetails>::DFA7289CF8D8029ACEE90F30530C6926950E8DD20285772458F271AFD015BAD5
	__StaticArrayInitTypeSizeU3D14_t5A7362C844CE0A2E27C9434CD029FA82339CFF26 ___DFA7289CF8D8029ACEE90F30530C6926950E8DD20285772458F271AFD015BAD5_14;
};

// <PrivateImplementationDetails>

// System.Configuration.ConfigurationElement

// System.Configuration.ConfigurationElement

// System.Configuration.ConfigurationPropertyCollection

// System.Configuration.ConfigurationPropertyCollection

// System.Diagnostics.DiagnosticsConfigurationHandler

// System.Diagnostics.DiagnosticsConfigurationHandler

// System.Net.EndPoint

// System.Net.EndPoint

// System.EventArgs
struct EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377_StaticFields
{
	// System.EventArgs System.EventArgs::Empty
	EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377* ___Empty_0;
};

// System.EventArgs

// System.IOAsyncResult

// System.IOAsyncResult

// System.Net.IPAddress
struct IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484_StaticFields
{
	// System.Net.IPAddress System.Net.IPAddress::Any
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___Any_0;
	// System.Net.IPAddress System.Net.IPAddress::Loopback
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___Loopback_1;
	// System.Net.IPAddress System.Net.IPAddress::Broadcast
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___Broadcast_2;
	// System.Net.IPAddress System.Net.IPAddress::None
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___None_3;
	// System.Net.IPAddress System.Net.IPAddress::IPv6Any
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___IPv6Any_5;
	// System.Net.IPAddress System.Net.IPAddress::IPv6Loopback
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___IPv6Loopback_6;
	// System.Net.IPAddress System.Net.IPAddress::IPv6None
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___IPv6None_7;
};

// System.Net.IPAddress

// System.Net.Sockets.LingerOption

// System.Net.Sockets.LingerOption

// Mono.Security.Interface.MonoTlsProvider

// Mono.Security.Interface.MonoTlsProvider

// System.Net.Sockets.MulticastOption

// System.Net.Sockets.MulticastOption

// System.Threading.SemaphoreSlim
struct SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2_StaticFields
{
	// System.Threading.Tasks.Task`1<System.Boolean> System.Threading.SemaphoreSlim::s_trueTask
	Task_1_t824317F4B958F7512E8F7300511752937A6C6043* ___s_trueTask_7;
	// System.Threading.Tasks.Task`1<System.Boolean> System.Threading.SemaphoreSlim::s_falseTask
	Task_1_t824317F4B958F7512E8F7300511752937A6C6043* ___s_falseTask_8;
	// System.Action`1<System.Object> System.Threading.SemaphoreSlim::s_cancellationTokenCanceledEventHandler
	Action_1_t6F9EB113EB3F16226AEF811A2744F4111C116C87* ___s_cancellationTokenCanceledEventHandler_9;
};

// System.Threading.SemaphoreSlim

// System.Runtime.Serialization.SerializationInfo

// System.Runtime.Serialization.SerializationInfo

// System.Diagnostics.StackTrace
struct StackTrace_t7C150C7C14136F985311A83A93524B1019F70853_StaticFields
{
	// System.Boolean System.Diagnostics.StackTrace::isAotidSet
	bool ___isAotidSet_3;
	// System.String System.Diagnostics.StackTrace::aotid
	String_t* ___aotid_4;
};

// System.Diagnostics.StackTrace

// System.String
struct String_t_StaticFields
{
	// System.String System.String::Empty
	String_t* ___Empty_6;
};

// System.String

// System.Text.StringBuilder

// System.Text.StringBuilder

// System.Threading.Tasks.Task
struct Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_StaticFields
{
	// System.Int32 System.Threading.Tasks.Task::s_taskIdCounter
	int32_t ___s_taskIdCounter_0;
	// System.Object System.Threading.Tasks.Task::s_taskCompletionSentinel
	RuntimeObject* ___s_taskCompletionSentinel_8;
	// System.Boolean System.Threading.Tasks.Task::s_asyncDebuggingEnabled
	bool ___s_asyncDebuggingEnabled_9;
	// System.Action`1<System.Object> System.Threading.Tasks.Task::s_taskCancelCallback
	Action_1_t6F9EB113EB3F16226AEF811A2744F4111C116C87* ___s_taskCancelCallback_11;
	// System.Func`1<System.Threading.Tasks.Task/ContingentProperties> System.Threading.Tasks.Task::s_createContingentProperties
	Func_1_tD59A12717D79BFB403BF973694B1BE5B85474BD1* ___s_createContingentProperties_14;
	// System.Threading.Tasks.TaskFactory System.Threading.Tasks.Task::<Factory>k__BackingField
	TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* ___U3CFactoryU3Ek__BackingField_15;
	// System.Threading.Tasks.Task System.Threading.Tasks.Task::<CompletedTask>k__BackingField
	Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* ___U3CCompletedTaskU3Ek__BackingField_16;
	// System.Predicate`1<System.Threading.Tasks.Task> System.Threading.Tasks.Task::s_IsExceptionObservedByParentPredicate
	Predicate_1_t7F48518B008C1472339EEEBABA3DE203FE1F26ED* ___s_IsExceptionObservedByParentPredicate_17;
	// System.Threading.ContextCallback System.Threading.Tasks.Task::s_ecCallback
	ContextCallback_tE8AFBDBFCC040FDA8DA8C1EEFE9BD66B16BDA007* ___s_ecCallback_18;
	// System.Predicate`1<System.Object> System.Threading.Tasks.Task::s_IsTaskContinuationNullPredicate
	Predicate_1_t8342C85FF4E41CD1F7024AC0CDC3E5312A32CB12* ___s_IsTaskContinuationNullPredicate_19;
	// System.Collections.Generic.Dictionary`2<System.Int32,System.Threading.Tasks.Task> System.Threading.Tasks.Task::s_currentActiveTasks
	Dictionary_2_t403063CE4960B4F46C688912237C6A27E550FF55* ___s_currentActiveTasks_20;
	// System.Object System.Threading.Tasks.Task::s_activeTasksLock
	RuntimeObject* ___s_activeTasksLock_21;
};

// System.Threading.Tasks.Task
struct Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_ThreadStaticFields
{
	// System.Threading.Tasks.Task System.Threading.Tasks.Task::t_currentTask
	Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* ___t_currentTask_12;
	// System.Threading.Tasks.StackGuard System.Threading.Tasks.Task::t_stackGuard
	StackGuard_tACE063A1B7374BDF4AD472DE4585D05AD8745352* ___t_stackGuard_13;
};

// System.Security.Cryptography.X509Certificates.X509Chain

// System.Security.Cryptography.X509Certificates.X509Chain

// System.Xml.XmlNode

// System.Xml.XmlNode

// System.Net.Sockets.Socket/<>c__DisplayClass240_0

// System.Net.Sockets.Socket/<>c__DisplayClass240_0

// System.Net.Sockets.Socket/<>c__DisplayClass298_0

// System.Net.Sockets.Socket/<>c__DisplayClass298_0

// System.Net.Sockets.SocketAsyncResult/<>c
struct U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields
{
	// System.Net.Sockets.SocketAsyncResult/<>c System.Net.Sockets.SocketAsyncResult/<>c::<>9
	U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* ___U3CU3E9_0;
	// System.Threading.WaitCallback System.Net.Sockets.SocketAsyncResult/<>c::<>9__27_0
	WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* ___U3CU3E9__27_0_1;
};

// System.Net.Sockets.SocketAsyncResult/<>c

// System.Net.Security.SslStream/<>c__DisplayClass21_0

// System.Net.Security.SslStream/<>c__DisplayClass21_0

// System.Collections.Generic.List`1/Enumerator<System.Object>

// System.Collections.Generic.List`1/Enumerator<System.Object>

// System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>

// System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>

// System.Memory`1<System.Byte>

// System.Memory`1<System.Byte>

// System.Threading.Tasks.Task`1<System.Net.IPAddress[]>
struct Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670_StaticFields
{
	// System.Threading.Tasks.TaskFactory`1<TResult> System.Threading.Tasks.Task`1::s_defaultFactory
	TaskFactory_1_tDD05F826FE9D42F2F143BC685A7396C663E6CFE4* ___s_defaultFactory_23;
};

// System.Threading.Tasks.Task`1<System.Net.IPAddress[]>

// System.Threading.Tasks.Task`1<System.Int32>
struct Task_1_t4C228DE57804012969575431CFF12D57C875552D_StaticFields
{
	// System.Threading.Tasks.TaskFactory`1<TResult> System.Threading.Tasks.Task`1::s_defaultFactory
	TaskFactory_1_t0BEF06D58E44525B9135AB0B22D016856EE69FF3* ___s_defaultFactory_23;
};

// System.Threading.Tasks.Task`1<System.Int32>

// System.Boolean
struct Boolean_t09A6377A54BE2F9E6985A8149F19234FD7DDFE22_StaticFields
{
	// System.String System.Boolean::TrueString
	String_t* ___TrueString_5;
	// System.String System.Boolean::FalseString
	String_t* ___FalseString_6;
};

// System.Boolean

// System.Net.Configuration.BypassElement

// System.Net.Configuration.BypassElement

// System.Byte

// System.Byte

// System.Threading.CancellationToken
struct CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED_StaticFields
{
	// System.Action`1<System.Object> System.Threading.CancellationToken::s_actionToActionObjShunt
	Action_1_t6F9EB113EB3F16226AEF811A2744F4111C116C87* ___s_actionToActionObjShunt_1;
};

// System.Threading.CancellationToken

// System.Char
struct Char_t521A6F19B456D956AF452D926C32709DC03D6B17_StaticFields
{
	// System.Byte[] System.Char::s_categoryForLatin1
	ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___s_categoryForLatin1_3;
};

// System.Char

// System.Net.Configuration.ConnectionManagementElement

// System.Net.Configuration.ConnectionManagementElement

// System.Net.Configuration.HttpWebRequestElement

// System.Net.Configuration.HttpWebRequestElement

// System.Net.IPEndPoint
struct IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB_StaticFields
{
	// System.Net.IPEndPoint System.Net.IPEndPoint::Any
	IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* ___Any_2;
	// System.Net.IPEndPoint System.Net.IPEndPoint::IPv6Any
	IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* ___IPv6Any_3;
};

// System.Net.IPEndPoint

// System.Int32

// System.Int32

// System.Int64

// System.Int64

// System.IntPtr
struct IntPtr_t_StaticFields
{
	// System.IntPtr System.IntPtr::Zero
	intptr_t ___Zero_1;
};

// System.IntPtr

// System.Net.Configuration.Ipv6Element

// System.Net.Configuration.Ipv6Element

// Mono.Net.Security.MobileTlsProvider

// Mono.Net.Security.MobileTlsProvider

// System.Net.Configuration.NetSectionGroup

// System.Net.Configuration.NetSectionGroup

// System.Net.Configuration.PerformanceCountersElement

// System.Net.Configuration.PerformanceCountersElement

// System.Net.Configuration.ProxyElement

// System.Net.Configuration.ProxyElement

// System.Net.Configuration.ServicePointManagerElement

// System.Net.Configuration.ServicePointManagerElement

// System.Net.Configuration.SocketElement

// System.Net.Configuration.SocketElement

// System.IO.Stream
struct Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_StaticFields
{
	// System.IO.Stream System.IO.Stream::Null
	Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___Null_1;
};

// System.IO.Stream

// System.Threading.Thread
struct Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F_StaticFields
{
	// System.LocalDataStoreMgr System.Threading.Thread::s_LocalDataStoreMgr
	LocalDataStoreMgr_t205F1783D5CC2B148E829B5882E5406FF9A3AC1E* ___s_LocalDataStoreMgr_0;
	// System.Threading.AsyncLocal`1<System.Globalization.CultureInfo> System.Threading.Thread::s_asyncLocalCurrentCulture
	AsyncLocal_1_t1D3339EA4C8650D2DEDDF9553E5C932B3DC2CCFD* ___s_asyncLocalCurrentCulture_4;
	// System.Threading.AsyncLocal`1<System.Globalization.CultureInfo> System.Threading.Thread::s_asyncLocalCurrentUICulture
	AsyncLocal_1_t1D3339EA4C8650D2DEDDF9553E5C932B3DC2CCFD* ___s_asyncLocalCurrentUICulture_5;
};

// System.Threading.Thread
struct Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F_ThreadStaticFields
{
	// System.LocalDataStoreHolder System.Threading.Thread::s_LocalDataStore
	LocalDataStoreHolder_t789DD474AE5141213C2105CE57830ECFC2D3C03F* ___s_LocalDataStore_1;
	// System.Globalization.CultureInfo System.Threading.Thread::m_CurrentCulture
	CultureInfo_t9BA817D41AD55AC8BD07480DD8AC22F8FFA378E0* ___m_CurrentCulture_2;
	// System.Globalization.CultureInfo System.Threading.Thread::m_CurrentUICulture
	CultureInfo_t9BA817D41AD55AC8BD07480DD8AC22F8FFA378E0* ___m_CurrentUICulture_3;
	// System.Threading.Thread System.Threading.Thread::current_thread
	Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___current_thread_9;
};

// System.UInt32

// System.UInt32

// System.Void

// System.Void

// System.Net.Configuration.WebProxyScriptElement

// System.Net.Configuration.WebProxyScriptElement

// System.Net.Configuration.WebRequestModuleElement

// System.Net.Configuration.WebRequestModuleElement

// System.Security.Cryptography.X509Certificates.X509CertificateCollection

// System.Security.Cryptography.X509Certificates.X509CertificateCollection

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=12

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=12

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=128

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=128

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=14

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=14

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=256

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=256

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=32

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=44

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=44

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=6

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=6

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=9

// <PrivateImplementationDetails>/__StaticArrayInitTypeSize=9

// System.Net.Sockets.AddressFamily

// System.Net.Sockets.AddressFamily

// System.Net.Security.AuthenticatedStream

// System.Net.Security.AuthenticatedStream

// System.Net.Security.AuthenticationLevel

// System.Net.Security.AuthenticationLevel

// System.Net.Configuration.BypassElementCollection

// System.Net.Configuration.BypassElementCollection

// System.Net.Configuration.ConnectionManagementElementCollection

// System.Net.Configuration.ConnectionManagementElementCollection

// System.Net.Configuration.ConnectionManagementSection

// System.Net.Configuration.ConnectionManagementSection

// System.Net.Configuration.DefaultProxySection

// System.Net.Configuration.DefaultProxySection

// System.Delegate

// System.Delegate

// System.Net.Security.EncryptionPolicy

// System.Net.Security.EncryptionPolicy

// System.Exception
struct Exception_t_StaticFields
{
	// System.Object System.Exception::s_EDILock
	RuntimeObject* ___s_EDILock_0;
};

// System.Exception

// System.Net.Sockets.IOControlCode

// System.Net.Sockets.IOControlCode

// System.Net.Sockets.IPProtectionLevel

// System.Net.Sockets.IPProtectionLevel

// System.Net.Sockets.NetworkStream

// System.Net.Sockets.NetworkStream

// System.Net.Sockets.ProtocolType

// System.Net.Sockets.ProtocolType

// System.Runtime.InteropServices.SafeHandle

// System.Runtime.InteropServices.SafeHandle

// System.IO.SeekOrigin

// System.IO.SeekOrigin

// System.Net.Sockets.SelectMode

// System.Net.Sockets.SelectMode

// System.Net.Configuration.SettingsSection

// System.Net.Configuration.SettingsSection

// System.Net.Sockets.SocketAsyncOperation

// System.Net.Sockets.SocketAsyncOperation

// System.Net.Sockets.SocketError

// System.Net.Sockets.SocketError

// System.Net.Sockets.SocketFlags

// System.Net.Sockets.SocketFlags

// System.Net.Sockets.SocketOperation

// System.Net.Sockets.SocketOperation

// System.Net.Sockets.SocketOptionLevel

// System.Net.Sockets.SocketOptionLevel

// System.Net.Sockets.SocketOptionName

// System.Net.Sockets.SocketOptionName

// System.Net.Sockets.SocketShutdown

// System.Net.Sockets.SocketShutdown

// System.Net.Sockets.SocketType

// System.Net.Sockets.SocketType

// System.Net.Security.SslPolicyErrors

// System.Net.Security.SslPolicyErrors

// System.Security.Authentication.SslProtocols

// System.Security.Authentication.SslProtocols

// System.Net.Configuration.WebRequestModuleElementCollection

// System.Net.Configuration.WebRequestModuleElementCollection

// System.Net.Configuration.WebRequestModulesSection

// System.Net.Configuration.WebRequestModulesSection

// System.Security.Cryptography.X509Certificates.X509Certificate

// System.Security.Cryptography.X509Certificates.X509Certificate

// System.Security.Cryptography.X509Certificates.X509RevocationMode

// System.Security.Cryptography.X509Certificates.X509RevocationMode

// System.Net.Sockets.Socket/<>c__DisplayClass355_0

// System.Net.Sockets.Socket/<>c__DisplayClass355_0

// System.AggregateException

// System.AggregateException

// System.IOSelectorJob

// System.IOSelectorJob

// Mono.Net.Security.MobileAuthenticatedStream
struct MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E_StaticFields
{
	// System.Int32 Mono.Net.Security.MobileAuthenticatedStream::uniqueNameInteger
	int32_t ___uniqueNameInteger_17;
	// System.Int32 Mono.Net.Security.MobileAuthenticatedStream::nextId
	int32_t ___nextId_22;
};

// Mono.Net.Security.MobileAuthenticatedStream

// Microsoft.Win32.SafeHandles.SafeHandleMinusOneIsInvalid

// Microsoft.Win32.SafeHandles.SafeHandleMinusOneIsInvalid

// System.Net.Sockets.Socket
struct Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_StaticFields
{
	// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs> System.Net.Sockets.Socket::AcceptCompletedHandler
	EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* ___AcceptCompletedHandler_0;
	// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs> System.Net.Sockets.Socket::ReceiveCompletedHandler
	EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* ___ReceiveCompletedHandler_1;
	// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs> System.Net.Sockets.Socket::SendCompletedHandler
	EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* ___SendCompletedHandler_2;
	// System.Net.Sockets.Socket/TaskSocketAsyncEventArgs`1<System.Net.Sockets.Socket> System.Net.Sockets.Socket::s_rentedSocketSentinel
	TaskSocketAsyncEventArgs_1_tEB937620E5B15D91E5BFEFFA707CF800930F8401* ___s_rentedSocketSentinel_3;
	// System.Net.Sockets.Socket/Int32TaskSocketAsyncEventArgs System.Net.Sockets.Socket::s_rentedInt32Sentinel
	Int32TaskSocketAsyncEventArgs_t36C5FC82499ED9DAFE7F05C38EF92D77A0B248E9* ___s_rentedInt32Sentinel_4;
	// System.Threading.Tasks.Task`1<System.Int32> System.Net.Sockets.Socket::s_zeroTask
	Task_1_t4C228DE57804012969575431CFF12D57C875552D* ___s_zeroTask_5;
	// System.Object System.Net.Sockets.Socket::s_InternalSyncObject
	RuntimeObject* ___s_InternalSyncObject_7;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.Socket::s_SupportsIPv4
	bool ___s_SupportsIPv4_8;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.Socket::s_SupportsIPv6
	bool ___s_SupportsIPv6_9;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.Socket::s_OSSupportsIPv6
	bool ___s_OSSupportsIPv6_10;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.Socket::s_Initialized
	bool ___s_Initialized_11;
	// System.Boolean modreq(System.Runtime.CompilerServices.IsVolatile) System.Net.Sockets.Socket::s_LoggingEnabled
	bool ___s_LoggingEnabled_12;
	// System.AsyncCallback System.Net.Sockets.Socket::AcceptAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___AcceptAsyncCallback_28;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginAcceptCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginAcceptCallback_29;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginAcceptReceiveCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginAcceptReceiveCallback_30;
	// System.AsyncCallback System.Net.Sockets.Socket::ConnectAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___ConnectAsyncCallback_31;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginConnectCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginConnectCallback_32;
	// System.AsyncCallback System.Net.Sockets.Socket::DisconnectAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___DisconnectAsyncCallback_33;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginDisconnectCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginDisconnectCallback_34;
	// System.AsyncCallback System.Net.Sockets.Socket::ReceiveAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___ReceiveAsyncCallback_35;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginReceiveCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginReceiveCallback_36;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginReceiveGenericCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginReceiveGenericCallback_37;
	// System.AsyncCallback System.Net.Sockets.Socket::ReceiveFromAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___ReceiveFromAsyncCallback_38;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginReceiveFromCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginReceiveFromCallback_39;
	// System.AsyncCallback System.Net.Sockets.Socket::SendAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___SendAsyncCallback_40;
	// System.IOAsyncCallback System.Net.Sockets.Socket::BeginSendGenericCallback
	IOAsyncCallback_tDBBA8BBDA6B203613680E77BD4AD6320A1268388* ___BeginSendGenericCallback_41;
	// System.AsyncCallback System.Net.Sockets.Socket::SendToAsyncCallback
	AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___SendToAsyncCallback_42;
};

// System.Net.Sockets.Socket

// System.Net.Sockets.SocketAsyncEventArgs

// System.Net.Sockets.SocketAsyncEventArgs

// System.Net.Sockets.SocketAsyncResult

// System.Net.Sockets.SocketAsyncResult

// System.Net.Security.SslClientAuthenticationOptions

// System.Net.Security.SslClientAuthenticationOptions

// System.Net.Security.SslStream

// System.Net.Security.SslStream

// System.Runtime.Serialization.StreamingContext

// System.Runtime.Serialization.StreamingContext

// System.Threading.Tasks.TaskFactory

// System.Threading.Tasks.TaskFactory

// System.Net.Sockets.TcpClient

// System.Net.Sockets.TcpClient

// System.Type
struct Type_t_StaticFields
{
	// System.Reflection.Binder modreq(System.Runtime.CompilerServices.IsVolatile) System.Type::s_defaultBinder
	Binder_t91BFCE95A7057FADF4D8A1A342AFE52872246235* ___s_defaultBinder_0;
	// System.Char System.Type::Delimiter
	Il2CppChar ___Delimiter_1;
	// System.Type[] System.Type::EmptyTypes
	TypeU5BU5D_t97234E1129B564EB38B8D85CAC2AD8B5B9522FFB* ___EmptyTypes_2;
	// System.Object System.Type::Missing
	RuntimeObject* ___Missing_3;
	// System.Reflection.MemberFilter System.Type::FilterAttribute
	MemberFilter_tF644F1AE82F611B677CE1964D5A3277DDA21D553* ___FilterAttribute_4;
	// System.Reflection.MemberFilter System.Type::FilterName
	MemberFilter_tF644F1AE82F611B677CE1964D5A3277DDA21D553* ___FilterName_5;
	// System.Reflection.MemberFilter System.Type::FilterNameIgnoreCase
	MemberFilter_tF644F1AE82F611B677CE1964D5A3277DDA21D553* ___FilterNameIgnoreCase_6;
};

// System.Type

// System.Action`1<System.IAsyncResult>

// System.Action`1<System.IAsyncResult>

// System.EventHandler`1<System.Object>

// System.EventHandler`1<System.Object>

// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs>

// System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs>

// System.Func`5<System.String,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>

// System.Func`5<System.String,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>

// System.ArgumentException

// System.ArgumentException

// System.AsyncCallback

// System.AsyncCallback

// System.InvalidOperationException

// System.InvalidOperationException

// System.Net.Security.LocalCertSelectionCallback

// System.Net.Security.LocalCertSelectionCallback

// System.Net.Security.LocalCertificateSelectionCallback

// System.Net.Security.LocalCertificateSelectionCallback

// System.Threading.ManualResetEvent

// System.Threading.ManualResetEvent

// Mono.Security.Interface.MonoLocalCertificateSelectionCallback

// Mono.Security.Interface.MonoLocalCertificateSelectionCallback

// Mono.Security.Interface.MonoRemoteCertificateValidationCallback

// Mono.Security.Interface.MonoRemoteCertificateValidationCallback

// Mono.Security.Interface.MonoTlsSettings
struct MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0_StaticFields
{
	// Mono.Security.Interface.MonoTlsSettings Mono.Security.Interface.MonoTlsSettings::defaultSettings
	MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* ___defaultSettings_18;
};

// Mono.Security.Interface.MonoTlsSettings

// System.NotSupportedException

// System.NotSupportedException

// System.OperationCanceledException

// System.OperationCanceledException

// System.OutOfMemoryException

// System.OutOfMemoryException

// System.Net.Security.RemoteCertificateValidationCallback

// System.Net.Security.RemoteCertificateValidationCallback

// System.Net.Sockets.SafeSocketHandle
struct SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields
{
	// System.Boolean System.Net.Sockets.SafeSocketHandle::THROW_ON_ABORT_RETRIES
	bool ___THROW_ON_ABORT_RETRIES_9;
};

// System.Net.Sockets.SafeSocketHandle

// System.Net.Security.ServerCertSelectionCallback

// System.Net.Security.ServerCertSelectionCallback

// System.StackOverflowException

// System.StackOverflowException

// System.Threading.ThreadAbortException

// System.Threading.ThreadAbortException

// System.Threading.WaitCallback

// System.Threading.WaitCallback

// System.ArgumentNullException

// System.ArgumentNullException

// System.ArgumentOutOfRangeException

// System.ArgumentOutOfRangeException

// System.ObjectDisposedException

// System.ObjectDisposedException

// System.PlatformNotSupportedException

// System.PlatformNotSupportedException

// System.ComponentModel.Win32Exception

// System.ComponentModel.Win32Exception

// System.Net.Sockets.SocketException

// System.Net.Sockets.SocketException

// Unity.ThrowStub

// Unity.ThrowStub
#ifdef __clang__
#pragma clang diagnostic pop
#endif
// System.Net.IPAddress[]
struct IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D  : public RuntimeArray
{
	ALIGN_FIELD (8) IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* m_Items[1];

	inline IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* GetAt(il2cpp_array_size_t index) const
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items[index];
	}
	inline IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484** GetAddressAt(il2cpp_array_size_t index)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items + index;
	}
	inline void SetAt(il2cpp_array_size_t index, IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* value)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
	inline IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* GetAtUnchecked(il2cpp_array_size_t index) const
	{
		return m_Items[index];
	}
	inline IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484** GetAddressAtUnchecked(il2cpp_array_size_t index)
	{
		return m_Items + index;
	}
	inline void SetAtUnchecked(il2cpp_array_size_t index, IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* value)
	{
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
};
// System.Object[]
struct ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918  : public RuntimeArray
{
	ALIGN_FIELD (8) RuntimeObject* m_Items[1];

	inline RuntimeObject* GetAt(il2cpp_array_size_t index) const
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items[index];
	}
	inline RuntimeObject** GetAddressAt(il2cpp_array_size_t index)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items + index;
	}
	inline void SetAt(il2cpp_array_size_t index, RuntimeObject* value)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
	inline RuntimeObject* GetAtUnchecked(il2cpp_array_size_t index) const
	{
		return m_Items[index];
	}
	inline RuntimeObject** GetAddressAtUnchecked(il2cpp_array_size_t index)
	{
		return m_Items + index;
	}
	inline void SetAtUnchecked(il2cpp_array_size_t index, RuntimeObject* value)
	{
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
};
// System.Byte[]
struct ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031  : public RuntimeArray
{
	ALIGN_FIELD (8) uint8_t m_Items[1];

	inline uint8_t GetAt(il2cpp_array_size_t index) const
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items[index];
	}
	inline uint8_t* GetAddressAt(il2cpp_array_size_t index)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items + index;
	}
	inline void SetAt(il2cpp_array_size_t index, uint8_t value)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		m_Items[index] = value;
	}
	inline uint8_t GetAtUnchecked(il2cpp_array_size_t index) const
	{
		return m_Items[index];
	}
	inline uint8_t* GetAddressAtUnchecked(il2cpp_array_size_t index)
	{
		return m_Items + index;
	}
	inline void SetAtUnchecked(il2cpp_array_size_t index, uint8_t value)
	{
		m_Items[index] = value;
	}
};
// System.Delegate[]
struct DelegateU5BU5D_tC5AB7E8F745616680F337909D3A8E6C722CDF771  : public RuntimeArray
{
	ALIGN_FIELD (8) Delegate_t* m_Items[1];

	inline Delegate_t* GetAt(il2cpp_array_size_t index) const
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items[index];
	}
	inline Delegate_t** GetAddressAt(il2cpp_array_size_t index)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items + index;
	}
	inline void SetAt(il2cpp_array_size_t index, Delegate_t* value)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
	inline Delegate_t* GetAtUnchecked(il2cpp_array_size_t index) const
	{
		return m_Items[index];
	}
	inline Delegate_t** GetAddressAtUnchecked(il2cpp_array_size_t index)
	{
		return m_Items + index;
	}
	inline void SetAtUnchecked(il2cpp_array_size_t index, Delegate_t* value)
	{
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
};
// System.String[]
struct StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248  : public RuntimeArray
{
	ALIGN_FIELD (8) String_t* m_Items[1];

	inline String_t* GetAt(il2cpp_array_size_t index) const
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items[index];
	}
	inline String_t** GetAddressAt(il2cpp_array_size_t index)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		return m_Items + index;
	}
	inline void SetAt(il2cpp_array_size_t index, String_t* value)
	{
		IL2CPP_ARRAY_BOUNDS_CHECK(index, (uint32_t)(this)->max_length);
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
	inline String_t* GetAtUnchecked(il2cpp_array_size_t index) const
	{
		return m_Items[index];
	}
	inline String_t** GetAddressAtUnchecked(il2cpp_array_size_t index)
	{
		return m_Items + index;
	}
	inline void SetAtUnchecked(il2cpp_array_size_t index, String_t* value)
	{
		m_Items[index] = value;
		Il2CppCodeGenWriteBarrier((void**)m_Items + index, (void*)value);
	}
};

IL2CPP_EXTERN_C void Exception_t_marshal_pinvoke(const Exception_t& unmarshaled, Exception_t_marshaled_pinvoke& marshaled);
IL2CPP_EXTERN_C void Exception_t_marshal_pinvoke_back(const Exception_t_marshaled_pinvoke& marshaled, Exception_t& unmarshaled);
IL2CPP_EXTERN_C void Exception_t_marshal_pinvoke_cleanup(Exception_t_marshaled_pinvoke& marshaled);
IL2CPP_EXTERN_C void Exception_t_marshal_com(const Exception_t& unmarshaled, Exception_t_marshaled_com& marshaled);
IL2CPP_EXTERN_C void Exception_t_marshal_com_back(const Exception_t_marshaled_com& marshaled, Exception_t& unmarshaled);
IL2CPP_EXTERN_C void Exception_t_marshal_com_cleanup(Exception_t_marshaled_com& marshaled);

// TResult System.Threading.Tasks.Task`1<System.Object>::get_Result()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* Task_1_get_Result_m6897AA7D4A583F9E81EC01C1BF788372D64C5A26_gshared (Task_1_t0C4CD3A5BB93A184420D73218644C56C70FDA7E2* __this, const RuntimeMethod* method) ;
// System.Void System.Func`5<System.Object,System.Int32,System.Object,System.Object,System.Object>::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Func_5__ctor_m9D2CC015419A9C8DA2FCE687A56D04B455180559_gshared (Func_5_tD02E129BCD67EF3C2C268C78FD4614374A3597CA* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) ;
// System.Void System.Action`1<System.Object>::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Action_1__ctor_m2E1DFA67718FC1A0B6E5DFEB78831FFE9C059EB4_gshared (Action_1_t6F9EB113EB3F16226AEF811A2744F4111C116C87* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) ;
// System.Threading.Tasks.Task System.Threading.Tasks.TaskFactory::FromAsync<System.Object,System.Int32>(System.Func`5<TArg1,TArg2,System.AsyncCallback,System.Object,System.IAsyncResult>,System.Action`1<System.IAsyncResult>,TArg1,TArg2,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* TaskFactory_FromAsync_TisRuntimeObject_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_m9F0B7197E1791E4B49DEB7271C267973AC699E94_gshared (TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* __this, Func_5_tA07BEDDD813173420A179C25AEDEA235CEB634BB* ___0_beginMethod, Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780* ___1_endMethod, RuntimeObject* ___2_arg1, int32_t ___3_arg2, RuntimeObject* ___4_state, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.Dictionary`2<System.Object,System.Object>::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Dictionary_2__ctor_m5B32FBC624618211EB461D59CFBB10E987FD1329_gshared (Dictionary_2_t14FE4A752A83D53771C584E4C8D14E01F2AFD7BA* __this, const RuntimeMethod* method) ;
// System.Collections.Generic.List`1/Enumerator<T> System.Collections.Generic.List`1<System.Object>::GetEnumerator()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A List_1_GetEnumerator_mD8294A7FA2BEB1929487127D476F8EC1CDC23BFC_gshared (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.List`1/Enumerator<System.Object>::Dispose()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Enumerator_Dispose_mD9DC3E3C3697830A4823047AB29A77DBBB5ED419_gshared (Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A* __this, const RuntimeMethod* method) ;
// T System.Collections.Generic.List`1/Enumerator<System.Object>::get_Current()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR RuntimeObject* Enumerator_get_Current_m6330F15D18EE4F547C05DF9BF83C5EB710376027_gshared_inline (Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A* __this, const RuntimeMethod* method) ;
// TValue System.Collections.Generic.Dictionary`2<System.Object,System.Object>::get_Item(TKey)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* Dictionary_2_get_Item_m4AAAECBE902A211BF2126E6AFA280AEF73A3E0D6_gshared (Dictionary_2_t14FE4A752A83D53771C584E4C8D14E01F2AFD7BA* __this, RuntimeObject* ___0_key, const RuntimeMethod* method) ;
// System.Boolean System.Collections.Generic.List`1/Enumerator<System.Object>::MoveNext()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Enumerator_MoveNext_mE921CC8F29FBBDE7CC3209A0ED0D921D58D00BCB_gshared (Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A* __this, const RuntimeMethod* method) ;
// System.Int32 System.Collections.Generic.List`1<System.Object>::get_Count()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t List_1_get_Count_m4407E4C389F22B8CEC282C15D56516658746C383_gshared_inline (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, const RuntimeMethod* method) ;
// T System.Collections.Generic.List`1<System.Object>::get_Item(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* List_1_get_Item_m33561245D64798C2AB07584C0EC4F240E4839A38_gshared (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, int32_t ___0_index, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.List`1<System.Object>::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void List_1__ctor_m7F078BB342729BDF11327FD89D7872265328F690_gshared (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.List`1<System.Object>::Add(T)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void List_1_Add_mEBCF994CC3814631017F46A387B1A192ED6C85C7_gshared_inline (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, RuntimeObject* ___0_item, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.Dictionary`2<System.Object,System.Object>::Add(TKey,TValue)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Dictionary_2_Add_m93FFFABE8FCE7FA9793F0915E2A8842C7CD0C0C1_gshared (Dictionary_2_t14FE4A752A83D53771C584E4C8D14E01F2AFD7BA* __this, RuntimeObject* ___0_key, RuntimeObject* ___1_value, const RuntimeMethod* method) ;
// System.Boolean System.Collections.Generic.List`1<System.Object>::Remove(T)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool List_1_Remove_m4DFA48F4CEB9169601E75FC28517C5C06EFA5AD7_gshared (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, RuntimeObject* ___0_item, const RuntimeMethod* method) ;
// System.Int32 System.Collections.Generic.List`1<System.Object>::IndexOf(T)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t List_1_IndexOf_m378F61BA812B79DEE58D86FE8AA9F20E3FC7D85F_gshared (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, RuntimeObject* ___0_item, const RuntimeMethod* method) ;
// System.Boolean System.Collections.Generic.Dictionary`2<System.Object,System.Object>::Remove(TKey)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Dictionary_2_Remove_m5C7C45E75D951A75843F3F7AADD56ECD64F6BC86_gshared (Dictionary_2_t14FE4A752A83D53771C584E4C8D14E01F2AFD7BA* __this, RuntimeObject* ___0_key, const RuntimeMethod* method) ;
// System.Void System.EventHandler`1<System.Object>::Invoke(System.Object,TEventArgs)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void EventHandler_1_Invoke_mBD72C04FF5A08A2EA93DFD21037CD1C27A48D07A_gshared_inline (EventHandler_1_tD8C4A5BE1F7C91B1A7E99AE87AFD2F5432C38746* __this, RuntimeObject* ___0_sender, RuntimeObject* ___1_e, const RuntimeMethod* method) ;
// System.Int32 System.Memory`1<System.Byte>::get_Length()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_gshared (Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036* __this, const RuntimeMethod* method) ;
// System.Memory`1<T> System.Memory`1<System.Byte>::op_Implicit(T[])
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C_gshared (ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_array, const RuntimeMethod* method) ;
// TResult System.Threading.Tasks.TaskToApm::End<System.Int32>(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED_gshared (RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) ;

// System.Void System.Object::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2 (RuntimeObject* __this, const RuntimeMethod* method) ;
// System.Boolean System.Threading.Tasks.Task::get_IsFaulted()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Task_get_IsFaulted_mC0AD3EA4EAF3B47C1F5FE9624541F0A00B9426D9 (Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* __this, const RuntimeMethod* method) ;
// System.AggregateException System.Threading.Tasks.Task::get_Exception()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR AggregateException_t51B6205846DFB356B94452702201DD239D44422F* Task_get_Exception_m11317CABA220E351C84B3E94B8A98F1565F42B45 (Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* __this, const RuntimeMethod* method) ;
// System.Exception System.Exception::get_InnerException()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Exception_t* Exception_get_InnerException_m0C1BDB339C786BA4DA7D2C1AD214571CFBBB1410_inline (Exception_t* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Exception)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_m49FA96276F9EBB207D8194440AE89300DDDAC513 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Exception_t* ___0_e, const RuntimeMethod* method) ;
// System.Boolean System.Threading.Tasks.Task::get_IsCanceled()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Task_get_IsCanceled_m96A8D3F85158A9CB3AEA50A00A55BE4E0F0E21FA (Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* __this, const RuntimeMethod* method) ;
// System.Void System.OperationCanceledException::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void OperationCanceledException__ctor_m2F34C3B8AEE2AA6C7EB2BB77AE5E0289101293E4 (OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662* __this, const RuntimeMethod* method) ;
// TResult System.Threading.Tasks.Task`1<System.Net.IPAddress[]>::get_Result()
inline IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* Task_1_get_Result_m0F2300B3BCA792B7EFFAF002F88E53F5DFC64394 (Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* __this, const RuntimeMethod* method)
{
	return ((  IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* (*) (Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670*, const RuntimeMethod*))Task_1_get_Result_m6897AA7D4A583F9E81EC01C1BF788372D64C5A26_gshared)(__this, method);
}
// System.Boolean System.Net.Sockets.Socket::BeginMConnect(System.Net.Sockets.SocketAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Socket_BeginMConnect_mA33FD740EBFB2B4394392CCEBAAB3DFB630214C9 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* ___0_sockares, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::BeginSendCallback(System.Net.Sockets.SocketAsyncResult,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_BeginSendCallback_m4B6E09619A84BCECCA322F4B20C819428C4861D9 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* ___0_sockares, int32_t ___1_sent_so_far, const RuntimeMethod* method) ;
// System.Boolean System.Net.Sockets.Socket::get_CleanedUp()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Socket_get_CleanedUp_m8F6EDAE37C56CBC282D3EAF9D93B5726EEC85988 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, const RuntimeMethod* method) ;
// System.Void System.IOSelectorJob::MarkDisposed()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOSelectorJob_MarkDisposed_mBCCE13D70B97BB7AE46D976AE207DF642C4531F0 (IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31* __this, const RuntimeMethod* method) ;
// System.Void System.IOSelector::Add(System.IntPtr,System.IOSelectorJob)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOSelector_Add_mC5550BDC7E4702420FACE7E9E0F55361E35DAAC2 (intptr_t ___0_handle, IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31* ___1_job, const RuntimeMethod* method) ;
// System.Int32 System.Net.Sockets.SocketException::WSAGetLastError_icall()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketException_WSAGetLastError_icall_mD9AF5BE627EB9CB0032D7F681CB839141CB99E98 (const RuntimeMethod* method) ;
// System.Void System.ComponentModel.Win32Exception::.ctor(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Win32Exception__ctor_mFC38F1C99031D227C6BB3CA07099866D39994281 (Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9* __this, int32_t ___0_error, const RuntimeMethod* method) ;
// System.Void System.ComponentModel.Win32Exception::.ctor(System.Int32,System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Win32Exception__ctor_mB0380F996944EDFCF9CACC7F28A18874F19B4FD4 (Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9* __this, int32_t ___0_error, String_t* ___1_message, const RuntimeMethod* method) ;
// System.Void System.ComponentModel.Win32Exception::.ctor(System.Runtime.Serialization.SerializationInfo,System.Runtime.Serialization.StreamingContext)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Win32Exception__ctor_m1B3DF3F51BBF050C18F933B36027A8A4902668C4 (Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9* __this, SerializationInfo_t3C47F63E24BEB9FCE2DC6309E027F238DC5C5E37* ___0_info, StreamingContext_t56760522A751890146EE45F82F866B55B7E33677 ___1_context, const RuntimeMethod* method) ;
// System.String System.Exception::get_Message()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* Exception_get_Message_mCFE0D38FDF9001405299AAE43BBE2A9AD806C441 (Exception_t* __this, const RuntimeMethod* method) ;
// System.String System.String::Concat(System.String,System.String,System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* String_Concat_m8855A6DE10F84DA7F4EC113CADDB59873A25573B (String_t* ___0_str0, String_t* ___1_str1, String_t* ___2_str2, const RuntimeMethod* method) ;
// System.Int32 System.ComponentModel.Win32Exception::get_NativeErrorCode()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t Win32Exception_get_NativeErrorCode_m33C1D176D9178C36DA32F8FEC251B923EE53CE5E_inline (Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.LingerOption::set_Enabled(System.Boolean)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void LingerOption_set_Enabled_mEDF2FED046EA58F9C23BEF3BF1184C3A890419DB_inline (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, bool ___0_value, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.LingerOption::set_LingerTime(System.Int32)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void LingerOption_set_LingerTime_mDC6B06F96C04C5A69E19912EBB37DC5BDE5A6DFB_inline (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, int32_t ___0_value, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.TcpClient::.ctor(System.Net.Sockets.AddressFamily)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient__ctor_m94DC92A387808F584010C708C071197B52B48B55 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, int32_t ___0_family, const RuntimeMethod* method) ;
// System.Boolean System.Net.Logging::get_On()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE (const RuntimeMethod* method) ;
// System.String SR::GetString(System.String,System.Object[])
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SR_GetString_m56640423E54C5F10BFDEF27B7E692A33745EB696 (String_t* ___0_name, ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* ___1_args, const RuntimeMethod* method) ;
// System.Void System.ArgumentException::.ctor(System.String,System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ArgumentException__ctor_m8F9D40CE19D19B698A70F9A258640EB52DB39B62 (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* __this, String_t* ___0_message, String_t* ___1_paramName, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.TcpClient::initialize()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_initialize_mE26B7664FC1F1DB4D963065A7E47848798697114 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) ;
// System.Type System.Object::GetType()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Type_t* Object_GetType_mE10A8FC1E57F3DF29972CCBC026C2DC3942263B3 (RuntimeObject* __this, const RuntimeMethod* method) ;
// System.Void System.ObjectDisposedException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48 (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* __this, String_t* ___0_objectName, const RuntimeMethod* method) ;
// System.Void System.ArgumentNullException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ArgumentNullException__ctor_m444AE141157E333844FC1A9500224C2F9FD24F4B (ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129* __this, String_t* ___0_paramName, const RuntimeMethod* method) ;
// System.Boolean System.Net.ValidationHelper::ValidateTcpPort(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool ValidationHelper_ValidateTcpPort_m307286F0DF94E8741F0359186B58464E422B1B4A (int32_t ___0_port, const RuntimeMethod* method) ;
// System.Void System.ArgumentOutOfRangeException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ArgumentOutOfRangeException__ctor_mBC1D5DEEA1BA41DE77228CB27D6BAFEB6DCCBF4A (ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F* __this, String_t* ___0_paramName, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketException::.ctor(System.Net.Sockets.SocketError)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_m5E9DDE94C8009DE11B8CE2EF1611A5CE8A900AC3 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, int32_t ___0_socketError, const RuntimeMethod* method) ;
// System.Net.IPAddress[] System.Net.Dns::GetHostAddresses(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* Dns_GetHostAddresses_mB44EA8DE1F3C53F533578949FED6BB98B0F1F212 (String_t* ___0_hostNameOrAddress, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::Close()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, const RuntimeMethod* method) ;
// System.Boolean System.Net.Sockets.Socket::get_OSSupportsIPv4()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Socket_get_OSSupportsIPv4_m17A77A027E3FE4EBDB927A2796551F55C0594889 (const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::.ctor(System.Net.Sockets.AddressFamily,System.Net.Sockets.SocketType,System.Net.Sockets.ProtocolType)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket__ctor_m35F1F4B4872E251867DA16460F06E903A30E4595 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, int32_t ___0_addressFamily, int32_t ___1_socketType, int32_t ___2_protocolType, const RuntimeMethod* method) ;
// System.Boolean System.Net.Sockets.Socket::get_OSSupportsIPv6()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Socket_get_OSSupportsIPv6_mDF10C5F623BBCBB218599E9C4B1AE3E7EE2AB2CB (const RuntimeMethod* method) ;
// System.Net.Sockets.AddressFamily System.Net.IPAddress::get_AddressFamily()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t IPAddress_get_AddressFamily_m1CE4BCCE499BD70B22F9E37B3F266F9306A98C21 (IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::Connect(System.Net.IPAddress,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Connect_mEE1E8320C097486AFA5432284C1B7FFB0DDC35D6 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___0_address, int32_t ___1_port, const RuntimeMethod* method) ;
// System.Void System.Net.IPEndPoint::.ctor(System.Net.IPAddress,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IPEndPoint__ctor_m902C98F9E3F36B20B3C2E030AA91B62E2BC7A85A (IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* __this, IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* ___0_address, int32_t ___1_port, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.TcpClient::Connect(System.Net.IPEndPoint)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* ___0_remoteEP, const RuntimeMethod* method) ;
// System.Net.Sockets.Socket System.Net.Sockets.TcpClient::get_Client()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::Connect(System.Net.EndPoint)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Connect_m874A510F986F4D91446B2F53688D0BCFFE0A6BE1 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___0_remoteEP, const RuntimeMethod* method) ;
// System.IAsyncResult System.Net.Sockets.Socket::BeginConnect(System.String,System.Int32,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* Socket_BeginConnect_mC246F9195AE1804CE282BFCA75B1C14E998244FD (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, String_t* ___0_host, int32_t ___1_port, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___2_callback, RuntimeObject* ___3_state, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::EndConnect(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_EndConnect_m10F5B4F43A68F96891DB3AEF493AB160CD8104D6 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) ;
// System.Threading.Tasks.TaskFactory System.Threading.Tasks.Task::get_Factory()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* Task_get_Factory_m8272CF9106A72E5F0B8E8C6CEE025D748FBD6D11_inline (const RuntimeMethod* method) ;
// System.Void System.Func`5<System.String,System.Int32,System.AsyncCallback,System.Object,System.IAsyncResult>::.ctor(System.Object,System.IntPtr)
inline void Func_5__ctor_m6B3982ABCDD8853956F2D12232F37EAEAC86D18E (Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method)
{
	((  void (*) (Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818*, RuntimeObject*, intptr_t, const RuntimeMethod*))Func_5__ctor_m9D2CC015419A9C8DA2FCE687A56D04B455180559_gshared)(__this, ___0_object, ___1_method, method);
}
// System.Void System.Action`1<System.IAsyncResult>::.ctor(System.Object,System.IntPtr)
inline void Action_1__ctor_m0164A28339B1089B156891E229EE08B0D2184DEA (Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method)
{
	((  void (*) (Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780*, RuntimeObject*, intptr_t, const RuntimeMethod*))Action_1__ctor_m2E1DFA67718FC1A0B6E5DFEB78831FFE9C059EB4_gshared)(__this, ___0_object, ___1_method, method);
}
// System.Threading.Tasks.Task System.Threading.Tasks.TaskFactory::FromAsync<System.String,System.Int32>(System.Func`5<TArg1,TArg2,System.AsyncCallback,System.Object,System.IAsyncResult>,System.Action`1<System.IAsyncResult>,TArg1,TArg2,System.Object)
inline Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* TaskFactory_FromAsync_TisString_t_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mD129D3A417906F121881179FDB395C2AC54E94BC (TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* __this, Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818* ___0_beginMethod, Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780* ___1_endMethod, String_t* ___2_arg1, int32_t ___3_arg2, RuntimeObject* ___4_state, const RuntimeMethod* method)
{
	return ((  Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* (*) (TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0*, Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818*, Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780*, String_t*, int32_t, RuntimeObject*, const RuntimeMethod*))TaskFactory_FromAsync_TisRuntimeObject_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_m9F0B7197E1791E4B49DEB7271C267973AC699E94_gshared)(__this, ___0_beginMethod, ___1_endMethod, ___2_arg1, ___3_arg2, ___4_state, method);
}
// System.Boolean System.Net.Sockets.Socket::get_Connected()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR bool Socket_get_Connected_m6E61ADEB0124FB8D46AC1EDFDC268D3C9F7869C5_inline (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, const RuntimeMethod* method) ;
// System.String SR::GetString(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SR_GetString_m2F0EFCFA5FE3DB6DE89C5823284302D691355AA6 (String_t* ___0_name, const RuntimeMethod* method) ;
// System.Void System.InvalidOperationException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void InvalidOperationException__ctor_mE4CB6F4712AB6D99A2358FBAE2E052B3EE976162 (InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB* __this, String_t* ___0_message, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.NetworkStream::.ctor(System.Net.Sockets.Socket,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void NetworkStream__ctor_mA44C3DB90F5B64F230CC2176DC7E722F4077A9DE (NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_socket, bool ___1_ownsSocket, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.TcpClient::set_Client(System.Net.Sockets.Socket)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void TcpClient_set_Client_m145E6A35F13B52AF10DE077FD19DE79A622E5F5C_inline (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_value, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::InternalShutdown(System.Net.Sockets.SocketShutdown)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_InternalShutdown_mD0EF08D54151D53369D32CD61C8518454AAAEFE8 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, int32_t ___0_how, const RuntimeMethod* method) ;
// System.Void System.GC::SuppressFinalize(System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void GC_SuppressFinalize_m71815DBD5A0CD2EA1BE43317B08B7A14949EDC65 (RuntimeObject* ___0_obj, const RuntimeMethod* method) ;
// System.Void System.Object::Finalize()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Object_Finalize_mC98C96301CCABFE00F1A7EF8E15DF507CACD42B2 (RuntimeObject* __this, const RuntimeMethod* method) ;
// System.Void Microsoft.Win32.SafeHandles.SafeHandleMinusOneIsInvalid::.ctor(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeHandleMinusOneIsInvalid__ctor_m626FCBF3D556AE7B1CBE07DCC4913D1B2D629A46 (SafeHandleMinusOneIsInvalid_t85F57717BC66C06B714910FA13BF2E49AA5BD1A6* __this, bool ___0_ownsHandle, const RuntimeMethod* method) ;
// System.Void System.Runtime.InteropServices.SafeHandle::SetHandle(System.IntPtr)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SafeHandle_SetHandle_m003D64748F9DFBA1E3C0B23798C23BA81AA21C2A_inline (SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7* __this, intptr_t ___0_handle, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>::.ctor()
inline void Dictionary_2__ctor_m50735B02E2653B889CED4DFEFA0C22917F620C78 (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* __this, const RuntimeMethod* method)
{
	((  void (*) (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED*, const RuntimeMethod*))Dictionary_2__ctor_m5B32FBC624618211EB461D59CFBB10E987FD1329_gshared)(__this, method);
}
// System.Void System.Net.Sockets.Socket::Blocking_icall(System.IntPtr,System.Boolean,System.Int32&)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Blocking_icall_m226DF9E2D2C681B8C5B2E115BCA9B308463383B3 (intptr_t ___0_socket, bool ___1_block, int32_t* ___2_error, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::Shutdown_icall(System.IntPtr,System.Net.Sockets.SocketShutdown,System.Int32&)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Shutdown_icall_m96E5BC1E1CB1DE4AF812E9892EFAE78B6638561E (intptr_t ___0_socket, int32_t ___1_how, int32_t* ___2_error, const RuntimeMethod* method) ;
// System.Void System.Threading.Monitor::Exit(System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Monitor_Exit_m05B2CF037E2214B3208198C282490A2A475653FA (RuntimeObject* ___0_obj, const RuntimeMethod* method) ;
// System.Void System.Threading.Monitor::Enter(System.Object,System.Boolean&)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Monitor_Enter_m3CDB589DA1300B513D55FDCFB52B63E879794149 (RuntimeObject* ___0_obj, bool* ___1_lockTaken, const RuntimeMethod* method) ;
// System.Void System.Text.StringBuilder::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void StringBuilder__ctor_m1D99713357DE05DAFA296633639DB55F8C30587D (StringBuilder_t* __this, const RuntimeMethod* method) ;
// System.Text.StringBuilder System.Text.StringBuilder::AppendLine(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR StringBuilder_t* StringBuilder_AppendLine_mF75744CE941C63E33188E22E936B71A24D3CBF88 (StringBuilder_t* __this, String_t* ___0_value, const RuntimeMethod* method) ;
// System.Collections.Generic.List`1/Enumerator<T> System.Collections.Generic.List`1<System.Threading.Thread>::GetEnumerator()
inline Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5 (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, const RuntimeMethod* method)
{
	return ((  Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, const RuntimeMethod*))List_1_GetEnumerator_mD8294A7FA2BEB1929487127D476F8EC1CDC23BFC_gshared)(__this, method);
}
// System.Void System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>::Dispose()
inline void Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9 (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936* __this, const RuntimeMethod* method)
{
	((  void (*) (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936*, const RuntimeMethod*))Enumerator_Dispose_mD9DC3E3C3697830A4823047AB29A77DBBB5ED419_gshared)(__this, method);
}
// T System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>::get_Current()
inline Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_inline (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936* __this, const RuntimeMethod* method)
{
	return ((  Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* (*) (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936*, const RuntimeMethod*))Enumerator_get_Current_m6330F15D18EE4F547C05DF9BF83C5EB710376027_gshared_inline)(__this, method);
}
// TValue System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>::get_Item(TKey)
inline StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* Dictionary_2_get_Item_m5B51F010936EF5BBF6820068B9143D37CB2D2D84 (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_key, const RuntimeMethod* method)
{
	return ((  StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* (*) (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, const RuntimeMethod*))Dictionary_2_get_Item_m4AAAECBE902A211BF2126E6AFA280AEF73A3E0D6_gshared)(__this, ___0_key, method);
}
// System.Boolean System.Collections.Generic.List`1/Enumerator<System.Threading.Thread>::MoveNext()
inline bool Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936* __this, const RuntimeMethod* method)
{
	return ((  bool (*) (Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936*, const RuntimeMethod*))Enumerator_MoveNext_mE921CC8F29FBBDE7CC3209A0ED0D921D58D00BCB_gshared)(__this, method);
}
// System.Text.StringBuilder System.Text.StringBuilder::AppendLine()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR StringBuilder_t* StringBuilder_AppendLine_m3BC704C4E6A8531027D8C9287D0AB2AA0188AC4E (StringBuilder_t* __this, const RuntimeMethod* method) ;
// System.Void System.Exception::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Exception__ctor_m9B2BD92CD68916245A75109105D9071C9D430E7F (Exception_t* __this, String_t* ___0_message, const RuntimeMethod* method) ;
// System.Int32 System.Collections.Generic.List`1<System.Threading.Thread>::get_Count()
inline int32_t List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_inline (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, const RuntimeMethod* method)
{
	return ((  int32_t (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, const RuntimeMethod*))List_1_get_Count_m4407E4C389F22B8CEC282C15D56516658746C383_gshared_inline)(__this, method);
}
// T System.Collections.Generic.List`1<System.Threading.Thread>::get_Item(System.Int32)
inline Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* List_1_get_Item_m60371F3C4A4481F126B9BDBFDD9D69F464780693 (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, int32_t ___0_index, const RuntimeMethod* method)
{
	return ((  Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, int32_t, const RuntimeMethod*))List_1_get_Item_m33561245D64798C2AB07584C0EC4F240E4839A38_gshared)(__this, ___0_index, method);
}
// System.Threading.Thread System.Threading.Thread::get_CurrentThread()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* Thread_get_CurrentThread_m6D4719F4993DB9200490531FF02D4076FF9CA9BD (const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::cancel_blocking_socket_operation(System.Threading.Thread)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_cancel_blocking_socket_operation_mB4D1FCFA8304B975A60833FB2805B767DE533C0E (Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_thread, const RuntimeMethod* method) ;
// System.Boolean System.Threading.Monitor::Wait(System.Object,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Monitor_Wait_m60B0A448270187089CC75DE856B496C452EF4601 (RuntimeObject* ___0_obj, int32_t ___1_millisecondsTimeout, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.Socket::Close_icall(System.IntPtr,System.Int32&)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Socket_Close_icall_mD2A065D0EA71E9007587C22A8B6C3A95B8F5812A (intptr_t ___0_socket, int32_t* ___1_error, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.List`1<System.Threading.Thread>::.ctor()
inline void List_1__ctor_m1104DB0524C8D81E9C97C6068CC22A52CCE146EC (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, const RuntimeMethod* method)
{
	((  void (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, const RuntimeMethod*))List_1__ctor_m7F078BB342729BDF11327FD89D7872265328F690_gshared)(__this, method);
}
// System.Void System.Collections.Generic.List`1<System.Threading.Thread>::Add(T)
inline void List_1_Add_m5F800AC3A9BEE2C4459A42828FB152CD7A848A8C_inline (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_item, const RuntimeMethod* method)
{
	((  void (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, const RuntimeMethod*))List_1_Add_mEBCF994CC3814631017F46A387B1A192ED6C85C7_gshared_inline)(__this, ___0_item, method);
}
// System.Void System.Diagnostics.StackTrace::.ctor(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_NO_INLINE IL2CPP_METHOD_ATTR void StackTrace__ctor_m269D00E49783BC3205FD5C7E879888884D72107C (StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* __this, bool ___0_fNeedFileInfo, const RuntimeMethod* method) ;
// System.Void System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>::Add(TKey,TValue)
inline void Dictionary_2_Add_m021BDE42D4C8CFA7729AE1FA4645832CB8510A45 (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_key, StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* ___1_value, const RuntimeMethod* method)
{
	((  void (*) (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, StackTrace_t7C150C7C14136F985311A83A93524B1019F70853*, const RuntimeMethod*))Dictionary_2_Add_m93FFFABE8FCE7FA9793F0915E2A8842C7CD0C0C1_gshared)(__this, ___0_key, ___1_value, method);
}
// System.Void System.Runtime.InteropServices.SafeHandle::DangerousRelease()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeHandle_DangerousRelease_m30A8B4E5BEA935C8925BC2115CD0AD13B937953E (SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7* __this, const RuntimeMethod* method) ;
// System.Boolean System.Runtime.InteropServices.SafeHandle::get_IsClosed()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SafeHandle_get_IsClosed_mD2CD4AA6E3B0A242E48080F18BC07199CAB80273 (SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketException::.ctor(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_mC09AA11941DA13359416179634B151A3FE2B9A64 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, int32_t ___0_errorCode, const RuntimeMethod* method) ;
// System.Void System.Runtime.InteropServices.SafeHandle::DangerousAddRef(System.Boolean&)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeHandle_DangerousAddRef_m9FA46208A92D8B33059B8E8712F49AE45BB5E922 (SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7* __this, bool* ___0_success, const RuntimeMethod* method) ;
// System.Boolean System.Collections.Generic.List`1<System.Threading.Thread>::Remove(T)
inline bool List_1_Remove_m6BC6C5F209825F38868B72599159757677635886 (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_item, const RuntimeMethod* method)
{
	return ((  bool (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, const RuntimeMethod*))List_1_Remove_m4DFA48F4CEB9169601E75FC28517C5C06EFA5AD7_gshared)(__this, ___0_item, method);
}
// System.Int32 System.Collections.Generic.List`1<System.Threading.Thread>::IndexOf(T)
inline int32_t List_1_IndexOf_m005D5A092A56062F7E23CE7234826D77A54AAFC2 (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_item, const RuntimeMethod* method)
{
	return ((  int32_t (*) (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, const RuntimeMethod*))List_1_IndexOf_m378F61BA812B79DEE58D86FE8AA9F20E3FC7D85F_gshared)(__this, ___0_item, method);
}
// System.Boolean System.Collections.Generic.Dictionary`2<System.Threading.Thread,System.Diagnostics.StackTrace>::Remove(TKey)
inline bool Dictionary_2_Remove_m43BD8FD04F14C57EA9B7F4017447E918FA3B2557 (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* __this, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* ___0_key, const RuntimeMethod* method)
{
	return ((  bool (*) (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED*, Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*, const RuntimeMethod*))Dictionary_2_Remove_m5C7C45E75D951A75843F3F7AADD56ECD64F6BC86_gshared)(__this, ___0_key, method);
}
// System.Void System.Threading.Monitor::Pulse(System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Monitor_Pulse_mA8279943D6C2913ADFAF661E35C4951BDFACE43D (RuntimeObject* ___0_obj, const RuntimeMethod* method) ;
// System.String System.Environment::GetEnvironmentVariable(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* Environment_GetEnvironmentVariable_mAF8CC6EC1CB916789ABB16A02C032F89E508C21A (String_t* ___0_variable, const RuntimeMethod* method) ;
// System.Boolean System.String::op_Equality(System.String,System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool String_op_Equality_m030E1B219352228970A076136E455C4E568C02C1 (String_t* ___0_a, String_t* ___1_b, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncResult::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult__ctor_m2123FFF2C2A56DB840E109348FEEA575B5B691E4 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) ;
// System.Void System.EventArgs::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void EventArgs__ctor_mC6F9412D03203ADEF854117542C8EBF61624C8C3 (EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncEventArgs::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_Dispose_mA5B8D8ADB8521BC9219061C13CB7C07BD02E16DB (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, bool ___0_disposing, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_BytesTransferred(System.Int32)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_BytesTransferred_mAB2EB75BE1BB7B97DBEFCAF3DD42E26BC787D625_inline (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) ;
// System.Int32 System.Threading.Interlocked::Exchange(System.Int32&,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t Interlocked_Exchange_m5810F59A9EF49D8003BA76D95398854254A091DF (int32_t* ___0_location1, int32_t ___1_value, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_LastOperation(System.Net.Sockets.SocketAsyncOperation)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_LastOperation_m7BA8A0B5CB76B4C095BBB9E1124C3F237B51E26A_inline (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) ;
// System.Void System.EventHandler`1<System.Net.Sockets.SocketAsyncEventArgs>::Invoke(System.Object,TEventArgs)
inline void EventHandler_1_Invoke_mF0FFF25301C90C9B2E0769E48203543140B6A01C_inline (EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* __this, RuntimeObject* ___0_sender, SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* ___1_e, const RuntimeMethod* method)
{
	((  void (*) (EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED*, RuntimeObject*, SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC*, const RuntimeMethod*))EventHandler_1_Invoke_mBD72C04FF5A08A2EA93DFD21037CD1C27A48D07A_gshared_inline)(__this, ___0_sender, ___1_e, method);
}
// System.Int32 System.Memory`1<System.Byte>::get_Length()
inline int32_t Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714 (Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036* __this, const RuntimeMethod* method)
{
	return ((  int32_t (*) (Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036*, const RuntimeMethod*))Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_gshared)(__this, method);
}
// System.String SR::Format(System.String,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09 (String_t* ___0_resourceFormat, RuntimeObject* ___1_p1, const RuntimeMethod* method) ;
// System.Void System.ArgumentException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ArgumentException__ctor_m026938A67AF9D36BB7ED27F80425D7194B514465 (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* __this, String_t* ___0_message, const RuntimeMethod* method) ;
// System.IntPtr System.Net.Sockets.Socket::get_Handle()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR intptr_t Socket_get_Handle_mBF42AC5E9403C23BE532F17EE337BF9F150A0256 (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, const RuntimeMethod* method) ;
// System.Void System.IOAsyncResult::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOAsyncResult__ctor_mAA7211B9E6FDF23D0012354FEE350B6B5EB786F4 (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, const RuntimeMethod* method) ;
// System.Void System.IOAsyncResult::Init(System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOAsyncResult_Init_m6580DFCCB89EF7CFF91F7040C62ECC3811A3A98D (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___0_async_callback, RuntimeObject* ___1_async_state, const RuntimeMethod* method) ;
// System.Memory`1<T> System.Memory`1<System.Byte>::op_Implicit(T[])
inline Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C (ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_array, const RuntimeMethod* method)
{
	return ((  Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 (*) (ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*, const RuntimeMethod*))Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C_gshared)(___0_array, method);
}
// System.Void System.IOAsyncResult::.ctor(System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOAsyncResult__ctor_mCDC93C97872E9DAB6A1D9F27C1FA14DDFC6A400D (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___0_async_callback, RuntimeObject* ___1_async_state, const RuntimeMethod* method) ;
// System.Net.Sockets.SocketError System.Net.Sockets.SocketException::get_SocketErrorCode()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketException_get_SocketErrorCode_m84FB2D308F046A24A1355975F3BF689C988224C6 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncResult::Complete()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) ;
// System.Void System.IOAsyncResult::set_IsCompleted(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void IOAsyncResult_set_IsCompleted_m72D907352B8AC32DF47864E4F4EDFADEC825A6F9 (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, bool ___0_value, const RuntimeMethod* method) ;
// System.Boolean System.IOAsyncResult::get_CompletedSynchronously()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR bool IOAsyncResult_get_CompletedSynchronously_m26279BC02AFFE83009A7EEABFFF58EEB2D12CD24_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, const RuntimeMethod* method) ;
// System.AsyncCallback System.IOAsyncResult::get_AsyncCallback()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* IOAsyncResult_get_AsyncCallback_mC6150F5FC2CEE8FDC22EACD9B9BC72544FE801CC_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, const RuntimeMethod* method) ;
// System.Void System.Threading.WaitCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void WaitCallback__ctor_m9730564F9A28ECB72462D05AA92CA9E43DE9B41C (WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) ;
// System.Boolean System.Threading.ThreadPool::UnsafeQueueUserWorkItem(System.Threading.WaitCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_NO_INLINE IL2CPP_METHOD_ATTR bool ThreadPool_UnsafeQueueUserWorkItem_m05CF00E355CE07D625386779E3F3D3D2341FEE86 (WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* ___0_callBack, RuntimeObject* ___1_state, const RuntimeMethod* method) ;
// System.Int32 System.Threading.SemaphoreSlim::Release()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SemaphoreSlim_Release_m4F3CB668D91DEE067A67266C266FEE5AADF56148 (SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* __this, const RuntimeMethod* method) ;
// System.Void System.IOAsyncResult::set_CompletedSynchronously(System.Boolean)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void IOAsyncResult_set_CompletedSynchronously_m29ECF9F6388C1F9A12AF83DF8CD19D9FFB4B4D6D_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, bool ___0_value, const RuntimeMethod* method) ;
// System.Void System.Net.Sockets.SocketAsyncResult/<>c::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__ctor_mAC11B6F52B2C5BA43E6B14CF8338841CAA0ABF26 (U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* __this, const RuntimeMethod* method) ;
// System.Void System.AsyncCallback::Invoke(System.IAsyncResult)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void AsyncCallback_Invoke_mA85DEAE969B1D0FD97E934DC39439E89D7553910_inline (AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* __this, RuntimeObject* ___0_ar, const RuntimeMethod* method) ;
// System.Void System.IO.Stream::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Stream__ctor_mE8B074A0EBEB026FFF14062AB4B8A78E17EFFBF0 (Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* __this, const RuntimeMethod* method) ;
// System.Void System.IO.Stream::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Stream_Dispose_m9B37BD21A57F8F2BD20EE353DE14405700810C5C (Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* __this, bool ___0_disposing, const RuntimeMethod* method) ;
// System.Void System.Net.Security.SslStream::CheckDisposed()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) ;
// System.String Mono.Net.Security.MobileAuthenticatedStream::get_TargetHost()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR String_t* MobileAuthenticatedStream_get_TargetHost_m572DA701CB3CE3FA4006D5745F9A3B9A52739CAB_inline (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* __this, const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoTlsProvider Mono.Security.Interface.MonoTlsProviderFactory::GetProvider()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E* MonoTlsProviderFactory_GetProvider_m8684E3A1AFB043FA00DEC4BCF95F8B288C136936 (const RuntimeMethod* method) ;
// System.Void System.Net.Security.SslStream::.ctor(System.IO.Stream,System.Boolean,System.Net.Security.RemoteCertificateValidationCallback,System.Net.Security.LocalCertificateSelectionCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream__ctor_mB4497903594E8A33C14B9A4C4DAB7B16BA2E9B2C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___2_userCertificateValidationCallback, LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___3_userCertificateSelectionCallback, const RuntimeMethod* method) ;
// System.Void System.Net.Security.AuthenticatedStream::.ctor(System.IO.Stream,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357 (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, const RuntimeMethod* method) ;
// Mono.Net.Security.MobileTlsProvider System.Net.Security.SslStream::GetProvider()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* SslStream_GetProvider_mE1D070C13DB759156659F57AB9DF4D2E397FD85B (const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoTlsSettings Mono.Security.Interface.MonoTlsSettings::CopyDefaultSettings()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* MonoTlsSettings_CopyDefaultSettings_m4B0A3E8B7D106FA7F0D243FB2A0A4B115CD21942 (const RuntimeMethod* method) ;
// System.Void System.Net.Security.SslStream::SetAndVerifyValidationCallback(System.Net.Security.RemoteCertificateValidationCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_SetAndVerifyValidationCallback_m6FA4BE931FA1C7BC5D175B4E1CA2959742FA9C9D (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___0_callback, const RuntimeMethod* method) ;
// System.Void System.Net.Security.SslStream::SetAndVerifySelectionCallback(System.Net.Security.LocalCertificateSelectionCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_SetAndVerifySelectionCallback_m8B82CA433CF92AD113F606ACE7229D442587DA0B (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___0_callback, const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoTlsSettings Mono.Security.Interface.MonoTlsSettings::Clone()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* MonoTlsSettings_Clone_mC4F9A27889ADD0B275018B32BCDA67C30865EA7D (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoRemoteCertificateValidationCallback Mono.Net.Security.Private.CallbackHelpers::PublicToMono(System.Net.Security.RemoteCertificateValidationCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* CallbackHelpers_PublicToMono_m6404DAFAD159E4D91FB5822B4855B6A8F05777EE (RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___0_callback, const RuntimeMethod* method) ;
// System.Void Mono.Security.Interface.MonoTlsSettings::set_RemoteCertificateValidationCallback(Mono.Security.Interface.MonoRemoteCertificateValidationCallback)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void MonoTlsSettings_set_RemoteCertificateValidationCallback_m6CEA8A6E38C85A96C2D26613407C13DD4F965C87_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* ___0_value, const RuntimeMethod* method) ;
// System.Boolean System.Delegate::op_Inequality(System.Delegate,System.Delegate)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool Delegate_op_Inequality_mA9EAADBA0C976289CCD49DC5A4BEDBB060B579E0 (Delegate_t* ___0_d1, Delegate_t* ___1_d2, const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoRemoteCertificateValidationCallback Mono.Security.Interface.MonoTlsSettings::get_RemoteCertificateValidationCallback()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* MonoTlsSettings_get_RemoteCertificateValidationCallback_mE07825B4A75DAE2A4BB5037D504A36311814446C_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, const RuntimeMethod* method) ;
// System.Void System.Net.Security.SslStream/<>c__DisplayClass21_0::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass21_0__ctor_mBE4019B5D6D20FC100DAF52B1172ACF497046370 (U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* __this, const RuntimeMethod* method) ;
// System.Void Mono.Security.Interface.MonoTlsSettings::set_ClientCertificateSelectionCallback(Mono.Security.Interface.MonoLocalCertificateSelectionCallback)
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void MonoTlsSettings_set_ClientCertificateSelectionCallback_mB404DFD0C0475254CC129740A472D6D9615C56FD_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* ___0_value, const RuntimeMethod* method) ;
// System.Void Mono.Security.Interface.MonoLocalCertificateSelectionCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void MonoLocalCertificateSelectionCallback__ctor_mCA81824D698BD5808E501A9AC4DA99758B69D3FC (MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) ;
// Mono.Security.Interface.MonoLocalCertificateSelectionCallback Mono.Security.Interface.MonoTlsSettings::get_ClientCertificateSelectionCallback()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* MonoTlsSettings_get_ClientCertificateSelectionCallback_mCFE63487D867109AD1AF856ECC8BA0996C0AA605_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, const RuntimeMethod* method) ;
// Mono.Net.Security.MobileAuthenticatedStream System.Net.Security.SslStream::get_Impl()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) ;
// System.Void Mono.Net.Security.MobileAuthenticatedStream::AuthenticateAsClient(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Authentication.SslProtocols,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void MobileAuthenticatedStream_AuthenticateAsClient_mEB1237B52A4AB1FF3D7D3E52F61CE78A744973FC (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_clientCertificates, int32_t ___2_enabledSslProtocols, bool ___3_checkCertificateRevocation, const RuntimeMethod* method) ;
// System.Threading.Tasks.Task Mono.Net.Security.MobileAuthenticatedStream::AuthenticateAsClientAsync(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Authentication.SslProtocols,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* MobileAuthenticatedStream_AuthenticateAsClientAsync_mBD8A0E7FFAFDE608063E545EF057304AF2C35EF2 (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_clientCertificates, int32_t ___2_enabledSslProtocols, bool ___3_checkCertificateRevocation, const RuntimeMethod* method) ;
// System.IAsyncResult System.Threading.Tasks.TaskToApm::Begin(System.Threading.Tasks.Task,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* TaskToApm_Begin_mA26D2A4F6EE0582B181AFD83B913916909D02777 (Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* ___0_task, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___1_callback, RuntimeObject* ___2_state, const RuntimeMethod* method) ;
// System.Void System.Threading.Tasks.TaskToApm::End(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TaskToApm_End_mDFB29EEE501409834D464F4C249A535723B7E6ED (RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) ;
// System.Security.Cryptography.X509Certificates.X509Certificate Mono.Net.Security.MobileAuthenticatedStream::get_LocalCertificate()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* MobileAuthenticatedStream_get_LocalCertificate_m59FF8438D988F2CE633B454CE2B54D614E6C50EF (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* __this, const RuntimeMethod* method) ;
// System.IO.Stream System.Net.Security.AuthenticatedStream::get_InnerStream()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7_inline (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, const RuntimeMethod* method) ;
// System.Void System.NotSupportedException::.ctor(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void NotSupportedException__ctor_mE174750CF0247BBB47544FFD71D66BB89630945B (NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A* __this, String_t* ___0_message, const RuntimeMethod* method) ;
// System.Void System.Net.Security.AuthenticatedStream::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void AuthenticatedStream_Dispose_m031F600EE876A06427FB0713D992FF02B9C3320F (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, bool ___0_disposing, const RuntimeMethod* method) ;
// System.Void System.IO.Stream::Dispose()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Stream_Dispose_mCDB42F32A17541CCA6D3A5906827A401570B07A8 (Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* __this, const RuntimeMethod* method) ;
// System.Threading.Tasks.Task`1<System.Int32> System.IO.Stream::ReadAsync(System.Byte[],System.Int32,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_1_t4C228DE57804012969575431CFF12D57C875552D* Stream_ReadAsync_m734E5C146A1217C9E8FEC56ABDBD2AC33F5F8F87 (Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, const RuntimeMethod* method) ;
// TResult System.Threading.Tasks.TaskToApm::End<System.Int32>(System.IAsyncResult)
inline int32_t TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED (RuntimeObject* ___0_asyncResult, const RuntimeMethod* method)
{
	return ((  int32_t (*) (RuntimeObject*, const RuntimeMethod*))TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED_gshared)(___0_asyncResult, method);
}
// System.Threading.Tasks.Task System.IO.Stream::WriteAsync(System.Byte[],System.Int32,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* Stream_WriteAsync_m51D91C94481BB32FE1A876A789C9705F433B133D (Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, const RuntimeMethod* method) ;
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.LocalCertificateSelectionCallback::Invoke(System.Object,System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Cryptography.X509Certificates.X509Certificate,System.String[])
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_inline (LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method) ;
// System.Char System.String::get_Chars(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Il2CppChar String_get_Chars_mC49DF0CD2D3BE7BE97B3AD9C995BE3094F8E36D3 (String_t* __this, int32_t ___0_index, const RuntimeMethod* method) ;
// System.Int32 System.String::get_Length()
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t String_get_Length_m42625D67623FA5CC7A44D47425CE86FB946542D2_inline (String_t* __this, const RuntimeMethod* method) ;
// System.Void System.PlatformNotSupportedException::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void PlatformNotSupportedException__ctor_mD5DBE8E9A6FF4B75EF02671029C6D67A51EAFBD1 (PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A* __this, const RuntimeMethod* method) ;
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass240_0::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass240_0__ctor_mD80242D91D46B0815EF88CBCBBB2D45EC20CC5FC (U3CU3Ec__DisplayClass240_0_t99833891E615E48FC65509B7FAFD7A266C1515F3* __this, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass240_0::<BeginConnect>b__0(System.Threading.Tasks.Task`1<System.Net.IPAddress[]>)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass240_0_U3CBeginConnectU3Eb__0_m9866140191D83FDC7415CF75C68ED71440846A9C (U3CU3Ec__DisplayClass240_0_t99833891E615E48FC65509B7FAFD7A266C1515F3* __this, Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* ___0_t, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Task_1_get_Result_m0F2300B3BCA792B7EFFAF002F88E53F5DFC64394_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* L_0 = ___0_t;
		NullCheck(L_0);
		bool L_1;
		L_1 = Task_get_IsFaulted_mC0AD3EA4EAF3B47C1F5FE9624541F0A00B9426D9(L_0, NULL);
		if (!L_1)
		{
			goto IL_001f;
		}
	}
	{
		SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* L_2 = __this->___sockares_0;
		Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* L_3 = ___0_t;
		NullCheck(L_3);
		AggregateException_t51B6205846DFB356B94452702201DD239D44422F* L_4;
		L_4 = Task_get_Exception_m11317CABA220E351C84B3E94B8A98F1565F42B45(L_3, NULL);
		NullCheck(L_4);
		Exception_t* L_5;
		L_5 = Exception_get_InnerException_m0C1BDB339C786BA4DA7D2C1AD214571CFBBB1410_inline(L_4, NULL);
		NullCheck(L_2);
		SocketAsyncResult_Complete_m49FA96276F9EBB207D8194440AE89300DDDAC513(L_2, L_5, NULL);
		return;
	}

IL_001f:
	{
		Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* L_6 = ___0_t;
		NullCheck(L_6);
		bool L_7;
		L_7 = Task_get_IsCanceled_m96A8D3F85158A9CB3AEA50A00A55BE4E0F0E21FA(L_6, NULL);
		if (!L_7)
		{
			goto IL_0038;
		}
	}
	{
		SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* L_8 = __this->___sockares_0;
		OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662* L_9 = (OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662*)il2cpp_codegen_object_new(OperationCanceledException_tC97D0B4532C15E6F0E9F9375091C9ECCA438D662_il2cpp_TypeInfo_var);
		NullCheck(L_9);
		OperationCanceledException__ctor_m2F34C3B8AEE2AA6C7EB2BB77AE5E0289101293E4(L_9, NULL);
		NullCheck(L_8);
		SocketAsyncResult_Complete_m49FA96276F9EBB207D8194440AE89300DDDAC513(L_8, L_9, NULL);
		return;
	}

IL_0038:
	{
		SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* L_10 = __this->___sockares_0;
		Task_1_tAC26C3CB300EB1DB608D4ADE41BFFBD5924E4670* L_11 = ___0_t;
		NullCheck(L_11);
		IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* L_12;
		L_12 = Task_1_get_Result_m0F2300B3BCA792B7EFFAF002F88E53F5DFC64394(L_11, Task_1_get_Result_m0F2300B3BCA792B7EFFAF002F88E53F5DFC64394_RuntimeMethod_var);
		NullCheck(L_10);
		L_10->___Addresses_14 = L_12;
		Il2CppCodeGenWriteBarrier((void**)(&L_10->___Addresses_14), (void*)L_12);
		SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* L_13 = __this->___sockares_0;
		il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		bool L_14;
		L_14 = Socket_BeginMConnect_mA33FD740EBFB2B4394392CCEBAAB3DFB630214C9(L_13, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass298_0::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass298_0__ctor_mC8FFE6C7BDBE2A999CF8792C81BD55DF56BF5004 (U3CU3Ec__DisplayClass298_0_t508378F2011A6BCD457AA7612AAD1D4AF537825E* __this, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass298_0::<BeginSendCallback>b__0(System.IOAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass298_0_U3CBeginSendCallbackU3Eb__0_m2A2487EF9F99DDE2CA0FD582FC353D04BA4FC843 (U3CU3Ec__DisplayClass298_0_t508378F2011A6BCD457AA7612AAD1D4AF537825E* __this, IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* ___0_s, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* L_0 = ___0_s;
		int32_t L_1 = __this->___sent_so_far_0;
		il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		Socket_BeginSendCallback_m4B6E09619A84BCECCA322F4B20C819428C4861D9(((SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340*)CastclassSealed((RuntimeObject*)L_0, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var)), L_1, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass355_0::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass355_0__ctor_mA97F35DC1143434E4ED8C3A6BFDD83162EA26BDE (U3CU3Ec__DisplayClass355_0_tE1E6F6152220887E65219B2C9CA1073F6F5D8819* __this, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.Socket/<>c__DisplayClass355_0::<QueueIOSelectorJob>b__0(System.Threading.Tasks.Task)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass355_0_U3CQueueIOSelectorJobU3Eb__0_m7B1B6117A026B2295671986FBAFF0F01C11F94AB (U3CU3Ec__DisplayClass355_0_tE1E6F6152220887E65219B2C9CA1073F6F5D8819* __this, Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* ___0_t, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___U3CU3E4__this_0;
		NullCheck(L_0);
		bool L_1;
		L_1 = Socket_get_CleanedUp_m8F6EDAE37C56CBC282D3EAF9D93B5726EEC85988(L_0, NULL);
		if (!L_1)
		{
			goto IL_0019;
		}
	}
	{
		IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31* L_2 = __this->___job_1;
		NullCheck(L_2);
		IOSelectorJob_MarkDisposed_mBCCE13D70B97BB7AE46D976AE207DF642C4531F0(L_2, NULL);
		return;
	}

IL_0019:
	{
		intptr_t L_3 = __this->___handle_2;
		IOSelectorJob_t988E3C2831A98169EFDB178A08E8548A4E90DD31* L_4 = __this->___job_1;
		IOSelector_Add_mC5550BDC7E4702420FACE7E9E0F55361E35DAAC2(L_3, L_4, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Int32 System.Net.Sockets.SocketException::WSAGetLastError_icall()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketException_WSAGetLastError_icall_mD9AF5BE627EB9CB0032D7F681CB839141CB99E98 (const RuntimeMethod* method) 
{
	typedef int32_t (*SocketException_WSAGetLastError_icall_mD9AF5BE627EB9CB0032D7F681CB839141CB99E98_ftn) ();
	using namespace il2cpp::icalls;
	return ((SocketException_WSAGetLastError_icall_mD9AF5BE627EB9CB0032D7F681CB839141CB99E98_ftn)System::System::Net::Sockets::SocketException::WSAGetLastError_icall) ();
}
// System.Void System.Net.Sockets.SocketException::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_mDDA59569C2C05AB013AF9670BD6EC070086239AB (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0;
		L_0 = SocketException_WSAGetLastError_icall_mD9AF5BE627EB9CB0032D7F681CB839141CB99E98(NULL);
		Win32Exception__ctor_mFC38F1C99031D227C6BB3CA07099866D39994281(__this, L_0, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketException::.ctor(System.Int32,System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_m0F0444CE9F1E2BF7AF1D0A89DC29FD973AE743AC (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, int32_t ___0_error, String_t* ___1_message, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_error;
		String_t* L_1 = ___1_message;
		Win32Exception__ctor_mB0380F996944EDFCF9CACC7F28A18874F19B4FD4(__this, L_0, L_1, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketException::.ctor(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_mC09AA11941DA13359416179634B151A3FE2B9A64 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, int32_t ___0_errorCode, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_errorCode;
		Win32Exception__ctor_mFC38F1C99031D227C6BB3CA07099866D39994281(__this, L_0, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketException::.ctor(System.Net.Sockets.SocketError)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_m5E9DDE94C8009DE11B8CE2EF1611A5CE8A900AC3 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, int32_t ___0_socketError, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_socketError;
		Win32Exception__ctor_mFC38F1C99031D227C6BB3CA07099866D39994281(__this, L_0, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketException::.ctor(System.Runtime.Serialization.SerializationInfo,System.Runtime.Serialization.StreamingContext)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketException__ctor_m830447309D5813D7FDE050DB3A075BDCE787B046 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, SerializationInfo_t3C47F63E24BEB9FCE2DC6309E027F238DC5C5E37* ___0_serializationInfo, StreamingContext_t56760522A751890146EE45F82F866B55B7E33677 ___1_streamingContext, const RuntimeMethod* method) 
{
	{
		SerializationInfo_t3C47F63E24BEB9FCE2DC6309E027F238DC5C5E37* L_0 = ___0_serializationInfo;
		StreamingContext_t56760522A751890146EE45F82F866B55B7E33677 L_1 = ___1_streamingContext;
		Win32Exception__ctor_m1B3DF3F51BBF050C18F933B36027A8A4902668C4(__this, L_0, L_1, NULL);
		return;
	}
}
// System.String System.Net.Sockets.SocketException::get_Message()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SocketException_get_Message_mA34986089059D346C42DB47FB8BD331856BE0492 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&_stringLiteral2386E77CF610F786B06A91AF2C1B3FD2282D2745);
		s_Il2CppMethodInitialized = true;
	}
	{
		EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* L_0 = __this->___m_EndPoint_19;
		if (L_0)
		{
			goto IL_000f;
		}
	}
	{
		String_t* L_1;
		L_1 = Exception_get_Message_mCFE0D38FDF9001405299AAE43BBE2A9AD806C441(__this, NULL);
		return L_1;
	}

IL_000f:
	{
		String_t* L_2;
		L_2 = Exception_get_Message_mCFE0D38FDF9001405299AAE43BBE2A9AD806C441(__this, NULL);
		EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* L_3 = __this->___m_EndPoint_19;
		NullCheck(L_3);
		String_t* L_4;
		L_4 = VirtualFuncInvoker0< String_t* >::Invoke(3 /* System.String System.Object::ToString() */, L_3);
		String_t* L_5;
		L_5 = String_Concat_m8855A6DE10F84DA7F4EC113CADDB59873A25573B(L_2, _stringLiteral2386E77CF610F786B06A91AF2C1B3FD2282D2745, L_4, NULL);
		return L_5;
	}
}
// System.Net.Sockets.SocketError System.Net.Sockets.SocketException::get_SocketErrorCode()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketException_get_SocketErrorCode_m84FB2D308F046A24A1355975F3BF689C988224C6 (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0;
		L_0 = Win32Exception_get_NativeErrorCode_m33C1D176D9178C36DA32F8FEC251B923EE53CE5E_inline(__this, NULL);
		return (int32_t)(L_0);
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.LingerOption::.ctor(System.Boolean,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void LingerOption__ctor_m0BECD7DEA12AC52917D065B27818677F4DE7CEBC (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, bool ___0_enable, int32_t ___1_seconds, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		bool L_0 = ___0_enable;
		LingerOption_set_Enabled_mEDF2FED046EA58F9C23BEF3BF1184C3A890419DB_inline(__this, L_0, NULL);
		int32_t L_1 = ___1_seconds;
		LingerOption_set_LingerTime_mDC6B06F96C04C5A69E19912EBB37DC5BDE5A6DFB_inline(__this, L_1, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.LingerOption::set_Enabled(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void LingerOption_set_Enabled_mEDF2FED046EA58F9C23BEF3BF1184C3A890419DB (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, bool ___0_value, const RuntimeMethod* method) 
{
	{
		bool L_0 = ___0_value;
		__this->___enabled_0 = L_0;
		return;
	}
}
// System.Void System.Net.Sockets.LingerOption::set_LingerTime(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void LingerOption_set_LingerTime_mDC6B06F96C04C5A69E19912EBB37DC5BDE5A6DFB (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___lingerTime_1 = L_0;
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.TcpClient::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient__ctor_m709FAC4D5B9DDB279F9B97D90D3164B92E1F2C87 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	{
		TcpClient__ctor_m94DC92A387808F584010C708C071197B52B48B55(__this, 2, NULL);
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		bool L_1;
		L_1 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::.ctor(System.Net.Sockets.AddressFamily)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient__ctor_m94DC92A387808F584010C708C071197B52B48B55 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, int32_t ___0_family, const RuntimeMethod* method) 
{
	{
		__this->___m_Family_3 = 2;
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		int32_t L_1 = ___0_family;
		if ((((int32_t)L_1) == ((int32_t)2)))
		{
			goto IL_003f;
		}
	}
	{
		int32_t L_2 = ___0_family;
		if ((((int32_t)L_2) == ((int32_t)((int32_t)23))))
		{
			goto IL_003f;
		}
	}
	{
		ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* L_3 = (ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918*)(ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918*)SZArrayNew(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918_il2cpp_TypeInfo_var)), (uint32_t)1);
		ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* L_4 = L_3;
		NullCheck(L_4);
		ArrayElementTypeCheck (L_4, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralD6B94B0798130EA7E48D45E575293E70EDAC7151)));
		(L_4)->SetAt(static_cast<il2cpp_array_size_t>(0), (RuntimeObject*)((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralD6B94B0798130EA7E48D45E575293E70EDAC7151)));
		String_t* L_5;
		L_5 = SR_GetString_m56640423E54C5F10BFDEF27B7E692A33745EB696(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral8F37F8E27E5857460347BD55FFBFB98FCC58AC10)), L_4, NULL);
		ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* L_6 = (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var)));
		NullCheck(L_6);
		ArgumentException__ctor_m8F9D40CE19D19B698A70F9A258640EB52DB39B62(L_6, L_5, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral319D1EB2B79ACDA1B65E4BA4BA666745E69FEECF)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_6, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient__ctor_m94DC92A387808F584010C708C071197B52B48B55_RuntimeMethod_var)));
	}

IL_003f:
	{
		int32_t L_7 = ___0_family;
		__this->___m_Family_3 = L_7;
		TcpClient_initialize_mE26B7664FC1F1DB4D963065A7E47848798697114(__this, NULL);
		bool L_8;
		L_8 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.Net.Sockets.Socket System.Net.Sockets.TcpClient::get_Client()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___m_ClientSocket_0;
		return L_0;
	}
}
// System.Void System.Net.Sockets.TcpClient::set_Client(System.Net.Sockets.Socket)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_set_Client_m145E6A35F13B52AF10DE077FD19DE79A622E5F5C (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_value, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_value;
		__this->___m_ClientSocket_0 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___m_ClientSocket_0), (void*)L_0);
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::Connect(System.String,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, String_t* ___0_hostname, int32_t ___1_port, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ValidationHelper_tA6AE681FCDFA9BE8E3EF31C28A624AADF973A25C_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* V_0 = NULL;
	Exception_t* V_1 = NULL;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* V_2 = NULL;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* V_3 = NULL;
	IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* V_4 = NULL;
	int32_t V_5 = 0;
	IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* V_6 = NULL;
	Exception_t* V_7 = NULL;
	Exception_t* V_8 = NULL;
	il2cpp::utils::ExceptionSupportStack<RuntimeObject*, 2> __active_exceptions;
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		bool L_1 = __this->___m_CleanedUp_4;
		if (!L_1)
		{
			goto IL_001f;
		}
	}
	{
		Type_t* L_2;
		L_2 = Object_GetType_mE10A8FC1E57F3DF29972CCBC026C2DC3942263B3(__this, NULL);
		NullCheck(L_2);
		String_t* L_3;
		L_3 = VirtualFuncInvoker0< String_t* >::Invoke(26 /* System.String System.Type::get_FullName() */, L_2);
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_4 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_4, L_3, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
	}

IL_001f:
	{
		String_t* L_5 = ___0_hostname;
		if (L_5)
		{
			goto IL_002d;
		}
	}
	{
		ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129* L_6 = (ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129_il2cpp_TypeInfo_var)));
		NullCheck(L_6);
		ArgumentNullException__ctor_m444AE141157E333844FC1A9500224C2F9FD24F4B(L_6, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral143956766707E0543589DAB7DBE2742B2B993D2E)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_6, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
	}

IL_002d:
	{
		int32_t L_7 = ___1_port;
		il2cpp_codegen_runtime_class_init_inline(ValidationHelper_tA6AE681FCDFA9BE8E3EF31C28A624AADF973A25C_il2cpp_TypeInfo_var);
		bool L_8;
		L_8 = ValidationHelper_ValidateTcpPort_m307286F0DF94E8741F0359186B58464E422B1B4A(L_7, NULL);
		if (L_8)
		{
			goto IL_0040;
		}
	}
	{
		ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F* L_9 = (ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentOutOfRangeException_tEA2822DAF62B10EEED00E0E3A341D4BAF78CF85F_il2cpp_TypeInfo_var)));
		NullCheck(L_9);
		ArgumentOutOfRangeException__ctor_mBC1D5DEEA1BA41DE77228CB27D6BAFEB6DCCBF4A(L_9, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral670E6E5CD786FBA46F50395F2FE281B1DF2C9985)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_9, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
	}

IL_0040:
	{
		bool L_10 = __this->___m_Active_1;
		if (!L_10)
		{
			goto IL_0053;
		}
	}
	{
		SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_11 = (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var)));
		NullCheck(L_11);
		SocketException__ctor_m5E9DDE94C8009DE11B8CE2EF1611A5CE8A900AC3(L_11, ((int32_t)10056), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_11, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
	}

IL_0053:
	{
		String_t* L_12 = ___0_hostname;
		IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* L_13;
		L_13 = Dns_GetHostAddresses_mB44EA8DE1F3C53F533578949FED6BB98B0F1F212(L_12, NULL);
		V_0 = L_13;
		V_1 = (Exception_t*)NULL;
		V_2 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL;
		V_3 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL;
	}
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0180:
			{// begin finally (depth: 1)
				{
					bool L_14 = __this->___m_Active_1;
					if (L_14)
					{
						goto IL_01aa;
					}
				}
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_15 = V_2;
					if (!L_15)
					{
						goto IL_0191;
					}
				}
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_16 = V_2;
					NullCheck(L_16);
					Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4(L_16, NULL);
				}

IL_0191:
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_17 = V_3;
					if (!L_17)
					{
						goto IL_019a;
					}
				}
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_18 = V_3;
					NullCheck(L_18);
					Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4(L_18, NULL);
				}

IL_019a:
				{
					Exception_t* L_19 = V_1;
					if (!L_19)
					{
						goto IL_019f;
					}
				}
				{
					Exception_t* L_20 = V_1;
					IL2CPP_RAISE_MANAGED_EXCEPTION(L_20, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
				}

IL_019f:
				{
					SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_21 = (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var)));
					NullCheck(L_21);
					SocketException__ctor_m5E9DDE94C8009DE11B8CE2EF1611A5CE8A900AC3(L_21, ((int32_t)10057), NULL);
					IL2CPP_RAISE_MANAGED_EXCEPTION(L_21, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_mED87D733E342BEAF8DCB947F612FD25C505753BB_RuntimeMethod_var)));
				}

IL_01aa:
				{
					return;
				}
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			try
			{// begin try (depth: 2)
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_22 = __this->___m_ClientSocket_0;
					if (L_22)
					{
						goto IL_0089_2;
					}
				}
				{
					il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
					bool L_23;
					L_23 = Socket_get_OSSupportsIPv4_m17A77A027E3FE4EBDB927A2796551F55C0594889(NULL);
					if (!L_23)
					{
						goto IL_0078_2;
					}
				}
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_24 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)il2cpp_codegen_object_new(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
					NullCheck(L_24);
					Socket__ctor_m35F1F4B4872E251867DA16460F06E903A30E4595(L_24, 2, 1, 6, NULL);
					V_3 = L_24;
				}

IL_0078_2:
				{
					il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
					bool L_25;
					L_25 = Socket_get_OSSupportsIPv6_mDF10C5F623BBCBB218599E9C4B1AE3E7EE2AB2CB(NULL);
					if (!L_25)
					{
						goto IL_0089_2;
					}
				}
				{
					Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_26 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)il2cpp_codegen_object_new(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
					NullCheck(L_26);
					Socket__ctor_m35F1F4B4872E251867DA16460F06E903A30E4595(L_26, ((int32_t)23), 1, 6, NULL);
					V_2 = L_26;
				}

IL_0089_2:
				{
					IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* L_27 = V_0;
					V_4 = L_27;
					V_5 = 0;
					goto IL_014f_2;
				}

IL_0094_2:
				{
					IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* L_28 = V_4;
					int32_t L_29 = V_5;
					NullCheck(L_28);
					int32_t L_30 = L_29;
					IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_31 = (L_28)->GetAt(static_cast<il2cpp_array_size_t>(L_30));
					V_6 = L_31;
				}
				try
				{// begin try (depth: 3)
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_32 = __this->___m_ClientSocket_0;
						if (L_32)
						{
							goto IL_00fd_3;
						}
					}
					{
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_33 = V_6;
						NullCheck(L_33);
						int32_t L_34;
						L_34 = IPAddress_get_AddressFamily_m1CE4BCCE499BD70B22F9E37B3F266F9306A98C21(L_33, NULL);
						if ((!(((uint32_t)L_34) == ((uint32_t)2))))
						{
							goto IL_00cb_3;
						}
					}
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_35 = V_3;
						if (!L_35)
						{
							goto IL_00cb_3;
						}
					}
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_36 = V_3;
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_37 = V_6;
						int32_t L_38 = ___1_port;
						NullCheck(L_36);
						Socket_Connect_mEE1E8320C097486AFA5432284C1B7FFB0DDC35D6(L_36, L_37, L_38, NULL);
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_39 = V_3;
						__this->___m_ClientSocket_0 = L_39;
						Il2CppCodeGenWriteBarrier((void**)(&__this->___m_ClientSocket_0), (void*)L_39);
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_40 = V_2;
						if (!L_40)
						{
							goto IL_00e7_3;
						}
					}
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_41 = V_2;
						NullCheck(L_41);
						Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4(L_41, NULL);
						goto IL_00e7_3;
					}

IL_00cb_3:
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_42 = V_2;
						if (!L_42)
						{
							goto IL_00e7_3;
						}
					}
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_43 = V_2;
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_44 = V_6;
						int32_t L_45 = ___1_port;
						NullCheck(L_43);
						Socket_Connect_mEE1E8320C097486AFA5432284C1B7FFB0DDC35D6(L_43, L_44, L_45, NULL);
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_46 = V_2;
						__this->___m_ClientSocket_0 = L_46;
						Il2CppCodeGenWriteBarrier((void**)(&__this->___m_ClientSocket_0), (void*)L_46);
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_47 = V_3;
						if (!L_47)
						{
							goto IL_00e7_3;
						}
					}
					{
						Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_48 = V_3;
						NullCheck(L_48);
						Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4(L_48, NULL);
					}

IL_00e7_3:
					{
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_49 = V_6;
						NullCheck(L_49);
						int32_t L_50;
						L_50 = IPAddress_get_AddressFamily_m1CE4BCCE499BD70B22F9E37B3F266F9306A98C21(L_49, NULL);
						__this->___m_Family_3 = L_50;
						__this->___m_Active_1 = (bool)1;
						goto IL_015a_2;
					}

IL_00fd_3:
					{
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_51 = V_6;
						NullCheck(L_51);
						int32_t L_52;
						L_52 = IPAddress_get_AddressFamily_m1CE4BCCE499BD70B22F9E37B3F266F9306A98C21(L_51, NULL);
						int32_t L_53 = __this->___m_Family_3;
						if ((!(((uint32_t)L_52) == ((uint32_t)L_53))))
						{
							goto IL_0123_3;
						}
					}
					{
						IPAddress_t2F4486449B0D73FF2D3B36A9FE5E9C3F63116484* L_54 = V_6;
						int32_t L_55 = ___1_port;
						IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* L_56 = (IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB*)il2cpp_codegen_object_new(IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB_il2cpp_TypeInfo_var);
						NullCheck(L_56);
						IPEndPoint__ctor_m902C98F9E3F36B20B3C2E030AA91B62E2BC7A85A(L_56, L_54, L_55, NULL);
						TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE(__this, L_56, NULL);
						__this->___m_Active_1 = (bool)1;
						goto IL_015a_2;
					}

IL_0123_3:
					{
						goto IL_0149_2;
					}
				}// end try (depth: 3)
				catch(Il2CppExceptionWrapper& e)
				{
					if(il2cpp_codegen_class_is_assignable_from (((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&Exception_t_il2cpp_TypeInfo_var)), il2cpp_codegen_object_class(e.ex)))
					{
						IL2CPP_PUSH_ACTIVE_EXCEPTION(e.ex);
						goto CATCH_0125_2;
					}
					throw e;
				}

CATCH_0125_2:
				{// begin catch(System.Exception)
					{
						Exception_t* L_57 = ((Exception_t*)IL2CPP_GET_ACTIVE_EXCEPTION(Exception_t*));;
						V_7 = L_57;
						Exception_t* L_58 = V_7;
						if (((ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4*)IsInstSealed((RuntimeObject*)L_58, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4_il2cpp_TypeInfo_var)))))
						{
							goto IL_0142_2;
						}
					}
					{
						Exception_t* L_59 = V_7;
						if (((StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9*)IsInstSealed((RuntimeObject*)L_59, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9_il2cpp_TypeInfo_var)))))
						{
							goto IL_0142_2;
						}
					}
					{
						Exception_t* L_60 = V_7;
						if (!((OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F*)IsInstClass((RuntimeObject*)L_60, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F_il2cpp_TypeInfo_var)))))
						{
							goto IL_0144_2;
						}
					}

IL_0142_2:
					{
						IL2CPP_RETHROW_MANAGED_EXCEPTION(IL2CPP_POP_ACTIVE_EXCEPTION(Exception_t*));
					}

IL_0144_2:
					{
						Exception_t* L_61 = V_7;
						V_1 = L_61;
						IL2CPP_POP_ACTIVE_EXCEPTION(Exception_t*);
						goto IL_0149_2;
					}
				}// end catch (depth: 3)

IL_0149_2:
				{
					int32_t L_62 = V_5;
					V_5 = ((int32_t)il2cpp_codegen_add(L_62, 1));
				}

IL_014f_2:
				{
					int32_t L_63 = V_5;
					IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D* L_64 = V_4;
					NullCheck(L_64);
					if ((((int32_t)L_63) < ((int32_t)((int32_t)(((RuntimeArray*)L_64)->max_length)))))
					{
						goto IL_0094_2;
					}
				}

IL_015a_2:
				{
					goto IL_01ab;
				}
			}// end try (depth: 2)
			catch(Il2CppExceptionWrapper& e)
			{
				if(il2cpp_codegen_class_is_assignable_from (((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&Exception_t_il2cpp_TypeInfo_var)), il2cpp_codegen_object_class(e.ex)))
				{
					IL2CPP_PUSH_ACTIVE_EXCEPTION(e.ex);
					goto CATCH_015c_1;
				}
				throw e;
			}

CATCH_015c_1:
			{// begin catch(System.Exception)
				{
					Exception_t* L_65 = ((Exception_t*)IL2CPP_GET_ACTIVE_EXCEPTION(Exception_t*));;
					V_8 = L_65;
					Exception_t* L_66 = V_8;
					if (((ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4*)IsInstSealed((RuntimeObject*)L_66, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ThreadAbortException_tCA1833E5D49782387EDF3BDCBDB90597B273F3C4_il2cpp_TypeInfo_var)))))
					{
						goto IL_0179_1;
					}
				}
				{
					Exception_t* L_67 = V_8;
					if (((StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9*)IsInstSealed((RuntimeObject*)L_67, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&StackOverflowException_t938C753F2BB15F526FD564CCF52061BBE5BC97C9_il2cpp_TypeInfo_var)))))
					{
						goto IL_0179_1;
					}
				}
				{
					Exception_t* L_68 = V_8;
					if (!((OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F*)IsInstClass((RuntimeObject*)L_68, ((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&OutOfMemoryException_tE6DC2F937EC4A8699271D5151C4DF83BDE99EE7F_il2cpp_TypeInfo_var)))))
					{
						goto IL_017b_1;
					}
				}

IL_0179_1:
				{
					IL2CPP_RETHROW_MANAGED_EXCEPTION(IL2CPP_POP_ACTIVE_EXCEPTION(Exception_t*));
				}

IL_017b_1:
				{
					Exception_t* L_69 = V_8;
					V_1 = L_69;
					IL2CPP_POP_ACTIVE_EXCEPTION(Exception_t*);
					goto IL_01ab;
				}
			}// end catch (depth: 2)
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_01ab:
	{
		bool L_70;
		L_70 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::Connect(System.Net.IPEndPoint)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* ___0_remoteEP, const RuntimeMethod* method) 
{
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		bool L_1 = __this->___m_CleanedUp_4;
		if (!L_1)
		{
			goto IL_001f;
		}
	}
	{
		Type_t* L_2;
		L_2 = Object_GetType_mE10A8FC1E57F3DF29972CCBC026C2DC3942263B3(__this, NULL);
		NullCheck(L_2);
		String_t* L_3;
		L_3 = VirtualFuncInvoker0< String_t* >::Invoke(26 /* System.String System.Type::get_FullName() */, L_2);
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_4 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_4, L_3, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE_RuntimeMethod_var)));
	}

IL_001f:
	{
		IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* L_5 = ___0_remoteEP;
		if (L_5)
		{
			goto IL_002d;
		}
	}
	{
		ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129* L_6 = (ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129_il2cpp_TypeInfo_var)));
		NullCheck(L_6);
		ArgumentNullException__ctor_m444AE141157E333844FC1A9500224C2F9FD24F4B(L_6, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral6C52C8D495F05CFD54188F5CD5E5C6E98067B112)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_6, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_Connect_m45A6127922B8ECB5CFBC879DE6A3BAB7BEC8ABBE_RuntimeMethod_var)));
	}

IL_002d:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_7;
		L_7 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		IPEndPoint_t2F09CBA7A808B67724B4E2954EEDC46D910F4ECB* L_8 = ___0_remoteEP;
		NullCheck(L_7);
		Socket_Connect_m874A510F986F4D91446B2F53688D0BCFFE0A6BE1(L_7, L_8, NULL);
		__this->___m_Active_1 = (bool)1;
		bool L_9;
		L_9 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.IAsyncResult System.Net.Sockets.TcpClient::BeginConnect(System.String,System.Int32,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* TcpClient_BeginConnect_mBE3D6DA720BCE275EABB903E75D8CD19BB52083B (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, String_t* ___0_host, int32_t ___1_port, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___2_requestCallback, RuntimeObject* ___3_state, const RuntimeMethod* method) 
{
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_1;
		L_1 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		String_t* L_2 = ___0_host;
		int32_t L_3 = ___1_port;
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_4 = ___2_requestCallback;
		RuntimeObject* L_5 = ___3_state;
		NullCheck(L_1);
		RuntimeObject* L_6;
		L_6 = Socket_BeginConnect_mC246F9195AE1804CE282BFCA75B1C14E998244FD(L_1, L_2, L_3, L_4, L_5, NULL);
		bool L_7;
		L_7 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return L_6;
	}
}
// System.Void System.Net.Sockets.TcpClient::EndConnect(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_EndConnect_m7A72422D31A93819CAAAA16FC663F729D2B56300 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) 
{
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_1;
		L_1 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		RuntimeObject* L_2 = ___0_asyncResult;
		NullCheck(L_1);
		Socket_EndConnect_m10F5B4F43A68F96891DB3AEF493AB160CD8104D6(L_1, L_2, NULL);
		__this->___m_Active_1 = (bool)1;
		bool L_3;
		L_3 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.Threading.Tasks.Task System.Net.Sockets.TcpClient::ConnectAsync(System.String,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* TcpClient_ConnectAsync_m5CA2391FA5091EC441C8E94068F5EE61863659BF (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, String_t* ___0_host, int32_t ___1_port, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&TaskFactory_FromAsync_TisString_t_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mD129D3A417906F121881179FDB395C2AC54E94BC_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&TcpClient_BeginConnect_mBE3D6DA720BCE275EABB903E75D8CD19BB52083B_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&TcpClient_EndConnect_m7A72422D31A93819CAAAA16FC663F729D2B56300_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_runtime_class_init_inline(Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var);
		TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* L_0;
		L_0 = Task_get_Factory_m8272CF9106A72E5F0B8E8C6CEE025D748FBD6D11_inline(NULL);
		Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818* L_1 = (Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818*)il2cpp_codegen_object_new(Func_5_t805619620EC28CDACD9F4CC1037A05B0A4B80818_il2cpp_TypeInfo_var);
		NullCheck(L_1);
		Func_5__ctor_m6B3982ABCDD8853956F2D12232F37EAEAC86D18E(L_1, __this, (intptr_t)((void*)TcpClient_BeginConnect_mBE3D6DA720BCE275EABB903E75D8CD19BB52083B_RuntimeMethod_var), NULL);
		Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780* L_2 = (Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780*)il2cpp_codegen_object_new(Action_1_t8AE9CB3E5DC764211C9FEAC59DE7B65AD1444780_il2cpp_TypeInfo_var);
		NullCheck(L_2);
		Action_1__ctor_m0164A28339B1089B156891E229EE08B0D2184DEA(L_2, __this, (intptr_t)((void*)TcpClient_EndConnect_m7A72422D31A93819CAAAA16FC663F729D2B56300_RuntimeMethod_var), NULL);
		String_t* L_3 = ___0_host;
		int32_t L_4 = ___1_port;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_5;
		L_5 = TaskFactory_FromAsync_TisString_t_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mD129D3A417906F121881179FDB395C2AC54E94BC(L_0, L_1, L_2, L_3, L_4, NULL, TaskFactory_FromAsync_TisString_t_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mD129D3A417906F121881179FDB395C2AC54E94BC_RuntimeMethod_var);
		return L_5;
	}
}
// System.Net.Sockets.NetworkStream System.Net.Sockets.TcpClient::GetStream()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* TcpClient_GetStream_mDD54336B17D1267BD593E0A1EB9EDF3E9506AEBA (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		bool L_1 = __this->___m_CleanedUp_4;
		if (!L_1)
		{
			goto IL_001f;
		}
	}
	{
		Type_t* L_2;
		L_2 = Object_GetType_mE10A8FC1E57F3DF29972CCBC026C2DC3942263B3(__this, NULL);
		NullCheck(L_2);
		String_t* L_3;
		L_3 = VirtualFuncInvoker0< String_t* >::Invoke(26 /* System.String System.Type::get_FullName() */, L_2);
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_4 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_4, L_3, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_GetStream_mDD54336B17D1267BD593E0A1EB9EDF3E9506AEBA_RuntimeMethod_var)));
	}

IL_001f:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_5;
		L_5 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		NullCheck(L_5);
		bool L_6;
		L_6 = Socket_get_Connected_m6E61ADEB0124FB8D46AC1EDFDC268D3C9F7869C5_inline(L_5, NULL);
		if (L_6)
		{
			goto IL_003c;
		}
	}
	{
		String_t* L_7;
		L_7 = SR_GetString_m2F0EFCFA5FE3DB6DE89C5823284302D691355AA6(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralC346DF19BCA13733A62F157B7D32A1ADA5023CC6)), NULL);
		InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB* L_8 = (InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB_il2cpp_TypeInfo_var)));
		NullCheck(L_8);
		InvalidOperationException__ctor_mE4CB6F4712AB6D99A2358FBAE2E052B3EE976162(L_8, L_7, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_8, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&TcpClient_GetStream_mDD54336B17D1267BD593E0A1EB9EDF3E9506AEBA_RuntimeMethod_var)));
	}

IL_003c:
	{
		NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* L_9 = __this->___m_DataStream_2;
		if (L_9)
		{
			goto IL_0056;
		}
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_10;
		L_10 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* L_11 = (NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0*)il2cpp_codegen_object_new(NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0_il2cpp_TypeInfo_var);
		NullCheck(L_11);
		NetworkStream__ctor_mA44C3DB90F5B64F230CC2176DC7E722F4077A9DE(L_11, L_10, (bool)1, NULL);
		__this->___m_DataStream_2 = L_11;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___m_DataStream_2), (void*)L_11);
	}

IL_0056:
	{
		bool L_12;
		L_12 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* L_13 = __this->___m_DataStream_2;
		return L_13;
	}
}
// System.Void System.Net.Sockets.TcpClient::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Dispose_mE2C0688BB1E1E9E25CC9E7CA016AE27547DAEC82 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, bool ___0_disposing, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&GC_t920F9CF6EBB7C787E5010A4352E1B587F356DC58_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&IDisposable_t030E0496B4E0E4E4F086825007979AF51F7248C5_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	RuntimeObject* V_0 = NULL;
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* V_1 = NULL;
	{
		bool L_0;
		L_0 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		bool L_1 = __this->___m_CleanedUp_4;
		if (!L_1)
		{
			goto IL_0015;
		}
	}
	{
		bool L_2;
		L_2 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}

IL_0015:
	{
		bool L_3 = ___0_disposing;
		if (!L_3)
		{
			goto IL_0051;
		}
	}
	{
		NetworkStream_tF39C3684B6D572BF47F518AD1DB1F4B12CEE4AE0* L_4 = __this->___m_DataStream_2;
		V_0 = L_4;
		RuntimeObject* L_5 = V_0;
		if (!L_5)
		{
			goto IL_002a;
		}
	}
	{
		RuntimeObject* L_6 = V_0;
		NullCheck(L_6);
		InterfaceActionInvoker0::Invoke(0 /* System.Void System.IDisposable::Dispose() */, IDisposable_t030E0496B4E0E4E4F086825007979AF51F7248C5_il2cpp_TypeInfo_var, L_6);
		goto IL_004b;
	}

IL_002a:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_7;
		L_7 = TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline(__this, NULL);
		V_1 = L_7;
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_8 = V_1;
		if (!L_8)
		{
			goto IL_004b;
		}
	}
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_003d:
			{// begin finally (depth: 1)
				Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_9 = V_1;
				NullCheck(L_9);
				Socket_Close_m5EBF3D8BE2C42EF8037BC9372CE7760B1717EEE4(L_9, NULL);
				TcpClient_set_Client_m145E6A35F13B52AF10DE077FD19DE79A622E5F5C_inline(__this, (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL, NULL);
				return;
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_10 = V_1;
			NullCheck(L_10);
			Socket_InternalShutdown_mD0EF08D54151D53369D32CD61C8518454AAAEFE8(L_10, 2, NULL);
			goto IL_004b;
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_004b:
	{
		il2cpp_codegen_runtime_class_init_inline(GC_t920F9CF6EBB7C787E5010A4352E1B587F356DC58_il2cpp_TypeInfo_var);
		GC_SuppressFinalize_m71815DBD5A0CD2EA1BE43317B08B7A14949EDC65(__this, NULL);
	}

IL_0051:
	{
		__this->___m_CleanedUp_4 = (bool)1;
		bool L_11;
		L_11 = Logging_get_On_m994E65D71DFC6C69D1C2097A8908D10D31B6CAEE(NULL);
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::Dispose()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Dispose_m533DFCEE37B4299BBB5FACD9A98F8181EC2386D0 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	{
		VirtualActionInvoker1< bool >::Invoke(5 /* System.Void System.Net.Sockets.TcpClient::Dispose(System.Boolean) */, __this, (bool)1);
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::Finalize()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_Finalize_mE6E1D5B6566ABF5AFEF53EF025298EE6F33E7AE2 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0009:
			{// begin finally (depth: 1)
				Object_Finalize_mC98C96301CCABFE00F1A7EF8E15DF507CACD42B2(__this, NULL);
				return;
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			VirtualActionInvoker1< bool >::Invoke(5 /* System.Void System.Net.Sockets.TcpClient::Dispose(System.Boolean) */, __this, (bool)0);
			goto IL_0010;
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_0010:
	{
		return;
	}
}
// System.Void System.Net.Sockets.TcpClient::initialize()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void TcpClient_initialize_mE26B7664FC1F1DB4D963065A7E47848798697114 (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = __this->___m_Family_3;
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_1 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)il2cpp_codegen_object_new(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		NullCheck(L_1);
		Socket__ctor_m35F1F4B4872E251867DA16460F06E903A30E4595(L_1, L_0, 1, 6, NULL);
		TcpClient_set_Client_m145E6A35F13B52AF10DE077FD19DE79A622E5F5C_inline(__this, L_1, NULL);
		__this->___m_Active_1 = (bool)0;
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.SafeSocketHandle::.ctor(System.IntPtr,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeSocketHandle__ctor_mA17D19F4D9F5F0D3C29A0C46702E85B9C9D87295 (SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE* __this, intptr_t ___0_preexistingHandle, bool ___1_ownsHandle, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Dictionary_2__ctor_m50735B02E2653B889CED4DFEFA0C22917F620C78_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		bool L_0 = ___1_ownsHandle;
		SafeHandleMinusOneIsInvalid__ctor_m626FCBF3D556AE7B1CBE07DCC4913D1B2D629A46(__this, L_0, NULL);
		intptr_t L_1 = ___0_preexistingHandle;
		SafeHandle_SetHandle_m003D64748F9DFBA1E3C0B23798C23BA81AA21C2A_inline(__this, L_1, NULL);
		il2cpp_codegen_runtime_class_init_inline(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		bool L_2 = ((SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields*)il2cpp_codegen_static_fields_for(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var))->___THROW_ON_ABORT_RETRIES_9;
		if (!L_2)
		{
			goto IL_0020;
		}
	}
	{
		Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* L_3 = (Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED*)il2cpp_codegen_object_new(Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED_il2cpp_TypeInfo_var);
		NullCheck(L_3);
		Dictionary_2__ctor_m50735B02E2653B889CED4DFEFA0C22917F620C78(L_3, Dictionary_2__ctor_m50735B02E2653B889CED4DFEFA0C22917F620C78_RuntimeMethod_var);
		__this->___threads_stacktraces_7 = L_3;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___threads_stacktraces_7), (void*)L_3);
	}

IL_0020:
	{
		return;
	}
}
// System.Boolean System.Net.Sockets.SafeSocketHandle::ReleaseHandle()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SafeSocketHandle_ReleaseHandle_m6861A54261EEC98F3DB4BB27251AA7ED6BD4DCE3 (SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Dictionary_2_get_Item_m5B51F010936EF5BBF6820068B9143D37CB2D2D84_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_get_Item_m60371F3C4A4481F126B9BDBFDD9D69F464780693_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&StringBuilder_t_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&_stringLiteral49FB20AF7D832AF20AD2F4170339D473684018D4);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&_stringLiteral7A62F4EEAA031F30B545A398D12F81D6413DE012);
		s_Il2CppMethodInitialized = true;
	}
	int32_t V_0 = 0;
	List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* V_1 = NULL;
	bool V_2 = false;
	int32_t V_3 = 0;
	StringBuilder_t* V_4 = NULL;
	Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 V_5;
	memset((&V_5), 0, sizeof(V_5));
	Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* V_6 = NULL;
	{
		V_0 = 0;
		intptr_t L_0 = ((SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7*)__this)->___handle_0;
		il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		Socket_Blocking_icall_m226DF9E2D2C681B8C5B2E115BCA9B308463383B3(L_0, (bool)0, (&V_0), NULL);
		intptr_t L_1 = ((SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7*)__this)->___handle_0;
		Socket_Shutdown_icall_m96E5BC1E1CB1DE4AF812E9892EFAE78B6638561E(L_1, 2, (&V_0), NULL);
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_2 = __this->___blocking_threads_6;
		if (!L_2)
		{
			goto IL_0161;
		}
	}
	{
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_3 = __this->___blocking_threads_6;
		V_1 = L_3;
		V_2 = (bool)0;
	}
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0157:
			{// begin finally (depth: 1)
				{
					bool L_4 = V_2;
					if (!L_4)
					{
						goto IL_0160;
					}
				}
				{
					List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_5 = V_1;
					Monitor_Exit_m05B2CF037E2214B3208198C282490A2A475653FA(L_5, NULL);
				}

IL_0160:
				{
					return;
				}
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_6 = V_1;
				Monitor_Enter_m3CDB589DA1300B513D55FDCFB52B63E879794149(L_6, (&V_2), NULL);
				V_3 = 0;
				goto IL_0144_1;
			}

IL_0041_1:
			{
				int32_t L_7 = V_3;
				int32_t L_8 = L_7;
				V_3 = ((int32_t)il2cpp_codegen_add(L_8, 1));
				if ((((int32_t)L_8) < ((int32_t)((int32_t)10))))
				{
					goto IL_00d8_1;
				}
			}
			{
				il2cpp_codegen_runtime_class_init_inline(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
				bool L_9 = ((SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields*)il2cpp_codegen_static_fields_for(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var))->___THROW_ON_ABORT_RETRIES_9;
				if (!L_9)
				{
					goto IL_0155_1;
				}
			}
			{
				StringBuilder_t* L_10 = (StringBuilder_t*)il2cpp_codegen_object_new(StringBuilder_t_il2cpp_TypeInfo_var);
				NullCheck(L_10);
				StringBuilder__ctor_m1D99713357DE05DAFA296633639DB55F8C30587D(L_10, NULL);
				V_4 = L_10;
				StringBuilder_t* L_11 = V_4;
				NullCheck(L_11);
				StringBuilder_t* L_12;
				L_12 = StringBuilder_AppendLine_mF75744CE941C63E33188E22E936B71A24D3CBF88(L_11, _stringLiteral7A62F4EEAA031F30B545A398D12F81D6413DE012, NULL);
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_13 = __this->___blocking_threads_6;
				NullCheck(L_13);
				Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 L_14;
				L_14 = List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5(L_13, List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5_RuntimeMethod_var);
				V_5 = L_14;
			}
			{
				auto __finallyBlock = il2cpp::utils::Finally([&]
				{

FINALLY_00b5_1:
					{// begin finally (depth: 2)
						Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9((&V_5), Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9_RuntimeMethod_var);
						return;
					}// end finally (depth: 2)
				});
				try
				{// begin try (depth: 2)
					{
						goto IL_00aa_2;
					}

IL_007a_2:
					{
						Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_15;
						L_15 = Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_inline((&V_5), Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_RuntimeMethod_var);
						V_6 = L_15;
						StringBuilder_t* L_16 = V_4;
						NullCheck(L_16);
						StringBuilder_t* L_17;
						L_17 = StringBuilder_AppendLine_mF75744CE941C63E33188E22E936B71A24D3CBF88(L_16, _stringLiteral49FB20AF7D832AF20AD2F4170339D473684018D4, NULL);
						StringBuilder_t* L_18 = V_4;
						Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* L_19 = __this->___threads_stacktraces_7;
						Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_20 = V_6;
						NullCheck(L_19);
						StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* L_21;
						L_21 = Dictionary_2_get_Item_m5B51F010936EF5BBF6820068B9143D37CB2D2D84(L_19, L_20, Dictionary_2_get_Item_m5B51F010936EF5BBF6820068B9143D37CB2D2D84_RuntimeMethod_var);
						NullCheck(L_21);
						String_t* L_22;
						L_22 = VirtualFuncInvoker0< String_t* >::Invoke(3 /* System.String System.Object::ToString() */, L_21);
						NullCheck(L_18);
						StringBuilder_t* L_23;
						L_23 = StringBuilder_AppendLine_mF75744CE941C63E33188E22E936B71A24D3CBF88(L_18, L_22, NULL);
					}

IL_00aa_2:
					{
						bool L_24;
						L_24 = Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE((&V_5), Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE_RuntimeMethod_var);
						if (L_24)
						{
							goto IL_007a_2;
						}
					}
					{
						goto IL_00c3_1;
					}
				}// end try (depth: 2)
				catch(Il2CppExceptionWrapper& e)
				{
					__finallyBlock.StoreException(e.ex);
				}
			}

IL_00c3_1:
			{
				StringBuilder_t* L_25 = V_4;
				NullCheck(L_25);
				StringBuilder_t* L_26;
				L_26 = StringBuilder_AppendLine_m3BC704C4E6A8531027D8C9287D0AB2AA0188AC4E(L_25, NULL);
				StringBuilder_t* L_27 = V_4;
				NullCheck(L_27);
				String_t* L_28;
				L_28 = VirtualFuncInvoker0< String_t* >::Invoke(3 /* System.String System.Object::ToString() */, L_27);
				Exception_t* L_29 = (Exception_t*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&Exception_t_il2cpp_TypeInfo_var)));
				NullCheck(L_29);
				Exception__ctor_m9B2BD92CD68916245A75109105D9071C9D430E7F(L_29, L_28, NULL);
				IL2CPP_RAISE_MANAGED_EXCEPTION(L_29, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SafeSocketHandle_ReleaseHandle_m6861A54261EEC98F3DB4BB27251AA7ED6BD4DCE3_RuntimeMethod_var)));
			}

IL_00d8_1:
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_30 = __this->___blocking_threads_6;
				NullCheck(L_30);
				int32_t L_31;
				L_31 = List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_inline(L_30, List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var);
				if ((!(((uint32_t)L_31) == ((uint32_t)1))))
				{
					goto IL_00fb_1;
				}
			}
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_32 = __this->___blocking_threads_6;
				NullCheck(L_32);
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_33;
				L_33 = List_1_get_Item_m60371F3C4A4481F126B9BDBFDD9D69F464780693(L_32, 0, List_1_get_Item_m60371F3C4A4481F126B9BDBFDD9D69F464780693_RuntimeMethod_var);
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_34;
				L_34 = Thread_get_CurrentThread_m6D4719F4993DB9200490531FF02D4076FF9CA9BD(NULL);
				if ((!(((RuntimeObject*)(Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*)L_33) == ((RuntimeObject*)(Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F*)L_34))))
				{
					goto IL_00fb_1;
				}
			}
			{
				goto IL_0161;
			}

IL_00fb_1:
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_35 = __this->___blocking_threads_6;
				NullCheck(L_35);
				Enumerator_tDF821B4E909C93A3769A3FA70C611EE7BDE6D936 L_36;
				L_36 = List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5(L_35, List_1_GetEnumerator_mAD8AB2F66B018A6AC6744664D44E4C95F93DF9C5_RuntimeMethod_var);
				V_5 = L_36;
			}
			{
				auto __finallyBlock = il2cpp::utils::Finally([&]
				{

FINALLY_0121_1:
					{// begin finally (depth: 2)
						Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9((&V_5), Enumerator_Dispose_m6D733CC71CDE13E8737D066B069A9372CF3BABC9_RuntimeMethod_var);
						return;
					}// end finally (depth: 2)
				});
				try
				{// begin try (depth: 2)
					{
						goto IL_0116_2;
					}

IL_010a_2:
					{
						Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_37;
						L_37 = Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_inline((&V_5), Enumerator_get_Current_mE729A688F942830177F032C45AE3FD4EB5E5DD7B_RuntimeMethod_var);
						il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
						Socket_cancel_blocking_socket_operation_mB4D1FCFA8304B975A60833FB2805B767DE533C0E(L_37, NULL);
					}

IL_0116_2:
					{
						bool L_38;
						L_38 = Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE((&V_5), Enumerator_MoveNext_m579F59E32FB57F80500F3D341A737BEC441918BE_RuntimeMethod_var);
						if (L_38)
						{
							goto IL_010a_2;
						}
					}
					{
						goto IL_012f_1;
					}
				}// end try (depth: 2)
				catch(Il2CppExceptionWrapper& e)
				{
					__finallyBlock.StoreException(e.ex);
				}
			}

IL_012f_1:
			{
				__this->___in_cleanup_8 = (bool)1;
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_39 = __this->___blocking_threads_6;
				bool L_40;
				L_40 = Monitor_Wait_m60B0A448270187089CC75DE856B496C452EF4601(L_39, ((int32_t)100), NULL);
			}

IL_0144_1:
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_41 = __this->___blocking_threads_6;
				NullCheck(L_41);
				int32_t L_42;
				L_42 = List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_inline(L_41, List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var);
				if ((((int32_t)L_42) > ((int32_t)0)))
				{
					goto IL_0041_1;
				}
			}

IL_0155_1:
			{
				goto IL_0161;
			}
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_0161:
	{
		intptr_t L_43 = ((SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7*)__this)->___handle_0;
		il2cpp_codegen_runtime_class_init_inline(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E_il2cpp_TypeInfo_var);
		Socket_Close_icall_mD2A065D0EA71E9007587C22A8B6C3A95B8F5812A(L_43, (&V_0), NULL);
		int32_t L_44 = V_0;
		return (bool)((((int32_t)L_44) == ((int32_t)0))? 1 : 0);
	}
}
// System.Void System.Net.Sockets.SafeSocketHandle::RegisterForBlockingSyscall()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeSocketHandle_RegisterForBlockingSyscall_mFCDDEAB42C472D9A8E46B76C0BAECEB7D1832A98 (SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Dictionary_2_Add_m021BDE42D4C8CFA7729AE1FA4645832CB8510A45_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_Add_m5F800AC3A9BEE2C4459A42828FB152CD7A848A8C_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1__ctor_m1104DB0524C8D81E9C97C6068CC22A52CCE146EC_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&StackTrace_t7C150C7C14136F985311A83A93524B1019F70853_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	bool V_0 = false;
	List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* V_1 = NULL;
	bool V_2 = false;
	{
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_0 = __this->___blocking_threads_6;
		if (L_0)
		{
			goto IL_001a;
		}
	}
	{
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D** L_1 = (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D**)(&__this->___blocking_threads_6);
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_2 = (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*)il2cpp_codegen_object_new(List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D_il2cpp_TypeInfo_var);
		NullCheck(L_2);
		List_1__ctor_m1104DB0524C8D81E9C97C6068CC22A52CCE146EC(L_2, List_1__ctor_m1104DB0524C8D81E9C97C6068CC22A52CCE146EC_RuntimeMethod_var);
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_3;
		L_3 = InterlockedCompareExchangeImpl<List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*>(L_1, L_2, (List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D*)NULL);
	}

IL_001a:
	{
		V_0 = (bool)0;
	}
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0026:
			{// begin finally (depth: 1)
				{
					List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_4 = __this->___blocking_threads_6;
					V_1 = L_4;
					V_2 = (bool)0;
				}
				{
					auto __finallyBlock = il2cpp::utils::Finally([&]
					{

FINALLY_0066:
						{// begin finally (depth: 2)
							{
								bool L_5 = V_2;
								if (!L_5)
								{
									goto IL_006f;
								}
							}
							{
								List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_6 = V_1;
								Monitor_Exit_m05B2CF037E2214B3208198C282490A2A475653FA(L_6, NULL);
							}

IL_006f:
							{
								return;
							}
						}// end finally (depth: 2)
					});
					try
					{// begin try (depth: 2)
						{
							List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_7 = V_1;
							Monitor_Enter_m3CDB589DA1300B513D55FDCFB52B63E879794149(L_7, (&V_2), NULL);
							List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_8 = __this->___blocking_threads_6;
							Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_9;
							L_9 = Thread_get_CurrentThread_m6D4719F4993DB9200490531FF02D4076FF9CA9BD(NULL);
							NullCheck(L_8);
							List_1_Add_m5F800AC3A9BEE2C4459A42828FB152CD7A848A8C_inline(L_8, L_9, List_1_Add_m5F800AC3A9BEE2C4459A42828FB152CD7A848A8C_RuntimeMethod_var);
							il2cpp_codegen_runtime_class_init_inline(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
							bool L_10 = ((SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields*)il2cpp_codegen_static_fields_for(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var))->___THROW_ON_ABORT_RETRIES_9;
							if (!L_10)
							{
								goto IL_0064_1;
							}
						}
						{
							Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* L_11 = __this->___threads_stacktraces_7;
							Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_12;
							L_12 = Thread_get_CurrentThread_m6D4719F4993DB9200490531FF02D4076FF9CA9BD(NULL);
							StackTrace_t7C150C7C14136F985311A83A93524B1019F70853* L_13 = (StackTrace_t7C150C7C14136F985311A83A93524B1019F70853*)il2cpp_codegen_object_new(StackTrace_t7C150C7C14136F985311A83A93524B1019F70853_il2cpp_TypeInfo_var);
							NullCheck(L_13);
							StackTrace__ctor_m269D00E49783BC3205FD5C7E879888884D72107C(L_13, (bool)1, NULL);
							NullCheck(L_11);
							Dictionary_2_Add_m021BDE42D4C8CFA7729AE1FA4645832CB8510A45(L_11, L_12, L_13, Dictionary_2_Add_m021BDE42D4C8CFA7729AE1FA4645832CB8510A45_RuntimeMethod_var);
						}

IL_0064_1:
						{
							goto IL_0070;
						}
					}// end try (depth: 2)
					catch(Il2CppExceptionWrapper& e)
					{
						__finallyBlock.StoreException(e.ex);
					}
				}

IL_0070:
				{
					bool L_14 = V_0;
					if (!L_14)
					{
						goto IL_0079;
					}
				}
				{
					SafeHandle_DangerousRelease_m30A8B4E5BEA935C8925BC2115CD0AD13B937953E(__this, NULL);
				}

IL_0079:
				{
					bool L_15;
					L_15 = SafeHandle_get_IsClosed_mD2CD4AA6E3B0A242E48080F18BC07199CAB80273(__this, NULL);
					if (!L_15)
					{
						goto IL_008c;
					}
				}
				{
					SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_16 = (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var)));
					NullCheck(L_16);
					SocketException__ctor_mC09AA11941DA13359416179634B151A3FE2B9A64(L_16, ((int32_t)10004), NULL);
					IL2CPP_RAISE_MANAGED_EXCEPTION(L_16, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SafeSocketHandle_RegisterForBlockingSyscall_mFCDDEAB42C472D9A8E46B76C0BAECEB7D1832A98_RuntimeMethod_var)));
				}

IL_008c:
				{
					return;
				}
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			SafeHandle_DangerousAddRef_m9FA46208A92D8B33059B8E8712F49AE45BB5E922(__this, (&V_0), NULL);
			goto IL_008d;
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_008d:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SafeSocketHandle::UnRegisterForBlockingSyscall()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeSocketHandle_UnRegisterForBlockingSyscall_m806E8BB959EDBB3B78061C5628A6F2F6EB6800B4 (SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Dictionary_2_Remove_m43BD8FD04F14C57EA9B7F4017447E918FA3B2557_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_IndexOf_m005D5A092A56062F7E23CE7234826D77A54AAFC2_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_Remove_m6BC6C5F209825F38868B72599159757677635886_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* V_0 = NULL;
	bool V_1 = false;
	Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* V_2 = NULL;
	{
		List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_0 = __this->___blocking_threads_6;
		V_0 = L_0;
		V_1 = (bool)0;
	}
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0069:
			{// begin finally (depth: 1)
				{
					bool L_1 = V_1;
					if (!L_1)
					{
						goto IL_0072;
					}
				}
				{
					List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_2 = V_0;
					Monitor_Exit_m05B2CF037E2214B3208198C282490A2A475653FA(L_2, NULL);
				}

IL_0072:
				{
					return;
				}
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_3 = V_0;
				Monitor_Enter_m3CDB589DA1300B513D55FDCFB52B63E879794149(L_3, (&V_1), NULL);
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_4;
				L_4 = Thread_get_CurrentThread_m6D4719F4993DB9200490531FF02D4076FF9CA9BD(NULL);
				V_2 = L_4;
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_5 = __this->___blocking_threads_6;
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_6 = V_2;
				NullCheck(L_5);
				bool L_7;
				L_7 = List_1_Remove_m6BC6C5F209825F38868B72599159757677635886(L_5, L_6, List_1_Remove_m6BC6C5F209825F38868B72599159757677635886_RuntimeMethod_var);
				il2cpp_codegen_runtime_class_init_inline(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
				bool L_8 = ((SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields*)il2cpp_codegen_static_fields_for(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var))->___THROW_ON_ABORT_RETRIES_9;
				if (!L_8)
				{
					goto IL_0047_1;
				}
			}
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_9 = __this->___blocking_threads_6;
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_10 = V_2;
				NullCheck(L_9);
				int32_t L_11;
				L_11 = List_1_IndexOf_m005D5A092A56062F7E23CE7234826D77A54AAFC2(L_9, L_10, List_1_IndexOf_m005D5A092A56062F7E23CE7234826D77A54AAFC2_RuntimeMethod_var);
				if ((!(((uint32_t)L_11) == ((uint32_t)(-1)))))
				{
					goto IL_0047_1;
				}
			}
			{
				Dictionary_2_t7422864FC6442AAC5C652136E3EDEF5F9AC328ED* L_12 = __this->___threads_stacktraces_7;
				Thread_t0A773B9DE873D2DCAA7D229EAB36757B500E207F* L_13 = V_2;
				NullCheck(L_12);
				bool L_14;
				L_14 = Dictionary_2_Remove_m43BD8FD04F14C57EA9B7F4017447E918FA3B2557(L_12, L_13, Dictionary_2_Remove_m43BD8FD04F14C57EA9B7F4017447E918FA3B2557_RuntimeMethod_var);
			}

IL_0047_1:
			{
				bool L_15 = __this->___in_cleanup_8;
				if (!L_15)
				{
					goto IL_0067_1;
				}
			}
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_16 = __this->___blocking_threads_6;
				NullCheck(L_16);
				int32_t L_17;
				L_17 = List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_inline(L_16, List_1_get_Count_m5C2C4F7F515869E497B68E132BF2A2029A73C932_RuntimeMethod_var);
				if (L_17)
				{
					goto IL_0067_1;
				}
			}
			{
				List_1_tC6F988464EEDD8B37B6AA2E9D311D598FC07845D* L_18 = __this->___blocking_threads_6;
				Monitor_Pulse_mA8279943D6C2913ADFAF661E35C4951BDFACE43D(L_18, NULL);
			}

IL_0067_1:
			{
				goto IL_0073;
			}
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_0073:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SafeSocketHandle::.cctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SafeSocketHandle__cctor_m2159D3A155140D67E2B910DB409FE4FBF290B26F (const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&_stringLiteralED7D39EDF70E08FE57B2522D102BF72B9E5D5331);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&_stringLiteralF9714C70F08B15BEF4414BD33A16130BD670348F);
		s_Il2CppMethodInitialized = true;
	}
	{
		String_t* L_0;
		L_0 = Environment_GetEnvironmentVariable_mAF8CC6EC1CB916789ABB16A02C032F89E508C21A(_stringLiteralF9714C70F08B15BEF4414BD33A16130BD670348F, NULL);
		bool L_1;
		L_1 = String_op_Equality_m030E1B219352228970A076136E455C4E568C02C1(L_0, _stringLiteralED7D39EDF70E08FE57B2522D102BF72B9E5D5331, NULL);
		((SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_StaticFields*)il2cpp_codegen_static_fields_for(SafeSocketHandle_t5A597D30D951E736B750ED09D5B3AB72F98407EE_il2cpp_TypeInfo_var))->___THROW_ON_ABORT_RETRIES_9 = L_1;
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncEventArgs::get_AcceptSocket()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* SocketAsyncEventArgs_get_AcceptSocket_mF12CD5B1067314F3970759A5AE7C94BF1070A4CE (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___U3CAcceptSocketU3Ek__BackingField_6;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_AcceptSocket(System.Net.Sockets.Socket)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_AcceptSocket_m1F265F9F17EE63050B267DFE9B5CA0F3E487B93B (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_value, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_value;
		__this->___U3CAcceptSocketU3Ek__BackingField_6 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CAcceptSocketU3Ek__BackingField_6), (void*)L_0);
		return;
	}
}
// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::get_BytesTransferred()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketAsyncEventArgs_get_BytesTransferred_m18A255B3F69597D990A7287AAC5310DCDE9B77AE (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->___U3CBytesTransferredU3Ek__BackingField_7;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_BytesTransferred(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_BytesTransferred_mAB2EB75BE1BB7B97DBEFCAF3DD42E26BC787D625 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CBytesTransferredU3Ek__BackingField_7 = L_0;
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_LastOperation(System.Net.Sockets.SocketAsyncOperation)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_LastOperation_m7BA8A0B5CB76B4C095BBB9E1124C3F237B51E26A (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CLastOperationU3Ek__BackingField_8 = L_0;
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_RemoteEndPoint(System.Net.EndPoint)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_RemoteEndPoint_m63E183C33AA5B60D8331A1CEC30A008B6AC7206F (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* ___0_value, const RuntimeMethod* method) 
{
	{
		EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564* L_0 = ___0_value;
		__this->___remote_ep_3 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___remote_ep_3), (void*)L_0);
		return;
	}
}
// System.Net.Sockets.SocketError System.Net.Sockets.SocketAsyncEventArgs::get_SocketError()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketAsyncEventArgs_get_SocketError_m0609040BBEA9BB0C0C6161A7B9A0B5EF50A6346B (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->___U3CSocketErrorU3Ek__BackingField_9;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_SocketError(System.Net.Sockets.SocketError)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_SocketError_m0E7EC931E1FDA4014BCB6A5E7D5E3E216B80BFB2 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CSocketErrorU3Ek__BackingField_9 = L_0;
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_SocketFlags(System.Net.Sockets.SocketFlags)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_SocketFlags_m0ED0ECFCD2A27C9B5D9AD5F6C38B1146685F1EFA (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CSocketFlagsU3Ek__BackingField_10 = L_0;
		return;
	}
}
// System.Object System.Net.Sockets.SocketAsyncEventArgs::get_UserToken()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* SocketAsyncEventArgs_get_UserToken_m7953E8417ECA92D0AE46615B343136763B0CFB12 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = __this->___U3CUserTokenU3Ek__BackingField_11;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::set_UserToken(System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_UserToken_m10B93BD715163BE1A9C813413BD5BD3C4209032D (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, RuntimeObject* ___0_value, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = ___0_value;
		__this->___U3CUserTokenU3Ek__BackingField_11 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CUserTokenU3Ek__BackingField_11), (void*)L_0);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::.ctor(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs__ctor_m14B163B655DD0353E9E022305674A77BF5958D79 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, bool ___0_flowExecutionContext, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* L_0 = (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340*)il2cpp_codegen_object_new(SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var);
		NullCheck(L_0);
		SocketAsyncResult__ctor_m2123FFF2C2A56DB840E109348FEEA575B5B691E4(L_0, NULL);
		__this->___socket_async_result_5 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___socket_async_result_5), (void*)L_0);
		il2cpp_codegen_runtime_class_init_inline(EventArgs_t37273F03EAC87217701DD431B190FBD84AD7C377_il2cpp_TypeInfo_var);
		EventArgs__ctor_mC6F9412D03203ADEF854117542C8EBF61624C8C3(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::Finalize()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_Finalize_m59E3BE36B99D1B090D58EBA606D6750E040D6D01 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0009:
			{// begin finally (depth: 1)
				Object_Finalize_mC98C96301CCABFE00F1A7EF8E15DF507CACD42B2(__this, NULL);
				return;
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			SocketAsyncEventArgs_Dispose_mA5B8D8ADB8521BC9219061C13CB7C07BD02E16DB(__this, (bool)0, NULL);
			goto IL_0010;
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_0010:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_Dispose_mA5B8D8ADB8521BC9219061C13CB7C07BD02E16DB (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, bool ___0_disposing, const RuntimeMethod* method) 
{
	{
		__this->___disposed_1 = (bool)1;
		bool L_0 = ___0_disposing;
		if (!L_0)
		{
			goto IL_0014;
		}
	}
	{
		int32_t L_1 = __this->___in_progress_2;
		il2cpp_codegen_memory_barrier();
		return;
	}

IL_0014:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::Dispose()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_Dispose_m6DCE1086200FEDE70AFC93B221810F02334FCEA1 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&GC_t920F9CF6EBB7C787E5010A4352E1B587F356DC58_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		SocketAsyncEventArgs_Dispose_mA5B8D8ADB8521BC9219061C13CB7C07BD02E16DB(__this, (bool)1, NULL);
		il2cpp_codegen_runtime_class_init_inline(GC_t920F9CF6EBB7C787E5010A4352E1B587F356DC58_il2cpp_TypeInfo_var);
		GC_SuppressFinalize_m71815DBD5A0CD2EA1BE43317B08B7A14949EDC65(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::SetBytesTransferred(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_SetBytesTransferred_m5ACAA13E707A9D5B8F75FD06DD54AB6F21F60585 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		SocketAsyncEventArgs_set_BytesTransferred_mAB2EB75BE1BB7B97DBEFCAF3DD42E26BC787D625_inline(__this, L_0, NULL);
		return;
	}
}
// System.Net.Sockets.Socket System.Net.Sockets.SocketAsyncEventArgs::get_CurrentSocket()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* SocketAsyncEventArgs_get_CurrentSocket_m1FA3256E04EA21BDF5B2ACC9813165D21E614F43 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___current_socket_4;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::SetCurrentSocket(System.Net.Sockets.Socket)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_SetCurrentSocket_m1A43B5217A35F7EB632A0AF19CAC038378E21026 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_socket, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_socket;
		__this->___current_socket_4 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___current_socket_4), (void*)L_0);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::SetLastOperation(System.Net.Sockets.SocketAsyncOperation)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_SetLastOperation_m73C3FAA754E365C569C956149E6E10E9739DBB17 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_op, const RuntimeMethod* method) 
{
	{
		bool L_0 = __this->___disposed_1;
		if (!L_0)
		{
			goto IL_0013;
		}
	}
	{
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_1 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var)));
		NullCheck(L_1);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_1, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralCDBA3FD0CA21972DFCCD218E38E1454CA7EEE93B)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_1, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketAsyncEventArgs_SetLastOperation_m73C3FAA754E365C569C956149E6E10E9739DBB17_RuntimeMethod_var)));
	}

IL_0013:
	{
		int32_t* L_2 = (int32_t*)(&__this->___in_progress_2);
		il2cpp_codegen_memory_barrier();
		int32_t L_3;
		L_3 = Interlocked_Exchange_m5810F59A9EF49D8003BA76D95398854254A091DF(L_2, 1, NULL);
		if (!L_3)
		{
			goto IL_002c;
		}
	}
	{
		InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB* L_4 = (InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		InvalidOperationException__ctor_mE4CB6F4712AB6D99A2358FBAE2E052B3EE976162(L_4, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralFCC60170DC9ECA079BBFC781ACD49B93D0E8C4AE)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketAsyncEventArgs_SetLastOperation_m73C3FAA754E365C569C956149E6E10E9739DBB17_RuntimeMethod_var)));
	}

IL_002c:
	{
		int32_t L_5 = ___0_op;
		SocketAsyncEventArgs_set_LastOperation_m7BA8A0B5CB76B4C095BBB9E1124C3F237B51E26A_inline(__this, L_5, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::Complete_internal()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_Complete_internal_m80E7F9C012A9FF94E68C09B05314042FA80549B0 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		il2cpp_codegen_memory_barrier();
		__this->___in_progress_2 = 0;
		VirtualActionInvoker1< SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* >::Invoke(5 /* System.Void System.Net.Sockets.SocketAsyncEventArgs::OnCompleted(System.Net.Sockets.SocketAsyncEventArgs) */, __this, __this);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::OnCompleted(System.Net.Sockets.SocketAsyncEventArgs)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_OnCompleted_mA73590353C7D619F0A57E5D65C7D6CF1EF41AD6C (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* ___0_e, const RuntimeMethod* method) 
{
	EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* V_0 = NULL;
	{
		SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* L_0 = ___0_e;
		if (L_0)
		{
			goto IL_0004;
		}
	}
	{
		return;
	}

IL_0004:
	{
		SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* L_1 = ___0_e;
		NullCheck(L_1);
		EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* L_2 = L_1->___Completed_12;
		V_0 = L_2;
		EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* L_3 = V_0;
		if (!L_3)
		{
			goto IL_001b;
		}
	}
	{
		EventHandler_1_t5D3FC4609BD8133ED1226D6D49A1D8076B16A9ED* L_4 = V_0;
		SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* L_5 = ___0_e;
		NullCheck(L_5);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_6 = L_5->___current_socket_4;
		SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* L_7 = ___0_e;
		NullCheck(L_4);
		EventHandler_1_Invoke_mF0FFF25301C90C9B2E0769E48203543140B6A01C_inline(L_4, L_6, L_7, NULL);
	}

IL_001b:
	{
		return;
	}
}
// System.Memory`1<System.Byte> System.Net.Sockets.SocketAsyncEventArgs::get_MemoryBuffer()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 SocketAsyncEventArgs_get_MemoryBuffer_m431CDEB0A4D2496B5A4C44232A72FF9DE95FEE1A (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 L_0 = __this->____buffer_13;
		return L_0;
	}
}
// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::get_Offset()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketAsyncEventArgs_get_Offset_m0D3ADE0DFB4BACC0FC3C85ED389DC9DCE451EA01 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->____offset_14;
		return L_0;
	}
}
// System.Int32 System.Net.Sockets.SocketAsyncEventArgs::get_Count()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketAsyncEventArgs_get_Count_m4B67FDD41086AF476925D19F590DDE95579B5313 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->____count_15;
		return L_0;
	}
}
// System.Collections.Generic.IList`1<System.ArraySegment`1<System.Byte>> System.Net.Sockets.SocketAsyncEventArgs::get_BufferList()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* SocketAsyncEventArgs_get_BufferList_mD9E837C74ED11F81EC7076E532FA5067DCCAB173 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = __this->____bufferList_17;
		return L_0;
	}
}
// System.Void System.Net.Sockets.SocketAsyncEventArgs::SetBuffer(System.Memory`1<System.Byte>)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_SetBuffer_m442BE1EFE47E011A747645F5A1F31F0702852EF7 (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 ___0_buffer, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0;
		L_0 = Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714((&___0_buffer), Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_RuntimeMethod_var);
		if (!L_0)
		{
			goto IL_0026;
		}
	}
	{
		RuntimeObject* L_1 = __this->____bufferList_17;
		if (!L_1)
		{
			goto IL_0026;
		}
	}
	{
		String_t* L_2;
		L_2 = SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralF17F1002CEBE6D8756D7A588CEF423B082021CD0)), ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralF95A5F2D2E318732DB7B00E08A46A31CDE16171E)), NULL);
		ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* L_3 = (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var)));
		NullCheck(L_3);
		ArgumentException__ctor_m026938A67AF9D36BB7ED27F80425D7194B514465(L_3, L_2, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_3, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketAsyncEventArgs_SetBuffer_m442BE1EFE47E011A747645F5A1F31F0702852EF7_RuntimeMethod_var)));
	}

IL_0026:
	{
		Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 L_4 = ___0_buffer;
		__this->____buffer_13 = L_4;
		Il2CppCodeGenWriteBarrier((void**)&(((&__this->____buffer_13))->____object_0), (void*)NULL);
		__this->____offset_14 = 0;
		int32_t L_5;
		L_5 = Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714((&___0_buffer), Memory_1_get_Length_mA63190F8E6F8A531AA0A55447D48210D8083B714_RuntimeMethod_var);
		__this->____count_15 = L_5;
		__this->____bufferIsExplicitArray_16 = (bool)0;
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif


// Conversion methods for marshalling of: System.Net.Sockets.SocketAsyncResult
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_pinvoke(const SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340& unmarshaled, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_pinvoke& marshaled)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	Exception_t* ___socket_5Exception = il2cpp_codegen_get_marshal_directive_exception("Cannot marshal field '%s' of type '%s': Reference type field marshaling is not supported.", SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
	IL2CPP_RAISE_MANAGED_EXCEPTION(___socket_5Exception, NULL);
}
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_pinvoke_back(const SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_pinvoke& marshaled, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340& unmarshaled)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	Exception_t* ___socket_5Exception = il2cpp_codegen_get_marshal_directive_exception("Cannot marshal field '%s' of type '%s': Reference type field marshaling is not supported.", SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
	IL2CPP_RAISE_MANAGED_EXCEPTION(___socket_5Exception, NULL);
}
// Conversion method for clean up from marshalling of: System.Net.Sockets.SocketAsyncResult
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_pinvoke_cleanup(SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_pinvoke& marshaled)
{
}


// Conversion methods for marshalling of: System.Net.Sockets.SocketAsyncResult
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_com(const SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340& unmarshaled, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_com& marshaled)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	Exception_t* ___socket_5Exception = il2cpp_codegen_get_marshal_directive_exception("Cannot marshal field '%s' of type '%s': Reference type field marshaling is not supported.", SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
	IL2CPP_RAISE_MANAGED_EXCEPTION(___socket_5Exception, NULL);
}
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_com_back(const SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_com& marshaled, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340& unmarshaled)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	Exception_t* ___socket_5Exception = il2cpp_codegen_get_marshal_directive_exception("Cannot marshal field '%s' of type '%s': Reference type field marshaling is not supported.", SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340____socket_5_FieldInfo_var, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_0_0_0_var);
	IL2CPP_RAISE_MANAGED_EXCEPTION(___socket_5Exception, NULL);
}
// Conversion method for clean up from marshalling of: System.Net.Sockets.SocketAsyncResult
IL2CPP_EXTERN_C void SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshal_com_cleanup(SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_marshaled_com& marshaled)
{
}
// System.IntPtr System.Net.Sockets.SocketAsyncResult::get_Handle()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR intptr_t SocketAsyncResult_get_Handle_mBEAD04497226B5110889462C480B2BBF93AAD430 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&IntPtr_t_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___socket_5;
		if (L_0)
		{
			goto IL_000e;
		}
	}
	{
		intptr_t L_1 = ((IntPtr_t_StaticFields*)il2cpp_codegen_static_fields_for(IntPtr_t_il2cpp_TypeInfo_var))->___Zero_1;
		return L_1;
	}

IL_000e:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_2 = __this->___socket_5;
		NullCheck(L_2);
		intptr_t L_3;
		L_3 = Socket_get_Handle_mBF42AC5E9403C23BE532F17EE337BF9F150A0256(L_2, NULL);
		return L_3;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult__ctor_m2123FFF2C2A56DB840E109348FEEA575B5B691E4 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	{
		IOAsyncResult__ctor_mAA7211B9E6FDF23D0012354FEE350B6B5EB786F4(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Init(System.Net.Sockets.Socket,System.AsyncCallback,System.Object,System.Net.Sockets.SocketOperation)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Init_m4FFCC4198F1133FC01DB80D562B59FB35086EC45 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_socket, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___1_callback, RuntimeObject* ___2_state, int32_t ___3_operation, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_0 = ___1_callback;
		RuntimeObject* L_1 = ___2_state;
		IOAsyncResult_Init_m6580DFCCB89EF7CFF91F7040C62ECC3811A3A98D(__this, L_0, L_1, NULL);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_2 = ___0_socket;
		__this->___socket_5 = L_2;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___socket_5), (void*)L_2);
		int32_t L_3 = ___3_operation;
		__this->___operation_6 = L_3;
		__this->___DelayedException_7 = (Exception_t*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___DelayedException_7), (void*)(Exception_t*)NULL);
		__this->___EndPoint_8 = (EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___EndPoint_8), (void*)(EndPoint_t6233F4E2EB9F0F2D36E187F12BE050E6D8B73564*)NULL);
		Memory_1_tB7CEF4416F5014E364267478CEF016A4AC5C0036 L_4;
		L_4 = Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C((ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*)NULL, Memory_1_op_Implicit_m40D10666CBBA1748CA281BAB5EDA02E8525D1B3C_RuntimeMethod_var);
		__this->___Buffer_9 = L_4;
		Il2CppCodeGenWriteBarrier((void**)&(((&__this->___Buffer_9))->____object_0), (void*)NULL);
		__this->___Offset_10 = 0;
		__this->___Size_11 = 0;
		__this->___SockFlags_12 = 0;
		__this->___AcceptSocket_13 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___AcceptSocket_13), (void*)(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL);
		__this->___Addresses_14 = (IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___Addresses_14), (void*)(IPAddressU5BU5D_t3AEDF3B94746C9023A4549F878AA47F702C9CD0D*)NULL);
		__this->___Port_15 = 0;
		__this->___Buffers_16 = (RuntimeObject*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___Buffers_16), (void*)(RuntimeObject*)NULL);
		__this->___ReuseSocket_17 = (bool)0;
		__this->___CurrentAddress_18 = 0;
		__this->___AcceptedSocket_19 = (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___AcceptedSocket_19), (void*)(Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E*)NULL);
		__this->___Total_20 = 0;
		__this->___error_21 = 0;
		__this->___EndCalled_22 = 0;
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::.ctor(System.Net.Sockets.Socket,System.AsyncCallback,System.Object,System.Net.Sockets.SocketOperation)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult__ctor_m3C1FE5E660B13BB4C17D1756B918D9EEC6052F79 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_socket, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___1_callback, RuntimeObject* ___2_state, int32_t ___3_operation, const RuntimeMethod* method) 
{
	{
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_0 = ___1_callback;
		RuntimeObject* L_1 = ___2_state;
		IOAsyncResult__ctor_mCDC93C97872E9DAB6A1D9F27C1FA14DDFC6A400D(__this, L_0, L_1, NULL);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_2 = ___0_socket;
		__this->___socket_5 = L_2;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___socket_5), (void*)L_2);
		int32_t L_3 = ___3_operation;
		__this->___operation_6 = L_3;
		return;
	}
}
// System.Net.Sockets.SocketError System.Net.Sockets.SocketAsyncResult::get_ErrorCode()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SocketAsyncResult_get_ErrorCode_m2CFEB18E19F49775804758830C58B1B068CF936C (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* V_0 = NULL;
	{
		Exception_t* L_0 = __this->___DelayedException_7;
		V_0 = ((SocketException_t6D10102A62EA871BD31748E026A372DB6804083B*)IsInstClass((RuntimeObject*)L_0, SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var));
		SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_1 = V_0;
		if (!L_1)
		{
			goto IL_0016;
		}
	}
	{
		SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_2 = V_0;
		NullCheck(L_2);
		int32_t L_3;
		L_3 = SocketException_get_SocketErrorCode_m84FB2D308F046A24A1355975F3BF689C988224C6(L_2, NULL);
		return L_3;
	}

IL_0016:
	{
		int32_t L_4 = __this->___error_21;
		if (!L_4)
		{
			goto IL_0025;
		}
	}
	{
		int32_t L_5 = __this->___error_21;
		return (int32_t)(L_5);
	}

IL_0025:
	{
		return (int32_t)(0);
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::CheckIfThrowDelayedException()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_CheckIfThrowDelayedException_m25275FD1C77F37FB2CFDE46BDC669CF9B31031C9 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	{
		Exception_t* L_0 = __this->___DelayedException_7;
		if (!L_0)
		{
			goto IL_001b;
		}
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_1 = __this->___socket_5;
		NullCheck(L_1);
		L_1->___is_connected_25 = (bool)0;
		Exception_t* L_2 = __this->___DelayedException_7;
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_2, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketAsyncResult_CheckIfThrowDelayedException_m25275FD1C77F37FB2CFDE46BDC669CF9B31031C9_RuntimeMethod_var)));
	}

IL_001b:
	{
		int32_t L_3 = __this->___error_21;
		if (!L_3)
		{
			goto IL_003b;
		}
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_4 = __this->___socket_5;
		NullCheck(L_4);
		L_4->___is_connected_25 = (bool)0;
		int32_t L_5 = __this->___error_21;
		SocketException_t6D10102A62EA871BD31748E026A372DB6804083B* L_6 = (SocketException_t6D10102A62EA871BD31748E026A372DB6804083B*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketException_t6D10102A62EA871BD31748E026A372DB6804083B_il2cpp_TypeInfo_var)));
		NullCheck(L_6);
		SocketException__ctor_mC09AA11941DA13359416179634B151A3FE2B9A64(L_6, L_5, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_6, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SocketAsyncResult_CheckIfThrowDelayedException_m25275FD1C77F37FB2CFDE46BDC669CF9B31031C9_RuntimeMethod_var)));
	}

IL_003b:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::CompleteDisposed()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_CompleteDisposed_m41B0FF709D71B1B321871B18BCFAAB3005419BAC (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	{
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&U3CU3Ec_U3CCompleteU3Eb__27_0_m267CAA8BCD77C0263EF3DAC0336FC29A937D2C76_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* V_0 = NULL;
	int32_t V_1 = 0;
	WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* G_B7_0 = NULL;
	WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* G_B6_0 = NULL;
	{
		int32_t L_0 = __this->___operation_6;
		if ((((int32_t)L_0) == ((int32_t)2)))
		{
			goto IL_0031;
		}
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_1 = __this->___socket_5;
		NullCheck(L_1);
		bool L_2;
		L_2 = Socket_get_CleanedUp_m8F6EDAE37C56CBC282D3EAF9D93B5726EEC85988(L_1, NULL);
		if (!L_2)
		{
			goto IL_0031;
		}
	}
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_3 = __this->___socket_5;
		NullCheck(L_3);
		Type_t* L_4;
		L_4 = Object_GetType_mE10A8FC1E57F3DF29972CCBC026C2DC3942263B3(L_3, NULL);
		NullCheck(L_4);
		String_t* L_5;
		L_5 = VirtualFuncInvoker0< String_t* >::Invoke(3 /* System.String System.Object::ToString() */, L_4);
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_6 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var);
		NullCheck(L_6);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_6, L_5, NULL);
		__this->___DelayedException_7 = L_6;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___DelayedException_7), (void*)L_6);
	}

IL_0031:
	{
		IOAsyncResult_set_IsCompleted_m72D907352B8AC32DF47864E4F4EDFADEC825A6F9(__this, (bool)1, NULL);
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_7 = __this->___socket_5;
		V_0 = L_7;
		int32_t L_8 = __this->___operation_6;
		V_1 = L_8;
		bool L_9;
		L_9 = IOAsyncResult_get_CompletedSynchronously_m26279BC02AFFE83009A7EEABFFF58EEB2D12CD24_inline(__this, NULL);
		if (L_9)
		{
			goto IL_007c;
		}
	}
	{
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_10;
		L_10 = IOAsyncResult_get_AsyncCallback_mC6150F5FC2CEE8FDC22EACD9B9BC72544FE801CC_inline(__this, NULL);
		if (!L_10)
		{
			goto IL_007c;
		}
	}
	{
		il2cpp_codegen_runtime_class_init_inline(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var);
		WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* L_11 = ((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9__27_0_1;
		WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* L_12 = L_11;
		G_B6_0 = L_12;
		if (L_12)
		{
			G_B7_0 = L_12;
			goto IL_0075;
		}
	}
	{
		il2cpp_codegen_runtime_class_init_inline(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var);
		U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* L_13 = ((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9_0;
		WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* L_14 = (WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3*)il2cpp_codegen_object_new(WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3_il2cpp_TypeInfo_var);
		NullCheck(L_14);
		WaitCallback__ctor_m9730564F9A28ECB72462D05AA92CA9E43DE9B41C(L_14, L_13, (intptr_t)((void*)U3CU3Ec_U3CCompleteU3Eb__27_0_m267CAA8BCD77C0263EF3DAC0336FC29A937D2C76_RuntimeMethod_var), NULL);
		WaitCallback_tFB2C7FD58D024BBC2B0333DC7A4CB63B8DEBD5D3* L_15 = L_14;
		((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9__27_0_1 = L_15;
		Il2CppCodeGenWriteBarrier((void**)(&((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9__27_0_1), (void*)L_15);
		G_B7_0 = L_15;
	}

IL_0075:
	{
		bool L_16;
		L_16 = ThreadPool_UnsafeQueueUserWorkItem_m05CF00E355CE07D625386779E3F3D3D2341FEE86(G_B7_0, __this, NULL);
	}

IL_007c:
	{
		int32_t L_17 = V_1;
		switch (L_17)
		{
			case 0:
			{
				goto IL_00b3;
			}
			case 1:
			{
				goto IL_00cc;
			}
			case 2:
			{
				goto IL_00b3;
			}
			case 3:
			{
				goto IL_00b3;
			}
			case 4:
			{
				goto IL_00c0;
			}
			case 5:
			{
				goto IL_00c0;
			}
			case 6:
			{
				goto IL_00cc;
			}
			case 7:
			{
				goto IL_00cc;
			}
			case 8:
			{
				goto IL_00cc;
			}
			case 9:
			{
				goto IL_00cc;
			}
			case 10:
			{
				goto IL_00b3;
			}
			case 11:
			{
				goto IL_00c0;
			}
		}
	}
	{
		return;
	}

IL_00b3:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_18 = V_0;
		NullCheck(L_18);
		SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* L_19 = L_18->___ReadSem_21;
		NullCheck(L_19);
		int32_t L_20;
		L_20 = SemaphoreSlim_Release_m4F3CB668D91DEE067A67266C266FEE5AADF56148(L_19, NULL);
		return;
	}

IL_00c0:
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_21 = V_0;
		NullCheck(L_21);
		SemaphoreSlim_t0D5CB5685D9BFA5BF95CEC6E7395490F933E8DB2* L_22 = L_21->___WriteSem_22;
		NullCheck(L_22);
		int32_t L_23;
		L_23 = SemaphoreSlim_Release_m4F3CB668D91DEE067A67266C266FEE5AADF56148(L_22, NULL);
	}

IL_00cc:
	{
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_m218F8233E7ADB1AFFE69406F6337E09726E640D2 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, bool ___0_synch, const RuntimeMethod* method) 
{
	{
		bool L_0 = ___0_synch;
		IOAsyncResult_set_CompletedSynchronously_m29ECF9F6388C1F9A12AF83DF8CD19D9FFB4B4D6D_inline(__this, L_0, NULL);
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_mC5E61EFDC26D56DCFD98A8600B185043FA285B6E (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, int32_t ___0_total, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_total;
		__this->___Total_20 = L_0;
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Exception,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_m6F201277AC561C7A755617A4963735B70D178B12 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Exception_t* ___0_e, bool ___1_synch, const RuntimeMethod* method) 
{
	{
		Exception_t* L_0 = ___0_e;
		__this->___DelayedException_7 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___DelayedException_7), (void*)L_0);
		bool L_1 = ___1_synch;
		IOAsyncResult_set_CompletedSynchronously_m29ECF9F6388C1F9A12AF83DF8CD19D9FFB4B4D6D_inline(__this, L_1, NULL);
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Exception)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_m49FA96276F9EBB207D8194440AE89300DDDAC513 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Exception_t* ___0_e, const RuntimeMethod* method) 
{
	{
		Exception_t* L_0 = ___0_e;
		__this->___DelayedException_7 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___DelayedException_7), (void*)L_0);
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Net.Sockets.Socket)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_mD191B47C619B02AB538B7A00721E689AAFBBD3EA (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_s, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_s;
		__this->___AcceptedSocket_19 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___AcceptedSocket_19), (void*)L_0);
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult::Complete(System.Net.Sockets.Socket,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketAsyncResult_Complete_m85EF6089B3B9ECB2A9737E9C694BCCFFEAE48CF1 (SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_s, int32_t ___1_total, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_s;
		__this->___AcceptedSocket_19 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___AcceptedSocket_19), (void*)L_0);
		int32_t L_1 = ___1_total;
		__this->___Total_20 = L_1;
		SocketAsyncResult_Complete_mE0BFD66E710CA1E69D16B0283A5C637BDE354CAA(__this, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Sockets.SocketAsyncResult/<>c::.cctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__cctor_m7B1D6CBE8BBE98411F97B3A4327AFA320BB7C32E (const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* L_0 = (U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45*)il2cpp_codegen_object_new(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var);
		NullCheck(L_0);
		U3CU3Ec__ctor_mAC11B6F52B2C5BA43E6B14CF8338841CAA0ABF26(L_0, NULL);
		((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9_0 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&((U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_StaticFields*)il2cpp_codegen_static_fields_for(U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45_il2cpp_TypeInfo_var))->___U3CU3E9_0), (void*)L_0);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult/<>c::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__ctor_mAC11B6F52B2C5BA43E6B14CF8338841CAA0ABF26 (U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* __this, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
// System.Void System.Net.Sockets.SocketAsyncResult/<>c::<Complete>b__27_0(System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec_U3CCompleteU3Eb__27_0_m267CAA8BCD77C0263EF3DAC0336FC29A937D2C76 (U3CU3Ec_tB9FAB1E6994B42B67246693CD6E523D26D4BCC45* __this, RuntimeObject* ___0_state, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		RuntimeObject* L_0 = ___0_state;
		NullCheck(((SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340*)CastclassSealed((RuntimeObject*)L_0, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var)));
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_1;
		L_1 = IOAsyncResult_get_AsyncCallback_mC6150F5FC2CEE8FDC22EACD9B9BC72544FE801CC_inline(((SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340*)CastclassSealed((RuntimeObject*)L_0, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var)), NULL);
		RuntimeObject* L_2 = ___0_state;
		NullCheck(L_1);
		AsyncCallback_Invoke_mA85DEAE969B1D0FD97E934DC39439E89D7553910_inline(L_1, ((SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340*)CastclassSealed((RuntimeObject*)L_2, SocketAsyncResult_tB157B0D0A1219D5A71AC5030C8BA8EA9D0F25340_il2cpp_TypeInfo_var)), NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.String System.Net.Security.SslClientAuthenticationOptions::get_TargetHost()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SslClientAuthenticationOptions_get_TargetHost_mC4CF1905E7B4F8193544D4997E305406B976A797 (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, const RuntimeMethod* method) 
{
	{
		String_t* L_0 = __this->___U3CTargetHostU3Ek__BackingField_4;
		return L_0;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::set_TargetHost(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions_set_TargetHost_m0D8FF94F267DC51F2BDE6E1C94C2CD67783C4019 (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, String_t* ___0_value, const RuntimeMethod* method) 
{
	{
		String_t* L_0 = ___0_value;
		__this->___U3CTargetHostU3Ek__BackingField_4 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CTargetHostU3Ek__BackingField_4), (void*)L_0);
		return;
	}
}
// System.Security.Cryptography.X509Certificates.X509CertificateCollection System.Net.Security.SslClientAuthenticationOptions::get_ClientCertificates()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* SslClientAuthenticationOptions_get_ClientCertificates_mD6898001726E82ADD9E3083D78BD05DB619B313C (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, const RuntimeMethod* method) 
{
	{
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_0 = __this->___U3CClientCertificatesU3Ek__BackingField_5;
		return L_0;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::set_ClientCertificates(System.Security.Cryptography.X509Certificates.X509CertificateCollection)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions_set_ClientCertificates_m08B053F1C158912EEA43A4A28D37A740041CA78B (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___0_value, const RuntimeMethod* method) 
{
	{
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_0 = ___0_value;
		__this->___U3CClientCertificatesU3Ek__BackingField_5 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CClientCertificatesU3Ek__BackingField_5), (void*)L_0);
		return;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::set_CertificateRevocationCheckMode(System.Security.Cryptography.X509Certificates.X509RevocationMode)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions_set_CertificateRevocationCheckMode_m5DB15346690DAC5A8F610AA8C7DEC7F2DFC29DE9 (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		if (!L_0)
		{
			goto IL_0025;
		}
	}
	{
		int32_t L_1 = ___0_value;
		if ((((int32_t)L_1) == ((int32_t)2)))
		{
			goto IL_0025;
		}
	}
	{
		int32_t L_2 = ___0_value;
		if ((((int32_t)L_2) == ((int32_t)1)))
		{
			goto IL_0025;
		}
	}
	{
		String_t* L_3;
		L_3 = SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral215B9008FEB54933AF219588889451A0CB610D34)), ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral73E7438EC9EFF139A2E1CEA81F03B87F9EF5CF0A)), NULL);
		ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* L_4 = (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		ArgumentException__ctor_m8F9D40CE19D19B698A70F9A258640EB52DB39B62(L_4, L_3, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral46F273EF641E07D271D91E0DC24A4392582671F8)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslClientAuthenticationOptions_set_CertificateRevocationCheckMode_m5DB15346690DAC5A8F610AA8C7DEC7F2DFC29DE9_RuntimeMethod_var)));
	}

IL_0025:
	{
		int32_t L_5 = ___0_value;
		__this->____checkCertificateRevocation_1 = L_5;
		return;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::set_EncryptionPolicy(System.Net.Security.EncryptionPolicy)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions_set_EncryptionPolicy_m008E6F28D3C4BFEFFB32A9BEA6AEF1C2B53F1C77 (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		if (!L_0)
		{
			goto IL_0025;
		}
	}
	{
		int32_t L_1 = ___0_value;
		if ((((int32_t)L_1) == ((int32_t)1)))
		{
			goto IL_0025;
		}
	}
	{
		int32_t L_2 = ___0_value;
		if ((((int32_t)L_2) == ((int32_t)2)))
		{
			goto IL_0025;
		}
	}
	{
		String_t* L_3;
		L_3 = SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral215B9008FEB54933AF219588889451A0CB610D34)), ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralCB9340620E0F239BC39D9B1AA9CCF6119A0BB3FF)), NULL);
		ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* L_4 = (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var)));
		NullCheck(L_4);
		ArgumentException__ctor_m8F9D40CE19D19B698A70F9A258640EB52DB39B62(L_4, L_3, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral46F273EF641E07D271D91E0DC24A4392582671F8)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_4, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslClientAuthenticationOptions_set_EncryptionPolicy_m008E6F28D3C4BFEFFB32A9BEA6AEF1C2B53F1C77_RuntimeMethod_var)));
	}

IL_0025:
	{
		int32_t L_5 = ___0_value;
		__this->____encryptionPolicy_0 = L_5;
		return;
	}
}
// System.Security.Authentication.SslProtocols System.Net.Security.SslClientAuthenticationOptions::get_EnabledSslProtocols()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SslClientAuthenticationOptions_get_EnabledSslProtocols_mEC43F31DCFB3322900792809C72AC48070A9D93B (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->____enabledSslProtocols_2;
		return L_0;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::set_EnabledSslProtocols(System.Security.Authentication.SslProtocols)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions_set_EnabledSslProtocols_mC0D304F69A5DC05292B87E00A73415BC83A632D5 (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->____enabledSslProtocols_2 = L_0;
		return;
	}
}
// System.Void System.Net.Security.SslClientAuthenticationOptions::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslClientAuthenticationOptions__ctor_mFFA8C3E39592A8376E7C3F6AC6FB89A6B7A702CD (SslClientAuthenticationOptions_tA5277DAACAFB33887D2513D9399C5154B0D5CBC9* __this, const RuntimeMethod* method) 
{
	{
		__this->____allowRenegotiation_3 = (bool)1;
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Security.AuthenticatedStream::.ctor(System.IO.Stream,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357 (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_runtime_class_init_inline(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var);
		Stream__ctor_mE8B074A0EBEB026FFF14062AB4B8A78E17EFFBF0(__this, NULL);
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = ___0_innerStream;
		if (!L_0)
		{
			goto IL_0011;
		}
	}
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_1 = ___0_innerStream;
		il2cpp_codegen_runtime_class_init_inline(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var);
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_2 = ((Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_StaticFields*)il2cpp_codegen_static_fields_for(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var))->___Null_1;
		if ((!(((RuntimeObject*)(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE*)L_1) == ((RuntimeObject*)(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE*)L_2))))
		{
			goto IL_001c;
		}
	}

IL_0011:
	{
		ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129* L_3 = (ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentNullException_t327031E412FAB2351B0022DD5DAD47E67E597129_il2cpp_TypeInfo_var)));
		NullCheck(L_3);
		ArgumentNullException__ctor_m444AE141157E333844FC1A9500224C2F9FD24F4B(L_3, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralA6EAFAF4139980ACB770E237272125EB9180B7A6)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_3, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357_RuntimeMethod_var)));
	}

IL_001c:
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_4 = ___0_innerStream;
		NullCheck(L_4);
		il2cpp_codegen_runtime_class_init_inline(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var);
		bool L_5;
		L_5 = VirtualFuncInvoker0< bool >::Invoke(7 /* System.Boolean System.IO.Stream::get_CanRead() */, L_4);
		if (!L_5)
		{
			goto IL_002c;
		}
	}
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_6 = ___0_innerStream;
		NullCheck(L_6);
		il2cpp_codegen_runtime_class_init_inline(Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE_il2cpp_TypeInfo_var);
		bool L_7;
		L_7 = VirtualFuncInvoker0< bool >::Invoke(10 /* System.Boolean System.IO.Stream::get_CanWrite() */, L_6);
		if (L_7)
		{
			goto IL_0041;
		}
	}

IL_002c:
	{
		String_t* L_8;
		L_8 = SR_GetString_m2F0EFCFA5FE3DB6DE89C5823284302D691355AA6(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral1F40F39B9F659411903AC93DF93E8658291E5522)), NULL);
		ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263* L_9 = (ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ArgumentException_tAD90411542A20A9C72D5CDA3A84181D8B947A263_il2cpp_TypeInfo_var)));
		NullCheck(L_9);
		ArgumentException__ctor_m8F9D40CE19D19B698A70F9A258640EB52DB39B62(L_9, L_8, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteralA6EAFAF4139980ACB770E237272125EB9180B7A6)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_9, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357_RuntimeMethod_var)));
	}

IL_0041:
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_10 = ___0_innerStream;
		__this->____InnerStream_4 = L_10;
		Il2CppCodeGenWriteBarrier((void**)(&__this->____InnerStream_4), (void*)L_10);
		bool L_11 = ___1_leaveInnerStreamOpen;
		__this->____LeaveStreamOpen_5 = L_11;
		return;
	}
}
// System.IO.Stream System.Net.Security.AuthenticatedStream::get_InnerStream()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7 (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = __this->____InnerStream_4;
		return L_0;
	}
}
// System.Void System.Net.Security.AuthenticatedStream::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void AuthenticatedStream_Dispose_m031F600EE876A06427FB0713D992FF02B9C3320F (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, bool ___0_disposing, const RuntimeMethod* method) 
{
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0025:
			{// begin finally (depth: 1)
				bool L_0 = ___0_disposing;
				Stream_Dispose_m9B37BD21A57F8F2BD20EE353DE14405700810C5C(__this, L_0, NULL);
				return;
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			{
				bool L_1 = ___0_disposing;
				if (!L_1)
				{
					goto IL_0023_1;
				}
			}
			{
				bool L_2 = __this->____LeaveStreamOpen_5;
				if (!L_2)
				{
					goto IL_0018_1;
				}
			}
			{
				Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_3 = __this->____InnerStream_4;
				NullCheck(L_3);
				VirtualActionInvoker0::Invoke(20 /* System.Void System.IO.Stream::Flush() */, L_3);
				goto IL_002d;
			}

IL_0018_1:
			{
				Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_4 = __this->____InnerStream_4;
				NullCheck(L_4);
				VirtualActionInvoker0::Invoke(18 /* System.Void System.IO.Stream::Close() */, L_4);
			}

IL_0023_1:
			{
				goto IL_002d;
			}
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_002d:
	{
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_Multicast(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	il2cpp_array_size_t length = __this->___delegates_13->max_length;
	Delegate_t** delegatesToInvoke = reinterpret_cast<Delegate_t**>(__this->___delegates_13->GetAddressAtUnchecked(0));
	X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* retVal = NULL;
	for (il2cpp_array_size_t i = 0; i < length; i++)
	{
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* currentDelegate = reinterpret_cast<LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859*>(delegatesToInvoke[i]);
		typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
		retVal = ((FunctionPointerType)currentDelegate->___invoke_impl_1)((Il2CppObject*)currentDelegate->___method_code_6, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers, reinterpret_cast<RuntimeMethod*>(currentDelegate->___method_3));
	}
	return retVal;
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenInst(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenStatic(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenStaticInvoker(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	return InvokerFuncInvoker5< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_ClosedStaticInvoker(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	return InvokerFuncInvoker6< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, RuntimeObject*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, __this->___m_target_2, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenVirtual(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return VirtualFuncInvoker4< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke(il2cpp_codegen_method_get_slot(method), ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenInterface(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return InterfaceFuncInvoker4< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke(il2cpp_codegen_method_get_slot(method), il2cpp_codegen_method_get_declaring_type(method), ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenGenericVirtual(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return GenericVirtualFuncInvoker4< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke(method, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenGenericInterface(LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return GenericInterfaceFuncInvoker4< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke(method, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers);
}
// System.Void System.Net.Security.LocalCertificateSelectionCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void LocalCertificateSelectionCallback__ctor_mE4F3A8BFF483881C2634112BB581CA4B07809805 (LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) 
{
	__this->___method_ptr_0 = (intptr_t)il2cpp_codegen_get_virtual_call_method_pointer((RuntimeMethod*)___1_method);
	__this->___method_3 = ___1_method;
	__this->___m_target_2 = ___0_object;
	Il2CppCodeGenWriteBarrier((void**)(&__this->___m_target_2), (void*)___0_object);
	int parameterCount = il2cpp_codegen_method_parameter_count((RuntimeMethod*)___1_method);
	__this->___method_code_6 = (intptr_t)__this;
	if (MethodIsStatic((RuntimeMethod*)___1_method))
	{
		bool isOpen = parameterCount == 5;
		if (il2cpp_codegen_call_method_via_invoker((RuntimeMethod*)___1_method))
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenStaticInvoker;
			else
				__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_ClosedStaticInvoker;
		else
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenStatic;
			else
				{
					__this->___invoke_impl_1 = __this->___method_ptr_0;
					__this->___method_code_6 = (intptr_t)__this->___m_target_2;
				}
	}
	else
	{
		bool isOpen = parameterCount == 4;
		if (isOpen)
		{
			if (__this->___method_is_virtual_12)
			{
				if (il2cpp_codegen_method_is_generic_instance_method((RuntimeMethod*)___1_method))
					if (il2cpp_codegen_method_is_interface_method((RuntimeMethod*)___1_method))
						__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenGenericInterface;
					else
						__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenGenericVirtual;
				else
					if (il2cpp_codegen_method_is_interface_method((RuntimeMethod*)___1_method))
						__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenInterface;
					else
						__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenVirtual;
			}
			else
			{
				__this->___invoke_impl_1 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_OpenInst;
			}
		}
		else
		{
			if (___0_object == NULL)
				il2cpp_codegen_raise_exception(il2cpp_codegen_get_argument_exception(NULL, "Delegate to an instance method cannot have null 'this'."), NULL);
			__this->___invoke_impl_1 = __this->___method_ptr_0;
			__this->___method_code_6 = (intptr_t)__this->___m_target_2;
		}
	}
	__this->___extra_arg_5 = (intptr_t)&LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_Multicast;
}
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.LocalCertificateSelectionCallback::Invoke(System.Object,System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Cryptography.X509Certificates.X509Certificate,System.String[])
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C (LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method) 
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_Multicast(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	il2cpp_array_size_t length = __this->___delegates_13->max_length;
	Delegate_t** delegatesToInvoke = reinterpret_cast<Delegate_t**>(__this->___delegates_13->GetAddressAtUnchecked(0));
	bool retVal = false;
	for (il2cpp_array_size_t i = 0; i < length; i++)
	{
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* currentDelegate = reinterpret_cast<RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955*>(delegatesToInvoke[i]);
		typedef bool (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t, const RuntimeMethod*);
		retVal = ((FunctionPointerType)currentDelegate->___invoke_impl_1)((Il2CppObject*)currentDelegate->___method_code_6, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors, reinterpret_cast<RuntimeMethod*>(currentDelegate->___method_3));
	}
	return retVal;
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenInst(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	typedef bool (*FunctionPointerType) (RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors, method);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenStatic(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	typedef bool (*FunctionPointerType) (RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors, method);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenStaticInvoker(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	return InvokerFuncInvoker4< bool, RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_ClosedStaticInvoker(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	return InvokerFuncInvoker5< bool, RuntimeObject*, RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, __this->___m_target_2, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenVirtual(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return VirtualFuncInvoker3< bool, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke(il2cpp_codegen_method_get_slot(method), ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenInterface(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return InterfaceFuncInvoker3< bool, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke(il2cpp_codegen_method_get_slot(method), il2cpp_codegen_method_get_declaring_type(method), ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenGenericVirtual(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return GenericVirtualFuncInvoker3< bool, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke(method, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenGenericInterface(RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method)
{
	NullCheck(___0_sender);
	return GenericInterfaceFuncInvoker3< bool, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t >::Invoke(method, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors);
}
// System.Void System.Net.Security.RemoteCertificateValidationCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void RemoteCertificateValidationCallback__ctor_mC5C0650D4E5D29475BDBDA5B43FEA44DB0DBF367 (RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) 
{
	__this->___method_ptr_0 = (intptr_t)il2cpp_codegen_get_virtual_call_method_pointer((RuntimeMethod*)___1_method);
	__this->___method_3 = ___1_method;
	__this->___m_target_2 = ___0_object;
	Il2CppCodeGenWriteBarrier((void**)(&__this->___m_target_2), (void*)___0_object);
	int parameterCount = il2cpp_codegen_method_parameter_count((RuntimeMethod*)___1_method);
	__this->___method_code_6 = (intptr_t)__this;
	if (MethodIsStatic((RuntimeMethod*)___1_method))
	{
		bool isOpen = parameterCount == 4;
		if (il2cpp_codegen_call_method_via_invoker((RuntimeMethod*)___1_method))
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenStaticInvoker;
			else
				__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_ClosedStaticInvoker;
		else
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenStatic;
			else
				{
					__this->___invoke_impl_1 = __this->___method_ptr_0;
					__this->___method_code_6 = (intptr_t)__this->___m_target_2;
				}
	}
	else
	{
		bool isOpen = parameterCount == 3;
		if (isOpen)
		{
			if (__this->___method_is_virtual_12)
			{
				if (il2cpp_codegen_method_is_generic_instance_method((RuntimeMethod*)___1_method))
					if (il2cpp_codegen_method_is_interface_method((RuntimeMethod*)___1_method))
						__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenGenericInterface;
					else
						__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenGenericVirtual;
				else
					if (il2cpp_codegen_method_is_interface_method((RuntimeMethod*)___1_method))
						__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenInterface;
					else
						__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenVirtual;
			}
			else
			{
				__this->___invoke_impl_1 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_OpenInst;
			}
		}
		else
		{
			if (___0_object == NULL)
				il2cpp_codegen_raise_exception(il2cpp_codegen_get_argument_exception(NULL, "Delegate to an instance method cannot have null 'this'."), NULL);
			__this->___invoke_impl_1 = __this->___method_ptr_0;
			__this->___method_code_6 = (intptr_t)__this->___m_target_2;
		}
	}
	__this->___extra_arg_5 = (intptr_t)&RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83_Multicast;
}
// System.Boolean System.Net.Security.RemoteCertificateValidationCallback::Invoke(System.Object,System.Security.Cryptography.X509Certificates.X509Certificate,System.Security.Cryptography.X509Certificates.X509Chain,System.Net.Security.SslPolicyErrors)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool RemoteCertificateValidationCallback_Invoke_m5256BF7D3B9BFFFAA959191FD5117C393F670D83 (RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* __this, RuntimeObject* ___0_sender, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___1_certificate, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5* ___2_chain, int32_t ___3_sslPolicyErrors, const RuntimeMethod* method) 
{
	typedef bool (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, X509Chain_t2E7F88A4C97CAB82AF938F13A11354A0475469E5*, int32_t, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_sender, ___1_certificate, ___2_chain, ___3_sslPolicyErrors, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_Multicast(LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method)
{
	il2cpp_array_size_t length = __this->___delegates_13->max_length;
	Delegate_t** delegatesToInvoke = reinterpret_cast<Delegate_t**>(__this->___delegates_13->GetAddressAtUnchecked(0));
	X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* retVal = NULL;
	for (il2cpp_array_size_t i = 0; i < length; i++)
	{
		LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* currentDelegate = reinterpret_cast<LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2*>(delegatesToInvoke[i]);
		typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
		retVal = ((FunctionPointerType)currentDelegate->___invoke_impl_1)((Il2CppObject*)currentDelegate->___method_code_6, ___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers, reinterpret_cast<RuntimeMethod*>(currentDelegate->___method_3));
	}
	return retVal;
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenInst(LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method)
{
	NullCheck(___0_targetHost);
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenStatic(LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method)
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenStaticInvoker(LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method)
{
	return InvokerFuncInvoker4< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, ___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_ClosedStaticInvoker(LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method)
{
	return InvokerFuncInvoker5< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, __this->___m_target_2, ___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers);
}
// System.Void System.Net.Security.LocalCertSelectionCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void LocalCertSelectionCallback__ctor_mA7B36924E69A918A71649CFF95CB361E33317C38 (LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) 
{
	__this->___method_ptr_0 = (intptr_t)il2cpp_codegen_get_virtual_call_method_pointer((RuntimeMethod*)___1_method);
	__this->___method_3 = ___1_method;
	__this->___m_target_2 = ___0_object;
	Il2CppCodeGenWriteBarrier((void**)(&__this->___m_target_2), (void*)___0_object);
	int parameterCount = il2cpp_codegen_method_parameter_count((RuntimeMethod*)___1_method);
	__this->___method_code_6 = (intptr_t)__this;
	if (MethodIsStatic((RuntimeMethod*)___1_method))
	{
		bool isOpen = parameterCount == 4;
		if (il2cpp_codegen_call_method_via_invoker((RuntimeMethod*)___1_method))
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenStaticInvoker;
			else
				__this->___invoke_impl_1 = (intptr_t)&LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_ClosedStaticInvoker;
		else
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenStatic;
			else
				{
					__this->___invoke_impl_1 = __this->___method_ptr_0;
					__this->___method_code_6 = (intptr_t)__this->___m_target_2;
				}
	}
	else
	{
		bool isOpen = parameterCount == 3;
		if (isOpen)
		{
			__this->___invoke_impl_1 = (intptr_t)&LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_OpenInst;
		}
		else
		{
			if (___0_object == NULL)
				il2cpp_codegen_raise_exception(il2cpp_codegen_get_argument_exception(NULL, "Delegate to an instance method cannot have null 'this'."), NULL);
			__this->___invoke_impl_1 = __this->___method_ptr_0;
			__this->___method_code_6 = (intptr_t)__this->___m_target_2;
		}
	}
	__this->___extra_arg_5 = (intptr_t)&LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9_Multicast;
}
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.LocalCertSelectionCallback::Invoke(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Cryptography.X509Certificates.X509Certificate,System.String[])
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertSelectionCallback_Invoke_m3AD956B5D53937117BD3E036068B49615337B5B9 (LocalCertSelectionCallback_t3AB79A8AA642074A3EF886118963EEA3CCABABE2* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_acceptableIssuers, const RuntimeMethod* method) 
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_targetHost, ___1_localCertificates, ___2_remoteCertificate, ___3_acceptableIssuers, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_Multicast(ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method)
{
	il2cpp_array_size_t length = __this->___delegates_13->max_length;
	Delegate_t** delegatesToInvoke = reinterpret_cast<Delegate_t**>(__this->___delegates_13->GetAddressAtUnchecked(0));
	X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* retVal = NULL;
	for (il2cpp_array_size_t i = 0; i < length; i++)
	{
		ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* currentDelegate = reinterpret_cast<ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654*>(delegatesToInvoke[i]);
		typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, const RuntimeMethod*);
		retVal = ((FunctionPointerType)currentDelegate->___invoke_impl_1)((Il2CppObject*)currentDelegate->___method_code_6, ___0_hostName, reinterpret_cast<RuntimeMethod*>(currentDelegate->___method_3));
	}
	return retVal;
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenInst(ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method)
{
	NullCheck(___0_hostName);
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (String_t*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_hostName, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenStatic(ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method)
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (String_t*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___method_ptr_0)(___0_hostName, method);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenStaticInvoker(ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method)
{
	return InvokerFuncInvoker1< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, String_t* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, ___0_hostName);
}
X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_ClosedStaticInvoker(ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method)
{
	return InvokerFuncInvoker2< X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, RuntimeObject*, String_t* >::Invoke((Il2CppMethodPointer)__this->___method_ptr_0, method, NULL, __this->___m_target_2, ___0_hostName);
}
// System.Void System.Net.Security.ServerCertSelectionCallback::.ctor(System.Object,System.IntPtr)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ServerCertSelectionCallback__ctor_m06DDCEDF49D7A5F0F8494E3E960C7DFC64C64946 (ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, RuntimeObject* ___0_object, intptr_t ___1_method, const RuntimeMethod* method) 
{
	__this->___method_ptr_0 = (intptr_t)il2cpp_codegen_get_virtual_call_method_pointer((RuntimeMethod*)___1_method);
	__this->___method_3 = ___1_method;
	__this->___m_target_2 = ___0_object;
	Il2CppCodeGenWriteBarrier((void**)(&__this->___m_target_2), (void*)___0_object);
	int parameterCount = il2cpp_codegen_method_parameter_count((RuntimeMethod*)___1_method);
	__this->___method_code_6 = (intptr_t)__this;
	if (MethodIsStatic((RuntimeMethod*)___1_method))
	{
		bool isOpen = parameterCount == 1;
		if (il2cpp_codegen_call_method_via_invoker((RuntimeMethod*)___1_method))
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenStaticInvoker;
			else
				__this->___invoke_impl_1 = (intptr_t)&ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_ClosedStaticInvoker;
		else
			if (isOpen)
				__this->___invoke_impl_1 = (intptr_t)&ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenStatic;
			else
				{
					__this->___invoke_impl_1 = __this->___method_ptr_0;
					__this->___method_code_6 = (intptr_t)__this->___m_target_2;
				}
	}
	else
	{
		bool isOpen = parameterCount == 0;
		if (isOpen)
		{
			__this->___invoke_impl_1 = (intptr_t)&ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_OpenInst;
		}
		else
		{
			if (___0_object == NULL)
				il2cpp_codegen_raise_exception(il2cpp_codegen_get_argument_exception(NULL, "Delegate to an instance method cannot have null 'this'."), NULL);
			__this->___invoke_impl_1 = __this->___method_ptr_0;
			__this->___method_code_6 = (intptr_t)__this->___m_target_2;
		}
	}
	__this->___extra_arg_5 = (intptr_t)&ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1_Multicast;
}
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.ServerCertSelectionCallback::Invoke(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ServerCertSelectionCallback_Invoke_mB6A207E1A8CA76F667A242B68943297741CC6EC1 (ServerCertSelectionCallback_t653386CAEAE0236FCF61A92963AB1646BB23C654* __this, String_t* ___0_hostName, const RuntimeMethod* method) 
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, String_t*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_hostName, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// Mono.Net.Security.MobileAuthenticatedStream System.Net.Security.SslStream::get_Impl()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95(__this, NULL);
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0 = __this->___impl_10;
		return L_0;
	}
}
// System.String System.Net.Security.SslStream::get_InternalTargetHost()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR String_t* SslStream_get_InternalTargetHost_m9434AC1422D463C25B5C5313AC9BE45B095136AE (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95(__this, NULL);
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0 = __this->___impl_10;
		NullCheck(L_0);
		String_t* L_1;
		L_1 = MobileAuthenticatedStream_get_TargetHost_m572DA701CB3CE3FA4006D5745F9A3B9A52739CAB_inline(L_0, NULL);
		return L_1;
	}
}
// Mono.Net.Security.MobileTlsProvider System.Net.Security.SslStream::GetProvider()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* SslStream_GetProvider_mE1D070C13DB759156659F57AB9DF4D2E397FD85B (const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E* L_0;
		L_0 = MonoTlsProviderFactory_GetProvider_m8684E3A1AFB043FA00DEC4BCF95F8B288C136936(NULL);
		return ((MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017*)CastclassClass((RuntimeObject*)L_0, MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var));
	}
}
// System.Void System.Net.Security.SslStream::.ctor(System.IO.Stream,System.Boolean,System.Net.Security.RemoteCertificateValidationCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream__ctor_m9096E6E85E60C4D205A26FA43D121FECEC735484 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___2_userCertificateValidationCallback, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = ___0_innerStream;
		bool L_1 = ___1_leaveInnerStreamOpen;
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_2 = ___2_userCertificateValidationCallback;
		SslStream__ctor_mB4497903594E8A33C14B9A4C4DAB7B16BA2E9B2C(__this, L_0, L_1, L_2, (LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859*)NULL, NULL);
		return;
	}
}
// System.Void System.Net.Security.SslStream::.ctor(System.IO.Stream,System.Boolean,System.Net.Security.RemoteCertificateValidationCallback,System.Net.Security.LocalCertificateSelectionCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream__ctor_mB4497903594E8A33C14B9A4C4DAB7B16BA2E9B2C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___2_userCertificateValidationCallback, LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___3_userCertificateSelectionCallback, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = ___0_innerStream;
		bool L_1 = ___1_leaveInnerStreamOpen;
		AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357(__this, L_0, L_1, NULL);
		MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* L_2;
		L_2 = SslStream_GetProvider_mE1D070C13DB759156659F57AB9DF4D2E397FD85B(NULL);
		__this->___provider_6 = L_2;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___provider_6), (void*)L_2);
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_3;
		L_3 = MonoTlsSettings_CopyDefaultSettings_m4B0A3E8B7D106FA7F0D243FB2A0A4B115CD21942(NULL);
		__this->___settings_7 = L_3;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___settings_7), (void*)L_3);
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_4 = ___2_userCertificateValidationCallback;
		SslStream_SetAndVerifyValidationCallback_m6FA4BE931FA1C7BC5D175B4E1CA2959742FA9C9D(__this, L_4, NULL);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_5 = ___3_userCertificateSelectionCallback;
		SslStream_SetAndVerifySelectionCallback_m8B82CA433CF92AD113F606ACE7229D442587DA0B(__this, L_5, NULL);
		MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* L_6 = __this->___provider_6;
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_7 = ___0_innerStream;
		bool L_8 = ___1_leaveInnerStreamOpen;
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_9 = __this->___settings_7;
		NullCheck(L_6);
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_10;
		L_10 = VirtualFuncInvoker4< MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E*, SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27*, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE*, bool, MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* >::Invoke(11 /* Mono.Net.Security.MobileAuthenticatedStream Mono.Net.Security.MobileTlsProvider::CreateSslStream(System.Net.Security.SslStream,System.IO.Stream,System.Boolean,Mono.Security.Interface.MonoTlsSettings) */, L_6, __this, L_7, L_8, L_9);
		__this->___impl_10 = L_10;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___impl_10), (void*)L_10);
		return;
	}
}
// System.Void System.Net.Security.SslStream::.ctor(System.IO.Stream,System.Boolean,Mono.Security.Interface.MonoTlsProvider,Mono.Security.Interface.MonoTlsSettings)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream__ctor_m222C4F58E1CBA2382263444001AA70862B2EC462 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* ___0_innerStream, bool ___1_leaveInnerStreamOpen, MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E* ___2_provider, MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* ___3_settings, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = ___0_innerStream;
		bool L_1 = ___1_leaveInnerStreamOpen;
		AuthenticatedStream__ctor_m8B163146A01B938A3E87E1BED07EE07494A3E357(__this, L_0, L_1, NULL);
		MonoTlsProvider_t39C898CDC9458EEAD7C019B4B23701EAF9E24F7E* L_2 = ___2_provider;
		__this->___provider_6 = ((MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017*)CastclassClass((RuntimeObject*)L_2, MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var));
		Il2CppCodeGenWriteBarrier((void**)(&__this->___provider_6), (void*)((MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017*)CastclassClass((RuntimeObject*)L_2, MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017_il2cpp_TypeInfo_var)));
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_3 = ___3_settings;
		NullCheck(L_3);
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_4;
		L_4 = MonoTlsSettings_Clone_mC4F9A27889ADD0B275018B32BCDA67C30865EA7D(L_3, NULL);
		__this->___settings_7 = L_4;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___settings_7), (void*)L_4);
		__this->___explicitSettings_11 = (bool)1;
		MobileTlsProvider_tD60D82BEBF267F50F388A026DBB092C7188BB017* L_5 = __this->___provider_6;
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_6 = ___0_innerStream;
		bool L_7 = ___1_leaveInnerStreamOpen;
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_8 = ___3_settings;
		NullCheck(L_5);
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_9;
		L_9 = VirtualFuncInvoker4< MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E*, SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27*, Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE*, bool, MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* >::Invoke(11 /* Mono.Net.Security.MobileAuthenticatedStream Mono.Net.Security.MobileTlsProvider::CreateSslStream(System.Net.Security.SslStream,System.IO.Stream,System.Boolean,Mono.Security.Interface.MonoTlsSettings) */, L_5, __this, L_6, L_7, L_8);
		__this->___impl_10 = L_9;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___impl_10), (void*)L_9);
		return;
	}
}
// System.Void System.Net.Security.SslStream::SetAndVerifyValidationCallback(System.Net.Security.RemoteCertificateValidationCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_SetAndVerifyValidationCallback_m6FA4BE931FA1C7BC5D175B4E1CA2959742FA9C9D (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* ___0_callback, const RuntimeMethod* method) 
{
	{
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_0 = __this->___validationCallback_8;
		if (L_0)
		{
			goto IL_0021;
		}
	}
	{
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_1 = ___0_callback;
		__this->___validationCallback_8 = L_1;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___validationCallback_8), (void*)L_1);
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_2 = __this->___settings_7;
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_3 = ___0_callback;
		MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* L_4;
		L_4 = CallbackHelpers_PublicToMono_m6404DAFAD159E4D91FB5822B4855B6A8F05777EE(L_3, NULL);
		NullCheck(L_2);
		MonoTlsSettings_set_RemoteCertificateValidationCallback_m6CEA8A6E38C85A96C2D26613407C13DD4F965C87_inline(L_2, L_4, NULL);
		return;
	}

IL_0021:
	{
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_5 = ___0_callback;
		if (!L_5)
		{
			goto IL_0032;
		}
	}
	{
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_6 = __this->___validationCallback_8;
		RemoteCertificateValidationCallback_t2F4C5801F96B2C2BF934511796C5BFEAEBF01955* L_7 = ___0_callback;
		bool L_8;
		L_8 = Delegate_op_Inequality_mA9EAADBA0C976289CCD49DC5A4BEDBB060B579E0(L_6, L_7, NULL);
		if (L_8)
		{
			goto IL_0049;
		}
	}

IL_0032:
	{
		bool L_9 = __this->___explicitSettings_11;
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_10 = __this->___settings_7;
		NullCheck(L_10);
		MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* L_11;
		L_11 = MonoTlsSettings_get_RemoteCertificateValidationCallback_mE07825B4A75DAE2A4BB5037D504A36311814446C_inline(L_10, NULL);
		if (!((int32_t)((int32_t)L_9&((!(((RuntimeObject*)(MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0*)L_11) <= ((RuntimeObject*)(RuntimeObject*)NULL)))? 1 : 0))))
		{
			goto IL_005e;
		}
	}

IL_0049:
	{
		String_t* L_12;
		L_12 = SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral94EC39D9CD015C12CA91AF28F761F4059A1C6437)), ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral6F581DC31922CB8BA16D8D1268547106C0D2A599)), NULL);
		InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB* L_13 = (InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB_il2cpp_TypeInfo_var)));
		NullCheck(L_13);
		InvalidOperationException__ctor_mE4CB6F4712AB6D99A2358FBAE2E052B3EE976162(L_13, L_12, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_13, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslStream_SetAndVerifyValidationCallback_m6FA4BE931FA1C7BC5D175B4E1CA2959742FA9C9D_RuntimeMethod_var)));
	}

IL_005e:
	{
		return;
	}
}
// System.Void System.Net.Security.SslStream::SetAndVerifySelectionCallback(System.Net.Security.LocalCertificateSelectionCallback)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_SetAndVerifySelectionCallback_m8B82CA433CF92AD113F606ACE7229D442587DA0B (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* ___0_callback, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3_il2cpp_TypeInfo_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&U3CU3Ec__DisplayClass21_0_U3CSetAndVerifySelectionCallbackU3Eb__0_mADCFBC85C66A800C57CD2CECA4CDB6A9E5BB020D_RuntimeMethod_var);
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* V_0 = NULL;
	{
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_0 = (U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C*)il2cpp_codegen_object_new(U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C_il2cpp_TypeInfo_var);
		NullCheck(L_0);
		U3CU3Ec__DisplayClass21_0__ctor_mBE4019B5D6D20FC100DAF52B1172ACF497046370(L_0, NULL);
		V_0 = L_0;
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_1 = V_0;
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_2 = ___0_callback;
		NullCheck(L_1);
		L_1->___callback_0 = L_2;
		Il2CppCodeGenWriteBarrier((void**)(&L_1->___callback_0), (void*)L_2);
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_3 = V_0;
		NullCheck(L_3);
		L_3->___U3CU3E4__this_1 = __this;
		Il2CppCodeGenWriteBarrier((void**)(&L_3->___U3CU3E4__this_1), (void*)__this);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_4 = __this->___selectionCallback_9;
		if (L_4)
		{
			goto IL_0055;
		}
	}
	{
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_5 = V_0;
		NullCheck(L_5);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_6 = L_5->___callback_0;
		__this->___selectionCallback_9 = L_6;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___selectionCallback_9), (void*)L_6);
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_7 = V_0;
		NullCheck(L_7);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_8 = L_7->___callback_0;
		if (L_8)
		{
			goto IL_003d;
		}
	}
	{
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_9 = __this->___settings_7;
		NullCheck(L_9);
		MonoTlsSettings_set_ClientCertificateSelectionCallback_mB404DFD0C0475254CC129740A472D6D9615C56FD_inline(L_9, (MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3*)NULL, NULL);
		return;
	}

IL_003d:
	{
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_10 = __this->___settings_7;
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_11 = V_0;
		MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* L_12 = (MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3*)il2cpp_codegen_object_new(MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3_il2cpp_TypeInfo_var);
		NullCheck(L_12);
		MonoLocalCertificateSelectionCallback__ctor_mCA81824D698BD5808E501A9AC4DA99758B69D3FC(L_12, L_11, (intptr_t)((void*)U3CU3Ec__DisplayClass21_0_U3CSetAndVerifySelectionCallbackU3Eb__0_mADCFBC85C66A800C57CD2CECA4CDB6A9E5BB020D_RuntimeMethod_var), NULL);
		NullCheck(L_10);
		MonoTlsSettings_set_ClientCertificateSelectionCallback_mB404DFD0C0475254CC129740A472D6D9615C56FD_inline(L_10, L_12, NULL);
		return;
	}

IL_0055:
	{
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_13 = V_0;
		NullCheck(L_13);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_14 = L_13->___callback_0;
		if (!L_14)
		{
			goto IL_0070;
		}
	}
	{
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_15 = __this->___selectionCallback_9;
		U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* L_16 = V_0;
		NullCheck(L_16);
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_17 = L_16->___callback_0;
		bool L_18;
		L_18 = Delegate_op_Inequality_mA9EAADBA0C976289CCD49DC5A4BEDBB060B579E0(L_15, L_17, NULL);
		if (L_18)
		{
			goto IL_0085;
		}
	}

IL_0070:
	{
		bool L_19 = __this->___explicitSettings_11;
		if (!L_19)
		{
			goto IL_009a;
		}
	}
	{
		MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* L_20 = __this->___settings_7;
		NullCheck(L_20);
		MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* L_21;
		L_21 = MonoTlsSettings_get_ClientCertificateSelectionCallback_mCFE63487D867109AD1AF856ECC8BA0996C0AA605_inline(L_20, NULL);
		if (!L_21)
		{
			goto IL_009a;
		}
	}

IL_0085:
	{
		String_t* L_22;
		L_22 = SR_Format_m9E8DC9AEFDC34AC67473EFAEAB78C5066C1A0D09(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral94EC39D9CD015C12CA91AF28F761F4059A1C6437)), ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral7432BC139FBCA09AC74A0F2BAE3FBB728A59E72D)), NULL);
		InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB* L_23 = (InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&InvalidOperationException_t5DDE4D49B7405FAAB1E4576F4715A42A3FAD4BAB_il2cpp_TypeInfo_var)));
		NullCheck(L_23);
		InvalidOperationException__ctor_mE4CB6F4712AB6D99A2358FBAE2E052B3EE976162(L_23, L_22, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_23, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslStream_SetAndVerifySelectionCallback_m8B82CA433CF92AD113F606ACE7229D442587DA0B_RuntimeMethod_var)));
	}

IL_009a:
	{
		return;
	}
}
// System.Void System.Net.Security.SslStream::AuthenticateAsClient(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Authentication.SslProtocols,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_AuthenticateAsClient_mB88736B524C5E2095C51B96634E38E38D759853C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_clientCertificates, int32_t ___2_enabledSslProtocols, bool ___3_checkCertificateRevocation, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		String_t* L_1 = ___0_targetHost;
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_2 = ___1_clientCertificates;
		int32_t L_3 = ___2_enabledSslProtocols;
		bool L_4 = ___3_checkCertificateRevocation;
		NullCheck(L_0);
		MobileAuthenticatedStream_AuthenticateAsClient_mEB1237B52A4AB1FF3D7D3E52F61CE78A744973FC(L_0, L_1, L_2, L_3, L_4, NULL);
		return;
	}
}
// System.IAsyncResult System.Net.Security.SslStream::BeginAuthenticateAsClient(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Authentication.SslProtocols,System.Boolean,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* SslStream_BeginAuthenticateAsClient_mC26B4A47157FFFB8015EB46AB417452E886A215F (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_clientCertificates, int32_t ___2_enabledSslProtocols, bool ___3_checkCertificateRevocation, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___4_asyncCallback, RuntimeObject* ___5_asyncState, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		String_t* L_1 = ___0_targetHost;
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_2 = ___1_clientCertificates;
		int32_t L_3 = ___2_enabledSslProtocols;
		bool L_4 = ___3_checkCertificateRevocation;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_5;
		L_5 = MobileAuthenticatedStream_AuthenticateAsClientAsync_mBD8A0E7FFAFDE608063E545EF057304AF2C35EF2(L_0, L_1, L_2, L_3, L_4, NULL);
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_6 = ___4_asyncCallback;
		RuntimeObject* L_7 = ___5_asyncState;
		RuntimeObject* L_8;
		L_8 = TaskToApm_Begin_mA26D2A4F6EE0582B181AFD83B913916909D02777(L_5, L_6, L_7, NULL);
		return L_8;
	}
}
// System.Void System.Net.Security.SslStream::EndAuthenticateAsClient(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_EndAuthenticateAsClient_mE0D4B8AC81E851403DDA1819C19539F94386D1FE (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = ___0_asyncResult;
		TaskToApm_End_mDFB29EEE501409834D464F4C249A535723B7E6ED(L_0, NULL);
		return;
	}
}
// System.Threading.Tasks.Task System.Net.Security.SslStream::AuthenticateAsClientAsync(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Authentication.SslProtocols,System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* SslStream_AuthenticateAsClientAsync_mAD6DEB8ADE693F969D039B19C19A85F13A2DCB23 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, String_t* ___0_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_clientCertificates, int32_t ___2_enabledSslProtocols, bool ___3_checkCertificateRevocation, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		String_t* L_1 = ___0_targetHost;
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_2 = ___1_clientCertificates;
		int32_t L_3 = ___2_enabledSslProtocols;
		bool L_4 = ___3_checkCertificateRevocation;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_5;
		L_5 = MobileAuthenticatedStream_AuthenticateAsClientAsync_mBD8A0E7FFAFDE608063E545EF057304AF2C35EF2(L_0, L_1, L_2, L_3, L_4, NULL);
		return L_5;
	}
}
// System.Boolean System.Net.Security.SslStream::get_IsAuthenticated()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SslStream_get_IsAuthenticated_m5056C714711944DA54B35F6F2A3025A720B49290 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		bool L_1;
		L_1 = VirtualFuncInvoker0< bool >::Invoke(37 /* System.Boolean System.Net.Security.AuthenticatedStream::get_IsAuthenticated() */, L_0);
		return L_1;
	}
}
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.SslStream::get_LocalCertificate()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* SslStream_get_LocalCertificate_mA79551323D9A742DA85F9377868860CDABB3C1AA (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* L_1;
		L_1 = MobileAuthenticatedStream_get_LocalCertificate_m59FF8438D988F2CE633B454CE2B54D614E6C50EF(L_0, NULL);
		return L_1;
	}
}
// System.Boolean System.Net.Security.SslStream::get_CanSeek()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SslStream_get_CanSeek_mA98FD41EDB70B03DB44F714CE952E128CA9D9B4B (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		return (bool)0;
	}
}
// System.Boolean System.Net.Security.SslStream::get_CanRead()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SslStream_get_CanRead_m0B2ED5711B4D7C796529F56D7039AD546571FEF2 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0 = __this->___impl_10;
		if (!L_0)
		{
			goto IL_0014;
		}
	}
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_1 = __this->___impl_10;
		NullCheck(L_1);
		bool L_2;
		L_2 = VirtualFuncInvoker0< bool >::Invoke(7 /* System.Boolean System.IO.Stream::get_CanRead() */, L_1);
		return L_2;
	}

IL_0014:
	{
		return (bool)0;
	}
}
// System.Boolean System.Net.Security.SslStream::get_CanTimeout()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SslStream_get_CanTimeout_m92F31A9B8D6C1010B59BE8FECD10CFA3B880FEFF (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0;
		L_0 = AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7_inline(__this, NULL);
		NullCheck(L_0);
		bool L_1;
		L_1 = VirtualFuncInvoker0< bool >::Invoke(9 /* System.Boolean System.IO.Stream::get_CanTimeout() */, L_0);
		return L_1;
	}
}
// System.Boolean System.Net.Security.SslStream::get_CanWrite()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR bool SslStream_get_CanWrite_mCBCABFAC940539066F34E4480E52E07CA06DA7CE (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0 = __this->___impl_10;
		if (!L_0)
		{
			goto IL_0014;
		}
	}
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_1 = __this->___impl_10;
		NullCheck(L_1);
		bool L_2;
		L_2 = VirtualFuncInvoker0< bool >::Invoke(10 /* System.Boolean System.IO.Stream::get_CanWrite() */, L_1);
		return L_2;
	}

IL_0014:
	{
		return (bool)0;
	}
}
// System.Int32 System.Net.Security.SslStream::get_ReadTimeout()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SslStream_get_ReadTimeout_m75B9430F39F610082318B1D37925CA4B651F1E12 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		int32_t L_1;
		L_1 = VirtualFuncInvoker0< int32_t >::Invoke(14 /* System.Int32 System.IO.Stream::get_ReadTimeout() */, L_0);
		return L_1;
	}
}
// System.Void System.Net.Security.SslStream::set_ReadTimeout(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_set_ReadTimeout_m0043E5CDEDC5690A118A429F79CF54DC071F895F (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		int32_t L_1 = ___0_value;
		NullCheck(L_0);
		VirtualActionInvoker1< int32_t >::Invoke(15 /* System.Void System.IO.Stream::set_ReadTimeout(System.Int32) */, L_0, L_1);
		return;
	}
}
// System.Int32 System.Net.Security.SslStream::get_WriteTimeout()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SslStream_get_WriteTimeout_mDC207A2CBAD4D969AC4924A0704242936086D0FF (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		int32_t L_1;
		L_1 = VirtualFuncInvoker0< int32_t >::Invoke(16 /* System.Int32 System.IO.Stream::get_WriteTimeout() */, L_0);
		return L_1;
	}
}
// System.Void System.Net.Security.SslStream::set_WriteTimeout(System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_set_WriteTimeout_m316C6CCEC0BE604E8D0A05328FBAB4D946CF3409 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		int32_t L_1 = ___0_value;
		NullCheck(L_0);
		VirtualActionInvoker1< int32_t >::Invoke(17 /* System.Void System.IO.Stream::set_WriteTimeout(System.Int32) */, L_0, L_1);
		return;
	}
}
// System.Int64 System.Net.Security.SslStream::get_Length()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int64_t SslStream_get_Length_mD6515D77DBB57912C598761F67C321B6B16E129C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		int64_t L_1;
		L_1 = VirtualFuncInvoker0< int64_t >::Invoke(11 /* System.Int64 System.IO.Stream::get_Length() */, L_0);
		return L_1;
	}
}
// System.Int64 System.Net.Security.SslStream::get_Position()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int64_t SslStream_get_Position_mD2DBB36BA22F4620B635557B5748E0928F28B6A2 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		NullCheck(L_0);
		int64_t L_1;
		L_1 = VirtualFuncInvoker0< int64_t >::Invoke(12 /* System.Int64 System.IO.Stream::get_Position() */, L_0);
		return L_1;
	}
}
// System.Void System.Net.Security.SslStream::set_Position(System.Int64)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_set_Position_m70987A87C7DB346D2FD447EA11E96774105EE5D2 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, int64_t ___0_value, const RuntimeMethod* method) 
{
	{
		String_t* L_0;
		L_0 = SR_GetString_m2F0EFCFA5FE3DB6DE89C5823284302D691355AA6(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral9A1C57A14DBAA067A98C8B33FA6B28D793AFE67D)), NULL);
		NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A* L_1 = (NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A_il2cpp_TypeInfo_var)));
		NullCheck(L_1);
		NotSupportedException__ctor_mE174750CF0247BBB47544FFD71D66BB89630945B(L_1, L_0, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_1, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslStream_set_Position_m70987A87C7DB346D2FD447EA11E96774105EE5D2_RuntimeMethod_var)));
	}
}
// System.Int64 System.Net.Security.SslStream::Seek(System.Int64,System.IO.SeekOrigin)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int64_t SslStream_Seek_mFEDBF392998DDDB0F7A71F25D1BB821EA1463B25 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, int64_t ___0_offset, int32_t ___1_origin, const RuntimeMethod* method) 
{
	{
		String_t* L_0;
		L_0 = SR_GetString_m2F0EFCFA5FE3DB6DE89C5823284302D691355AA6(((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral9A1C57A14DBAA067A98C8B33FA6B28D793AFE67D)), NULL);
		NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A* L_1 = (NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&NotSupportedException_t1429765983D409BD2986508963C98D214E4EBF4A_il2cpp_TypeInfo_var)));
		NullCheck(L_1);
		NotSupportedException__ctor_mE174750CF0247BBB47544FFD71D66BB89630945B(L_1, L_0, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_1, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslStream_Seek_mFEDBF392998DDDB0F7A71F25D1BB821EA1463B25_RuntimeMethod_var)));
	}
}
// System.Threading.Tasks.Task System.Net.Security.SslStream::FlushAsync(System.Threading.CancellationToken)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* SslStream_FlushAsync_mF90FEDB78373A361409477F77B3B6F1BBB8F6818 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED ___0_cancellationToken, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0;
		L_0 = AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7_inline(__this, NULL);
		CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED L_1 = ___0_cancellationToken;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_2;
		L_2 = VirtualFuncInvoker1< Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572*, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED >::Invoke(21 /* System.Threading.Tasks.Task System.IO.Stream::FlushAsync(System.Threading.CancellationToken) */, L_0, L_1);
		return L_2;
	}
}
// System.Void System.Net.Security.SslStream::Flush()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_Flush_m61B328FCFEB939DE23740823CE3EB7E8DF3B14A4 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0;
		L_0 = AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7_inline(__this, NULL);
		NullCheck(L_0);
		VirtualActionInvoker0::Invoke(20 /* System.Void System.IO.Stream::Flush() */, L_0);
		return;
	}
}
// System.Void System.Net.Security.SslStream::CheckDisposed()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0 = __this->___impl_10;
		if (L_0)
		{
			goto IL_0013;
		}
	}
	{
		ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB* L_1 = (ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ObjectDisposedException_tC5FB29E8E980E2010A2F6A5B9B791089419F89EB_il2cpp_TypeInfo_var)));
		NullCheck(L_1);
		ObjectDisposedException__ctor_mB2C8582279AF3F0C1CF9AA52DA7331BF848DFD48(L_1, ((String_t*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&_stringLiteral6FE6AA6EF03FB6550152903A7B201A6108F728DB)), NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_1, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&SslStream_CheckDisposed_m884E18F119B366EE6734CCFA126A1F633687FA95_RuntimeMethod_var)));
	}

IL_0013:
	{
		return;
	}
}
// System.Void System.Net.Security.SslStream::Dispose(System.Boolean)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_Dispose_m187B76F54E2471469F5B62B8DDB33E11E40EE397 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, bool ___0_disposing, const RuntimeMethod* method) 
{
	{
		auto __finallyBlock = il2cpp::utils::Finally([&]
		{

FINALLY_0021:
			{// begin finally (depth: 1)
				bool L_0 = ___0_disposing;
				AuthenticatedStream_Dispose_m031F600EE876A06427FB0713D992FF02B9C3320F(__this, L_0, NULL);
				return;
			}// end finally (depth: 1)
		});
		try
		{// begin try (depth: 1)
			{
				MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_1 = __this->___impl_10;
				bool L_2 = ___0_disposing;
				if (!((int32_t)(((!(((RuntimeObject*)(MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E*)L_1) <= ((RuntimeObject*)(RuntimeObject*)NULL)))? 1 : 0)&(int32_t)L_2)))
				{
					goto IL_001f_1;
				}
			}
			{
				MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_3 = __this->___impl_10;
				NullCheck(L_3);
				Stream_Dispose_mCDB42F32A17541CCA6D3A5906827A401570B07A8(L_3, NULL);
				__this->___impl_10 = (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E*)NULL;
				Il2CppCodeGenWriteBarrier((void**)(&__this->___impl_10), (void*)(MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E*)NULL);
			}

IL_001f_1:
			{
				goto IL_0029;
			}
		}// end try (depth: 1)
		catch(Il2CppExceptionWrapper& e)
		{
			__finallyBlock.StoreException(e.ex);
		}
	}

IL_0029:
	{
		return;
	}
}
// System.Int32 System.Net.Security.SslStream::Read(System.Byte[],System.Int32,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SslStream_Read_mEAFC2AA192D9EAF401E2B067395EA08EDC5FB47E (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		NullCheck(L_0);
		int32_t L_4;
		L_4 = VirtualFuncInvoker3< int32_t, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*, int32_t, int32_t >::Invoke(31 /* System.Int32 System.IO.Stream::Read(System.Byte[],System.Int32,System.Int32) */, L_0, L_1, L_2, L_3);
		return L_4;
	}
}
// System.Void System.Net.Security.SslStream::Write(System.Byte[],System.Int32,System.Int32)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_Write_mCF650FDF725E1AAEDC462A016A38E535F7210F3C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		NullCheck(L_0);
		VirtualActionInvoker3< ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*, int32_t, int32_t >::Invoke(34 /* System.Void System.IO.Stream::Write(System.Byte[],System.Int32,System.Int32) */, L_0, L_1, L_2, L_3);
		return;
	}
}
// System.Threading.Tasks.Task`1<System.Int32> System.Net.Security.SslStream::ReadAsync(System.Byte[],System.Int32,System.Int32,System.Threading.CancellationToken)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_1_t4C228DE57804012969575431CFF12D57C875552D* SslStream_ReadAsync_m0F251A72A8D1C8C41A49CC99AE55D8D0F96A956C (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED ___3_cancellationToken, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED L_4 = ___3_cancellationToken;
		NullCheck(L_0);
		Task_1_t4C228DE57804012969575431CFF12D57C875552D* L_5;
		L_5 = VirtualFuncInvoker4< Task_1_t4C228DE57804012969575431CFF12D57C875552D*, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*, int32_t, int32_t, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED >::Invoke(24 /* System.Threading.Tasks.Task`1<System.Int32> System.IO.Stream::ReadAsync(System.Byte[],System.Int32,System.Int32,System.Threading.CancellationToken) */, L_0, L_1, L_2, L_3, L_4);
		return L_5;
	}
}
// System.Threading.Tasks.Task System.Net.Security.SslStream::WriteAsync(System.Byte[],System.Int32,System.Int32,System.Threading.CancellationToken)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* SslStream_WriteAsync_m8211FC8F3FF4552E897516BE6CD70923848B89C3 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED ___3_cancellationToken, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED L_4 = ___3_cancellationToken;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_5;
		L_5 = VirtualFuncInvoker4< Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572*, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031*, int32_t, int32_t, CancellationToken_t51142D9C6D7C02D314DA34A6A7988C528992FFED >::Invoke(28 /* System.Threading.Tasks.Task System.IO.Stream::WriteAsync(System.Byte[],System.Int32,System.Int32,System.Threading.CancellationToken) */, L_0, L_1, L_2, L_3, L_4);
		return L_5;
	}
}
// System.IAsyncResult System.Net.Security.SslStream::BeginRead(System.Byte[],System.Int32,System.Int32,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* SslStream_BeginRead_m716BC242891C530ECB08DA4989AE27EEBAC71C98 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___3_callback, RuntimeObject* ___4_state, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		NullCheck(L_0);
		Task_1_t4C228DE57804012969575431CFF12D57C875552D* L_4;
		L_4 = Stream_ReadAsync_m734E5C146A1217C9E8FEC56ABDBD2AC33F5F8F87(L_0, L_1, L_2, L_3, NULL);
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_5 = ___3_callback;
		RuntimeObject* L_6 = ___4_state;
		RuntimeObject* L_7;
		L_7 = TaskToApm_Begin_mA26D2A4F6EE0582B181AFD83B913916909D02777(L_4, L_5, L_6, NULL);
		return L_7;
	}
}
// System.Int32 System.Net.Security.SslStream::EndRead(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR int32_t SslStream_EndRead_m881FE67FB5376508CBB50A96ED65BC672FB3A7A0 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		RuntimeObject* L_0 = ___0_asyncResult;
		int32_t L_1;
		L_1 = TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED(L_0, TaskToApm_End_TisInt32_t680FF22E76F6EFAD4375103CBBFFA0421349384C_mCA309E122256972A27C74FC814F47AD13AF623ED_RuntimeMethod_var);
		return L_1;
	}
}
// System.IAsyncResult System.Net.Security.SslStream::BeginWrite(System.Byte[],System.Int32,System.Int32,System.AsyncCallback,System.Object)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* SslStream_BeginWrite_m66F3B2CDAAE0E1FD662552A8D1ECAC889F2AD2D0 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* ___0_buffer, int32_t ___1_offset, int32_t ___2_count, AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* ___3_callback, RuntimeObject* ___4_state, const RuntimeMethod* method) 
{
	{
		MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* L_0;
		L_0 = SslStream_get_Impl_m1356F55D556B6CBE922FB053F79BC930FCADF057(__this, NULL);
		ByteU5BU5D_tA6237BF417AE52AD70CFB4EF24A7A82613DF9031* L_1 = ___0_buffer;
		int32_t L_2 = ___1_offset;
		int32_t L_3 = ___2_count;
		NullCheck(L_0);
		Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572* L_4;
		L_4 = Stream_WriteAsync_m51D91C94481BB32FE1A876A789C9705F433B133D(L_0, L_1, L_2, L_3, NULL);
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_5 = ___3_callback;
		RuntimeObject* L_6 = ___4_state;
		RuntimeObject* L_7;
		L_7 = TaskToApm_Begin_mA26D2A4F6EE0582B181AFD83B913916909D02777(L_4, L_5, L_6, NULL);
		return L_7;
	}
}
// System.Void System.Net.Security.SslStream::EndWrite(System.IAsyncResult)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SslStream_EndWrite_m78C915054198C80ECEE67CBB07D782E10686BA61 (SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* __this, RuntimeObject* ___0_asyncResult, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = ___0_asyncResult;
		TaskToApm_End_mDFB29EEE501409834D464F4C249A535723B7E6ED(L_0, NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Security.SslStream/<>c__DisplayClass21_0::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void U3CU3Ec__DisplayClass21_0__ctor_mBE4019B5D6D20FC100DAF52B1172ACF497046370 (U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* __this, const RuntimeMethod* method) 
{
	{
		Object__ctor_mE837C6B9FA8C6D5D109F4B2EC885D79919AC0EA2(__this, NULL);
		return;
	}
}
// System.Security.Cryptography.X509Certificates.X509Certificate System.Net.Security.SslStream/<>c__DisplayClass21_0::<SetAndVerifySelectionCallback>b__0(System.String,System.Security.Cryptography.X509Certificates.X509CertificateCollection,System.Security.Cryptography.X509Certificates.X509Certificate,System.String[])
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* U3CU3Ec__DisplayClass21_0_U3CSetAndVerifySelectionCallbackU3Eb__0_mADCFBC85C66A800C57CD2CECA4CDB6A9E5BB020D (U3CU3Ec__DisplayClass21_0_t651FDD00AC35BAE47495F8C7D934432BEC7A178C* __this, String_t* ___0_t, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___1_lc, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___2_rc, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___3_ai, const RuntimeMethod* method) 
{
	{
		LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* L_0 = __this->___callback_0;
		SslStream_t19A079881850F9CAD7BAA6FB625BBC4647ED5A27* L_1 = __this->___U3CU3E4__this_1;
		String_t* L_2 = ___0_t;
		X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* L_3 = ___1_lc;
		X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* L_4 = ___2_rc;
		StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* L_5 = ___3_ai;
		NullCheck(L_0);
		X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* L_6;
		L_6 = LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_inline(L_0, L_1, L_2, L_3, L_4, L_5, NULL);
		return L_6;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.UInt32 <PrivateImplementationDetails>::ComputeStringHash(System.String)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR uint32_t U3CPrivateImplementationDetailsU3E_ComputeStringHash_m171C269D828658C44041FA68B6DE8CA290ED517F (String_t* ___0_s, const RuntimeMethod* method) 
{
	uint32_t V_0 = 0;
	int32_t V_1 = 0;
	{
		String_t* L_0 = ___0_s;
		if (!L_0)
		{
			goto IL_002a;
		}
	}
	{
		V_0 = ((int32_t)-2128831035);
		V_1 = 0;
		goto IL_0021;
	}

IL_000d:
	{
		String_t* L_1 = ___0_s;
		int32_t L_2 = V_1;
		NullCheck(L_1);
		Il2CppChar L_3;
		L_3 = String_get_Chars_mC49DF0CD2D3BE7BE97B3AD9C995BE3094F8E36D3(L_1, L_2, NULL);
		uint32_t L_4 = V_0;
		V_0 = ((int32_t)il2cpp_codegen_multiply(((int32_t)((int32_t)L_3^(int32_t)L_4)), ((int32_t)16777619)));
		int32_t L_5 = V_1;
		V_1 = ((int32_t)il2cpp_codegen_add(L_5, 1));
	}

IL_0021:
	{
		int32_t L_6 = V_1;
		String_t* L_7 = ___0_s;
		NullCheck(L_7);
		int32_t L_8;
		L_8 = String_get_Length_m42625D67623FA5CC7A44D47425CE86FB946542D2_inline(L_7, NULL);
		if ((((int32_t)L_6) < ((int32_t)L_8)))
		{
			goto IL_000d;
		}
	}

IL_002a:
	{
		uint32_t L_9 = V_0;
		return L_9;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.BypassElementCollection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void BypassElementCollection__ctor_m344EC8CD2EACA36E1213C5914037BB133E87FE54 (BypassElementCollection_t15465BC07F8DC8BABA6953182904AF762D638DBE* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&BypassElementCollection__ctor_m344EC8CD2EACA36E1213C5914037BB133E87FE54_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(BypassElementCollection__ctor_m344EC8CD2EACA36E1213C5914037BB133E87FE54_RuntimeMethod_var);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.ConnectionManagementElementCollection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ConnectionManagementElementCollection__ctor_m4698342A715032A762FA04B2745BFCA46263BCB3 (ConnectionManagementElementCollection_tEEB0BEE8289364CCA1D76DDA3F862429612DE0F1* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ConnectionManagementElementCollection__ctor_m4698342A715032A762FA04B2745BFCA46263BCB3_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ConnectionManagementElementCollection__ctor_m4698342A715032A762FA04B2745BFCA46263BCB3_RuntimeMethod_var);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.ConnectionManagementSection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ConnectionManagementSection__ctor_m2FA1D29F887BD6498FBEF692BBC0AA727E38E802 (ConnectionManagementSection_t8F4D45BE147F24F54F2124CC800B4594031F41C2* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ConnectionManagementSection__ctor_m2FA1D29F887BD6498FBEF692BBC0AA727E38E802_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ConnectionManagementSection__ctor_m2FA1D29F887BD6498FBEF692BBC0AA727E38E802_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.ConnectionManagementSection::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* ConnectionManagementSection_get_Properties_mFCE0F9418E9E8CD2E1301815CC4D548261F58E95 (ConnectionManagementSection_t8F4D45BE147F24F54F2124CC800B4594031F41C2* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ConnectionManagementSection_get_Properties_mFCE0F9418E9E8CD2E1301815CC4D548261F58E95_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ConnectionManagementSection_get_Properties_mFCE0F9418E9E8CD2E1301815CC4D548261F58E95_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.DefaultProxySection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void DefaultProxySection__ctor_m962B68CEFCDE7DFB7646EF16297CA4BE0097284D (DefaultProxySection_tC0247A534955DC9D887B03AA4EFB1C1E01AD1E34* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&DefaultProxySection__ctor_m962B68CEFCDE7DFB7646EF16297CA4BE0097284D_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(DefaultProxySection__ctor_m962B68CEFCDE7DFB7646EF16297CA4BE0097284D_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.DefaultProxySection::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* DefaultProxySection_get_Properties_m5186F1A2734EB96F065C7BC14A215D17F8325418 (DefaultProxySection_tC0247A534955DC9D887B03AA4EFB1C1E01AD1E34* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&DefaultProxySection_get_Properties_m5186F1A2734EB96F065C7BC14A215D17F8325418_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(DefaultProxySection_get_Properties_m5186F1A2734EB96F065C7BC14A215D17F8325418_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
// System.Void System.Net.Configuration.DefaultProxySection::Reset(System.Configuration.ConfigurationElement)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void DefaultProxySection_Reset_m0B9A1155840E7C55FC24AEDB23FB17A2815EEB77 (DefaultProxySection_tC0247A534955DC9D887B03AA4EFB1C1E01AD1E34* __this, ConfigurationElement_tAE3EE71C256825472831FFBB7F491275DFAF089E* ___0_parentElement, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&DefaultProxySection_Reset_m0B9A1155840E7C55FC24AEDB23FB17A2815EEB77_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(DefaultProxySection_Reset_m0B9A1155840E7C55FC24AEDB23FB17A2815EEB77_RuntimeMethod_var);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.ProxyElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ProxyElement__ctor_mC1BDF426F97F4F84CB5A34B423F59EC8748EBD70 (ProxyElement_t4D277820F9C77333BE8CC6498DE7E06C36868C65* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ProxyElement__ctor_mC1BDF426F97F4F84CB5A34B423F59EC8748EBD70_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ProxyElement__ctor_mC1BDF426F97F4F84CB5A34B423F59EC8748EBD70_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.ProxyElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* ProxyElement_get_Properties_mBFE6E283FE7E9329B586ACAADC8F2E047C7ABB2A (ProxyElement_t4D277820F9C77333BE8CC6498DE7E06C36868C65* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ProxyElement_get_Properties_mBFE6E283FE7E9329B586ACAADC8F2E047C7ABB2A_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ProxyElement_get_Properties_mBFE6E283FE7E9329B586ACAADC8F2E047C7ABB2A_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.HttpWebRequestElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void HttpWebRequestElement__ctor_mB9DDAC7FF431D4AF4CAFCBC96E1A3572A1F83717 (HttpWebRequestElement_t8D2FEEC531D1FBE1B6E3D1077350A1EBDCB77DC3* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&HttpWebRequestElement__ctor_mB9DDAC7FF431D4AF4CAFCBC96E1A3572A1F83717_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(HttpWebRequestElement__ctor_mB9DDAC7FF431D4AF4CAFCBC96E1A3572A1F83717_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.HttpWebRequestElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* HttpWebRequestElement_get_Properties_mAA0F11EA73B5397C50809EAC679F2C564F9EC7CA (HttpWebRequestElement_t8D2FEEC531D1FBE1B6E3D1077350A1EBDCB77DC3* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&HttpWebRequestElement_get_Properties_mAA0F11EA73B5397C50809EAC679F2C564F9EC7CA_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(HttpWebRequestElement_get_Properties_mAA0F11EA73B5397C50809EAC679F2C564F9EC7CA_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.Ipv6Element::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void Ipv6Element__ctor_m869AC261E0994A8B3D83CEE25C6CB99E6A5EA4C1 (Ipv6Element_t480208D0E23E612DC1FD1CE19D9FB491F0F66932* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Ipv6Element__ctor_m869AC261E0994A8B3D83CEE25C6CB99E6A5EA4C1_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(Ipv6Element__ctor_m869AC261E0994A8B3D83CEE25C6CB99E6A5EA4C1_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.Ipv6Element::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* Ipv6Element_get_Properties_m382D5B5B098EFF19C2A4AC1889CD1C8EAB918322 (Ipv6Element_t480208D0E23E612DC1FD1CE19D9FB491F0F66932* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Ipv6Element_get_Properties_m382D5B5B098EFF19C2A4AC1889CD1C8EAB918322_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(Ipv6Element_get_Properties_m382D5B5B098EFF19C2A4AC1889CD1C8EAB918322_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.NetSectionGroup::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void NetSectionGroup__ctor_mCD2F0DC2B9F0C6C346BC91BD8750C7111A9528AC (NetSectionGroup_tA83DCAF89773087D0E921B92F67441132B71D52F* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&NetSectionGroup__ctor_mCD2F0DC2B9F0C6C346BC91BD8750C7111A9528AC_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(NetSectionGroup__ctor_mCD2F0DC2B9F0C6C346BC91BD8750C7111A9528AC_RuntimeMethod_var);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.SettingsSection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SettingsSection__ctor_mBD94E7E098221DC09775A3AE1713930F6820D242 (SettingsSection_tC931BA69B22D5E0ED07698F02778482987319A12* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SettingsSection__ctor_mBD94E7E098221DC09775A3AE1713930F6820D242_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(SettingsSection__ctor_mBD94E7E098221DC09775A3AE1713930F6820D242_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.SettingsSection::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* SettingsSection_get_Properties_mA774C9C884F4AD5DEC2BBE31DF7E35CAC71EBF8F (SettingsSection_tC931BA69B22D5E0ED07698F02778482987319A12* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SettingsSection_get_Properties_mA774C9C884F4AD5DEC2BBE31DF7E35CAC71EBF8F_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(SettingsSection_get_Properties_mA774C9C884F4AD5DEC2BBE31DF7E35CAC71EBF8F_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.PerformanceCountersElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void PerformanceCountersElement__ctor_m9537195CBC7DC607F7253D06E7B6B1090456E68F (PerformanceCountersElement_tCB62C8CA7C218E35653DC5BB690A5FFD6D608B30* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&PerformanceCountersElement__ctor_m9537195CBC7DC607F7253D06E7B6B1090456E68F_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(PerformanceCountersElement__ctor_m9537195CBC7DC607F7253D06E7B6B1090456E68F_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.PerformanceCountersElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* PerformanceCountersElement_get_Properties_m7C16FBC3E915E8384FB4409209628737B1537C9B (PerformanceCountersElement_tCB62C8CA7C218E35653DC5BB690A5FFD6D608B30* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&PerformanceCountersElement_get_Properties_m7C16FBC3E915E8384FB4409209628737B1537C9B_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(PerformanceCountersElement_get_Properties_m7C16FBC3E915E8384FB4409209628737B1537C9B_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.ServicePointManagerElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ServicePointManagerElement__ctor_m0F1CF21589CA283DA734A472930A9665698B2D36 (ServicePointManagerElement_t98ED8374270537E77E57D014ECEE8B1A3A8EEFF2* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ServicePointManagerElement__ctor_m0F1CF21589CA283DA734A472930A9665698B2D36_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ServicePointManagerElement__ctor_m0F1CF21589CA283DA734A472930A9665698B2D36_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.ServicePointManagerElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* ServicePointManagerElement_get_Properties_m70469523AAD1A2C2E9D4F90DDA1BA619FCE68B58 (ServicePointManagerElement_t98ED8374270537E77E57D014ECEE8B1A3A8EEFF2* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&ServicePointManagerElement_get_Properties_m70469523AAD1A2C2E9D4F90DDA1BA619FCE68B58_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(ServicePointManagerElement_get_Properties_m70469523AAD1A2C2E9D4F90DDA1BA619FCE68B58_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.SocketElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void SocketElement__ctor_m4FB4C851F1BB20CED74C206EFB1BA9DDFBE024D2 (SocketElement_tDC83FCE3C280D8D590972F989292C30E2B7F6CC6* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketElement__ctor_m4FB4C851F1BB20CED74C206EFB1BA9DDFBE024D2_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(SocketElement__ctor_m4FB4C851F1BB20CED74C206EFB1BA9DDFBE024D2_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.SocketElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* SocketElement_get_Properties_m8AF94DEEDA0EB4092F59FBDC361592E1B9EBDEC3 (SocketElement_tDC83FCE3C280D8D590972F989292C30E2B7F6CC6* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&SocketElement_get_Properties_m8AF94DEEDA0EB4092F59FBDC361592E1B9EBDEC3_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(SocketElement_get_Properties_m8AF94DEEDA0EB4092F59FBDC361592E1B9EBDEC3_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.WebProxyScriptElement::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void WebProxyScriptElement__ctor_mB613D71DE569317E40726CD8DB9A6ABA0A302821 (WebProxyScriptElement_t0C0E2338F7F42D5D386F5AE68FF7F870FA6E7167* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WebProxyScriptElement__ctor_mB613D71DE569317E40726CD8DB9A6ABA0A302821_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(WebProxyScriptElement__ctor_mB613D71DE569317E40726CD8DB9A6ABA0A302821_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.WebProxyScriptElement::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* WebProxyScriptElement_get_Properties_mF5188225C0C1D64BC136465BB4B0E962FFE9240B (WebProxyScriptElement_t0C0E2338F7F42D5D386F5AE68FF7F870FA6E7167* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WebProxyScriptElement_get_Properties_mF5188225C0C1D64BC136465BB4B0E962FFE9240B_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(WebProxyScriptElement_get_Properties_mF5188225C0C1D64BC136465BB4B0E962FFE9240B_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.WebRequestModulesSection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void WebRequestModulesSection__ctor_mF25CD80A5111A4F87B7FBF7663A3442470B97C0E (WebRequestModulesSection_t4099F9890D7851314C5528C577E4C51B36C97D00* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WebRequestModulesSection__ctor_mF25CD80A5111A4F87B7FBF7663A3442470B97C0E_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(WebRequestModulesSection__ctor_mF25CD80A5111A4F87B7FBF7663A3442470B97C0E_RuntimeMethod_var);
		return;
	}
}
// System.Configuration.ConfigurationPropertyCollection System.Net.Configuration.WebRequestModulesSection::get_Properties()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93* WebRequestModulesSection_get_Properties_mBDBDED569E677717FB617CD36B94AE34BF4208F6 (WebRequestModulesSection_t4099F9890D7851314C5528C577E4C51B36C97D00* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WebRequestModulesSection_get_Properties_mBDBDED569E677717FB617CD36B94AE34BF4208F6_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(WebRequestModulesSection_get_Properties_mBDBDED569E677717FB617CD36B94AE34BF4208F6_RuntimeMethod_var);
		return (ConfigurationPropertyCollection_t1DEB95D3283BB11A46B862E9D13710ED698B6C93*)NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Net.Configuration.WebRequestModuleElementCollection::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void WebRequestModuleElementCollection__ctor_m62C156D78336B2E37EA6F10D477FDEAE4161F99E (WebRequestModuleElementCollection_t5979A19A48B822831C1D351EB2AA25058C7F7463* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&WebRequestModuleElementCollection__ctor_m62C156D78336B2E37EA6F10D477FDEAE4161F99E_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(WebRequestModuleElementCollection__ctor_m62C156D78336B2E37EA6F10D477FDEAE4161F99E_RuntimeMethod_var);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void System.Diagnostics.DiagnosticsConfigurationHandler::.ctor()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void DiagnosticsConfigurationHandler__ctor_mDFB0000E6218840F66D21BB6C9BF3F647CCBC5A3 (DiagnosticsConfigurationHandler_t0C4B611E2C6EA17B288C43070FAF65F96DCA96E7* __this, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&DiagnosticsConfigurationHandler__ctor_mDFB0000E6218840F66D21BB6C9BF3F647CCBC5A3_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(DiagnosticsConfigurationHandler__ctor_mDFB0000E6218840F66D21BB6C9BF3F647CCBC5A3_RuntimeMethod_var);
		return;
	}
}
// System.Object System.Diagnostics.DiagnosticsConfigurationHandler::Create(System.Object,System.Object,System.Xml.XmlNode)
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR RuntimeObject* DiagnosticsConfigurationHandler_Create_mF21B1CC68B87DE078690B2B493FCDC5F728CBAAB (DiagnosticsConfigurationHandler_t0C4B611E2C6EA17B288C43070FAF65F96DCA96E7* __this, RuntimeObject* ___0_parent, RuntimeObject* ___1_configContext, XmlNode_t3180B9B3D5C36CD58F5327D9F13458E3B3F030AF* ___2_section, const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&DiagnosticsConfigurationHandler_Create_mF21B1CC68B87DE078690B2B493FCDC5F728CBAAB_RuntimeMethod_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_raise_profile_exception(DiagnosticsConfigurationHandler_Create_mF21B1CC68B87DE078690B2B493FCDC5F728CBAAB_RuntimeMethod_var);
		return NULL;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void Unity.ThrowStub::ThrowNotSupportedException()
IL2CPP_EXTERN_C IL2CPP_METHOD_ATTR void ThrowStub_ThrowNotSupportedException_m14168B43936EF78B7B8AC08BD815254DAA1D9FA2 (const RuntimeMethod* method) 
{
	{
		PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A* L_0 = (PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A*)il2cpp_codegen_object_new(((RuntimeClass*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&PlatformNotSupportedException_tD2BD7EB9278518AA5FE8AE75AD5D0D4298A4631A_il2cpp_TypeInfo_var)));
		NullCheck(L_0);
		PlatformNotSupportedException__ctor_mD5DBE8E9A6FF4B75EF02671029C6D67A51EAFBD1(L_0, NULL);
		IL2CPP_RAISE_MANAGED_EXCEPTION(L_0, ((RuntimeMethod*)il2cpp_codegen_initialize_runtime_metadata_inline((uintptr_t*)&ThrowStub_ThrowNotSupportedException_m14168B43936EF78B7B8AC08BD815254DAA1D9FA2_RuntimeMethod_var)));
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Exception_t* Exception_get_InnerException_m0C1BDB339C786BA4DA7D2C1AD214571CFBBB1410_inline (Exception_t* __this, const RuntimeMethod* method) 
{
	{
		Exception_t* L_0 = __this->____innerException_4;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t Win32Exception_get_NativeErrorCode_m33C1D176D9178C36DA32F8FEC251B923EE53CE5E_inline (Win32Exception_t15A75629914EB77C816D8219D93ED78E50C74BE9* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->___nativeErrorCode_18;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void LingerOption_set_Enabled_mEDF2FED046EA58F9C23BEF3BF1184C3A890419DB_inline (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, bool ___0_value, const RuntimeMethod* method) 
{
	{
		bool L_0 = ___0_value;
		__this->___enabled_0 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void LingerOption_set_LingerTime_mDC6B06F96C04C5A69E19912EBB37DC5BDE5A6DFB_inline (LingerOption_t0053FF52A038EABFF91A11997A93ACC04C85AB98* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___lingerTime_1 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* TcpClient_get_Client_m38B7DCD73EB42C6BBE6340B5E4C0A8432C1140C0_inline (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = __this->___m_ClientSocket_0;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* Task_get_Factory_m8272CF9106A72E5F0B8E8C6CEE025D748FBD6D11_inline (const RuntimeMethod* method) 
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_runtime_metadata((uintptr_t*)&Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var);
		s_Il2CppMethodInitialized = true;
	}
	{
		il2cpp_codegen_runtime_class_init_inline(Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var);
		TaskFactory_tF781BD37BE23917412AD83424D1497C7C1509DF0* L_0 = ((Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_StaticFields*)il2cpp_codegen_static_fields_for(Task_t751C4CC3ECD055BABA8A0B6A5DFBB4283DCA8572_il2cpp_TypeInfo_var))->___U3CFactoryU3Ek__BackingField_15;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR bool Socket_get_Connected_m6E61ADEB0124FB8D46AC1EDFDC268D3C9F7869C5_inline (Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* __this, const RuntimeMethod* method) 
{
	{
		bool L_0 = __this->___is_connected_25;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void TcpClient_set_Client_m145E6A35F13B52AF10DE077FD19DE79A622E5F5C_inline (TcpClient_t753B702EE06B59897564F75CEBFB6C8AFF10BD58* __this, Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* ___0_value, const RuntimeMethod* method) 
{
	{
		Socket_t1F49472CDA22B581C29A258225ABF3ADA9DED67E* L_0 = ___0_value;
		__this->___m_ClientSocket_0 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___m_ClientSocket_0), (void*)L_0);
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SafeHandle_SetHandle_m003D64748F9DFBA1E3C0B23798C23BA81AA21C2A_inline (SafeHandle_tC1A4DA80DA89B867CC011B707A07275230321BF7* __this, intptr_t ___0_handle, const RuntimeMethod* method) 
{
	{
		intptr_t L_0 = ___0_handle;
		__this->___handle_0 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_BytesTransferred_mAB2EB75BE1BB7B97DBEFCAF3DD42E26BC787D625_inline (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CBytesTransferredU3Ek__BackingField_7 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void SocketAsyncEventArgs_set_LastOperation_m7BA8A0B5CB76B4C095BBB9E1124C3F237B51E26A_inline (SocketAsyncEventArgs_t69FA99F5C4FBBE1F321A770B65D38FE5D62027EC* __this, int32_t ___0_value, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = ___0_value;
		__this->___U3CLastOperationU3Ek__BackingField_8 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR bool IOAsyncResult_get_CompletedSynchronously_m26279BC02AFFE83009A7EEABFFF58EEB2D12CD24_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, const RuntimeMethod* method) 
{
	{
		bool L_0 = __this->___completed_synchronously_3;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* IOAsyncResult_get_AsyncCallback_mC6150F5FC2CEE8FDC22EACD9B9BC72544FE801CC_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, const RuntimeMethod* method) 
{
	{
		AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* L_0 = __this->___async_callback_0;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void IOAsyncResult_set_CompletedSynchronously_m29ECF9F6388C1F9A12AF83DF8CD19D9FFB4B4D6D_inline (IOAsyncResult_t667861995468DDF5B2CFED1940CC2BCEEDD2B374* __this, bool ___0_value, const RuntimeMethod* method) 
{
	{
		bool L_0 = ___0_value;
		__this->___completed_synchronously_3 = L_0;
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void AsyncCallback_Invoke_mA85DEAE969B1D0FD97E934DC39439E89D7553910_inline (AsyncCallback_t7FEF460CBDCFB9C5FA2EF776984778B9A4145F4C* __this, RuntimeObject* ___0_ar, const RuntimeMethod* method) 
{
	typedef void (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, const RuntimeMethod*);
	((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_ar, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR String_t* MobileAuthenticatedStream_get_TargetHost_m572DA701CB3CE3FA4006D5745F9A3B9A52739CAB_inline (MobileAuthenticatedStream_tD0306DC2B0CDA3C7DB261C19FFA35CA8EE24309E* __this, const RuntimeMethod* method) 
{
	{
		String_t* L_0 = __this->___U3CTargetHostU3Ek__BackingField_21;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void MonoTlsSettings_set_RemoteCertificateValidationCallback_m6CEA8A6E38C85A96C2D26613407C13DD4F965C87_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* ___0_value, const RuntimeMethod* method) 
{
	{
		MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* L_0 = ___0_value;
		__this->___U3CRemoteCertificateValidationCallbackU3Ek__BackingField_0 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CRemoteCertificateValidationCallbackU3Ek__BackingField_0), (void*)L_0);
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* MonoTlsSettings_get_RemoteCertificateValidationCallback_mE07825B4A75DAE2A4BB5037D504A36311814446C_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, const RuntimeMethod* method) 
{
	{
		MonoRemoteCertificateValidationCallback_t1A389B61998873F6B9A2EE7A11C36333A8AECCA0* L_0 = __this->___U3CRemoteCertificateValidationCallbackU3Ek__BackingField_0;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void MonoTlsSettings_set_ClientCertificateSelectionCallback_mB404DFD0C0475254CC129740A472D6D9615C56FD_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* ___0_value, const RuntimeMethod* method) 
{
	{
		MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* L_0 = ___0_value;
		__this->___U3CClientCertificateSelectionCallbackU3Ek__BackingField_1 = L_0;
		Il2CppCodeGenWriteBarrier((void**)(&__this->___U3CClientCertificateSelectionCallbackU3Ek__BackingField_1), (void*)L_0);
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* MonoTlsSettings_get_ClientCertificateSelectionCallback_mCFE63487D867109AD1AF856ECC8BA0996C0AA605_inline (MonoTlsSettings_tD79AF4AE5C2CD533A3D7A08FED479B1EC1A031B0* __this, const RuntimeMethod* method) 
{
	{
		MonoLocalCertificateSelectionCallback_t34F7772BA5ECE38E6CBD4C311F579DD1D4724DE3* L_0 = __this->___U3CClientCertificateSelectionCallbackU3Ek__BackingField_1;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* AuthenticatedStream_get_InnerStream_m5BDA4857898A90C696DAD90FD61C536FC3EB10C7_inline (AuthenticatedStream_t8DCF41E151F705E2494FC7836F5E2EF7C539FA39* __this, const RuntimeMethod* method) 
{
	{
		Stream_tF844051B786E8F7F4244DBD218D74E8617B9A2DE* L_0 = __this->____InnerStream_4;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* LocalCertificateSelectionCallback_Invoke_mED43EE6E88B8C653C7D68966F86751B70907591C_inline (LocalCertificateSelectionCallback_t71A03329606A5610ECC62BFBE6327C1EF9195859* __this, RuntimeObject* ___0_sender, String_t* ___1_targetHost, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE* ___2_localCertificates, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* ___3_remoteCertificate, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248* ___4_acceptableIssuers, const RuntimeMethod* method) 
{
	typedef X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4* (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, String_t*, X509CertificateCollection_t2900D71D188EDCA7DEB5077D36103EE5DA6805CE*, X509Certificate_t966CC553AF25AE7991F5B4C2AACBCF6C66C8F9C4*, StringU5BU5D_t7674CD946EC0CE7B3AE0BE70E6EE85F2ECD9F248*, const RuntimeMethod*);
	return ((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_sender, ___1_targetHost, ___2_localCertificates, ___3_remoteCertificate, ___4_acceptableIssuers, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t String_get_Length_m42625D67623FA5CC7A44D47425CE86FB946542D2_inline (String_t* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->____stringLength_4;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR RuntimeObject* Enumerator_get_Current_m6330F15D18EE4F547C05DF9BF83C5EB710376027_gshared_inline (Enumerator_t9473BAB568A27E2339D48C1F91319E0F6D244D7A* __this, const RuntimeMethod* method) 
{
	{
		RuntimeObject* L_0 = __this->____current_3;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR int32_t List_1_get_Count_m4407E4C389F22B8CEC282C15D56516658746C383_gshared_inline (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, const RuntimeMethod* method) 
{
	{
		int32_t L_0 = __this->____size_2;
		return L_0;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void List_1_Add_mEBCF994CC3814631017F46A387B1A192ED6C85C7_gshared_inline (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D* __this, RuntimeObject* ___0_item, const RuntimeMethod* method) 
{
	ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* V_0 = NULL;
	int32_t V_1 = 0;
	{
		int32_t L_0 = __this->____version_3;
		__this->____version_3 = ((int32_t)il2cpp_codegen_add(L_0, 1));
		ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* L_1 = __this->____items_1;
		V_0 = L_1;
		int32_t L_2 = __this->____size_2;
		V_1 = L_2;
		int32_t L_3 = V_1;
		ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* L_4 = V_0;
		NullCheck(L_4);
		if ((!(((uint32_t)L_3) < ((uint32_t)((int32_t)(((RuntimeArray*)L_4)->max_length))))))
		{
			goto IL_0034;
		}
	}
	{
		int32_t L_5 = V_1;
		__this->____size_2 = ((int32_t)il2cpp_codegen_add(L_5, 1));
		ObjectU5BU5D_t8061030B0A12A55D5AD8652A20C922FE99450918* L_6 = V_0;
		int32_t L_7 = V_1;
		RuntimeObject* L_8 = ___0_item;
		NullCheck(L_6);
		(L_6)->SetAt(static_cast<il2cpp_array_size_t>(L_7), (RuntimeObject*)L_8);
		return;
	}

IL_0034:
	{
		RuntimeObject* L_9 = ___0_item;
		((  void (*) (List_1_tA239CB83DE5615F348BB0507E45F490F4F7C9A8D*, RuntimeObject*, const RuntimeMethod*))il2cpp_codegen_get_method_pointer(il2cpp_rgctx_method(method->klass->rgctx_data, 11)))(__this, L_9, il2cpp_rgctx_method(method->klass->rgctx_data, 11));
		return;
	}
}
IL2CPP_MANAGED_FORCE_INLINE IL2CPP_METHOD_ATTR void EventHandler_1_Invoke_mBD72C04FF5A08A2EA93DFD21037CD1C27A48D07A_gshared_inline (EventHandler_1_tD8C4A5BE1F7C91B1A7E99AE87AFD2F5432C38746* __this, RuntimeObject* ___0_sender, RuntimeObject* ___1_e, const RuntimeMethod* method) 
{
	typedef void (*FunctionPointerType) (RuntimeObject*, RuntimeObject*, RuntimeObject*, const RuntimeMethod*);
	((FunctionPointerType)__this->___invoke_impl_1)((Il2CppObject*)__this->___method_code_6, ___0_sender, ___1_e, reinterpret_cast<RuntimeMethod*>(__this->___method_3));
}
