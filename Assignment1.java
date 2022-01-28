import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;


/*
 * Glenn Hartwell
 * Assignment1
 * Spring 22
 * 
 * 
 * The sequential and concurrent solutions here are adaptations 
 * of the sieve of eratosthenes code from geeksforgeeks
 */
public class Assignment1{

    // allows for returning different 
    final static int PRIME_LIST_SIZE = 10;

    // Concurrent solution to print number of primes <= n,
    // the sum of those primes, the ten greatest
    // primes in ascending order, and the execution time
    // to a text file
    public static void concurrentDoPrimeWork(int n)
    {
        // Considering the array logically as it is named
        // keeps me from needed to intialize with a loop, runtime saver :)
        boolean isNotPrime[] = new boolean[n+1];

        // Start with 2 already counted as a prime
        // to make loop faster
        AtomicInteger currInt = new AtomicInteger(3); // haha curr Int, get it???
        int primeCount = 1;
        long primeSum = 2;
        
        // Class that implements the parallelizable part of Sieve
        class PrimeThread implements Runnable
        {
            // Sieve of Eratosthenes modified to never consider
            // multiples of 2
            public void run() 
            {
                int i = currInt.getAndAdd(2);
                while (i * i <= n)
                {
                    // If i is prime, silly logic make program run little bit faster :)
                    if (!isNotPrime[i])
                    {
                        // I tried to count and sum as I marked off for effeciency but realized
                        // the algo does not touch all primes 

                        // Set all multiples of this prime to not prime
                        for (int j = i * i; j <= n; j += i)
                        {
                            isNotPrime[j] = true;
                        }
                    }
                    // increment loop var
                    i = currInt.getAndAdd(2);
                }
            }
        }

        // There's got to be a cleaner way to do this...
        try
        {
            PrimeThread pt = new PrimeThread();
            Thread t1 = new Thread(pt);
            Thread t2 = new Thread(pt);
            Thread t3 = new Thread(pt);
            Thread t4 = new Thread(pt);
            Thread t5 = new Thread(pt);
            Thread t6 = new Thread(pt);
            Thread t7 = new Thread(pt);
            Thread t8 = new Thread(pt);
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            t8.start();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.toString());
        }

        // Count and sum primes
        for (int i = 3; i <= n; i+=2)
        {
            if (!isNotPrime[i])
            {
                primeCount++;
                primeSum += i;
            }
        }

        // Push last ten primes onto stack
        int i = 0;
        int j = n;
        ArrayDeque<Integer> lastTen = new ArrayDeque<>();
        while (i < PRIME_LIST_SIZE)
        {
            // We ignore multiples of 2 in O(1) time
            // Push if prime, count number so far
            // otherwise ignore
            if (j % 2 == 0)
            {
                j--;
            }
            else if (!isNotPrime[j])
            {
                lastTen.push(j);
                i++;
                j--;
            }
            else
            {
                j--;
            }
        }

        // Write to File
        try 
        {
            // Create file if not there
            File outputFile = new File("primes.txt");
            outputFile.createNewFile();

            // write to file
            PrintWriter writer = new PrintWriter(new FileWriter("primes.txt", true));
            writer.println("======== Concurrent Solution ========");
            writer.println("Number of primes: " + primeCount);
            writer.println("Sum of primes: " + primeSum);

            i = 0;
            writer.println("Last " + PRIME_LIST_SIZE + " Primes: ");
            while(i < 10)
            {
                writer.println(lastTen.pop());
                i++;
            }
            writer.close();
            
        } 
        catch (IOException e) 
        {
            System.out.println("There was an error while creating your file.");
            System.out.println(e.toString());
        }

        // This code prints to the console.
        // OUTPUT
        // System.out.println("======== Concurrent Solution ========");
        // System.out.println("Number of primes: " + primeCount);
        // System.out.println("Sum of primes: " + primeSum);
        
        // // Pop stack to get last ten in ascending order
        // i = 0;
        // while(i < 10)
        // {
        //     System.out.println("Last " + PRIME_LIST_SIZE + " Primes: " + lastTen.pop());
        //     i++;
        // }
    }

    // Sequential solution to print number of primes <= n,
    // the sum of those primes, the ten greatest
    // primes in ascending order, and the execution time
    // to a text file
    public static void sequentialDoPrimeWork(int n)
    {
        // Considering the array logically as it is named
        // keeps me from needed to intialize with a loop, runtime saver :)
        boolean isNotPrime[] = new boolean[n+1];

        // Start with 2 already counted as a prime
        // to make loop (slightly) faster
        int primeCount = 1;
        long primeSum = 2;

        // Sieve of Eratosthenes modified to never consider
        // multiples of 2
        for (int i = 3; i * i <= n; i += 2)
        {
            if (!isNotPrime[i])
            {
                for (int j = i * i; j <= n; j += i)
                {
                    isNotPrime[j] = true;
                }
            }
        }

        // Count and sum primes
        for (int i = 3; i <= n; i+=2)
        {
            if (!isNotPrime[i])
            {
                primeCount++;
                primeSum += i;
            }
        }

        // Push last ten primes onto stack
        int i = 0;
        int j = n;
        ArrayDeque<Integer> lastTen = new ArrayDeque<>();
        while (i < PRIME_LIST_SIZE)
        {
            // We ignore multiples of 2 in O(1) time
            // Push if prime, count number so far
            // otherwise ignore
            if (j % 2 == 0)
            {
                j--;
            }
            else if (!isNotPrime[j])
            {
                lastTen.push(j);
                i++;
                j--;
            }
            else
            {
                j--;
            }
        }

        // Write to file
        // This is where the file is initially created
        try 
        {
            // Create file if not there
            // If the file already exists nothing happens.
            File outputFile = new File("primes.txt");
            outputFile.createNewFile();

            
            // write to file and overwrite any old data from previous runs
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile, false));;
            writer.println("======== Sequential Solution ========");
            writer.println("Number of primes: " + primeCount);
            writer.println("Sum of primes: " + primeSum);

            i = 0;
            writer.println("Last " + PRIME_LIST_SIZE + " Primes: ");
            while(i < 10)
            {
                writer.println(lastTen.pop());
                i++;
            }
            writer.close();
            
        } 
        catch (IOException e) 
        {
            System.out.println("There was an error while writing your file.");
            System.out.println(e.toString());
        }

        // This code prints to the console.
        // // OUTPUT
        // System.out.println("======== Sequential Solution ========");
        // System.out.println("Number of primes: " + primeCount);
        // System.out.println("Sum of primes: " + primeSum);
        // i = 0;
        // while(i < 10)
        // {
        //     System.out.println("Last " + PRIME_LIST_SIZE + " Primes: " + lastTen.pop());
        //     i++;
        // }

    }
    
    public static void main(String[] args) {
        // System.out.println();
        long start = System.currentTimeMillis();
        sequentialDoPrimeWork((int)Float.parseFloat(args[0]));
        long end = System.currentTimeMillis();

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter("primes.txt", true));
            writer.println("Execution time: " + (end - start) + " ms");
            writer.println();
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("There was an error while writing your file.");
            System.out.println(e.toString());
        }

        long start2 = System.currentTimeMillis();
        concurrentDoPrimeWork((int)Float.parseFloat(args[0]));
        long end2 = System.currentTimeMillis();

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter("primes.txt", true));
            writer.println("Execution time: " + (end2 - start2) + " ms");
            writer.println();
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("There was an error while writing your file.");
            System.out.println(e.toString());
        }
        
    }
}