# Assignment 1 README #

Glenn Hartwell  
Spring 2022

I will start by saying that if you're using vscode to view this then you can press ctrl+shift+V to change the view so this will look pretty for you.

## Definitions ##

**floatInput**: a number that can be represented as a float; i.e. 100000000 or 1e8  

**/directory_of_file**: this is the location where the file is saved on your computer

## Compilation ##

To compile and run this program at the command line in Ubuntu or Windows Powershell please do the following:  

>`> /directory_of_file javac Assignment1.java`  
>`> /directory_of_file java Assignment1 <floatInput>`

## After Execution ## 

Once the program has finished running it will create a file "primes.txt", in the same directory where the program  
file is stored, where information from the run can be viewed.

## Efficiency ##

This program works by implementing a traditional Sieve of Eratosthenes algorithm and using an atomic integer  
to allow multiple threads to mark off multiples of known primes concurrently. Each thread accesses the atomic integer  
and atomically increments it appropriately so that other threads may continue to work. This is a lock-free algorithm.

I tested several powers of 10 for correctness and compared their runtimes to a sequential run of the same algorithm.  
As the input variable, *n*, increases the parallel algorithm's speed up is decreased. It is likely this is because the  
space between prime numbers becomes much larger as *n* increases and therefore requires more computation time to mark  
off multiples of the primes it finds.

### Some other steps I took to decrease runtime ###
Instead of the isPrime approach where the entire array needs to be initialized to true, I decided to use a double  
negative isNotPrime to avoid initialization and to keep the naming logical. This seemed to save approximately 30-50ms  
of time. I also wrote the loops to never consider multiples of 2 (we know even number aren't prime) which added some  
extra O(1) checks when searching for the top ten primes. I assume this improved the runtime, however I started with the loops  
performing this action so I never acquired a runtime for a sieve that *does* consider all of the evens.