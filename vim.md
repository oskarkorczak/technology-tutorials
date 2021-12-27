# Vim
Use `Vim` over `Vi`, as it is heavily improved. 

Usually lowercase verbs work forward (`x`) and uppercase work backwards (`shift x` or `X`).

Doubling the verb refers to appling it to current line (`dd`, `cc` etc.).


## Modes
`Vim` allows to use few different types of modes, depending on what you need to do with text file

* browse
* insert or add more text
* replace already existing text
* select text and manipulate it
* application configuration

### Normal (default)
This ia a `default` mode, where you land after entering application. You want to be in this mode as often as possible. This is an entry point to other modes.
Normal mode is a programming language in itself with its own syntax. 


### Insert (`i`)
Allows to insert text.

### Replace (`R`)
Allows to replace text.

### Visual Select (`v`)
This is for visual text selection letter by letter.

They say the Visual mode usage is usually a usage smell, as there are commands to do it and most likely the user's imagination is the limit, which he tries to jump over by using visual selection mode. All in all, `Vim` has a pretty powerfull programming language in `normal` mode. 

#### Line Select (`V`)
Allows to select entire lines in one go.

#### Block Select (`Ctrl v`)
Allows to select rectangular block of text. Similar to InteliJ block selection.

### Command  (`:`)
Allows to configure `Vim` usually via `set` command. 

Also, `Vim` has a configuration file called `~/.vimrc` where all `set <option>` commands are set once and for all. 

## Syntax (Gerb + Noun)
`Vim`'s `normal` mode allows to use a very powerful and simpole programming language to communicate with. 

### Verbs

* `d<MOTION>` for delete; eg. `dw` delete word and stay in normal mode
* `c<MOTION>` for change; eg. `ce` change word ie delete till end of word and put insert mode
* `><MOTION>` for indent
* `v<MOTION>` for visually select
* `y<MOTION>` for yank (copy)
* `.` for repeating the last modification; VERY USEFUL
* `i` INSERT mode; start typing BEFORE the cursor
* `a` INSERT mode; start typing AFTER the cursor
* `u` undo last action
* `ctrl + r` redo; undo last action ie undo


### Nouns (aka Motions)

* `w` for word
* `b` go to BEGINING of the word
* `e` go to END of the word
* `0` go to beginning of the line
* `$` go to end of the line
* `^` go to first non-empty char in the line (eg. `TABTABTAB- apples are sweet` it would go to `-`)
* `crtl + u` up in the buffer (aka page up)
* `crtl + d` down in the buffer (aka page down)
* `G` go to end of entire text (buffer)
* `gg` go to beginning of the entire text (buffer)
* `7G` go to line no 7 (where `G` may act as GO TO) or through the command `:3 <enter>`
* `2j` down x2 lines
* `L` lowest line on the screen
* `M` medium line on the screen
* `H` highest line on the screen
* `*` go to function definition while standing in the middle of the function call (function name highlighted by cursor)
* `%` go to closest opening/closing parenthesis `{` or `}`, `(` or `)`


#### Text Objects

* `iw` inner word
* `it` inner tag
* `i"` inner "
* `ip` inner paragraph
* `as` around sentennce (a sentence)

#### Parametrised Text Objects

* `f<char>` find text forward in the line
* `F<char>` find text backward in the line
* `t<char>` to text forward, but stop before the text in the line
* `T<char>` to text backward, but stop after the text in the line
* `/<phrase>` forward search of phrase
* `?<phrase>` backward search of phrase
* `n` go to next matching pattern
* `N` go to previous matching pattern


## Applications

* `o` open new paragraph BELOW current line and move cursor there
* `O` open new paragraph ABOVE current line and move cursor there
* `x` cut or delete the highlighted char
* `r<char>` replace current char with `<char>`
* `ce` change till the end of the word
* `cb` change till the beginning of the word
* `c3e` change till the end  of 3 words
* `ctH` change till the letter `H`
* `R` permanent replace like `r` until revoked by `ESC`
* `dd` delete this line
* `d5d` or `5dd` DELETE 5 lines
* `v` visual; select small text fragments
* `V` visual; select lines of text; then you may `d` to delete it
* `yw` copy word
* `yy` yank; copies selected text
* `p` paste below; paste text from clipboard
* `P` paste above; paste text from clipboard

## Selections
Use `v` for entering visual selection mode and then use `j`, `l` etc to select portion of text eg: `vw` select word and then you may use copy `y` which puts back in normal mode. 

`~` flips lowercase to uppercase and vice versa in selected portion of text. 

## Counts

* `4j` move down 4 lines
* `v3e` select 3 words till the end
* `7dw` or `d7w` delete 7 words

## Modifiers
There are two modifiers which change the meaning of verbs:

* `i` inner or inside (also excluding)
* `a` around (also including)

There are few usage examples:

* `c2w` change two words and put into insert mode so that you may add some other words instead
* `ci[` change `inside` square brackets and put insert mode
* `di(` delete `inside` parenthesis
* `da(` delete what is inside parenthesis and parenthesis themselves

## Screens

* `:sp` split pane; replicates the text file (buffer) in tab
* `ctrl Wk` move up
* `ctrl Wj` move down


## Configurations
* `:set nocompatible` don't be compatible with old Vim version, all new stuff works fine; enter the current millenium
* `syntax enable` enable syntax colouring
* `filetype plugin on` Vim built in plugin used for file browsing; MORE DESC NEEDED HERE
* `:set incsearch <phrase>` incremental search; highlights parts of the searched word AS YOU TYPE
* `:set autoindent` automatically INDENT lines after `enter` and indents even more after `TAB`
* `:set hlsearch` highlights all `<phrase>` you are looking for; `n` works as NEXT with different highlight colour
* `set relativenumber` turns on relative lines numbering allowing to see absolute line where the cursor is and line offsets (up & down) for all other lines. It allows faster naviagion like `7j` or `c6j`.
* `nohl` switch off `hlsearch` highlights

### Fuzzy Search
`Vim` could be used as fuzzy search tool for files in the current directory and any nesting level below. 

Necessary configurations:

* `set path+=**` use recurssive dir search for `:find` command
* `set wildmenu` shows a bar with available filenames while using `:find` search in `Vim`

What it gives:

* hit `tab` to `:find` by partial match
* use `*` to make it fuzzy

Complementary commands:

* `:b` lets you autocomplete any open buffer (file); requires to start typing filename and hitting `tab`
* `:ls` shows already open buffers (files) in `Vim`

## Help

* `:help` first man page
* `:help <phrase>` searches manual for a `<phrase>` (by default in normal mode)
	* `i_<phrase>` search for `<phrase>` in the insert mode
	* `c_<phrase>` search for `<phrase>` in the command mode
* `:helpgrep <phrase>` it searches and greps through every single help document it can find in the entire manual; most useful command
	* `cn` next document
	* `cp` previous document
	* `cl` list documents