# [Safe Shell Scripts][yt-course-bash]
How to write **safe** Bash shell scripts. This is especially useful, when running someone else's external scripts e.g.: downloaded from the Internet on a server. This is potentially a very destructive and unsafe operation, even though the script may look harmless. Often neglected non-happy day scenarios, can cause server destruction and/or data loss.

## Detecting Unsafe Shell Scripts
Script for sample debugging is kept in the same directory, as this file lives in. It is called `beznadziejny_instalator_wp.sh` and note that is does **NOT** have `-x` right set for execution (safety reasons). **If run from `root` it removes data!**

### Entry Params

#### Problem

```
# Wywołanie: ./skrypt.sh /var/www/wordpress
...
if [ -z $1 ]; then
    echo "Nie podano katalogu instalacji!";
fi
```
This script takes a single parameter, a path to directory, where wordpress should be installed. 

We have `if [ -z ...]` which means `zero length`, which means if the given script param has zero length (is not passed), then execute `echo`. 

`$1` is a param number passed to bash script. This means first param. 

#### Solution
Missing `exit` after `echo` in the `if [ -z ...]`, so that we can leave the script. 

```
# Wywołanie: ./skrypt.sh /var/www/wordpress
...
if [ -z "$1" ]; then
    echo "Nie podano katalogu instalacji!";
    exit;
fi
```

Also there are quotes added around `"$1"`, for more details see one of the problems below dealing with changing directory `cd $1`.

### Lock File

#### Problem

```
# zabezpieczenie przed uruchomieniem instalator 2x w tym samym czasie
if [ -f wp.lock ]; then
    echo "Poczekaj najpierw na zakończenie wywołania poprzedniej kopii skryptu"
    exit;
fi
```
`if` checks if the `lock` files exists. It is a mechanism allowing to prevent script multirun. Two users may run this installation script, resulting in two installations. 

`-f` checks, if the file exists. If file exists, then display `echo` and `exit`. Apparently, there is no error here. 

Next there is:

```
# zakładamy plik z lockiem
touch wp.lock
```
Order is also correct, as we need to first check the `lock` existence and only if it does not exist, create a `lock` and start installation. 

Problem is with two, potential users running the scrip same time. Let's assume the script is in a shared location, which user A and B can access. If A and B run the scrip from their `home` directory, then the `lock` file will be created in their respective `home` dirs. It will not prevent us from the script multirun problem, at all. 


#### Solution
We have to create the `lock` in a single, central location, accessible by both users. 

```
# zabezpieczenie przed uruchomieniem instalator 2x w tym samym czasie
if [ -f /tmp/wp.lock ]; then
    echo "Poczekaj najpierw na zakończenie wywołania poprzedniej kopii skryptu"
    exit;
fi

# zakładamy plik z lockiem
touch /tmp/wp.lock
```

### Changing Directory

#### Problem

```
# idiemy do katalogu instalacyjnego
cd $1
```
We go to the script's first param `$1` and we count files. The issue here is with `$1`. The directory may **not** exist or the path may have some spaces (or special chars)like so `/var/www/my websites/wp`.

#### Solution

Addressing space or special char in the path name, we have to use `"$1"` in quotes, otherwise code may fail. To be frank, we have to do it in all relevant `if` statements too!

Below options refer to dir existence. 

##### Option 1
Try to set up the `$1` directory to make sure it exists. 

```
mkdir -p $1
# idiemy do katalogu instalacyjnego
cd "$1"
```
`mkdir -p` sets all dir tree necessary for `$1`.

##### Option 2
Go to `$1` and if it does not work, then exit. `||` means OR. 

```
# idiemy do katalogu instalacyjnego
cd "$1" || exit
```


### Greater Than

```
# ile jest plików w katalogu instalacji?
liczbaPlikow=$(ls * | wc -l)

# czyscimy miejsce instalacji jeśli są tam jakieś pliki
if [ $liczbaPlikow > 0 ]; then
    rm -rf *
fi
```

Variable `liczbaPlikow` receives a command result `$(ls * | wc -l)`. `ls *` prints all dirs and files. Next `|` gets that stream and counts amont of lines `wc -l`. In this case, `ls *` provides each dir and file in a single line to `|` operator, so the `liczbaPlikow` variable is populated by total amount of dirs and files in the directory. 

Next, we have a very serious instruction `rm -rf *` guarded, by some condition. Only if the condition was written correctly. `> 0` is **not** the way to define grater than zero in Bash. What it means is, if the `liczbaPlikow` was redirected (streamed) to a file called `0`, then execute `rm -rf *`. This streaming or file saving command **always** works!!


#### Solution

```
# czyscimy miejsce instalacji jeśli są tam jakieś pliki
if [ $liczbaPlikow -gt 0 ]; then
    rm -rf "$1"/*
fi
```
`-gt` means greater than. Also, it is safer to add `"$1"/` before `*` to make it much safer. 



