import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/EaseBanking";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "AJ@2004";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String accountNo = request.getParameter("account_number");
        String phone_number = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String setPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!setPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(setPassword, BCrypt.gensalt());

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String checkQuery = "SELECT * FROM user WHERE account_number = ? OR email = ?";
            try (PreparedStatement pstmt = con.prepareStatement(checkQuery)) {
                pstmt.setString(1, accountNo);
                pstmt.setString(2, email);
                if (pstmt.executeQuery().next()) {
                    request.setAttribute("errorMessage", "Account already exists with this Account Number or Email.");
                    request.getRequestDispatcher("signup.jsp").forward(request, response);
                    return;
                }
            }

            // Insert user into database
            String insertQuery = "INSERT INTO user (account_number, username, phone_number, email, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                pstmt.setString(1, accountNo);
                pstmt.setString(2, username);
                pstmt.setString(3, phone_number);
                pstmt.setString(4, email);
                pstmt.setString(5, hashedPassword);
                pstmt.executeUpdate();

                // Generate OTP
                String otp = generateOTP();

                // Store OTP in session
                HttpSession session = request.getSession();
                session.setAttribute("otp", otp);
                session.setAttribute("email", email);

                // Send OTP via email
                EmailUtility.sendEmail(email, "EaseBanking OTP", "Your OTP is: " + otp);

                // Redirect to OTP verification page
                request.getRequestDispatcher("verify.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}
