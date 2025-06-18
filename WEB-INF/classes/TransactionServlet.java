import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve username from session (ensure user is logged in)
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null || username.isEmpty()) {
            // Redirect to login page if username is not found in session (user not logged in)
            response.sendRedirect("login.jsp");
            return;
        }

        // Set the file path for the user's Excel statement
        String excelFilePath = "C:/apache-tomcat-7.0.39/webapps/EaseBanking/User_Excels/" + username + ".xlsx";

        File file = new File(excelFilePath);
        if (file.exists()) {
            // Set the content type for Excel file download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + username + "_Statement.xlsx\"");

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        } else {
            // Handle the case when the file does not exist
            request.setAttribute("errorMessage", "Transaction statement not found.");
            request.getRequestDispatcher("transaction.jsp").forward(request, response);
        }
    }
}
