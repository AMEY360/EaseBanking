import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/EaseBanking";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "AJ@2004";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch the username from session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Query to fetch user data
            String query = "SELECT account_number, username, phone_number, email, balance FROM user WHERE username = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, username);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Store fetched data in request attributes
                    request.setAttribute("account_number", rs.getString("account_number"));
                    request.setAttribute("username", rs.getString("username"));
                    request.setAttribute("phone_number", rs.getString("phone_number"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("balance", rs.getString("balance")); // Fix case sensitivity issue
                } else {
                    request.setAttribute("errorMessage", "No user found with the given username.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while retrieving user data.");
        }

        // Forward to profile.jsp
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String password = request.getParameter("password");
        String newPhoneNumber = request.getParameter("phone_number");
        String newEmail = request.getParameter("email");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Fetch stored password from the database
            String query = "SELECT password FROM user WHERE username = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, username);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, storedPassword)) {
                        // Password matches, proceed with the update
                        String updateQuery = "UPDATE user SET phone_number = ?, email = ? WHERE username = ?";
                        try (PreparedStatement updatePstmt = con.prepareStatement(updateQuery)) {
                            updatePstmt.setString(1, newPhoneNumber);
                            updatePstmt.setString(2, newEmail);
                            updatePstmt.setString(3, username);
                            updatePstmt.executeUpdate();
                        }

                        // Update session attributes
                        session.setAttribute("phone_number", newPhoneNumber);
                        session.setAttribute("email", newEmail);

                        // Redirect back to profile page to show updated info
                        response.sendRedirect("ProfileServlet");
                    } else {
                        // Invalid password
                        request.setAttribute("errorMessage", "Invalid password.");
                        doGet(request, response);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while updating your profile.");
            doGet(request, response);
        }
    }
}
