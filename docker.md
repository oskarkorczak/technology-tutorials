# Docker tutorial
Docker containers tutorial.

## Concepts and definitions


### Images
Read only templates used to create containers

Images are created with docker build command, either by us or by other users.

Images are designed to be composed of layers of other images. 

Images are stored in a Docker registry (Docker Hub).

There are 2 ways of building an image:

* commit changes made in a Docker container
* write a Dockerfile

### Containers
It is a runtime object (analogy: if image is a class, then a container is an instance of a class). They are lightweight and portable encapsulations of an environment in which application is run. 

### Registry
It is a place where we store our images. You can host your own registry, or you can use Docker’s public registry which is called Docker Hub.

### Repository
Inside registry, images are stored in repositories. Docker repository is a collection of different docker images with the same name, that have different tags, each tag usually represents a different version of the image. 

### Docker Hub
Docker Hub is a registry containing Docker images ready to be downloaded.  
`URL: hub.docker.com`

### Dockerfile
text document that contains all the instructions users provide to assemble an image. Each instruction will create a new image layer to the image. Instructions specify what to do when building the image. 
Dockerfile has to be named `Dockerfile` with capital D and has no extensions.

### Build context path
Takes the path to the build context as an argument. 
eg: if you want to copy some project files from local host to container, those files have to exist on build path. 
Bear in mind that Docker daemon may be run on remote server and no parsing happens on the client side. When build starts, docker client would pack all the files in the build context into a tarball, then transfer the tarsal file to the daemon. 
By default, docker would search for the `Dockerfile` in the root directory of build context path.

`-f search for Dockerfile in location provided`

### Docker cache
each time Docker executes an instruction it builds a new image layer. The next time, if the instruction does not change, Docker will simply reuse the existing layer. 
This helps to build containers faster. If you build many containers then is can greatly reduce the build time.
However, if docker cache is used too aggressively, then it may cause troubles. 

##### Scenario 1
```
FROM ubuntu:14.04
RUN apt-get update
RUN apt-get install -y git
```

After building an image all layers are in the docker cache. Suppose that we add another module to be installed say `curl` then docker sees that 1st and 2nd commands are **NOT** changed. 

##### Scenario 2
```
FROM ubuntu:14.04 (NOT changed, reusing cache)
RUN apt-get update (NOT changed, reusing cache)
RUN apt-get install -y git curl (changed)
```

The problem is that `update` operation is not executed, so potentially we may get state `curl` software. 

The solution is to chain `update` and `install` apt-get commands, to ensure we have the latest version of the package. 

##### Scenario 3
```
FROM ubuntu:14.04 (NOT changed, reusing cache)
RUN apt-get update && apt-get install -y \
	git \
	curl
```

Also, we may instruct Docker to invalidate cache by adding flag:
`docker build -t jameslee/debian . --no-cache=true`

### Docker container links
The main use of docker container link is when we build an application with a micro service architecture, we are able to run many independent components in different containers. 
Docker creates a secure tunnel between the containers that does not need to expose any ports externally on the container

In other words, it allows 2 applications to talk to each other without exposing any network ports. 
Container links allow containers to discover each other and securely transfer information about one container to another container. 
When you set up a `link` you create a conduit between `recipient` container and `source` container. The `recipient` container can then access and select data of `source` container. Links are established by container names. 
E.g.: 
application is the “recipient” container
redid (DB) is the source container



## Docker commands

### docker version
prints docker client and server versions

### docker images
finds out what docker images you have on your local box

### docker run
crates and runs an image specifies in the command eg: 

```
docker run busybox:1.24
docker run busybox:1.24 echo “hello docker”
docker run busy box:1.24 ls /
```

#### Syntax:
`docker run repository:tag command [arguments]`

`-i` flag starts interactive container (you land inside container and can invoke commands agains container OS) - `exit` command leaves the interactive mode

`-t` flag creates a pseudo-TTY that attaches stdin and stdout

`docker run -i -t busy box:1.24`
gives an access right inside the container

`-d` detached mode (container will be running in the background)
`docker run -d busy box:1.24 sleep 1000`
This command returns long hash ID of the container running in the background.

