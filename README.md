# BigNumber
Separate the definition and the execution of Java BigInteger operations. Define your CPU shredding calculations and push them into your infrastructure with an API similar to BigInteger.

## Define your calculations

```java
BigNumber a = new BigNumber("1");
BigNumber b = new BigNumber("2");
BigNumber c = a.add(b);
```

## Resolve your calculations
```java
BigNumberEngine engine = new ParallelNumberEngine();
engine.resolve(c); // c="3"
```
```java
BigNumberEngine engine = new BatchingNumberEngine();
engine.resolve(listOfBigNumbers);
```

## Lazy predicates
```java
BigNumber z = ORDER.random();
BigNumber mq = z.modExp(ORDER, MODULUS).test(BigNumber.ONE) // default is an equals test
  .then(z)
  .otherwise(MODULUS.subtract(z)));
```

# BigNumberEngines
The following engines to resolve a BigNumber are implemented:
* **RecursiveNumberEngine**
Single threaded, walks the tree up to the root and starts resolving it.
* **ParalellNumberEngine**
Separates the tree in its layers and resolves this layers parallel.
* **BatchingNumberEngine**
Separates a List of BigNumber into chunks and reslove this chuncks on different Threads with the recursive strategy.
* **HazelcastNumberEngine**
Separates a List of BigNumber into chunks and resolve this chunks with an HazelcastServiceExecutor and the recursive strategy.
