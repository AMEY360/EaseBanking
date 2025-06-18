import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");
        HttpSession session = request.getSession();
        String generatedOTP = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");

        if (enteredOTP != null && enteredOTP.equals(generatedOTP)) {
            // Send success email
            EmailUtility.sendEmail(email, "Signup Successful", "Congratulations! Your signup is successful.");
            
            // Redirect to login page
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("verify.jsp").forward(request, response);
        }
    }
}
