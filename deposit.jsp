<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Deposit</title>
    <link rel="stylesheet" type="text/css" href="sign-up.css">
    <style>
        /* Ensure the background video covers the entire page */
        html, body {
            margin: 0;
            padding: 0;
            height: 100%; /* Ensure the body takes up the entire height */
            font-family: Arial, sans-serif;
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

        /* Header blur effect */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background: rgba(255, 255, 255, 0.5); /* Semi-transparent background */
            backdrop-filter: blur(8px); /* Blur effect for the background */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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
            margin-bottom: 15px;
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

        .error, .success {
            color: red;
            font-size: 12px;
            text-align: center;
        }

        .success {
            color: green;
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
        <!-- Logo on the left -->
        <div class="logo">
            <img src="images/Logo_img.png" alt="EaseBanking Logo">
        </div>

        <!-- Navigation bar on the right -->
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
    
    <div class="form-container">
        <h2>Deposit</h2>
        <form action="DepositServlet" method="post">
            <label for="username">Your Username:</label>
            <input type="text" id="username" name="username" required readonly value="<%= session.getAttribute("username") %>">

            <label for="account_number">Your Account Number:</label>
            <input type="text" id="account_number" name="account_number" required readonly value="<%= session.getAttribute("account_number") %>">
            
            <label for="Amount_To_Deposit">Minimum Amount to Deposit is &#8377;500</label>
            <input type="number" id="Amount_To_Deposit" name="Amount_To_Deposit" min="500" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <div class="error">
                <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
            </div>
            <div class="success">
                <%= request.getAttribute("successMessage") != null ? request.getAttribute("successMessage") : "" %>
            </div>

            <button type="submit">Confirm Deposit</button>
        </form>
    </div>
</body>
</html>
