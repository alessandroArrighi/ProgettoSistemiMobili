import express from 'express'
import mysql from 'mysql2'
import cors from 'cors'

const app = express()
const port = 3000
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'Arrighi',
    password: '1234',
    database: 'PasswordManager'
})

app.use(cors())

app.use(express.json());

app.get("/", async function(req, res) {

    getCallInfo(req)

    connection.execute(
        "SELECT * FROM Utenze",
        [],
        function(err, results, fields) {
            res.json(results)
        }
    )
})

app.post("/add/service", async function(req, res) {
    const { Service, User, Password } = req.body

    getCallInfo(req)

    connection.execute(
        "INSERT INTO Utenze(Service, User, Password) VALUES (?, ?, ?)",
        [Service, User, Password],
        function(err, results, fields) {
            if(err) {
                res.json({message: err})
            }
            res.json({message: "Servizio Aggiunto"})
        }
    )
})

app.post("/mod/service", async function(req, res) {
    const { OldService, OldUser, OldPassword, Service, User, Password } = req.body

    getCallInfo(req)

    connection.execute(
        "SELECT * FROM Utenze WHERE Service = ? AND User = ? AND Password = ?",
        [OldService, OldUser, OldPassword],
        function(err, results, fields) {
            if(err) {
                res.json({message: err})
                return
            }

            if(results.length <= 0 || !results[0].IDService) {
                res.json({message: "Serivizio non trovato"})
                return
            }

            connection.execute(
                "UPDATE Utenze SET Service = ?, User = ?, Password = ? WHERE IDService = ?",
                [Service, User, Password, results[0].IDService],
                function(err, results, fields) {
                    if(err) {
                        res.json({message: err})
                        return
                    }
                    res.json({message: "Serivizio aggiornato"})
                }
            )
        }
    )
})

app.post("/del/service/", async function(req, res) {

    const { Service, User, Password } = req.body

    getCallInfo(req)

    connection.execute(
        "SELECT * FROM Utenze WHERE Service = ? AND User = ? AND Password = ?",
        [Service, User, Password],
        function(err, results, fields) {
            if(err) {
                res.send(err)
                return
            }

            if(results.length <= 0) {
                res.json({message: "Serivizio non trovato"})
                return
            }

            connection.execute(
                "DELETE FROM Utenze WHERE IDService = ?",
                [results[0].IDService],
                function(err, results, fields) {
                    if(err) {
                        res.json({message: err})
                        return
                    }
                    
                    res.json({message: "Serivizio eliminato"})
                }
            )
        }
    )
})

app.listen(port, function() {
    console.log("Listen on port: " + port)
})

function getCallInfo(req) {
    let data = new Date()
    let ora = data.getHours().toString()
    ora += ":" + data.getMinutes().toString()
    ora += ":" + data.getSeconds().toString()
    console.log("Time: ", ora, "\nMethod: ", req.method, "\nURL: ", req.url, "\nIP: ", req.ip, "\nBody: ", req.body)
}