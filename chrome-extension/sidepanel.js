async function loadSelectedText() {
  const data = await chrome.storage.local.get(['selectedText']);
  if (data.selectedText) {
    document.getElementById('sourceText').value = data.selectedText;
  }
}

async function translate() {
  const baseUrl = document.getElementById('baseUrl').value.trim();
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;
  const text = document.getElementById('sourceText').value.trim();
  const status = document.getElementById('status');
  const resultText = document.getElementById('resultText');

  if (!text) {
    status.textContent = 'Please select or enter text first.';
    return;
  }

  status.textContent = 'Translating...';
  resultText.value = '';

  try {
    const response = await fetch(`${baseUrl}/translator/translate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(`${username}:${password}`)
      },
      body: JSON.stringify({
        text,
        sourceLanguage: 'English',
        targetLanguage: 'Moroccan Darija'
      })
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.error || 'Translation failed');
    }

    resultText.value = data.translatedText || '';
    status.textContent = `Done using ${data.provider}`;
  } catch (error) {
    status.textContent = error.message;
  }
}

document.getElementById('translateBtn').addEventListener('click', translate);
loadSelectedText();
