# HashMap

## 简介

HashMap是基于哈希表的Map接口实现。它跟HashTable大致相同，除了它是不同步的，而且它能接受null，无论是key还是value。

对于HashMap的实例，有个两个参数可以影响它的性能，分别是initial capacity（初始容量）以及load factor（负载因子）。容量指的是哈希表中有多少个桶。负载因子定义为：![\alpha ](https://wikimedia.org/api/rest_v1/media/math/render/svg/b79333175c8b3f0840bfb4ec41b8072c83ea88d3) = 填入表中的元素个数 / 散列表的长度。当哈希表中的条目数超过加载因子和当前容量的乘积时，哈希表将被重新哈希（即，重建内部数据结构），以便哈希表具有大约两倍的桶数。

在看源码之前，需要有点位运算的知识。[可能是最通俗易懂的位运算讲解](https://blog.csdn.net/briblue/article/details/70296326)。

## Put函数的实现

1.HashMap底层使用`Node<K,V>[]`作为哈希表。

```java
transient Node<K,V>[] table;
static class Node<K,V> implements Map.Entry<K,V>
```

2.对key进行hash运算，key的hashCode低16位与高16位异或。

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

3.根据hash值计算出索引

```java
i = (n - 1) & hash // n是哈希表的容量
```

4.根据索引从哈希表中查找元素

-   如果元素不存在，则直接插入哈希表

    ```java
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    ```

-   如果元素存在

    -   判断key是否相同，相同则修改value。
    -   判断是否是红黑树（TreeNode），从红黑树找出节点，如果没有则创建，然后写入value。
    -   剩余就是链表了，遍历链表，如果不存在节点，则直接插入，然后判断是否需要转换成红黑树，如果存在则修改value。

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; 
    Node<K,V> p; 
    int n, i;
    // table为null或者内容为空
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
   /*
   	 i = (n - 1) & hash hash值与索引的关系
   */
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; 
        K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

## GET

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

```java
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

## REMOVE

```java
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}
```



## RESIZE

## 其他

[HashMap工作原理](https://yikun.github.io/2015/04/01/Java-HashMap%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E5%8F%8A%E5%AE%9E%E7%8E%B0/)

