package com.msendyka.adventofcode.p6;

import com.msendyka.adventofcode.Functions;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Tree;

public class P6 {

    public static void main(String[] args) {
        partOne();
        List<String> strings = Functions.readInput("p6/input.txt");
        List<Tuple2> tuples = strings.map(s -> s.split("\\)"))
                .map(arr -> new Tuple2(arr[0], arr[1]));
        tuples = tuples.append(new Tuple2(null, "COM"));

        List<Tree.Node<Tuple2>> treeList = Tree.build(tuples, tuple2 -> tuple2._2, tuple2 -> tuple2._1);

        Tree.Node<Tuple2> tree = treeList.head();
        List<Tree.Node<Tuple2>> nodes = tree.traverse(Tree.Order.IN_ORDER)
                .filter(node -> findBoth(node)).toList();

        Tree.Node<Tuple2> x = nodes.minBy(a -> a.size()).get();

        int you = find(x, "YOU");
        int san = find(x, "SAN");
        System.out.println(you + san);
    }

    private static int find(Tree.Node<Tuple2> a, String nodeVal) {
        if (a == null) {
            return -1;
        }
        int dist = -1;
        if (a.get().equals(nodeVal)) {
            return dist + 1;
        }
        for (Tree.Node<Tuple2> tuple2s : a.getChildren()) {
            if (tuple2s.traverse().find(node -> node.getValue()._2().equals(nodeVal)).isDefined()) {
                return find(tuple2s, nodeVal) + 1;

            }
        }
        return dist;
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
