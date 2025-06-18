<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <style>
        /* Background video styling */
        .video-container {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: -1;
        }

        .video-container video {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: Arial, sans-serif;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background: rgba(255, 255, 255, 0.2); /* Semi-transparent background */
            backdrop-filter: blur(10px); /* Blur effect */
            -webkit-backdrop-filter: blur(10px); /* Safari support */
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
            background-color: #00ff1a;
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

        .container {
            width: 50%;
            margin: 100px auto;
            background: rgba(255, 255, 255, 0.2); /* Semi-transparent white */
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 10px rgba(255, 255, 255, 0.2);
            text-align: center;
            backdrop-filter: blur(10px); /* Apply blur effect */
            -webkit-backdrop-filter: blur(10px); /* Safari support */
        }

        .container h1 {
            color: #16fd01;
        }
        

        .container h2 b {
            color: #16fd01;
            background: rgb(204, 200, 200);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .container p {
            color: #ffffff;
        }

        /* Footer section */
        .footer {
            display: flex;
            justify-content: space-around;
            align-items: center;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 10px 0;
            position: absolute;
            bottom: 0;
            width: 100%;
            box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
        }

        .footer .footer-item {
            font-size: 16px;
            color: #333;
        }

        .footer .footer-item span {
            font-weight: bold;
        }

        .footer .footer-item a {
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
            padding: 5px 10px;
            border-radius: 5px;
            transition: background-color 0.3s, color 0.3s;
        }

        .footer .footer-item a:hover {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Background Video -->
    <div class="video-container">
        <video autoplay loop muted>
            <source src="images/background1.mp4" type="video/mp4">
            Your browser does not support the video tag.
        </video>
    </div>

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

    <!-- Main Content -->
    <div class="container">
        <h1>Welcome, ${username}!</h1>
        <h2><i>Account Number: </i>
            <b>
            <input type="password" id="accountNumber" value="${account_number}" readonly 
                   style="border: none; background: transparent; font-size: 25px; letter-spacing: 2px; width: 140px;">
            </b>
            <span onclick="toggleAccountNumber()" style="cursor: pointer; font-size: 40px; margin-left: 5px;">	
                &#128064;
            </span>
        </h2>
        <h1><i>Your money is<br> where you are!!!</i></h1> 
        <p><b>Secure, Efficient & Reliable Banking Platform</b></p>
    </div>

    <script>
        function toggleAccountNumber() {
            var accountInput = document.getElementById("accountNumber");
            if (accountInput.type === "password") {
                accountInput.type = "text";
            } else {
                accountInput.type = "password";
            }
        }
    </script>

    <!-- Footer Section -->
    <div class="footer">
        <div class="footer-item">
            <a href="KnowMore.html">Know More</a>
        </div>
        <div class="footer-item">
            <span>Contact Us: </span>+91-7208108421
        </div>
        <div class="footer-item">
            <a href="FAQ.html">FAQ</a>
        </div>
        <div class="footer-item">
            <span>Email Id: </span>easebanking@gmail.com
        </div>
        <div class="footer-item">
            <a href="AboutUs.html">About Us</a>
        </div>
    </div>
</body>
</html>
