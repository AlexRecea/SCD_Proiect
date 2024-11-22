const express = require('express');
const dotenv = require('dotenv');
const db = require('./db');

// Configurare variabile de mediu
dotenv.config();

const app = express();
app.use(express.json());

// Endpoint simplu pentru testare
app.get('/users', (req, res) => {
    db.query('SELECT * FROM users', (err, results) => {
        if (err) {
            console.error('Eroare la query:', err);
            res.status(500).send('Eroare la baza de date.');
        } else {
            res.json(results);
        }
    });
});

// Pornirea serverului
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Serverul rulează pe http://localhost:${8083}`);
});