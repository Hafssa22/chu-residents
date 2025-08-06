from kafka import KafkaConsumer
import threading
import json
from twilio.rest import Client
from flask import Flask, jsonify

app = Flask(__name__)
messages = []

# === CONFIG TWILIO ===
TWILIO_ACCOUNT_SID = 'AC6112bb5a36aa30b42ad214f26822a0e7'
TWILIO_AUTH_TOKEN = '4152c02dcf73a8e98739696edf8b2db4'
TWILIO_PHONE_NUMBER = '+17622635994'

# === Formatage numéro Maroc ===
def format_number(phone):
    """
    Convertit un numéro marocain comme '061498XXXX' en '+21261498XXXX'
    """
    phone = phone.strip().replace(" ", "")
    if phone.startswith("0"):
        return "+212" + phone[1:]
    elif phone.startswith("+"):
        return phone
    else:
        return phone

# === Fonction SMS dynamique ===
def send_sms(body, destination_number):
    try:
        formatted_number = format_number(destination_number)
        print(f"📞 Numéro formaté : {formatted_number}")

        client = Client(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)
        message = client.messages.create(
            body=body,
            from_=TWILIO_PHONE_NUMBER,
            to=formatted_number
        )
        print(f"📱 SMS envoyé à {formatted_number} avec SID : {message.sid}")
    except Exception as e:
        print(f"❌ Erreur lors de l'envoi du SMS à {destination_number} : {e}")

# === Désérialiseur sécurisé ===
def safe_deserializer(m):
    try:
        if m is None or m.strip() == b'':
            print("⚠️ Message Kafka vide ignoré.")
            return None
        return json.loads(m.decode('utf-8'))
    except Exception as e:
        print(f"❌ Erreur de désérialisation JSON : {e}")
        return None

# === Thread Kafka Consumer ===
def kafka_consumer_job():
    print("🚀 Kafka consumer démarré... en attente de messages.")
    consumer = KafkaConsumer(
        'account_deactivated_topic',
        bootstrap_servers='localhost:9092',
        value_deserializer=safe_deserializer,
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='chu-consumer-group'
    )
    for message in consumer:
        data = message.value
        if data is None:
            continue

        print(f"📩 Event reçu : {data}")
        messages.append(data)

        phone_number = data.get('phoneNumber')
        if not phone_number:
            print("⚠️ Numéro de téléphone manquant, SMS ignoré.")
            continue

        sms_body = f"""
        ⚠️ Résident désactivé :
        Matricule : {data.get('matricule')}
        Raison : {data.get('reason')}
        Date : {data.get('date')}
        """

        send_sms(sms_body.strip(), phone_number)

# === Routes Flask ===
@app.route('/')
def index():
    return '✅ Kafka + Twilio SMS Consumer is running!'

@app.route('/messages')
def get_messages():
    return jsonify(messages)

# === Lancement ===
if __name__ == '__main__':
    threading.Thread(target=kafka_consumer_job, daemon=True).start()
    app.run(port=5001)
