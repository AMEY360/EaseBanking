import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class TransferServlet extends HttpServlet {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/EaseBanking";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "AJ@2004";

    // Directory to store Excel files
    private static final String EXCEL_DIRECTORY = "C:/apache-tomcat-7.0.39/webapps/EaseBanking/User_Excels/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get transfer details from the request
        String username = request.getParameter("username");
        String accountNumber = request.getParameter("account_number");
        String receiverUsername = request.getParameter("receiver_username");
        String receiverAccountNumber = request.getParameter("receiver_account_number");
        double amount = Double.parseDouble(request.getParameter("amount"));

        // Variables to hold the user's and receiver's balance
        double userBalance = 0.0;
        double receiverBalance = 0.0;

        // Connection and PreparedStatement for database operations
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Check if the user exists and retrieve the balance
            String checkUserQuery = "SELECT balance FROM user WHERE account_number = ? AND username = ?";
            stmt = conn.prepareStatement(checkUserQuery);
            stmt.setString(1, accountNumber);
            stmt.setString(2, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                userBalance = rs.getDouble("balance");
            } else {
                request.setAttribute("errorMessage", "User not found or invalid credentials.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Check if the receiver exists and retrieve the balance
            String checkReceiverQuery = "SELECT balance FROM user WHERE account_number = ? AND username = ?";
            stmt = conn.prepareStatement(checkReceiverQuery);
            stmt.setString(1, receiverAccountNumber);
            stmt.setString(2, receiverUsername);
            rs = stmt.executeQuery();

            if (rs.next()) {
                receiverBalance = rs.getDouble("balance");
            } else {
                request.setAttribute("errorMessage", "Receiver not found or invalid credentials.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Check if the user has sufficient balance
            if (userBalance < amount) {
                request.setAttribute("errorMessage", "Insufficient balance.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Proceed with the transfer: deduct from user's balance and add to receiver's balance
            String updateUserBalanceQuery = "UPDATE user SET balance = balance - ? WHERE account_number = ?";
            String updateReceiverBalanceQuery = "UPDATE user SET balance = balance + ? WHERE account_number = ?";

            // Start transaction
            conn.setAutoCommit(false);

            // Deduct from user
            stmt = conn.prepareStatement(updateUserBalanceQuery);
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();

            // Add to receiver
            stmt = conn.prepareStatement(updateReceiverBalanceQuery);
            stmt.setDouble(1, amount);
            stmt.setString(2, receiverAccountNumber);
            stmt.executeUpdate();

            // Commit the transaction
            conn.commit();

            // Set success message
            request.setAttribute("transferMessage", "Amount transferred successfully!");

            // Create or update the Excel file with the transfer details
            updateTransactionExcel(username, accountNumber, receiverUsername, receiverAccountNumber, amount);

            // Forward to the transfer page with the success message
            RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            // In case of an error, roll back the transaction
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Log the error
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing the transfer.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
            dispatcher.forward(request, response);

        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTransactionExcel(String username, String senderAccountNumber, String receiverUsername, String receiverAccountNumber, double amount) throws IOException {
        // Define the Excel file path using the username as the file name in "C:/apache-tomcat-7.0.39/webapps/EaseBanking/User_Excels"
        String excelFilePath = EXCEL_DIRECTORY + username + ".xlsx";

        // Ensure the directory exists
        File directory = new File(EXCEL_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Check if the file already exists
        File excelFile = new File(excelFilePath);
        Workbook workbook = null;
        Sheet sheet = null;

        if (excelFile.exists()) {
            // If file exists, open it
            FileInputStream fis = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } else {
            // If file doesn't exist, create a new one
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Transactions");
            // Create headers for the Excel sheet
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Sender Username");
            headerRow.createCell(1).setCellValue("Sender Account Number");
            headerRow.createCell(2).setCellValue("Transaction Type");
            headerRow.createCell(3).setCellValue("Receiver Account Number");
            headerRow.createCell(4).setCellValue("Amount");
            headerRow.createCell(5).setCellValue("Receiver Username");
        }

        // Find the last row to append new transaction
        int lastRowNum = sheet.getLastRowNum();
        Row newRow = sheet.createRow(lastRowNum + 1);

        // Add transaction details to the new row
        newRow.createCell(0).setCellValue(username);
        newRow.createCell(1).setCellValue(senderAccountNumber);
        newRow.createCell(2).setCellValue("Transfer"); // Assuming transaction type is "Transfer"
        newRow.createCell(3).setCellValue(receiverAccountNumber);
        newRow.createCell(4).setCellValue(amount);
        newRow.createCell(5).setCellValue(receiverUsername);

        // Write the updated workbook to the file
        FileOutputStream fos = new FileOutputStream(excelFile);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
