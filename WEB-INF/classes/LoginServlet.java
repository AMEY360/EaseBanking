import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String username = request.getParameter("username");
        String accountNo = request.getParameter("account_number");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/EaseBanking", "root", "AJ@2004");

            // SQL query to check if the user exists with the given username and password
            //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
           // System.out.println(hashedPassword);
           // String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
           String sql = "SELECT password,phone_number,email FROM user WHERE username = ? and account_number=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, accountNo);  
           /* session.setAttribute("phone_number", phone_number);
            session.setAttribute("email", email);
            */
            //in above 2 commands changes to be made

           // pstmt.setString(2, hashedPassword); // Check password without hashing (if not using hashing)

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // User found, redirect to home.jsp
                String hashPassword = rs.getString(1);
                if(BCrypt.checkpw(password.trim(),hashPassword))
                {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);  // Store username in session
                    session.setAttribute("account_number", accountNo);// Set account_number in session
                    response.sendRedirect("home.jsp"); 
                }
                else
                    {
                         request.setAttribute("errorMessage", "Invalid  password.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);

                    }
            } else {
                // User not found, send error message
                request.setAttribute("errorMessage", "Invalid username or password.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
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
}