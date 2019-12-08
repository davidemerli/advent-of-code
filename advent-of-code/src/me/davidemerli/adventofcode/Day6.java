package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.*;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day6 {

    private static List<TreeNode> nodeList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day6.txt"));

        TreeNode root = new TreeNode("COM");
        root.parent = null;

        fillNode(inputLines, root);
        fillNodes(nodeList, root);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        System.out.println("result1: " + nodeList.stream().map(Day6::value).reduce((sum, el) -> sum += el).get());
    }

    private static void secondPart() {
        System.out.println("result2: " + BFS(getNode(nodeList, "YOU"), getNode(nodeList, "SAN"), nodeList));
    }

    private static TreeNode getNode(List<TreeNode> nodes, String name) {
        return nodes.stream().filter(n -> n.name.equals(name)).findFirst().orElse(null);
    }

    private static int value(TreeNode node) {
        int v = 0;
        for (; node.parent != null; node = node.parent, v++) ;
        return v;
    }

    private static void fillNode(List<String> inputLines, TreeNode cursor) {
        inputLines.stream()
                .map(s -> s.split("\\)"))
                .filter(s -> s[0].equals(cursor.name))
                .forEach(s -> {
                    TreeNode child = new TreeNode(s[1]);
                    cursor.children.add(child);
                    child.parent = cursor;
                    fillNode(inputLines, child);
                });
    }

    private static void fillNodes(List<TreeNode> nodes, TreeNode root) {
        root.children.stream().filter(c -> !nodes.contains(c)).forEach(c -> {
            nodes.add(c);
            fillNodes(nodes, c);
        });
    }

    private static List<TreeNode> getJumps(TreeNode from) {
        List<TreeNode> jumps = new ArrayList<>(from.children);
        if (from.parent != null) jumps.add(from.parent);

        return jumps;
    }

    private static int BFS(TreeNode from, TreeNode to, List<TreeNode> list) {
        int from_index = list.indexOf(from);
        int to_index = list.indexOf(to);

        boolean[] visited = new boolean[list.size()];
        int[] distance = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            visited[i] = false;
            distance[i] = 0;
        }

        Queue<Integer> queue = new LinkedList<>();
        distance[from_index] = 0;

        queue.add(from_index);
        visited[from_index] = true;

        while (!queue.isEmpty()) {
            int x = queue.peek();
            queue.poll();

            List<TreeNode> jumps = getJumps(list.get(x));

            for (TreeNode jump : jumps) {
                if (!list.contains(jump)) continue;

                if (visited[list.indexOf(jump)]) continue;

                distance[list.indexOf(jump)] = distance[x] + 1;
                queue.add(list.indexOf(jump));

                visited[list.indexOf(jump)] = true;
            }
        }

        return distance[to_index] - 2;
    }

    private static class TreeNode {
        List<TreeNode> children = new ArrayList<>();
        TreeNode parent;

        String name;

        TreeNode(String name) {
            this.name = name;
        }
    }
}
