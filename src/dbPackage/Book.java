package dbPackage; // dbPackage라는 패키지에 속하는 클래스

import java.sql.*; // JDBC API를 사용하기 위한 패키지
import java.util.Scanner; // 입력 값 패키지

// Book 클래스는 도서의 정보를 저장하고 관리하는 역할을 한다.
public class Book {
    // 도서의 속성 정의
    private int bookId; // 도서 ID
    private String title; // 도서 제목
    private String author; // 도서 저자
    private String publisher; // 도서 출판사
    private String publicationDate; // 도서 출판일
    private String category; // 도서 카테고리
    private int quantity; // 도서 수량
    private boolean isRented; // 대여 여부

    // 생성자: 도서 정보를 초기화하는 역할
    public Book(int bookId, String title, String author, String publisher, String publicationDate, String category,
                int quantity, boolean isRented) {
        this.bookId = bookId; // 도서 ID 초기화
        this.title = title; // 도서 제목 초기화
        this.author = author; // 도서 저자 초기화
        this.publisher = publisher; // 도서 출판사 초기화
        this.publicationDate = publicationDate; // 도서 출판일 초기화
        this.category = category; // 도서 카테고리 초기화
        this.quantity = quantity; // 도서 수량 초기화
        this.isRented = isRented; // 대여 여부 초기화
    }

    // Getter 메서드: 각 속성 값을 반환하는 메서드들
    public int getBookID() {
        return bookId; // 도서 ID 반환
    }

    public String getBookName() {
        return title; // 도서 제목 반환
    }

    public String getAuthor() {
        return author; // 도서 저자 반환
    }

    public String getPublisher() {
        return publisher; // 도서 출판사 반환
    }

    public String getPublicationDate() {
        return publicationDate; // 도서 출판일 반환
    }

    public String getCategory() {
        return category; // 도서 카테고리 반환
    }

    public int getQuantity() {
        return quantity; // 도서 수량 반환
    }

    public boolean isRented() {
        return isRented; // 대여 여부 반환
    }

    // 도서 제목을 기반으로 도서를 검색하는 메서드
    public static void searchBook() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DBConnection.getConnection(); // 데이터베이스 연결

        int choice = 0;

        // 사용자가 올바른 선택을 할 때까지 반복
        while (choice < 1 || choice > 3) {
            System.out.println("검색할 기준을 선택하세요:");
            System.out.println("1. 도서명");
            System.out.println("2. 저자");
            System.out.println("3. 카테고리");
            System.out.print("선택 (1/2/3): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // 줄바꿈 제거

                if (choice < 1 || choice > 3) {
                    System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
                }
            } else {
                System.out.println("숫자를 입력해주세요.");
                scanner.next(); // 잘못된 입력 처리 (숫자가 아닌 입력)
            }
        }

        String query = "SELECT * FROM booktbl WHERE ";
        String filter = "";
        String searchValue = ""; // 검색할 값을 저장할 변수

        switch (choice) {
            case 1:
                System.out.print("검색할 도서명을 입력하세요: ");
                searchValue = scanner.nextLine();
                filter = "bookName LIKE ?";
                break;
            case 2:
                System.out.print("검색할 저자를 입력하세요: ");
                searchValue = scanner.nextLine();
                filter = "author LIKE ?";
                break;
            case 3:
                System.out.print("검색할 카테고리를 입력하세요: ");
                searchValue = scanner.nextLine();
                filter = "category LIKE ?";
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                return; // 잘못된 선택 시 메서드 종료
        }

        query += filter; // 쿼리에 필터 추가
        PreparedStatement pstmt = conn.prepareStatement(query); // PreparedStatement 객체 생성

        // 사용자 입력에 따른 LIKE 조건 설정
        pstmt.setString(1, "%" + searchValue + "%");

        ResultSet rs = pstmt.executeQuery(); // 쿼리 실행하여 결과 집합 가져오기

