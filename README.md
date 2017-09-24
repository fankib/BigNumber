# BigNumber
Separate the definition and the execution of Java BigInteger operations

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

