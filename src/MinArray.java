import javax.lang.model.element.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Class which find the solution for the task second min
 * Build the heap(tournament tree) for n - 1 operation
 * Because all inner vertex match to one comparison and inner vertex = n - 1 (*)
 * For find the second min we should go down the tree:
 * We know that the number of vertex decreases by half at each iteration,
 * so we need (log2(n) round up - 1) comparisons
 * Sum = n - 1 + log2(n) round up - 1 = n - 2 + log2(n) round up
 * -----------------------------------------------------------------------------
 * (*) Докажем (*), по индукции. Для n=1 это верно.
 * Тогда докажем, что при добавлении нижнего слоя в такое дерево количество внутренних вершин останется N - 1
 * где N новое число вершин на нижнем слое.
 * n - число вершин на текущем слое.
 * n - 1 внутренних по предположению
 * По построению дерева у нас есть два варианта текущего слоя.
 * 1) n % 2 == 0, тогда мы можем добавить два сына ко всем вершинам тогда N = 2 * n, т.е N - 1 = n + n - 1
 * либо не добавлять к последнему тогда N = 2 * (n-1) + 1 = 2 * n - 1, т.е N - 1 = n - 1 + n - 1 = 2 * n - 2
 * 2)n % 2 == 1, тогда мы можем добавить два сына ко всем вершинам аналогично N = 2 * n, т.е N - 1 = n + n - 1
 * не добавлять к последнему два ребенка нельзя
 * Остальные варианты не возможны по построению.
 * @param <E> type
 */
public class MinArray<E extends Comparable<E>> {

    public class Element implements Comparable<Element> {
        E number;
        int index;
        public Element(E number, int index) {
            this.number = number;
            this.index = index;
        }

        public int compareTo(Element element) {
            int compareResult = this.number.compareTo(element.number);
            if (compareResult != 0) {
                return compareResult;
            } else {
                return Integer.compare(this.index, element.index);
            }
        }
    }

    private class Node {
        private Node left, right;
        Element element;
        public Node(Node left, Node right, Element element) {
            this.left = left;
            this.right = right;
            this.element = element;
        }
    }

    public static void main(String[] args) throws IOException {
        final List<Long> list = new ArrayList<>(List.of(1L, -3L, 5L, 9L, -2L, 0L));
        final MinArray<Long> minArray = new MinArray<>();
        System.out.println(minArray.findSecondMin(list));
    }

    public E findSecondMin(final List<E> list) {
        if (list.size() < 2) {
            throw new IllegalArgumentException("List size should be > 2");
        }
        Node root = buildHeap(buildLeaves(list));
        return findSecondMin(null, root);
    }

    private void createRootAndPutInList(Node left, Node right, List<Node> list) {
        if (left.element.compareTo(right.element) < 0) {
            list.add(new Node(left, right, left.element));
        } else {
            list.add(new Node(left, right, right.element));
        }
    }

    private List<Node> buildLeaves(final List<E> list) {
        List<Node> leafList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            Node current = new Node(null, null, new Element(list.get(i), i));
            Node next;
            if (i + 1 == list.size()) {
                leafList.add(current);
                continue;
            } else {
                next = new Node(null, null, new Element(list.get(i + 1), i + 1));
            }
            createRootAndPutInList(current, next, leafList);
        }
        return leafList;
    }

    private Node buildHeap(final List<Node> currentNodes) {
        if (currentNodes.size() == 1) {
            return currentNodes.get(0);
        } else {
            List<Node> nextStage = new ArrayList<>();
            for (int i = 0; i < currentNodes.size(); i += 2) {
                Node current = currentNodes.get(i);
                Node next;
                if (i + 1 == currentNodes.size()) {
                    nextStage.add(current);
                    continue;
                } else {
                    next = currentNodes.get(i + 1);
                }
                createRootAndPutInList(current, next, nextStage);
            }
            return buildHeap(nextStage);
        }
    }


    private E findSecondMin(E answer, final Node node) {
        if (node == null || node.left == null || node.right == null) {
            return answer;
        }
        if (node.left.element.index != node.element.index
                            && (answer == null || node.left.element.number.compareTo(answer) < 0)) {
            answer = node.left.element.number;
            return findSecondMin(answer, node.right);
        } else if (node.right.element.index != node.element.index
                            && (answer == null || node.right.element.number.compareTo(answer) < 0)) {
            answer = node.right.element.number;
            return findSecondMin(answer, node.left);
        } else if (node.left.element.index != node.element.index) {
            return findSecondMin(answer, node.right);
        } else if (node.right.element.index != node.element.index) {
            return findSecondMin(answer, node.left);
        }
        return answer;
    }


}
