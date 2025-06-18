<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify OTP</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            background: url('images/verifybackground.jpg') no-repeat center center fixed;
            background-size: cover; /* Cover the entire page with the background */
            text-align: center;
            margin-top: 100px;
        }
        
        .container {
            width: 400px;
            margin: 0 auto;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        }
        .container h2{
            background-color: #adfaff;
        }
        .container label{
            background-color: #adfaff;
        }
        input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        button {
            width: 100%;
            padding: 10px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Verify OTP</h2>
        <form action="VerifyServlet" method="post">
            <label for="otp"><b>Enter OTP:</b></label>
            <input type="text" id="otp" name="otp" required>
            <button type="submit">Verify</button>
        </form>
    </div>
</body>
</html>
