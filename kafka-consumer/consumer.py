from kafka import KafkaConsumer
import threading
import json
from flask import Flask, jsonify

app = Flask(__name__)
messages = []

def kafka_consumer_job():
    consumer = KafkaConsumer(
        'account_deactivated_topic',
        bootstrap_servers='localhost:9092',
        value_deserializer=lambda m: json.loads(m.decode('utf-8')),
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='chu-consumer-group'
    )
    for message in consumer:
        data = message.value
        print(f"📩 Event reçu : {data}")
        messages.append(data)

@app.route('/')
def index():
    return '✅ Kafka Consumer Service is Running'

@app.route('/messages')
def get_messages():
    return jsonify(messages)

if __name__ == '__main__':
    threading.Thread(target=kafka_consumer_job, daemon=True).start()
    app.run(port=5001)
