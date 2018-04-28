<!--ts-->
   * [argparse](#argparse)
   * [gzip file](#gzip-file)
   * [build-in decorators](#build-in-decorators)
<!--te-->

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

func without any decorators is the usual way an object instance calls a method. The object instance, a, is implicitly passed as the first argument.

- @classmethod makes a method whose first argument is the class it's called from (rather than the class instance)
- @staticmethod does not have any implicit arguments.
Why we need @staticmethod?
You might want to move a function into a class because it logically belongs with the class. In the Python source code (e.g. multiprocessing,turtle,dist-packages), it is used to "hide" single-underscore "private" functions from the module namespace. Its use, though, is highly concentrated in just a few modules -- perhaps an indication that it is mainly a stylistic thing. Though I could not find any example of this, @staticmethod might help organize your code by being overridable by subclasses. Without it you'd have variants of the function floating around in the module namespace

**@property**
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


