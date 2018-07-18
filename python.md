#### Table of Content
   * [argparse](#argparse)
   * [gzip file](#gzip-file)
   * [build-in decorators](#build-in-decorators)
   * [visualisation](#visualisation)
        * [subplots using pandas](#subplots-using-pandas)
        * [subplots using matplotlib](#subplots-using-matplotlib)
   * [feature for debag](#feature-for-debag)
   * [optimization](#optimization)
        * [decorator timer](#decorator-timer)
        * [time complexity](#time-complexity)
   * [heaps](#heaps)
   * [namedtyple](#namedtyple)
   * [split list feature](#split-list-feature)
   * [reader](#reader)

#### Argparse

``` python
import argparse
parser = argparse.ArgumentParser()
parser.add_argument(
        "--gamma",
        nargs='+',
        type=float,
        default= [1.5, 1.6, 1.7],
        help="list of gammas for 4 bytes IPs")
parser.add_argument(
        "--rng_seed",
        type=str,
        default="",
        help="random seed to initialize a pseudorandom number generator")
FLAGS, unparsed = parser.parse_known_args()
print(FLAGS.gamma, FLAGS.rng_seed)
```

#### gzip file
``` python
import gzip
content = ["Lots of content here", "and here"]
f = gzip.open('file_path/file.txt.gz', 'wb')
f.writelines(content)
f.close()
```

#### build-in decorators
``` python
#Example:
class A(object):
    def foo(self,x):
        print "executing foo(%s,%s)"%(self,x)
 
    @classmethod
    def class_foo(cls, x):
        print "executing class_foo(%s,%s)"%(cls,x)
 
    @staticmethod
    def static_foo(x):
        print "executing static_foo(%s)"%x   
a=A()
 
a.foo(1)
# executing foo(<__main__.A object at 0xb7dbef0c>,1)
 
a.class_foo(1)
# executing class_foo(<class '__main__.A'>,1)
 
a.static_foo(1)
# executing static_foo(1)
 
A.static_foo('hi')
# executing static_foo(hi)
```


Basically:

- **func without any decorators** is the usual way an **object instance** calls a method. The object instance, a, is implicitly passed as the first argument
- **@classmethod** makes a method whose first argument is the **class** it's called from (**rather than the class instance**)
- **@staticmethod** does not have any implicit arguments.
	Why we need @staticmethod?
	You might want to move a function into a class because it logically belongs with the class. In the Python source code (e.g. multiprocessing,turtle,dist-packages), it is used to "hide" single-underscore "private" functions from the module namespace. Its use, though, is highly concentrated in just a few modules -- perhaps an indication that it is mainly a stylistic thing. Though I could not find any example of this, @staticmethod might help organize your code by being overridable by subclasses. Without it you'd have variants of the function floating around in the module namespace

- **@property**
The first part is simple:
    @property
    def x(self): ...
is the same as
    def x(self): ...
    x = property(x)
which, in turn, is the simplified syntax for creating a property with just a getter.
The next step would be to extend this property with a setter and a deleter. And this happens with the appropriate methods:
    @x.setter 
    def x(self, value): ...
returns a new property which inherits everything from the old x plus the given setter.
    x.deleter 
works the same way

Details: <br>
- https://docs.python.org/2/howto/descriptor.html
- https://docs.python.org/3/library/functions.html#property
- https://goo.gl/cLYMI2

#### Visualisation
##### Subplots using pandas
``` python
import pandas as pd
import matplotlib.pyplot as plt
%matplotlib inline
 
fig, axes = plt.subplots(1, 2)
df.plot.scatter("A", "B", figsize=(8,4), ax=axes[0])
df.plot.scatter("A", "C", figsize=(8,4), ax=axes[1])
plt.title("title")
plt.show()
```
##### Subplots using matplotlib
``` python
fig, ax = plt.subplots(nrows=2, ncols=5, figsize=(5,2))
 
ax[0][0].plot(x,y)
ax[0][1].plot(x,y)
ax[0][2].plot(x,y)
ax[0][3].plot(x,y)
ax[0][4].plot(x,y)
ax[1][0].plot(x,y)
ax[1][1].plot(x,y)
ax[1][2].plot(x,y)
ax[1][3].plot(x,y)
ax[1][4].plot(x,y)
plt.show()
```
#### Feature for debag
``` python
def _start_shell(local_ns=None):
    # An interactive shell is useful for debugging/development.
    import IPython
    user_ns = {}
    if local_ns:
        user_ns.update(local_ns)
    user_ns.update(globals())
    IPython.start_ipython(argv=[], user_ns=user_ns)
 
if FLAGS.interactive:
    _start_shell(locals())
```

#### Optimization
##### decorator timer
``` python

import time
from functools import wraps
def fn_timer(function):
    @wraps(function)
    def function_timer(*args, **kwargs):
        t0 = time.time()
        result = function(*args, **kwargs)
        t1 = time.time()
        print ("Total time running %s: %s seconds" %
               (function.func_name, str(t1-t0))
               )
        return result
    return function_timer
 
@fn_timer
def job():
    print "Hi"
    time.sleep(1)
 
job()
```
##### Time Complexity
https://wiki.python.org/moin/TimeComplexity


##### Heaps
``` python
import heapq

# min heap
a = list(range(10))[::-1]
heapq.heapify(a) # build heap

# priority heap
a = [(0.9, "a"), (0.2, "b"), (0.5, "c"), (0.1, "d"), (0.15, "e")]
heapq.heapify(a)
heapq.nlargest(3, a)
# results:  [(0.9, 'a'), (0.5, 'c'), (0.2, 'b')]

# available methods of heapq class:
# dir(heapq)
# -heapify
# -heappop
# -heappush
# -heappushpop
# -heapreplace
# -merge
# -nlargest
# -nsmallest
```



##### Namedtyple

``` python
from collections import namedtuple
# simple way to create class
Cell = namedtuple("my_cell", ["x", "y", "z"])  #, verbose=True
a = Cell(1,2,3)
print (a)
# my_cell(x=1, y=2, z=3)
cells = []
for x,y,z in zip(range(0,7), range(1,8), range(2,9)):
    cells.append(Cell(x,y,z))
print (cells)
"""
[my_cell(x=0, y=1, z=2),
 my_cell(x=1, y=2, z=3),
 my_cell(x=2, y=3, z=4),
 my_cell(x=3, y=4, z=5),
 my_cell(x=4, y=5, z=6),
 my_cell(x=5, y=6, z=7),
 my_cell(x=6, y=7, z=8)]
"""

# if you want to add a methods to namedtyple, you can inherit namedtuple
class Cell(namedtuple("my_cell", ["x", "y", "z"]))
    def lenght(self):
        return (self.x**2 + self.y**2 + self.z**2)**0.5
```


##### Split list feature
``` python
a = [7,1,2,3,4,5,6,7]  # a[0] - number of items, a[1:] - items
num, *items = a  # pythonik way to split list
```

##### Reader

``` python
import sys
reader = (map(int, line.split()) for line in sys.stdin)
n, *items = next(reader)
k, *queries = next(reader)
```

##### Function memorization

``` python
from functools import lru_cache
import time

@lru_cache(maxsize = None)  # unlimited max size
def calc_sum(a,b):
    time.sleep(2)
    return a + b

# %time calc_sum(1,2)
# CPU times: user 1.14 ms, sys: 42 µs, total: 1.18 ms
# Wall time: 2 s
# again
# %time calc_sum(1,2)
# CPU times: user 10 µs, sys: 1e+03 ns, total: 11 µs
# Wall time: 16.2 µs
```

