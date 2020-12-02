IN = [int(v) for v in open('data').read().split(',')]
IN.extend([0] * 10000000)
pc = 0

def reset():
    global IN, pc
    IN = [int(v) for v in open('data').read().split(',')]
    pc = 0

def run_next():
    global pc
 
    op, a1, a2, a3 = IN[pc:pc+4]

    if op == 1:
        IN[a3] = IN[a1] + IN[a2]
    elif op == 2:
        IN[a3] = IN[a1] * IN[a2]

    pc += 4

    return op

IN[1], IN[2] = 12, 2

search = 19690720

def compute():
    global pc, search, IN

    for i in range(100):
        for j in range(100):
            reset()            

            IN[1], IN[2] = i, j

            while True:
                ret = run_next()

                if ret == 99:
                    break

            if IN[0] == search:
                return 100 * i + j

print(compute())    