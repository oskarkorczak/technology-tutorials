## Diagnostics
Few tips and tricks for diagnosing a problem with computer such as `kernel panic` etc. 
Techinques for testing each component to narrow down the problem.


### RAM
* Download `MemTest64`
* Create its image on pendrive
* Boot computer from pendrive
* Run test suite. It takes to run a full suite about 6-8h

### SSD

#### Drive Test
These tests check mechanical, eletrical and many others features of the drive. 

* Install `SmartMonTools` as it is not in Ubuntu Live distro
* Get basic info about your drive and see if it is in drive's DB: `sudo smartctl -i /dev/nvme0n1p2`
* Run short test: `sudo smartctl -t short -a /dev/nvme0n1p2`. Look for `PASSED` in the result section at the top.
* Run long test: `sudo smartctl -t long -a /dev/nvme0n1p2`. Look for `PASSED` in the result section at the top.

#### Partitions
There is also a Ubuntu GUI tool called `gparted` available from live distro allowing to see all drives together with their partitions e.g.: 

* `nvme0n1` main drive
* `nvme0n1p1` partition #1
* `nvme0n1p2` partition #2

#### Mounting & Retrieving Data
* Run Ubuntu Live distro and run terminal
* `sudo mkdir /media/ssd`
* `gparted` GUI tool shows NVMe drive location e.g.: `/dev/nvme0n1p2` and file system `ext4`
* `sudo mount /dev/nvme0n1p2 /media/ssd`
* `cd /media/ssd/` and browse data on your drive