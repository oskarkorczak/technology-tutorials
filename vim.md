# Vim useful commands
Bear in using `Vim` is much better option than only `Vi`.


If `x` is a command, then`shift x` or `X` works in opposite way.

Vim has a configuration file called `~/.vimrc` where all `set <option` commands are set once and for all. 


## Navigation

* `:set nocompatible` don't be compatible with old Vim version, all new stuff works fine
* `e` go to END of the word
* `b` go to BEGINING of the word
* `$` go to end of the LINE
* `^` go to beginning of the LINE
* `G` go to end of entire TEXT
* `gg` go to beginning of the entire TEXT
* `7G` go to line no 7 (where `G` may act as GO TO) or through the command `:3 <enter>`
* `*` go to FUNCTION DEFINITION while standing in the middle of the function call (function name highlighted by cursor)
* `%` go to OPENING/CLOSING parenthesis `{` or `}`, `(` or `)`


## Search

* `/<phrase>` FORWARD search for a `phrase`
* `shift /<phrase>` BACKWARD search for a `phrase`
* `:set incsearch <phrase>` incremental search; highlights parts of the searched word AS YOU TYPE
* `n` NEXT matching pattern
* `N` PREVIOUS matching pattern
* `:set hlsearch` highlights all `<phrase>` you are looking for; `n` works as NEXT with different highlight colour
* `nohl` switch off `hlsearch` highlights


## Edition

* `i` INSERT mode; start typing BEFORE the cursor
* `a	` APPEND mode; start typing AFTER the cursor
* `u` UNDO last action
* `ctrl r` REDO; undo last action ie UNDO
* `o` OPEN new paragraph BELOW current line and move cursor there
* `O` OPEN new paragraph ABOVE current line and move cursor there
* `x` CUT the highlighted char (similar to `ctrl x` on Windows)
* `r <new-char>` REPLACE current char to `<new-char>`
* `c <direction>` CHANGE text from current position (could be in the middle of the word) to `<direction>` which could be:
	- `e` END of the word
	- `b` BEGINNING of the word
	- `3e` END of 3 words
	- `tH` TILL letter `H`
	- `i{` or `i}` INSIDE CURLY BRACE; doesn't matter if you use opening or closing brace ie useful when you write a `function abc() {...}`
	- `i"` INSIDE QUOTE
* `R` PERMANENT REPLACE like `r` until revoked by `ESC`
* `:set autoindent` automatically INDENT lines after `enter` and indents even more after `TAB`
* `dd` DELETE this line
* `d5d` or `5dd` DELETE 5 lines
* `v` VISUAL; select small text fragments
* `V` VISUAL; select lines of text; then you may `d` to DELETE it
* `yy` YANK; copies selected text
* `p` PASTE BELOW; paste text from clipboard
* `P` PASTE ABOVE; paste text from clipboard
