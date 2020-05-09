# Scala seed
All details and examples of bleow features could be found at [scala-seed.g8](https://github.com/DevInsideYou/scala-seed.g8) template.

## Plugins
Plugins worth looking at in most of the Scala related projects.

### Git
Allows to run git commands through sbt, when it is fired. It can add the prompt, as well. 

### Clippy 
Highlights error messages and gives advices. There is few configs which are in the Giter8 template. 

### Sbt dependency updates
In `aliases.sbt` file there is:

```
addCommandAlias("up2date", 
	"reload plugins; dependencyUpdates; reload return; dependencyUpdates")
```
`reload plugins` will got to plugins project and run dependencies update command searching for new versions of plugins. Then `reload return` goes back and runs the dependencies update command on the actual project, looking for outdated dependencies. 

Now, everytime we go to `reload plugins` we will start the sbt server, which is not necessary, therefore in `plugins.sbt` we have: `ThisBuild / autoStartServer := false`.

Also, `plugins.sbt` has to be enriched with `./whateverroot/project/project` same `addSbtPlugin()` statement, which was used in main `plugins.sbt` for depency updater. 


### Scala formatter config
Sample code formatter config is in the scala-seed.g8. More details could be found in this [video](https://www.youtube.com/watch?v=nN2aBMcUAjE).


## Fancy features

### Promopt supporting Git
amended propmt to support Git, so when sbt shell is run inside Git project it looks like this `git:master:13ede4d:sbt:whatever-project>`. Fancy prompt is located in `./project/Util.scala` file.

### Aliases
Command `alias` shows available aliases. It shows alias and command run behind the alias defined. 

Check file called `aliases.sbt`.

### Continuous run
Use usual continuous run marker `~` and when in that mode below settings nicely clear the screen for every refresh and move new run on the top.

In `sbt.sbt` file add:
```
ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
```
### On load message
`aliases.sbt` defines the `onLoadMessage` setting, which could be extended with aliases already defined for the project. 

### Sharing common functionality across sbt files
If you have functionality (dependencies, utils, prompts, settings etc) which you want to share across `*.sbt` files located in root project, then you may want to extract the common part to `Dependencies.scala` or `Util.scala` files and locate them in `./project` directory. Then in the relevant sbt file just import it `import Util._`.

### gitignore
Put whatever you need there for your git ignores in reference to usual technology stach you use. 

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

