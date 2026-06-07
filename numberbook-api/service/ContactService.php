<?php
require_once __DIR__ . '/../config/Database.php';

class ContactService {
    private PDO $conn;
    private string $table = "contact";

    public function __construct() {
        $database = new Database();
        $connection = $database->getConnection();

        if ($connection === null) {
            http_response_code(500);
            echo json_encode([
                "success" => false,
                "message" => "Connexion indisponible"
            ]);
            exit;
        }

        $this->conn = $connection;
    }

    public function insert(string $name, string $phone, string $source = "mobile"): bool {
        $sql = "INSERT INTO " . $this->table . " (name, phone, source)
                VALUES (:name, :phone, :source)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    source = VALUES(source)";

        $stmt = $this->conn->prepare($sql);

        return $stmt->execute([
            ':name' => $name,
            ':phone' => $phone,
            ':source' => $source
        ]);
    }

    public function getAll(): array {
        $sql = "SELECT id, name, phone, source, created_at
                FROM " . $this->table . "
                ORDER BY name ASC";

        $stmt = $this->conn->prepare($sql);
        $stmt->execute();

        return $stmt->fetchAll();
    }

    public function search(string $keyword): array {
        $sql = "SELECT id, name, phone, source, created_at
                FROM " . $this->table . "
                WHERE name LIKE :keyword OR phone LIKE :keyword
                ORDER BY name ASC";

        $stmt = $this->conn->prepare($sql);
        $stmt->execute([
            ':keyword' => '%' . $keyword . '%'
        ]);

        return $stmt->fetchAll();
    }
}
?>