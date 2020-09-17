## Python project set up
How to set up Terminal for working with Python.

### Create virtualenv from scratch

Python 3 presents opportunity of creating `venv` without `virtualenv` command:

Assumptions:

* current python is python3
* location is python project root directory

```
python -m venv venv
python --version
source venv/bin/activate

pip install -r app/requirements.txt

export PYTHONPATH="app/"
echo $PYTHONPATH  

// Coding stuff happens here
...
pytest -vv 

// Closing up work with repository
deactivate
```


### Activate existing virtualenv
If you virtualenv already exists, then it is just a matter of activating it:

```
source venv/bin/activate
pip install -r app/requirements.txt
```


### Setting up InteliJ project

This setup works on both InteliJ with Python plug-in or PyCharm for both subscription types (Community & Ultimate).

- Go to `Project structure` window
	+ select `Project` view in `Project settings` section in Left Hand Navigation (LHN)
	+ Add new project SDK (choose Python SDK)
- In `Add Python Interpreter`
	+ choose `Virtual Environment` in LHN
	+ click `New environment` radio button 
	+ `Location` points to `venv` environment setup in current Python project directory e.g.: `/path/to/python-project/venv`. If environment `venv` exists, it will only let you click OK. Remove the `venv` directory and set it up from scratch. 
- Ensure the SDK is selected in `Project` section in LHN and click OK
- A bar at the top of the screen should prompt for installing `requirements.txt`. Otherwise, import requirements manually via command line.
- Go to `Preferences` -> `Python Integrated Tools`and amend `Default test runner` in `Testing` section from `Unittest` to `pytest`
