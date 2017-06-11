# 네트워크 - 인터넷 연결
각 장비마다 네트워크 구성이 달라진다. 앱의 현재 연결 상태에 대한 정보를 얻어 사용 가능한 최적의 방법을 찾아야 하는 작업이 매우 중요하다.

장비의 연결 상태를 확인하는 단계는 아래와 같다.
1. 매니페스트에 퍼미션을 지정한다.
2. 연결 관리자를 호출한다.
3. 연결 관리자의 메서드를 호출해 네트워크 정보를 구한다.

## 1. 퍼미션 지정
매니페스트에 아래의 코드를 추가해 퍼미션을 지정한다.

**[AndroidManifest.xml]**
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

* * * 

## 2. 연결 관리자 호출
연결 관리자는 시스템 서비스이므로 별도의 객체를 생성할 필요 없이 바로 호출할 수 있다.


```java
ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
```

getSystemService(CONNECTIVITY_SERVICE)로 호출되어 리턴되는 ConnectivityManager 객체가 연결 관리자가 된다.

### > 연결 관리자의 기능
- 사용 가능한 네트워크에 대한 정보 조사
- 각 연결 방법의 현재 상태를 조사
- 네트워크 연결 상태가 변경될 때 모든 응용 프로그램에게 인텐트로 알림
- 한 연결에 실패시 대체 연결을 찾음

* * *

## 3. 네트워크 정보 추출
네트워크에 대한 정보를 구할 때는 연결 관리자의 메서드를 활용한다.

- NetworkInfo[] getAllNetworkInfo()
- NetworkInfo getActiveNetworkInfo()
- NewworkInfo getNetworkInfo (int networkType)

위의 메서드는 NetworkInfo 객체를 리턴한다. 리턴된 객체는 아래와 같은 '속성 조사 메서드'가 제공된다.

- boolean isAvailable()
- boolean isConnected()
- boolean isRoaming()
- NetworkInfo.State getState()

```java
NetworkInfo[] ani = mgr.getAllNetworkInfo();
```

조사된 연결 정보는 toString으로 문자열화 하면 확인할 수 있다.

```java
String result = "";
for (NetworkInfo item : ani) {
	result = result + (item.toString() + "\n\n");
}
```

```flow
st=>start: Start
e=>end
op=>operation: My Operation
cond=>condition: Yes or No?

st->op->cond
cond(yes)->e
cond(no)->op
```

