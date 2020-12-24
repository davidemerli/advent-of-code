from tqdm import tqdm
import aocd

DAY, YEAR = 23, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR)
    return list(map(int, data))


class Node():
    def __init__(self, val):
        self.val = val
        self.next = None

    def __repr__(self) -> str:
        el = []
        curr = self
        while curr.val not in el:
            el.append(curr.val)
            curr = curr.next

        return ', '.join(map(str, el))

    def __str__(self) -> str:
        return ''.join(self.__repr__().split(', '))


def circular_linked_list(cups):
    nodes = dict()

    head = Node(cups.pop(0))
    nodes[head.val] = head

    cursor = head

    for cup in cups:
        cursor.next = Node(cup)
        nodes[cup] = cursor.next
        cursor = cursor.next

    cursor.next = head

    return head, nodes


def get_dest(curr, max_val):
    picked = [curr.next]
    for _ in range(2):
        picked.append(picked[-1].next)

    dest = curr.val - 1 if curr.val != 1 else max_val

    while dest in [c.val for c in picked]:
        dest = dest - 1 if dest != 1 else max_val

    return dest


def compute_turns(cups, nodes, turns, max_val):
    for _ in tqdm(range(turns), ncols=60):
        curr = cups
        dest = get_dest(curr, max_val)

        first_picked = curr.next
        last_picked = curr.next.next.next

        cursor = last_picked.next
        curr.next = cursor

        cursor = nodes[dest]

        temp = cursor.next
        cursor.next = first_picked
        last_picked.next = temp

        cups = curr.next

    return cups


def first():
    cups, nodes = circular_linked_list(data())

    cups = compute_turns(cups, nodes, turns=100, max_val=9)

    while cups.val != 1:
        cups = cups.next

    return str(cups)[1:]


def second():
    cups = data() + list(range(10, 10**6 + 1))
    cups, nodes = circular_linked_list(cups)

    cups = compute_turns(cups, nodes, turns=10**7, max_val=10**6)

    while cups.val != 1:
        cups = cups.next

    return cups.next.val * cups.next.next.val


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
