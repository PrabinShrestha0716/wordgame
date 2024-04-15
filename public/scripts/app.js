document.addEventListener('DOMContentLoaded', function() {
    const serverUrl = 'ws://127.0.0.1:9880';
    const loginForm = document.querySelector("#loginForm");
    const activeUsersList = document.getElementById("activeUsersList");

    let websocket;

    // Function to fetch active users from the server
    function fetchActiveUsers() {
        fetch('/active-users')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                updateActiveUsers(data.activeUsers);
            })
            .catch(error => {
                console.error('Error fetching active users:', error);
            });
    }

    // Function to update active users table
    function updateActiveUsers(users) {
        activeUsersList.innerHTML = '';
        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user}</td>
                <td><span class="badge bg-success">Online</span></td>
            `;
            activeUsersList.appendChild(row);
        });
    }

    // WebSocket connection
    websocket = new WebSocket(serverUrl);

    websocket.onopen = function() {
        console.log('WebSocket connected');
    };

    websocket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        if (data.type === 'activeUsersUpdate') {
            updateActiveUsers(data.activeUsers);
        }
    };

    // Fetch active users when the page loads
    fetchActiveUsers();

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        // Get username from form input
        var username = document.getElementById('username').value;
        // save username to localstorage
        localStorage.setItem("username", username);
        // Construct message to send to server
        const message = {
            type: 'login',
            username: username
        };

        // Send message to WebSocket server
      const response =   websocket.send(JSON.stringify(message));
      loginForm.style.display = "none";
    });

    messageform.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        // Get message from form input
        var message = document.getElementById('chatInput').value;
    })

    function sendChatMessage() {
        const messageInput = document.getElementById('chatInput');
        const message = messageInput.value;
        messageInput.value = ''; // Clear input after sending
        const chatMessage = {
            type: 'chatMessage',
            username: username, // Assume username is globally stored after login
            message: message
        };
        websocket.send(JSON.stringify(chatMessage));
    }
    
    websocket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        if (data.type === 'activeUsersUpdate') {
            updateActiveUsers(data.activeUsers);
        } else if (data.type === 'chatMessage') {
            const chatMessages = document.getElementById('chatMessages');
            const messageElement = document.createElement('div');
            messageElement.textContent = data.username + ': ' + data.message;
            chatMessages.appendChild(messageElement);
            chatMessages.scrollTop = chatMessages.scrollHeight; // Auto-scroll to the latest message
        }
    };
});
