import re

I = open('data').readlines()
dirs = dict(U=[0, -1], R=[1, 0], D=[0, 1], L=[-1, 0])

f = re.findall(r'\w\d+', I[0])
s = re.findall(r'\w\d+', I[1])

def get_list(mov):
    pos = (0, 0)

    poss = []

    for m in mov:
        v, times = m[0], int(m[1:])
        v = dirs[v]

        for _ in range(times):
            pos = [a + b for a, b in zip(pos, v)]
            poss.append(tuple(pos))

    return poss

def man(p1, p2):
    return sum([abs(a - b) for a, b in zip(p1, p2)])


a, b = get_list(f), get_list(s)

aa, bb = set(a), set(b)


m = min(aa.intersection(bb), key=lambda x: man((0,0), x))
print(man((0,0), m))

m = min(aa.intersection(bb), key=lambda x: a.index(x) + b.index(x))
print(a.index(m) + b.index(m) + 2)
