from hypothesis import given, example
from hypothesis.strategies import text


def encode(input_string):
    count = 0
    prev = ''
    lst = []
    for character in input_string:
        if character != prev:
            if prev:
                entry = (prev, count)
                lst.append(entry)
            # count = 0
            prev = character
        count += 1
    else:
        if prev:
            entry = (prev, count)
            lst.append(entry)
    return lst


def decode(lst):
    q = ''
    for character, count in lst:
        q += character * count
    return q


@given(text())
@example('')
@example('aa')
@example('aba')
@example('aabb')
def test_decode_inverts_encode(s):
    assert decode(encode(s)) == s


# if __name__ == '__main__':
#     test_decode_inverts_encode()