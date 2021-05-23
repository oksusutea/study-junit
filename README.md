# 좋은 테스트의 FIRST 속성

* [F]ast : 빠른
* [I]solated : 고립된
* [R]epeatable : 반복가능한
* [S]elf-validating : 스스로 검증 가능한
* [T]imely : 적시의

### [F]IRST : 빠르다

빠른 테스트는 코드만 실행하며 소요시간은 수 밀리초 수준이다.  
DB등 영속성 저장소와 연관되어 있는 코드는 테스트 할 떄 수백밀리초까지 걸릴 수 있기 때문에, 이와 분리하여 빠르게 테스트를 수행할 수 있도록 테스트 코드를 작성해주어야 한다.

### F[I]RST : 고립시킨다

좋은 단위 테스트는 검증하려는 작은 양의 코드에 집중한다. 또한, 좋은 단위 테스트는 다른 단위 테스트에 의존하지 않는다.(혹은 같은 메서드에 있는 테스트 케이스)

### FI[R]ST : 좋은 테스트는 반복 가능해야 한다

반복 가능한 테스트는 실행할 때마다 결과가 같아야 한다. 만일 시간과 같이 통제할 수 없는 요소에 대해 테스트를 한다면? 진짜 시간을 가진 것 처럼 속이는 방법을 사용해보자.
(`java.time.Clock`을 사용한다)

### FIR[S]T : 스스로 검증 가능하다

`main()`메소드 같은 것을 사용하면 사용자가 결국 실행 한 값이 맞는지를 확인해주어야 한다. 스스로 검증하지 못하면 좋은 테스트라고 할 수 었다.

### FIRS[T] : 적시에 사용한다

단위 테스트를 많이 할 수록 테스트 대상 코드가 줄어든다. 그리고 새로운 코드를 넣었을 때 테스트 효과가 즉시 나타난다.

***

# Right-BICEP: 무엇을 테스트할 것인가?

* [Right]-BICEP: 결과가 올바른가?
* Right-[B]ICEP: 경계 조건은 맞는가?
* Right-B[I]CEP: 역 관계를 검사할 수 있는가?
* Right-BI[C]EP: 다른 수단을 활용하여 교차 검사할 수 있는가?
* Right-BIC[E]P: 오류 조건을 강제로 일어나게 할 수 있는가?
* Right-BICE[P]: 성능 조건은 기준에 부합하는가?

### [Right]-BICEP: 결과가 올바른가?

테스트 코드는 무엇보다도 먼저 기대한 결과를 산출하는지 검증 할 수 있어야 한다.  
단위테스트를 작성할때 결과 단언 값을 문서화하자. 어떤 변경이 발생하면, 적어도 현재까지 코드가 어떻게 동작했는지 알 수 있다.

### Right-[B]ICEP: 경계 조건은 맞는가? (boundary Conditions)

테스트 코드에서는 기능의 결과가 올바른 값을 산출하는지 검증하는 것도 중요하지만, courner case에 대해서도 오류가 적절하게 처리되는지 검증해야 한다.  
생각해야 하는 경계 조건은 아래와 같다 :

