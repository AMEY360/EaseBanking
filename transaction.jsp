<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
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

        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .header h1 {
            color: #1c7e1f;
            background-color: #a3e79c;
        }
        .header p{
            color: #f7ff85;
            background-color: #4CAF50;
        }
        .transaction-form {
            text-align: center;
        }
        .btn {
            background-color: #4CAF50;
            color: yellow;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            font-size: 16px;
            border-radius: 5px;
        }
        .btn:hover {
            background-color: #ff0000;
        }
        .error-message {
            color: red;
            text-align: center;
        }
        .success-message {
            color: green;
            text-align: center;
        }
    </style>
</head>
    <body>
        <!-- Background video -->
        <video class="background-video" autoplay muted loop>
            <source src="images/transactions1.mp4" type="video/mp4">
            Your browser does not support the video tag.
        </video>
        

        <div class="container">
            <div class="header">
                <h1>Transaction Page</h1>
                <h2>
                    <p><b>Welcome to your transaction dashboard. You can view your transaction statement here.</b></p>
                </h2>
            </div>

            <!-- Display success or error messages -->
            <div class="error-message">
                <c:if test="${not empty errorMessage}">
                    <p>${errorMessage}</p>
                </c:if>
            </div>
            <div class="success-message">
                <c:if test="${not empty successMessage}">
                <p>${successMessage}</p>
                </c:if>
            </div>

            <!-- Form to get the transaction statement -->
            <form action="TransactionServlet" method="POST" class="transaction-form">
                <button type="submit" name="getStatement" class="btn"><b>GET STATEMENT</b></button>
            </form>
        </div>

    </body>
</html>
