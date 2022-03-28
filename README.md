# proter-online

This is the repo for Martin Lewis' Honours Project. The project involves creating a front end and corresponding API to expose elements of proter that are only available programmatically.

# Installing and Running

For Local use:

Requriements: sbt >1.5.8, Java 8, Scala 2.12.12

1. `git clone https://github.com/workflowfm/proter-online`
2. `cd proter-online`
3. `sbt run` in order to start

Streaming mode can be enabled by running the project with the -stream argument. I.e. `sbt run -stream`

Using Docker

Requirements: Docker

1. `git clone https://github.com/workflowfm/proter-online`
2. `sudo docker build -t backend:1.0.0 proter-online/`
3. `sudo docker run -p 127.0.0.1:8080:8080 backend:1.0.0` to run

Streaming mode can be enabled by running the image with the `sudo docker run -p 127.0.0.1:8080:8080 backend:1.0.0 sbt "run -stream"` command (Note this doesn't currently work and I don't know why, it runs fine but the ports doesn't seem to bind correctly. Works fine in local use tho).


# Ports
In all instances the backend is build to set up and bind to localhost:8080, that's where you can connect to it.
