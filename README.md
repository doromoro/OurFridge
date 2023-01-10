![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=300&section=header&text=Our%20Fridge&fontSize=90&animation=fadeIn&fontAlignY=38&desc=&descAlignY=51&descAlign=62)
    <hr>
    
# 📍 Our Fridge (우리의 냉장고)
사진

## ✨ 프로젝트에 사용한 스택 ✨
<p>
	<img src="https://img.shields.io/badge/Backend-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/spring-boot-6DB33F?style=flat&logo=spring-boot&logoColor=6DB33F" />
	<img src="https://img.shields.io/badge/mariadb-1572B6?style=flat&logo=mariadb&logoColor=003545" />
  <img src="https://img.shields.io/badge/redis-1572B6?style=flat&logo=redis&logoColor=DC382D" />
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=SpringSecurity&logoColor=F8DC75" />
</p>
<p>
	<img src="https://img.shields.io/badge/Frontend-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/spring-boot-6DB33F?style=flat&logo=spring-boot&logoColor=6DB33F" />
</p>
<p>
	<img src="https://img.shields.io/badge/Tool-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/Psostman-FF6C37?style=flat&logo=postman&logoColor=FFFFFF" />
	<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/Tomcat-1572B6?style=flat&logo=ApacheTomcat&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/IntelliJ-1572B6?style=flat&logo=IntelliJIDEA&logoColor=FFFFFF" />
</p>
<p>
	<img src="https://img.shields.io/badge/Cooperation-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/Miro-050038?style=flat&logo=Miro&logoColor=FFFFFF" />
    <img src="https://img.shields.io/badge/GitHub-1572B6?style=flat&logo=GitHub&logoColor=000000" />   
	<img src="https://img.shields.io/badge/Discord-5865F2?style=flat&logo=Discord&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=FFFFFF" />
</p>

## 📍 프로젝트의 기획부터 개발 기간:
개발: 

## :triangular_flag_on_post: 우리의 냉장고

    - ### 프로젝트 개요

  - 🏠 []()

- ### 주요 기능 

  - **나의 냉장고**
    > 1) 사용자의 **냉장고를 만들고** 재료를 추가할 수 있다
    >
    > 2) 냉장고에 있는 재료들로 **레시피를 추천 받**는다. 
  - **계절/랭킹 레시피** 

    > 1) 계절별 특별한 레시피를 **제공**한다. 
    >
    > 2) 레시피를 올리면 **추천을 통해**랭크에 진입한다. 
  - **레시피 추천 서비스**

    > 1) 남은 재료들을 기반으로 **레시피를 추천**해준다.
    >
    > 2) 
- ### 향후 계획
  - **FAQ 및 문의 기능** : 문의 API
  - 
## 📌 목차
* [사용된 도구](#hammer_and_wrench-사용된-도구)

* [사용된 기술](#desktop_computer-사용된-기술)

* [시스템 아키텍쳐](#desktop_computer-시스템-아키텍쳐)

* [서비스 소개](#-서비스-소개)


## :hammer_and_wrench: 사용된 도구

> **Development**
* Spring Boot 3.0.0
* Gradle 7.5.1
* Redis 2.7.6
> **Tools**
* IDE: IntelliJ IDEA 2022.02
* SQL: MariaDB
* GitHub
> **Cooperation**
* Notion
* Discord
* GitHub
* Miro
> **Test**
* Postman
* Swagger


## :desktop_computer: 사용된 기술

> **[ BACK END ]**

- **Spring Boot** : Rest Controller 구현.
- **Spring Security** : SecurityFilterChain을 통한 권한 부여 및 권한에 따른 접근 제어
- **JWT** : JSON Web Token을 활용하여 회원 인증을 진행
- **JPA (Hibernate)** : ORM인 Hibernate를 활용, Entity를 이용한 데이터베이스 접근
- **MariaDB** : RDBMS로 이용
- **Redis** : (key-value) 방식의 비관계형 DBMS, 토큰 및 이메일 인증에 사용
- **OAuth2** : OAuth2를 이용해 소셜 로그인 구현(NAVER, KAKAO, GOOGLE)
- **Mail** : 메일 인증 서비스 구현
> **[ FRONT END ]**

> **[ TEAM Cooperation ]**

## :desktop_computer: 시스템 아키텍쳐
> ERD
![image](https://user-images.githubusercontent.com/120362572/211525669-0ff65b43-d85d-4489-b234-fbb26d3c732a.png)

> 흐름