        // 결과 집합을 순회하며 도서 정보를 출력
        while (rs.next()) {
            int bookId = rs.getInt("bookId");
            String bookName = rs.getString("bookName");
            String authorName = rs.getString("author");
            String publisher = rs.getString("publisher");
            String categoryName = rs.getString("category");
            int quantity = rs.getInt("quantity");

            // 도서 정보를 출력
            System.out.println("도서 ID: " + bookId + ", 제목: " + bookName + ", 저자: " + authorName + ", 출판사: " + publisher
                    + ", 카테고리: " + categoryName + ", 수량: " + quantity);
        }

        // 자원 해제
        rs.close();
        pstmt.close();
        conn.close();
    }

    // 도서를 대여하는 메서드
    public static void rentBook(Member member) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DBConnection.getConnection(); // 데이터베이스 연결

        try {
            // 트랜잭션 시작
            conn.setAutoCommit(false);

            System.out.println("대여할 책 제목을 입력해주세요: ");
            String bookTitle = scanner.nextLine(); // 책 제목을 문자열로 입력받기

            String query = "SELECT * FROM booktbl WHERE bookName LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + bookTitle + "%"); // 사용자 입력에 따른 LIKE 조건 설정

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("해당 책이 존재하지 않습니다.");
                return;
            }

            do {
                int bookId = rs.getInt("bookId");
                String bookName = rs.getString("bookName");
                String authorName = rs.getString("author");
                String publisher = rs.getString("publisher");
                String categoryName = rs.getString("category");
                int quantity = rs.getInt("quantity");

                // 도서 정보를 출력
                System.out.println("도서 ID: " + bookId + ", 제목: " + bookName + ", 저자: " + authorName + ", 출판사: "
                        + publisher + ", 카테고리: " + categoryName + ", 수량: " + quantity);

                if (quantity <= 0) {
                    System.out.println("해당 도서는 대여 가능한 수량이 없습니다.");
                    return;
                }

                System.out.println("해당 도서를 대여 하시겠습니까?: Y / N");
                String select = scanner.nextLine();

                if (!select.equalsIgnoreCase("Y")) {
                    System.out.println("도서 대여를 취소하셨습니다.");
                    return;
                }

                // 1. 대여 기록 삽입
                String insertRentalQuery = "INSERT INTO rentalTbl (rentalID, memberID, bookID, rentalDate, dueDate) VALUES (null, ?, ?, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 14 DAY))";
                PreparedStatement insertRentalPstmt = conn.prepareStatement(insertRentalQuery);
                insertRentalPstmt.setString(1, member.getMemberId()); // 실제 대여자의 ID
                insertRentalPstmt.setInt(2, bookId); // 대여된 도서 ID
                int rentalRowsInserted = insertRentalPstmt.executeUpdate();

                if (rentalRowsInserted <= 0) {
                    System.out.println("도서 대여 기록 업데이트에 실패했습니다.");
                    conn.rollback();
                    return;
                }

                // 2. 도서 수량 감소
                String updateBook = "UPDATE booktbl SET quantity = quantity-1 WHERE bookId = ?";
                PreparedStatement updatePstmt = conn.prepareStatement(updateBook);
                updatePstmt.setInt(1, bookId); // 도서 ID를 이용하여 해당 책을 업데이트
                int rowsUpdated = updatePstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("도서 대여가 완료되었습니다.");
                    conn.commit(); // 성공 시 커밋
                } else {
                    System.out.println("도서 대여에 실패하였습니다.");
                    conn.rollback(); // 실패 시 롤백
                }

                insertRentalPstmt.close(); // 자원 해제
                updatePstmt.close(); // 자원 해제

            } while (rs.next());

            rs.close();
            pstmt.close();
            scanner.close();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                conn.rollback(); // 오류 발생 시 롤백
            }
        } finally {
            // 자원 해제 및 트랜잭션 종료
            if (conn != null) {
                conn.setAutoCommit(true); // 자동 커밋 모드로 복구
                conn.close();
            }

        }
    }

    // 도서를 반납하는 메서드
    public static void returnBook(Member member) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DBConnection.getConnection();
        System.out.println("반납할 책 제목을 입력해주세요: ");
        String bookTitle = scanner.nextLine();
        try {
            conn.setAutoCommit(false); // 수동으로 트랜잭션 관리

            String updateBookQuery = "UPDATE booktbl SET quantity = quantity + 1 WHERE bookName = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
            updateBookStmt.setString(1, bookTitle);
            int bookupdateRows = updateBookStmt.executeUpdate();

            if (bookupdateRows == 0) {
                System.out.println("해당 도서를 찾을 수 없습니다.");
                conn.rollback(); // 해당 도서를 찾을 수 없을 시 롤백
                return;
            }

            // 반납 확인 메시지 출력
            System.out.println("해당 도서를 반납하시겠습니까? : Y / N");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("Y")) {
                String updateRentalQuery = "UPDATE booktbl SET rentalDate = ? WHERE bookid = ? AND rentalDate IS NULL";
                PreparedStatement updateRentalStmt = conn.prepareStatement(updateRentalQuery);
                updateRentalStmt.setDate(1, new java.sql.Date(System.currentTimeMillis())); // 현재 날짜로 반납 날짜 설정
                updateRentalStmt.setInt(2, returnBookId);
                int rentalupdateRows = updateRentalStmt.executeUpdate();

                System.out.println("도서 반납이 완료되었습니다."); // 반납 완료 메시지 출력
                conn.commit(); // 수동 커밋
                updateRentalStmt.close(); // 자원 정리 : PreparedStatement 닫기
            } else if (userInput.equalsIgnoreCase("N")) {
                System.out.println("도서 반납이 취소되었습니다.");
                conn.rollback(); // 반납이 취소되면 롤백
            } else {
                System.out.println("잘못된 입력입니다. 반납이 취소되었습니다.");
                conn.rollback(); // 잘못된 입력해도 롤백
            }
            updateBookStmt.close(); // 자원 정리: PreparedStatement 닫기

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // 예외 발생시 롤백
            }
            e.printStackTrace();
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true); // 다시 자동 커밋 모드로
                conn.close(); // 디비 커넥션 닫기
            }
        }
    }

    // ======================================= ADMIN 구역 ==============================================

    // 도서를 등록하는 메서드
    public static void addBook(Book book) throws SQLException {
        Connection conn = DBConnection.getConnection(); // 데이터베이스 연결
        String query = "INSERT INTO books (title, author, publisher, publicationDate, category, quantity, isRented) VALUES (?, ?, ?, ?, ?, ?, ?)"; // 도서
        // 등록
        // SQL
        // 쿼리
        PreparedStatement pstmt = conn.prepareStatement(query); // PreparedStatement 객체 생성
        pstmt.setString(1, book.getBookName()); // 도서 제목 설정
        pstmt.setString(2, book.getAuthor()); // 저자 설정
        pstmt.setString(3, book.getPublisher()); // 출판사 설정
        pstmt.setString(4, book.getPublicationDate()); // 출판일 설정
        pstmt.setString(5, book.getCategory()); // 카테고리 설정
        pstmt.setInt(6, book.getQuantity()); // 수량 설정
        pstmt.setBoolean(7, book.isRented()); // 대여 여부 설정

        pstmt.executeUpdate(); // 쿼리 실행하여 도서 추가

        // 자원 정리: PreparedStatement, Connection 닫기
        pstmt.close();
        conn.close();
        System.out.println("도서 등록이 완료되었습니다."); // 등록 완료 메시지 출력
    }

    // 도서를 삭제하는 메서드
    public static void deleteBook(int bookId) throws SQLException {
        Connection conn = DBConnection.getConnection(); // 데이터베이스 연결
        String query = "DELETE FROM books WHERE bookId = ?"; // 도서 삭제 SQL 쿼리
        PreparedStatement pstmt = conn.prepareStatement(query); // PreparedStatement 객체 생성
        pstmt.setInt(1, bookId); // 삭제할 도서 ID 설정

        int affectedRows = pstmt.executeUpdate(); // 쿼리 실행하여 삭제
        if (affectedRows > 0) {
            System.out.println("도서 삭제가 완료되었습니다."); // 삭제 완료 메시지 출력
        } else {
            System.out.println("삭제할 도서가 존재하지 않습니다."); // 도서가 없을 경우 메시지 출력
        }

        // 자원 정리: PreparedStatement, Connection 닫기
        pstmt.close();
        conn.close();
    }
}
