const mysql = require('mysql2');

// Configurare conexiune MySQL
const db = mysql.createConnection({
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || 'password',
    database: process.env.DB_NAME || 'test_db'
});

// Conectare la baza de date
db.connect((err) => {
    if (err) {
        console.error('Eroare la conectarea la MySQL:', err);
        process.exit(1);
    }
    console.log('Conectat la MySQL!');
});

module.exports = db;