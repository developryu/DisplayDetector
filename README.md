# <center> **DisplayDetector**</center>

DisplayDetector는 Android 폴더블 기기의 상태를 감지하는 모듈입니다. 폴더블 디바이스와 플립 디바이스의 다양한 상태를 실시간으로 감지하고, Jetpack Compose와 Flow를 활용하여 디스플레이 상태 변화를 쉽게 관찰할 수 있습니다.

## 특징

- 폴더블/플립 디바이스 상태 감지
- 실시간 디스플레이 상태 모니터링
- Jetpack Compose 지원
- Flow API 활용
- 멀티 윈도우 상태 감지
- 디바이스 방향 감지

## 사용 방법

Compose에서 디스플레이 상태를 관찰하려면 다음과 같이 사용하세요:

```kotlin
val displayState = activity.observeDisplayState().collectAsState(initial = null).value
```

이를 통해 디스플레이 상태의 변화를 감지하고 UI를 동적으로 업데이트할 수 있습니다.

## 상태 정보 구조

### DisplayState

주요 상태 정보를 포함하는 데이터 클래스입니다:

```kotlin
data class DisplayState(
    val displayPxSize: DisplaySize,        // 픽셀 단위 디스플레이 크기
    val displayDpSize: DisplaySize,        // DP 단위 디스플레이 크기
    val deviceType: DeviceType,            // 디바이스 타입
    val multiWindowType: MultiWindowType,  // 멀티 윈도우 상태
    val deviceOrientation: Orientation,    // 디바이스 방향
    val foldableState: FoldableState,     // 폴더블 상태
)
```

### 주요 열거형 (Enum) 클래스

#### MultiWindowType
- `SINGLE`: 단일 윈도우
- `MULTI_HORIZONTAL`: 가로 분할 멀티 윈도우
- `MULTI_VERTICAL`: 세로 분할 멀티 윈도우
- `MULTI_BOTH`: 가로/세로 동시 분할

#### DeviceType
- `PHONE`: 일반 스마트폰
- `TABLET`: 태블릿
- `FLIP`: 플립형 폴더블
- `FOLD`: 폴드형 폴더블

#### FoldableState
- `FLAT`: 완전히 펼쳐진 상태
- `HALF_OPENED`: 반으로 접힌 상태
- `CLOSED`: 완전히 접힌 상태

#### Orientation
- `HORIZONTAL`: 가로 방향
- `VERTICAL`: 세로 방향

### DisplaySize

디스플레이 크기 정보를 담는 데이터 클래스:

```kotlin
data class DisplaySize(
    val width: Int = 0,
    val height: Int = 0,
)
```

## 👨‍💻 개발자 정보
류호일(ryuhoil0712@gmail.com)
