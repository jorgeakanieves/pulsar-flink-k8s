import pulsar

client = pulsar.Client('pulsar://ip:6650')
producer = client.create_producer('input-topic')

for i in range(10):
    producer.send(('hello-pulsar-%d' % i).encode('utf-8'))

client.close()
