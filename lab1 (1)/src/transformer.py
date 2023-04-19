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


def shift_letter(letter: str, k:int) -> str: # when a letter goes backwards it doesnt wrap around correctly
    """
    shifts a singular letter forward or backward in the alphabet
    :param letter: the letter being changed
    :param k: the amount the letter must move (default=1)
    :return: the new letter
    """
    upper = letter.isupper()
    letter = letter.upper()

    start = ord("A")
    end = ord("Z")

    uniC = ord(letter)

    if k > 26 or k < 0:
        k %= 26
    uniC += k
    if uniC > end:
        uniC = uniC - end + start - 1
    letter = chr(uniC)
    if upper is False:
        letter = letter.lower()

    return letter


def shift(message: str, i: int, k: int) -> str:
    """
    takes a message and shifts one letter forward or backward in the alphabet
    :param message: the message
    :param i: the index of the letter
    :param k: the amount the letter must shift (default=1)
    :return: the new message
    """

    return message[:i] + shift_letter(message[i], k) + message[i+1:]


def duplicate(message: str, i: int, k: int) -> str:
    """
    Duplicates a letter in a message
    :param message: the message given
    :param i: the index of the letter to be duplicated
    :param k: the amount of times the letter should be duplicated (default=1)
    :return: the encrypted
    """

    if k is None:
        k = 1

    new_message = message[:i+1]

    duplicates = message[i] * k

    new_message += duplicates

    new_message += message[i+1:]

    return new_message


def undo_duplicate(message: str, i: int, k:int) -> str:
    """
    Undo's a duplication encryption
    :param message: the message
    :param i: the starting index of the duplicated letter
    :param k: the number of times the letter was duplicated
    :return: the decrypted message
    """
    if k is None:
        k = 1

    new_message = message[:i+1]
    new_message += message[i+1+k:]

    return new_message


def trade(message: str, i: int, k: int) -> str:
    """
    swaps two letters in a given message
    :param message: the message given
    :param i: the first letter
    :param k: the second letter (can assume k > i)
    :return: the new message
    """

    new_message = message[:i]
    new_message += message[k]
    new_message += message[i+1:k]
    new_message += message[i]
    new_message += message[k+1:]
    return new_message


def rotate(message: str, exp: int) -> str:
    """
    moves every letter in a word forward. Letters will wrap around
    :param message: the message given
    :param exp: the amount each letter moves (default=1)
    :return: the new message
    """
    if exp is None:
        exp = 1

    count = exp % len(message)

    if count == 0:
        count = exp

    new_message = message[-count:]
    new_message += message[0:len(message)-count]

    return new_message


def perform_instruction(operation: str, instruction: str, transform_message: str, message: str, i: int, k: int):
    """
    decides what type of operation should be performed based on user input
    :param operation: whether to encrypt or decrypt
    :param instruction: what type of encryption/decryption to use
    :param transform_message: the message informing the user of the action that takes place
    :param message: the message
    :param i: the first variable given
    :param k: the second variable given (may be given as None)
    :return: the message notifying the user of what action was performed and the new message
    """

    # goes though the possible actions the user could want
    if instruction[0] == "S":
        transform_message += " Shift (S) "

        if k is None:
            k = 1
        if operation == "E":
            message = shift(message, i, k)
        else:
            # have to check if k is None before this since k has some math applied to it
            message = shift(message, i, -k)

    elif instruction[0] == "D":
        transform_message += " Duplicate (D) "

        if operation == "E":
            message = duplicate(message, i, k)
        else:
            message = undo_duplicate(message, i, k)

    elif instruction[0] == "T":
        transform_message += " Trade (T) "
        message = trade(message, i, k)

    elif instruction[0] == "R":
        transform_message += " Rotate (R) "

        if i is None:
            i = 1
        if operation == "E":
            message = rotate(message, i)
        else:
            message = rotate(message, -i)

    return transform_message, message


def encrypt_decrypt(message: str, instruction: str, operation: str) -> str:
    """
    Gets the information in a instruction into usable variables
    :param message: the message
    :param instruction: the action the user wants to take place (such as "S1,3")
    :param operation: whether to encrypt or decrypt the message
    :return: the new message
    """

    i = None
    k = None

    instruction = instruction.strip()

    # gets the i and k values
    if instruction.find(",") != -1:
        i = instruction[1:instruction.index(",")]
        k = instruction[instruction.index(",") + 1:]
    else:
        i = instruction[1:]

    if i == "":
        i = None

    if i is not None:
        i = int(i)
    if k is not None:
        k = int(k)

    # encrypts the message and gets the transform message thats going to be printed to the console
    transform_message = "Operation "

    transform_message, message = perform_instruction(operation, instruction, transform_message, message, i, k)

    transform_message += " - Parameters: "
    if i is not None:
        transform_message += str(i)
    if k is not None:
        transform_message += "," + str(k)

    print(transform_message)

    return message


def transform(message: str, transformations: str, operation: str) -> str:
    """
    Separates the instructions and performs each action
    :param message: the message the user sends in
    :param transformations: the transformations the user wants applied
    :param operation: the end goal such as decrypting
    :return: returns nothing for now
    """

    transforms = transformations.split(";")

    if operation == "E":
        for instruction in transforms:
            message = encrypt_decrypt(message, instruction, operation)
    else:
        # if the user wants to decrypt, do the instructions backwards
        for instruction in transforms[::-1]:
            message = encrypt_decrypt(message, instruction, operation)

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
