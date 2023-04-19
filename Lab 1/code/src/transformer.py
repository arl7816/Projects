"""
CSAPX Lab 1: Secret Messages

A program that encodes/decodes a message by applying a set of transformation operations.
The transformation operations are:
    shift - Sa[,n] changes letter at index a by moving it n letters fwd in the alphabet. A negative
        value for n shifts the letter backward in the alphabet.
    rotate - R[n] rotates the string n positions to the right. A negative value for n rotates the string
        to the left.
    duplicate - Da[,n] follows character at index a with n copies of itself.

All indices numbers (the subscript parameters) are 0-based.

author: Alex Lee
"""
def shift_letter(letter: str, k:int) -> str:
    upper = letter.isupper()
    letter = letter.upper()

    start = ord("A")
    end = ord("Z")

    uniC = ord(letter)
    if k > 26:
        k %= 26
    uniC += k
    if uniC > end:
        uniC = uniC - end + start - 1
    letter = chr(uniC)
    if upper == False:
        letter = letter.lower()

    return letter


def shift(message: str, i: int, k: int) -> str:
    """
    takes a message and shifts one letter forward or backward in the alphabet
    :param message: the message
    :param i: the index of the letter
    :param k: the amount the letter must shift
    :return: the new message
    """
    if k is None:
        k = 1

    return message[:i] + shift_letter(message[i], k) + message[i+1:]


def duplicate(message: str, i:int, k:int) -> str:
    pass


def trade(message: str, i:int, k:int) -> str:
    pass


def rotate(message: str, i:int, k:int) -> str:
    pass


def transform(message: str, transformations: str, operation: str) -> str:
    """
    Is in charge of decrypting/encrypting messages. (however just for now does the printing)
    :param message: the message the user sends in
    :param transformations: the transformations the user wants applied
    :param operation: the end goal such as decrypting
    :return: returns nothing for now
    """

    transforms = transformations.split(";")

    for transform in transforms:
        i = None
        k = None

        transform = transform.strip()

        # gets the i and k values
        if transform.find(",") != -1:
            i = transform[1:transform.index(",")]
            k = transform[transform.index(",") + 1:]
        else:
            i = transform[1:]

        if i is not None:
            i = int(i)
        if k is not None:
            k = int(k)

        transform_message = "Operation "

        if transform[0] == "S":
            transform_message += " Shift (S) "
            message = shift(message, i, k)
        elif transform[0] == "D":
            transform_message += " Duplicate (D) "
        elif transform[0] == "T":
            transform_message += " Trade (T) "
        elif transform[0] == "R":
            transform_message += " Rotate (R) "

        transform_message += " - Parameters: "
        if i != None:
            transform_message += str(i)
        if k != None:
            transform_message += "," + str(k)

        print(transform_message)
    return message

def main() -> None:
    """
    The main loop responsible for getting the input details from the user
    and printing in the standard output the results
    of encrypting or decrypting the message applying the transformations
    :return: None
    """

    print("Welcome to the secret message")

    # keeps track of what the users end goal is
    goal = ""
    while goal != "Q": # will loop until the user enters "Q"

        goal = input("What do you want to do: (E)ncrypt, (D)ecrypt, or (Q)uit? ")
        if goal != "E" and goal != "D":
            continue

        message = input("Enter the message: ")
        transforms = input("Enter the encrypting transformation operations: ")
        print("Generating output ... ")
        print("Transforming message:", message)
        print("Applying...")

        # activates the transform function
        new_message = transform(message, transforms, goal)

        print("Your new message is " + new_message)



    # loop is complete
    print("Goodbye!")


if __name__ == '__main__':
    main()
