# Multiplexers

Split, attach and detach terminal windows. 

## Installation
Install Byobu (Vlad's script also adds xclip tool allowing to copy and paste outside tmux/byobu)

Set up linux alias "b" for byobu (perhaps create install script like Vlad has on his GH)

## Configuration
Byobu configuration folder location: `~/.byobu`.


### Two terminals attached to same session bug fix
In order to address some weird session name related issue in Byoby, while connecting to same session from two separate terminals, create empty file in main config directory:

```
cd ~/.byobu
touch .reuse-session
```