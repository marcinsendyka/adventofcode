package com.msendyka.adventofcode.p6;

import com.msendyka.adventofcode.Functions;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Tree;

public class P6 {

    public static void main(String[] args) {
        partOne();

        List<String> strings = Functions.readInput("p6/test.txt");
        List<Tuple2> tuples = strings.map(s -> s.split("\\)"))
                .map(arr -> new Tuple2(arr[0], arr[1]));
//        Tuple2 root = tuples.find(a -> a._2.equals("YOU")).get();
//        tuples = tuples.push(new Tuple2( "YOU", null ));
        tuples = tuples.append(new Tuple2(null, "COM"));

        List<Tree.Node<Tuple2>> treeList = Tree.build(tuples, tuple2 -> tuple2._2, tuple2 -> tuple2._1);

        System.out.println(treeList);
        Tree.Node<Tuple2> tree = treeList.head();
        List<Tree.Node<Tuple2>> nodes = tree.traverse(Tree.Order.IN_ORDER)
                .filter(node -> findBoth(node)).toList();

        nodes.forEach(a -> System.out.println(a.size()));
//        System.out.println(nodes.map(list -> list.size()));
    }

    private static boolean findBoth(Tree.Node<Tuple2> node) {
        return node.find(a -> a._2.equals("YOU")).isDefined() && node.find(a -> a._2.equals("SAN")).isDefined();
    }

    private static void partOne() {
        List<String> strings = Functions.readInput("p6/input.txt");
        List<Tuple2> tuples = strings.map(s -> s.split("\\)"))
                .map(arr -> new Tuple2(arr[0], arr[1]));


        tuples = tuples.append(new Tuple2(null, "COM"));
        List<Tree.Node<Tuple2>> treeList = Tree.build(tuples, tuple2 -> tuple2._2, tuple2 -> tuple2._1);

        Tree.Node<Tuple2> tree = treeList.head();
        Number children = tree
                .traverse()
                .map(a -> a.get()._1 == null ? 0 : a.size()).sum();
        System.out.println(children);
    }
}