* 모호하고 일관성 없는 입력 값. 예를 들어 특수문자("!@#$%^)가 포함된 파일 이름
* 잘못된 양식의 데이터. 예를 들어 최상위 도메인이 빠진 이메일 주소 등(fred@foobar.)
* 수치적 오버플로를 일으키는 계산
* 비거나 빠진 값. 예를 들어 0, 0.0, "" 혹은 null
* 이성적인 기댓값을 훨씬 벗어나는 값. 예를 들어 나이가 160살일 경우
* 교실의 당번표처럼 중복을 허용해서는 안되는 목록에 중복 값이 있는 경우
* 정렬이 안된 정렬 리스트 혹은 그 반대. 정렬 알고리즘에 이미 정렬된 입력 값을 넣는 경우나, 정렬 알고리즘에 역순 데이터를 넣는 경우
* 시간 순이 맞지 않는 경우. 예를들어 HTTP 서버가 OPTIONS 메서드의 결과를 POST보다 먼저 반환해야 하지만, 그 후에 반환하는 경우

유호하지 않은 값을 넣자마자 오류가 발생하도록 하는 것이 좋다. 아래와 같이 말이다.

```java
public void add(Scoreable scoreable){
    if(scoreable==null){
    throw new IllegalArgumentException();
    }
    scores.add(scoreable);
    }
```

이런 경계 조건에서는 **CORRECT** 약어를 기억해 적절하게 처리해주자.

### Right-B[I]CEP: 역 관계를 검사할 수 있는가? (inverse relationship)

때때로 논리적인 역 관계를 적용하여 행동을 검사할 수 있다. 수학 계산에서 곱셈을 나눗셈으로 검증하고, 뺄셈으로 덧셈을 검증하는 것과 같이 말이다.

### Right-BI[C]EP: 다른 수단을 활용하여 교차 검사할 수 있는가? (cross-check)

우리가 개발을 할 때는 성능이 좋은 1등 해법을 선택하지만, 운영상에는 그 외 이슈로 선택하지 못한 알고리즘이 있을 수 있다. 이런 알고리즘은(운영상 적용되지 못했지만, 기능상
적용 가능한 것들) 교차검사할 때 활용할 수 있다. 교차 검사를 보는 다른 방법은, 클래스의 서로 다른 조각 데이터를 사용해 모든 데이터가 합산되는지 확인해 보는 것이다.

### Right-BIC[E]P: 오류 조건을 강제로 일어나게 할 수 있는가? (error conditions)

기능상의 예외가 발생하여 오류가 일어날 수도 있지만, 디스크가 꽉차거나 네트워크 선이 떨어지거나 등 각종 원인으로 인해 프로그램이 중단될 수 있다. 이러한 현상을 효과적으로
처리하기 위해 테스트에서도 오류를 강제로 발생시켜야 한다. 먼저 코드를 테스트하기 위해 도입할 수 있는 오류의 종류 혹은 다른 환경적 제약 사항을 생각해볼 떄, 아래 시나리오를
고려해볼 수 있다 :

* 메모리가 가득 찰 때
* 디스크 공간이 가득 찰 때
* 벽시계 시간(클라이언트 시간 != 서버 시간)에 관한 문제들
* 네트워크 가용성 및 오류
* 시스템 로드
* 제한된 색상 팔레트
* 매우 높거나 낮은 비디오 해상도

### Right-BICE[P]: 성능 조건은 기준에 부합하는가? (performance characteristics)

성능이 핵심 고려 사항이라면 단위 테스트보다는 좀 더 고수준에서 문제를 집중하고 싶을 것이고, JMeter 같은 도구를 사용해주는 것이 좋다.


***

# 경계 조건: CORRECT 기억법

단위 테스트는 종종 경계 조건들에 관계된 결함들을 미연에 방지하는데 도움이 된다. 경게 조건은 행복 경로의 끝에 있는 것으로, 사람들이 종종

* [C]onformance(준수) : 값이 기대한 양식을 준수하고 있는가?
* [O]rdering(순서) : 값의 집합이 적절하게 정렬되거나 정렬되지 않았나?
* [R]ange(범위) : 이성적인 최솟값과 최댓값 안에 있는가?
* [R]eference(참조) : 코드 자체에서 통제할 수 없는 어떤 외부 참조를 포함하고 있는가?
* [E]xistence(존재) : 값이 존재하는가(널이 아니거나(Non-null), 0이 아니거나(nonzero), 집합에 존재하는가 등)?
* [C]ardinality(기수) : 정확히 충분한 값들이 있는가?
* [T]ime(절대적 혹은 상대적 시간) : 모든 것이 순서대로 일어나는가? 정확한 시간에? 정시에?

### [C]onformance(준수) : 값이 기대한 양식을 준수하고 있는가?

이메일을 예시로 들어보면 `name@somedomain`과 같은 양식으로 입력할 수 있다.여기서 보통 코드는 이메일주소를 파싱하여 이름 부분을 추출할 수 있다. `@`기호
앞부분이다. 하지만 이 문자가 없을때의 경우에도 대처가 필요하다.

이 외에도, 계좌 번호 같은 필드는 시스템에 있는 수많은 메서드에 넘겨질 것이다. 하지만 시스템에 그 필드가 처음 입력될 때 검증한다면, 그 필드를 인자로 넘길 때마다 검사하지
않아도 된다. 즉 불필요한 검사를 최소화 할 수 있다.

### [O]rdering(순서) : 값의 집합이 적절하게 정렬되거나 정렬되지 않았나?

정해진 순서로 출력을 해야 할 때, 순서가 맞게 정렬되어 있는지 확인해야 한다.

### [R]ange(범위) : 이성적인 최솟값과 최댓값 안에 있는가?

나이를 Integer로 선언하지만 최대 150~200까지만 지정해야 하는 등, 비즈니스 로직적 혹은 데이터 타입 별로 최소, 최대 값을 지정해야 한다.

### [R]eference(참조) : 코드 자체에서 통제할 수 없는 어떤 외부 참조를 포함하고 있는가?

어떤 메소드를 테스트할 때는 다음을 고려해야 한다 :

* 범위를 넘어서는 것을 참조하고 있지 않은지
* 외부 의존성은 무엇인지
* 특정 상태에 있는 객체를 의존하고 있는지 여부
* 반드시 존재해야 하는 그 외 다른 조건들 예를 들어 계정 히드토리를 표시하는 웹 앱은 고객이 먼저 로그인을 해야하고, 스택의 `pop()`메소드를 호출할 때는 스택이 비어있으면
  안된다.

어떤 상태에 대해 가정할 때, 그 가정이 맞지 않으면 코드가 합리적으로 잘 동작하는지 검사해야 한다.

### [E]xistence(존재) : 값이 존재하는가(널이 아니거나(Non-null), 0이 아니거나(nonzero), 집합에 존재하는가 등)?

스스로에게 "주어진 값이 존재하는가?"라고 물어봄으로써 많은 잠재적 결함을 발견할 수 있다. 호출된 메서드가 null을 반환하거나, 기대하는 파일이 없거나, 네트워크가 다운되었을
때 등 어떤 일이 일어나는지 확인하는 테스트를 작성하자.

### [C]ardinality(기수) : 정확히 충분한 값들이 있는가?

기수를 사용하면 '일부'혹은 '없음'보다 좀 더 구체적인 답변을 볼 수 있다. 그런데도 집합을 이루는 값 개수는 0, 1, 다수(1보다 많은)이 세가지 경우에 더 흥미를 둔다.
보통 테스트 코드는 0,1, n이라는 경계 조건에 집중하고, n은 비즈니스 요구 사항에 따라 바뀔 수 있다.

### [T]ime(절대적 혹은 상대적 시간) : 모든 것이 순서대로 일어나는가? 정확한 시간에? 정시에?

시간에 관해 고려해야 할 측면은 아래와 같다 :

* 상대적 시간(시간순서)
* 절대적 시간(측정된 시간)
* 동시성 문제

비즈니스 로직적으로 로그인은 로그아웃보다 앞서 호출되어야 하고, 파일을 읽는 메소드는 파일을 여는 메소드 후에 호출해야 한다. 이 때, 메소드의 호출 순서가 맞지 않았을 때 어떤
일이 발생할지 생각해보자.

***

# 깔끔한 코드로 리팩토링 하기

리팩토링은 기존 기능은 그대로 유지하며 코드의 하부 구조를 건강하게 변형하는 것이다. 작은 코드들을 계속해서 리팩토링 함으로써 유지 보수 비용을 지속적으로 낮추어 준다.

### 첫번째 리팩토링 : rename

클래스, 메서드, 모등 종류의 변수에 해당된다. 대개 코드 의도를 명확하게 선언하고, 코드 의도를 전달하는 가장 좋은 수단이다.

### 두번째 리팩토링 : 메서드 추출

별도의 메소드로 추출하여 복잡성을 고립시킨다.  
저수준의 세부 사항을 추출했기 때문에 고수준의 정책만 이해하는 것으로 충분하다면 저수준으로 관심이 분산되지 않는다. 단위 테스트를 이용해 코드를 안전하게 변경할 수 있다. 새로운
기능을 안전하게 추가할 수 있고 좋은 설계를 유지하며 변경할 수 있다.

### 디메테르의 법칙 지키기

다른 객체로 전파되는 연쇄적인 메소드 호출을 피해야 한다. 결합도가 낮은 설계를 위한 법칙이다.

### 임시변수

임시변수로 값비싼 비용의 계산 값을 캐시에 넣거나, 메서드 몸체에서 변경되는 수집할 수 있다. 그 외에도 코드 의도를 명확하게 할 때 사용할 수 있다. 맥에서는 option +
command + N키를 이용해 변수를 리팩토링할 수 있다.

### 리팩토링 후에는?

리팩토링을 하면 테스트를 다시 실행해야 한다. 서로 떨어진 코드들을 새로운 메소드로 추출할 때는 자동화 하는 방법이 없기 때문에, 수동으로 리팩토링 해야 한다. 명확하고 테스트
가능한 단위들로 리팩토링하자. 이렇게 하면 의도를 파악하기 쉽고 고립된 방식으로 잘 표현되어 테스트가 용이하다.

### 성능

만약 데이터가 수백만건 이상 처리해야 한다면 성능이 최우선 고려 대상이나, 그 외는 코드를 깔끔하게 유지하는데 신경쓰자.  
예상보다 성능이 나쁘지 않을 수 있고, 일반적으로 성능 유지시 가독성이 낮고 유지 보수 비용이 증가한다.  
반대로 깔끔할 설계는 성능 최적화를 즉시 대응할 수 있는 최선희 보호막이다.  
성능이 당장 문제될 경우 리팩토링 전 후 코드를 테스트하는 작은 테스트코드를 작성해 성능을 측정하자.

***

# 더 큰 설계 문제