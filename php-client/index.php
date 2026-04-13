<?php
$translation = "";
$provider = "";
$error = "";
$sourceText = $_POST['text'] ?? '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $payload = json_encode([
        "text" => $sourceText,
        "sourceLanguage" => "English",
        "targetLanguage" => "Moroccan Darija"
    ]);

    $ch = curl_init("http://localhost:8080/translator/translate");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        "Content-Type: application/json",
        "Authorization: Basic " . base64_encode("admin:admin123")
    ]);
    curl_setopt($ch, CURLOPT_TIMEOUT, 30);

    $response = curl_exec($ch);
    $curlError = curl_error($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($response === false) {
        $error = "cURL error: " . $curlError;
    } else {
        $data = json_decode($response, true);

        if ($status >= 200 && $status < 300) {
            $translation = $data['translatedText'] ?? '';
            $provider = $data['provider'] ?? '';
        } else {
            $error = $data['error'] ?? ("Request failed with status " . $status . ". Raw response: " . $response);
        }
    }
}
?>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>PHP Darija Translator Client</title>
  <style>
    body { font-family: Arial; max-width: 700px; margin: 30px auto; }
    textarea, button { width: 100%; padding: 10px; margin-top: 10px; }
    .result { background: #f4f4f4; padding: 12px; margin-top: 10px; }
    .error { color: red; white-space: pre-wrap; }
  </style>
</head>
<body>
  <h2>PHP Client - Darija Translator</h2>
  <form method="post">
    <textarea name="text" rows="6" placeholder="Enter English text..."><?= htmlspecialchars($sourceText) ?></textarea>
    <button type="submit">Translate</button>
  </form>

  <?php if ($translation): ?>
    <div class="result">
      <strong>Translation:</strong><br>
      <?= htmlspecialchars($translation) ?><br><br>
      <strong>Provider:</strong> <?= htmlspecialchars($provider) ?>
    </div>
  <?php endif; ?>

  <?php if ($error): ?>
    <p class="error"><?= htmlspecialchars($error) ?></p>
  <?php endif; ?>
</body>
</html>