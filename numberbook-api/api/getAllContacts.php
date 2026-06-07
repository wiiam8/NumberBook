<?php
header("Content-Type: application/json; charset=utf-8");

require_once __DIR__ . '/../service/ContactService.php';

try {
    $service = new ContactService();
    $result = $service->getAll();

    echo json_encode($result);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([]);
}
?>