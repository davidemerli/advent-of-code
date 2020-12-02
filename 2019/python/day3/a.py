import re
I = [245182, 790572]

def match(s):
    if not re.match(r'.*(\d)\1.*', s):
        return False

    for i, c in enumerate(s[:-1]):
        if int(c) > int(s[i + 1]):
            return False

    return True  

def match2(s):
    if not re.match(r'.*(\d)\1.*', s):
        return False

    all_ = [m.replace('.', '0') for m in re.findall(r'.*((\d)\1).*', s)]

    print(all_)

    if '00' not in all_:
        return False
    

    for i, c in enumerate(s[:-1]):
        if int(c) > int(s[i + 1]):
            return False

    return True   

print(len([p for p in range(*I) if match(str(p))]))
print(len([p for p in range(*I) if match2(str(p))]))

