default: all

all: clean stubs bin

stubs: clean java-stubs python-stubs

bin: java-bin

# cleans and wipe the build dir
clean:
	./gradlew clean

# dunno
java-bin:
	./gradlew installDist

# generate java stubs for grpc
java-stubs:
	./gradlew assemble build

# generate python stubs for grpc
python-stubs:
	mkdir -p ./build/generated/source/python
	touch ./build/generated/source/python/__init__.py
	python3 -m grpc_tools.protoc -I. --python_out=./build/generated/source/python --grpc_python_out=./build/generated/source/python --proto_path=./src/main/proto CVData.proto

# initiate java server
java-server:
	build/install/Deep-Space-Robot/bin/grpc-Server

# initiated python server, nonexistent as of now
python-server:
	python3 src/main/python/greeter_server.py

# initiate python client, nonapplicable, the usage of client is different and can be found under src/main/python/grpc
python-client:
	python3 src/main/python/grpc_utils/routeClient.py

# initiate java client, not applicable
java-client:
	build/install/Deep-Space-Robot/bin/grpc-Server

