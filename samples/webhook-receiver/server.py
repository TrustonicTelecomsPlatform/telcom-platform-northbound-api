
from flask import Flask, request, jsonify
import datetime

app = Flask(__name__)

webhook_count = 0

@app.route('/webhook', methods=['POST'])
def handle_webhook():
    """
    Endpoint to receive, process, and log webhooks.
    """
    global webhook_count
    timestamp = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    print("================================================")
    print(f"[{timestamp}] - New request received...")

    try:
        data = request.get_json()
        if data is None:
            print("❌ ERROR: Request is not a JSON or 'Content-Type' header is missing.")
            return jsonify({"status": "error", "message": "Invalid JSON or Content-Type header missing"}), 400
    except Exception as e:
        print(f"❌ ERROR: Failed to parse JSON. Error: {e}")
        return jsonify({"status": "error", "message": "Failed to decode JSON object"}), 400

    webhook_count += 1
    print(f"✅ Webhook #{webhook_count} received successfully!")
    print("Payload content:")
    print(data)
    print("================================================")

    return jsonify({
        "status": "success",
        "message": f"Webhook #{webhook_count} received successfully"
    }), 200

if __name__ == '__main__':
    app.run(port=3000, debug=True)