# Sample scala seed
Details, setup and configuration of below plugins and features can be found at [scala-seed.g8](https://github.com/DevInsideYou/scala-seed.g8) template.


## Plugins
Some potentially useful plugins worth looking at in Scala related projects.

### Git
Allows to run git commands through sbt shell, when it is running. It can also add its own prompt. 

plugin: `addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")`


### Clippy 
[Clippy](https://github.com/softwaremill/scala-clippy) highlights error messages and gives advices during compilation. 


### Sbt dependency updates
In `aliases.sbt` file there is:

```
addCommandAlias("up2date", 
	"reload plugins; dependencyUpdates; reload return; dependencyUpdates")
```
`reload plugins` will go to plugins project and run dependencies update command searching for new versions of plugins. Then `reload return` goes back and runs the dependencies update command on the actual project, looking for outdated dependencies. 

Now, everytime we go to `reload plugins` we will start the sbt server, which is not necessary, therefore in `plugins.sbt` we have: `ThisBuild / autoStartServer := false`.

Also, `plugins.sbt` has to be enriched with `./whateverroot/project/project` same `addSbtPlugin()` statement, which was used in main `plugins.sbt` for depency updater. 

plugin: `addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.0")`

For more details check [scala-seed.g8](https://github.com/DevInsideYou/scala-seed.g8) template setup, as it is non-trivial.


### Scala formatter config
Sample code formatter config is in the scala-seed.g8. More details could be found in this [video](https://www.youtube.com/watch?v=nN2aBMcUAjE).

plugin: `addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.4")` with its [sample configuration](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/.scalafmt.conf).


## Fancy features

### Git supporting promopt
Amended propmt to support Git, so when sbt shell is running inside Git project. Fancy prompt is located in `./project/Util.scala` file.

sample prompt: `git:master:13ede4d:sbt:whatever-project>`


### Aliases
Display all available aliases defined in the [configuration](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/aliases.sbt) via `alias` command. It shows alias and commands run behind the alias defined.

Check file called `aliases.sbt`.


### Continuous run
Use usual continuous run marker `~` and when in that mode, below settings nicely clear the screen for every refresh and move new run on the top.

In [`sbt.sbt`](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/sbt.sbt) file add:

```
ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
```


### On load message
[`aliases.sbt`](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/aliases.sbt) defines the `onLoadMessage` setting, which could be extended with aliases already defined for the project. 


### Sharing common functionality across sbt files
If you have functionality (dependencies, utils, prompts, settings etc) which you want to share across `*.sbt` files located in root project, then you may want to extract the common part to [`Dependencies.scala`](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/project/Dependencies.scala) or [`Util.scala`](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/project/Util.scala) files and locate them in `./project` directory. Then in the relevant sbt file just import it `import Util._` or [`import Dependencies._`](https://github.com/DevInsideYou/scala-seed.g8/blob/master/src/main/g8/build.sbt).


### gitignore
Put all necessary technology stack items used in your project in `.gitignore`.


### automatic build file reload
`Global / onChangeBuildSource := ReloadOnSourceChanges` in build file (`build.sbt`).


### testing
Parallel test execution is switched off, as it might be viewed as premature optimization `Test / parallelExecution := false`.

Another setting is for scala test output: `Test / testOptions += Tests.Argument(TestFramework.ScalaTest, "=oSD")`, where

* `o` is meaning that `S` & `D` are modifing the output of ScalaTest
* `S` is short stack traces 
* `D` is duration of the test (next to test name)

### Auto start server
`ThisBuild / autoStartServer := false`

When set to true, sbt shell will automatically start sbt server. Otherwise, it will not start the server until `startSever` command is issued. This could be used to opt out of server for security reasons.

### Turbo
`ThisBuild / turbo := true`

Adds third layer of class loaders to already existing sbt two layered classloader for speed optimization. 

### Super shell
For small projects it might be annoying, so one might want to switch it off: `ThisBuild / useSuperShell := false`.

