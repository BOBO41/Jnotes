# PriorityQueue

**注意：我会跳过1.8相关的知识，例如forEach方法等等**

优先队列

![1545386152710](/home/hdr/Documents/pictures/Java集合源码解读/优先队列继承关系.png)

## 定义

```java
public class PriorityQueue<E> extends AbstractQueue<E> implements java.io.Serializable {
    // 。。。
}
```

## 字段

```java
private static final long serialVersionUID = -7720805057305804111L;

private static final int DEFAULT_INITIAL_CAPACITY = 11; // 默认初始化容量

transient Object[] queue; // 为了方便内部类获取该字段，所以不设置为private

private int size = 0; // 队列中元素个数

private final Comparator<? super E> comparator; // 用来做对比

transient int modCount = 0; //队列的结构被修改过多少次，为了方便内部类获取该字段，所以不设置为private
```

## 构造器

```java
public PriorityQueue() {
    this(DEFAULT_INITIAL_CAPACITY, null);
}
// 指定初始化容量
public PriorityQueue(int initialCapacity) {
    this(initialCapacity, null);
}
// 指定Comparator
public PriorityQueue(Comparator<? super E> comparator) {
    this(DEFAULT_INITIAL_CAPACITY, comparator);
}

// 指定初始化容量 和 Comparator
public PriorityQueue(int initialCapacity,
                     Comparator<? super E> comparator) {
    // Note: This restriction of at least one is not actually needed,
    // but continues for 1.5 compatibility
    if (initialCapacity < 1)
        throw new IllegalArgumentException();
    this.queue = new Object[initialCapacity];
    this.comparator = comparator;
}

@SuppressWarnings("unchecked")
public PriorityQueue(Collection<? extends E> c) {
    if (c instanceof SortedSet<?>) {
        SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
        this.comparator = (Comparator<? super E>) ss.comparator();
        initElementsFromCollection(ss);
    }
    else if (c instanceof PriorityQueue<?>) {
        PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
        this.comparator = (Comparator<? super E>) pq.comparator();
        initFromPriorityQueue(pq);
    }
    else {
        this.comparator = null;
        initFromCollection(c);
    }
}

@SuppressWarnings("unchecked")
public PriorityQueue(PriorityQueue<? extends E> c) {
    this.comparator = (Comparator<? super E>) c.comparator();
    initFromPriorityQueue(c);
}

@SuppressWarnings("unchecked")
public PriorityQueue(SortedSet<? extends E> c) {
    this.comparator = (Comparator<? super E>) c.comparator();
    initElementsFromCollection(c);
}
```

## 方法

### 真正执行初始化的方法

从构造器中，我们可以看到几个有趣的方法

- initFromPriorityQueue
- initElementsFromCollection
- initFromCollection

```java
private void initFromPriorityQueue(PriorityQueue<? extends E> c) {
    // getClass方法，返回运行时类型，如果是PriorityQueue，则直接使用
    // 否则使用initFromCollection
    if (c.getClass() == PriorityQueue.class) {
        this.queue = c.toArray();
        this.size = c.size();
    } else {
        initFromCollection(c);
    }
}

private void initFromCollection(Collection<? extends E> c) {
    initElementsFromCollection(c);
    // 对数组结构进行调整，是之符合优先队列的要求
    heapify();
}

private void initElementsFromCollection(Collection<? extends E> c) {
    Object[] a = c.toArray();
    // If c.toArray incorrectly doesn't return Object[], copy it.
    if (a.getClass() != Object[].class)
        a = Arrays.copyOf(a, a.length, Object[].class);
    int len = a.length;
    // 当数组长度为1，或者该队列具有comparator的情况下，该数组内不能存有null
    // 当长度为1，内容为null，则是空数组，不合理
    // 如果具有comparator，null.comparaTo方法更是不合理
    if (len == 1 || this.comparator != null)
        for (int i = 0; i < len; i++)
            if (a[i] == null)
                throw new NullPointerException();
    this.queue = a;
    this.size = a.length;
}

```

### 扩容

```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

private void grow(int minCapacity) {
    int oldCapacity = queue.length;
    // Double size if small; else grow by 50%
    int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                                     (oldCapacity + 2) :
                                     (oldCapacity >> 1));
    // 如果新容量超过最大允许值，则扩容至最少需要的容量
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    queue = Arrays.copyOf(queue, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
	if (minCapacity < 0) // overflow
		throw new OutOfMemoryError();
	return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
}
```

### 添加元素

```java
public boolean add(E e) {
    return offer(e);
}

public boolean offer(E e) {
    if (e == null)
        throw new NullPointerException();
    modCount++;
    int i = size;
    // 是否要扩容
    if (i >= queue.length)
        grow(i + 1);
    size = i + 1;
    if (i == 0)
        queue[0] = e;
    else
        siftUp(i, e);
    return true;
}
```

### 上浮操作

```java
private void siftUp(int k, E x) {
    if (comparator != null)
        siftUpUsingComparator(k, x);
    else
        siftUpComparable(k, x);
}

@SuppressWarnings("unchecked")
private void siftUpComparable(int k, E x) {
    Comparable<? super E> key = (Comparable<? super E>) x;
    while (k > 0) {
        int parent = (k - 1) >>> 1;
        Object e = queue[parent];
        if (key.compareTo((E) e) >= 0)
            break;
        queue[k] = e;
        k = parent;
    }
    queue[k] = key;
}

@SuppressWarnings("unchecked")
private void siftUpUsingComparator(int k, E x) {
    while (k > 0) {
        int parent = (k - 1) >>> 1;
        Object e = queue[parent];
        if (comparator.compare(x, (E) e) >= 0)
            break;
        queue[k] = e;
        k = parent;
    }
    queue[k] = x;
}
```

### 查看顶元素

```java
public E peek() {
    return (size == 0) ? null : (E) queue[0];
}
```

### 删除元素

```java
private int indexOf(Object o) {
    if (o != null) {
        for (int i = 0; i < size; i++)
            if (o.equals(queue[i]))
                return i;
    }
    return -1;
}
// 根据equeue方法删除对象
public boolean remove(Object o) {
    int i = indexOf(o);
    if (i == -1)
        return false;
    else {
        removeAt(i);
        return true;
    }
}
// 根据==删除对象
boolean removeEq(Object o) {
    for (int i = 0; i < size; i++) {
        if (o == queue[i]) {
            removeAt(i);
            return true;
        }
    }
    return false;
}
```

