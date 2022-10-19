# OnTheWayToWork


## 프로젝트 시작 동기

> 시위를 막을 수는 없어도, 미리 알고 피할 수 있지 않을까?

한달 전, 여느때와 다름없던 출근길에 장애인 이동권 보장 관련 시위로 인해 지하철이 멈췄습니다.

출근 중이던 다른 승객들과 함께 꼼짝없이 운행 재개만을 기다리던 중 이런 생각이 들었습니다.

'오늘 시위하는 줄 알았으면 아침에 조금 더 일찍 일어나서 나왔을텐데'
'미리 알았으면 버스타고 갔을텐데'

출근 준비로 바쁜 나 대신 시위 정보와 지하철 운행 지연 여부를 알아보고 알려주는 OWW 프로젝트를 시작하게 되었습니다.

## 기술 스택
- 언어 : Java 11
- 프레임워크 : SpringBoot 2.7.x
- DB : AWS RDS(MySql)
- 서버 : AWS EC2(Ubuntu 20.04)
- 빌드 : Gradle 7.5.x
- 라이브러리 : JSoup, OpenFeign

## 개발 기간
- ver0.9 : 2022년 9월 24일 ~ 2022년 10월 11일

## ToDo
- 로깅 - 새로운 시위 정보 확인 및 알림 전송 시 확인 용도
- CI/CD 자동화
- 정보 수집처 및 알림 제공 메신저 확대
