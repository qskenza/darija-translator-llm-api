KenzaQribis_project2

LLM-powered RESTful Web Service for Moroccan Darija Translation
Overview

This project implements a RESTful web service in Java that translates English text into Moroccan Darija using a Large Language Model (LLM).

The system is designed as a distributed architecture, where multiple heterogeneous clients (web, mobile, browser extension, CLI) interact with a centralized REST API. This demonstrates interoperability between distributed components using standard HTTP communication.

System Components

The project includes:

Java backend built with Jakarta RESTful Web Services (JAX-RS)
Basic Authentication for protected endpoints
Integration with Google Gemini API for LLM-based translation
Fallback local translation mechanism

Multiple clients:
Chrome Extension (Manifest V3 + Side Panel)
PHP Web Client
Python CLI Client
React Native Mobile Client (Expo)
UML documentation (class, deployment, sequence, and use-case diagrams)

Project Structure
KenzaQribis_project2/
├── llm-darija-translator-service/   # Java REST API
├── chrome-extension/                # Chrome extension client
├── php-client/                      # PHP web client
├── python-client/                   # Python CLI client
├── react-native-client/             # Mobile client (Expo)
├── uml/                             # UML diagrams (PlantUML)
├── docs/                            # Additional documentation (optional)
└── README.md

Backend Features
Available Endpoints
Method	Endpoint	Description	Auth Required
GET	/translator/health	Health check	No
GET	/translator/info	Service information	No
POST	/translator/translate	Translate text to Darija	Yes
Authentication

The protected endpoint uses Basic Authentication.

Default credentials:

Username: admin
Password: admin123

These can be overridden using environment variables:

APP_USERNAME
APP_PASSWORD
Translation Engine

The service supports two modes:

1. LLM Mode (Primary)

Uses Google Gemini API (gemini-2.5-flash-lite) when an API key is provided.

2. Fallback Mode

A local dictionary-based translator is used when:

no API key is configured
or the external API is unavailable (e.g., high demand or rate limits)
Request Example
{
  "text": "Hello, how are you?",
  "sourceLanguage": "English",
  "targetLanguage": "Moroccan Darija"
}
Response Example
{
  "sourceText": "Hello, how are you?",
  "translatedText": "salam, labas 3lik?",
  "sourceLanguage": "English",
  "targetLanguage": "Moroccan Darija",
  "provider": "gemini-gemini-2.5-flash-lite"
}
Running the Backend
Requirements
Java 17+
Maven 3.9+
Environment Configuration

Set the following environment variables:

export APP_USERNAME=admin
export APP_PASSWORD=admin123
export GEMINI_API_KEY=your_api_key_here
export GEMINI_MODEL=gemini-2.5-flash-lite
Build and Run
cd llm-darija-translator-service
mvn clean package
java -jar target/llm-darija-translator-service-1.0.0-jar-with-dependencies.jar

The service runs on:

http://localhost:8080
API Testing (HTTP Tool / cURL Equivalent)
Health Check
curl http://localhost:8080/translator/health
Translation Request
curl -X POST http://localhost:8080/translator/translate \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "text":"Thank you",
    "sourceLanguage":"English",
    "targetLanguage":"Moroccan Darija"
  }'

The API was also tested using PowerShell (Invoke-RestMethod), which is equivalent to cURL.

Clients
1. Chrome Extension
Built with Manifest V3
Uses chrome.sidePanel API
Accessible via right-click context menu
Automatically captures selected text

Installation:

Open chrome://extensions
Enable Developer Mode
Click Load unpacked
Select the chrome-extension folder
2. PHP Web Client

Run locally:

cd php-client
php -S localhost:8000

Open in browser:

http://localhost:8000
3. Python CLI Client
cd python-client
pip install -r requirements.txt
python client.py
4. React Native Mobile Client
cd react-native-client
npm install
npx expo start
Important

When testing on a physical device, replace localhost with your machine’s IP address:

http://10.126.151.169:8080
UML Documentation

Located in /uml:

Class Diagram
Deployment Diagram
Sequence Diagram
Use Case Diagram

Generated using PlantUML.

Encoding Note (Windows PowerShell)

UTF-8 Arabic text may appear corrupted in PowerShell due to encoding limitations.

To display correctly:

$response.translatedText | Out-File result.txt -Encoding utf8
notepad result.txt

This ensures proper visualization of Moroccan Darija output.

Demonstration Summary

The system was demonstrated using:

Direct API testing (PowerShell / cURL)
Chrome extension translation from selected webpage text
PHP web client interface
Python CLI interaction
React Native mobile application
UML diagrams explaining architecture
Conclusion

This project successfully implements a secure, LLM-powered RESTful service with multiple interoperable clients. It highlights:

REST API design and security
Integration of modern LLM APIs
Cross-platform client development
Distributed system interoperability