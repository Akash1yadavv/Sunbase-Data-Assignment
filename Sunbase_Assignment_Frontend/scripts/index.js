let baseUrl = "http://localhost:8080/api/auth/sunbasedata";

document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const email = document.getElementById('loginId').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message');

    try {
        const response = await fetch(`${baseUrl}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            data = await response.json();
            console.log(data);
            const token = data.token;

            messageElement.textContent = 'Login successful!';
            messageElement.style.color = 'green';

            localStorage.setItem('token', token);

            setTimeout(() => {
                window.location.href = './customer.html';
            }, 600); // Redirect after 1 second

        } else {
            messageElement.textContent = 'Invalid email or password. Please try again.';
            messageElement.style.color = 'red';
        }
    } catch (error) {
        console.error('Error:', error);
        messageElement.textContent = 'An error occurred while fetching auth api. Please try again later.';
        messageElement.style.color = 'red';
    }


});