`--rm` removes container when it stops
`docker run --rm busy box:1.24 sleep 1` then list all the containers and you will see that id does not exist there: `docker ps -a`

`--name` allows to run a container and give it a name
`docker run --name hello_world busy box:1.24`

`-p` exposes host port (eg.: 8888) to container port (eg.: `8080`)
`docker run -it -p 8888:8080 tomcat:8.0`
(tomcat can be accessed on `localhost:8888`)


### docker ps
displays running container information, such as: ID (short version, which is the prefix of long container ID), image name, command we run

`-a` displays all running and stopped containers


### docker inspect
displays the low level information about a container or image in JSON. It includes entire network setup, image ID, log path etc (many other useful information)

`docker inspect <image-id>`


### docker logs
shows docker logs from running container(s)

`docker run -it -p 8888:8080 tomcat:8.0`
grab the returned long ID
`docker logs <long-ID>`


### docker history
shows the layers for given image

`docker history busy box:1.24`


### docker build
builds an image based on Dockerfile

`docker build -t jameslee/debian .`

Docker executes each command of Dockerfile in separate (ephemeral) intermediate container. One container per command. Container is the writable process, which remembers filesystem amendments and it is added on a top of result image. Images are read-only. 

### docker commit
saves the changes we made to the Docker container’s file system to a new image. 

##### Sytanx
`docker commit container-ID repository-name:tag`

### docker exec
allows you to run command against running container
`docker exec -it <container-ID> bash`
usually `bash` is run as a command while trying to enter running container


## Docker-compose commands

### docker-compose
is a tool for defining and running multi-container Docker applications. 
It is a handy tool to get docker environment up and running. It uses yaml files to store the configuration of all the containers, which removes the burden to maintain our scripts for docker orchestration. 

### docker-compose up
starts up all the containers

`docker-compose ps`
checks the status of the containers manages by docker compose

`docker-compose logs`
outputs coloured and aggregated logs for the compose-managed containers

`-f` outputs appended log when the log grows

`container-name` outputs the logs of a specific container

##### Syntax
`docker-compose logs [-f] [container-name]`

### docker-compose stop
stops all the running containers without removing them

`docker-compose rm`
removes all the containers

`docker-compose build`
rebuilds all the images



## Dockerfile

### FROM
it has to be the first instruction specifying what is the base image

### RUN
specify command to execute. It could be any command you run in the linux terminal.   
Each `RUN` command will execute the command on the top writable layer of container, then commit the container as a new image. The new image is used for the next step in the Dockerfile. So each `RUN` instruction will create a new image layer. 
It is recommended to chain the `RUN` instructions in the Dockerfile to reduce the number of images layers it creates. 

#### Example 1 (simple set of changes)
```
FROM debian:jessie
RUN apt-get update
RUN apt-get install -y git
RUN apt-get install -y vim
```
`-y` says yes to questions raised in the interactive mode, which we will not be able to access during image building

#### Example 2 (chaining RUN instructions)
```
FROM debian:jessie
RUN apt-get update && apt-get install -y \
        git \
      	vim
```

#### Example 3 (sort multi-line arguments alphabetically)
```
FROM debian:jessie
RUN apt-get update && apt-get install -y \
        git \
	python \
      	vim
```

### CMD
specifies what command you want to run when the container starts up. If no `CMD` instruction is specified in the Dockerfile, Docker will use the default command defined in the basis image (for debian/jessie it’s `bash`). 
The `CMD` instruction does not run when building the image, it only runs when the container starts up. You can specify the command in either exec form which is proffered or in the shell form. 

#### Example 4 (use CMD instruction)
```
FROM debian:jessie
RUN apt-get update && apt-get install -y \
        git \
	python \
      	vim
CMD [“echo”, “hello world”]
```


`docker build -t jameslee/debian . `(rebuild the image)

`docker run <short-ID-returned-by-docker-build-command>` (start image)

`hello world` (is printed)

`docker run <short-ID-returned-by-docker-build-command> echo “hello 
docker”` (overrides the Dockerfile CMD instruction)


### COPY
copies new files or directories from build context and adds them to the file system of the container

#### Example 1
```
FROM debian:jessie
RUN apt-get update && apt-get install -y \
        git \
      	vim
COPY abc.txt /src/abc.txt
```

