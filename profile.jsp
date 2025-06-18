<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <style>
        /* Ensure the background video covers the entire page */
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: Arial, sans-serif;
        }

        /* Background video */
        .background-video {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            z-index: -1; /* Ensure video is behind all other content */
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background: rgba(255, 255, 255, 0.9);
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

        .container {
            max-width: 600px;
            margin: 50px auto;
            background: rgba(255, 255, 255, 0.9);
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            border-radius: 8px;
        }

        h2 {
            text-align: center;
            color: #333;
        }

        .profile-info {
            font-size: 16px;
            margin-bottom: 20px;
        }

        .update-form input {
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 90%;
            margin-bottom: 10px;
        }

        .update-form label {
            font-weight: bold;
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .error {
            color: red;
            font-weight: bold;
            text-align: center;
        }
    </style>
    <script>
        function toggleEditMode() {
            const updateForm = document.getElementById('updateForm');
            const profileInfo = document.getElementById('profileInfo');
            const updateBtn = document.getElementById('updateBtn');
            
            // Toggle visibility of profile info and update form
            profileInfo.style.display = profileInfo.style.display === 'none' ? 'block' : 'none';
            updateForm.style.display = updateForm.style.display === 'none' ? 'block' : 'none';

            // Change button text based on the mode
            if (updateBtn.innerText === 'Make Update') {
                updateBtn.innerText = 'Cancel Update';
            } else {
                updateBtn.innerText = 'Make Update';
            }
        }
    </script>
</head>
<body>
    <!-- Background video -->
    <video class="background-video" autoplay muted loop>
        <source src="images/profile.mp4" type="video/mp4">
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

    <!-- Profile Section -->
    <div class="container">
        <h2>Profile Information</h2>

        <!-- Error message -->
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>

        <!-- Profile Information (view mode) -->
        <div id="profileInfo" class="profile-info">
            <p><strong>Account Number:</strong> ${account_number}</p>
            <p><strong>Username:</strong> ${username}</p>
            <p><strong>Phone Number:</strong> ${phone_number != null ? phone_number : 'Not available'}</p>
            <p><strong>Email:</strong> ${email != null ? email : 'Not available'}</p>
            <p><strong>Balance :</strong> ${balance  != null ? balance  : 'Not available'}</p>
            <button type="button" id="updateBtn" onclick="toggleEditMode()">Make Update</button>
        </div>

        <!-- Update Form (edit mode) -->
        <div id="updateForm" class="update-form" style="display:none;">
            <form action="ProfileServlet" method="post">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${username}">

                <label for="phone_number">Phone Number</label>
                <input type="text" id="phone_number" name="phone_number" value="${phone_number}">

                <label for="email">Email</label>
                <input type="text" id="email" name="email" value="${email}">

                <label for="password">Enter Password to Confirm</label>
                <input type="password" id="password" name="password">

                <button type="submit">Confirm Update</button>
            </form>
            <button type="button" onclick="toggleEditMode()">Cancel Update</button>
        </div>
    </div>
</body>
</html>
