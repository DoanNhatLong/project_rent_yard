const express = require('express');
const http = require('http');
const { Server } = require('socket.io');

const app = express();
const server = http.createServer(app);

const io = new Server(server, {
    cors: { origin: '*' }
});

app.use(express.json());

app.post('/send', (req, res) => {
    const { message } = req.body;
    io.emit('message', message);
    res.sendStatus(200);
});

// SOCKET
io.on('connection', (socket) => {
    console.log('Client connected:', socket.id);

    socket.on('message', (data) => {
        console.log('Message received:', data);

        // 1️⃣ gửi xác nhận CHỈ cho người gửi
        socket.emit('ack');

        // 2️⃣ gửi tin nhắn cho TẤT CẢ (chat 2 chiều)
        io.emit('message', data);
    });

    socket.on('disconnect', () => {
        console.log('Client disconnected:', socket.id);
    });
});

server.listen(3000, () => {
    console.log('Socket.IO server running at http://localhost:3000');
});
