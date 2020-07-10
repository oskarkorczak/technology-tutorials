## Python project set up
How to set up Terminal for working with Python.

### virtualenv

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
