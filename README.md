# gidiLibrary
GidiLibrary is a simple REST API for a small library.  That creates REST APIs to add, update, delete and lend books to users and search for books in the library.

This application is not a finished product but has its limitation that requires improvements.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

This application uses npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start

The `npm run` command will list all of the scripts available to run for this project.

## Building for production

To optimize the gidiLibrary application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    npm test

## Using Docker 

To dockerize thus application and all the services that it depends on.
First build a docker image of your app by running:

    ./mvnw package -Pprod jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d
