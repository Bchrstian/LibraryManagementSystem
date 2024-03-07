package View;

import Model.Book;
import Model.LibraryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryAppView {
    public static void main(String[] args) {
        boolean condition = true;

        while (condition) {
            displayMenu();

            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    registerBook(input);
                    break;
                case 2:
                    updateBookRecord(input);
                    break;
                case 3:
                    retrieveAllBooks();
                    break;
                case 4:
                    deleteBookById(input);
                    break;
                case 5:
                    findBookById(input);
                    break;
                case 6:
                    findBookByName(input);
                    break;
                case 0:
                    System.out.println("Thank you for Using the system!");
                    condition = false;
                    break;
                default:
                    System.out.println("Wrong Choice!!!!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("===================");
        System.out.println("LIBRARY MGT SYSTEM");
        System.out.println("===================");
        System.out.println("1. Register Book");
        System.out.println("2. Update Book Record");
        System.out.println("3. Retrieve All Books");
        System.out.println("4. Delete Book By Id");
        System.out.println("5. Find Book by Id");
        System.out.println("6. Find Book By Name");
        System.out.println("0. Exit");
        System.out.println("-------------");
        System.out.println("Choose: ");
    }

    private static void registerBook(Scanner input) {
        System.out.println("Enter Book ID: ");
        int bookId = input.nextInt();
        System.out.println("Enter Book Title: ");
        String bookTitle = input.next();

        try (Connection con = LibraryDatabase.getConnection()) {
            String sql = "INSERT INTO book (book_id, title) values (?, ?)";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, bookId);
                st.setString(2, bookTitle);
                st.executeUpdate();
                System.out.println("Book Registered Successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void updateBookRecord(Scanner input) {
        System.out.println("Enter Book ID to update: ");
        int bookId = input.nextInt();

        try (Connection con = LibraryDatabase.getConnection()) {
            if (isBookExists(con, bookId)) {
                System.out.println("Enter new Book Title: ");
                String newTitle = input.next();

                String sql = "UPDATE book SET title = ? WHERE book_id = ?";
                try (PreparedStatement st = con.prepareStatement(sql)) {
                    st.setString(1, newTitle);
                    st.setInt(2, bookId);
                    st.executeUpdate();
                    System.out.println("Book Record Updated Successfully!");
                }
            } else {
                System.out.println("Book with ID " + bookId + " not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void retrieveAllBooks() {
      try (Connection con = LibraryDatabase.getConnection();
           PreparedStatement st = con.prepareStatement("SELECT * FROM book");
           ResultSet rs = st.executeQuery()) {
          while (rs.next()) {
              System.out.println("Book ID: " + rs.getInt("book_id") + ", Title: " + rs.getString("title"));
          }
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
  }
  

    private static void deleteBookById(Scanner input) {
        System.out.println("Enter Book ID to delete: ");
        int bookId = input.nextInt();

        try (Connection con = LibraryDatabase.getConnection()) {
            if (isBookExists(con, bookId)) {
                String sql = "DELETE FROM book WHERE book_id = ?";
                try (PreparedStatement st = con.prepareStatement(sql)) {
                    st.setInt(1, bookId);
                    st.executeUpdate();
                    System.out.println("Book Deleted Successfully!");
                }
            } else {
                System.out.println("Book with ID " + bookId + " not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void findBookById(Scanner input) {
        System.out.println("Enter Book ID to find: ");
        int bookId = input.nextInt();

        try (Connection con = LibraryDatabase.getConnection()) {
            String sql = "SELECT * FROM book WHERE book_id = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, bookId);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Book ID: " + rs.getInt("book_id") + ", Title: " + rs.getString("title"));
                    } else {
                        System.out.println("Book with ID " + bookId + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void findBookByName(Scanner input) {
        System.out.println("Enter Book Title to find: ");
        String bookTitle = input.next();

        try (Connection con = LibraryDatabase.getConnection()) {
            String sql = "SELECT * FROM book WHERE title = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, bookTitle);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Book ID: " + rs.getInt("book_id") + ", Title: " + rs.getString("title"));
                    } else {
                        System.out.println("Book with Title '" + bookTitle + "' not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isBookExists(Connection con, int bookId) throws SQLException {
        String checkSql = "SELECT 1 FROM book WHERE book_id = ?";
        try (PreparedStatement checkStatement = con.prepareStatement(checkSql)) {
            checkStatement.setInt(1, bookId);
            try (ResultSet checkResultSet = checkStatement.executeQuery()) {
                return checkResultSet.next();
            }
        }
    }
}
