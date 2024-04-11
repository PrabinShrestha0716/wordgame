document.addEventListener('DOMContentLoaded', function () {
    const usernameInput = document.getElementById('username');
    const joinGameButton = document.getElementById('joinGame');
    const gameTypeForm = document.getElementById('gameTypeForm');
    const gameScreen = document.getElementById('game-screen');
    const sendMessageButton = document.getElementById('send-message');
    const chatMessageInput = document.getElementById('chat-message');
    const messagesDiv = document.getElementById('messages');
    let websocket;

    joinGameButton.addEventListener('click', function () {
        const username = usernameInput.value.trim();
        if (username) {
            // Establish connection to WebSocket server
            websocket = new WebSocket('ws:127.0.0.1:9880');
            websocket.onopen = function () {
                console.log('WebSocket connection established');
                websocket.send(JSON.stringify({ type: 'join', username: username }));
            };

            websocket.onmessage = function (event) {
                const data = JSON.parse(event.data);
                // Handle different types of messages here
            };
        } else {
            alert('Please enter a username');
        }
    });

    gameTypeForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(gameTypeForm);
        const playerCount = formData.get('playerCount');
        websocket.send(JSON.stringify({ type: 'gameType', playerCount: playerCount }));
        // Hide lobby and show game screen
        document.getElementById('lobby').style.display = 'none';
        gameScreen.style.display = 'flex';
    });

    sendMessageButton.addEventListener('click', function () {
        const message = chatMessageInput.value.trim();
        if (message) {
            websocket.send(JSON.stringify({ type: 'message', text: message }));
            chatMessageInput.value = '';
            const messageElement = document.createElement('div');
            messageElement.textContent = message;
            messagesDiv.appendChild(messageElement);
        }
    });

   


    function updatePlayerList(players) {
        const onlineGamersDiv = document.getElementById('online-gamers');
        onlineGamersDiv.innerHTML = ''; // Clear current list
        players.forEach(player => {
            const playerDiv = document.createElement('div');
            playerDiv.textContent = player.name + (player.status === 'Ready' ? ' (Ready)' : ' (Waiting)');
            onlineGamersDiv.appendChild(playerDiv);
        });
    }

    function updateGameState(gameState) {
        const wordSearchGridDiv = document.getElementById('word-search-grid');
        // Assuming gameState is an object with a 'board' array representing the game state
        // Clear existing game board
        wordSearchGridDiv.innerHTML = '';
        gameState.board.forEach(row => {
            const rowDiv = document.createElement('div');
            row.forEach(cell => {
                const cellDiv = document.createElement('div');
                cellDiv.textContent = cell; // Replace with actual game logic
                wordSearchGridDiv.appendChild(cellDiv);
            });
        });
    }

    function updateChatMessages(message) {
        const messageElement = document.createElement('div');
        messageElement.textContent = `${message.sender}: ${message.text}`;
        messagesDiv.appendChild(messageElement);
    }

    // WebSocket event handler
    websocket.onmessage = function (event) {
        const data = JSON.parse(event.data);
        switch (data.type) {
            case 'playerList':
                updatePlayerList(data.players);
                break;
            case 'gameState':
                updateGameState(data.gameState);
                break;
            case 'chat':
                updateChatMessages(data.message);
                break;
            // Add more cases as needed for different message types
            default:
                console.log('Received unknown message type:', data.type);
        }
    };
});
