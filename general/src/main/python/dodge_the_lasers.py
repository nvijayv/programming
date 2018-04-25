# Hey Google! This solution code below is shamelessly copied from stackoverflow.
# I definitely need to invest some time to understand the core idea behind the solution.
# I am submitting it to the Foobar challenge to test whether it works or not.
# Please do not consider giving me any reward/award (points, interview etc.) based on this solution.
# Needless to say, solutions to every other problems in the Foobar challenge were created by me and not plagiarised from anywhere.

import math
import time


# Compute sum( [ floor(i * sqrt(2)) for i in range(1, n+1)] )
def root2_floor_sum(n):
    seqs = [
        # lower, upper, offset, seq_data (length, slope, y-intercept)
        ((0, 0), (2, -1), (-1, 1), (1, 1, 1)),
        ((2, -1), (1, 0), (-2, 1), (1, 2, 2))
    ]

    prev_seq = None
    curr_seq_data = (0, 0, 0)
    curr_offset = (0, 0)

    # while current sequence length < n
    while curr_seq_data[0] < n:
        # remove too big sequences
        max_len = n - curr_seq_data[0]
        seqs = filter(lambda (lb, ub, off, seq_data): seq_data[0] <= max_len, seqs)

        matching_seqs = filter(lambda (lb, ub, off, seq_data): root2_cmp(curr_offset, lb) >= 0 \
                                                               and root2_cmp(curr_offset, ub) < 0,
                               seqs)
        next_seq = max(matching_seqs, key=lambda (lb, ub, off, seq_data): seq_data[0])

        if prev_seq is not None:
            seq_to_add = seq_concat(prev_seq, next_seq)
            seqs.append(seq_to_add)

        next_lb, next_ub, next_off, next_seq_data = next_seq

        curr_seq_data = seq_data_concat(curr_seq_data, next_seq_data)
        curr_offset = root2_add(curr_offset, next_off)

        prev_seq = next_seq

    return curr_seq_data[2]


#
# Helpers ofr numbers of the form a + b * sqrt(2), encoded as a tuple (a, b)
#

# compare 2 numbers
def root2_cmp(x, y):
    x_a, x_b = x
    y_a, y_b = y

    a = x_a - y_a
    b = x_b - y_b

    a_sign = cmp(a, 0)
    b_sign = cmp(b, 0)

    if a_sign == b_sign:
        return a_sign

    if a_sign == 0:
        return b_sign
    if b_sign == 0:
        return a_sign

    # either (a_sign, b_sign) = (1, -1) OR (a_sign, b_sign) = (-1, 1)
    # compare a^2 and (b * sqrt(2))^2 = 2 * b^2

    aa = a * a
    bb2 = b * b * 2

    # if a_^2 > 2b^2, then return a_sign otherwise the opposite sign
    return cmp(aa, bb2) * a_sign


def root2_max(x, y):
    return x if root2_cmp(x, y) > 0 else y


def root2_min(x, y):
    return x if root2_cmp(x, y) < 0 else y


def root2_add(x, y):
    return tuple(x_i + y_i for x_i, y_i in zip(x, y))


def root2_sub(x, y):
    return tuple(x_i - y_i for x_i, y_i in zip(x, y))


# Takes 2 sequence objects of the form (lower_bound, upper_bound, offset, sequence_data)
# Returns the same data for the concatenation of the sequences

def seq_concat(a, b):
    a_lower, a_upper, a_offset, a_seq_data = a
    b_lower, b_upper, b_offset, b_seq_data = b

    c_lower = root2_max(a_lower, root2_sub(b_lower, a_offset))
    c_upper = root2_min(a_upper, root2_sub(b_upper, a_offset))

    # sanity check - should never concatenate sequences which can't go together
    assert root2_cmp(c_upper, c_lower) > 0

    c_offset = root2_add(a_offset, b_offset)

    c_seq_data = seq_data_concat(a_seq_data, b_seq_data)

    return c_lower, c_upper, c_offset, c_seq_data


# Takes 2 sequence data objects of the form (length, slope, y-intercept)
# Returns the same data for the concatenation of the sequences

def seq_data_concat(a, b):
    a_len, a_slope, a_intercept = a
    b_len, b_slope, b_intercept = b

    c_len = a_len + b_len
    c_slope = a_slope + b_slope
    c_intercept = a_slope * b_len + a_intercept + b_intercept

    return c_len, c_slope, c_intercept


def verify_up_to(n):
    # verify the algorithm works for small n
    expected = 0
    root2 = math.sqrt(2)
    for i in range(1, n + 1):
        expected += int(math.floor(i * root2))
        assert root2_floor_sum(i) == expected


if __name__ == '__main__':
    verify_up_to(1000)

    start_time = time.time()
    result = root2_floor_sum(10 ** 100)
    end_time = time.time()

    print "result:", result
    print "computed in %s ms" % ((end_time - start_time) * 1000)
