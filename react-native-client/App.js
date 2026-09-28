import React, { useState } from 'react';
import { SafeAreaView, View, Text, TextInput, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';

const API_URL = 'http://YOUR-COMPUTER-IP:8080/translator/translate';
const AUTH = 'Basic YWRtaW46YWRtaW4xMjM=';

export default function App() {
  const [text, setText] = useState('Hello, how are you?');
  const [result, setResult] = useState('');
  const [status, setStatus] = useState('');

  const translate = async () => {
    setStatus('Translating...');
    setResult('');
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': AUTH,
        },
        body: JSON.stringify({
          text,
          sourceLanguage: 'English',
          targetLanguage: 'Moroccan Darija',
        }),
      });

      const data = await response.json();
      if (!response.ok) throw new Error(data.error || 'Translation failed');
      setResult(data.translatedText || '');
      setStatus(`Done using ${data.provider}`);
    } catch (error) {
      setStatus(error.message);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.title}>React Native Darija Translator</Text>
        <TextInput
          style={styles.input}
          multiline
          value={text}
          onChangeText={setText}
          placeholder="Enter English text"
        />
        <TouchableOpacity style={styles.button} onPress={translate}>
          <Text style={styles.buttonText}>Translate</Text>
        </TouchableOpacity>
        <Text style={styles.label}>Result</Text>
        <TextInput style={styles.output} multiline editable={false} value={result} />
        <Text style={styles.status}>{status}</Text>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff' },
  content: { padding: 20 },
  title: { fontSize: 24, fontWeight: '700', marginBottom: 16 },
  label: { fontWeight: '600', marginTop: 12, marginBottom: 8 },
  input: { borderWidth: 1, borderColor: '#ccc', borderRadius: 8, minHeight: 120, padding: 12, textAlignVertical: 'top' },
  output: { borderWidth: 1, borderColor: '#ccc', borderRadius: 8, minHeight: 120, padding: 12, textAlignVertical: 'top' },
  button: { backgroundColor: '#222', padding: 14, borderRadius: 8, marginTop: 14 },
  buttonText: { color: '#fff', textAlign: 'center', fontWeight: '700' },
  status: { marginTop: 12, color: '#555' },
});