default: all

all: clean stubs bin

stubs: java-stubs python-stubs

bin: java-bin

clean:
	./gradlew clean

java-bin:
	./gradlew install

java-stubs:
	./gradlew assemble build

python-stubs:
	mkdir -p ./build/generated/source/python
	touch ./build/generated/source/python/__init__.py
	python3 -m grpc_tools.protoc -I. --python_out=./build/generated/source/python --grpc_python_out=./build/generated/source/python --proto_path=./src/main/proto CVData.proto

java-client:
	build/install/HelloWorldGrpc/bin/hello-world-client

java-server:
	build/install/HelloWorldGrpc/bin/hello-world-server

python-client:
	python3 src/main/python/greeter_client.py

python-server:
	python3 src/main/python/greeter_server.py