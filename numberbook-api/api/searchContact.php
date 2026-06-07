<?php
header("Content-Type: application/json; charset=utf-8");

require_once __DIR__ . '/../service/ContactService.php';

$keyword = isset($_GET['keyword']) ? trim($_GET['keyword']) : "";

if ($keyword === "") {
    echo json_encode([]);
    exit;
}

try {
    $service = new ContactService();
    $result = $service->search($keyword);

    echo json_encode($result);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([]);
}
?>