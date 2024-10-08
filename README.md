# JAVA DB 프로젝트: 티니핑 대도서관 (도서 대여 관리 프로그램)

## 팀 정보
| 항목   | 내용              |
|--------|------------------|
| 팀명   | Code Typing      |
| 팀장   | 이승호           |
| 팀원   | 류재영, 천성윤, 최준호 |

## 프로젝트 개요
티니핑 대도서관은 회원 관리, 도서 대여 및 반납 기능을 포함한 도서관 관리 시스템입니다. 회원은 일반, 연체, 퇴출, 관리자 등으로 분류되며, 도서관 관리자는 도서와 회원을 효율적으로 관리할 수 있습니다. 회원가입 시 유효성 검사, 연체 관리, 도서 검색 및 필터링, 통계 기능 등을 통해 도서관 운영을 체계적으로 지원합니다.

## 1. 데이터베이스 설계
- **데이터베이스명**: Library_Management_System

### UserTbl (회원 정보 테이블)
| 컬럼명            | 설명                          |
|-------------------|-------------------------------|
| memberID          | 회원 고유 번호                |
| name              | 회원 이름                    |
| memberGrade       | 회원 등급 (일반, 퇴출, 관리자) |
| ID                | 로그인 ID                    |
| PW                | 로그인 비밀번호              |
| phone             | 전화번호                     |
| overdueCount      | 연체 횟수                    |
| currentRentalCount| 현재 대여 중인 도서 수       |

### BookTbl (도서 정보 테이블)
| 컬럼명            | 설명                          |
|-------------------|-------------------------------|
| bookID            | 도서 고유 번호                |
| bookName          | 도서명 (티니핑 이름)         |
| author            | 저자                          |
| publisher         | 출판사                       |
| category          | 도서 카테고리                |
| quantity          | 도서 수량                    |

### RentalTbl (도서 대여 정보 테이블)
| 컬럼명            | 설명                          |
|-------------------|-------------------------------|
| rentalID          | 대여 회원 고유 번호           |
| memberID          | 대여 회원 고유 번호           |
| rentalDate        | 대여 시작일                   |
| dueDate           | 대여 종료일                   |
| returnDate        | 반납일                        |
| overdue           | 연체 진행일                   |
| extended          | 대여 고유 번호                |
| quantity          | 대여한 수량                   |

## 2. 기능 설계
### 패키지명: dpPackage

#### 2.1. 회원 관리
**Member 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `registerMember()`| 회원가입 (ID, PW, 전화번호, 이름을 DB 조건에 맞게 입력) |
| `Member Login()`  | 로그인                                 |

**회원 등급**
| 등급       | 설명                                 |
|------------|--------------------------------------|
| 일반 회원  | 정상적으로 대여 및 반납을 진행하는 회원 |
| 퇴출 회원  | 연체가 누적되어 서비스 이용이 제한된 회원 |
| 관리자     | 도서관 시스템을 관리하는 관리자     |

#### 2.2. 도서 관리
**Book 클래스**
| 메소드            | 설명                                 |
|-------------------|--------------------------------------|
| `searchBook()`    | 도서 검색 (제목, 카테고리, 저자별 필터링) |
| `rentBook()`      | 도서 대여                           |
| `returnBook()`    | 도서 반납                           |
| `extendRentalPeriod()` | 대여 연장 요청                 |

**Rental 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `Rental()`        | 도서 대여 정보를 관리하는 기능 제공   |

#### 2.3. 관리자 기능
**Admin 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `manageUsers()`   | 전체 회원 목록 관리                   |
| `manageBooks()`   | 전체 도서 목록 관리                   |
| `updateMemberLevel()` | 회원 등급 수정                   |
| `deleteMember()`  | 회원 삭제                             |
| `viewMemberInfo()` | 회원 정보 조회                       |
| `checkExtensionStatus()` | 연장 승인 여부 확인           |

#### 2.4. 메뉴 기능
**Menu 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `JoinTitle()`     | 타이틀 화면 출력                       |
| `MainMenu()`      | 접속경로 선택                         |
| `Member loginMenu()` | 로그인 메뉴                       |
| `UserMenu()`      | 사용자 메뉴                           |
| `adminMenu()`     | 관리자 메뉴                           |

#### 2.5. Main 메소드 기능
**Main 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `Main()`          | 접속 타이틀 화면 출력 및 메인 메뉴 호출 |

#### 2.6. 데이터베이스 연결 기능
**DB Connection 클래스**
| 메소드            | 설명                                   |
|-------------------|----------------------------------------|
| `DBConnection()`   | 데이터베이스 연결 관리                |
