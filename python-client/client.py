import base64
import json
import requests

BASE_URL = "http://localhost:8080"
USERNAME = "admin"
PASSWORD = "admin123"


def translate(text: str):
    token = base64.b64encode(f"{USERNAME}:{PASSWORD}".encode()).decode()
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Basic {token}",
    }
    payload = {
        "text": text,
        "sourceLanguage": "English",
        "targetLanguage": "Moroccan Darija",
    }

    response = requests.post(
        f"{BASE_URL}/translator/translate",
        headers=headers,
        data=json.dumps(payload),
        timeout=30
    )

    response.raise_for_status()
    return response.json()


if __name__ == "__main__":
    text = input("Enter English text: ")
    result = translate(text)
    print("\nTranslation:", result.get("translatedText"))
    print("Provider:", result.get("provider"))