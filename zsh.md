# Emacs and zsh shell shortcuts
Emacs shorctus are probably the most popular key bindings in Linux terminals.

## Key bindng
 
`set -o` see current settings

`set -o emacs` turn on emacs key bindings

`set -o vim` turn on vim key bindings


## Shortcuts
Emacs shorctus are the most popular in Linux terminals. Most emacs shortcuts are a combination of ctrl/alt.


### Exitting
* `exit` exit
* `ctrl d` exit
* `ctrl c` kill process


### Navigation
* `ctrl l` clear screen
* `ctrl a` go to the beginnig of the line
* `ctrl e` go to the end of the line
* `ctrl <-` go one word to the left
* `ctrl ->` go one word to the right
* `ctrl u` cut from cursor to the beginnig
* `ctrl k` cut from cursor to the end
* `ctrl y` paste the buffer content
* `ctrl w` cut the portion of the text (max one word) from cursor to beginnig of the word before the cursor
* `alt d` cut the portion of the text (max one word) from cursor to end of the word after  the cursor
* `ctrl t` swaps two characters from before and after coursor location
* `esc t` swaps two words from before and after cursor location
* `alt u` capitalize the word
* `alt l` lowercase the word
* `ctrl s` freeze the moving screen eg. htop
* `ctrl q` un-freeze the screen eg. htop
* `<arrow-up>` or `<arrow-down>` browse history commands
* `history` shows command history
* `!<history-number>` brings chosen command from history
* `!<phrase>` search through history with <phrase> matching
* `!!` run last command
* `ctrl r` search through the history
* `crtl x, e` if you type long command, the spell opens vim editor, allows to finish typing the command and then save and quit (:wq) to run the command
* `ctrl z` send app to the background and suspend it
* `fg` brings app to the foreground (if there is one app in the backgrond, just type `fg`; if more then use either `fg 2` or `fg vim`, given vim was sencond app send to background

### Directories
Let's assume there is `sample-dir` direcotry in current location. One may switch to `sample-dir` by invoking below variations of commands, which get resolved correctly even if they are fuzzy match:

```
cd sample-dir
cd mple <tab>
cd Sam <tab>
sample-dir
```

If you plan to go to remote directory location and roughly know where to go and need some guidance then use <tab>, to get insight of all subdirectories starting with `g`:

```
cd /usr/lib/g <tab>
```

Double <tab> gives you coursor allowing to navigate through the short list. 

* `~` home direcotry 
* `-` previous directory
* `..` parent direcotry
* `...` 2 directoris up
* `....` 3 directoris up

#### Previously visited directories
`d` previous directories one was in; type order number to go there

`z` fuzzy search for directories one has been into; it gives a list of dirs similar to `d`

`z coll` moves straight to `/some/path/homebrew-collections/` directory via fuzzy matching. 

#### Create multiple directories
`take a/b/c` creates nested directories `a/b/c` and goes there

`md a/b/c` creates nested direcotires `a/b/c` and does NOT go there

#### Create multiple files 
`touch 1 2 3` creates three files

### Locate application
`which java` shows where `java` is located e.g.: `/usr/bin/java`

`where java` shows all `java` applications e.g.: `/usr/bin/java` and `/usr/lib/jvm/graalvm/bin/java`


## Installation
Zsh shell will be installed together with couple of usuful plugins and Oh_my_zsh configuration.

### Automated way
Installs zsh together with Oh My Zsh configuration. Run [this](https://github.com/DevInsideYou/install-zsh) script and its done. 

### Manual steps
Below manual steps are just for showing amount of work and are NOT necessary when automated script is run. 

#### zsh 
`sudo apt install -y zsh` installs zsh shell from bash shell using default package manager. After installation, still the default shell is bash, see `echo $SHELL`. 

If you type `zsh` in bash terminal you will switch to zsh shell with default settings. `ctrl d` to go out from zsh. 

`where zhs` shows all zsh shells; choose one and change default shell `chsh -s /bin/zsh` and restart terminal and logout the account. 

`cat /etc/shells` lists all the installed shells in the system. 

#### Oh My Zsh
Install [Oh My Zsh](https://github.com/ohmyzsh/ohmyzsh) configuration manager.

## Configuration
`~/.zshrc` file holds Oh My Zsh configuration. 

### Prompt themes
`echo $ZSH_THEME` shows current theme.

#### Predefined themes
There is plethora of predefined themes already available located at `.oh-my-zsh/themes/` directory. 

##### Agnoster theme
One of the most common themes is `ZSH_THEME="agnoster"` set in zsh configuration file. 

##### PowerLevel9k
However, the most popular theme is `powerlevel9k` and recently there is also `powerlevel10k`.

Basically it's all about cloning relevant Git repository from installation options and amending `ZSH_THEME="powerlevel9k/powerlevel9k"`

##### InTheLoop
This is quite minimalistic prompt, which is decent enough; `ZSH_THEME="intheloop"`. 

#### Custom themes
Usuall it is a matter of coping already existing theme and tweaking it `cp ~/.oh-my-zsh/themes/intheloop.zsh-theme ~/.oh-my-zsh/custom/themes`. Open the copied file and play with it. 


### Other configurations
Browse through `.zshrc` file and uncomment what you think is needed. 

#### Aliases
If you have bash_aliases file you can source it in `.zshrc` configuration file with below command:

```
if [ -f "$HOME/.bash_aliases" ] ; then
	source "$HOME/.bash_aliases"
fi
```

#### Plugins
`cd .oh-my-zsh/plugins` and list the direcotry to see all plugins. They all should have README file. 

##### git
Adds Git commands aliases and autocomplition.

##### docker
Docker autocomplition.

##### dotenv
If you add `.env` file to `whatever` directory with variable defined e.g.: `FOO=BAR`, then whenever you go to `whatever` directory it will load that variable `FOO` to the context. 

##### extract
Single commnad to extract/unzip/untar/etc various formats of the archives `extract bla.zip`.

##### mosh
Replacement for `ssh` and it gives autocomplition.

##### timer
Shows time spent on executing command or being inside app like `htop`. 

##### zsh-autosuggestions
It looks into history and hints commands executed in the past, if they match what is typed at the moment. Arrow keys are used for complition. 

Install it from the [script](https://github.com/DevInsideYou?q=install-zsh&type=&language=).

##### zsh-syntax-highlighting
Shows in red when somehting is done in a wrong way. Highlights in green correct zsh commands.

Install it from the [script](https://github.com/DevInsideYou?q=install-zsh&type=&language=).

##### zsh-z
Fuzzy search into directories one have already been so far e.g.: `z coll` searches for `homebrew-collections` direcotry and jumps there.

Install it from the [script](https://github.com/DevInsideYou?q=install-zsh&type=&language=).

##### fzf
Fuzzy searcher for text (e.g.: list of commands). Similar to `ctrl + shift + R`, but order of words does not matter and gives more Google like experience.

Install it from the [script](https://github.com/DevInsideYou/install-fzf).

During installation do NOT update shell configuration files. Instead there should be below IF in `.zshrc` file. 

```
if [ -f "$HOME/.fzf.zsh" ] ; then
	source "$HOME/.fzf.zsh"
fi
```

###### Usage
In Terminal type:

- `fzf` and keep typing to search for phrase
- `ll | fzf` and keep typing to search for phrase
- `ctrl + shift + R` integrates with shell search, so keep typing to search for phrase

