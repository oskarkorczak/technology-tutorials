# Scalafmt
Scala code formatter/linter utilizing Scala style guide. 

## Setup
In general scalafmt config file (sample [here](https://github.com/DevInsideYou/scala-seed.g8/blob/master/.scalafmt.conf)) should be kept rather to the minimum, so that there are:

* not too much deviations from the original style guide
* not too many things to maintain

### IDE format on save
It is very useful to set the IDE to format the code on save event. 

In Intelij IDEA preferences search for `scalafmt` and go to Editor => Code Style => Scala, choose `Scalafmt` formatter from the dropdown, tick `reformat on save` and point to the configuration file in the project. 

### Sbt plugin
`addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.4")`

Discover all available plugin commands by going to sbt shell, writing `scalafmt` and clicking `TAB` for auto-completion:

* `scalafmt` - formats the code
* `scalafmtCheck` - checks, whether the code is formatted
* `scalafmtAll` - formats test files, as well
* `scalafmtSbt` - formats sbt files

### Configuration file format
The `.scalafmt.conf` is actually written in `HOCON` (Human-Optimized Config Object Notation) format. 

It means that the file could be transformed from HOCON to JSON and vice versa.

##### HOCON
```
rewrite.rules = [SortModifiers]
rewrite.sortModifiers.order = [
	"final", "sealed", "abstract", "override",
	"implicit", "private", "protected", "lazy"
]
```

##### JSON
```
rewrite {
		rules = [SortModifiers]
		sortModifiers.order = [
		"final", "sealed", "abstract", "override",
		"implicit", "private", "protected", "lazy"
	]
}
```


## Rules
Set of useful rules to be set in `.scalafmt.conf` file. All rules coudl be found on [Scalafmt website](https://scalameta.org/scalafmt/docs/configuration.html).

### skip formatting
Switches off scalafmt formatting for a piece of code by using scalafmt instructions in the comment:

```
// format: off
println(2*6 + 5*6)
// format: on
```


### maxColumn
Maximum width of text after which wrapping starts.

`maxColumn = 80`

### strip margin
Nicely aligns all the standard strip margin pipes one over another. 

`assumeStandardLibraryStripMargin = true`

### align
Provides spacing and sense of breath for formatted code, which is rather pleasant for the eye.  

`align = more`

*PS. Playing too much with alignment may cause unnecessary Git diffs.*

### rewrite rule
Sorts keywords like `private`, `implict`, `final` etc. 
`rewrite.rules = [SortModifiers]`

The order could also be overriden:

```
rewrite.sortModifiers.order = [
	"final", "sealed", "abstract", "override",
	"implicit", "private", "protected", "lazy"
]
```

### vertical multiline
The formatting is triggered if the method definition exceeds the `maxColumn` value in width or if the number of arguments to the methods exceed the `verticalMultiline.arityThreshold`.

```
verticalMultiline {
  arityThreshold = 3
  atDefnSite = true
  excludeDanglingParens = []
  newlineAfterImplicitKW = true
  newlineAfterOpenParen = true
  newlineBeforeImplicitKW = false
}
```

### curly fors
Removes `(...)` and replaces it with `{...}` for `for` comprehensions.

`rewrite.rules = [PreferCurlyFors]`
