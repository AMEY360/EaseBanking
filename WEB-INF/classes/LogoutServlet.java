import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the session
        HttpSession session = request.getSession(false); // Fetch the session if it exists
        if (session != null) {
            session.invalidate(); // Destroy the session
        }
        // Redirect to login page
        response.sendRedirect("login.jsp");
    }
}