```
docker build -t jameslee/debian .`
docker run -it <short-ID>
cd src
ls (there should be abc.txt file inside the container)
```

### ADD
instruction not only copies files but also allows you to download a file from internet and copy to the container. `ADD` instruction also has the ability to automatically unpack compressed files. 
The rule of thumb: use `COPY` for the sake of transparency, unless you are absolutely sure you need `ADD`. 

### WORKDIR
sets the working directory for any `RUN`, `CMD`, `ENTRYPOINT`, `COPY` & `ADD` instructions that follow in the Dockerfile. 
Also it is the default directory for user after logging into the running container via `docker exec …`



## Build Docker image by using Docker commit command
1. spin up a container from base image
2. install git package in the container
3. commit changes made in the container

In Docker Hub search for relevant base image distribution eg: `debian:jessie` (2 layers & 125MB)

`docker run -it debian:jessie` (download and run debian distribution)

`apt-get update && apt-get install -y git` (`-y` for saying “yes” whenever interactive prompt asks questions)

`clear`

`git` (check whether git is installed)

`exit` (leave the container and move to host prompt)

`docker ps -a` (get the id of the container running)

`docker commit  <container-ID> jameslee/debian:1.00` (Docker commits our changes to new image and returns a long ID of the new image)

`docker images` (proves the new image is in my local repository)

`docker run -it jameslee/debian:1.00` (spin up a new container based on new image)

`git` (proves that git is installed on the newly run image)


## Push Docker images to Docker Hub
Create Docker Hub account though the website

`docker images` (choose image you want to push to Docker Hub)

`docker tag <iamge-id> jleetutorial/debian:1.01`

`docker images` (chosen & new image have exactly the same image IDs, but different repository names)

`docker login --username=jleetutorial` (use credentials from Docker Hub account setup)

`docker push jleetutorial/debian:1.01`

Docker Hub account - refresh view and search for you new image



## How to containerize a simple Hello World web application
`git clone -b v0.1 https://github.com/jleetutorial/dockerapp.git` (it contains app source code and `Dockerfile`)

### Sample Dockerfile content
```
FROM python:3.5
RUN pip install Flask==0.11.1
RUN useradd -ms /bin/bash admin (always use non-privileged user ie non-root, to run app)
USER admin
WORKDIR /app
COPY app /app
CMD [“python”, “app.py”]
```

```
docker build -t docker app:v0.1
docker images
docker run -d -p 5000:5000 <image-ID>
```

IP address should be `localhost` but if not try using IP returned by `docker-machine ls`

open browser and put `localhost:5000` into URL section

`docker exec -it <container-ID> bash` (you are now inside running container OS)

`pwd` (it should be `WORKDIR`)

`cd /home/admin`

`ps aux` (our app is run as `admin` user)


### Docker compose for many micro services
`git clone -b v0.1 https://github.com/jleetutorial/dockerapp.git` (it contains app source code and `Dockerfile`)

We need to run Redis (DB) container, then our application container and link it to Redis. This manual approach works for small projects, but quickly becomes a bottle-neck. 

`docker-compose version` (verify if docker-compose is installed)

```
docker-compose.yml
version: ‘2’
services:
	docker app:
		build: .
		ports:
			- “5000:5000”
		volumes:
			- ./app:/app (host dir: container dir)
	redis:
		image: redis:3.2.0
```

`Version '2'` automatically supports containers discovery and therefore docker container links are not necessary to be mentioned explicitly. 

`docker-compose up -d` (executed in the directory where `docker-compose.yml` exists)

`docker-compose ps` (check status of running containers)

`docker-compose logs` (shows all he logs for all containers)

`docker-compose logs -f` (`-f` switch helps to listen for new logs coming, when the log grows)

`docker-compose logs dockerapp` (displays logs belonging only to `dockerapp` container)

`docker-compose stop` (stops all containers)

`docker-compose rm` (remove all containers)

`docker-compose ps`

If you amend one of the `Dockerfile(s)` you have to run `docker-compose` build, as docker-compose up ONLY relies on already built images in repository. 

```
docker-compose build
docker-compose up -d
```