### Download

#### Problem
```
# ściągamy instalator
wget https://wordpress.org/latest.tar.gz -O /tmp/wp.tar.gz
```
Here is relatively okay, as we use `wget` for download of external script. The official site is used for that. `-O` defines output, which is in `/tmp` direcotry; again safe place as it could be removed just in case. 

The only thing here is that the network theoretically may not work.


#### Solution
We should check, before we proceed further, if the file is correctly downloaded. 

```
# ściągamy instalator
wget https://wordpress.org/latest.tar.gz -O /tmp/wp.tar.gz

if [ $? -ne 0 ]; then
	echo "cos jest nie tak z siecia";
	exit;
fi
```
`-ne` not equal `0` checks if the `wget` exited without errors. Otherwise, `echo` message and `exit`. 

### Extract/Unzip

#### Problem
```
# czas na rozpakowanie
tar -zxvf wp.tar.gz
```
We unpack the newly downloaded file. `-z` means file is gziped, `-x` means extract, `-v` verbose and `-f` file locaiton.  

Problem is we download to `/tmp` dir, but we unpack from local direcotry. 

#### Solution

```
# czas na rozpakowanie
tar -zxvf /tmp/wp.tar.gz
```

### Installator Removal

#### Problem
```
# pozbywamy się instalatora
rm *.gz
```
The intention is to remove the installer. By using `*` we remove more `gz` files than just the installer. For example, we may also remove all backups in single run. 

#### Solution
We have to use full file name.

```
# pozbywamy się instalatora
rm wp.tar.gz
```

### Lock Removal

#### Problem

```
# usuwamy locka
rm wp.lock
```
Lock should be removed from `/tmp` dir, as it is the common location accessible by more than one user. Otherwise, the lock would be removed from local dir. 

#### Solution
```
# usuwamy locka
rm /tmp/wp.lock
```

### Target Shell (shebang)

#### Problem
Nobody defined the shell as bash. Which means the script itself is written in bash, but the actual shell could be `ash`, `zsh` etc. 


#### Solution
We have to explicitely define the shell.

```
#!/bin/bash
```

### Variables Inheritance

#### Problem
All the variables defined in this script will be inherited by the current shell, when the script finishes. 

#### Solution


## [Shellcheck][shellcheck]
Shellcheck checks for script vulnerabilities and lists all the issues it can detect. 

```
shellcheck instalator_wp.sh
```

## Shell Flags
Let's write another script from scratch. Let's call it `a.sh`.

### Undefined Variables (`set -u`)

`-u` (`unassigned`) exits the script, if the there is a use of variable never set by script developer. 

#### Example
If `set -u` is NOT set. 

```
#!/bin/bash
echo $imie
```
Let's run it. 

The console output is empty line and all works without any issue, even though `$imie` is not defined. 

##### Why is it unsafe?

There might be a code like that:

```
#!/bin/bash
echo $katalog

rm -rf $katalog/*
```
If `katalog` is empty, then all files starting from `root` will be removed. Newer Linux distributions prevent it, but still issue is an issue. 


### Non-Zero Exit Status (`set -e`) aka Fail Fast

`-e` checks exit codes of all commands and if any of them returned non-zero exit code, it breaks the script. 

#### Example

```
#!/bin/bash
set -ue
cd /tmp/non-existing-dir

echo "gotowe"
```
Script is stopped after unsuccessful execution of `cd` command. If `set -e` is not set, then `cd` would be unsuccessfully executed and would display `echo` as if all instructions were done correctly. 


### Pipefail (`set -o pipefail`)
It checks the exit code for all pipe commands and not only for the last one. 

#### Example
```
> grep batman /etc/passwd
> echo $? 
1
> grep batman /etc/passwd | sort
> echo $?
0
> grep batman /etc/non-existing-file | sort
> grep: /etc/non-existing-file: File of directory does not exist
> echo $?
0
> set -o pipefail
> grep batman /etc/non-existing-file | sort
> grep: /etc/non-existing-file: File of directory does not exist
> echo $?
2
```
`$?` checks the last executed command's exit code. 

`grep` exit code:

* `0` everything is okay
* `1` something is wrong
* `2` critical exception


## Debugging (traps)

```
#! /bin/bash

a=10
b=30
wynik=$[$a+$b]
echo $wynik
```
If you like to debug line by line, then set a `trap`. 


```
#! /bin/bash
set -x

trap read debug
a=10
b=30
wynik=$[$a+$b]
echo $wynik
```
`set -x` means debug every executed line. 

Run script and it will start in debug mode, where each line has to be confirmed by `enter` key. `Crtl + C` exits script.


[yt-course-bash]: https://www.youtube.com/watch?v=fiTTWcfFyYA
[shellcheck]: https://www.shellcheck.net/