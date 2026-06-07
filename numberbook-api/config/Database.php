<?php
class Database {
    private string $host = "localhost";
    private string $dbName = "numberbook";
    private string $username = "root";
    private string $password = "";
    public ?PDO $conn = null;

    public function getConnection(): ?PDO {
        $this->conn = null;

        try {
            $this->conn = new PDO(
                "mysql:host=" . $this->host . ";dbname=" . $this->dbName . ";charset=utf8mb4",
                $this->username,
                $this->password
            );

            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

        } catch (PDOException $exception) {
            http_response_code(500);
            echo json_encode([
                "success" => false,
                "message" => "Erreur de connexion à la base de données"
            ]);
            exit;
        }

        return $this->conn;
    }
}
?>