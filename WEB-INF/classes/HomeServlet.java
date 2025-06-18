import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetching session attributes
        String username = (String) request.getSession().getAttribute("username");
        String accountNo = (String) request.getSession().getAttribute("account_number");
        String phone_number = (String) request.getSession().getAttribute("phone_number");
        String email = (String) request.getSession().getAttribute("email");


        // If session attributes are not available, redirect to login page
        if (username == null || accountNo == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Set the attributes in the request to be accessed in the JSP page
        request.setAttribute("username", username);
        request.setAttribute("account_number", accountNo);

        // Forward the request to home.jsp to display user info
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}

