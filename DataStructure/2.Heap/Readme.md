# 堆

堆（英语：heap)是计算机科学中一类特殊的数据结构的统称。堆通常是一个可以被看做一棵树的数组对象。

堆总是满足下列性质：

- 堆中某个节点的值总是不大于或不小于其父节点的值；
- 堆总是一棵完全二叉树；

将根节点最大的堆叫做最大堆或大根堆，根节点最小的堆叫做最小堆或小根堆。常见的堆有二叉堆、斐波那契堆等。

**父子节点对应索引的关系**

- 从索引为0开始存储数据
    - 父节点的索引 =（子节点索引-1）/ 2
    - 左节点的索引 =（父节点索引*2）+ 1
    - 右节点的索引 =（父节点索引*2）+ 2

![](https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=80d882349322720e6fc3eaa81aa26123/574e9258d109b3de15adc33ec7bf6c81800a4c51.jpg)

## 方法与调用关系

- add

    - addLast ---> 把数据添加到数组末端
    - siftUp ---> 把数据上浮到合适的位置 ---> 与父节点比较，如果比父节点大则交换位置

- extractMax

    - 删除并且返回堆顶部元素，然后把最后的数据放到堆顶，最后对堆顶元素进行下沉操作

    - findMax

    - swap

    - removeLast

    - siftDown

        ```java
        private void siftDown(int k){
        
            while(leftChild(k) < data.getSize()){
                int leftChildIndex = leftChild(k); // 在此轮循环中,data[k]和data[j]交换位置
                int rightChildIndex = leftIndex + 1;
                int maxChildIndex = rightChildIndex < data.getSize() && 
                    data.get(rightChildIndex).compareTo(data.get(leftChildIndex)>0) ? 
                    rightChildIndex : leftChildIndex;
        
                if(data.get(k).compareTo(data.get(maxChildIndex)) >= 0 )
                    break;
        
                data.swap(k, maxChildIndex);
                k = maxChildIndex;
            }
        }
        ```

## 应用

- 优先队列