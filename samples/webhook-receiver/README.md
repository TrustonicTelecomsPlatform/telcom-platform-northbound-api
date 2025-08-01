# 🚀 Webhook Receiver Setup Guide

## --- Step 1: Install Flask ---
```bash
python3 -m pip install -q Flask
```

---

## --- Step 2: Start webhook-receiver Server 📥 ---
```bash
python3 server.py
```

---

## --- Step 3: Expose Local Server Using ngrok 🌐 ---

### 👉 If ngrok is not installed:

- Download from [https://ngrok.com/download](https://ngrok.com/download)
- Or install via package manager:

---

### 🔐 Authenticate ngrok (only once)
Get your auth token from your [ngrok dashboard](https://dashboard.ngrok.com/get-started/setup), then run:

```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN
```

---

### 🚀 Start ngrok in a **new terminal**
```bash
ngrok http 3000
```

You’ll get a public URL like `https://9c1676b3bf92.ngrok-free.app` – use this as the webhook endpoint.

## --- Example payload subscription to TPP---
```bash
{
  "eventType": "locked",
  "url": "https://9c1676b3bf92.ngrok-free.app/webhook"
}
```
