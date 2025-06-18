import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class DepositServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String username = request.getParameter("username");
        String accountNo = request.getParameter("account_number");
        String password = request.getParameter("password");
        String amountToDepositStr = request.getParameter("Amount_To_Deposit");
        double amountToDeposit = 0;

        // Validate Amount to Deposit (Must be >= 500 and positive)
        try {
            amountToDeposit = Double.parseDouble(amountToDepositStr);
            if (amountToDeposit < 500) {
                request.setAttribute("errorMessage", "The minimum deposit amount is 500.");
                request.getRequestDispatcher("deposit.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid amount entered.");
            request.getRequestDispatcher("deposit.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EaseBanking", "root", "AJ@2004");

            // SQL query to retrieve password from database
            String sql = "SELECT password, balance, excel_file_path FROM user WHERE username = ? AND account_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, accountNo);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve the stored hashed password, balance, and excel file path
                String hashPassword = rs.getString("password");
                double currentBalance = rs.getDouble("balance");
                String excelFilePath = rs.getString("excel_file_path");

                // Validate password
                if (BCrypt.checkpw(password.trim(), hashPassword)) {
                    // Update the user's balance
                    double newBalance = currentBalance + amountToDeposit;
                    String updateBalanceSql = "UPDATE user SET balance = ? WHERE username = ? AND account_number = ?";
                    pstmt = conn.prepareStatement(updateBalanceSql);
                    pstmt.setDouble(1, newBalance);
                    pstmt.setString(2, username);
                    pstmt.setString(3, accountNo);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        // Check if Excel file path exists in the database
                        if (excelFilePath == null || excelFilePath.isEmpty()) {
                            // File does not exist, create a new one
                            excelFilePath = updateTransactionExcel(username, accountNo, "Deposit", "", "", amountToDeposit);
                            // Update the database with the path to the Excel file
                            String updateFilePathSql = "UPDATE user SET excel_file_path = ? WHERE username = ? AND account_number = ?";
                            pstmt = conn.prepareStatement(updateFilePathSql);
                            pstmt.setString(1, excelFilePath);
                            pstmt.setString(2, username);
                            pstmt.setString(3, accountNo);
                            pstmt.executeUpdate();
                        } else {
                            // File exists, just update the transaction log
                            updateTransactionExcel(username, accountNo, "Deposit", "", "", amountToDeposit);
                        }
                        request.setAttribute("successMessage", "Amount successfully deposited.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update balance.");
                    }
                } else {
                    request.setAttribute("errorMessage", "Invalid password.");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid username or account number.");
            }

            request.getRequestDispatcher("deposit.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("deposit.jsp").forward(request, response);
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String updateTransactionExcel(String username, String senderAccountNumber, String transactionType, String receiverUsername, String receiverAccountNumber, double amount) {
        // Ensure directory exists
        String directoryPath = "C:/apache-tomcat-7.0.39/webapps/EaseBanking/User_Excels/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Correct file extension
        String filePath = directoryPath + username + ".xlsx";

        XSSFWorkbook workbook;
        XSSFSheet sheet;
        File file = new File(filePath);

        try {
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                    sheet = workbook.getSheetAt(0);
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Transactions");

                // Create header row (same as Transfer Page)
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Sender Username");
                headerRow.createCell(1).setCellValue("Sender Account Number");
                headerRow.createCell(2).setCellValue("Transaction Type");
                headerRow.createCell(3).setCellValue("Receiver Account Number");
                headerRow.createCell(4).setCellValue("Amount");
                headerRow.createCell(5).setCellValue("Receiver Username");
            }

            // Create a new row for the transaction
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount);

            // Add transaction details
            row.createCell(0).setCellValue(username);
            row.createCell(1).setCellValue(senderAccountNumber);
            row.createCell(2).setCellValue(transactionType);
            row.createCell(3).setCellValue(receiverAccountNumber); // Empty for Deposit
            row.createCell(4).setCellValue(amount);
            row.createCell(5).setCellValue(receiverUsername); // Empty for Deposit

            // Write to the Excel file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                workbook.close();  // Ensure workbook is closed
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating/updating Excel file: " + e.getMessage());
        }

        return filePath;
    }
}
