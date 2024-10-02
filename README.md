What's Next?
=============
사람들의 관심이 급증하는 주식을 찾아 사용자에게 알람을 알려주는 서비스입니다.<br>
Rest API형 서버로써 클라이언트는 프로토타입으로 제작하여 서버 공부에 좀 더 집중할 수 있도록 하고 있습니다.<br>
자세한 구현 내용은 PR에서 확인하실 수 있습니다. Business Rule, 기술적인 문제에 대한 해결 방법은 WIKI에서 확인할 수 있습니다.<br>

## 프로젝트의 전체적인 구조
- github hook을 받아 Jenkins에서 CI/CD를 진행합니다.
- 모든 서버는 Naver Cloud Platform에 올라가 있습니다.<br>
- 구동중인 서버 : Main Delfood Server, Redis Server, MariaDB Server
- public IP는 Main Server에만 할당되어 있습니다.


## 프로젝트의 주요 관심사
<b>공통사항</b><br>
- 지속적인 성능 개선
- 나쁜 냄새가 나는 코드에 대한 리팩토링
<br><br>
<b>코드 컨벤션</b><br>
- Google code Style을 준수
- STS Check Style 플러그인을 적용하여 코드 컨벤션을 유지 
- 링크 https://google.github.io/styleguide/javaguide.html
<br><br>
<b>성능 최적화</b><br>
- 서버 부하를 줄이기 위해 캐싱 서버 적극 활용
- DB서버와의 통신을 최소화(당연한 이야기지만 N+1 쿼리를 지양)
- 인덱스와 쿼리 튜닝을 활용
- 비동기를 활용하여 빠른 시간 내에 외부 API 호출
<br><br>
<b>그 외</b><br>
- Project Wiki를 참고해주세요!
<br><br>

### 브랜치 관리 전략
GitHub Flow를 사용하여 브랜치를 관리합니다.<br>
모든 브랜치는 Pull Request에 리뷰를 진행한 후 merge를 진행합니다.<br>
메인 브렌치인 Develop에는 아직 많은 내용이 merge되지 않았습니다. 현재 개발 진행사항을 확인하고 싶다면 PR를 확인해주세요.<br><br>
DelFood PR : https://github.com/f-lab-edu/food-delivery/pulls
<br>
<br><br>
![image](https://user-images.githubusercontent.com/46917538/72450182-44475300-37fd-11ea-8a1b-ecce20fd6fcb.png)
<br><br>
- Main : 배포시 사용합니다. 아직 배포단계에 이르지 않아 Master 브랜치에 내용이 없습니다.
- Feature : 기능 개발을 진행할 때 사용합니다.
<br><br>
<b>브랜치 관리 전략 참고 문헌</b><br>

### 테스트
- Mockito Framework를 활용하여 고립된 테스트 코드를 작성
- Jenkins CI를 적용하여 테스트 자동화
- 협업하는 동료의 소스코드에 서로 테스트코드를 작성하여 서로의 소스코드를 알 수 있도록 하고 있습니다.
<br><br>

### 성능 테스트
NGrinder를 설치하여 테스트하고있습니다.<br>


## 사용 기술 및 환경
Spring boot, Maven, Mybatis, Redis, Docker, MariaDB, Jenkins, Java8, Naver Cloud Platform
<br>

## Wiki
<[https://github.com/Hot-stock/backend/wiki](https://github.com/Hot-stock/backend/wiki)><br>
Wiki에 기술 이슈에 대한 고민과 해결 방법을 포스팅한 개인 블로그의 url이 포함되어있습니다.<br>
<br>
## CI
Jenkins : 서버 운영을 종료하였습니다.<br>
Naver Cloud Platform(Cloud server)를 사용하고 있습니다.<br>
PR시마다 자동 Build 및 Test 적용<br>
비로그인 상태로도 확인이 가능합니다.<br>

## CD
Docker 이미지를 제작하여 배포합니다.<br>
CI 서버에서 빌드 완료시 Shell script가 작동하여 빌드된 이미지가 docker hub에 저장됩니다.<br>
Push 완료시 Delfood 메인 서버에서 docker hub에 올라간 이미지를 받아 실행시킵니다.<br>

<br>
## Database
- MariaDB<br>
cafe24 web hosting 서비스를 사용하고 있습니다.
- Redis<br>
docker 컨테이너를 사용하고 있습니다.
<br>

## 프로젝트 DB ERD
2019-11-08 수정 (쿠폰 스키마 추가)
