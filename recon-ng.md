# Recon-ng Tutorial
Use TAB everywhere to get hints on available commands, modules and in general work much faster than typing. 

## Installation 

Ubuntu: `sudo apt install recon-ng`


## Running

There are three apps installed:

* `recon-ng` terminal 
* `recon-web` web DB tables browser
* `recon-cli` ???


## Modules
Fresh installation has no modules installed. 

### Install all

Install all available modules in one go:

```
marketplace refresh
marketplace info all 
marketplace install all
```


### Install single

Install only chosen modules:

```
marketplace search <TAB> => show what can I choose from
marketplace search hackertarget
marketplace info hackertarget
marketplace install recon/domains-hosts/hackertarget

marketplace search domains-h <Enter> => show all modules form "domains" to "hosts"
```


### Search

Search could be also used as a filter to find all `shodan` modules, which incidently require API key to be set up. 

```
marketplace search shodan
```


### Usage

When module is already installed, then you may start using it. 

#### Loading

Load module using full or partial name. Usually the actual modul name is enough, or any string allowing to identify module uniquely. 

```	
modules load recon/domains-contacts/pgp_search
modules load pgp_search
info => shows module settings
```

#### Running 

When module is loaded it has to be run: `run`.


## Research


### Workspace

Go to workspace: `workspaces create/select panoptykon`

Add URL to domains table with no protocol: ``db insert domains panoptykon.org`


### Useful commands

```
show <db-table-name>
dashboard
```


### Sample run

```
recon/domains-hosts/certificate_transparency
recon/domains-hosts/google_site_web
recon/domains-hosts/hackertarget
```


Having some hosts names now we can try to resolve their IPs:

```
recon/hosts-hosts/resolve
recon/hosts-ports/binaryedge => checks in BinaryEdge DB if the host was scanned by BinaryEdge before looking of open ports. Information comes without scanning theirs hosts

```

## [Tutorial][tutorial]




## API Key 

### Management

* `keys list` - show all API keys
* `keys add key value` - add key
* `key remove key` - remove key


### Popular APIs
List of most important and useful APIs. 

### Shodan
Probably the most important and popular. Definitely set up a key with Shodan. 

When accessing Shodan too often, the website blocks access temporarily. 

#### [Twitter][twitter-dashboard]
Gives pushpin access and few other modules also use it. 









[tutorial]: https://geekwire.eu/recon-ng-v5-tutorial/
[twitter-dashboard]: https://developer.twitter.com/en