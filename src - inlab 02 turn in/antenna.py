"""
Lab: lab-02 Fractal Antenna
Name: Alex Lee
Purpose: Develops a graphical application that draws a fractal antenna in a turtle window using
recursive techniques
"""

import turtle
from math import sqrt


def draw_side_original(n: float, level: int, trtl: turtle.Turtle) -> float:
    """
    Will draw a quarter of the parameter of a fractal antenna using recursion
    :param n: the length of one side
    :param level: the level of the antenna
    :param trtl: the turtle object
    :return: the total length of the lines drawn
    """
    dist = 0
    if level == 1:
        trtl.forward(n)
        return n
    else:
        n /= 3
        dist += draw_side_original(n, level-1, trtl)
        trtl.left(90)
        dist += draw_side_original(n, level-1, trtl)
        trtl.right(90)
        dist += draw_side_original(n, level - 1, trtl)
        trtl.right(90)
        dist += draw_side_original(n, level - 1, trtl)
        trtl.left(90)
        dist += draw_side_original(n, level - 1, trtl)
        return dist


def draw_side_strat1(length: float, level: int, trtl: turtle.Turtle) -> float:
    """
    Will draw a fractal antenna by drawing its parameter using recursion
    :param length: the total length of the parameter (in pixels)
    :param level: the level of the antenna
    :param trtl: the turtle object
    :return: the total length of the lines drawn
    """

    # since the draw_side_orginal already draws 1/4 of the parameter, the method gets called 4 times with adjustments
    dist = 0
    dist += draw_side_original(length, level, trtl)
    trtl.left(90)
    dist += draw_side_original(length, level, trtl)
    trtl.left(90)
    dist += draw_side_original(length, level, trtl)
    trtl.left(90)
    dist += draw_side_original(length, level, trtl)

    return dist


def draw_side_strat2(length: float, level: int, trtl: turtle.Turtle) -> float:
    """
    Draws a fractal antenna by drawing each square separately using recursion
    :param length: the total length of the parameter (in pixels)
    :param level: the level of the antenna
    :param trtl: the turtle object
    :return: the total length of the lines drawn
    """
    if level == 1:
        # at its base case, just draw a square
        trtl.forward(length)
        trtl.right(90)

        trtl.forward(length)
        trtl.right(90)

        trtl.forward(length)
        trtl.right(90)

        trtl.forward(length)
        trtl.right(90)

        trtl.forward(length)
        trtl.right(90)

        trtl.forward(length)

        return length * 4
    else:
        # draws the shape of level 2 and above recursively
        dist = 0

        length /= 3
        dist += draw_side_strat2(length, level-1, trtl)

        trtl.left(90)
        dist += draw_side_strat2(length, level-1, trtl)

        trtl.penup()
        trtl.left(180)
        trtl.forward(length)
        trtl.pendown()
        dist += draw_side_strat2(length, level-1, trtl)

        trtl.penup()
        trtl.right(45+90)
        trtl.forward(length*sqrt(2)*2)
        trtl.left(45)
        trtl.pendown()
        dist += draw_side_strat2(length, level-1, trtl)

        trtl.penup()
        trtl.right(90+45)
        trtl.forward(length*sqrt(2))
        trtl.right(45)
        trtl.forward(length)
        trtl.pendown()
        dist += draw_side_strat2(length, level-1, trtl)

        return dist


def check_data_type(checking: str, data_type: str) -> bool:
    """
    checks whether or not a piece of data matches the desired data type
    :param checking: the piece of data being checked for a valid type
    :param data_type: the desired data type (floats are treated like ints, but ints are not treated like floats)
                      valid arguments include ("string", "int", "float", "None", "boolean")
    :return: whether or not the data inputted matches the desired data type
    """

    # keeps track of the data type of checking to compare with desired result at end
    type_given = ""

    # gets the data type that is checking
    if checking == "None":
        type_given = "None"
    elif checking == "True" or checking == "False":
        type_given = "boolean"

    try:
        # if no error is raised, than checking is a number
        float_value = float(checking)

        # ex. 5.0 == 5 python will mark this as true
        if float_value == int(float_value):
            type_given = "int"

            # if the desired data type is float than ints can act as the same
            if data_type == "float":
                type_given = "float"
        else:
            type_given = "float"

    except ValueError:
        # if everything else fails, the type must must a string
        type_given = "string"

    # if the data type doesnt match, the program prints the problem and the returns False
    if type_given is not data_type:
        print("Value must be a " + data_type + ". You entered '" + type_given + "-value'")
        return False

    return True


def reset_canvas(trtl: turtle.Turtle) -> None:
    """
    Resets the canvas, turtle position, and hides the turtle
    :param trtl: the turtle object
    :return: None
    """

    trtl.reset()
    trtl.left(45)
    trtl.hideturtle()
    trtl.speed(0)


def get_parameter(question: str, data_type: str) -> str:
    """
    Will keep asking for the users input until they give something that is the desired data type
    :param question: the question the user is asked in the input
    :param data_type: the desired type of data
    :return: the users input
    """

    parameter = input(question)
    while not check_data_type(parameter, data_type):
        parameter = input(question)

    return parameter


def main():
    """
    Is used to control the main loop of input and fractal antenna making
    :return: None
    """

    # needs to be an float or int
    length = get_parameter("Length of initial side: ", "float")
    length = float(length)

    # needs to be an int
    level = get_parameter("Number of levels: ", "int")
    level = int(level)

    trtl = turtle.Turtle()

    reset_canvas(trtl)

    strat1_distance = draw_side_strat1(length, level, trtl)
    print("Strategy 1 - Antenna's length is", strat1_distance, "units")

    input("Hit enter to continue...")
    reset_canvas(trtl)

    strat2_distance = draw_side_strat2(length, level, trtl)
    print("Strategy 2 - Antenna's length is", strat2_distance, "units")

    # used to keep the application alive after its drawn
    input()


if __name__ == "__main__":
    main()