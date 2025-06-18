<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transfer</title>
    <script>
        // Function to show the message modal
        function showMessage(message) {
            var modal = document.getElementById('messageModal');
            var modalMessage = document.getElementById('modalMessage');
            modalMessage.innerText = message;
            modal.style.display = 'block'; // Show the modal
        }

        // Function to close the modal
        function closeModal() {
            var modal = document.getElementById('messageModal');
            modal.style.display = 'none'; // Hide the modal
        }
    </script>
    <style>
        /* Ensure the background video covers the entire page */
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: Arial, sans-serif;
            overflow: hidden;
        }

        /* Video background */
        .background-video {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover; /* Ensure the video covers the entire screen */
            z-index: -1; /* Ensure video is behind all other content */
        }

        /* Header Styles */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background: rgba(255, 255, 255, 0.7);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px); /* Apply blur effect */
        }

        .header .logo img {
            width: 120px;
        }

        .nav {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .nav a {
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
            padding: 10px 15px;
            border-radius: 5px;
            transition: background-color 0.3s, color 0.3s;
        }

        .nav a:hover {
            background-color: #007bff;
            color: white;
        }

        .logout-button {
            display: inline-block;
            padding: 8px 15px;
            color: #fff;
            background-color: #00ff04;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            font-size: 14px;
            cursor: pointer;
            margin-left: 10px;
        }

        .logout-button:hover {
            background-color: #ff0000;
        }

        /* Container blur effect */
        .form-container {
            width: 400px;
            margin: 50px auto;
            background: rgba(255, 255, 255, 0.7); /* Slightly transparent background */
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
            filter: blur(2px); /* Apply blur effect to the container */
            transition: filter 0.3s; /* Smooth transition for blur effect */
        }

        .form-container:hover {
            filter: blur(0px); /* Remove blur on hover */
        }

        .form-container h2 {
            text-align: center;
            color: #333;
        }

        .form-container label {
            display: block;
            margin: 10px 0 5px;
            font-size: 14px;
            color: #555;
        }

        .form-container input {
            width: 90%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .form-container button {
            width: 100%;
            padding: 10px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }

        .form-container button:hover {
            background: #0056b3;
        }

        .error {
            color: red;
            font-size: 12px;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5); /* Dark background */
            text-align: center;
        }

        .modal-content {
            background-color: #fff;
            margin: 15% auto;
            padding: 30px;
            border-radius: 10px;
            width: 50%;
        }

        .modal-content h2 {
            margin: 0;
        }

        .close-btn {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .close-btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <!-- Background video -->
    <video class="background-video" autoplay muted loop>
        <source src="images/transactions1.mp4" type="video/mp4">
        Your browser does not support the video tag.
    </video>
   
    <!-- Header Section -->
    <div class="header">
        <div class="logo">
            <img src="images/Logo_img.png" alt="EaseBanking Logo">
        </div>

        <div class="nav">
            <a href="home.jsp">Home</a>
            <a href="deposit.jsp">Deposit</a>
            <a href="transfer.jsp">Transfer</a>
            <a href="transaction.jsp">Transaction</a>
            <a href="ProfileServlet">Profile</a>
            <form action="LogoutServlet" method="post" style="display:inline;">
                <button type="submit" class="logout-button">Logout</button>
            </form>
        </div>
    </div>

    <!-- Transfer Form Section -->
    <div class="form-container">
        <h2>Transfer Funds</h2>
        <form action="TransferServlet" method="post">
            <label for="username">Your Username:</label>
            <input type="text" id="username" name="username" required readonly value="<%= session.getAttribute("username") %>">

            <label for="account_number">Your Account Number:</label>
            <input type="text" id="account_number" name="account_number" required readonly value="<%= session.getAttribute("account_number") %>">

            <label for="receiver_username">Receiver's Username:</label>
            <input type="text" id="receiver_username" name="receiver_username" required>

            <label for="receiver_account_number">Receiver's Account Number:</label>
            <input type="text" id="receiver_account_number" name="receiver_account_number" required>

            <label for="amount">Amount to Transfer:</label>
            <input type="number" id="amount" name="amount" required min="1">

            <div class="error" id="error-message">
                <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
            </div>

            <button type="submit">Transfer</button>
        </form>
    </div>

    <!-- Modal for displaying success message -->
    <div id="messageModal" class="modal">
        <div class="modal-content">
            <h2 id="modalMessage">
                <%= request.getAttribute("transferMessage") != null ? request.getAttribute("transferMessage") : "" %>
            </h2>
            <button class="close-btn" onclick="closeModal()">Close</button>
        </div>
    </div>

    <script>
        // If there's a message set, show it as a pop-up
        <% if (request.getAttribute("transferMessage") != null) { %>
            showMessage("<%= request.getAttribute("transferMessage") %>");
        <% } %>
    </script>

</body>
</html>
