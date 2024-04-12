document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById('gameCanvas');
    const startButton = document.querySelector("#startGame");
    const ctx = canvas.getContext('2d');
    const cellSize = 50; // Assuming each cell is 50x50 pixels
    const gridSize = 50; // Grid size
    let grid = createEmptyGrid(gridSize); // Create an empty grid

    startButton.addEventListener('click', function(){
        document.querySelector("#gameSection").style.display ="block";
    })

    function createEmptyGrid(size) {
        return Array.from({ length: size }, () => Array.from({ length: size }, () => '-'));
    }

    function fetchWordListAndInitialize() {
        fetch('/api/wordlist')
            .then(response => response.json())
            .then(words => {
                initializeGridWithWords(words);
                drawGrid();
            })
            .catch(error => console.error('Error fetching word list:', error));
    }

    function initializeGridWithWords(words) {
        // Use your logic to place words in the grid here
        // For simplicity, this example randomly places words horizontally
        words.forEach(word => {
            if (word.length <= gridSize) {
                placeWordInGrid(word.toUpperCase());
            } else {
                replaceWithRandomWord();
            }
        });
        
    }

    function replaceWithRandomWord() {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                if (grid[i][j] === '-') {
                    const randomChar = characters.charAt(Math.floor(Math.random() * characters.length));
                    grid[i][j] = randomChar;
                }
            }
        }
    }

    function placeWordInGrid(word) {
        // Example: Place words horizontally at random starting points
        const row = Math.floor(Math.random() * gridSize);
        let col = Math.floor(Math.random() * (gridSize - word.length)); // Ensure word fits
        for (let i = 0; i < word.length; i++) {
            grid[row][col + i] = word[i];
        }
    }

    function drawGrid() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.font = '20px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';

        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                const x = j * cellSize + cellSize / 2;
                const y = i * cellSize + cellSize / 2;
                ctx.fillText(grid[i][j], x, y);
            }
        }
    }

    canvas.addEventListener('mousedown', function(event) {
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        startX = Math.floor(x / cellSize);
        startY = Math.floor(y / cellSize);
    });

    canvas.addEventListener('mouseup', function(event) {
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const endX = Math.floor(x / cellSize);
        const endY = Math.floor(y / cellSize);

        if (startX === endX || startY === endY || Math.abs(startX - endX) === Math.abs(startY - endY)) {
            selectedWord = extractWord(startX, startY, endX, endY);
            document.getElementById('selectedWord').textContent = selectedWord;
        }
    });

    function extractWord(startX, startY, endX, endY) {
        let word = '';
        if (startX === endX) { // Vertical selection
            for (let i = Math.min(startY, endY); i <= Math.max(startY, endY); i++) {
                word += grid[i][startX];
            }
        } else if (startY === endY) { // Horizontal selection
            for (let i = Math.min(startX, endX); i <= Math.max(startX, endX); i++) {
                word += grid[startY][i];
            }
        } else { // Diagonal selection
            const stepX = startX < endX ? 1 : -1;
            const stepY = startY < endY ? 1 : -1;
            let y = startY;
            for (let x = startX; x !== endX + stepX; x += stepX) {
                word += grid[y][x];
                y += stepY;
            }
        }
        return word;
    }
    fetchWordListAndInitialize(); // Fetch words and initialize grid
});
