# makemake

Generates a makefile using the C files in the current directory. Is not recursive (yet!). Primarily for the purpose of writing a proper program using Clojure. 

## Installation

Install the leiningen build tool if it is not installed already.

    $ git clone github.come/aaparella/makemake
    $ cd makemake
    $ lein uberjar

## Usage

    $ java -jar makemake-0.1.0-standalone.jar [path] [executable name]

## Examples

    $ java -jar makemake-0.1.0-standalone.jar . my_program.out

### Bugs

* Does not recursively search for source files, will only look at the top level of the directory that is specified. 
* Currently lacks tests.

Copyright © 2016 Alexander Parella

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
