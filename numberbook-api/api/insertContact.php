<?php
header("Content-Type: application/json; charset=utf-8");

require_once __DIR__ . '/../service/ContactService.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        "success" => false,
        "message" => "Méthode non autorisée"
    ]);
    exit;
}

$data = json_decode(file_get_contents("php://input"), true);

if (!is_array($data)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "JSON invalide"
    ]);
    exit;
}

$name = isset($data['name']) ? trim($data['name']) : "";
$phone = isset($data['phone']) ? trim($data['phone']) : "";

if ($name === "" || $phone === "") {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Champs manquants"
    ]);
    exit;
}

try {
    $service = new ContactService();
    $ok = $service->insert($name, $phone, "mobile");

    echo json_encode([
        "success" => $ok,
        "message" => $ok ? "Contact inséré avec succès" : "Erreur d'insertion"
    ]);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Erreur serveur"
    ]);
}
?>